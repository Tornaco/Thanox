package github.tornaco.android.thanos.dashboard;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StatusHeaderInfo {
    private int runningAppsCount;
    private int memUsagePercent;
    private String memAvailablePercentString;
}
