package github.tornaco.android.nitro.framework.plugin;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;

import github.tornaco.android.nitro.framework.plugin.wrapper.PluginActivityWrapper;
import lombok.Setter;

public class PluginActivityInvoker {
    @Setter
    HostActivityInvoker hostActivityInvoker;
    @Setter
    PluginActivityWrapper pluginActivity;

    public void onCreate(Bundle savedInstanceState) {
        this.pluginActivity.onCreate(savedInstanceState);
    }

    public void onStart() {
        pluginActivity.onStart();
    }


    public void onResume() {
        pluginActivity.onResume();
    }


    public void onNewIntent(Intent intent) {
        pluginActivity.onNewIntent(intent);
    }


    public Intent getIntent() {
        return hostActivityInvoker.getIntent();
    }

    public void onSaveInstanceState(Bundle outState) {
        pluginActivity.onSaveInstanceState(outState);
    }

    public void onPause() {
        pluginActivity.onPause();
    }

    public void recreate() {
        pluginActivity.recreate();
    }

    public void onRestart() {
        pluginActivity.onRestart();
    }

    public void onStop() {
        pluginActivity.onStop();
    }

    public void onDestroy() {
        if (pluginActivity != null) {
            pluginActivity.onDestroy();
        } else {
            hostActivityInvoker.superOnDestroy();
        }
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        return pluginActivity.dispatchKeyEvent(event);
    }

    public void onBackPressed() {
        pluginActivity.onBackPressed();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return pluginActivity.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        return pluginActivity.onOptionsItemSelected(item);
    }

    public boolean onTouchEvent(MotionEvent event) {
        return pluginActivity.onTouchEvent(event);
    }

    public void finish() {
        if (pluginActivity != null) pluginActivity.finish();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        pluginActivity.onActivityResult(requestCode, resultCode, data);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        pluginActivity.onRestoreInstanceState(savedInstanceState);
    }

    public ClassLoader getClassLoader() {
        return pluginActivity.getClassLoader();
    }

    public LayoutInflater getLayoutInflater() {
        return pluginActivity.getLayoutInflater();
    }

    public Resources getResources() {
        return pluginActivity.getResources();
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        pluginActivity.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
