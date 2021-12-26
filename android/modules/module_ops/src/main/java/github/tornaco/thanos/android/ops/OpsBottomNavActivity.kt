package github.tornaco.thanos.android.ops

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import github.tornaco.android.thanos.BuildProp
import github.tornaco.android.thanos.theme.ThemeActivity
import github.tornaco.android.thanos.util.ActivityUtils
import github.tornaco.thanos.android.ops.databinding.ModuleOpsLayoutBottomNavBinding
import github.tornaco.thanos.android.ops.ops.by.ops.AllOpsListFragment
import github.tornaco.thanos.android.ops.ops.dashboard.OpsDashboardFragment

class OpsBottomNavActivity : ThemeActivity() {

    companion object Starter {
        fun start(context: Context) {
            ActivityUtils.startActivity(context, OpsBottomNavActivity::class.java)
        }
    }

    private lateinit var binding: ModuleOpsLayoutBottomNavBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ModuleOpsLayoutBottomNavBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(AllOpsListFragment.newInstance())

        binding.bottomNavigation.menu.findItem(R.id.page_1).isVisible = BuildProp.THANOS_BUILD_DEBUG
        binding.bottomNavigation.setOnItemSelectedListener { menu ->
            when (menu.itemId) {
                R.id.page_1 -> replaceFragment(OpsDashboardFragment.newInstance())
                R.id.page_2 -> replaceFragment(AllOpsListFragment.newInstance())
            }
            true
        }
    }

    private fun replaceFragment(newFragment: Fragment) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragment_container, newFragment).commit()
    }
}