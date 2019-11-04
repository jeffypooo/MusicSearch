package com.jmj.musicsearch.frameworks.dagger.component

import com.jmj.musicsearch.frameworks.dagger.module.ApiModule
import com.jmj.musicsearch.frameworks.dagger.module.LoggingModule
import com.jmj.musicsearch.presentation.search.ArtistSearchPresenter
import dagger.Component

@Component(modules = [LoggingModule::class, ApiModule::class])
interface SearchComponent {
  
  fun presenter(): ArtistSearchPresenter
  
}