package com.glowka.rafal.topmusic.presentation.architecture

import co.touchlab.kermit.Logger
import com.glowka.rafal.topmusic.di.getGlobalKoin
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.dsl.ScopeDSL

/**
 *
 * Business flow abstraction. Business flow contains few screens and a separate DI scope.
 *
 */

fun Flow<*, *>.createScope(): Scope {
  return getGlobalKoin().getOrCreateScope(flowScopeName, named(flowScopeName))
}

fun Flow<*, *>.closeScope() {
  val scope = getGlobalKoin().getScopeOrNull(flowScopeName)
  if (scope == null) {
    Logger.e("scope $flowScopeName do not exist !")
    return
  }
  scope.close()
}

@Suppress("NOTHING_TO_INLINE")
inline fun Module.businessFlow(
  scopeName: String,
  noinline scopeSet: ScopeDSL.() -> Unit
) {
  scope(
    named(scopeName),
    scopeSet
  )
}
