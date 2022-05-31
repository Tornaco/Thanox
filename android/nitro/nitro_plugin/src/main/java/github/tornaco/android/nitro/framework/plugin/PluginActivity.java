package github.tornaco.android.nitro.framework.plugin;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Application;
import android.app.FragmentManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toolbar;

import androidx.annotation.Keep;

import java.util.Objects;

import github.tornaco.android.nitro.framework.plugin.iface.HostActivityInvokerInterface;
import github.tornaco.android.nitro.framework.plugin.util.ReflectionUtils;
import github.tornaco.android.nitro.framework.plugin.wrapper.HostActivityInvokerWrapper;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@ToString
@SuppressLint({"MissingSuperCall", "Registered"})
@Keep
public class PluginActivity extends Activity {
    private static final String TAG = "PluginActivity";
    @Getter
    private final PluginContext pluginContext;
    @Setter
    @Getter
    private int themeResource;
    private HostActivityInvokerInterface hostActivityInvoker;

    public PluginActivity() {
        this.pluginContext = new PluginContext();
    }

    public void setHostActivityInvoker(Object hostActivityInvoker) {
        this.hostActivityInvoker = new HostActivityInvokerWrapper(hostActivityInvoker);
        copyFields();
    }

    private void copyFields() {
        // set app.
        Application hostApp = this.hostActivityInvoker.getApplication();
        Log.d(TAG, "setHostActivityInvoker, host app: " + hostApp);
        ReflectionUtils.setField(
                Objects.requireNonNull(ReflectionUtils.findField(Activity.class, "mApplication")),
                this,
                hostApp);
        Log.d(TAG, "setHostActivityInvoker, getApplication: " + getApplication());

        Window window = this.hostActivityInvoker.getWindow();
        Log.d(TAG, "setHostActivityInvoker, host window: " + window);
        ReflectionUtils.setField(
                Objects.requireNonNull(ReflectionUtils.findField(Activity.class, "mWindow")),
                this,
                window);
    }

    public Activity getHostActivity() {
        return hostActivityInvoker.getHostActivity();
    }

    public Application getHostApplication() {
        return hostActivityInvoker.getApplication();
    }

    public Resources getPluginResources() {
        return pluginContext.getPluginResources();
    }

    public void setPluginResources(Resources pluginResources) {
        Log.d(TAG, "setPluginResources: " + pluginResources);
        pluginContext.setPluginResources(pluginResources);
    }

    // Must override for N.
    @Override
    public Resources getResources() {
        return pluginContext.getResources();
    }

    public void setPluginClassLoader(ClassLoader pluginClassLoader) {
        pluginContext.setPluginClassLoader(pluginClassLoader);
    }

    public void attachHostBaseContext(Context context) {
        pluginContext.attachHostBaseContext(context);
        attachBaseContext(pluginContext);
    }

    @Override
    public void setTheme(int resId) {
        themeResource = resId;
        hostActivityInvoker.setTheme(resId);
        pluginContext.setTheme(resId);
        super.setTheme(resId);
    }

    @Override
    public Resources.Theme getTheme() {
        return super.getTheme();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        hostActivityInvoker.superOnCreate(savedInstanceState);
        setTheme(themeResource);
        pluginContext.setTheme(themeResource);
        super.setTheme(themeResource);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        hostActivityInvoker.onPostCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        hostActivityInvoker.superOnStart();
    }

    @Override
    public void onResume() {
        hostActivityInvoker.superOnResume();
    }

    @Override
    protected void onPostResume() {
        hostActivityInvoker.onPostResume();
    }

    @Override
    public void onPause() {
        hostActivityInvoker.superOnPause();
    }

    @Override
    public void onRestart() {
        hostActivityInvoker.superOnRestart();
    }

    @Override
    public void onStop() {
        hostActivityInvoker.superOnStop();
    }

    @Override
    public void recreate() {
        hostActivityInvoker.superRecreate();
    }

    @Override
    public void onDestroy() {
        hostActivityInvoker.superOnDestroy();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return hostActivityInvoker.superDispatchKeyEvent(event);
    }

    @Override
    public void onNewIntent(Intent intent) {
        hostActivityInvoker.superOnNewIntent(intent);
    }

    @Override
    public Intent getIntent() {
        return hostActivityInvoker.getIntent();
    }

    @Override
    public ActionBar getActionBar() {
        return hostActivityInvoker.getActionBar();
    }

    @Override
    public void setActionBar(Toolbar toolbar) {
        hostActivityInvoker.setActionBar(toolbar);
    }

    @Override
    public void setTitle(CharSequence title) {
        hostActivityInvoker.setTitle(title);
    }

    @Override
    public void setTitle(int titleRes) {
        hostActivityInvoker.setTitle(titleRes);
    }

    @Override
    public void setContentView(View view) {
        hostActivityInvoker.setContentView(view);
    }

    @Override
    public void setContentView(int layoutResID) {
        View inflate = LayoutInflater.from(this).inflate(layoutResID, null);
        hostActivityInvoker.setContentView(inflate);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return hostActivityInvoker.superOnCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return hostActivityInvoker.superOnOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        hostActivityInvoker.superOnBackPressed();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return hostActivityInvoker.superOnTouchEvent(event);
    }

    @Override
    public void finish() {
        hostActivityInvoker.finish();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        hostActivityInvoker.superOnSaveInstanceState(outState);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    @NonNull
    public LayoutInflater getLayoutInflater() {
        return (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        hostActivityInvoker.startActivityForResult(intent, requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        hostActivityInvoker.superOnRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        hostActivityInvoker.superOnActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        hostActivityInvoker.superOnRestoreInstanceState(savedInstanceState);
    }

    @Override
    public <T extends View> T findViewById(int id) {
        return hostActivityInvoker.findViewById(id);
    }

    @Override
    protected void onApplyThemeResource(Resources.Theme theme, int resId, boolean first) {
        hostActivityInvoker.onApplyThemeResource(theme, resId, first);
    }

    @Override
    public Window getWindow() {
        return hostActivityInvoker.getWindow();
    }

    @Override
    public ComponentName getComponentName() {
        return hostActivityInvoker.getComponentName();
    }

    @Override
    public FragmentManager getFragmentManager() {
        return hostActivityInvoker.getFragmentManager();
    }

    @Override
    public ApplicationInfo getApplicationInfo() {
        return hostActivityInvoker.getApplicationInfo();
    }

    @Override
    public SharedPreferences getPreferences(int mode) {
        return super.getSharedPreferences(getLocalClassName(), mode);
    }

    public String getLocalClassName() {
        return this.getClass().getName();
    }

    @Override
    public MenuInflater getMenuInflater() {
        return hostActivityInvoker.getMenuInflater();
    }

    @Override
    public void startActivity(Intent intent) {
        hostActivityInvoker.startActivity(intent);
    }

    @Override
    public WindowManager getWindowManager() {
        return hostActivityInvoker.getWindowManager();
    }

    @Override
    public Object getSystemService(String name) {
        if (WINDOW_SERVICE.equals(name)) {
            return getWindowManager();
        }
        return super.getSystemService(name);
    }

    @Override
    public PackageManager getPackageManager() {
        return new PackageManagerWrapper(super.getPackageManager());
    }
}
