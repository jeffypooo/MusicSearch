package com.jmj.musicsearch.frameworks.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RoomHistoryRecord::class], version = 1)
abstract class RoomHistoryDb: RoomDatabase() {
  abstract fun getDao(): RoomHistoryDao
}