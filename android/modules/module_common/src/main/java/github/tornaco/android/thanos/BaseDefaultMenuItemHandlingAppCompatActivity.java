package github.tornaco.android.thanos;

import android.app.Activity;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class BaseDefaultMenuItemHandlingAppCompatActivity extends AppCompatActivity {

    protected void showHomeAsUpNavigator() {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    protected boolean onHomeMenuSelected() {
        finish();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            return onHomeMenuSelected();
        }
        return super.onOptionsItemSelected(item);
    }

    protected Activity thisActivity() {
        return this;
    }
}
