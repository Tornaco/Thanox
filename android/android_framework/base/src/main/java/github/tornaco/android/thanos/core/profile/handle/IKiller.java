package github.tornaco.android.thanos.core.profile.handle;

@HandlerName("killer")
interface IKiller {

    boolean killPackage(String pkgName);
}
