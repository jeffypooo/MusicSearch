package com.jmj.musicsearch.frameworks.dagger.module

import com.google.gson.*
import com.jmj.musicsearch.domain.data.www.MusicBrainzApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.lang.reflect.Type
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
  override fun deserialize(
    json: JsonElement,
    typeOfT: Type,
    context: JsonDeserializationContext
  ): Date {
    val splits = json.asString.split("-")
    val cal = Calendar.getInstance().apply {
      set(
        splits.first().toInt(),
        if (splits.size >= 2) splits[1].toInt() else 0,
        if (splits.size >= 3) splits[2].toInt() else 1,
        0,
        0,
        0
      )
    }
    return cal.time
    
  }
}