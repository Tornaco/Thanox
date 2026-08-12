package com.anggrayudi.storage

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent

/**
 * Created on 18/08/20
 *
 * @author Anggrayudi H
 */
internal class ActivityWrapper(private val _activity: Activity) : ComponentWrapper {

  override val context: Context
    get() = _activity

  override val activity: Activity
    get() = _activity

  override fun startActivityForResult(intent: Intent, requestCode: Int): Boolean {
    return try {
      _activity.startActivityForResult(intent, requestCode)
      true
    } catch (_: ActivityNotFoundException) {
      false
    }
  }
}
