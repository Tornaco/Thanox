package github.tornaco.android.thanos.core.app;

import android.content.Context;
import android.content.IntentFilter;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;

import com.elvishew.xlog.XLog;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import github.tornaco.android.thanos.core.IPluginLogger;
import github.tornaco.android.thanos.core.IThanos;
import github.tornaco.android.thanos.core.app.activity.ActivityStackSupervisor;
import github.tornaco.android.thanos.core.app.activity.IActivityStackSupervisor;
import github.tornaco.android.thanos.core.app.event.IEventSubscriber;
import github.tornaco.android.thanos.core.app.infinite.InfiniteZ;
import github.tornaco.android.thanos.core.app.infinite.InfiniteZManager;
import github.tornaco.android.thanos.core.app.usage.IUsageStatsManager;
import github.tornaco.android.thanos.core.app.usage.UsageStatsManager;
import github.tornaco.android.thanos.core.audio.AudioManager;
import github.tornaco.android.thanos.core.audio.IAudioManager;
import github.tornaco.android.thanos.core.backup.BackupAgent;
import github.tornaco.android.thanos.core.backup.IBackupAgent;
import github.tornaco.android.thanos.core.input.IInputManager;
import github.tornaco.android.thanos.core.input.InputManager;
import github.tornaco.android.thanos.core.n.INotificationManager;
import github.tornaco.android.thanos.core.n.NotificationManager;
import github.tornaco.android.thanos.core.net.INetworkManager;
import github.tornaco.android.thanos.core.net.NetworkManager;
import github.tornaco.android.thanos.core.ops.IOps;
import github.tornaco.android.thanos.core.ops.OpsManager;
import github.tornaco.android.thanos.core.os.IServiceManager;
import github.tornaco.android.thanos.core.os.ServiceManager;
import github.tornaco.android.thanos.core.pm.IPkgManager;
import github.tornaco.android.thanos.core.pm.PackageManager;
import github.tornaco.android.thanos.core.power.IPowerManager;
import github.tornaco.android.thanos.core.power.PowerManager;
import github.tornaco.android.thanos.core.pref.IPrefManager;
import github.tornaco.android.thanos.core.pref.PrefManager;
import github.tornaco.android.thanos.core.profile.IProfileManager;
import github.tornaco.android.thanos.core.profile.ProfileManager;
import github.tornaco.android.thanos.core.push.IPushManager;
import github.tornaco.android.thanos.core.push.PushManager;
import github.tornaco.android.thanos.core.push.wechat.IPushDelegateManager;
import github.tornaco.android.thanos.core.push.wechat.PushDelegateManager;
import github.tornaco.android.thanos.core.secure.IPrivacyManager;
import github.tornaco.android.thanos.core.secure.PrivacyManager;
import github.tornaco.android.thanos.core.secure.ops.AppOpsManager;
import github.tornaco.android.thanos.core.secure.ops.IAppOpsService;
import github.tornaco.android.thanos.core.wm.IWindowManager;
import github.tornaco.android.thanos.core.wm.WindowManager;
import lombok.Getter;
import lombok.SneakyThrows;
import util.Consumer;

public class ThanosManager {
    public static final String PROXIED_ANDROID_SERVICE_NAME = Context.DROPBOX_SERVICE;
    public static final int IPC_TRANS_CODE_THANOS_SERVER = "github.tornaco.android.thanos.core.IPC_TRANS_CODE_THANOS_SERVER".hashCode();

    private static final IThanos sDefaultFallbackService = new IThanos.Default() {
        @Override
        public IAppOpsService getAppOpsService() throws RemoteException {
            return new IAppOpsService.Default();
        }

        @Override
        public IServiceManager getServiceManager() throws RemoteException {
            return new IServiceManager.Default();
        }

        @Override
        public IActivityManager getActivityManager() throws RemoteException {
            return new IActivityManager.Default();
        }

        @Override
        public IActivityStackSupervisor getActivityStackSupervisor() throws RemoteException {
            return new IActivityStackSupervisor.Default();
        }

        @Override
        public IAudioManager getAudioManager() throws RemoteException {
            return new IAudioManager.Default();
        }

        @Override
        public IBackupAgent getBackupAgent() throws RemoteException {
            return new IBackupAgent.Default();
        }

        @Override
        public IInputManager getInputManager() throws RemoteException {
            return new IInputManager.Default();
        }

        @Override
        public INetworkManager getNetworkManager() throws RemoteException {
            return new INetworkManager.Default();
        }

        @Override
        public InfiniteZ getInfiniteZ() throws RemoteException {
            return new InfiniteZ.Default();
        }

        @Override
        public INotificationManager getNotificationManager() throws RemoteException {
            return new INotificationManager.Default();
        }

        @Override
        public IOps getOpsManager() throws RemoteException {
            return new IOps.Default();
        }

        @Override
        public IPkgManager getPkgManager() throws RemoteException {
            return new IPkgManager.Default();
        }

        @Override
        public IPluginLogger getPluginLogger(String pluginAlias) throws RemoteException {
            return new IPluginLogger.Default();
        }

        @Override
        public IPowerManager getPowerManager() throws RemoteException {
            return new IPowerManager.Default();
        }

        @Override
        public IPrefManager getPrefManager() throws RemoteException {
            return new IPrefManager.Default();
        }

        @Override
        public IPrivacyManager getPrivacyManager() throws RemoteException {
            return new IPrivacyManager.Default();
        }

        @Override
        public IProfileManager getProfileManager() throws RemoteException {
            return new IProfileManager.Default();
        }

        @Override
        public IPushDelegateManager getPushDelegateManager() throws RemoteException {
            return new IPushDelegateManager.Default();
        }

        @Override
        public IPushManager getPushManager() throws RemoteException {
            return new IPushManager.Default();
        }

        @Override
        public IUsageStatsManager getUsageStatsManager() throws RemoteException {
            return new IUsageStatsManager.Default();
        }

        @Override
        public IWindowManager getWindowManager() throws RemoteException {
            return new IWindowManager.Default();
        }

        @Override
        public List<String> getPatchingSource() throws RemoteException {
            return Collections.emptyList();
        }
    };

    private IThanos service;
    @Getter
    private Context context;

    public ThanosManager(Context context, IThanos service) {
        this.context = context;
        this.service = service;
    }

    public IThanos getService() {
        return service;
    }

    public boolean isServiceInstalled() {
        boolean firstCheck = service != null
                && sDefaultFallbackService != service
                && service.asBinder() != null
                && service.asBinder().isBinderAlive();
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

    @SneakyThrows
    public void writeLogsTo(ParcelFileDescriptor fd) {
        service.writeLogsTo(fd);
    }

    public static ThanosManager from(Context context) {
        IThanos def = ThanosManagerNative.getDefault();
        IThanos thanos = def;
        if (def == null) {
            XLog.w("Using sDefaultFallbackService");
            thanos = sDefaultFallbackService;
        }
        return new ThanosManager(context, thanos);
    }
}
