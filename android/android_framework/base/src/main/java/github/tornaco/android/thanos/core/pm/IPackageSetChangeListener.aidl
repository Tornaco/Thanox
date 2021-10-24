package github.tornaco.android.thanos.core.pm;

interface IPackageSetChangeListener {
   oneway void onPackageSetAdded(String pkgSetId);
   oneway void onPackageSetRemoved(String pkgSetId);
   oneway void onPackageSetChanged(String pkgSetId);
}
