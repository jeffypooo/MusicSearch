package com.jmj.musicsearch.log

import com.jmj.musicsearch.domain.log.LogLevel
import com.jmj.musicsearch.domain.log.LogSink

class StdoutSink: LogSink {

  override var logLevel: LogLevel = LogLevel.VERBOSE

  override fun println(level: LogLevel, tag: String, msg: String, throwable: Throwable?) {
    if (logLevel > level) return
    val levelChar =  when (level) {
      LogLevel.VERBOSE -> "V"
      LogLevel.DEBUG   -> "D"
      LogLevel.INFO    -> "I"
      LogLevel.WARN    -> "W"
      LogLevel.ERROR   -> "E"
    }
    val time = System.currentTimeMillis()
    val threadName = Thread.currentThread().name
    println("$time - $levelChar/[$threadName] $tag: $msg")
    throwable?.printStackTrace()
  }
}