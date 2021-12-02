package github.tornaco.android.thanos.core.app.usage;

import android.app.usage.UsageStats;

interface IUsageStatsManager {
    List<UsageStats> queryUsageStats(int intervalType, long beginTime, long endTime);
    Map<String, UsageStats> queryAndAggregateUsageStats(long beginTime, long endTime);
}