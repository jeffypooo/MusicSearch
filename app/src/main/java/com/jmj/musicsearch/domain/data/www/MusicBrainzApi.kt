package com.jmj.musicsearch.domain.data.www

import com.jmj.musicsearch.domain.data.www.model.Artist
import com.jmj.musicsearch.domain.data.www.model.Release
import com.jmj.musicsearch.domain.data.www.model.ReleasePage
import com.jmj.musicsearch.domain.data.www.model.SearchResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface MusicBrainzApi {
  
  @Headers("Accept:application/json; charset=UTF-8")
  @GET("2/artist")
  fun searchArtists(
    @Query("query") query: String,
    @Query("limit") limit: Int,
    @Query("offset") offset: Int
  ): Observable<SearchResponse>
  
  @Headers("Accept:application/json; charset=UTF-8")
  @GET("2/artist/{mbid}")
  fun getArtist(
    @Path("mbid") id: String,
    @Query("inc") includes: String = "tags"
  ): Observable<Artist>
  
  @Headers("Accept:application/json; charset=UTF-8")
  @GET("2/release")
  fun getReleases(
    @Query("artist") artistUid: String,
    @Query("limit") limit: Int,
    @Query("offset") offset: Int
  ): Observable<ReleasePage>
}