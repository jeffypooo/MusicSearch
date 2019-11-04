package com.jmj.musicsearch.frameworks.dagger.module

import com.jmj.musicsearch.domain.log.GlobalLogger
import com.jmj.musicsearch.domain.log.Logger
import dagger.Module
import dagger.Provides

@Module
class LoggingModule {
  
  @Provides
  fun logger(): Logger = GlobalLogger
  
}