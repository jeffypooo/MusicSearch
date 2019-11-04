package com.jmj.musicsearch.presentation.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jmj.musicsearch.R
import com.jmj.musicsearch.domain.data.db.entity.HistoryRecord
import com.jmj.musicsearch.frameworks.android.showLongSnackbar
import com.jmj.musicsearch.frameworks.android.showShortSnackbar
import com.jmj.musicsearch.frameworks.dagger.component.DaggerHistoryComponent
import com.jmj.musicsearch.presentation.common.AppBottomSheet
import com.jmj.musicsearch.presentation.common.runOnUiThread
import com.jmj.musicsearch.presentation.history.list.HistoryAdapter
import kotlinx.android.synthetic.main.fragment_history.*

class ArtistHistoryBottomSheet : AppBottomSheet<ArtistHistoryPresenter>(), ArtistHistoryView {
  
  private val historyAdapter: HistoryAdapter
    get() = historyRecycler.adapter as HistoryAdapter
  
  override val presenter: ArtistHistoryPresenter by lazy {
    DaggerHistoryComponent.create().presenter()
  }
  
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_history, container, false)
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    
    /* configure recycler view that will display the history */
    historyRecycler.apply {
      layoutManager = LinearLayoutManager(context)
      adapter = HistoryAdapter().apply { setOnClickListener(presenter::onRecordClicked) }
    }
    
    presenter.onViewCreated(this)
  }
  
  override fun shortSnackbar(msg: String) = runOnUiThread { showShortSnackbar(contentView, msg) }
  
  override fun longSnackbar(msg: String) = runOnUiThread { showLongSnackbar(contentView, msg) }
  
  override fun showNoHistoryMessage() = runOnUiThread { noHistoryLabel.isVisible = true }
  
  override fun addRecords(records: List<HistoryRecord>) = runOnUiThread {
    historyAdapter.addAll(records)
  }
  
  override fun presentArtistDetails(id: String) = runOnUiThread {
    val action = ArtistHistoryBottomSheetDirections.actionHistoryToArtist(id)
    findNavController().navigate(action)
  }
}

