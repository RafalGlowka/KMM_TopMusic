package com.glowka.rafal.topmusic.presentation.architecture

import platform.UIKit.UIViewController

interface UIViewFactory {
  fun createViewController(screenStructure: ScreenStructure<*, *, *, *>): UIViewController?
}
