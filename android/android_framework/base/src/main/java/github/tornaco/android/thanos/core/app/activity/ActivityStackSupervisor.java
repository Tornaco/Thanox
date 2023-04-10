package github.tornaco.android.thanos.core.app.activity;

import android.content.ComponentName;

import java.util.List;

import github.tornaco.android.thanos.core.app.component.ComponentReplacement;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

@AllArgsConstructor
public class ActivityStackSupervisor {
    private final IActivityStackSupervisor supervisor;

    @SneakyThrows
    public boolean shouldVerifyActivityStarting(ComponentName componentName, String pkg, String source) {
        return supervisor.shouldVerifyActivityStarting(componentName, pkg, source);
    }


    @SneakyThrows
    public String getCurrentFrontApp() {
        return supervisor.getCurrentFrontApp();
    }

    @SneakyThrows
    public void setAppLockEnabled(boolean enabled) {
        supervisor.setAppLockEnabled(enabled);
    }

    @SneakyThrows
    public boolean isAppLockEnabled() {
        return supervisor.isAppLockEnabled();
    }

    @SneakyThrows
    public boolean isPackageLocked(String pkg) {
        return supervisor.isPackageLocked(pkg);
    }

    @SneakyThrows
    public void setPackageLocked(String pkg, boolean locked) {
        supervisor.setPackageLocked(pkg, locked);
    }

    @SneakyThrows
    public void setVerifyResult(int request, int result, int reason) {
        supervisor.setVerifyResult(request, result, reason);
    }

    @SneakyThrows
    public void addComponentReplacement(ComponentReplacement replacement) {
        supervisor.addComponentReplacement(replacement);
    }

    @SneakyThrows
    public void removeComponentReplacement(ComponentReplacement replacement) {
        supervisor.removeComponentReplacement(replacement);
    }

    @SneakyThrows
    public List<ComponentReplacement> getComponentReplacements() {
        return supervisor.getComponentReplacements();
    }

    @SneakyThrows
    public void setActivityTrampolineEnabled(boolean enabled) {
        supervisor.setActivityTrampolineEnabled(enabled);
    }

    @SneakyThrows
    public boolean isActivityTrampolineEnabled() {
        return supervisor.isActivityTrampolineEnabled();
    }

    @SneakyThrows
    public void setShowCurrentComponentViewEnabled(boolean enabled) {
        supervisor.setShowCurrentComponentViewEnabled(enabled);
    }

    @SneakyThrows
    public boolean isShowCurrentComponentViewEnabled() {
        return supervisor.isShowCurrentComponentViewEnabled();
    }

    @SneakyThrows
    public void registerTopPackageChangeListener(TopPackageChangeListener listener) {
        supervisor.registerTopPackageChangeListener(listener.getListener());
    }

    @SneakyThrows
    public void unRegisterTopPackageChangeListener(TopPackageChangeListener listener) {
        supervisor.unRegisterTopPackageChangeListener(listener.getListener());
    }

    @SneakyThrows
    public boolean isVerifyOnScreenOffEnabled() {
        return supervisor.isVerifyOnScreenOffEnabled();
    }

    @SneakyThrows
    public void setVerifyOnScreenOffEnabled(boolean enabled) {
        supervisor.setVerifyOnScreenOffEnabled(enabled);
    }

    @SneakyThrows
    public boolean isVerifyOnAppSwitchEnabled() {
        return supervisor.isVerifyOnAppSwitchEnabled();
    }

    @SneakyThrows
    public void setVerifyOnAppSwitchEnabled(boolean enabled) {
        supervisor.setVerifyOnAppSwitchEnabled(enabled);
    }

    @SneakyThrows
    public boolean isVerifyOnTaskRemovedEnabled() {
        return supervisor.isVerifyOnTaskRemovedEnabled();
    }

    @SneakyThrows
    public void setVerifyOnTaskRemovedEnabled(boolean enabled) {
        supervisor.setVerifyOnTaskRemovedEnabled(enabled);
    }

    @SneakyThrows
    public void registerActivityLifecycleListener(IActivityLifecycleListener listener) {
        supervisor.registerActivityLifecycleListener(listener);
    }

    @SneakyThrows
    public void unRegisterActivityLifecycleListener(IActivityLifecycleListener listener) {
        supervisor.unRegisterActivityLifecycleListener(listener);
    }
}
