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

package now.fortuitous.thanos.power

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Parcelable
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.fragment.app.Fragment
import com.elvishew.xlog.XLog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import github.tornaco.android.thanos.BaseFragment
import github.tornaco.android.thanos.R
import github.tornaco.android.thanos.core.pm.PackageSet
import github.tornaco.android.thanos.databinding.SmartFreezeLayoutBottomNavBinding
import github.tornaco.android.thanos.module.compose.common.theme.ThanoxTheme
import github.tornaco.android.thanos.module.compose.common.theme.getColorAttribute
import github.tornaco.android.thanos.theme.ThemeActivity
import github.tornaco.android.thanos.util.ActivityUtils
import github.tornaco.android.thanos.util.pleaseReadCarefully
import kotlinx.parcelize.Parcelize

class SmartFreezeActivity : ThemeActivity() {
    private val viewModel: SmartFreezeBottomNavViewModel by viewModels()

    companion object Starter {
        const val EXTRA_EXPAND_SEARCH = "expand.search"

        fun start(context: Context) {
            ActivityUtils.startActivity(context, SmartFreezeActivity::class.java)
        }
    }

    private lateinit var binding: SmartFreezeLayoutBottomNavBinding

    

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = SmartFreezeLayoutBottomNavBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inflateTabs()

        binding.bottomNavigation.post {
            val hasExpandSearchIntent = intent?.hasExtra(EXTRA_EXPAND_SEARCH) ?: false
            XLog.i("SmartFreezeActivity hasExpandSearchIntent: $hasExpandSearchIntent")
            if (hasExpandSearchIntent) {
                val fragment = supportFragmentManager.findFragmentByTag("SMART_FREEZE_FRAGMENT")
                XLog.i("SmartFreezeActivity fragment: $fragment")
                (fragment as? SmartFreezeAppListFragment)?.showSearch()
            }
        }
    }

    private fun inflateTabs() {
        viewModel.getTabs(this)
        binding.bottomNavigation.removeAllTabs()
        viewModel.tabItems.forEach {
            binding.bottomNavigation.addTab(
                binding.bottomNavigation.newTab().setId(it.id).setText(it.pkgSet.label)
            )
        }
        binding.bottomNavigation.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
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
        binding.composeSort.setContent {
            ThanoxTheme {
                val context = LocalContext.current

                val tabSortDialogState =
                    rememberTabItemSortState(apply = {
                        viewModel.applySort(context, it)
                        refreshTabs()
                    })
                TabItemSortDialog(tabSortDialogState)

                val tintColor = getColorAttribute(android.R.attr.colorControlNormal)
                Column {
                    IconButton(onClick = {
                        tabSortDialogState.show(viewModel.tabItems)
                    }) {
                        Icon(
                            painter = painterResource(id = github.tornaco.android.thanos.module.common.R.drawable.module_common_ic_outline_sort_24),
                            contentDescription = "Sort",
                            tint = Color(tintColor)
                        )
                    }
                }
            }
        }
    }

    private fun refreshTabs() {
        viewModel.getTabs(this)
        binding.bottomNavigation.removeAllTabs()
        viewModel.tabItems.forEach {
            binding.bottomNavigation.addTab(
                binding.bottomNavigation.newTab().setId(it.id).setText(it.pkgSet.label)
            )
        }
        binding.bottomNavigation.selectTab(binding.bottomNavigation.getTabAt(0))
    }

    private fun handleTabSelect(tab: TabLayout.Tab?) {
        tab?.let { selectedTab ->
            viewModel.tabItems.firstOrNull {
                it.id == selectedTab.id
            }?.let { tab ->
                replaceFragment(
                    SmartFreezeAppListFragment.newInstance(
                        tab.pkgSet.id
                    )
                )
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
        if (now.fortuitous.thanos.pref.AppPreference.isFeatureNoticeAccepted(this, "SmartFreeze")) {
            return
        }
        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle(github.tornaco.android.thanos.res.R.string.feature_title_smart_app_freeze)
            .setMessage(github.tornaco.android.thanos.res.R.string.feature_desc_smart_app_freeze)
            .setCancelable(false)
            .setPositiveButton(android.R.string.ok, null)
            .setNegativeButton(
                android.R.string.cancel
            ) { _: DialogInterface?, _: Int -> finish() }
            .setNeutralButton(github.tornaco.android.thanos.res.R.string.title_remember) { _, _ ->
                now.fortuitous.thanos.pref.AppPreference.setFeatureNoticeAccepted(
                    this,
                    "SmartFreeze",
                    true
                )
            }
            .show()

        pleaseReadCarefully(Handler(Looper.getMainLooper()), dialog, 18)
    }
}

@Parcelize
data class TabItem(
    val id: Int,
    val pkgSet: PackageSet,
) : Parcelable