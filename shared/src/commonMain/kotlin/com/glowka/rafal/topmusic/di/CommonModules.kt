package com.glowka.rafal.topmusic.di

import com.glowka.rafal.topmusic.di.flow.dashboardFeatureModule
import com.glowka.rafal.topmusic.di.flow.introFeatureModule
import org.koin.core.module.Module

val commonModules = listOf<Module>(
  dataModule,
  musicModule,
  introFeatureModule,
  dashboardFeatureModule,
)