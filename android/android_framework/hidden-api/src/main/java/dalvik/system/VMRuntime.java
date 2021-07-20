package dalvik.system;

import android.annotation.UnsupportedAppUsage;

public class VMRuntime {
    /**
     * Sets the target SDK version. Should only be called before the
     * app starts to run, because it may change the VM's behavior in
     * dangerous ways. Defaults to {@link #SDK_VERSION_CUR_DEVELOPMENT}.
     *
     * @param targetSdkVersion the SDK version the app wants to run with.
     */
    @UnsupportedAppUsage(maxTargetSdk = 0, publicAlternatives = "Use the {@code targetSdkVersion}"
            + " attribute in the {@code uses-sdk} manifest tag instead.")
    public synchronized void setTargetSdkVersion(int targetSdkVersion) {
    }

    /**
     * Returns the object that represents the current runtime.
     *
     * @return the runtime object
     */
    @UnsupportedAppUsage
    public static VMRuntime getRuntime() {
        return null;
    }
}
