package github.tornaco.android.thanos.core.pm;

interface IAddPluginCallback {
   oneway void onPluginAdd();
   oneway void onFail(String message);
   oneway void onProgress(String progressMessage);
}
