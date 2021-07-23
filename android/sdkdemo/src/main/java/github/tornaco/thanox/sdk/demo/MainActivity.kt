package github.tornaco.thanox.sdk.demo

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import github.tornaco.android.thanos.theme.ThemeActivity
import github.tornaco.android.thanos.widget.ModernAlertDialog
import github.tornaco.thanox.sdk.demo.databinding.ActivityMainBinding

class MainActivity : ThemeActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.fab.setOnClickListener {
            // Test modern dialog.
            ModernAlertDialog(this)
                .apply {
                    setDialogTitle("Android dev.")
                    setDialogMessage("Find the latest and greatest on the world's most powerful mobile platform. Browse devices, explore resources and learn about the latest updates.")
                    setPositive(getString(android.R.string.ok))
                    setNegative(getString(android.R.string.cancel))
                    setOnPositive {}
                    show()
                }.show()

        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}