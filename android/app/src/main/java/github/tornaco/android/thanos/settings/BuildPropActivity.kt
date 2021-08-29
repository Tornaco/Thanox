package github.tornaco.android.thanos.settings

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import github.tornaco.android.thanos.BuildProp
import github.tornaco.android.thanos.util.ActivityUtils

class BuildPropActivity : ComponentActivity() {
    object Starter {
        fun start(context: Context?) {
            ActivityUtils.startActivity(context, BuildPropActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BuildPropScreen()
        }
    }

    @Composable
    private fun BuildPropScreen() {
        Surface(modifier = Modifier.fillMaxSize()) {
            Scaffold(modifier = Modifier.fillMaxSize(), topBar = { AppBar() }) {
                LazyColumn {
                    items(props()) {
                        PropItem(it)
                    }
                }
            }
        }
    }

    @Composable
    private fun AppBar() {
        TopAppBar(modifier = Modifier.fillMaxWidth()) {
            // Noop.
        }
    }

    @Composable
    private fun PropItem(prop: Pair<String, String>) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
        ) {
            Text(prop.first, style = MaterialTheme.typography.subtitle1)
            Spacer(modifier = Modifier
                .height(1.dp)
                .fillMaxWidth())
            Text(prop.second, style = MaterialTheme.typography.body2)
        }
    }

    private fun props(): List<Pair<String, String>> {
        return BuildProp::class.java.declaredFields.map {
            it.isAccessible = true
            it.name to (it.get(null) ?: "").toString()
        }
    }
}