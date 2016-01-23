package com.moolileo.meetup.streaming

import com.moolileo.meetup.Settings
import com.moolileo.meetup.streaming.processes.EsPersistStreamRSVP
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}


object MeetupStreaming {

  val settings = new Settings
  import settings._



  def main(args: Array[String]): Unit = {
    val Array(master, esNode) = args.take(2)

    if (master != null && !master.isEmpty)  SparkMaster = master
    if (esNode != null && !esNode.isEmpty) ESNode = esNode

    val conf = new SparkConf(true)
      .setMaster(SparkMaster)
      .setAppName(getClass.getSimpleName)
      .setJars(DeployJars)
      .set("spark.executor.memory", SparkExecutorMemory.toString)
      .set("spark.cores.max", SparkCoresMax.toString)

    val sc = new SparkContext(conf)
    val ssc = new StreamingContext(sc, Seconds(StreamingBatchInterval))
    ssc.checkpoint("/tmp/data/meetup.dat")
    val stream = new EsPersistStreamRSVP
    stream.start(ssc, MeetupRSVPWebSocketUrl, ESNode, ESPort)
  }
}
