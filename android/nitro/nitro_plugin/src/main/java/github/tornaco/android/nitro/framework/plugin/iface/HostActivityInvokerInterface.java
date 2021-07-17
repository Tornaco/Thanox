package github.tornaco.android.nitro.framework.plugin.iface;

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

public interface HostActivityInvokerInterface {

    Activity getHostActivity();

    void setTheme(int resId);

    void superOnCreate(Bundle savedInstanceState);

    void onPostCreate(Bundle savedInstanceState);

    void superOnStart();

    void superOnResume();

    void onPostResume();

    void superOnNewIntent(Intent intent);

    void superOnSaveInstanceState(Bundle outState);

    void superOnPause();

    void superRecreate();

    void superOnRestart();

    void superOnStop();

    void superOnDestroy();

    boolean superDispatchKeyEvent(KeyEvent event);

    void superOnBackPressed();

    boolean superOnCreateOptionsMenu(Menu menu);

    boolean superOnOptionsItemSelected(MenuItem item);

    boolean superOnTouchEvent(MotionEvent event);

    void startActivity(Intent intent);

    void startActivityForResult(Intent intent, int requestCode);

    void superOnActivityResult(int requestCode, int resultCode, Intent data);

    void superOnRestoreInstanceState(Bundle savedInstanceState);

    void superOnRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults);

    void finish();

    boolean isFinishing();

    <T extends View> T findViewById(int id);

    ActionBar getActionBar();

    void setTitle(int titleRes);

    void setTitle(CharSequence title);

    void setActionBar(Toolbar toolbar);

    void setContentView(int layoutResID);

    void setContentView(View view);

    void onApplyThemeResource(Resources.Theme theme, int resid, boolean first);

    ClassLoader getClassLoader();

    LayoutInflater getLayoutInflater();

    Resources getResources();

    Intent getIntent();

    Window getWindow();

    ComponentName getComponentName();

    FragmentManager getFragmentManager();

    Application getApplication();

    ApplicationInfo getApplicationInfo();

    MenuInflater getMenuInflater();

    WindowManager getWindowManager();
}
