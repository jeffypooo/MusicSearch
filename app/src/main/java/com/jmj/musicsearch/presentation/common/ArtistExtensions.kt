package com.jmj.musicsearch.presentation.common

import com.jmj.musicsearch.domain.data.www.model.Artist
import com.jmj.musicsearch.domain.data.www.model.MetaTag
import kotlin.math.min


val Artist.isPerson: Boolean
  get() = type == "Person"

val Artist.uiFriendlyTags: String
  get() {
    val important = tags?.filter { it.count > 0 }?.sortedByDescending { it.count }
    if (important == null || important.isEmpty()) {
      return "No tags."
    }
    val truncated = important.subList(0, min(8, important.size))
    val delta = important.size - truncated.size
    val genres = truncated.joinToString(separator = ", ", transform = MetaTag::name)
    return if (delta > 0) {
      "$genres, + $delta more"
    } else {
      genres
    }
  }

val Artist.uiFriendlyLifespanStart: String?
  get() {
    val begin = lifespan?.begin ?: return null
    return "${if (isPerson) "Born:" else "Founded:"} $begin"
  }

val Artist.uiFriendlyLifespanEnd: String?
  get() {
    val end = lifespan?.end ?: return null
    return "${if (isPerson) "Died:" else "Dissolved:"} $end"
  }