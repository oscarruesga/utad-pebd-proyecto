package com.moolileo.meetup.streaming.processes

import java.sql.Timestamp
import java.text.{ParsePosition, SimpleDateFormat}
import java.util.{Calendar, Date, Locale}

import com.moolileo.meetup.model._
import com.moolileo.meetup.websocket.WebSocketReader
import org.apache.spark.Logging
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.{Seconds, Minutes, StreamingContext}
import org.elasticsearch.spark._



class EsPersistStreamRSVP extends Serializable with Logging {

  def start(ssc: StreamingContext, websocket: String, EsNode: String, EsPort:String) {
    val stream = ssc.receiverStream[MeetupRsvp](new WebSocketReader(websocket, StorageLevel.MEMORY_ONLY_SER))

    stream.checkpoint(Seconds(60))
    //stream.repartition(2)

    //Bulk store of stream in elasticsearch
    stream.foreachRDD(rdd => {
      rdd.cache
      rdd.saveToEs("meetup-rsvps/meetup-rsvps",
        Map(  "es.mapping.timestamp" -> "mtime",
          "es.mapping.id" -> "rsvp_id",
          "es.nodes" -> EsNode,
          "es.port" -> EsPort))
      rdd.unpersist()
    })

    // Filter Accepted RSVP
    val rsvpAccepted = stream.filter(_.response == "yes")

    // Number attendees by Country by Period of Time


    val rsvpByCountryByDate = rsvpAccepted
      .map(
        rsvp => ((rsvp.group.group_country, rsvp.group.group_lat + "," + rsvp.group.group_lon, rsvp.mtime), rsvp.guests + 1)
      )
      .reduceByKey(_ + _)
      .map { case ((country, coords, date), attendees) =>
        CountryAttendeesByDate(
          date + "-" + country,
          country,
          coords,
          attendees,
          date,
          {
            val date2calc = Calendar.getInstance
            date2calc.setTimeInMillis(date)

            val cal = Calendar.getInstance()
            cal.set( Calendar.HOUR, 0)
            cal.set( Calendar.MINUTE, 0)
            cal.set( Calendar.SECOND, 0)
            cal.set( Calendar.MILLISECOND, 0)
            cal.set( Calendar.DATE, 1 )
            cal.set( Calendar.MONTH, date2calc.get(Calendar.MONTH) )
            cal.set( Calendar.YEAR, date2calc.get(Calendar.YEAR) )
            cal.getTimeInMillis
          },
          {
            val date2calc = Calendar.getInstance
            date2calc.setTimeInMillis(date)

            val cal = Calendar.getInstance()
            cal.set( Calendar.HOUR, 0)
            cal.set( Calendar.MINUTE, 0)
            cal.set( Calendar.SECOND, 0)
            cal.set( Calendar.MILLISECOND, 0)
            cal.set( Calendar.DATE, 1 )
            cal.set( Calendar.MONTH, 0 )
            cal.set( Calendar.YEAR, date2calc.get(Calendar.YEAR) )
            cal.getTimeInMillis
          }
        )
      }




    rsvpByCountryByDate.foreachRDD(rdd => {
      rdd.cache
      rdd.saveToEs("meetup_attending_by_date/meetup_attending_by_date",
        Map("es.write.operation" -> "upsert",
          "es.mapping.id" -> "key",
          "es.update.script.params" -> "inc:attendees",
          "es.update.script" -> "ctx._source.attendees+=inc",
          "es.nodes" -> EsNode,
          "es.port" -> EsPort
        )
      )
      rdd.unpersist()
    })

    rsvpByCountryByDate.foreachRDD( rdd => rdd.foreach( x => println(x.key + " : " + x.attendees + " : " + x.coordinates + " : " + x.day + " : " + x.month + " : " + x.year) ))


        val dateformat = new SimpleDateFormat("yyyyMMdd")

        // Global Trending Topics
        val globalTrendingTopics = stream
          .flatMap(rsvp => rsvp.group.group_topics)
          .map(topic => (topic.topic_name, 1))
          .reduceByKeyAndWindow((curr: Int, acc: Int) => curr + acc, Minutes(5), Seconds(10))
          .filter(t => t._2 > 10) // min threshold = 5
          .transform((rdd, time) => rdd.map {
          case (topic: String, count) => GlobalTrendingTopics(topic, count, time.milliseconds)
        })

        //globalTrendingTopics.foreachRDD( rdd => rdd.foreach( x => println(x.topic + " : " + x.counter + " : " + x.date) ))

        globalTrendingTopics.foreachRDD(rdd => {
          rdd.cache
          rdd.saveToEs("meetup_global_trending_topics/meetup_global_trending_topics",
            Map("es.write.operation" -> "upsert",
              "es.mapping.id" -> "topic",
              "es.mapping.timestamp" -> "date",
              "es.update.script.params" -> "inc:counter",
              "es.update.script" -> "ctx._source.counter+=inc",
              "es.nodes" -> EsNode,
              "es.port" -> EsPort
            ))
          rdd.unpersist()
        })

        def mapdata(group: MeetupGroup) = {
          group.group_topics.map(x => (x, group.group_country, group.group_lat + "," + group.group_lon))
        }

        // Country Trending Topics
        val countryTrendingTopics = stream
          .flatMap(rsvp => mapdata(rsvp.group))
          .map(x => ((x._1.topic_name, x._2, x._3), 1))
          .reduceByKeyAndWindow((curr: Int, acc: Int) => curr + acc, Minutes(10), Seconds(10))
          .filter(t => t._2 > 5) // min threshold = 5
          .transform((rdd, time) => rdd.map {
          case ((topic: String, country: String, coord: String), count) => CountryTrendingTopics(topic + "-" + country, country, coord, topic, count, time.milliseconds)
        })

        //countryTrendingTopics.foreachRDD( rdd => rdd.foreach( x => println(x.key + " : " + x.counter + " : " + x.coordinates + " : " + x.date) ))

        countryTrendingTopics.foreachRDD(rdd => {
          rdd.cache
          rdd.saveToEs("meetup_country_trending_topics/meetup_country_trending_topics",
            Map("es.write.operation" -> "upsert",
              "es.mapping.id" -> "key",
              "es.mapping.timestamp" -> "date",
              "es.update.script.params" -> "inc:counter",
              "es.update.script" -> "ctx._source.counter+=inc",
              "es.nodes" -> EsNode,
              "es.port" -> EsPort
            ))
            rdd.unpersist()
            })


    ssc.start()
    ssc.awaitTermination()

  }
}