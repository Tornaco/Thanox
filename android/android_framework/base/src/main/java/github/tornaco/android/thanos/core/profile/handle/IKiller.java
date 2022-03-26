package github.tornaco.android.thanos.core.profile.handle;

@HandlerName("killer")
public
interface IKiller {

    boolean killPackage(String pkgName);
}
