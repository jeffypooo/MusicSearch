package com.jmj.musicsearch.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.jmj.musicsearch.R
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Simple container activity for hosting fragment nav.
 */
class MainActivity : AppCompatActivity() {
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    setSupportActionBar(toolbar)
    NavigationUI.setupActionBarWithNavController(this, findNavController(R.id.nav_host_fragment))
  }
  
  override fun onSupportNavigateUp(): Boolean {
    return findNavController(R.id.nav_host_fragment).navigateUp()
  }
}
