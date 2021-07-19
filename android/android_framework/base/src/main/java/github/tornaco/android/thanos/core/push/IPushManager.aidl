package github.tornaco.android.thanos.core.push;

import github.tornaco.android.thanos.core.push.IChannelHandler;

interface IPushManager {
    void registerChannel(in PushChannel channel);
    void unRegisterChannel(in PushChannel channel);

    void registerChannelHandler(String channelId, in IChannelHandler handler);
    void unRegisterChannelHandler(in IChannelHandler handler);
}