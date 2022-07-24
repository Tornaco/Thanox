package github.tornaco.thanos.module.component.manager

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.ViewModelProviders
import github.tornaco.android.thanos.core.pm.AppInfo
import github.tornaco.android.thanos.util.ActivityUtils
import github.tornaco.thanos.module.component.manager.model.ComponentModel

class ServiceListActivity : ComponentListActivity() {

    companion object {
        @JvmStatic
        fun start(context: Context, appInfo: AppInfo) {
            val data = Bundle()
            data.putParcelable("app", appInfo)
            ActivityUtils.startActivity(context, ServiceListActivity::class.java, data)
        }
    }

    override fun obtainViewModel(activity: FragmentActivity): ComponentListViewModel {
        val factory = AndroidViewModelFactory
            .getInstance(activity.application)
        return ViewModelProviders.of(activity, factory)
            .get<ServiceListViewModel>(ServiceListViewModel::class.java)
    }

    override fun onCreateItemPopupMenu(anchor: View, componentModel: ComponentModel): PopupMenu {
        val popupMenu = PopupMenu(thisActivity(), anchor)
        popupMenu.inflate(R.menu.module_component_manager_component_service_item_menu)
        popupMenu.setOnMenuItemClickListener(onCreateItemOnMenuItemClickListener(componentModel))
        return popupMenu
    }

    override fun onCreateItemOnMenuItemClickListener(componentModel: ComponentModel): PopupMenu.OnMenuItemClickListener {
        val def = super.onCreateItemOnMenuItemClickListener(componentModel)
        return PopupMenu.OnMenuItemClickListener { item ->
            if (item!!.itemId == R.id.action_standby_keep) {
                AddToSmartStandByKeepsVarDialog(thisActivity(), componentModel.componentName).show()
                true
            } else def.onMenuItemClick(item)
        }
    }
}