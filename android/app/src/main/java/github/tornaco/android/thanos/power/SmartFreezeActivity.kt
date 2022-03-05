package github.tornaco.android.thanos.power

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import github.tornaco.android.thanos.BaseFragment
import github.tornaco.android.thanos.R
import github.tornaco.android.thanos.core.pm.PackageSet
import github.tornaco.android.thanos.databinding.SmartFreezeLayoutBottomNavBinding
import github.tornaco.android.thanos.pref.AppPreference
import github.tornaco.android.thanos.theme.ThemeActivity
import github.tornaco.android.thanos.util.ActivityUtils

class SmartFreezeActivity : ThemeActivity() {
    private val viewModel: SmartFreezeBottomNavViewModel by viewModels()

    companion object Starter {
        fun start(context: Context) {
            ActivityUtils.startActivity(context, SmartFreezeActivity::class.java)
        }
    }

    private lateinit var binding: SmartFreezeLayoutBottomNavBinding

    override fun isF(): Boolean {
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = SmartFreezeLayoutBottomNavBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inflateTabs()
    }

    private fun inflateTabs() {
        viewModel.getTabs(this)
        binding.bottomNavigation.removeAllTabs()
        viewModel.tabItems.forEach {
            binding.bottomNavigation.addTab(
                binding.bottomNavigation.newTab().setId(it.id).setText(it.pkgSet.label)
            )
        }
        binding.bottomNavigation.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                handleTabSelect(tab)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Noop.
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                handleTabSelect(tab)
            }
        })

        binding.bottomNavigation.selectTab(binding.bottomNavigation.getTabAt(0))
    }

    private fun handleTabSelect(tab: TabLayout.Tab?) {
        tab?.let { selectedTab ->
            viewModel.tabItems.firstOrNull {
                it.id == selectedTab.id
            }?.let { tab ->
                replaceFragment(SmartFreezeAppListFragment.newInstance(tab.pkgSet.id))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        showFeatureDialogIfNeed()
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentByTag("SMART_FREEZE_FRAGMENT")
        val consumed = fragment != null && (fragment as BaseFragment).onBackPressed()
        if (!consumed) {
            super.onBackPressed()
        }
    }

    private fun replaceFragment(newFragment: Fragment) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragment_container, newFragment, "SMART_FREEZE_FRAGMENT").commit()
    }

    private fun showFeatureDialogIfNeed() {
        if (AppPreference.isFeatureNoticeAccepted(this, "SmartFreeze")) {
            return
        }
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.feature_title_smart_app_freeze)
            .setMessage(R.string.feature_desc_smart_app_freeze)
            .setCancelable(false)
            .setPositiveButton(android.R.string.ok, null)
            .setNegativeButton(
                android.R.string.cancel
            ) { _: DialogInterface?, _: Int -> finish() }
            .setNeutralButton(R.string.title_remember) { _, _ ->
                AppPreference.setFeatureNoticeAccepted(
                    this,
                    "SmartFreeze",
                    true
                )
            }
            .show()
    }
}

data class TabItem(
    val id: Int,
    val pkgSet: PackageSet
)