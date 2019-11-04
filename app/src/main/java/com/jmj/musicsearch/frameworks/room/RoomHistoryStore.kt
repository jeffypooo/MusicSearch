package com.jmj.musicsearch.frameworks.room

import android.app.Application
import androidx.room.Room
import com.jmj.musicsearch.domain.data.db.HistoryStore
import com.jmj.musicsearch.domain.data.db.entity.HistoryRecord
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import java.io.Closeable
import java.util.*
import com.jmj.musicsearch.domain.log.GlobalLogger as Log


/**
 * Singleton implementation of [HistoryStore].
 */
object RoomHistoryStore : HistoryStore, Closeable {
  
  private const val TAG = "RoomHistoryStore"
  private const val DB_NAME = "musicsearch.history.db"
  
  private var database: RoomHistoryDb? = null
  
  private val dao: RoomHistoryDao
    get() = requireNotNull(database?.getDao()) { "Failed to get DAO ref. Did you call init()?" }
  
  fun setup(app: Application) {
    close()
    database = Room.databaseBuilder(app, RoomHistoryDb::class.java, DB_NAME).build()
    Log.i(TAG, "database opened")
  }
  
  override fun close() {
    database?.let {
      Log.i(TAG, "closing database")
      close()
    }
    database = null
  }
  
  override suspend fun getAll(): List<HistoryRecord> = dao.getAll().toDomainModels()
  
  override suspend fun getRecord(id: String): HistoryRecord? = dao.getById(id)?.toDomainModel()
  
  override suspend fun insert(record: HistoryRecord) = dao.insert(record.toRoomEntity())
  
  override suspend fun update(record: HistoryRecord) = dao.update(record.toRoomEntity())
  
  override suspend fun delete(record: HistoryRecord) = dao.delete(record.toRoomEntity())
  
  override suspend fun deleteAll() = dao.getAll().forEach { dao.delete(it) }
  
}

internal fun Collection<RoomHistoryRecord>.toDomainModels() = map(RoomHistoryRecord::toDomainModel)

internal fun Collection<HistoryRecord>.toRoomEntities() = map(HistoryRecord::toRoomEntity)

internal fun RoomHistoryRecord.toDomainModel() =
  HistoryRecord(artistUid, artistName, Date(lastVisitTime))

internal fun HistoryRecord.toRoomEntity() = RoomHistoryRecord(artistUid, artistName, lastVisit.time)