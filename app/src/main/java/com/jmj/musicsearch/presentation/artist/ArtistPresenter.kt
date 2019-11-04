package com.jmj.musicsearch.presentation.artist

import com.jmj.musicsearch.domain.data.db.HistoryStore
import com.jmj.musicsearch.domain.data.db.entity.HistoryRecord
import com.jmj.musicsearch.domain.data.www.MusicBrainzApi
import com.jmj.musicsearch.domain.data.www.model.Artist
import com.jmj.musicsearch.domain.log.Logger
import com.jmj.musicsearch.presentation.common.AppFragmentPresenter
import com.jmj.musicsearch.presentation.common.uiFriendlyLifespanEnd
import com.jmj.musicsearch.presentation.common.uiFriendlyLifespanStart
import com.jmj.musicsearch.presentation.common.uiFriendlyTags
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject
import javax.inject.Named

class ArtistPresenter @Inject constructor(
  private val logger: Logger,
  @Named("io-dispatcher") private val ioDispatcher: CoroutineDispatcher,
  private val api: MusicBrainzApi,
  private val historyStore: HistoryStore
) : AppFragmentPresenter<ArtistView>() {
  
  var artistId: String = ""
  
  private val createdDisposables = CompositeDisposable()
  private val createdScope = CoroutineScope(ioDispatcher)
  
  override fun onViewCreated(view: ArtistView) {
    super.onViewCreated(view)
    view.apply {
      setName(LOADING)
      setDetails(LOADING)
      setBeginDescription("")
      setEndDescription(LOADING)
      setGeneres(LOADING)
    }
    api.getArtist(artistId)
      .subscribeBy(onError = this::getArtistFailed, onNext = this::artistLoaded)
      .addTo(createdDisposables)
  }
  
  override fun onDestroyView() {
    super.onDestroyView()
    createdDisposables.clear()
    createdScope.cancel()
  }
  
  private fun getArtistFailed(err: Throwable) {
    logger.e(TAG, err, "failed to load artist")
    view?.dismiss()
  }
  
  private fun artistLoaded(artist: Artist) {
    logger.d(TAG, "loaded '${artist.name}'")
    updateHistory(artist)
    view?.apply {
      setName(artist.name)
      setType(artist.type)
      setDetails(artist.disambiguation ?: "")
      setGeneres(artist.uiFriendlyTags)
    }
    updateLifespan(artist)
    
  }
  
  
  private fun updateLifespan(artist: Artist) {
    val start = artist.uiFriendlyLifespanStart
    val end = artist.uiFriendlyLifespanEnd ?: ""
    view?.apply {
      if (start != null) {
        setBeginDescription(start)
        setEndDescription(end)
      } else {
        setBeginDescription("")
        setEndDescription("No lifespan information.")
      }
    }
    
  }
  
  private fun updateHistory(artist: Artist) {
    /* check if this artist is already in the history first */
    createdScope.launch {
      logger.d(TAG, "checking history for '${artist.name}'")
      val existing = historyStore.getRecord(artist.id)
      if (existing == null) {
        logger.d(TAG, "inserting history record")
        historyStore.insert(HistoryRecord(artist.id, artist.name))
      } else {
        logger.d(TAG, "updating history record")
        historyStore.update(existing.copy(lastVisit = Date()))
      }
    }
    
  }
  
  
  companion object {
    private const val TAG = "ArtistPresenter"
    private const val LOADING = "Loading..."
  }
  
}