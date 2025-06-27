package github.tornaco.android.thanox.module.activity.trampoline

import android.widget.Toast
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.application
import com.anggrayudi.storage.file.openInputStream
import com.anggrayudi.storage.file.openOutputStream
import com.elvishew.xlog.XLog
import github.tornaco.android.thanos.core.app.component.ComponentReplacement
import github.tornaco.android.thanos.support.withThanos
import github.tornaco.android.thanos.util.ToastUtils
import util.JsonFormatter

fun TrampolineViewModel.helperExportToFile(
    pickedFile: DocumentFile,
    componentReplacementKey: String?,
    replacements: List<ActivityTrampolineModel>
) {
    val pickedFileOS = pickedFile.openOutputStream(application)
    if (pickedFileOS == null) {
        Toast.makeText(application, "Unable to open output stream.", Toast.LENGTH_LONG).show()
        return
    }

    XLog.d("exportToFile: %s", componentReplacementKey)
    runCatching {
        val componentReplacements = mutableListOf<ComponentReplacement>()
        for (model in replacements) {
            if (componentReplacementKey == null
                || componentReplacementKey == model.replacement.from.flattenToString()
            ) {
                componentReplacements.add(model.replacement)
            }
        }
        val contentToWrite = JsonFormatter.toPrettyJson(componentReplacements)
        pickedFileOS.use {
            it.write(contentToWrite.toByteArray())
        }
        ToastUtils.ok(application)
    }.onFailure {
        ToastUtils.nook(application, it.toString())
    }
}

fun TrampolineViewModel.helperImportFromFile(pickedFile: DocumentFile) {
    val pickedFileIS = pickedFile.openInputStream(application)
    if (pickedFileIS == null) {
        Toast.makeText(application, "Unable to open input stream.", Toast.LENGTH_LONG).show()
        return
    }
    val componentReplacements = parseJson(pickedFileIS.reader().readText())
    if (!componentReplacements.isNullOrEmpty()) {
        application.withThanos {
            componentReplacements.forEach {
                activityStackSupervisor.addComponentReplacement(it)
            }
        }
        ToastUtils.ok(application)
        start()
    } else {
        ToastUtils.nook(application, "No component replacements found.")
    }
}
