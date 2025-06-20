package now.fortuitous.thanos.main

import androidx.compose.runtime.staticCompositionLocalOf
import com.anggrayudi.storage.SimpleStorageHelper

val LocalSimpleStorageHelper = staticCompositionLocalOf<SimpleStorageHelper> {
    error("No LocalSimpleStorageHelper")
}