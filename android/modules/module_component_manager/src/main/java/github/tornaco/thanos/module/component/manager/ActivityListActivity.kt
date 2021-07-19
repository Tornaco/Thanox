package github.tornaco.thanos.module.component.manager

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.ViewModelProviders
import github.tornaco.android.thanos.core.pm.AppInfo
import github.tornaco.android.thanos.util.ActivityUtils

class ActivityListActivity : ComponentListActivity() {

    companion object {
        @JvmStatic
        fun start(context: Context, appInfo: AppInfo) {
            val data = Bundle()
            data.putParcelable("app", appInfo)
            ActivityUtils.startActivity(context, ActivityListActivity::class.java, data)
        }
    }

    override fun obtainViewModel(activity: FragmentActivity): ComponentListViewModel {
        val factory = AndroidViewModelFactory
            .getInstance(activity.application)
        return ViewModelProviders.of(activity, factory)
            .get<ActivityListViewModel>(ActivityListViewModel::class.java)
    }
}