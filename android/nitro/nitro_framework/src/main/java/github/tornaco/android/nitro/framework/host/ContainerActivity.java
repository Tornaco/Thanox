package github.tornaco.android.nitro.framework.host;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Toolbar;

import androidx.annotation.NonNull;

import com.elvishew.xlog.XLog;

import github.tornaco.android.nitro.framework.host.res.MixResources;
import github.tornaco.android.nitro.framework.plugin.HostActivityInvoker;
import github.tornaco.android.nitro.framework.plugin.PluginActivityInvoker;
import github.tornaco.android.nitro.framework.plugin.wrapper.PluginActivityWrapper;

@SuppressLint("MissingSuperCall")
public class ContainerActivity extends Activity implements HostActivityInvoker {

    private PluginActivityInvoker pluginActivityInvoker;
    private MixResources mixResources;

    /**
     * Theme一旦设置了就不能更换Theme所在的Resources了，见{@link Resources.Theme#setTo(Resources.Theme)}
     * 而Activity在OnCreate之前需要设置Theme和使用Theme。我们需要在Activity OnCreate之后才能注入插件资源。
     * 这就需要在Activity OnCreate之前不要调用Activity的setTheme方法，同时在getTheme时返回宿主的Theme资源。
     * 注：{@link Activity#setTheme(int)}会触发初始化Theme，因此不能调用。
     */
    private Resources.Theme hostTheme;

    private boolean isBeforeOnCreate = true;

    public ContainerActivity() {
        pluginActivityInvoker = create();
        pluginActivityInvoker.setHostActivityInvoker(this);
    }

    private PluginActivityInvoker create() {
        return new PluginActivityInvoker();
    }

    @Override
    public Resources.Theme getTheme() {
        if (isBeforeOnCreate) {
            if (hostTheme == null) {
                hostTheme = super.getResources().newTheme();
            }
            return hostTheme;
        } else {
            return super.getTheme();
        }
    }

    @Override
    public void setTheme(int resId) {
        if (!isBeforeOnCreate) {
            super.setTheme(resId);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isBeforeOnCreate = false;
        //释放资源
        hostTheme = null;

        PluginActivityWrapper pluginActivity = Launcher.loadPluginActivityInstance(this);
        XLog.w("pluginActivity=%s", pluginActivity);
        if (pluginActivity == null) return;

        // Invoke lifecycle.
        pluginActivity.attachHostBaseContext(getHostActivity());
        this.mixResources = new MixResources(super.getResources(), pluginActivity.getPluginResources());

        // Theme.
        int pluginThemeRes = pluginActivity.getThemeResource();
        XLog.v("pluginThemeRes: %s", pluginThemeRes);
        setTheme(pluginThemeRes);

        this.pluginActivityInvoker.setPluginActivity(pluginActivity);
        // Call onCreate.
        this.pluginActivityInvoker.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        pluginActivityInvoker.onStart();
    }

    @Override
    protected void onResume() {
        pluginActivityInvoker.onResume();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        pluginActivityInvoker.onNewIntent(intent);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        pluginActivityInvoker.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        pluginActivityInvoker.onPause();
    }

    @Override
    protected void onRestart() {
        pluginActivityInvoker.onRestart();
    }

    @Override
    protected void onStop() {
        pluginActivityInvoker.onStop();
    }

    @Override
    protected void onDestroy() {
        pluginActivityInvoker.onDestroy();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return pluginActivityInvoker.dispatchKeyEvent(event);
    }

    @Override
    public void onBackPressed() {
        pluginActivityInvoker.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return pluginActivityInvoker.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return pluginActivityInvoker.onOptionsItemSelected(item);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return pluginActivityInvoker.onTouchEvent(event);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        pluginActivityInvoker.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public Activity getHostActivity() {
        return this;
    }

    @Override
    public void superOnCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void superOnStart() {
        super.onStart();
    }

    @Override
    public void superOnResume() {
        super.onResume();
    }

    @Override
    public void superOnNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    public void superOnSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void superOnPause() {
        super.onPause();
    }

    @Override
    public void superRecreate() {
        super.recreate();
    }

    @Override
    public void superOnRestart() {
        super.onRestart();
    }

    @Override
    public void superOnStop() {
        super.onStop();
    }

    @Override
    public void superOnDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPostResume() {
        super.onPostResume();
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public boolean superDispatchKeyEvent(KeyEvent event) {
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void superOnBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean superOnCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean superOnOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean superOnTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }

    @Override
    public void superOnActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void superOnRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void superOnRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public boolean isFinishing() {
        return super.isFinishing();
    }

    @Override
    public <T extends View> T findViewById(int id) {
        return super.findViewById(id);
    }

    @Override
    public ActionBar getActionBar() {
        return super.getActionBar();
    }

    @Override
    public void setTitle(CharSequence title) {
        XLog.i("setTitle: %s", title);
        super.setTitle(title);
    }

    @Override
    public void setTitle(int titleId) {
        XLog.i("setTitle: %s", titleId);
        super.setTitle(titleId);
    }

    @Override
    public void setActionBar(Toolbar toolbar) {
        super.setActionBar(toolbar);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
    }

    @Override
    public void onApplyThemeResource(Resources.Theme theme, int resId, boolean first) {
        super.onApplyThemeResource(theme, resId, first);
    }

    @Override
    public ClassLoader getClassLoader() {
        return super.getClassLoader();
    }

    @Override
    @NonNull
    public LayoutInflater getLayoutInflater() {
        return super.getLayoutInflater();
    }

    @Override
    public Resources getResources() {
        return mixResources == null ? super.getResources() : mixResources;
    }

    @Override
    public Intent getIntent() {
        return super.getIntent();
    }

    @Override
    public Window getWindow() {
        return super.getWindow();
    }

    @Override
    public ComponentName getComponentName() {
        return super.getComponentName();
    }

    @Override
    public ApplicationInfo getApplicationInfo() {
        return super.getApplicationInfo();
    }

    @Override
    public FragmentManager getFragmentManager() {
        return super.getFragmentManager();
    }
}
