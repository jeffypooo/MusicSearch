package com.jmj.musicsearch.presentation.search.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jmj.musicsearch.R
import com.jmj.musicsearch.domain.data.www.model.Artist
import com.jmj.musicsearch.domain.log.GlobalLogger as Log

class ArtistAdapter : RecyclerView.Adapter<ArtistViewHolder>() {
  
  private val artists = mutableListOf<Artist>()
  private var clickListener: ((Artist) -> Unit)? = null
  
  
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
    val inflater = LayoutInflater.from(parent.context)
    return ArtistViewHolder(inflater.inflate(R.layout.rv_artist, parent, false))
  }
  
  override fun getItemCount(): Int = artists.size
  
  override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
    val artist = artists[position]
    holder.bind(artist)
    holder.itemView.setOnClickListener { clickListener?.invoke(artist) }
  }
  
  fun addAll(newArtists: Collection<Artist>) {
    val insertionBegin = artists.indices.last + 1
    val insertionCount = newArtists.size
    Log.v(TAG, "adding $insertionCount items @ $insertionBegin")
    artists.addAll(newArtists)
    notifyItemRangeInserted(insertionBegin, insertionCount)
  }
  
  fun clear() {
    Log.v(TAG, "clearing ${artists.size} items")
    artists.clear()
    notifyDataSetChanged()
  }
  
  fun setOnClickListener(listener: ((Artist) -> Unit)?) {
    this.clickListener = listener
  }
  
  companion object {
    private const val TAG = "ArtistAdapter"
  }
}