package com.moolileo.meetup.websocket

import com.moolileo.meetup.model.MeetupRsvp
import org.apache.spark.Logging
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.receiver.Receiver
import org.json4s._
import org.json4s.jackson.JsonMethods._

import scalawebsocket.WebSocket

class WebSocketReader(url: String, storageLevel: StorageLevel)
  extends Receiver[MeetupRsvp](storageLevel) with Logging
{
  @volatile private var webSocket: WebSocket = _

  def onStart() {
    try{
      logInfo("Connecting to WebSocket: " + url)
      val newWebSocket = WebSocket().open(url).onTextMessage({ msg: String => parseJson(msg) })
      setWebSocket(newWebSocket)
      logInfo("Connected to: WebSocket" + url)
    } catch {
      case e: Exception => restart("Error starting WebSocket stream", e)
    }
  }

  def onStop() {
    setWebSocket(null)
    logInfo("WebSocket receiver stopped")
  }

  private def setWebSocket(newWebSocket: WebSocket) = synchronized {
    if (webSocket != null) {
      webSocket.shutdown()
    }
    webSocket = newWebSocket
  }

  private def parseJson(jsonStr: String): Unit =
  {
    implicit lazy val formats = DefaultFormats

    try {
      var json = parse(jsonStr)

      val rsvp = json.extract[MeetupRsvp]

      store(rsvp)
    } catch {
      case e: MappingException => logError("Unable to map JSON message to MeetupRsvp object:" + e.msg)
      case e: Exception => logError("Unable to map JSON message to MeetupRsvp object")
    }

  }
}


