package com.jmj.musicsearch.presentation.common

interface AppView {

  fun shortSnackbar(msg: String)

  fun longSnackbar(msg: String)

  fun dismiss()

}