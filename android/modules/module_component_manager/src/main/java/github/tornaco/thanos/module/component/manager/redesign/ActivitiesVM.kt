package github.tornaco.thanos.module.component.manager.redesign

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import github.tornaco.android.thanos.core.pm.AppInfo
import github.tornaco.thanos.module.component.manager.getActivityRule
import github.tornaco.thanos.module.component.manager.model.ComponentModel
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class ActivitiesVM @Inject constructor(
    @ApplicationContext context: Context,
) : ComponentsVM(context) {
    override fun loadComponents(appInfo: AppInfo, query: String): List<ComponentModel> {
        val res: MutableList<ComponentModel> = ArrayList()
        for (i in 0 until Int.MAX_VALUE) {
            val batch =
                thanox.pkgManager.getActivitiesInBatch(
                    appInfo.userId,
                    appInfo.pkgName,
                    20,
                    i
                ) ?: break
            batch
                .filter {
                    TextUtils.isEmpty(query) || it.name.lowercase().contains(query.lowercase())
                }
                .forEach { activityInfo ->
                    res.add(
                        ComponentModel.builder()
                            .name(activityInfo.name)
                            .componentName(activityInfo.componentName)
                            .isDisabledByThanox(activityInfo.isDisabledByThanox)
                            .label(activityInfo.label)
                            .componentObject(activityInfo)
                            .enableSetting(activityInfo.enableSetting)
                            .componentRule(getActivityRule(activityInfo.componentName))
                            .build()
                    )
                }
        }
        res.sort()
        return res
    }
}