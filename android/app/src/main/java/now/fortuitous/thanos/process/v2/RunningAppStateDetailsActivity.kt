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

package now.fortuitous.thanos.process.v2

import android.content.Context
import android.content.Intent
import android.os.Parcelable
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import dagger.hilt.android.AndroidEntryPoint
import github.tornaco.android.thanos.module.compose.common.ComposeThemeActivity
import kotlinx.parcelize.Parcelize

@Parcelize
data class RunningAppStateDetails(val state: RunningAppState) : Parcelable

@AndroidEntryPoint
class RunningAppStateDetailsActivity : ComposeThemeActivity() {
    companion object {
        const val EXTRA_DETAILS = "details"
    }

    object Starter {
        fun intent(context: Context, details: RunningAppStateDetails): Intent {
            return Intent(context, RunningAppStateDetailsActivity::class.java).apply {
                putExtra(EXTRA_DETAILS, details)
            }
        }
    }

    override fun isF(): Boolean {
        return true
    }

    override fun isADVF(): Boolean {
        return true
    }

    @Composable
    override fun Content() {
        val details = intent.getParcelableExtra(EXTRA_DETAILS) as RunningAppStateDetails?
        Surface {
            details?.let { stateDetails ->
                RunningAppStateDetailsPage(stateDetails) {
                    setResult(if (it) RESULT_OK else RESULT_CANCELED)
                    finish()
                }
            }
        }
    }
}