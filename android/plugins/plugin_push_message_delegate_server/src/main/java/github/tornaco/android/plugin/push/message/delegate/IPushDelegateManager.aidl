package github.tornaco.android.plugin.push.message.delegate;

interface IPushDelegateManager {

    boolean wechatEnabled();
    void setWeChatEnabled(boolean enabled);

    boolean wechatSoundEnabled();
    void setWechatSoundEnabled(boolean enabled);

    boolean wechatContentEnabled();
    void setWechatContentEnabled(boolean enabled);

    boolean wechatVibrateEnabled();
    void setWechatVibrateEnabled(boolean enabled);

    void mockWechatMessage();

    boolean startWechatOnPushEnabled();
    void setStartWechatOnPushEnabled(boolean enabled);

    boolean skipIfWeChatAppRunningEnabled();
    void setSkipIfWeChatAppRunningEnabled(boolean enabled);

    boolean shouldHookBroadcastPerformResult();
    int onHookBroadcastPerformResult(in Intent intent, int resultCode);
}
