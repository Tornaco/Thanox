package github.tornaco.android.thanos.core.app.usage;

import android.app.usage.UsageStats;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

@AllArgsConstructor
public class UsageStatsManager {
    private final IUsageStatsManager server;

    @SneakyThrows
    public List<UsageStats> queryUsageStats(int intervalType, long beginTime, long endTime) {
        return server.queryUsageStats(intervalType, beginTime, endTime);
    }
}
