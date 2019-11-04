package com.jmj.musicsearch.frameworks.dagger.module

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named

@Module
class DispatcherModule {
  
  @Provides
  @Named("io-dispatcher")
  fun ioDispatcher(): CoroutineDispatcher = Dispatchers.IO
  
}