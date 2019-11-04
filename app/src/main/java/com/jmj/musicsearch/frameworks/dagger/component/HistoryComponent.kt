package com.jmj.musicsearch.frameworks.dagger.component

import com.jmj.musicsearch.frameworks.dagger.module.ApiModule
import com.jmj.musicsearch.frameworks.dagger.module.DatabaseModule
import com.jmj.musicsearch.frameworks.dagger.module.DispatcherModule
import com.jmj.musicsearch.frameworks.dagger.module.LoggingModule
import com.jmj.musicsearch.presentation.history.ArtistHistoryPresenter
import dagger.Component

@Component(
  modules = [
    LoggingModule::class,
    DispatcherModule::class,
    DatabaseModule::class
  ]
)
interface HistoryComponent {
  
  fun presenter(): ArtistHistoryPresenter
  
}