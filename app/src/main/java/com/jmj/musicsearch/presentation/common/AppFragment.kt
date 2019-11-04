package com.jmj.musicsearch.presentation.common

import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jmj.musicsearch.frameworks.android.showShortSnackbar

abstract class AppFragment<P : AppFragmentPresenter<*>> : Fragment() {
  
  protected abstract val presenter: P
  
  override fun onDestroyView() {
    super.onDestroyView()
    presenter.onDestroyView()
  }
  
  override fun onStart() {
    super.onStart()
    presenter.onStart()
  }
  
  override fun onStop() {
    super.onStop()
    presenter.onStop()
  }
  
  override fun onResume() {
    super.onResume()
    presenter.onResume()
  }
  
  override fun onPause() {
    super.onPause()
    presenter.onPause()
  }
}

abstract class AppBottomSheet<P: AppFragmentPresenter<*>>: BottomSheetDialogFragment() {
  
  protected abstract val presenter: P
  
  override fun onDestroyView() {
    super.onDestroyView()
    presenter.onDestroyView()
  }
  
  override fun onStart() {
    super.onStart()
    presenter.onStart()
  }
  
  override fun onStop() {
    super.onStop()
    presenter.onStop()
  }
  
  override fun onResume() {
    super.onResume()
    presenter.onResume()
  }
  
  override fun onPause() {
    super.onPause()
    presenter.onPause()
  }
  
}

internal fun Fragment.runOnUiThread(block: () -> Unit) = activity!!.runOnUiThread(block)