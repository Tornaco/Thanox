package github.tornaco.android.thanos.core.app;

import android.content.Context;
import android.content.IntentFilter;

import com.elvishew.xlog.XLog;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import github.tornaco.android.thanos.core.IThanos;
import github.tornaco.android.thanos.core.app.activity.ActivityStackSupervisor;
import github.tornaco.android.thanos.core.app.event.IEventSubscriber;
import github.tornaco.android.thanos.core.app.infinite.InfiniteZManager;
import github.tornaco.android.thanos.core.app.usage.UsageStatsManager;
import github.tornaco.android.thanos.core.audio.AudioManager;
import github.tornaco.android.thanos.core.backup.BackupAgent;
import github.tornaco.android.thanos.core.input.InputManager;
import github.tornaco.android.thanos.core.n.NotificationManager;
import github.tornaco.android.thanos.core.net.NetworkManager;
import github.tornaco.android.thanos.core.ops.OpsManager;
import github.tornaco.android.thanos.core.os.ServiceManager;
import github.tornaco.android.thanos.core.plus.RSManager;
import github.tornaco.android.thanos.core.pm.PackageManager;
import github.tornaco.android.thanos.core.power.PowerManager;
import github.tornaco.android.thanos.core.pref.PrefManager;
import github.tornaco.android.thanos.core.profile.ProfileManager;
import github.tornaco.android.thanos.core.push.PushManager;
import github.tornaco.android.thanos.core.push.wechat.PushDelegateManager;
import github.tornaco.android.thanos.core.secure.PrivacyManager;
import github.tornaco.android.thanos.core.secure.ops.AppOpsManager;
import github.tornaco.android.thanos.core.wm.WindowManager;
import lombok.Getter;
import lombok.SneakyThrows;
import util.Consumer;

public class ThanosManager {
    public static final String PROXIED_ANDROID_SERVICE_NAME = Context.DROPBOX_SERVICE;
    public static final int IPC_TRANS_CODE_THANOS_SERVER = "github.tornaco.android.thanos.core.IPC_TRANS_CODE_THANOS_SERVER".hashCode();

    private IThanos service;
    @Getter
    private Context context;

    public ThanosManager(Context context, IThanos service) {
        this.context = context;
        this.service = service;
    }

    public boolean isServiceInstalled() {
        boolean firstCheck = service != null && service.asBinder() != null && service.asBinder().isBinderAlive();
        if (!firstCheck) {
            return false;
        }
        try {
            String answer = service.whoAreYou();
            if (answer == null) {
                return false;
            }
            if (!answer.contains("Thanox")) {
                return false;
            }
        } catch (Throwable e) {
            XLog.e("Ask for thanox server error", e);
            return false;
        }
        return true;
    }

    public void ifServiceInstalled(Consumer<ThanosManager> consumer) {
        if (isServiceInstalled()) {
            consumer.accept(this);
        }
    }

    @SneakyThrows
    public ServiceManager getServiceManager() {
        return new ServiceManager(service.getServiceManager());
    }

    @SneakyThrows
    public PrefManager getPrefManager() {
        return new PrefManager(service.getPrefManager());
    }

    @SneakyThrows
    public PackageManager getPkgManager() {
        return new PackageManager(service.getPkgManager());
    }

    @SneakyThrows
    public PrivacyManager getPrivacyManager() {
        return new PrivacyManager(service.getPrivacyManager());
    }

    @SneakyThrows
    public ActivityManager getActivityManager() {
        return new ActivityManager(service.getActivityManager(), service.getPkgManager());
    }

    @SneakyThrows
    public ActivityStackSupervisor getActivityStackSupervisor() {
        return new ActivityStackSupervisor(service.getActivityStackSupervisor());
    }

    @SneakyThrows
    public AppOpsManager getAppOpsManager() {
        return new AppOpsManager(context, service.getAppOpsService());
    }

    @SneakyThrows
    public PushManager getPushManager() {
        return new PushManager(service.getPushManager());
    }

    @SneakyThrows
    public NotificationManager getNotificationManager() {
        return new NotificationManager(service.getNotificationManager());
    }

    @SneakyThrows
    public BackupAgent getBackupAgent() {
        return new BackupAgent(service.getBackupAgent());
    }

    @SneakyThrows
    public ProfileManager getProfileManager() {
        return new ProfileManager(service.getProfileManager());
    }

    @SneakyThrows
    public InputManager getInputManager() {
        return new InputManager(service.getInputManager());
    }

    @SneakyThrows
    public PowerManager getPowerManager() {
        return new PowerManager(service.getPowerManager());
    }

    @SneakyThrows
    public RSManager getRSManager() {
        return new RSManager(service.getRS());
    }

    @SneakyThrows
    public UsageStatsManager getUsageStatsManager() {
        return new UsageStatsManager(service.getUsageStatsManager());
    }

    @SneakyThrows
    public WindowManager getWindowManager() {
        return new WindowManager(service.getWindowManager());
    }

    @SneakyThrows
    public NetworkManager getNetworkManager() {
        return new NetworkManager(service.getNetworkManager());
    }

    @SneakyThrows
    public AudioManager getAudioManager() {
        return new AudioManager(service.getAudioManager());
    }

    @SneakyThrows
    public InfiniteZManager getInfiniteZ() {
        return new InfiniteZManager(service.getInfiniteZ());
    }

    @SneakyThrows
    public PushDelegateManager getPushDelegateManager() {
        return new PushDelegateManager(service.getPushDelegateManager());
    }

    @SneakyThrows
    public OpsManager getOpsManager() {
        return new OpsManager(service.getOpsManager());
    }

    @SneakyThrows
    public void registerEventSubscriber(IntentFilter filter, IEventSubscriber subscriber) {
        service.registerEventSubscriber(filter, subscriber);
    }

    @SneakyThrows
    public void unRegisterEventSubscriber(IEventSubscriber subscriber) {
        service.unRegisterEventSubscriber(subscriber);
    }

    @SneakyThrows
    public String fingerPrint() {
        return service.fingerPrint();
    }

    @SneakyThrows
    public String getVersionName() {
        return service.getVersionName();
    }

    @SneakyThrows
    public boolean isLoggingEnabled() {
        return service.isLoggingEnabled();
    }

    @SneakyThrows
    public void setLoggingEnabled(boolean enable) {
        service.setLoggingEnabled(enable);
    }

    @SneakyThrows
    public boolean hasFeature(String feature) {
        return service.hasFeature(feature);
    }

    @SneakyThrows
    public boolean hasFrameworkInitializeError() {
        return service.hasFrameworkInitializeError();
    }

    @SneakyThrows
    public List<String> getPatchingSource() {
        return service.getPatchingSource().stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    public static ThanosManager from(Context context) {
        return new ThanosManager(context, ThanosManagerNative.getDefault());
    }
}
