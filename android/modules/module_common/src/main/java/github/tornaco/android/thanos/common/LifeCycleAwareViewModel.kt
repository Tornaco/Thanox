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

package github.tornaco.android.thanos.common

import androidx.annotation.CallSuper
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.elvishew.xlog.XLog

open class LifeCycleAwareViewModel : ViewModel() {
    private val logTag get() = "${javaClass.name}-lifecycle"

    private var _isResumed = false
    val isResumed get() = _isResumed

    private val obs = object : DefaultLifecycleObserver {
        override fun onResume(owner: LifecycleOwner) {
            super.onResume(owner)
            XLog.w("$logTag onResume")
            this@LifeCycleAwareViewModel.onResume()
        }

        override fun onStop(owner: LifecycleOwner) {
            super.onStop(owner)
            XLog.w("$logTag onStop")
            this@LifeCycleAwareViewModel.onStop()
        }

        override fun onDestroy(owner: LifecycleOwner) {
            super.onDestroy(owner)
            XLog.w("$logTag onDestroy")
            owner.lifecycle.removeObserver(this)
        }
    }

    fun bindLifecycle(lifecycle: Lifecycle) {
        XLog.w("$logTag bindLifecycle: $lifecycle")
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