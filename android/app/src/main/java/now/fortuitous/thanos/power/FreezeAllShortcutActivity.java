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

package now.fortuitous.thanos.power;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.elvishew.xlog.XLog;

import java.util.List;

import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.pm.PackageEnableStateChangeListener;
import github.tornaco.android.thanos.core.pm.Pkg;
import tornaco.apps.thanox.util.ToastUtils;

public class FreezeAllShortcutActivity extends Activity {

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, FreezeAllShortcutActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            XLog.i("FreezeAllShortcutActivity Freeze all.");
            ThanosManager.from(getApplicationContext())
                    .ifServiceInstalled(thanosManager ->
                            thanosManager.getPkgManager().freezeAllSmartFreezePackages(new PackageEnableStateChangeListener() {
                                @Override
                                public void onPackageEnableStateChanged(List<Pkg> pkgs) {
                                    super.onPackageEnableStateChanged(pkgs);
                                    runOnUiThread(() -> ToastUtils.ok(getApplicationContext()));
                                }
                            }));
        } finally {
            finish();
        }
    }
}
