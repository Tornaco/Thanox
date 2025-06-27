package now.fortuitous.thanos.main

import androidx.compose.runtime.staticCompositionLocalOf
import com.anggrayudi.storage.SimpleStorageHelper

val LocalSimpleStorageHelper = staticCompositionLocalOf<SimpleStorageHelper> {
    error("No LocalSimpleStorageHelper")
}

const val REQUEST_CODE_CREATE_BACKUP = 9990
const val REQUEST_CODE_CREATE_LOG = 9991

const val REQUEST_CODE_EXPORT_SF_APPS = 9992
const val REQUEST_CODE_IMPORT_SF_APPS = 9993

const val REQUEST_CODE_PICK_TRAMP_IMPORT_PATH = 9994
const val REQUEST_CODE_PICK_TRAMP_EXPORT_PATH = 9995

const val REQUEST_CODE_PICK_PROFILE_IMPORT_PATH = 9996