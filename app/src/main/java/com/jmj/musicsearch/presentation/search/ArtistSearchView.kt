package com.jmj.musicsearch.presentation.search

import com.jmj.musicsearch.domain.data.www.model.Artist
import com.jmj.musicsearch.presentation.common.AppView


interface ArtistSearchView : AppView {
  
  fun showHelperText(message: String)
  fun showHelperSearchButton()
  fun hideAllHelperControls()
  fun showLoadingIndicator()
  fun hideLoadingIndicator()
  fun addArtists(artists: List<Artist>)
  fun clearArtists()
  fun presentArtistDetails(id: String)
  fun presentArtistHistory()
  
}