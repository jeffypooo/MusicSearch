package com.jmj.musicsearch.presentation.artist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jmj.musicsearch.R
import com.jmj.musicsearch.domain.data.www.model.Release
import com.jmj.musicsearch.frameworks.android.showLongSnackbar
import com.jmj.musicsearch.frameworks.android.showShortSnackbar
import com.jmj.musicsearch.frameworks.dagger.component.DaggerArtistComponent
import com.jmj.musicsearch.presentation.common.AppFragment
import com.jmj.musicsearch.presentation.common.runOnUiThread
import kotlinx.android.synthetic.main.fragment_artist.*


class ArtistFragment : AppFragment<ArtistPresenter>(), ArtistView {
  
  override val presenter: ArtistPresenter by lazy { DaggerArtistComponent.create().presenter() }
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setHasOptionsMenu(false)
    arguments?.let { presenter.artistId = it.getString(ARG_ARTIST_ID)!! }
  }
  
  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? = inflater.inflate(R.layout.fragment_artist, container, false)
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    discographyRecycler.apply {
      layoutManager = LinearLayoutManager(context)
      adapter = ReleaseAdapter()
    }
    presenter.onViewCreated(this)
  }
  
  override fun shortSnackbar(msg: String) = runOnUiThread { showShortSnackbar(contentView, msg) }
  
  override fun longSnackbar(msg: String) = runOnUiThread { showLongSnackbar(contentView, msg) }
  
  override fun dismiss() = runOnUiThread { findNavController().popBackStack() }
  
  override fun setName(name: String) = runOnUiThread { this.name.text = name }
  
  override fun setType(type: String) = runOnUiThread { this.type.text = type }
  
  override fun setDetails(details: String) = runOnUiThread {
    this.details.isVisible = details.isNotEmpty()
    this.details.text = details
  }
  
  override fun setGeneres(genres: String) = runOnUiThread {
    this.genres.text = genres
  }
  
  override fun setBeginDescription(beginDesc: String) = runOnUiThread {
    this.begin.isVisible = beginDesc.isNotEmpty()
    this.begin.text = beginDesc
  }
  
  override fun setEndDescription(endDesc: String) = runOnUiThread {
    this.end.isVisible = endDesc.isNotEmpty()
    this.end.text = endDesc
  }
  
  private val releaseAdapter: ReleaseAdapter
    get() = discographyRecycler.adapter as ReleaseAdapter
  
  override fun addReleases(releases: List<Release>) = runOnUiThread {
    releaseAdapter.addAll(releases)
  }
  
  companion object {
    
    private const val ARG_ARTIST_ID = "artistId"
    
    fun newInstance(artistId: String) = ArtistFragment().apply {
      arguments = Bundle().apply { putString(ARG_ARTIST_ID, artistId) }
    }
  }
}
