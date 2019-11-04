package com.jmj.musicsearch.presentation.search.list

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.jmj.musicsearch.domain.data.www.model.Artist
import com.jmj.musicsearch.presentation.common.uiFriendlyTags
import kotlinx.android.synthetic.main.rv_artist.view.*

class ArtistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
  
  
  fun bind(artist: Artist) {
    itemView.apply {
      name.text = artist.name
      type.text = artist.type
      disambiguation.isVisible = !artist.disambiguation.isNullOrEmpty()
      disambiguation.text = artist.disambiguation
      tags.text = artist.uiFriendlyTags
    }
  }
  
  fun setOnClickListener(listener: View.OnClickListener) {
    itemView.setOnClickListener(listener)
  }
  
}