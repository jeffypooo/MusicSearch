package com.jmj.musicsearch.frameworks.dagger.component

import com.jmj.musicsearch.frameworks.dagger.module.ApiModule
import com.jmj.musicsearch.frameworks.dagger.module.DatabaseModule
import com.jmj.musicsearch.frameworks.dagger.module.DispatcherModule
import com.jmj.musicsearch.frameworks.dagger.module.LoggingModule
import com.jmj.musicsearch.presentation.artist.ArtistPresenter
import dagger.Component

@Component(
  modules = [
    LoggingModule::class,
    DispatcherModule::class,
    ApiModule::class,
    DatabaseModule::class
  ]
)
interface ArtistComponent {
  
  fun presenter(): ArtistPresenter
  
}