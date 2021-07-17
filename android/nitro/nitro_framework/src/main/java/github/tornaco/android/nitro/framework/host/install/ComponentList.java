/*
 * Copyright (C) 2005-2017 Qihoo 360 Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed To in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package github.tornaco.android.nitro.framework.host.install;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;
import android.os.Bundle;
import android.os.Environment;

import androidx.annotation.Nullable;

import com.elvishew.xlog.XLog;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import github.tornaco.android.nitro.framework.host.install.util.parser.apk.ApkParser;
import github.tornaco.android.nitro.framework.host.install.util.parser.manifest.IntentFilters;
import github.tornaco.android.nitro.framework.plugin.PluginFile;
import lombok.Getter;
import util.CollectionUtils;

/**
 * 用来快速获取四大组件和Application的系统Info的List
 * <p>
 * NOTE 每个Plugin对象维护一份ComponentList，且在第一次加载PackageInfo时被生成
 *
 * @author RePlugin Team
 * @see android.content.pm.ActivityInfo
 * @see android.content.pm.ServiceInfo
 * @see android.content.pm.ProviderInfo
 * @see android.content.pm.ApplicationInfo
 */
@Getter
public class ComponentList {

    private static final boolean LOG = true;
    /**
     * Class类名 - Activity的Map表
     */
    private final HashMap<String, ActivityInfo> activities = new HashMap<>();

    /**
     * Class类名 - Provider的Map表
     */
    private final HashMap<String, ProviderInfo> providersByName = new HashMap<>();

    /**
     * Authority - Provider的Map表
     */
    private final HashMap<String, ProviderInfo> providersByAuthority = new HashMap<>();

    /**
     * Class类名 - Service的Map表
     */
    private final HashMap<String, ServiceInfo> services = new HashMap<>();

    private IntentFilters intentFilters;

    /**
     * Application对象
     */
    private ApplicationInfo application;

    /**
     * Class类名 - BroadcastReceiver的Map表
     * 注意：是的，你没有看错，系统缓存Receiver就是用的ActivityInfo
     */
    private final HashMap<String, ActivityInfo> receivers = new HashMap<>();

    /**
     * 初始化ComponentList对象 <p>
     * 注意：仅框架内部使用
     */
    public ComponentList(PackageInfo pi, String path, PluginFile pli) {
        if (pi.activities != null) {
            for (ActivityInfo ai : pi.activities) {
                ai.applicationInfo.sourceDir = path;
                if (ai.processName == null) {
                    ai.processName = ai.applicationInfo.processName;
                }
                if (ai.processName == null) {
                    ai.processName = ai.packageName;
                }
                activities.put(ai.name, ai);
            }
        }
        if (pi.providers != null) {
            for (ProviderInfo ppi : pi.providers) {
                if (ppi.processName == null) {
                    ppi.processName = ppi.applicationInfo.processName;
                }
                if (ppi.processName == null) {
                    ppi.processName = ppi.packageName;
                }
                providersByName.put(ppi.name, ppi);
                providersByAuthority.put(ppi.authority, ppi);
            }
        }
        if (pi.services != null) {
            for (ServiceInfo si : pi.services) {
                if (si.processName == null) {
                    si.processName = si.applicationInfo.processName;
                }
                if (si.processName == null) {
                    si.processName = si.packageName;
                }
                services.put(si.name, si);
            }
        }
        if (pi.receivers != null) {
            for (ActivityInfo ri : pi.receivers) {
                if (ri.processName == null) {
                    ri.processName = ri.applicationInfo.processName;
                }
                if (ri.processName == null) {
                    ri.processName = ri.packageName;
                }
                receivers.put(ri.name, ri);
            }
        }

        application = pi.applicationInfo;

        if (application.dataDir == null) {
            application.dataDir = Environment.getDataDirectory() + File.separator + "data" + File.separator + application.packageName;
        }

        IntentFilters intentFilters = new IntentFilters();
        intentFilters.inflate(pli, getManifestFromApk(path));

        this.intentFilters = intentFilters;
    }

    /**
     * 从 APK 中获取 IntentFilters 内容
     *
     * @param apkFile apk 文件路径
     * @return apk 中 AndroidManifest 中的内容
     */
    private static String getManifestFromApk(String apkFile) {
        ApkParser parser = null;
        String manifest = null;
        try {
            parser = new ApkParser(apkFile);
            if (LOG) {
                long begin = System.currentTimeMillis();
                manifest = parser.getManifestXml();
                long end = System.currentTimeMillis();
                XLog.d("从 apk 中解析 xml 耗时 " + (end - begin) + " 毫秒");
            } else {
                manifest = parser.getManifestXml();
            }
            return manifest;

        } catch (IOException t) {
            t.printStackTrace();
        } finally {
            if (parser != null) {
                try {
                    parser.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    /**
     * 获取ServiceInfo对象
     */
    public ServiceInfo getService(String className) {
        return services.get(className);
    }

    /**
     * 获取该插件所有的ServiceInfo列表
     */
    public ServiceInfo[] getServices() {
        return services.values().toArray(new ServiceInfo[0]);
    }

    /**
     * 获取ServiceInfo对象
     */
    public ActivityInfo getActivity(String className) {
        return activities.get(className);
    }

    /**
     * 获取该插件所有的ActivityInfo列表
     */
    public ActivityInfo[] getActivities() {
        return activities.values().toArray(new ActivityInfo[0]);
    }

    /**
     * 获取 Receiver
     */
    public ActivityInfo getReveiver(String className) {
        return receivers.get(className);
    }

    /**
     * 获取该插件所有的 Receiver 列表
     */
    public ActivityInfo[] getReceivers() {
        return receivers.values().toArray(new ActivityInfo[0]);
    }

    /**
     * 通过类名获取ProviderInfo对象
     */
    public ProviderInfo getProvider(String className) {
        return providersByName.get(className);
    }

    /**
     * 通过Authority获取ProviderInfo对象
     */
    public ProviderInfo getProviderByAuthority(String authority) {
        return providersByAuthority.get(authority);
    }

    /**
     * 获取该插件所有的ProviderInfo列表
     */
    public ProviderInfo[] getProviders() {
        return providersByName.values().toArray(new ProviderInfo[0]);
    }

    @Nullable
    public String getMainActivityName() {
        IntentFilters intentFilter = getIntentFilters();
        for (String name : intentFilter.getActivityInfoMap().keySet()) {
            List<IntentFilter> intentFilters = intentFilter.getActivityInfoMap().get(name);
            if (!CollectionUtils.isNullOrEmpty(intentFilters)) {
                for (IntentFilter filter : intentFilters) {
                    if (filter.hasAction(Intent.ACTION_MAIN) && filter.hasCategory(Intent.CATEGORY_LAUNCHER)) {
                        return name;
                    }
                }
            }
        }
        return null;
    }

    @Nullable
    public String getDescription() {
        Bundle metaData = application.metaData;
        if (metaData != null) {
            return metaData.getString("thanox.plugin.description");
        }
        return null;
    }

    public int getRequiredMinThanoxVersion() {
        Bundle metaData = application.metaData;
        if (metaData != null) {
            return metaData.getInt("thanox.plugin.minThanoxVersion");
        }
        return Integer.MIN_VALUE;
    }

    public boolean getStable() {
        Bundle metaData = application.metaData;
        if (metaData != null) {
            return metaData.getBoolean("thanox.plugin.stable", false);
        }
        return false;
    }

    public boolean withHooks() {
        Bundle metaData = application.metaData;
        if (metaData != null) {
            // Default is true.
            return metaData.getBoolean("thanox.plugin.withHooks", true);
        }
        return false;
    }

    public String statusCallableClass() {
        Bundle metaData = application.metaData;
        if (metaData != null) {
            // Default is true.
            return metaData.getString("thanox.plugin.statusCallableClass", "");
        }
        return "";
    }
}
