package com.jmj.musicsearch.frameworks.dagger.module

import com.google.gson.GsonBuilder
import com.jmj.musicsearch.domain.data.www.MusicBrainzApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

@Module
class ApiModule {
  
  @Provides
  fun api(): MusicBrainzApi = Retrofit.Builder()
    .baseUrl(API_URL)
    .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
    .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
    .build()
    .create()
  
  
  companion object {
    private const val API_URL = "https://musicbrainz.org/ws/"
  }
  
}