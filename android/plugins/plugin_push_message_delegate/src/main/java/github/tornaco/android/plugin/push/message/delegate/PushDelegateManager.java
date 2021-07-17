package github.tornaco.android.plugin.push.message.delegate;

import android.content.Context;
import android.os.IBinder;

import com.elvishew.xlog.XLog;

import github.tornaco.android.thanos.core.app.ThanosManager;
import lombok.SneakyThrows;
import util.Consumer;

public class PushDelegateManager {

    private IPushDelegateManager manager;

    public PushDelegateManager(Context context) {
        ThanosManager.from(context)
                .ifServiceInstalled(new Consumer<ThanosManager>() {
                    @Override
                    public void accept(ThanosManager thanosManager) {
                        IBinder binder = thanosManager.getServiceManager().getService("push_delegate");
                        if (binder != null) {
                            manager = IPushDelegateManager.Stub.asInterface(binder);
                            XLog.w("IPushDelegateManager manager is: %s", manager);
                        }
                    }
                });
    }

    public boolean isServiceInstalled() {
        return manager != null && manager.asBinder().pingBinder();
    }

    @SneakyThrows
    public boolean wechatEnabled() {
        return manager.wechatEnabled();
    }

    @SneakyThrows
    public void setWeChatEnabled(boolean enabled) {
        manager.setWeChatEnabled(enabled);
    }

    @SneakyThrows
    public boolean wechatSoundEnabled() {
        return manager.wechatSoundEnabled();
    }

    @SneakyThrows
    public void setWechatSoundEnabled(boolean enabled) {
        manager.setWechatSoundEnabled(enabled);
    }

    @SneakyThrows
    public boolean wechatContentEnabled() {
        return manager.wechatContentEnabled();
    }

    @SneakyThrows
    public void setWechatContentEnabled(boolean enabled) {
        manager.setWechatContentEnabled(enabled);
    }

    @SneakyThrows
    public boolean wechatVibrateEnabled() {
        return manager.wechatVibrateEnabled();
    }

    @SneakyThrows
    public void setWechatVibrateEnabled(boolean enabled) {
        manager.setWechatVibrateEnabled(enabled);
    }

    @SneakyThrows
    public void mockWechatMessage() {
        manager.mockWechatMessage();
    }

    @SneakyThrows
    public boolean startWechatOnPushEnabled() {
        return manager.startWechatOnPushEnabled();
    }

    @SneakyThrows
    public void setStartWechatOnPushEnabled(boolean enabled) {
        manager.setStartWechatOnPushEnabled(enabled);
    }

    @SneakyThrows
    public boolean skipIfWeChatAppRunningEnabled() {
        return manager.skipIfWeChatAppRunningEnabled();
    }

    @SneakyThrows
    public void setSkipIfWeChatAppRunningEnabled(boolean enabled) {
        manager.setSkipIfWeChatAppRunningEnabled(enabled);
    }
}
