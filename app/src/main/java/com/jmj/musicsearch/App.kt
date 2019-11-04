package com.jmj.musicsearch

import android.app.Application
import com.jmj.musicsearch.domain.log.GlobalLogger
import com.jmj.musicsearch.frameworks.android.log.LogcatSink
import com.jmj.musicsearch.frameworks.room.RoomHistoryStore

class App: Application() {
  
  override fun onCreate() {
    super.onCreate()
    /* setup singletons */
    GlobalLogger.run {
      addSink(LogcatSink())
      i(TAG, "app launched")
    }
    RoomHistoryStore.setup(this)
  }
  
  companion object {
    private const val TAG = "App"
  }
  
}