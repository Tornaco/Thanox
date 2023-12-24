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

package now.fortuitous.thanos.process;

import java.util.List;

import github.tornaco.android.thanos.core.app.RunningServiceInfoCompat;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.core.process.ProcessRecord;
import util.PinyinComparatorUtils;

public class ProcessModel implements Comparable<ProcessModel> {
    private final List<ProcessRecord> processRecord;
    List<RunningServiceInfoCompat> runningServices;
    private final AppInfo appInfo;
    private final long size;
    private final String sizeStr;
    private final String badge1;
    private final String badge2;

    public ProcessModel(List<ProcessRecord> processRecord, List<RunningServiceInfoCompat> runningServices, AppInfo appInfo, long size, String sizeStr, String badge1, String badge2) {
        this.processRecord = processRecord;
        this.runningServices = runningServices;
        this.appInfo = appInfo;
        this.size = size;
        this.sizeStr = sizeStr;
        this.badge1 = badge1;
        this.badge2 = badge2;
    }

    @Override
    public int compareTo(ProcessModel processModel) {
        return PinyinComparatorUtils.compare(
                this.appInfo.getAppLabel(),
                processModel.appInfo.getAppLabel());
    }

    public List<ProcessRecord> getProcessRecord() {
        return this.processRecord;
    }

    public List<RunningServiceInfoCompat> getRunningServices() {
        return this.runningServices;
    }

    public AppInfo getAppInfo() {
        return this.appInfo;
    }

    public long getSize() {
        return this.size;
    }

    public String getSizeStr() {
        return this.sizeStr;
    }

    public String getBadge1() {
        return this.badge1;
    }

    public String getBadge2() {
        return this.badge2;
    }

    public String toString() {
        return "ProcessModel(processRecord=" + this.getProcessRecord() + ", runningServices=" + this.getRunningServices() + ", appInfo=" + this.getAppInfo() + ", size=" + this.getSize() + ", sizeStr=" + this.getSizeStr() + ", badge1=" + this.getBadge1() + ", badge2=" + this.getBadge2() + ")";
    }
}
