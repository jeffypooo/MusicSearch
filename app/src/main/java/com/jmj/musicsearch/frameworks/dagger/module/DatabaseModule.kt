package com.jmj.musicsearch.frameworks.dagger.module

import com.jmj.musicsearch.domain.data.db.HistoryStore
import com.jmj.musicsearch.frameworks.room.RoomHistoryStore
import dagger.Module
import dagger.Provides

@Module
class DatabaseModule {
  
  @Provides
  fun historyStore(): HistoryStore = RoomHistoryStore
  
}