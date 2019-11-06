package com.jmj.musicsearch.presentation.artist

import com.jmj.musicsearch.domain.data.db.HistoryStore
import com.jmj.musicsearch.domain.data.db.entity.HistoryRecord
import com.jmj.musicsearch.domain.data.www.MusicBrainzApi
import com.jmj.musicsearch.domain.data.www.model.Artist
import com.jmj.musicsearch.domain.data.www.model.Release
import com.jmj.musicsearch.domain.data.www.model.ReleasePage
import com.jmj.musicsearch.domain.log.Logger
import com.jmj.musicsearch.presentation.common.AppFragmentPresenter
import com.jmj.musicsearch.presentation.common.uiFriendlyLifespanEnd
import com.jmj.musicsearch.presentation.common.uiFriendlyLifespanStart
import com.jmj.musicsearch.presentation.common.uiFriendlyTags
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named
import kotlin.math.min

class ArtistPresenter @Inject constructor(
  private val logger: Logger,
  @Named("io-dispatcher") private val ioDispatcher: CoroutineDispatcher,
  private val api: MusicBrainzApi,
  private val historyStore: HistoryStore
) : AppFragmentPresenter<ArtistView>() {
  
  var artistId: String = ""
  
  private val createdDisposables = CompositeDisposable()
  private val createdScope = CoroutineScope(ioDispatcher)
  private var pageState = PageState()
  
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
    api.getReleases(artistId, limit = 1000, offset = 0)
      .subscribeBy(onError = this::getReleasesFailed, onNext = this::releasesLoaded)
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
    persistHistoryRecord(artist)
    updateArtistInfo(artist)
    
  }
  
  private fun updateArtistInfo(artist: Artist) {
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
  
  private fun persistHistoryRecord(artist: Artist) {
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
  
  private fun getReleasesFailed(err: Throwable) {
    logger.e(TAG, err, "failed to get releases for $artistId")
    view?.longSnackbar("Error loading discography.")
    pageState = PageState()
  }
  
  private val queryScheduler = Schedulers.from(Executors.newSingleThreadExecutor())
  
  private fun executeReleaseQuery() {
    
    api.getReleases(artistId, limit = PAGE_SIZE, offset = pageState.offset)
//      .delay(1, TimeUnit.SECONDS)
      .retryWhen { it.flatMap { Observable.timer(1, TimeUnit.SECONDS) } }
      .observeOn(queryScheduler)
      .subscribeBy(onError = this::getReleasesFailed, onNext = this::releasesLoaded)
      .addTo(createdDisposables)
  }
  
  private val releaseCache = mutableListOf<Release>()
  
  private fun releasesLoaded(page: ReleasePage) {
    pageState = pageState.copy(offset = page.offset, total = page.total)
    logger.d(TAG, "loaded release page, offset = ${page.offset}, total = ${page.total}")
    releaseCache.addAll(page.releases)
    /* load next page if necessary */
    val newOffset = min(pageState.offset + PAGE_SIZE, pageState.total)
    if (newOffset < pageState.total) {
      logger.d(TAG, "new offset = $newOffset")
      pageState = pageState.copy(offset = newOffset)
      executeReleaseQuery()
    } else {
      logger.d(TAG, "end of results.")
      val processed = releaseCache.sortedByDescending(Release::date)
        .distinctBy(Release::title)
      releaseCache.clear()
      view?.addReleases(processed)
    }
  }
  
  
  companion object {
    private const val TAG = "ArtistPresenter"
    private const val LOADING = "Loading..."
    const val PAGE_SIZE = 100
  }
  
}

data class PageState(
  val offset: Int = 0,
  val total: Int = 0
)
