@file:Suppress("MaxLineLength")

package com.glowka.rafal.topmusic.presentation

import androidx.fragment.app.Fragment
import com.glowka.rafal.topmusic.flow.dashboard.details.DetailsScreenStructure
import com.glowka.rafal.topmusic.flow.dashboard.details.DetailsViewModelToViewInterface
import com.glowka.rafal.topmusic.flow.dashboard.list.ListScreenStructure
import com.glowka.rafal.topmusic.flow.dashboard.list.ListViewModelToViewInterface
import com.glowka.rafal.topmusic.flow.intro.intro.IntroScreenStructure
import com.glowka.rafal.topmusic.flow.intro.intro.IntroViewModelToViewInterface
import com.glowka.rafal.topmusic.presentation.architecture.FragmentFactory
import com.glowka.rafal.topmusic.presentation.architecture.ScreenFragment
import com.glowka.rafal.topmusic.presentation.architecture.ScreenStructure
import com.glowka.rafal.topmusic.presentation.utils.logTag

internal class IntroFragment : ScreenFragment<IntroViewModelToViewInterface.State, IntroViewModelToViewInterface.ViewEvents>(
  screenStructure = IntroScreenStructure
)

internal class ListFragment : ScreenFragment<ListViewModelToViewInterface.ViewState, ListViewModelToViewInterface.ViewEvents>(
  screenStructure = ListScreenStructure
)

internal class DetailsFragment : ScreenFragment<DetailsViewModelToViewInterface.ViewState, DetailsViewModelToViewInterface.ViewEvents>(
  screenStructure = DetailsScreenStructure
)

class FragmentFactoryImpl : FragmentFactory {
  override fun create(screenStructure: ScreenStructure<*, *, *, *>): Fragment {
    return when (screenStructure) {
      is IntroScreenStructure -> IntroFragment()
      is ListScreenStructure -> ListFragment()
      is DetailsScreenStructure -> DetailsFragment()
      else -> throw RuntimeException("Missing fragment class for ${screenStructure.logTag}")
    }
  }
}