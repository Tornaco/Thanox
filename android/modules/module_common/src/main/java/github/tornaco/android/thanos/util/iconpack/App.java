/*
 * Copyright 2016 dvdandroid
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package github.tornaco.android.thanos.util.iconpack;

import android.content.Context;
import android.content.pm.PackageManager;
import github.tornaco.android.thanos.util.BitmapUtil;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class App {

    public String packageName;
    public byte[] originalIcon;

    public String componentName;
    public long lastUpdated;
    public long version;
    public byte[] icon;
    public byte[] icon_low_res;
    public CharSequence label;

    public App() {
    }

    public void loadIcon(Context context) throws PackageManager.NameNotFoundException {
        originalIcon = BitmapUtil.drawableToByteArray(context.getPackageManager().getApplicationIcon(packageName), false);
    }

    public void loadPackageName() {
        packageName = componentName.split("/")[0];
    }

    public String getFormattedDate() {
        DateFormat f = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.getDefault());

        return f.format(new Date(lastUpdated));
    }

}