package com.jmj.musicsearch.presentation.history

import com.jmj.musicsearch.domain.data.db.HistoryStore
import com.jmj.musicsearch.domain.data.db.entity.HistoryRecord
import com.jmj.musicsearch.domain.log.Logger
import com.jmj.musicsearch.presentation.common.AppFragmentPresenter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

class ArtistHistoryPresenter @Inject constructor(
  private val logger: Logger,
  @Named("io-dispatcher") private val ioDispatcher: CoroutineDispatcher,
  private val historyStore: HistoryStore
) : AppFragmentPresenter<ArtistHistoryView>() {
  
  
  private val createdScope = CoroutineScope(ioDispatcher)
  
  
  override fun onViewCreated(view: ArtistHistoryView) {
    super.onViewCreated(view)
    loadHistory()
  }
  
  override fun onDestroyView() {
    super.onDestroyView()
    createdScope.cancel()
  }
  
  fun onRecordClicked(record: HistoryRecord) {
    logger.w(TAG, "present artist details for '${record.artistName}'")
    view?.presentArtistDetails(record.artistUid)
  }
  
  private fun loadHistory() {
    createdScope.launch {
      val records = historyStore.getAll()
      if (records.isNotEmpty()) view?.addRecords(records) else view?.showNoHistoryMessage()
    }
  }
  
  companion object {
    private const val TAG = "ArtistHistoryPresenter"
  }
}