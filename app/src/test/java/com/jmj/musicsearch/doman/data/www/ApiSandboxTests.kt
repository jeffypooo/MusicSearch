package com.jmj.musicsearch.doman.data.www

import com.jmj.musicsearch.domain.data.www.MusicBrainzApi
import com.jmj.musicsearch.frameworks.dagger.module.ApiModule
import org.junit.Test


class ApiSandboxTests {
  
  
  private val api: MusicBrainzApi by lazy {
    ApiModule().let { it.api(it.gson()) }
  }
  
  @Test
  fun `get releases for an artist`() {
    val page = api.getReleases(UID_ICE_CUBE).blockingFirst()
    page.releases.sortedByDescending { it.date }.forEach {
      println(
        """
        Release {
          title: "${it.title}",
          year: ${it.date},
          id: ${it.id}
        }
      """.trimIndent()
      )
    }
  }
  
  
  companion object {
    const val UID_ICE_CUBE = "1d11e2a1-4531-4d61-a8c7-7b5c6a608fd2"
  }
  
  
}