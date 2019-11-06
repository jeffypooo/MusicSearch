package com.jmj.musicsearch.presentation.artist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jmj.musicsearch.R
import com.jmj.musicsearch.domain.data.www.model.Release
import com.jmj.musicsearch.domain.log.GlobalLogger
import kotlinx.android.synthetic.main.rv_release.view.*
import java.util.*
import kotlin.properties.Delegates

class ReleaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
  
  var release: Release? by Delegates.observable<Release?>(null) { _, _, new ->
    if (null == new) {
      itemView.name.text = ""
      itemView.year.text = ""
    } else {
      itemView.name.text = new.title
      itemView.year.text = new.date?.let {
        val cal = Calendar.getInstance().apply { time = it }
        "${cal.get(Calendar.YEAR)}"
      }
    }
    
  }
  
}

class ReleaseAdapter : RecyclerView.Adapter<ReleaseViewHolder>() {
  
  private val releases = mutableListOf<Release>()
  
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReleaseViewHolder =
    ReleaseViewHolder(
      LayoutInflater.from(parent.context).inflate(
        R.layout.rv_release,
        parent,
        false
      )
    )
  
  override fun getItemCount(): Int = releases.size
  
  override fun onBindViewHolder(holder: ReleaseViewHolder, position: Int) {
    holder.release = releases[position]
  }
  
  fun addAll(newReleases: Collection<Release>) {
    val insertionBegin = releases.indices.last + 1
    val insertionSize = newReleases.size
    GlobalLogger.v(TAG, "adding $insertionSize @ $insertionBegin")
    releases.addAll(newReleases)
    notifyItemRangeInserted(insertionBegin, insertionSize)
  }
  
  companion object {
    private const val TAG = "ReleaseAdapter"
  }
}