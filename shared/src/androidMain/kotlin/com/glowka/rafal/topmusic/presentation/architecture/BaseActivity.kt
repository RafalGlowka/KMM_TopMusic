package com.glowka.rafal.topmusic.presentation.architecture

import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

abstract class BaseActivity : AppCompatActivity() {

  protected abstract val navigator: FragmentNavigator

  @Suppress("DEPRECATION")
  override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
    super.onCreate(savedInstanceState, persistentState)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
      window.setDecorFitsSystemWindows(false)
    } else {
      window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or
         View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
         View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }
  }

  override fun onResume() {
    super.onResume()
    navigator.attach(this)
  }

  override fun onPause() {
    navigator.detach()
    super.onPause()
  }

  override fun onBackPressed() {
    val currentFragment = supportFragmentManager.fragments.lastBaseFragment()
    currentFragment?.onBackPressed()
    return
  }
}

fun List<Fragment>.lastBaseFragment(): BaseFragment<*, *>? {
  return lastOrNull { fragment -> fragment is BaseFragment<*, *> } as? BaseFragment<*, *>
}