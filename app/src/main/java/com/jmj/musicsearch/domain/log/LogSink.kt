package com.jmj.musicsearch.domain.log

import com.jmj.musicsearch.domain.log.LogLevel

/**
 * LogOutput - An endpoint for printing calls to [GlobalLogger] methods.
 */
interface LogSink {
  /**
   * The [LogLevel] for this sink.
   */
  var logLevel: LogLevel

  /**
   * Print a message to the output.
   *
   * @param level The [LogLevel] of the message.
   * @param tag   The log tag.
   * @param msg   The message to print.
   */
  fun println(level: LogLevel, tag: String, msg: String, throwable: Throwable? = null)


}
