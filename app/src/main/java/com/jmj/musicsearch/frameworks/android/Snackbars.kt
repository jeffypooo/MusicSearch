package com.jmj.musicsearch.frameworks.android

import android.view.View
import com.google.android.material.snackbar.Snackbar


fun showShortSnackbar(view: View, msg: String) = showSnackbar(view, msg, Snackbar.LENGTH_SHORT)

fun showLongSnackbar(view: View, msg: String) = showSnackbar(view, msg, Snackbar.LENGTH_LONG)

fun showSnackbar(view: View, msg: String, dur: Int) = Snackbar.make(view, msg, dur)
  .also(Snackbar::show)