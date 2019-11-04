package com.jmj.musicsearch.presentation.history.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jmj.musicsearch.R
import com.jmj.musicsearch.domain.data.db.entity.HistoryRecord
import kotlinx.android.synthetic.main.rv_history.view.*
import com.jmj.musicsearch.domain.log.GlobalLogger as Log


class HistoryAdapter: RecyclerView.Adapter<HistoryRecordViewHolder>() {
  
  private val records = mutableListOf<HistoryRecord>()
  private var clickListener: ((HistoryRecord) -> Unit)? = null
  
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryRecordViewHolder {
    val inflater = LayoutInflater.from(parent.context)
    return HistoryRecordViewHolder(inflater.inflate(R.layout.rv_history, parent, false))
  }
  
  override fun getItemCount(): Int = records.size
  
  override fun onBindViewHolder(holder: HistoryRecordViewHolder, position: Int) {
    val record = records[position]
    holder.bind(record)
    holder.itemView.setOnClickListener { clickListener?.invoke(record) }
  }
  
  fun addAll(newRecords: Collection<HistoryRecord>) {
    val insertionBegin = records.indices.last + 1
    val insertionCount = newRecords.size
    Log.v(TAG, "adding $insertionCount items @ $insertionBegin")
    records.addAll(newRecords)
    notifyItemRangeInserted(insertionBegin, insertionCount)
  }
  
  fun clear() {
    Log.v(TAG, "clearing ${records.size} items")
    records.clear()
    notifyDataSetChanged()
  }
  
  fun setOnClickListener(listener: ((HistoryRecord) -> Unit)?) {
    this.clickListener = listener
  }
  
  companion object {
    private const val TAG = "HistoryAdapter"
  }
  
}

class HistoryRecordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
  
  fun bind(record: HistoryRecord) {
    itemView.name.text = record.artistName
    itemView.visitTime.text = record.lastVisit.toString()
  }
  
}