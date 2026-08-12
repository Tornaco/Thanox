package com.anggrayudi.storage

import android.app.Activity
import android.content.Context
import android.content.Intent

/**
 * Created on 18/08/20
 *
 * @author Anggrayudi H
 */
internal interface ComponentWrapper {

  val context: Context

  val activity: Activity

  fun startActivityForResult(intent: Intent, requestCode: Int): Boolean
}
