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

package now.fortuitous.thanos.main;

import android.content.Context;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import github.tornaco.android.thanos.BuildProp;
import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.util.BrowserUtils;

public class DialogUtils {
    private DialogUtils() {
    }

    public static void showNotActivated(Context context) {
        new MaterialAlertDialogBuilder(context)
                .setTitle(R.string.status_not_active)
                .setMessage(R.string.message_active_needed)
                .setCancelable(true)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(R.string.common_menu_title_wiki,
                        (dialog, which) -> {
                            BrowserUtils.launch(context, BuildProp.THANOX_URL_DOCS_HOME);
                        })
                .show();
    }
}
