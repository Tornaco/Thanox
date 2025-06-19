package github.tornaco.thanos.android.ops.ops.by.ops

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import github.tornaco.android.thanos.R
import github.tornaco.android.thanos.databinding.ModuleOpsLayoutBottomNavBinding
import github.tornaco.android.thanos.theme.ThemeActivity
import github.tornaco.android.thanos.util.ActivityUtils

class ThanoxOpsBottomNavActivity : ThemeActivity() {

    companion object Starter {
        fun start(context: Context) {
            ActivityUtils.startActivity(context, ThanoxOpsBottomNavActivity::class.java)
        }
    }

    private lateinit var binding: ModuleOpsLayoutBottomNavBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ModuleOpsLayoutBottomNavBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(ThanoxOpsListFragment.newInstance())
    }

    private fun replaceFragment(newFragment: Fragment) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragment_container, newFragment).commit()
    }
}