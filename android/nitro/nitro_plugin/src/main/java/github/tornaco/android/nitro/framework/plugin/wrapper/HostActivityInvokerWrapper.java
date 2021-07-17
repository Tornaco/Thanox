package github.tornaco.android.nitro.framework.plugin.wrapper;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Application;
import android.app.FragmentManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.os.Bundle;
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

import github.tornaco.android.nitro.framework.plugin.iface.HostActivityInvokerInterface;
import github.tornaco.android.nitro.framework.plugin.util.ReflectionUtils;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class HostActivityInvokerWrapper implements HostActivityInvokerInterface {
    private Object impl;

    @Override
    public Activity getHostActivity() {
        return (Activity) ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(impl.getClass(), "getHostActivity"),
                impl
        );
    }

    @Override
    public void setTheme(int resId) {
        ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(impl.getClass(), "setTheme", int.class),
                impl,
                resId
        );
    }

    @Override
    public void superOnCreate(Bundle savedInstanceState) {
        ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(impl.getClass(), "superOnCreate", Bundle.class),
                impl,
                savedInstanceState
        );
    }

    @Override
    public void onPostResume() {
        ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(impl.getClass(), "onPostResume"),
                impl
        );
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(impl.getClass(), "onPostCreate", Bundle.class),
                impl,
                savedInstanceState
        );
    }

    @Override
    public void superOnStart() {
        ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(impl.getClass(), "superOnStart"),
                impl
        );
    }

    @Override
    public void superOnResume() {
        ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(impl.getClass(), "superOnResume"),
                impl
        );
    }

    @Override
    public void superOnNewIntent(Intent intent) {
        ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(impl.getClass(), "superOnNewIntent", Intent.class),
                impl,
                intent
        );
    }

    @Override
    public void superOnSaveInstanceState(Bundle outState) {
        ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(impl.getClass(), "superOnSaveInstanceState", Bundle.class),
                impl,
                outState
        );
    }

    @Override
    public void superOnPause() {
        ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(impl.getClass(), "superOnPause"),
                impl
        );
    }

    @Override
    public void superRecreate() {
        ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(impl.getClass(), "superRecreate"),
                impl
        );
    }

    @Override
    public void superOnRestart() {
        ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(impl.getClass(), "superOnRestart"),
                impl
        );
    }

    @Override
    public void superOnStop() {
        ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(impl.getClass(), "superOnStop"),
                impl
        );
    }

    @Override
    public void superOnDestroy() {
        ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(impl.getClass(), "superOnDestroy"),
                impl
        );
    }

    @Override
    public boolean superDispatchKeyEvent(KeyEvent event) {
        return (boolean) ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(impl.getClass(), "superDispatchKeyEvent", KeyEvent.class),
                impl,
                event
        );
    }

    @Override
    public void superOnBackPressed() {
        ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(impl.getClass(), "superOnBackPressed"),
                impl
        );
    }

    @Override
    public boolean superOnCreateOptionsMenu(Menu menu) {
        return (boolean) ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(impl.getClass(), "superOnCreateOptionsMenu", Menu.class),
                impl,
                menu
        );
    }

    @Override
    public boolean superOnOptionsItemSelected(MenuItem item) {
        return (boolean) ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(impl.getClass(), "superOnOptionsItemSelected", MenuItem.class),
                impl,
                item
        );
    }

    @Override
    public boolean superOnTouchEvent(MotionEvent event) {
        return (boolean) ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(impl.getClass(), "superOnTouchEvent", MotionEvent.class),
                impl,
                event
        );
    }

    @Override
    public void startActivity(Intent intent) {
        ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(impl.getClass(), "startActivity", Intent.class),
                impl,
                intent
        );
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(impl.getClass(), "startActivityForResult", Intent.class, int.class),
                impl,
                intent,
                requestCode
        );
    }

    @Override
    public void superOnActivityResult(int requestCode, int resultCode, Intent data) {
        ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(impl.getClass(), "superOnActivityResult", int.class, int.class, Intent.class),
                impl,
                requestCode,
                resultCode,
                data
        );
    }

    @Override
    public void superOnRestoreInstanceState(Bundle savedInstanceState) {
        ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(impl.getClass(), "superOnRestoreInstanceState", Bundle.class),
                impl,
                savedInstanceState
        );
    }

    @Override
    public void superOnRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(impl.getClass(), "superOnRequestPermissionsResult", int.class, String[].class, int[].class),
                impl,
                requestCode,
                permissions,
                grantResults
        );
    }

    @Override
    public void finish() {
        ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(impl.getClass(), "finish"),
                impl
        );
    }

    @Override
    public boolean isFinishing() {
        return (boolean) ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(impl.getClass(), "isFinishing"),
                impl
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends View> T findViewById(int id) {
        return (T) ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(impl.getClass(), "findViewById", int.class),
                impl,
                id
        );
    }

    @Override
    public ActionBar getActionBar() {
        return (ActionBar) ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(impl.getClass(), "getActionBar"),
                impl
        );
    }

    @Override
    public void setTitle(int titleRes) {
        ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(impl.getClass(), "setTitle", int.class),
                impl,
                titleRes
        );
    }

    @Override
    public void setTitle(CharSequence title) {
        ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(impl.getClass(), "setTitle", CharSequence.class),
                impl,
                title
        );
    }

    @Override
    public void setActionBar(Toolbar toolbar) {
        ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(impl.getClass(), "setActionBar", Toolbar.class),
                impl,
                toolbar
        );
    }

    @Override
    public void setContentView(int layoutResID) {
        ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(impl.getClass(), "setContentView", int.class),
                impl,
                layoutResID
        );
    }

    @Override
    public void setContentView(View view) {
        ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(impl.getClass(), "setContentView", View.class),
                impl,
                view
        );
    }

    @Override
    public void onApplyThemeResource(Resources.Theme theme, int resId, boolean first) {
        ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(impl.getClass(), "onApplyThemeResource", Resources.Theme.class, int.class, boolean.class),
                impl,
                theme,
                resId,
                first
        );
    }

    @Override
    public ClassLoader getClassLoader() {
        return (ClassLoader) ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(impl.getClass(), "getClassLoader"),
                impl
        );
    }

    @Override
    public LayoutInflater getLayoutInflater() {
        return (LayoutInflater) ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(impl.getClass(), "getLayoutInflater"),
                impl
        );
    }

    @Override
    public Resources getResources() {
        return (Resources) ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(impl.getClass(), "getResources"),
                impl
        );
    }

    @Override
    public Intent getIntent() {
        return (Intent) ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(impl.getClass(), "getIntent"),
                impl
        );
    }

    @Override
    public Window getWindow() {
        return (Window) ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(impl.getClass(), "getWindow"),
                impl
        );
    }

    @Override
    public ComponentName getComponentName() {
        return (ComponentName) ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(impl.getClass(), "getComponentName"),
                impl
        );
    }

    @Override
    public FragmentManager getFragmentManager() {
        return (FragmentManager) ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(impl.getClass(), "getFragmentManager"),
                impl
        );
    }

    @Override
    public Application getApplication() {
        return (Application) ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(impl.getClass(), "getApplication"),
                impl
        );
    }

    @Override
    public ApplicationInfo getApplicationInfo() {
        return (ApplicationInfo) ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(impl.getClass(), "getApplicationInfo"),
                impl
        );
    }

    @Override
    public MenuInflater getMenuInflater() {
        return (MenuInflater) ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(impl.getClass(), "getMenuInflater"),
                impl
        );
    }

    @Override
    public WindowManager getWindowManager() {
        return (WindowManager) ReflectionUtils.invokeMethod(
                ReflectionUtils.findMethod(impl.getClass(), "getWindowManager"),
                impl
        );
    }
}
