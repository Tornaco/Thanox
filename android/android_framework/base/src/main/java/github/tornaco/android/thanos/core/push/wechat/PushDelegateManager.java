package github.tornaco.android.thanos.core.push.wechat;

import android.content.Intent;

import lombok.SneakyThrows;

public class PushDelegateManager {
    private final IPushDelegateManager service;

    public PushDelegateManager(IPushDelegateManager service) {
        this.service = service;
    }

    @SneakyThrows
    public boolean wechatEnabled() {
        return service.wechatEnabled();
    }

    @SneakyThrows
    public void setWeChatEnabled(boolean enabled) {
        service.setWeChatEnabled(enabled);
    }

    @SneakyThrows
    public boolean wechatSoundEnabled() {
        return service.wechatSoundEnabled();
    }

    @SneakyThrows
    public void setWechatSoundEnabled(boolean enabled) {
        service.setWechatSoundEnabled(enabled);
    }

    @SneakyThrows
    public boolean wechatContentEnabled() {
        return service.wechatContentEnabled();
    }

    @SneakyThrows
    public void setWechatContentEnabled(boolean enabled) {
        service.setWechatContentEnabled(enabled);
    }

    @SneakyThrows
    public boolean wechatVibrateEnabled() {
        return service.wechatVibrateEnabled();
    }

    @SneakyThrows
    public void setWechatVibrateEnabled(boolean enabled) {
        service.setWechatVibrateEnabled(enabled);
    }

    @SneakyThrows
    public void mockWechatMessage() {
        service.mockWechatMessage();
    }

    @SneakyThrows
    public boolean startWechatOnPushEnabled() {
        return service.startWechatOnPushEnabled();
    }

    @SneakyThrows
    public void setStartWechatOnPushEnabled(boolean enabled) {
        service.setStartWechatOnPushEnabled(enabled);
    }

    @SneakyThrows
    public boolean skipIfWeChatAppRunningEnabled() {
        return service.skipIfWeChatAppRunningEnabled();
    }

    @SneakyThrows
    public void setSkipIfWeChatAppRunningEnabled(boolean enabled) {
        service.setSkipIfWeChatAppRunningEnabled(enabled);
    }

    @SneakyThrows
    public boolean shouldHookBroadcastPerformResult() {
        return service.shouldHookBroadcastPerformResult();
    }

    @SneakyThrows
    public int onHookBroadcastPerformResult(Intent intent, int resultCode) {
        return service.onHookBroadcastPerformResult(intent, resultCode);
    }
}
