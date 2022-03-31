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

package github.tornaco.thanos.android.noroot;


import android.os.Binder;
import android.os.Process;

import com.elvishew.xlog.XLog;

import github.tornaco.android.thanos.core.plus.ICallback;
import github.tornaco.android.thanos.core.plus.RR;

public class UserService extends ICallback.Stub {
    public UserService() {
    }

    @Override
    public void onRes(RR res) {
        XLog.w("Received call: " + res + " --- p" + Process.myPid() + " u" + Process.myUid());
    }
}