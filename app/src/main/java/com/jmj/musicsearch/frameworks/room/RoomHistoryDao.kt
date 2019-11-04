package com.jmj.musicsearch.frameworks.room

import androidx.room.*
import io.reactivex.Flowable

@Dao
interface RoomHistoryDao {
  
  @Query("SELECT * FROM history ORDER BY lastVisitTime DESC")
  suspend fun getAll(): List<RoomHistoryRecord>
  
  @Query("SELECT * FROM history WHERE artistUid == :id LIMIT 1")
  suspend fun getById(id: String): RoomHistoryRecord?
  
  @Insert
  suspend fun insert(record: RoomHistoryRecord)
  
  @Update
  suspend fun update(record: RoomHistoryRecord)
  
  @Delete
  suspend fun delete(record: RoomHistoryRecord)
  
}