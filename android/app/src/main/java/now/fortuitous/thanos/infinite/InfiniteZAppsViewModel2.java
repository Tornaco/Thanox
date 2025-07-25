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

package now.fortuitous.thanos.infinite;

import android.app.Application;

import androidx.annotation.NonNull;

import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.app.infinite.InfiniteZManager;

public class InfiniteZAppsViewModel2 extends BaseInfiniteZAppsViewModel {
    public InfiniteZAppsViewModel2(@NonNull Application application) {
        super(application);
    }

    @Override
    protected InfiniteZManager infiniteZManager() {
        return ThanosManager.from(getApplication()).getInfiniteZ2();
    }
}
