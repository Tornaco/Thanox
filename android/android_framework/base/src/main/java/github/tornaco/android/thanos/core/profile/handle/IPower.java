package github.tornaco.android.thanos.core.profile.handle;

@HandlerName("power")
interface IPower {

    void sleep(long delay);

    void wakeup(long delay);

    void setBrightness(int level);

    int getBrightness();

    void setAutoBrightnessEnabled(boolean enable);

    boolean isAutoBrightnessEnabled();
}
