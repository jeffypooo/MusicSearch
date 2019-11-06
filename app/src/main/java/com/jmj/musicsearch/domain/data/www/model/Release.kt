package com.jmj.musicsearch.domain.data.www.model

import java.util.*

data class Release(
  val id: String,
  val title: String,
  val date: Date
)

//val Release.year: Int
//  get() = date.substring(0..3).toInt()
//
//val Release.month: Int
//get() {
//  val split = date.split("-")
//  return if (split.size < 2) 1 else split[1].toInt()
//}
//
//val Release.day: Int
//  get() {
//    val split = date.split("-")
//    return if (split.size < 3) 1 else split[2].toInt()
//  }


