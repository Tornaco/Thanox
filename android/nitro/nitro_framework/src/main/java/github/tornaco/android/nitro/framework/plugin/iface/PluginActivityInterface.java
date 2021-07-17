package github.tornaco.android.nitro.framework.plugin.iface;

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

import androidx.annotation.NonNull;

public interface PluginActivityInterface {

    Activity getHostActivity();

    void setTheme(int resId);

    void onCreate(Bundle savedInstanceState);

    void onStart();

    void onResume();


    void onPause();


    void onRestart();


    void onStop();


    void recreate();


    void onDestroy();


    boolean dispatchKeyEvent(KeyEvent event);


    void onNewIntent(Intent intent);


    Intent getIntent();


    void setActionBar(Toolbar toolbar);


    ActionBar getActionBar();


    void setTitle(CharSequence title);


    void setTitle(int titleRes);


    void setContentView(View view);


    void setContentView(int layoutResID);


    boolean onCreateOptionsMenu(Menu menu);


    boolean onOptionsItemSelected(@NonNull MenuItem item);


    void onBackPressed();


    boolean onTouchEvent(MotionEvent event);


    void finish();


    void onSaveInstanceState(@NonNull Bundle outState);


    @NonNull
    LayoutInflater getLayoutInflater();

    void startActivityForResult(Intent intent, int requestCode);

    void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);

    void onActivityResult(int requestCode, int resultCode, Intent data);

    void onRestoreInstanceState(@NonNull Bundle savedInstanceState);

    <T extends View> T findViewById(int id);

    void onApplyThemeResource(Resources.Theme theme, int resId, boolean first);

    void setThemeResource(int theme);

    void setPluginClassLoader(ClassLoader pluginClassLoader);

    void setPluginResources(Resources pluginRes);

    void setHostActivityInvoker(Object hostActivityInvoker);

    void attachHostBaseContext(Context context);

    Resources getPluginResources();

    int getThemeResource();

    ClassLoader getClassLoader();

    Resources getResources();
}
