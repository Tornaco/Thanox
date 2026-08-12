@file:Suppress("EXPERIMENTAL_API_USAGE")

package com.anggrayudi.storage.extension

import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine

/**
 * @author Anggrayudi Hardiannico A. (anggrayudi.hardiannico@dana.id)
 * @version CoroutineExt.kt, v 0.0.1 04/04/20 18.26 by Anggrayudi Hardiannico A.
 */
@Suppress("OPT_IN_USAGE")
fun startCoroutineTimer(
  delayMillis: Long = 0,
  repeatMillis: Long = 0,
  runActionOnUiThread: Boolean = false,
  action: () -> Unit,
) =
  GlobalScope.launch {
    delay(delayMillis)
    if (repeatMillis > 0) {
      while (true) {
        if (runActionOnUiThread) {
          launchOnUiThread { action() }
        } else {
          action()
        }
        delay(repeatMillis)
      }
    } else {
      if (runActionOnUiThread) {
        launchOnUiThread { action() }
      } else {
        action()
      }
    }
  }

@Suppress("OPT_IN_USAGE")
fun launchOnUiThread(action: suspend CoroutineScope.() -> Unit) =
  GlobalScope.launch(Dispatchers.Main, block = action)

inline fun <R> awaitUiResultWithPending(
  uiScope: CoroutineScope,
  crossinline action: (CancellableContinuation<R>) -> Unit,
): R {
  return runBlocking {
    suspendCancellableCoroutine { uiScope.launch(Dispatchers.Main) { action(it) } }
  }
}

inline fun <R> awaitUiResult(uiScope: CoroutineScope, crossinline action: () -> R): R {
  return runBlocking {
    suspendCancellableCoroutine {
      uiScope.launch(Dispatchers.Main) { it.resumeWith(Result.success(action())) }
    }
  }
}

inline fun CoroutineScope.postToUi(crossinline action: () -> Unit) {
  launch(Dispatchers.Main) { action() }
}

suspend fun <E> SendChannel<E>.sendAndClose(element: E) {
  send(element)
  close()
}

suspend inline fun <E> SendChannel<E>.sendAll(flow: Flow<E>) {
  flow.collect { send(it) }
  close()
}
