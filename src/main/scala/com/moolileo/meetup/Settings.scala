package com.moolileo.meetup


import _root_.com.typesafe.config.ConfigFactory
import akka.japi.Util._


final class Settings {

  protected val config = ConfigFactory.load.getConfig("meetup-stream-reader")

  val MeetupRSVPWebSocketUrl = config.getString("meetup.ws.url")

  var SparkMaster: String = config.getString("spark.master")

  var StreamingBatchInterval = config.getInt("spark.streaming.batch.interval")

  var SparkExecutorMemory = config.getBytes("spark.executor.memory")

  var SparkCoresMax = config.getInt("spark.cores.max")

  var DeployJars: Seq[String] = immutableSeq(
    config.getStringList("spark.jars")).filter(new java.io.File(_).exists)

  var ESNode = config.getString("es.node")
  var ESPort = config.getString("es.port")

}