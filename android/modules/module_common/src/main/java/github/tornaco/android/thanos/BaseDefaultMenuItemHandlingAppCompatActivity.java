package github.tornaco.android.thanos;

import android.app.Activity;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import java.util.Objects;

public class BaseDefaultMenuItemHandlingAppCompatActivity extends BaseAppCompatActivity {

    protected void showHomeAsUpNavigator() {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    protected boolean onHomeMenuSelected() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            return onHomeMenuSelected();
        }
        return super.onOptionsItemSelected(item);
    }

    public Activity thisActivity() {
        return this;
    }
}
