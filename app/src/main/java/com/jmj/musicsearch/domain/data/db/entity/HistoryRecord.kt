package com.jmj.musicsearch.domain.data.db.entity

import java.util.*


data class HistoryRecord(
  val artistUid: String,
  val artistName: String,
  val lastVisit: Date = Date()
)