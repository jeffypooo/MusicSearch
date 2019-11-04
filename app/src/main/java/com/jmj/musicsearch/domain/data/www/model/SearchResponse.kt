package com.jmj.musicsearch.domain.data.www.model

data class SearchResponse(
  val created: String,
  val count: Int,
  val offset: Int,
  val artists: List<Artist>
)