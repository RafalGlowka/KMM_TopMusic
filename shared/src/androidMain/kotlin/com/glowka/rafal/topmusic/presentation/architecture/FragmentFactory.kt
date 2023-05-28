package com.glowka.rafal.topmusic.presentation.architecture

import androidx.fragment.app.Fragment

interface FragmentFactory {
  fun create(screenStructure: ScreenStructure<*, *, *, *>): Fragment
}