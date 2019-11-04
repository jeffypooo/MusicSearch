package com.jmj.musicsearch.presentation.search

import com.jmj.musicsearch.domain.data.www.MusicBrainzApi
import com.jmj.musicsearch.domain.data.www.model.Artist
import com.jmj.musicsearch.domain.data.www.model.Lifespan
import com.jmj.musicsearch.domain.data.www.model.MetaTag
import com.jmj.musicsearch.domain.data.www.model.SearchResponse
import com.jmj.musicsearch.domain.log.GlobalLogger
import com.jmj.musicsearch.log.StdoutSink
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Observable
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test


class ArtistSearchPresenterTest {
  
  private val mockApi = mock<MusicBrainzApi> {
    on { searchArtists(any(), any(), any()) } doReturn Observable.never()
  }
  private val mockView = mock<ArtistSearchView>()
  
  private val presenter = ArtistSearchPresenter(GlobalLogger, mockApi)
  
  @Test
  fun `when view is created, helper controls are displayed`() {
    presenter.onViewCreated(mockView)
    verify(mockView).showHelperText(ArtistSearchPresenter.SEARCH_HELP_TEXT)
    verify(mockView).showHelperSearchButton()
  }
  
  @Test
  fun `when search is focused, helper controls are hidden`() {
    driveToResumed()
    
    /* user focuses search field */
    presenter.onSearchMenuItemClicked()
    
    /* helper controls should be hidden */
    verify(mockView).hideAllHelperControls()
  }
  
  @Test
  fun `when search is submitted, api is queried and results are added`() {
    /* stub api mock with some test data */
    val resp = SearchResponse("1992", 1, 0, listOf(todayWasAGoodDay()))
    mockApi.stub {
      on { searchArtists(any(), any(), any()) } doReturn Observable.just(resp)
    }
    
    driveToResumed()
    
    /* user submits a search */
    val query = "foobar"
    presenter.onSearchSubmitted(query)
    
    /* progress indicator should show */
    verify(mockView).showLoadingIndicator()
    
    /* api should be queried */
    verify(mockApi).searchArtists(
      query = query,
      limit = ArtistSearchPresenter.SEARCH_PAGE_SIZE,
      offset = 0 // no offset for first call
    )
    
    /* results should be added to view and progress indicator hidden */
    verify(mockView).addArtists(resp.artists)
    verify(mockView).hideLoadingIndicator()
  }
  
  @Test
  fun `when search is submitted, existing results are cleared`() {
    driveToResumed()
    
    /* user submits a search */
    val query = "foobar"
    presenter.onSearchSubmitted(query)
    
    /* artists cleared */
    verify(mockView).clearArtists()
  }
  
  @Test
  fun `when the search returns nothing, helper text is shown with error message`() {
    /* stub mock api to return an empty response */
    val resp = SearchResponse("1991", 0, 0, emptyList())
    mockApi.stub {
      on { searchArtists(any(), any(), any()) } doReturn Observable.just(resp)
    }
    
    driveToResumed()
  
    /* user submits a search */
    val query = "foobar"
    presenter.onSearchSubmitted(query)
    
    /* helper text is shown with error */
    verify(mockView).showHelperText("No results :(.")
  }
  
  @Test
  fun `when the results list is scrolled to the bottom, the search is updated with a new page`() {
    /* stub api mock with some test data */
    val resp = SearchResponse("1992", 9001, 0, listOf(todayWasAGoodDay()))
    mockApi.stub {
      on { searchArtists(any(), any(), any()) } doReturn Observable.just(resp)
    }
    
    driveToResumed()
    
    
    /* user submits a search, and scrolls to bottom */
    val query = "foobar"
    presenter.onSearchSubmitted(query)
    presenter.onScrolledToBottom()
    
    /* api should be queried twice */
    verify(mockApi).searchArtists(
      query = query,
      limit = ArtistSearchPresenter.SEARCH_PAGE_SIZE,
      offset = 0 // no offset for first call
    )
    verify(mockApi).searchArtists(
      query = query,
      limit = ArtistSearchPresenter.SEARCH_PAGE_SIZE,
      offset = ArtistSearchPresenter.SEARCH_PAGE_SIZE // offset by 1 page
    )
    
    /* results should be added to view twice */
    verify(mockView, times(2)).addArtists(resp.artists)
  }
  
  @Test
  fun `when search is still running, scrolling to bottom does nothing`() {
    /* no stubbing required, the default mock behavior will never return from the
    * search call */
    driveToResumed()
    
    /* user submits a search, and scrolls to the bottom */
    val query = "foobar"
    presenter.onSearchSubmitted(query)
    presenter.onScrolledToBottom()
    
    
    /* api should be queried once only */
    verify(mockApi, times(1)).searchArtists(
      query = any(),
      limit = any(),
      offset = any()
    )
  }
  
  
  @Test
  fun `when artist is clicked, the artist details view is presented`() {
    /* stub api mock with some test data */
    val resp = SearchResponse("1992", 1, 0, listOf(todayWasAGoodDay()))
    mockApi.stub {
      on { searchArtists(any(), any(), any()) } doReturn Observable.just(resp)
    }
    
    driveToResumed()
    
    /* user submits a search and clicks a result */
    val query = "foobar"
    val artist = resp.artists.first()
    presenter.onSearchSubmitted(query)
    presenter.onArtistClick(artist)
    
    /* details view should be presented */
    verify(mockView).presentArtistDetails(artist.id)
  }
  
  @Test
  fun `when history menu item is clicked, the artist history view is presented`() {
    driveToResumed()
    
    /* user clicks the history menu item */
    presenter.onHistoryMenuItemClick()
    
    /* history view is presented */
    verify(mockView).presentArtistHistory()
  }
  
  
  private fun driveToResumed() {
    presenter.run {
      onViewCreated(mockView)
      onStart()
      onResume()
    }
  }
  
  
  companion object {
    
    @BeforeClass
    @JvmStatic
    fun setupClass() {
      GlobalLogger.addSink(StdoutSink())
    }
    
    @AfterClass
    @JvmStatic
    fun tearDownClass() {
      GlobalLogger.clearSinks()
    }
    
  }
  
}

/**
 * Create an [Artist] instance for one the all-time greats of west coast hip-hop.
 */
private fun todayWasAGoodDay() = Artist(
  id = "123",
  name = "Ice Cube",
  type = "Person",
  country = "US",
  disambiguation = "Ice Cube is a gangster rapper from Los Angeles.",
  lifespan = Lifespan(false, "1969-06-15", null),
  tags = listOf(MetaTag(1, "west coast gangster rap"))
)