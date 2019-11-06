package com.jmj.musicsearch.presentation.artist

import com.jmj.musicsearch.domain.data.www.model.Release
import com.jmj.musicsearch.presentation.common.AppView

interface ArtistView: AppView {
  
  fun setName(name: String)
  fun setType(type: String)
  fun setDetails(details: String)
  fun setGeneres(genres: String)
  fun setBeginDescription(beginDesc: String)
  fun setEndDescription(endDesc: String)
  fun addReleases(releases: List<Release>)
  
}