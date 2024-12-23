package github.tornaco.thanos.module.component.manager.redesign

import android.annotation.SuppressLint
import android.content.Context
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.android.thanos.core.pm.ComponentInfo
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class ReceiversVM @Inject constructor(
    @ApplicationContext context: Context,
) : ComponentsVM(context) {
    override fun ThanosManager.getComponentsInBatch(
        userId: Int,
        packageName: String,
        itemCountInEachBatch: Int,
        batchIndex: Int
    ): List<ComponentInfo>? {
        return pkgManager.getReceiversInBatch(
            userId,
            packageName,
            itemCountInEachBatch,
            batchIndex
        )
    }
}