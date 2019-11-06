package com.jmj.musicsearch.frameworks.dagger.module

import com.google.gson.*
import com.jmj.musicsearch.domain.data.www.MusicBrainzApi
import com.jmj.musicsearch.domain.data.www.tryParse
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*

@Module
class ApiModule {
  
  @Provides
  fun api(gson: Gson): MusicBrainzApi = Retrofit.Builder()
    .baseUrl(API_URL)
    .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
    .addConverterFactory(GsonConverterFactory.create(gson))
    .build()
    .create()
  
  @Provides
  fun gson(): Gson {
    return GsonBuilder()
      .setLenient()
      .registerTypeAdapter(Date::class.java, DateDeserializer())
      .create()
  }
  
  
  companion object {
    const val API_URL = "https://musicbrainz.org/ws/"
  }
  
}

class DateDeserializer : JsonDeserializer<Date> {
  
  private val yearMonthDay = SimpleDateFormat("yyyy-MM-dd")
  private val yearMonth = SimpleDateFormat("yyyy-MM")
  private val year = SimpleDateFormat("yyyy")
  
  override fun deserialize(
    json: JsonElement,
    typeOfT: Type,
    context: JsonDeserializationContext
  ): Date? {
    return yearMonthDay.tryParse(json.asString)
        ?: yearMonth.tryParse(json.asString)
        ?: year.tryParse(json.asString)
    
  }
  
  
}