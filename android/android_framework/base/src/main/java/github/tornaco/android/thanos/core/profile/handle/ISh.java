package github.tornaco.android.thanos.core.profile.handle;

@HandlerName("sh")
public
interface ISh {
    Object /* Shell.Result */ exe(String command);
}
