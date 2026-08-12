package com.anggrayudi.storage

import android.app.Activity
import android.os.Bundle

internal class EmptyActivity : Activity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setResult(RESULT_OK, intent)
    finish()
  }
}
