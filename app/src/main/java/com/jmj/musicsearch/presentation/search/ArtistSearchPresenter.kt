package com.jmj.musicsearch.presentation.search

import com.google.gson.GsonBuilder
import com.jmj.musicsearch.domain.data.www.MusicBrainzApi
import com.jmj.musicsearch.domain.data.www.model.Artist
import com.jmj.musicsearch.domain.data.www.model.SearchResponse
import com.jmj.musicsearch.domain.log.Logger
import com.jmj.musicsearch.presentation.common.AppFragmentPresenter
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject
import kotlin.math.min

class ArtistSearchPresenter @Inject constructor(
  private val logger: Logger,
  private val api: MusicBrainzApi
) : AppFragmentPresenter<ArtistSearchView>() {
  
  private val searchDisposables = CompositeDisposable()
  
  private val searchState = SearchState()
  
  override fun onViewCreated(view: ArtistSearchView) {
    super.onViewCreated(view)
    logger.d(TAG, "showing helper controls")
    view.run {
      showHelperText(SEARCH_HELP_TEXT)
      showHelperSearchButton()
    }
  }
  
  override fun onDestroyView() {
    super.onDestroyView()
    searchDisposables.clear()
  }
  
  fun onHistoryMenuItemClick() {
    logger.d(TAG, "presenting history")
    view?.presentArtistHistory()
  }
  
  fun onSearchMenuItemClicked() {
    logger.d(TAG, "hiding helper controls")
    view?.hideAllHelperControls()
  }
  
  fun onSearchSubmitted(query: String) {
    searchDisposables.clear()
    searchState.reset(query)
    view?.clearArtists()
    executeSearch()
  }
  
  fun onScrolledToBottom() {
    if (searchState.isRunningQuery) return
    val newOffset = min(searchState.offset + SEARCH_PAGE_SIZE, searchState.total)
    if (newOffset < searchState.total) {
      logger.d(TAG, "new offset = $newOffset")
      searchState.offset = newOffset
      executeSearch()
    } else {
      logger.d(TAG, "end of results.")
    }
  }
  
  fun onArtistClick(artist: Artist) {
    view?.presentArtistDetails(artist.id)
  }
  
  private fun executeSearch() {
    searchState.isRunningQuery = true
    view?.showLoadingIndicator()
    val query = searchState.query
    val offset = searchState.offset
    logger.d(TAG, "searching for '$query'... w/ offset $offset")
    api.searchArtists(query, SEARCH_PAGE_SIZE, offset)
      .subscribeBy(onError = this::searchFailed, onNext = this::searchCompleted)
      .addTo(searchDisposables)
  }
  
  private fun searchFailed(err: Throwable) {
    logger.e(TAG, err, "main failed!")
    view?.hideLoadingIndicator()
    searchState.reset()
  }
  
  private fun searchCompleted(resp: SearchResponse) {
    logger.d(TAG, "search completed")
    view?.hideLoadingIndicator()
    searchState.apply {
      total = resp.count
      offset = resp.offset
      isRunningQuery = false
    }
    if (resp.count > 0) {
      logger.d(TAG, "adding ${resp.artists.size} artists")
      view?.addArtists(resp.artists)
    } else {
      logger.d(TAG, "search returned no results.")
      view?.showHelperText("No results :(.")
    }
  }
  
  companion object {
    private const val TAG = "ArtistSearchPresenter"
    const val SEARCH_PAGE_SIZE = 25
    const val SEARCH_HELP_TEXT = "Tap below to start a search."
  }
  
}

class SearchState(
  var query: String = "",
  var offset: Int = 0,
  var total: Int = 0,
  var isRunningQuery: Boolean = false
) {
  fun reset(newQuery: String = "") {
    query = newQuery
    offset = 0
    total = 0
    isRunningQuery = false
  }
}