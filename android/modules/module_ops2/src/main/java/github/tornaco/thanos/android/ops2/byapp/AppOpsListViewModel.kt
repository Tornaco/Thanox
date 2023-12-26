package github.tornaco.thanos.android.ops2.byapp

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elvishew.xlog.XLog
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.android.thanos.core.ops.OpsManager
import github.tornaco.android.thanos.core.ops.PermInfo
import github.tornaco.android.thanos.core.ops.PermState
import github.tornaco.android.thanos.core.pm.AppInfo
import github.tornaco.android.thanos.core.pm.Pkg
import github.tornaco.android.thanos.core.util.PkgUtils
import github.tornaco.thanos.android.ops2.R
import github.tornaco.thanos.android.ops2.byop.opLabel
import github.tornaco.thanos.android.ops2.byop.opSummary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class OpItem(
    val code: Int,
    val label: String,
    val description: String,
    val iconRes: Int,
    val permInfo: PermInfo
)

data class OpsState(
    val isLoading: Boolean,
    val opsItems: List<OpItem>
)

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class AppOpsListViewModel @Inject constructor(@ApplicationContext private val context: Context) :
    ViewModel() {

    private val _state =
        MutableStateFlow(
            OpsState(
                isLoading = true,
                opsItems = emptyList()
            )
        )
    val state = _state.asStateFlow()

    private val thanos by lazy { ThanosManager.from(context) }
    private val opsManager by lazy { thanos.opsManager }

    fun refresh(appInfo: AppInfo) {
        _state.value = _state.value.copy(isLoading = true)
        viewModelScope.launch {
            val array = context.resources.obtainTypedArray(R.array.module_ops2_op_icon)
            val opItems = withContext(Dispatchers.IO) {
                val permissions = PkgUtils.getAllDeclaredPermissions(context, appInfo.pkgName)
                (0 until opsManager.opNum).filter { OpsManager.isOpSupported(it) }
                    .filter { opsManager.opToSwitch(it) == it }
                    .filter {
                        val perm = opsManager.opToPermission(it)
                        val show = (perm == null || permissions.contains(perm)
                                || appInfo.isDummy)
                        show
                    }
                    .mapNotNull { code ->
                        val permInfo = opsManager.getPackagePermInfo(
                            code,
                            Pkg.fromAppInfo(appInfo)
                        )
                        permInfo?.let {
                            OpItem(
                                code = code,
                                label = opLabel(context, code, opsManager.opToName(code)),
                                description = opSummary(
                                    context,
                                    code,
                                    opsManager.opToName(code)
                                ),
                                permInfo = permInfo,
                                iconRes = kotlin.runCatching {
                                    array.getResourceId(
                                        code,
                                        github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_settings_2_fill /* fallback */
                                    )
                                }.getOrElse {
                                    github.tornaco.android.thanos.icon.remix.R.drawable.ic_remix_settings_2_fill
                                }.also {
                                    XLog.d("Res: $it")
                                }
                            )
                        }
                    }
            }
            array.recycle()
            _state.value = _state.value.copy(opsItems = opItems, isLoading = false)
        }
    }

    fun setMode(appInfo: AppInfo, code: Int, mode: PermState) {
        opsManager.setMode(code, Pkg.fromAppInfo(appInfo), mode.name)
        refresh(appInfo)
    }
}