package github.tornaco.android.thanos.process;

import java.util.List;

import github.tornaco.android.thanos.core.app.RunningServiceInfoCompat;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.core.process.ProcessRecord;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import util.PinyinComparatorUtils;

@AllArgsConstructor
@Getter
@ToString
public class ProcessModel implements Comparable<ProcessModel> {
    private List<ProcessRecord> processRecord;
    List<RunningServiceInfoCompat> runningServices;
    private AppInfo appInfo;
    private long size;
    private String sizeStr;
    private String badge1, badge2;

    @Override
    public int compareTo(ProcessModel processModel) {
        return PinyinComparatorUtils.compare(
                this.appInfo.getAppLabel(),
                processModel.appInfo.getAppLabel());
    }
}
