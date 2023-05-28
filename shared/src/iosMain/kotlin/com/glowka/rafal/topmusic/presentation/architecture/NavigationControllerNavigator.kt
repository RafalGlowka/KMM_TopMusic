package com.glowka.rafal.topmusic.presentation.architecture

import androidx.compose.ui.window.ComposeUIViewController
import co.touchlab.kermit.Logger
import org.koin.core.scope.Scope
import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.UIKit.UIModalPresentationFormSheet
import platform.UIKit.UIModalPresentationPageSheet
import platform.UIKit.UINavigationController
import platform.UIKit.UIViewController
import platform.UIKit.navigationItem

internal data class ScreenStackItem(val screen: Screen<*, *>, val viewController: UIViewController)

internal class ScreenStack {

  private val stack = ArrayDeque<ScreenStackItem>()

  fun push(screen: Screen<*, *>, viewController: UIViewController) {
    stack.addLast(ScreenStackItem(screen, viewController))
  }

  fun pop(): UIViewController? {
    return stack.removeLastOrNull()?.viewController
  }

  fun popTo(screen: Screen<*, *>): UIViewController? {
    val lastIndex = stack.indexOfLast { item -> item.screen == screen }
    if (lastIndex == -1) {
      return null
    }
    if (lastIndex < stack.lastIndex) {
      for (i in stack.lastIndex downTo lastIndex + 1) {
        stack.removeAt(i)
      }
    }
    return stack[lastIndex].viewController
  }

  fun getStackString(): String {
    return buildString {
      stack.forEach { item ->
        append("${item.screen.flowScopeName}/${item.screen.screenTag}\n")
      }
    }
  }

  val size = stack.size
}

class NavigationControllerNavigatorImpl(
  private val controller: UINavigationController,
  private val viewFactory: UIViewFactory,
) : ScreenNavigator {

  private val log = Logger.withTag("Navigator")
  private val screenStack = ScreenStack()

  init {
    controller.navigationBarHidden = true
  }

  override fun <INPUT : ScreenInput, OUTPUT : ScreenOutput> replace(
      scope: Scope,
      screen: Screen<INPUT, OUTPUT>,
      onShowInput: INPUT?,
      onScreenOutput: (OUTPUT) -> Unit
  ) {
    log.d("replace ${screen.flowScopeName}/${screen.screenTag}")
    initFlowDestination(scope, FlowDestination(screen = screen, param = onShowInput), onScreenOutput)

    val viewController = getViewController(scope, screen.screenTag, screen.screenStructure)
    screenStack.pop()
    screenStack.push(screen, viewController)
    if (screenStack.size > 1) {
      controller.popViewControllerAnimated(false)
      controller.pushViewController(viewController = viewController, animated = true)
      log.d("pop and push view")
    } else {
      log.d("replace view")
      controller.setViewControllers(listOf(viewController), animated = true)
    }
  }

  override fun <INPUT : ScreenInput, OUTPUT : ScreenOutput> push(
    scope: Scope,
    screen: Screen<INPUT, OUTPUT>,
    onShowInput: INPUT?,
    onScreenOutput: (OUTPUT) -> Unit
  ) {
    log.d("push ${screen.flowScopeName}/${screen.screenTag}")
    initFlowDestination(scope, FlowDestination(screen = screen, param = onShowInput), onScreenOutput)

    val viewController = getViewController(scope, screen.screenTag, screen.screenStructure)
    screenStack.push(screen, viewController)

    controller.pushViewController(viewController = viewController, animated = true)
  }

  private fun <VIEWSTATE : ViewState, VIEWEVENT : ViewEvent> getViewController(
    scope: Scope,
    screenTag: String,
    screenStructure: ScreenStructure<*, *, VIEWSTATE, VIEWEVENT>
  ): UIViewController {
    val viewModel = getViewModelToView(scope = scope, screenTag = screenTag, screenStructure = screenStructure)
    val nativeView = viewFactory.createViewController(screenStructure)
//    log.d(" $screenStructure $nativeView")

    val viewController = nativeView ?: ComposeUIViewController(
      content = {
        screenStructure.content(viewModel)
      },
    )
//    viewController.navigationItem.hidesBackButton = true
    return viewController
  }

  override fun popBack(screen: Screen<*, *>) {
    log.d("popBack ${screen.flowScopeName}/${screen.screenTag}")
    makePopBackTo(screen)
    controller.popViewControllerAnimated(animated = true)
  }

  override fun popBackTo(screen: Screen<*, *>) {
    log.d("popBackTo ${screen.flowScopeName}/${screen.screenTag}")
    makePopBackTo(screen)
  }

  private fun makePopBackTo(screen: Screen<*, *>) {
    val viewControllerOfScreen = screenStack.popTo(screen = screen)
    viewControllerOfScreen?.let { viewController ->
      controller.popToViewController(viewController = viewController, animated = true)
    } ?: log.e("${screen.flowScopeName}/${screen.screenTag} not found ${screenStack.getStackString()}")
  }

  override fun <INPUT : ScreenInput, OUTPUT : ScreenOutput> showDialog(
    scope: Scope,
    screen: ScreenDialog<INPUT, OUTPUT>,
    onShowInput: INPUT?,
    onScreenOutput: (OUTPUT) -> Unit
  ) {
    log.d("showDialog ${screen.flowScopeName}/${screen.screenTag}")
    initFlowDestination(
      scope = scope,
      flowDestination = FlowDialogDestination(screen = screen, param = onShowInput),
      onScreenOUTPUT = onScreenOutput
    )

    makeShow(scope, screen.screenTag, screen.screenStructure)
  }

  private fun <VIEWSTATE : ViewState, VIEWEVENT : ViewEvent> makeShow(
    scope: Scope,
//    screen: ScreenDialog<*, *>,
    screenTag: String,
    screenStructure: ScreenDialogStructure<*, *, VIEWSTATE, VIEWEVENT>
  ) {
    val viewModel = getViewModelToView(scope = scope, screenTag = screenTag, screenStructure = screenStructure)
    val viewController = ComposeUIViewController(
      content = {
        screenStructure.content(viewModel)
      },
    )
    viewController.navigationItem.hidesBackButton = true

//    screenStack.push(screen, viewController)
    viewController.modalPresentationStyle = when (screenStructure.type) {
      DialogType.BOTTOM -> UIModalPresentationFormSheet
      DialogType.CENTER -> UIModalPresentationPageSheet
    }
    controller.presentModalViewController(modalViewController = viewController, animated = true)
  }

  override fun hideDialog(screenDialog: ScreenDialog<*, *>) {
    log.d("hideDialog ${screenDialog.flowScopeName}/${screenDialog.screenTag}")
    controller.dismissModalViewControllerAnimated(animated = true)
  }

  override fun openWebUrl(url: String) {
    UIApplication.sharedApplication.openURL(NSURL(string = url))
  }
}
