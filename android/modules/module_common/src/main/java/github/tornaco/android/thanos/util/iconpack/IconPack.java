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
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.core.content.res.ResourcesCompat;

import com.elvishew.xlog.XLog;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.util.PkgUtils;

public class IconPack extends App {

    private final Context mContext;
    private HashMap<String, String> mPackagesDrawables;
    private List<String> mAllDrawables;
    private Resources mIconPackRes = null;
    private boolean mLoaded = false;

    public IconPack(Context context, ResolveInfo resolveInfo) {
        this.mContext = context;
        this.label = resolveInfo.loadLabel(mContext.getPackageManager());
        this.packageName = resolveInfo.activityInfo.packageName;
    }

    public boolean isInstalled() {
        return PkgUtils.isPkgInstalled(mContext, packageName);
    }

    private void load() {
        PackageManager pm = mContext.getPackageManager();
        mAllDrawables = new ArrayList<>();
        mPackagesDrawables = new HashMap<>();
        try {
            XmlPullParser xpp = null;

            mIconPackRes = pm.getResourcesForApplication(packageName);
            int appFilterId = mIconPackRes.getIdentifier("appfilter", "xml", packageName);
            if (appFilterId > 0) {
                xpp = mIconPackRes.getXml(appFilterId);
            } else {
                try {
                    InputStream appFilterStream = mIconPackRes.getAssets().open("appfilter.xml");

                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    xpp = factory.newPullParser();
                    xpp.setInput(appFilterStream, "utf-8");
                } catch (IOException e1) {
                    XLog.d("No appfilter.xml file");
                }
            }

            if (xpp != null) {
                int eventType = xpp.getEventType();

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG && xpp.getName().equals("item")) {
                        String componentName = xpp.getAttributeValue(null, "component");
                        String drawableName = xpp.getAttributeValue(null, "drawable");

                        if (!mAllDrawables.contains(drawableName)) {
                            mAllDrawables.add(drawableName);
                        }

                        if (!mPackagesDrawables.containsKey(componentName)) {
                            mPackagesDrawables.put(componentName, drawableName);
                            XLog.d("add drawable name: %s %s", componentName, drawableName);
                        }
                    }
                    eventType = xpp.next();
                }
            }
            mLoaded = true;
        } catch (PackageManager.NameNotFoundException e) {
            XLog.d("Cannot load icon pack");
        } catch (XmlPullParserException e) {
            XLog.d("Cannot parse icon pack appfilter.xml");
        } catch (IOException e) {
            XLog.e(Log.getStackTraceString(e));
        }
    }

    public List<String> getAllDrawables() {
        if (!mLoaded) {
            load();
        }

        mAllDrawables.sort((s, t1) -> {
            if (s == null || t1 == null) {
                return -1;
            }

            return s.compareTo(t1);

        });
        return mAllDrawables;
    }

    public Drawable loadDrawable(String drawableName) {
        int id = mIconPackRes.getIdentifier(drawableName, "drawable", packageName);
        if (id > 0) {
            return ResourcesCompat.getDrawable(mIconPackRes, id, null);
        }
        return null;
    }

    public Drawable getDrawableIconForPackage(String appPackageName) {
        if (!mLoaded) {
            load();
        }

        // Query from Android.
        PackageManager pm = mContext.getPackageManager();
        Intent launchIntent = pm.getLaunchIntentForPackage(appPackageName);
        String componentNameString = null;
        if (launchIntent != null && launchIntent.getComponent() != null) {
            componentNameString = launchIntent.getComponent().toString();
        }

        // Query from Thanos.
        if (componentNameString == null) {
            ThanosManager thanosManager = ThanosManager.from(mContext);
            if (thanosManager.isServiceInstalled()) {
                Intent intent = thanosManager.getPkgManager().queryLaunchIntentForPackage(appPackageName);
                if (intent != null && intent.getComponent() != null) {
                    componentNameString = intent.getComponent().toString();
                }
            }
        }

        // Fallback, find first.
        if (componentNameString == null) {
            for (String key : mPackagesDrawables.keySet()) {
                if (key.contains(appPackageName)) {
                    XLog.d("Fallback componentNameString: %s", key);
                    componentNameString = key;
                }
            }
        }

        XLog.v("getDrawableIconForPackage componentNameString: " + componentNameString);
        String drawable = mPackagesDrawables.get(componentNameString);
        if (drawable != null) {
            return loadDrawable(drawable);
        } else {
            // try to get a resource with the component filename
            if (componentNameString != null) {
                int start = componentNameString.indexOf("{") + 1;
                int end = componentNameString.indexOf("}", start);
                if (end > start) {
                    drawable = componentNameString.substring(start, end).toLowerCase(Locale.getDefault()).replace(".", "_").replace("/", "_");
                    if (mIconPackRes.getIdentifier(drawable, "drawable", packageName) > 0) {
                        return loadDrawable(drawable);
                    }
                }
            }
        }
        return null;
    }

    public void unload() {
        mPackagesDrawables = null;
        mAllDrawables = null;
        mIconPackRes = null;
        mLoaded = false;
    }
}