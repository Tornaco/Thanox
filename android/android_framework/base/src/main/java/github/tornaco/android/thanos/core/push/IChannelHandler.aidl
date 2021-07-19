package github.tornaco.android.thanos.core.push;

interface IChannelHandler {
    void onMessageArrive(in Intent intent);
}