@file:JvmName("ContextUtils")

package com.anggrayudi.storage.extension

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.documentfile.provider.DocumentFile

/**
 * Created on 17/08/20
 *
 * @author Anggrayudi H
 */
fun Context.getAppDirectory(type: String? = null) = "${getExternalFilesDir(type)}"

fun Context.startActivitySafely(intent: Intent) {
  try {
    startActivity(intent)
  } catch (_: ActivityNotFoundException) {
    // ignore
  }
}

fun Activity.startActivityForResultSafely(requestCode: Int, intent: Intent) {
  try {
    startActivityForResult(intent, requestCode)
  } catch (_: ActivityNotFoundException) {
    // ignore
  }
}

fun Context.unregisterReceiverSafely(receiver: BroadcastReceiver?) {
  try {
    unregisterReceiver(receiver ?: return)
  } catch (_: IllegalArgumentException) {
    // ignore
  }
}

fun Context.fromTreeUri(fileUri: Uri) =
  try {
    DocumentFile.fromTreeUri(this, fileUri)
  } catch (_: Exception) {
    null
  }

fun Context.fromSingleUri(fileUri: Uri) =
  try {
    DocumentFile.fromSingleUri(this, fileUri)
  } catch (e: Exception) {
    Log.e("tornaco", "fromSingleUri ${e.stackTraceToString()}")
    null
  }
