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

package now.fortuitous.thanos.qs;

import android.os.IBinder;
import android.service.quicksettings.TileService;

import com.elvishew.xlog.XLog;

import util.XposedHelpers;

class QsHelper {

    static void collp(TileService service) {
        try {
            IBinder token = (IBinder) XposedHelpers.getObjectField(service, "mTileToken");
            Object tileService = XposedHelpers.getObjectField(service, "mService");
            // mService.onStartActivity(mTileToken);
            XposedHelpers.callMethod(tileService, "onStartActivity", token);
        } catch (Throwable e) {
            XLog.e(e);
        }
    }
}
