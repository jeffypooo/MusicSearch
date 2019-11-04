package com.jmj.musicsearch.domain.data.db

import com.jmj.musicsearch.domain.data.db.entity.HistoryRecord
import io.reactivex.Flowable

interface HistoryStore {
  
  suspend fun getAll(): List<HistoryRecord>
  
  suspend fun getRecord(id: String): HistoryRecord?
  
  suspend fun insert(record: HistoryRecord)
  
  suspend fun update(record: HistoryRecord)
  
  suspend fun delete(record: HistoryRecord)
  
  suspend fun deleteAll()
  
}