package com.anggrayudi.storage.permission

import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

/**
 * Created on 12/13/20
 *
 * @author Anggrayudi H
 */
class FragmentPermissionRequest
private constructor(
  private val fragment: Fragment,
  private val permissions: Array<String>,
  private val options: ActivityOptionsCompat?,
  private val callback: PermissionCallback,
) : PermissionRequest {

  private val launcher =
    fragment.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
      onRequestPermissionsResult(it)
    }

  override fun check() {
    val context = fragment.requireContext()
    if (
      permissions.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
      }
    ) {
      callback.onPermissionsChecked(
        PermissionResult(
          permissions.map { PermissionReport(it, isGranted = true, deniedPermanently = false) }
        ),
        false,
      )
    } else {
      callback.onDisplayConsentDialog(this)
    }
  }

  private fun onRequestPermissionsResult(result: Map<String, Boolean>) {
    if (result.isEmpty()) {
      callback.onPermissionRequestInterrupted(permissions)
      return
    }
    val activity = fragment.requireActivity()
    val reports =
      result.map {
        PermissionReport(
          it.key,
          it.value,
          !it.value && !ActivityCompat.shouldShowRequestPermissionRationale(activity, it.key),
        )
      }
    val blockedPermissions = reports.filter { it.deniedPermanently }
    if (blockedPermissions.isEmpty()) {
      callback.onPermissionsChecked(PermissionResult(reports), true)
    } else {
      callback.onShouldRedirectToSystemSettings(blockedPermissions)
    }
  }

  /**
   * If you override [PermissionCallback.onDisplayConsentDialog], then call this method in the
   * `onPositive` callback of the dialog.
   */
  override fun continueToPermissionRequest() {
    val activity = fragment.requireActivity()
    permissions.forEach {
      if (ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED) {
        launcher.launch(permissions, options)
        return
      }
    }
    callback.onPermissionsChecked(
      PermissionResult(
        permissions.map { PermissionReport(it, isGranted = true, deniedPermanently = false) }
      ),
      false,
    )
  }

  class Builder(private val fragment: Fragment) {

    private var permissions = emptySet<String>()

    private var callback: PermissionCallback? = null

    private var options: ActivityOptionsCompat? = null

    fun withPermissions(vararg permissions: String) = apply {
      this.permissions = permissions.toSet()
    }

    fun withCallback(callback: PermissionCallback) = apply { this.callback = callback }

    fun withActivityOptions(options: ActivityOptionsCompat?) = apply { this.options = options }

    fun build() =
      FragmentPermissionRequest(fragment, permissions.toTypedArray(), options, callback!!)

    fun check() = build().check()
  }
}
