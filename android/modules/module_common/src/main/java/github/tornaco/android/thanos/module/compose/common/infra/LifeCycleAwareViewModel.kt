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

package github.tornaco.android.thanos.module.compose.common.infra

import androidx.annotation.CallSuper
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import github.tornaco.android.thanos.core.Logger

open class LifeCycleAwareViewModel : ViewModel() {
    private val logger = Logger("${javaClass.name}-lifecycle")

    private var _isResumed = false
    val isResumed get() = _isResumed

    private val obs = object : DefaultLifecycleObserver {
        override fun onResume(owner: LifecycleOwner) {
            super.onResume(owner)
            logger.w("onResume")
            this@LifeCycleAwareViewModel.onResume()
        }

        override fun onStop(owner: LifecycleOwner) {
            super.onStop(owner)
            logger.w("onStop")
            this@LifeCycleAwareViewModel.onStop()
        }

        override fun onDestroy(owner: LifecycleOwner) {
            super.onDestroy(owner)
            logger.w("onDestroy")
            owner.lifecycle.removeObserver(this)
        }
    }

    fun bindLifecycle(lifecycle: Lifecycle) {
        lifecycle.addObserver(obs)
    }

    @CallSuper
    open fun onResume() {
        _isResumed = true
    }

    @CallSuper
    open fun onStop() {
        _isResumed = false
    }
}