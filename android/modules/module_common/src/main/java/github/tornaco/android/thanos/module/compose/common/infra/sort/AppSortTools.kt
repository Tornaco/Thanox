package github.tornaco.android.thanos.module.compose.common.infra.sort

import android.content.Context
import android.os.Build
import android.text.format.Formatter
import androidx.annotation.StringRes
import github.tornaco.android.thanos.core.util.DateUtils
import github.tornaco.android.thanos.module.compose.common.infra.AppUiModel
import github.tornaco.android.thanos.res.R
import si.virag.fuzzydateformatter.FuzzyDateTimeFormatter
import java.time.Duration
import java.util.Date

enum class AppSortTools(
    @field:StringRes @param:StringRes val labelRes: Int,
    val provider: AppSorterProvider
) {
    Default(
        R.string.common_sort_by_default,
        object : AppSorterProvider {
            override fun comparator(context: Context): Comparator<AppUiModel> {
                return Comparator { obj: AppUiModel, listModel: AppUiModel ->
                    obj.appInfo.compareTo(listModel.appInfo)
                }
            }

            override fun getAppSortDescription(context: Context, model: AppUiModel): String? {
                return null
            }
        }),
    CheckState(
        R.string.enabled,
        object : AppSorterProvider {
            override fun comparator(context: Context): Comparator<AppUiModel> {
                return Comparator { obj: AppUiModel, listModel: AppUiModel ->
                    listModel.isChecked.compareTo(obj.isChecked)
                }
            }

            override fun getAppSortDescription(context: Context, model: AppUiModel): String? {
                return null
            }
        }),
    OptionState(
        R.string.nav_title_settings,
        object : AppSorterProvider {
            override fun comparator(context: Context): Comparator<AppUiModel> {
                return Comparator { obj: AppUiModel, listModel: AppUiModel ->
                    listModel.selectedOptionId.orEmpty().compareTo(obj.selectedOptionId.orEmpty())
                }
            }

            override fun getAppSortDescription(context: Context, model: AppUiModel): String? {
                return null
            }
        }),
    Running(
        R.string.chip_title_app_only_running,
        object : AppSorterProvider {
            override fun comparator(context: Context): Comparator<AppUiModel> {
                return AppRunningStateComparator(context)
            }

            override fun getAppSortDescription(context: Context, model: AppUiModel): String? {
                return null
            }
        }),
    AppLabel(
        R.string.common_sort_by_install_app_label,
        object : AppSorterProvider {
            override fun comparator(context: Context): Comparator<AppUiModel> {
                return AppLabelComparator()
            }

            override fun getAppSortDescription(context: Context, model: AppUiModel): String? {
                return null
            }
        }),
    InstallTime(
        R.string.common_sort_by_install_time,
        object : AppSorterProvider {
            override fun comparator(context: Context): Comparator<AppUiModel> {
                return AppInstallTimeComparator()
            }

            override fun getAppSortDescription(context: Context, model: AppUiModel): String {
                return DateUtils.formatLongForMessageTime(model.appInfo.firstInstallTime)
            }
        }),
    UpdateTime(
        R.string.common_sort_by_update_time,
        object : AppSorterProvider {
            override fun comparator(context: Context): Comparator<AppUiModel> {
                return AppUpdateTimeComparator()
            }

            override fun getAppSortDescription(context: Context, model: AppUiModel): String {
                return DateUtils.formatLongForMessageTime(model.appInfo.lastUpdateTime)
            }
        }),
    LastUsedTime(
        R.string.common_sort_by_last_used_time,
        object : AppSorterProvider {
            override fun comparator(context: Context): Comparator<AppUiModel> {
                return AppLastUsedTimeComparator()
            }

            override fun getAppSortDescription(context: Context, model: AppUiModel): String {
                return FuzzyDateTimeFormatter.getTimeAgo(context, Date(model.lastUsedTimeMills))
            }
        }),
    TotalUsedTime(
        R.string.common_sort_by_total_used_time,
        object : AppSorterProvider {
            override fun comparator(context: Context): Comparator<AppUiModel> {
                return AppTotalUsedTimeComparator()
            }

            override fun getAppSortDescription(context: Context, model: AppUiModel): String {
                return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    DateUtils.formatDuration(Duration.ofMillis(model.totalUsedTimeMills))
                } else DateUtils.formatLongForMessageTime(model.totalUsedTimeMills)
            }
        }),
    SdkVersion(
        R.string.common_sort_by_install_sdk_version,
        object : AppSorterProvider {
            override fun comparator(context: Context): Comparator<AppUiModel> {
                return AppSDKVersionComparator()
            }

            override fun getAppSortDescription(context: Context, model: AppUiModel): String {
                return model.appInfo.targetSdkVersion.toString()
            }
        }),
    ApkSize(
        R.string.common_sort_by_install_apk_size,
        object : AppSorterProvider {
            override fun comparator(context: Context): Comparator<AppUiModel> {
                return AppApkSizeComparator(context)
            }

            override fun getAppSortDescription(context: Context, model: AppUiModel): String {
                return Formatter.formatFileSize(
                    context,
                    AppApkSizeComparator.getAppApkSize(context, model)
                )
            }
        }),
    AppUid(
        R.string.common_sort_by_install_app_uid,
        object : AppSorterProvider {
            override fun comparator(context: Context): Comparator<AppUiModel> {
                return AppUidComparator()
            }

            override fun getAppSortDescription(context: Context, model: AppUiModel): String {
                return model.appInfo.uid.toString()
            }
        });

    fun relyOnUsageStats(): Boolean {
        return this == TotalUsedTime || this == LastUsedTime
    }

    interface AppSorterProvider {
        fun comparator(context: Context): Comparator<AppUiModel>

        fun getAppSortDescription(context: Context, model: AppUiModel): String?
    }
}