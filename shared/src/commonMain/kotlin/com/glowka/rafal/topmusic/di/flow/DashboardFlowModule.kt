package com.glowka.rafal.topmusic.di.flow

import com.glowka.rafal.topmusic.flow.dashboard.DashboardFlow
import com.glowka.rafal.topmusic.flow.dashboard.DashboardFlowImpl
import com.glowka.rafal.topmusic.presentation.architecture.businessFlow
import com.glowka.rafal.topmusic.presentation.architecture.screen
import com.glowka.rafal.topmusic.presentation.architecture.screenDialog
import com.glowka.rafal.topmusic.presentation.formatter.ReleaseDateFormatter
import com.glowka.rafal.topmusic.presentation.formatter.ReleaseDateFormatterImpl
import org.koin.dsl.module

val dashboardFeatureModule = module {

  single<DashboardFlow> {
    DashboardFlowImpl()
  }

  businessFlow(
    scopeName = DashboardFlow.SCOPE_NAME,
  ) {
    screen(screen = DashboardFlow.Screens.List)
    screen(screen = DashboardFlow.Screens.Details)
    screenDialog(screen = DashboardFlow.ScreenDialogs.Country)
  }

  single<ReleaseDateFormatter> {
    ReleaseDateFormatterImpl()
  }
}