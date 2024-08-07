package github.tornaco.android.thanos

import android.content.Context
import java.io.File

// Note: Should align with MultipleModulesApp.initLog
val Context.logFolderPath get() = externalCacheDir.toString() + File.separator + "logs"