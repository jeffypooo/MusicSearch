package com.jmj.musicsearch.presentation.search

import android.app.SearchManager
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.core.content.getSystemService
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jmj.musicsearch.R
import com.jmj.musicsearch.domain.data.www.model.Artist
import com.jmj.musicsearch.frameworks.android.showLongSnackbar
import com.jmj.musicsearch.frameworks.android.showShortSnackbar
import com.jmj.musicsearch.frameworks.dagger.component.DaggerSearchComponent
import com.jmj.musicsearch.presentation.common.AppFragment
import com.jmj.musicsearch.presentation.common.runOnUiThread
import com.jmj.musicsearch.presentation.search.list.ArtistAdapter
import kotlinx.android.synthetic.main.fragment_search.*

/**
 * Entry point for application. Allows for simplistic searching of MusicBrainz.
 */
class ArtistSearchFragment : AppFragment<ArtistSearchPresenter>(), ArtistSearchView {
  
  private lateinit var menu: Menu
  
  private val artistAdapter: ArtistAdapter
    get() = artistRecycler.adapter as ArtistAdapter
  
  override val presenter: ArtistSearchPresenter by lazy {
    DaggerSearchComponent.create().presenter()
  }
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setHasOptionsMenu(true)
  }
  
  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? = inflater.inflate(R.layout.fragment_search, container, false)
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    
    /* configure the recycler view that will display search results */
    artistRecycler.apply {
      this.layoutManager = LinearLayoutManager(context)
      this.adapter = ArtistAdapter().apply { setOnClickListener(presenter::onArtistClick) }
      addOnScrollListener(bottomScrollListener)
    }
    
    /* the helper button is a secondary way to direct user to search function */
    helperButton.setOnClickListener {
      /* hacky, but drives same UX as menu item click :D */
      menu.performIdentifierAction(R.id.search, 0)
    }
    
    presenter.onViewCreated(this)
    
  }
  
  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    inflater.inflate(R.menu.main, menu)
    
    /* connect the search menu item to the search manager so it generates the same
    * intents as the system search dialog. */
    val compName = activity!!.componentName
    val searchableInfo = context!!.getSystemService<SearchManager>()!!.getSearchableInfo(compName)
    (menu.findItem(R.id.search).actionView as SearchView).apply {
      isIconifiedByDefault = false
      setSearchableInfo(searchableInfo)
      setOnQueryTextListener(searchQueryListener)
    }
    
    this.menu = menu
    
    
  }
  
  override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
    /* consume search and history item clicks */
    R.id.search -> {
      presenter.onSearchMenuItemClicked()
      true
    }
    R.id.history -> {
      presenter.onHistoryMenuItemClick()
      true
    }
    else        -> false
  }
  
  
  override fun shortSnackbar(msg: String) = runOnUiThread { showShortSnackbar(contentView, msg) }
  
  override fun longSnackbar(msg: String) = runOnUiThread { showLongSnackbar(contentView, msg) }
  
  override fun dismiss() = runOnUiThread { findNavController().popBackStack() }
  
  override fun showHelperText(message: String) = runOnUiThread {
    helperText.text = message
    helperText.isVisible = true
  }
  
  override fun showHelperSearchButton() = runOnUiThread { helperButton.isVisible = true }
  
  override fun hideAllHelperControls() = runOnUiThread {
    helperText.isVisible = false
    helperButton.isVisible = false
  }
  
  override fun showLoadingIndicator() = runOnUiThread { loadingIndicator.isVisible = true }
  
  override fun hideLoadingIndicator() = runOnUiThread { loadingIndicator.isVisible = false }
  
  override fun addArtists(artists: List<Artist>) = runOnUiThread { artistAdapter.addAll(artists) }
  
  override fun clearArtists() = runOnUiThread { artistAdapter.clear() }
  
  override fun presentArtistDetails(id: String) = runOnUiThread {
    val action = ArtistSearchFragmentDirections.actionSearchToArtist(id)
    findNavController().navigate(action)
  }
  
  override fun presentArtistHistory() = runOnUiThread {
    val action = ArtistSearchFragmentDirections.actionSearchToHistory()
    findNavController().navigate(action)
  }
  
  private val searchFocusListener = View.OnFocusChangeListener { _, hasFocus ->
    if (hasFocus) presenter.onSearchMenuItemClicked()
  }
  
  private val searchQueryListener = object : SearchView.OnQueryTextListener {
    override fun onQueryTextChange(newText: String?) = false
    override fun onQueryTextSubmit(query: String): Boolean {
      presenter.onSearchSubmitted(query)
      return false
    }
  }
  
  private val bottomScrollListener = object : RecyclerView.OnScrollListener() {
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
      if (dy < 0) return // ignore if scrolling up
      val lm = recyclerView.layoutManager as LinearLayoutManager
      /* if we have reached the bottom, the position (index) of the first
      * visible item PLUS the visible item count should be equal to total item count. */
      val visible = lm.childCount
      val total = lm.itemCount
      val fistVisiblePosition = lm.findFirstVisibleItemPosition()
      if (visible + fistVisiblePosition >= total) presenter.onScrolledToBottom()
      
    }
  }
  
  companion object {
    fun newInstance() = ArtistSearchFragment()
  }
}

