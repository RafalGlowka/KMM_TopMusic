package com.glowka.rafal.topmusic.modules

import com.glowka.rafal.topmusic.AndroidFragmentFactory
import com.glowka.rafal.topmusic.R
import com.glowka.rafal.topmusic.presentation.FragmentFactoryImpl
import com.glowka.rafal.topmusic.presentation.architecture.FragmentFactory
import com.glowka.rafal.topmusic.presentation.architecture.FragmentNavigator
import com.glowka.rafal.topmusic.presentation.architecture.FragmentNavigatorImpl
import org.koin.dsl.module

val androidModule = module {
    single<FragmentFactory> {
        AndroidFragmentFactory(FragmentFactoryImpl())
    }

    single<FragmentNavigator> {
        FragmentNavigatorImpl(
            containerId = R.id.fragment_container,
            fragmentFactory = get()
        )
    }
}