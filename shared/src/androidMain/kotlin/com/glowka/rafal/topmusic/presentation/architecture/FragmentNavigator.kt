package com.glowka.rafal.topmusic.presentation.architecture

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import co.touchlab.kermit.Logger
import org.koin.core.scope.Scope
import java.lang.StringBuilder

interface FragmentActivityAttachment {
  fun attach(fm: FragmentActivity)
  fun detach()
}

interface FragmentNavigator : FragmentActivityAttachment, ScreenNavigator

@Suppress("LargeClass", "TooManyFunctions")
class FragmentNavigatorImpl(
  private val containerId: Int,
  private val fragmentFactory: FragmentFactory
) : FragmentNavigator {

  private val log = Logger.withTag("FragmentNavigator")
  private var fragmentActivity: FragmentActivity? = null
  private var waitingOperation: (() -> Unit)? = null

  override fun attach(fm: FragmentActivity) {
    log.d("attach: $fm")
    fragmentActivity = fm
    waitingOperation?.invoke()
    waitingOperation = null
    log.d("attached: $fragmentActivity")
  }

  override fun detach() {
    fragmentActivity = null
    log.d("detach $fragmentActivity")
  }

  override fun <INPUT : ScreenInput, OUTPUT : ScreenOutput> replace(
    scope: Scope,
    screen: Screen<INPUT, OUTPUT>,
    onShowInput: INPUT?,
    onScreenOutput: (OUTPUT) -> Unit
  ) = showFragment(scope, screen, onShowInput, onScreenOutput, false)

  override fun <INPUT : ScreenInput, OUTPUT : ScreenOutput> push(
    scope: Scope,
    screen: Screen<INPUT, OUTPUT>,
    onShowInput: INPUT?,
    onScreenOutput: (OUTPUT) -> Unit
  ) = showFragment(scope, screen, onShowInput, onScreenOutput, true)

  private fun <INPUT : ScreenInput, OUTPUT : ScreenOutput> showFragment(
    scope: Scope,
    screen: Screen<INPUT, OUTPUT>,
    param: INPUT?,
    onScreenOutput: (OUTPUT) -> Unit,
    addToBackStack: Boolean
  ) {
    initFlowDestination(scope, FlowDestination(screen = screen, param = param), onScreenOutput)

    val fragmentTag = screen.screenTag
    val fm = fragmentActivity
    if (fm == null) {
      log.d("push waiting for attach")
      waitingOperation = {
        push(
          scope = scope,
          screen = screen,
          onShowInput = param,
          onScreenOutput = onScreenOutput
        )
      }
    } else {
      log.d("pushing transaction ${fm.supportFragmentManager.backStackToString()}")
      if (!addToBackStack) fm.supportFragmentManager.popBackStack()
      fm.supportFragmentManager.commit {
        val arguments = Bundle().apply {
          putString(BaseFragment.ARG_SCOPE, screen.flowScopeName)
          putString(BaseFragment.ARG_SCREEN_TAG, fragmentTag)
        }
        val fragment = fragmentFactory.create(screen.screenStructure)
        fragment.arguments = arguments
        replace(containerId, fragment, fragmentTag)
        addToBackStack(fragmentTag)
      }
    }
  }

  override fun popBack(screen: Screen<*, *>) {
    val fm = fragmentActivity
    if (fm == null) {
      log.d("popback waiting for attach")
      waitingOperation = {
        popBack(screen = screen)
      }
    } else {
      log.d("popback ${screen.screenTag} ${fm.supportFragmentManager.backStackToString()}")
      fm.supportFragmentManager.popBackStack(
        screen.screenTag,
        FragmentManager.POP_BACK_STACK_INCLUSIVE
      )
    }
  }

  override fun popBackTo(screen: Screen<*, *>) {
    val fm = fragmentActivity
    if (fm == null) {
      log.d("popbackto waiting for attach")
      waitingOperation = {
        popBackTo(screen = screen)
      }
    } else {
      log.d("popbackto ${screen.screenTag} ${fm.supportFragmentManager.backStackToString()}")
      fm.supportFragmentManager.popBackStack(screen.screenTag, 0)
    }
  }

  override fun <INPUT : ScreenInput, OUTPUT : ScreenOutput> showDialog(
    scope: Scope,
    screenDialog: ScreenDialog<INPUT, OUTPUT>,
    onShowInput: INPUT?,
    onScreenOutput: (OUTPUT) -> Unit
  ) {
    initFlowDestination(
      scope = scope,
      flowDestination = FlowDialogDestination(screen = screenDialog, param = onShowInput),
      onScreenOUTPUT = onScreenOutput,
    )

    val fragmentTag = screenDialog.screenTag
    val fm = fragmentActivity
    if (fm == null) {
      log.d("showDialog waiting for attach")
      waitingOperation = {
        showDialog(
          scope = scope,
          screenDialog = screenDialog,
          onShowInput = onShowInput,
          onScreenOutput = onScreenOutput
        )
      }
    } else {
      fm.supportFragmentManager.commit {
        val arguments = Bundle().apply {
          putString(BaseFragment.ARG_SCOPE, screenDialog.flowScopeName)
          putString(BaseFragment.ARG_SCREEN_TAG, fragmentTag)
        }
        val dialogFragment = when (screenDialog.screenStructure.type) {
          DialogType.CENTER -> ScreenDialogCenterFragment(screenStructure = screenDialog.screenStructure)
          DialogType.BOTTOM -> ScreenDialogBottomFragment(screenStructure = screenDialog.screenStructure)
        }
        dialogFragment.arguments = arguments

        add(dialogFragment, fragmentTag)
      }
    }
  }

  override fun hideDialog(screenDialog: ScreenDialog<*, *>) {
    val fm = fragmentActivity
    if (fm == null) {
      log.d("hideDialog waiting for attach")
      waitingOperation = {
        hideDialog(screenDialog = screenDialog)
      }
    } else {
      val dialogFragment = fm.supportFragmentManager.fragments.getScreenDialogFragment(
        scopeName = screenDialog.flowScopeName,
        screenTag = screenDialog.screenTag

      )
      dialogFragment?.dismiss()
    }
  }

  override fun openWebUrl(url: String) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(url)
    startActivity(intent)
  }

  private fun startActivity(intent: Intent) {
    val fm = fragmentActivity
    if (fm == null) {
      waitingOperation = {
        startActivity(intent)
      }
    } else {
      fm.startActivity(intent)
    }
  }
}

/**
 * Find the proper fragment in the stack.
 */
fun List<Fragment>.getScreenDialogFragment(scopeName: String, screenTag: String): DialogFragment? =
  firstOrNull { fragment ->
    fragment is DialogFragment &&
        fragment.arguments?.getString(BaseDialogFragment.ARG_SCOPE) == scopeName &&
        fragment.arguments?.getString(BaseDialogFragment.ARG_SCREEN_TAG) == screenTag
  } as? DialogFragment

private fun FragmentManager.backStackToString(): String {
  val result = StringBuilder()
  if (backStackEntryCount > 0) {
    for (i in 0 until backStackEntryCount) {
      result.append(getBackStackEntryAt(i).name).append(" ,")
    }
  }
  return result.toString()
}