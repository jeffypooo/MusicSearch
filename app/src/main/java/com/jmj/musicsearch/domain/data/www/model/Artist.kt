package com.jmj.musicsearch.domain.data.www.model

import com.google.gson.annotations.SerializedName

data class Artist(
  val id: String,
  val name: String,
  val type: String,
  val country: String,
  val disambiguation: String?,
  @SerializedName("life-span") val lifespan: Lifespan?,
  val tags: List<MetaTag>?
)