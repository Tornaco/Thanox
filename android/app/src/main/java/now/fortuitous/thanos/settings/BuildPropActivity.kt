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

package now.fortuitous.thanos.settings

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
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
            Scaffold(modifier = Modifier.fillMaxSize(), topBar = { AppBar() }) { paddingValues ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
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
            Spacer(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
            )
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