package com.jmj.musicsearch.domain.data.www

import java.text.DateFormat
import java.text.ParseException
import java.util.*

fun DateFormat.tryParse(str: String): Date? {
  return try {
    parse(str)
  } catch (e: ParseException) {
    null
  }
}