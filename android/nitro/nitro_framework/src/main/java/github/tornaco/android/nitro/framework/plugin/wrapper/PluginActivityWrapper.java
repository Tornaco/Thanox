package github.tornaco.android.nitro.framework.plugin.wrapper;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toolbar;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import github.tornaco.android.nitro.framework.plugin.iface.PluginActivityInterface;
import lombok.AllArgsConstructor;
import util.ReflectionUtils;

@AllArgsConstructor
@Keep
public class PluginActivityWrapper implements PluginActivityInterface {
    private Object plugin;

    @Override
    public Activity getHostActivity() {
        return (Activity) ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(plugin.getClass(), "getHostActivity"),
                plugin);
    }

    @Override
    public void setTheme(int resId) {
        ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(plugin.getClass(), "setTheme", int.class),
                plugin,
                resId);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(plugin.getClass(), "onCreate", Bundle.class),
                plugin,
                savedInstanceState);
    }

    @Override
    public void onStart() {
        ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(plugin.getClass(), "onStart"),
                plugin);
    }

    @Override
    public void onResume() {
        ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(plugin.getClass(), "onResume"),
                plugin);
    }

    @Override
    public void onPause() {
        ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(plugin.getClass(), "onPause"),
                plugin);
    }

    @Override
    public void onRestart() {
        ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(plugin.getClass(), "onRestart"),
                plugin);
    }

    @Override
    public void onStop() {
        ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(plugin.getClass(), "onStop"),
                plugin);
    }

    @Override
    public void recreate() {
        ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(plugin.getClass(), "recreate"),
                plugin);
    }

    @Override
    public void onDestroy() {
        ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(plugin.getClass(), "onDestroy"),
                plugin);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return (boolean) ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(plugin.getClass(), "dispatchKeyEvent", KeyEvent.class),
                plugin,
                event);
    }

    @Override
    public void onNewIntent(Intent intent) {
        ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(plugin.getClass(), "onNewIntent", Intent.class),
                plugin,
                intent);
    }

    @Override
    public Intent getIntent() {
        return (Intent) ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(plugin.getClass(), "getIntent"),
                plugin);
    }

    @Override
    public void setActionBar(Toolbar toolbar) {
        ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(plugin.getClass(), "setActionBar", Toolbar.class),
                plugin,
                toolbar);
    }

    @Override
    public ActionBar getActionBar() {
        return (ActionBar) ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(plugin.getClass(), "getActionBar"),
                plugin);
    }

    @Override
    public void setTitle(CharSequence title) {
        ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(plugin.getClass(), "setTitle", CharSequence.class),
                plugin,
                title);
    }

    @Override
    public void setTitle(int titleRes) {
        ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(plugin.getClass(), "setTitle", int.class),
                plugin,
                titleRes);
    }

    @Override
    public void setContentView(View view) {
        ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(plugin.getClass(), "setContentView", View.class),
                plugin,
                view);
    }

    @Override
    public void setContentView(int layoutResID) {
        ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(plugin.getClass(), "setContentView", int.class),
                plugin,
                layoutResID);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return (boolean) ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(plugin.getClass(), "onCreateOptionsMenu", Menu.class),
                plugin,
                menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return (boolean) ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(plugin.getClass(), "onOptionsItemSelected", MenuItem.class),
                plugin,
                item);
    }

    @Override
    public void onBackPressed() {
        ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(plugin.getClass(), "onBackPressed"),
                plugin);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return (boolean) ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(plugin.getClass(), "onTouchEvent", MotionEvent.class),
                plugin,
                event);
    }

    @Override
    public void finish() {
        ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(plugin.getClass(), "finish"),
                plugin);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(plugin.getClass(), "onSaveInstanceState", Bundle.class),
                plugin,
                outState);
    }

    @NonNull
    @Override
    public LayoutInflater getLayoutInflater() {
        return (LayoutInflater) ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(plugin.getClass(), "getLayoutInflater"),
                plugin);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(plugin.getClass(), "onRestoreInstanceState", Bundle.class),
                plugin,
                savedInstanceState);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends View> T findViewById(int id) {
        return (T) ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(plugin.getClass(), "findViewById", int.class),
                plugin,
                id);
    }

    @Override
    public void onApplyThemeResource(Resources.Theme theme, int resId, boolean first) {
        ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(plugin.getClass(), "onApplyThemeResource", Resources.Theme.class, int.class, boolean.class),
                plugin,
                theme,
                resId,
                first);
    }

    @Override
    public void setThemeResource(int theme) {
        ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(plugin.getClass(), "setThemeResource", int.class),
                plugin,
                theme);
    }

    @Override
    public void setPluginClassLoader(ClassLoader pluginClassLoader) {
        ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(plugin.getClass(), "setPluginClassLoader", ClassLoader.class),
                plugin,
                pluginClassLoader);
    }

    @Override
    public void setPluginResources(Resources pluginRes) {
        ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(plugin.getClass(), "setPluginResources", Resources.class),
                plugin,
                pluginRes);
    }

    @Override
    public void setHostActivityInvoker(Object hostActivityInvoker) {
        ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(plugin.getClass(), "setHostActivityInvoker", Object.class),
                plugin,
                hostActivityInvoker);
    }

    @Override
    public void attachHostBaseContext(Context context) {
        ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(plugin.getClass(), "attachHostBaseContext", Context.class),
                plugin,
                context);
    }

    @Override
    public Resources getPluginResources() {
        return (Resources) ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(plugin.getClass(), "getPluginResources"),
                plugin);
    }

    @Override
    public int getThemeResource() {
        return (int) ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(plugin.getClass(), "getThemeResource"),
                plugin);
    }

    @Override
    public ClassLoader getClassLoader() {
        return (ClassLoader) ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(plugin.getClass(), "getClassLoader"),
                plugin);
    }

    @Override
    public Resources getResources() {
        return (Resources) ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(plugin.getClass(), "getResources"),
                plugin);
    }
}
