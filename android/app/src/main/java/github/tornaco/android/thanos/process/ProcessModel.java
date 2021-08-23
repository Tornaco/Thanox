package github.tornaco.android.thanos.process;

import java.util.List;

import github.tornaco.android.thanos.core.app.RunningServiceInfoCompat;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.core.process.ProcessRecord;
import util.PinyinComparatorUtils;

public class ProcessModel implements Comparable<ProcessModel> {
    private List<ProcessRecord> processRecord;
    List<RunningServiceInfoCompat> runningServices;
    private AppInfo appInfo;
    private long size;
    private String sizeStr;
    private String badge1, badge2;

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
