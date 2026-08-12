package com.anggrayudi.storage.permission

/**
 * Dexter cannot display consent dialog before requesting runtime permissions, thus we create our
 * own permission request handler.
 *
 * Created on 12/13/20
 *
 * @author Anggrayudi H
 */
interface PermissionRequest {

  fun check()

  fun continueToPermissionRequest()
}
