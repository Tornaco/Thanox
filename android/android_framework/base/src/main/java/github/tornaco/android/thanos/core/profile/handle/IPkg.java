package github.tornaco.android.thanos.core.profile.handle;

@HandlerName("pkg")
public
interface IPkg {

    void disableApplication(String pkg);

    void enableApplication(String pkg);

    boolean isApplicationEnabled(String pkg);
}
