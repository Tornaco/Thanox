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

import android.content.Intent;

import github.tornaco.android.thanos.core.T;

public class OnyKeyClearTile extends FeatureOnOffTile {

    @Override
    boolean isOn() {
        return false;
    }

    @Override
    void setOn(boolean on) {
        getApplicationContext().sendBroadcast(new Intent(T.Actions.ACTION_RUNNING_PROCESS_CLEAR));
    }
}
