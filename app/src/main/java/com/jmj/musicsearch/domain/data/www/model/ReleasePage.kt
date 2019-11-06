package com.jmj.musicsearch.domain.data.www.model

import com.google.gson.annotations.SerializedName

data class ReleasePage(
  val releases: List<Release>,
  @SerializedName("release-offset") val offset: Int,
  @SerializedName("release-count") val total: Int
)