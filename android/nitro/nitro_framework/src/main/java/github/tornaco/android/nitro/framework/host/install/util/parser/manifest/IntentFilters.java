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

package github.tornaco.android.nitro.framework.host.install.util.parser.manifest;

import android.content.IntentFilter;
import android.os.PatternMatcher;

import com.elvishew.xlog.XLog;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import github.tornaco.android.nitro.framework.host.install.util.parser.manifest.bean.ComponentBean;
import github.tornaco.android.nitro.framework.plugin.PluginFile;
import lombok.Getter;

@Getter
public class IntentFilters {

    private static final String TAG = "ms";

    private Map<String, List<IntentFilter>> activityInfoMap = new HashMap<>();
    private Map<String, List<IntentFilter>> serviceInfoMap = new HashMap<>();
    private Map<String, List<IntentFilter>> receiverInfoMap = new HashMap<>();

    private Map<String, Set<String>> activityActionPluginsMap = new HashMap<>();
    private Map<String, Set<String>> serviceActionPluginsMap = new HashMap<>();

    /**
     * 解析 AndroidManifest
     *
     * @param pli         插件信息
     * @param manifestStr AndroidManifest.xml 字符串
     */
    public void inflate(PluginFile pli, String manifestStr) {
        XmlHandler handler = parseManifest(manifestStr);

        activityInfoMap = new HashMap<>();
        parseComponent(pli.getName(), activityInfoMap, handler.getActivities(), activityActionPluginsMap);

        serviceInfoMap = new HashMap<>();
        parseComponent(pli.getName(), serviceInfoMap, handler.getServices(), serviceActionPluginsMap);

        receiverInfoMap = new HashMap<>();
        parseComponent(pli.getName(), receiverInfoMap, handler.getReceivers(), null);
    }


    /**
     * parseComponent
     *
     * @param plugin         插件名称
     * @param filterMap      HashMap<组件名称, List<IntentFilters>>
     * @param componentBeans 从 manifest 中解析到的组件列表
     */
    private void parseComponent(String plugin,
                                Map<String, List<IntentFilter>> filterMap,
                                List<ComponentBean> componentBeans,
                                Map<String, Set<String>> actionPluginsMap) {

        if (componentBeans != null) {
            for (ComponentBean componentBean : componentBeans) {
                doFillFilters(componentBean, filterMap);
                doFillActionPlugins(plugin, componentBean, actionPluginsMap);
            }
        }
    }

    private void doFillFilters(ComponentBean b, Map<String, List<IntentFilter>> filterMap) {
        String cn = b.name;
        List<IntentFilter> filterList = filterMap.get(cn);
        if (filterList == null) {
            filterList = new ArrayList<>();
            filterMap.put(cn, filterList);
        }
        List<IntentFilter> filters = b.intentFilters;
        if (filters != null) {
            filterList.addAll(filters);
        }
    }

    /**
     * 将 filters 中 action 和组件的对应关系保存起来
     */
    private void doFillActionPlugins(String plugin, ComponentBean componentBean, Map<String, Set<String>> actionPluginsMap) {
        if (actionPluginsMap == null || componentBean.intentFilters == null) {
            return;
        }

        for (IntentFilter filter : componentBean.intentFilters) {
            Iterator<String> iterator = filter.actionsIterator();
            while (iterator.hasNext()) {
                String action = iterator.next();
                Set<String> plugins = actionPluginsMap.get(action);
                if (plugins == null) {
                    plugins = new HashSet<>();
                    actionPluginsMap.put(action, plugins);
                }
                plugins.add(plugin);
            }
        }
    }

    /**
     * 将 manifest 中的数据存储在 XmlHandler 中
     *
     * @param manifestStr AndroidManifest 内容
     * @return XmlHandler
     */
    private XmlHandler parseManifest(String manifestStr) {
        XMLReader xmlReader = null;
        XmlHandler handler = new XmlHandler();

        /* 解析字符串 */
        try {
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            xmlReader = parser.getXMLReader();
            xmlReader.setContentHandler(handler);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        if (xmlReader != null) {
            StringReader strReader = null;

            try {
                strReader = new StringReader(manifestStr);
                xmlReader.parse(new InputSource(strReader));
            } catch (Throwable e) {
                e.printStackTrace();
            } finally {
                if (strReader != null) {
                    strReader.close();
                }
            }
        }
        return handler;
    }

    /**
     * 打印 filter
     */
    private void printFilters(Map<String, List<IntentFilter>> actFilterMap,
                              Map<String, List<IntentFilter>> svcFilterMap,
                              Map<String, List<IntentFilter>> rcvFilterMap) {
        if (!actFilterMap.entrySet().isEmpty()) {
            XLog.d(TAG, "\n打印 Activity - IntentFilter");
        }
        for (HashMap.Entry<String, List<IntentFilter>> entry : actFilterMap.entrySet()) {
            List<IntentFilter> filter = entry.getValue();
            XLog.d(TAG, "key:" + entry.getKey() + "; val:" + intentFilterStr(filter));
        }

        if (!svcFilterMap.entrySet().isEmpty()) {
            XLog.d(TAG, "\n打印 Service - IntentFilter");
        }
        for (HashMap.Entry<String, List<IntentFilter>> entry : svcFilterMap.entrySet()) {
            List<IntentFilter> filter = entry.getValue();
            XLog.d(TAG, "key:" + entry.getKey() + "; val:" + intentFilterStr(filter));
        }

        if (!rcvFilterMap.entrySet().isEmpty()) {
            XLog.d(TAG, "\n打印 Receiver - IntentFilter");
        }
        for (HashMap.Entry<String, List<IntentFilter>> entry : rcvFilterMap.entrySet()) {
            List<IntentFilter> filter = entry.getValue();
            XLog.d(TAG, "key:" + entry.getKey() + "; val:" + intentFilterStr(filter));
        }
    }

    /**
     * 将 IntentFilter 列表转换成字符串
     *
     * @param filters IntentFilter 列表
     * @return IntentFilter 列表的字符串形式
     */
    private String intentFilterStr(List<IntentFilter> filters) {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (IntentFilter filter : filters) {
            builder.append("{");
            // actions
            int c = filter.countActions();
            if (c > 0) {
                builder.append("action:{");
            }
            while (c > 0) {
                builder.append(filter.getAction(c - 1)).append(",");
                c--;
                if (c == 0) {
                    builder.append("}");
                }
            }

            // category
            c = filter.countCategories();
            if (c > 0) {
                builder.append(", category:{");
            }
            while (c > 0) {
                builder.append(filter.getCategory(c - 1)).append(",");
                c--;
                if (c == 0) {
                    builder.append("}");
                }
            }

            // data-schema
            c = filter.countDataSchemes();
            if (c > 0) {
                builder.append(", data-scheme:{");
            }
            while (c > 0) {
                builder.append(filter.getDataScheme(c - 1)).append(",");
                c--;
                if (c == 0) {
                    builder.append("}");
                }
            }

            // data-path
            c = filter.countDataPaths();
            if (c > 0) {
                builder.append(", data-path:{");
            }
            while (c > 0) {
                PatternMatcher matcher = filter.getDataPath(c - 1);
                builder.append(matcher.getPath()).append(",").append(matcher.getType());
                c--;
                if (c == 0) {
                    builder.append("}");
                }
            }

            // data-type
            c = filter.countDataTypes();
            if (c > 0) {
                builder.append(", data-type:{");
            }
            while (c > 0) {
                builder.append(filter.getDataType(c - 1)).append(",");
                c--;
                if (c == 0) {
                    builder.append("}");
                }
            }

            builder.append("}, ");
        }
        builder.append("]");
        return builder.toString();
    }
}
