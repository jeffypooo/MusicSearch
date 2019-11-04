package com.jmj.musicsearch.frameworks.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "history")
data class RoomHistoryRecord(
  @PrimaryKey val artistUid: String,
  val artistName: String,
  val lastVisitTime: Long
)