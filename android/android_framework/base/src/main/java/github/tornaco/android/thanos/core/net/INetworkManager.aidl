package github.tornaco.android.thanos.core.net;

import github.tornaco.android.thanos.core.net.TrafficStats;

interface INetworkManager {
    TrafficStats getUidTrafficStats(int uid);
}