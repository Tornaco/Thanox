package github.tornaco.android.thanos.core.profile.handle;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.UserHandle;
import android.view.Display;
import android.view.DisplayAdjustments;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import github.tornaco.android.thanos.core.util.ArrayUtils;
import github.tornaco.android.thanos.core.util.function.Function;

/**
 * Android Context API: https://developer.android.com/reference/android/content/Context
 * No extra API.
 */
@HandlerName("context")
public abstract class IAndroidContext extends Context {

    public static class RefinedAndroidContext extends Stub {
        private static final String[] EXPOSED_SERVICES = new String[]{
                Context.NETWORK_STATS_SERVICE,
                Context.BLUETOOTH_SERVICE,
                Context.CLIPBOARD_SERVICE,
        };

        private final Function<String, Object> getSystemService;

        public RefinedAndroidContext(Function<String, Object> getSystemService) {
            this.getSystemService = getSystemService;
        }

        @Override
        public Object getSystemService(String serviceName) {
            if (ArrayUtils.contains(EXPOSED_SERVICES, serviceName)) {
                return getSystemService.apply(serviceName);
            } else {
                return null;
            }
        }
    }

    static class Stub extends IAndroidContext {
        @Override
        public AssetManager getAssets() {
            return null;
        }

        @Override
        public Resources getResources() {
            return null;
        }

        @Override
        public PackageManager getPackageManager() {
            return null;
        }

        @Override
        public ContentResolver getContentResolver() {
            return null;
        }

        @Override
        public Looper getMainLooper() {
            return null;
        }

        @Override
        public Context getApplicationContext() {
            return null;
        }

        @Override
        public void setTheme(int i) {

        }

        @Override
        public Resources.Theme getTheme() {
            return null;
        }

        @Override
        public ClassLoader getClassLoader() {
            return null;
        }

        @Override
        public String getPackageName() {
            return null;
        }

        @Override
        public String getBasePackageName() {
            return null;
        }

        @Override
        public String getOpPackageName() {
            return null;
        }

        @Override
        public ApplicationInfo getApplicationInfo() {
            return null;
        }

        @Override
        public String getPackageResourcePath() {
            return null;
        }

        @Override
        public String getPackageCodePath() {
            return null;
        }

        @Override
        public SharedPreferences getSharedPreferences(String s, int i) {
            return null;
        }

        @Override
        public SharedPreferences getSharedPreferences(File file, int i) {
            return null;
        }

        @Override
        public boolean moveSharedPreferencesFrom(Context context, String s) {
            return false;
        }

        @Override
        public boolean deleteSharedPreferences(String s) {
            return false;
        }

        @Override
        public void reloadSharedPreferences() {

        }

        @Override
        public FileInputStream openFileInput(String s) throws FileNotFoundException {
            return null;
        }

        @Override
        public FileOutputStream openFileOutput(String s, int i) throws FileNotFoundException {
            return null;
        }

        @Override
        public boolean deleteFile(String s) {
            return false;
        }

        @Override
        public File getFileStreamPath(String s) {
            return null;
        }

        @Override
        public File getSharedPreferencesPath(String s) {
            return null;
        }

        @Override
        public File getDataDir() {
            return null;
        }

        @Override
        public File getFilesDir() {
            return null;
        }

        @Override
        public File getNoBackupFilesDir() {
            return null;
        }

        @Override
        public File getExternalFilesDir(String s) {
            return null;
        }

        @Override
        public File[] getExternalFilesDirs(String s) {
            return new File[0];
        }

        @Override
        public File getObbDir() {
            return null;
        }

        @Override
        public File[] getObbDirs() {
            return new File[0];
        }

        @Override
        public File getCacheDir() {
            return null;
        }

        @Override
        public File getCodeCacheDir() {
            return null;
        }

        @Override
        public File getExternalCacheDir() {
            return null;
        }

        @Override
        public File getPreloadsFileCache() {
            return null;
        }

        @Override
        public File[] getExternalCacheDirs() {
            return new File[0];
        }

        @Override
        public File[] getExternalMediaDirs() {
            return new File[0];
        }

        @Override
        public String[] fileList() {
            return new String[0];
        }

        @Override
        public File getDir(String s, int i) {
            return null;
        }

        @Override
        public SQLiteDatabase openOrCreateDatabase(String s, int i, SQLiteDatabase.CursorFactory cursorFactory) {
            return null;
        }

        @Override
        public SQLiteDatabase openOrCreateDatabase(String s, int i, SQLiteDatabase.CursorFactory cursorFactory, DatabaseErrorHandler databaseErrorHandler) {
            return null;
        }

        @Override
        public boolean moveDatabaseFrom(Context context, String s) {
            return false;
        }

        @Override
        public boolean deleteDatabase(String s) {
            return false;
        }

        @Override
        public File getDatabasePath(String s) {
            return null;
        }

        @Override
        public String[] databaseList() {
            return new String[0];
        }

        @Override
        public Drawable getWallpaper() {
            return null;
        }

        @Override
        public Drawable peekWallpaper() {
            return null;
        }

        @Override
        public int getWallpaperDesiredMinimumWidth() {
            return 0;
        }

        @Override
        public int getWallpaperDesiredMinimumHeight() {
            return 0;
        }

        @Override
        public void setWallpaper(Bitmap bitmap) throws IOException {

        }

        @Override
        public void setWallpaper(InputStream inputStream) throws IOException {

        }

        @Override
        public void clearWallpaper() throws IOException {

        }

        @Override
        public void startActivity(Intent intent) {

        }

        @Override
        public void startActivity(Intent intent, Bundle bundle) {

        }

        @Override
        public void startActivities(Intent[] intents) {

        }

        @Override
        public void startActivities(Intent[] intents, Bundle bundle) {

        }

        @Override
        public void startIntentSender(IntentSender intentSender, Intent intent, int i, int i1, int i2) throws IntentSender.SendIntentException {

        }

        @Override
        public void startIntentSender(IntentSender intentSender, Intent intent, int i, int i1, int i2, Bundle bundle) throws IntentSender.SendIntentException {

        }

        @Override
        public void sendBroadcast(Intent intent) {

        }

        @Override
        public void sendBroadcast(Intent intent, String s) {

        }

        @Override
        public void sendBroadcastMultiplePermissions(Intent intent, String[] strings) {

        }

        @Override
        public void sendBroadcast(Intent intent, String s, Bundle bundle) {

        }

        @Override
        public void sendBroadcast(Intent intent, String s, int i) {

        }

        @Override
        public void sendOrderedBroadcast(Intent intent, String s) {

        }

        @Override
        public void sendOrderedBroadcast(Intent intent, String s, BroadcastReceiver broadcastReceiver, Handler handler, int i, String s1, Bundle bundle) {

        }

        @Override
        public void sendOrderedBroadcast(Intent intent, String s, Bundle bundle, BroadcastReceiver broadcastReceiver, Handler handler, int i, String s1, Bundle bundle1) {

        }

        @Override
        public void sendOrderedBroadcast(Intent intent, String s, int i, BroadcastReceiver broadcastReceiver, Handler handler, int i1, String s1, Bundle bundle) {

        }

        @Override
        public void sendBroadcastAsUser(Intent intent, UserHandle userHandle) {

        }

        @Override
        public void sendBroadcastAsUser(Intent intent, UserHandle userHandle, String s) {

        }

        @Override
        public void sendBroadcastAsUser(Intent intent, UserHandle userHandle, String s, Bundle bundle) {

        }

        @Override
        public void sendBroadcastAsUser(Intent intent, UserHandle userHandle, String s, int i) {

        }

        @Override
        public void sendOrderedBroadcastAsUser(Intent intent, UserHandle userHandle, String s, BroadcastReceiver broadcastReceiver, Handler handler, int i, String s1, Bundle bundle) {

        }

        @Override
        public void sendOrderedBroadcastAsUser(Intent intent, UserHandle userHandle, String s, int i, BroadcastReceiver broadcastReceiver, Handler handler, int i1, String s1, Bundle bundle) {

        }

        @Override
        public void sendOrderedBroadcastAsUser(Intent intent, UserHandle userHandle, String s, int i, Bundle bundle, BroadcastReceiver broadcastReceiver, Handler handler, int i1, String s1, Bundle bundle1) {

        }

        @Override
        public void sendStickyBroadcast(Intent intent) {

        }

        @Override
        public void sendStickyOrderedBroadcast(Intent intent, BroadcastReceiver broadcastReceiver, Handler handler, int i, String s, Bundle bundle) {

        }

        @Override
        public void removeStickyBroadcast(Intent intent) {

        }

        @Override
        public void sendStickyBroadcastAsUser(Intent intent, UserHandle userHandle) {

        }

        @Override
        public void sendStickyBroadcastAsUser(Intent intent, UserHandle userHandle, Bundle bundle) {

        }

        @Override
        public void sendStickyOrderedBroadcastAsUser(Intent intent, UserHandle userHandle, BroadcastReceiver broadcastReceiver, Handler handler, int i, String s, Bundle bundle) {

        }

        @Override
        public void removeStickyBroadcastAsUser(Intent intent, UserHandle userHandle) {

        }

        @Override
        public Intent registerReceiver(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter) {
            return null;
        }

        @Override
        public Intent registerReceiver(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter, int i) {
            return null;
        }

        @Override
        public Intent registerReceiver(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter, String s, Handler handler) {
            return null;
        }

        @Override
        public Intent registerReceiver(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter, String s, Handler handler, int i) {
            return null;
        }

        @Override
        public Intent registerReceiverAsUser(BroadcastReceiver broadcastReceiver, UserHandle userHandle, IntentFilter intentFilter, String s, Handler handler) {
            return null;
        }

        @Override
        public void unregisterReceiver(BroadcastReceiver broadcastReceiver) {

        }

        @Override
        public ComponentName startService(Intent intent) {
            return null;
        }

        @Override
        public ComponentName startForegroundService(Intent intent) {
            return null;
        }

        @Override
        public ComponentName startForegroundServiceAsUser(Intent intent, UserHandle userHandle) {
            return null;
        }

        @Override
        public boolean stopService(Intent intent) {
            return false;
        }

        @Override
        public ComponentName startServiceAsUser(Intent intent, UserHandle userHandle) {
            return null;
        }

        @Override
        public boolean stopServiceAsUser(Intent intent, UserHandle userHandle) {
            return false;
        }

        @Override
        public boolean bindService(Intent intent, ServiceConnection serviceConnection, int i) {
            return false;
        }

        @Override
        public void unbindService(ServiceConnection serviceConnection) {

        }

        @Override
        public boolean startInstrumentation(ComponentName componentName, String s, Bundle bundle) {
            return false;
        }

        @Override
        public Object getSystemService(String s) {
            return null;
        }

        @Override
        public String getSystemServiceName(Class<?> aClass) {
            return null;
        }

        @Override
        public int checkPermission(String s, int i, int i1) {
            return 0;
        }

        @Override
        public int checkPermission(String s, int i, int i1, IBinder iBinder) {
            return 0;
        }

        @Override
        public int checkCallingPermission(String s) {
            return 0;
        }

        @Override
        public int checkCallingOrSelfPermission(String s) {
            return 0;
        }

        @Override
        public int checkSelfPermission(String s) {
            return 0;
        }

        @Override
        public void enforcePermission(String s, int i, int i1, String s1) {

        }

        @Override
        public void enforceCallingPermission(String s, String s1) {

        }

        @Override
        public void enforceCallingOrSelfPermission(String s, String s1) {

        }

        @Override
        public void grantUriPermission(String s, Uri uri, int i) {

        }

        @Override
        public void revokeUriPermission(Uri uri, int i) {

        }

        @Override
        public void revokeUriPermission(String s, Uri uri, int i) {

        }

        @Override
        public int checkUriPermission(Uri uri, int i, int i1, int i2) {
            return 0;
        }

        @Override
        public int checkUriPermission(Uri uri, int i, int i1, int i2, IBinder iBinder) {
            return 0;
        }

        @Override
        public int checkCallingUriPermission(Uri uri, int i) {
            return 0;
        }

        @Override
        public int checkCallingOrSelfUriPermission(Uri uri, int i) {
            return 0;
        }

        @Override
        public int checkUriPermission(Uri uri, String s, String s1, int i, int i1, int i2) {
            return 0;
        }

        @Override
        public void enforceUriPermission(Uri uri, int i, int i1, int i2, String s) {

        }

        @Override
        public void enforceCallingUriPermission(Uri uri, int i, String s) {

        }

        @Override
        public void enforceCallingOrSelfUriPermission(Uri uri, int i, String s) {

        }

        @Override
        public void enforceUriPermission(Uri uri, String s, String s1, int i, int i1, int i2, String s2) {

        }

        @Override
        public Context createPackageContext(String s, int i) throws PackageManager.NameNotFoundException {
            return null;
        }

        @Override
        public Context createPackageContextAsUser(String s, int i, UserHandle userHandle) throws PackageManager.NameNotFoundException {
            return null;
        }

        @Override
        public Context createApplicationContext(ApplicationInfo applicationInfo, int i) throws PackageManager.NameNotFoundException {
            return null;
        }

        @Override
        public Context createContextForSplit(String s) throws PackageManager.NameNotFoundException {
            return null;
        }

        @Override
        public int getUserId() {
            return 0;
        }

        @Override
        public Context createConfigurationContext(Configuration configuration) {
            return null;
        }

        @Override
        public Context createDisplayContext(Display display) {
            return null;
        }

        @Override
        public Context createDeviceProtectedStorageContext() {
            return null;
        }

        @Override
        public Context createCredentialProtectedStorageContext() {
            return null;
        }

        @Override
        public DisplayAdjustments getDisplayAdjustments(int i) {
            return null;
        }

        @Override
        public Display getDisplay() {
            return null;
        }

        @Override
        public void updateDisplay(int i) {

        }

        @Override
        public boolean isDeviceProtectedStorage() {
            return false;
        }

        @Override
        public boolean isCredentialProtectedStorage() {
            return false;
        }

        @Override
        public boolean canLoadUnsafeResources() {
            return false;
        }
    }
}