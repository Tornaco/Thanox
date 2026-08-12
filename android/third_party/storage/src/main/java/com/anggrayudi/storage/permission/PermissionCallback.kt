package com.anggrayudi.storage.permission

/**
 * Created on 12/13/20
 *
 * @author Anggrayudi H
 */
interface PermissionCallback {

  fun onDisplayConsentDialog(request: PermissionRequest) {
    request.continueToPermissionRequest()
  }

  /** @param fromSystemDialog true if users agreed/denied the permission from the system dialog. */
  fun onPermissionsChecked(result: PermissionResult, fromSystemDialog: Boolean)

  fun onShouldRedirectToSystemSettings(blockedPermissions: List<PermissionReport>) {
    // default implementation
  }

  /** Triggered when you request another permission when a permission request dialog is showing. */
  fun onPermissionRequestInterrupted(permissions: Array<String>) {
    // default implementation
  }
}
