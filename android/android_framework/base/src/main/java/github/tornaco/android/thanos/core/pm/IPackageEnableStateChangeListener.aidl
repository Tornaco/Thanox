package github.tornaco.android.thanos.core.pm;

interface IPackageEnableStateChangeListener {
   oneway void onPackageEnableStateChanged(in List<Pkg> pkgs);
}
