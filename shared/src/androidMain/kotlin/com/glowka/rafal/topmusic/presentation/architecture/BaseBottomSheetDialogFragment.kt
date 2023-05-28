package com.glowka.rafal.topmusic.presentation.architecture

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.glowka.rafal.topmusic.di.injectViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheetDialogFragment<
    VIEW_STATE : ViewState,
    VIEW_EVENT : ViewEvent,
    VIEW_MODEL : ViewModelToViewInterface<VIEW_STATE, VIEW_EVENT>
   > :
  ScreenDialogFragment<VIEW_STATE, VIEW_EVENT, VIEW_MODEL>, BottomSheetDialogFragment() {
  protected val viewModel: VIEW_MODEL by injectViewModel()

  final override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    return ComposeView(requireContext()).apply {
      setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
      setContent(content)
    }
  }

  abstract val content: @Composable () -> Unit

  fun onBackPressed(): Boolean {
    return viewModel.onBackPressed()
  }

  companion object {
    const val ARG_SCOPE = "scope"
    const val ARG_SCREEN_TAG = "screenTag"
  }
}