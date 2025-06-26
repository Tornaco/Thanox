/*
 * (C) Copyright 2022 Thanox
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package now.fortuitous.thanos.sf

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.anggrayudi.storage.file.openInputStream
import com.anggrayudi.storage.file.openOutputStream
import com.elvishew.xlog.XLog
import com.google.gson.reflect.TypeToken
import com.topjohnwu.superuser.Shell
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import github.tornaco.android.thanos.common.AppLabelSearchFilter
import github.tornaco.android.thanos.core.Logger
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.android.thanos.core.pm.AppInfo
import github.tornaco.android.thanos.core.pm.PREBUILT_PACKAGE_SET_ID_3RD
import github.tornaco.android.thanos.core.pm.PREBUILT_PACKAGE_SET_ID_ALL
import github.tornaco.android.thanos.core.pm.PREBUILT_PACKAGE_SET_ID_SYSTEM
import github.tornaco.android.thanos.core.pm.PackageEnableStateChangeListener
import github.tornaco.android.thanos.core.pm.PackageSet
import github.tornaco.android.thanos.core.pm.Pkg
import github.tornaco.android.thanos.core.pm.PrebuiltPkgSets.isPrebuiltId
import github.tornaco.android.thanos.core.pm.USER_PACKAGE_SET_ID_USER_WHITELISTED
import github.tornaco.android.thanos.core.util.DateUtils
import github.tornaco.android.thanos.core.util.GsonUtils
import github.tornaco.android.thanos.core.util.PkgUtils
import github.tornaco.android.thanos.module.compose.common.infra.LifeCycleAwareViewModel
import github.tornaco.android.thanos.module.compose.common.widget.SortItem
import github.tornaco.android.thanos.support.withThanos
import github.tornaco.android.thanos.util.InstallerUtils
import github.tornaco.android.thanos.util.ToastUtils
import github.tornaco.android.thanos.util.sortByIndex
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import now.fortuitous.thanos.power.ShortcutHelper
import util.JsonFormatter
import java.io.File
import javax.inject.Inject

data class SFState(
    val isSFLoading: Boolean = false,
    val isSFMapLoading: Boolean = false,
    val isEditingMode: Boolean = false,
    val selectedApps: Set<Pkg> = emptySet(),
)

sealed interface StubApkEffect {
    data class ApkCreated(val path: File) : StubApkEffect
}

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class SFVM @Inject constructor(
    @ApplicationContext private val context: Context,
    private val savedStateHandle: SavedStateHandle,
    private val repo: SFRepoImpl
) :
    LifeCycleAwareViewModel() {
    companion object {
        const val KEY_QUERY = "query"
        const val KEY_SELECTED_PKG_SET_ID = "set"
    }

    private val logger = Logger("SFVM")

    private val _state = MutableStateFlow(
        SFState()
    )
    val state = _state.asStateFlow()

    private val _stubApkEffect = MutableSharedFlow<StubApkEffect>()
    val stubApkEffect = _stubApkEffect.asSharedFlow()

    private val searchQuery = savedStateHandle.getStateFlow(KEY_QUERY, "")
    val selectedPkgSetId: StateFlow<String?> =
        savedStateHandle.getStateFlow(KEY_SELECTED_PKG_SET_ID, null)
    private val appLabelSearchFilter by lazy { AppLabelSearchFilter() }

    val allSFPkgs by lazy {
        repo.freezePkgListFlow()
            .onStart {
                _state.update { it.copy(isSFLoading = true) }
            }
            .onEach {
                _state.update { it.copy(isSFLoading = false) }
            }
            .stateIn(viewModelScope, SharingStarted.Lazily, initialValue = emptyList())
    }

    val sfPkgMapping: StateFlow<List<AppInfo>> by lazy {
        combine(
            allSFPkgs,
            pkgSets,
            selectedPkgSetId,
            searchQuery
        ) { list, allSets, selectedId, query ->
            val selectedSet = allSets.firstOrNull { it.id == selectedId }
                ?: return@combine emptyList()
            val listOfSet = list.filter {
                selectedSet.pkgList.contains(Pkg.fromAppInfo(it))
            }

            if (query.isBlank()) {
                listOfSet
            } else {
                listOfSet.filter { appInfo ->
                    query.isEmpty() || (query.length > 2 && appInfo.pkgName.contains(
                        query
                    )) || appLabelSearchFilter.matches(query, appInfo.appLabel)
                }
            }
        }.onStart {
            // When the flow starts, set isLoading to true
            _state.update { it.copy(isSFMapLoading = true) }
        }.onEach {
            _state.update { it.copy(isSFMapLoading = false) }
        }.stateIn(viewModelScope, SharingStarted.Lazily, initialValue = emptyList())
    }

    val pkgSets by lazy {
        repo.pkgSetListFlow()
            .map { list ->
                list.filter {
                    (!it.isPrebuilt && USER_PACKAGE_SET_ID_USER_WHITELISTED != it.id) || arrayOf(
                        PREBUILT_PACKAGE_SET_ID_ALL,
                        PREBUILT_PACKAGE_SET_ID_3RD,
                        PREBUILT_PACKAGE_SET_ID_SYSTEM
                    ).contains(it.id)
                }
            }
            .onEach { allSets ->
                logger.d("pkgSets: $allSets")
                if (selectedPkgSetId.value == null) {
                    allSets.firstOrNull()?.let { selectPkgSet(it) }
                }
            }
            .stateIn(viewModelScope, SharingStarted.Lazily, initialValue = emptyList())
    }

    val isScreenOffEnabled = repo.isScreenOffEnabled()
        .stateIn(viewModelScope, SharingStarted.Lazily, initialValue = false)

    val isTaskRemovalEnabled = repo.isTaskRemovalEnabled()
        .stateIn(viewModelScope, SharingStarted.Lazily, initialValue = false)

    fun setScreenOffEnabled(enabled: Boolean) {
        viewModelScope.launch {
            repo.setScreenOffEnabled(enabled)
        }
    }

    fun setTaskRemovalEnabled(enabled: Boolean) {
        viewModelScope.launch {
            repo.setTaskRemovalEnabled(enabled)
        }
    }

    fun search(query: String) {
        savedStateHandle[KEY_QUERY] = query
    }

    fun startEdit() {
        _state.update { it.copy(isEditingMode = true) }
    }

    fun finishEdit() {
        _state.update { it.copy(isEditingMode = false, selectedApps = emptySet()) }
    }

    fun removeSelectedAppsFromSF() {
        viewModelScope.launch {
            state.value.selectedApps.forEach { pkg ->
                repo.removePkg(pkg)
                toggleSelectApp(pkg)
            }

            delayRefresh()
        }
    }

    fun removeSelectedAppsFromPkgSet() {
        viewModelScope.launch {
            selectedPkgSetId.value?.let {
                if (!it.isPrebuiltId()) {
                    repo.removePkgFromSet(
                        it,
                        state.value.selectedApps.toList()
                    )
                }
            }
            delayRefresh()
        }
    }

    fun tempUnFreezeSelectedApps() {
        viewModelScope.launch {
            context.withThanos {
                state.value.selectedApps.forEach {
                    pkgManager.setApplicationEnableState(it, true, false)
                }
                finishEdit()
                delayRefresh()
            }
        }
    }

    fun addSelectedAppsToPkgSet(id: String) {
        viewModelScope.launch {
            repo.addPkgToSet(id, state.value.selectedApps.toList())
            delayRefresh()
        }
    }

    fun addApps(list: List<AppInfo>) {
        addPkgs(list.map {
            Pkg.fromAppInfo(it)
        })
    }

    private fun addPkgs(apps: List<Pkg>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repo.addPkgs(apps)
                selectedPkgSetId.value?.let {
                    if (!it.isPrebuiltId()) {
                        repo.addPkgToSet(it, apps)
                    }
                }
            }
            delayRefresh()
        }
    }

    fun addPkgSet(label: String) {
        viewModelScope.launch {
            repo.addPkgSet(label)
            delayRefresh()
        }
    }

    fun renamePkgSet(label: String, id: String) {
        viewModelScope.launch {
            repo.renamePkgSet(id, label)
        }
    }

    fun applySort(sortItems: List<SortItem>) {
        viewModelScope.launch {
            val newSet = sortByIndex(pkgSets.value, sortItems.map { it.index })
            newSet.forEachIndexed { index, pkgSet ->
                repo.sortPkgSet(pkgSet.id, index)
            }
            delayRefresh()
        }
    }

    fun removePkgSet(id: String) {
        viewModelScope.launch {
            repo.removePkgSet(id)
        }
    }

    fun selectPkgSet(set: PackageSet) {
        savedStateHandle[KEY_SELECTED_PKG_SET_ID] = set.id
        finishEdit()
    }

    fun toggleSelectApp(app: AppInfo) {
        val pkg = Pkg.fromAppInfo(app)
        toggleSelectApp(pkg)
    }

    fun toggleSelectApp(pkg: Pkg) {
        _state.update {
            it.copy(selectedApps = it.selectedApps.toMutableSet().apply {
                if (contains(pkg)) remove(pkg) else add(pkg)
            })
        }

        if (_state.value.selectedApps.isEmpty()) {
            finishEdit()
        }
    }

    fun launchApp(pkg: Pkg) {
        viewModelScope.launch {
            context.withThanos {
                pkgManager.launchSmartFreezePkg(pkg)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        repo.update()
    }


    fun freeze() {
        viewModelScope.launch {
            context.withThanos {
                pkgManager.freezeAllSmartFreezePackages(
                    object : PackageEnableStateChangeListener() {
                        override fun onPackageEnableStateChanged(pkgs: MutableList<Pkg>?) {
                            super.onPackageEnableStateChanged(pkgs)
                            repo.update()
                        }
                    })

                delayRefresh()
            }
        }
    }

    fun refresh() {
        repo.update()
    }

    private fun delayRefresh() {
        viewModelScope.launch {
            _state.update { it.copy(isSFLoading = true) }
            delay(500)
            refresh()
        }
    }

    fun createShortcutStubApk(
        appInfo: AppInfo,
        appLabel: String,
        versionName: String,
        versionCode: Int,
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                runCatching {
                    val apkFile = ShortcutHelper.createShortcutStubApkFor(
                        context,
                        appInfo,
                        appLabel,
                        versionName,
                        versionCode
                    )
                    _stubApkEffect.emit(StubApkEffect.ApkCreated(apkFile))
                }.onFailure {
                    XLog.e(it, "createShortcutStubApkFor")
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            context,
                            it.message ?: it.toString(),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    fun requestInstallStubApk(apkFile: File, silent: Boolean) {
        if (silent) {
            val tmpFile = File("/data/local/tmp/" + apkFile.nameWithoutExtension + "_proxy.apk")
            Shell.cmd("cp " + apkFile.absolutePath + " " + tmpFile.absolutePath).exec()
            XLog.w("apk path: " + tmpFile.absolutePath)
            val installRes = Shell.cmd("pm install " + tmpFile.absolutePath).exec()
            XLog.w("Install res: $installRes")
            Shell.cmd("rm " + tmpFile.absolutePath).exec()
        } else {
            InstallerUtils.installUserAppWithIntent(context, apkFile)
        }
    }

    fun requestUnInstallStubApkIfInstalled(context: Context?, appInfo: AppInfo?) {
        XLog.v("requestUnInstallStubApkIfInstalled: %s", appInfo)
        val stubPkgName =
            ThanosManager.from(context).pkgManager.createShortcutStubPkgName(appInfo)
        XLog.v("requestUnInstallStubApkIfInstalled: %s", stubPkgName)
        if (PkgUtils.isPkgInstalled(context, stubPkgName)) {
            InstallerUtils.uninstallUserAppWithIntent(context, stubPkgName)
        }
    }

    fun selectAll() {
        val all = sfPkgMapping.value.map { Pkg.fromAppInfo(it) }
        _state.update {
            it.copy(selectedApps = all.toHashSet())
        }
    }

    fun unselectAll() {
        _state.update {
            it.copy(selectedApps = emptySet())
        }
    }

    private fun getExportPackageListContent(): String {
        return JsonFormatter.toPrettyJson(state.value.selectedApps.map { it.pkgName })
    }

    fun export(pickedFile: DocumentFile) {
        val pickedFileOS = pickedFile.openOutputStream(context)
        if (pickedFileOS == null) {
            Toast.makeText(context, "Unable to open output stream.", Toast.LENGTH_LONG).show()
            return
        }
        pickedFileOS.use {
            it.write(getExportPackageListContent().toByteArray())
        }
        ToastUtils.ok(context)
    }

    fun import(file: DocumentFile) {
        val pickedFileIS = file.openInputStream(context)
        if (pickedFileIS == null) {
            Toast.makeText(context, "Unable to open input stream.", Toast.LENGTH_LONG).show()
            return
        }
        val thanos = ThanosManager.from(context).pkgManager
        pickedFileIS.use {
            runCatching {
                val json = it.reader(Charsets.UTF_8).readText()
                val pkgs = parseJsonToPackages(json)
                addPkgs(pkgs.mapNotNull {
                    val pkg = Pkg.systemUserPkg(it)
                    val appInfo = thanos.getAppInfo(pkg)
                    if (appInfo == null) null else pkg
                })
            }.onFailure {
                Toast.makeText(context, "Unable to read file. ${it.toString()}", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun parseJsonToPackages(content: String): Set<String> {
        return GsonUtils.GSON.fromJson<Set<String>>(
            content,
            object : TypeToken<Set<String>>() {
            }.type
        )
    }
}

fun autoGenExportJsonFileName() =
    "SmartFreeze-Apps-${DateUtils.formatForFileName(System.currentTimeMillis())}"


