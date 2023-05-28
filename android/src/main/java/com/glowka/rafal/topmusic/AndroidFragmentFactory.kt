package com.glowka.rafal.topmusic

import androidx.fragment.app.Fragment
import com.glowka.rafal.topmusic.flow.intro.intro.IntroScreenStructure
import com.glowka.rafal.topmusic.presentation.FragmentFactoryImpl
import com.glowka.rafal.topmusic.presentation.architecture.FragmentFactory
import com.glowka.rafal.topmusic.presentation.architecture.ScreenStructure
import com.glowka.rafal.topmusic.presentation.flow.intro.AndroidIntroFragment

class AndroidFragmentFactory(val baseFactory: FragmentFactoryImpl) : FragmentFactory {
    override fun create(screenStructure: ScreenStructure<*, *, *, *>): Fragment {
        return when (screenStructure) {
            IntroScreenStructure -> AndroidIntroFragment()
            else -> baseFactory.create(screenStructure)
        }
    }
}