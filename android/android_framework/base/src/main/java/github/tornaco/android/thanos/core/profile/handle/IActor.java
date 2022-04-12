package github.tornaco.android.thanos.core.profile.handle;

@HandlerName("actor")
@Deprecated
public interface IActor {
    void delayed(long delayMillis, String action);
}
