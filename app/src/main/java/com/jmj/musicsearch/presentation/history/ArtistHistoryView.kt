package com.jmj.musicsearch.presentation.history

import com.jmj.musicsearch.domain.data.db.entity.HistoryRecord
import com.jmj.musicsearch.presentation.common.AppView

interface ArtistHistoryView : AppView {
  
  fun showNoHistoryMessage()

  fun addRecords(records: List<HistoryRecord>)
  
  fun presentArtistDetails(id: String)
  
}