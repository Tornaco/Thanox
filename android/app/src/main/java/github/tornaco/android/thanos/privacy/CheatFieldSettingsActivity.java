package github.tornaco.android.thanos.privacy;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.theme.ThemeActivity;
import github.tornaco.android.thanos.util.ActivityUtils;

public class CheatFieldSettingsActivity extends ThemeActivity {

  public static void start(Activity context, String id, int reqCode) {
    Bundle data = new Bundle();
    data.putString("id", id);
    ActivityUtils.startActivityForResult(context, CheatFieldSettingsActivity.class, reqCode, data);
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_settings);
    setSupportActionBar(findViewById(R.id.toolbar));
    Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

    String id = getIntent().getStringExtra("id");
    if (id == null) {
      Toast.makeText(thisActivity(), "Id is missing", Toast.LENGTH_LONG).show();
      finish();
      return;
    }
    if (savedInstanceState == null) {
      getSupportFragmentManager()
          .beginTransaction()
          .replace(R.id.container, CheatFieldSettingsFragment.newInstance(id))
          .commit();
    }
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (android.R.id.home == item.getItemId()) {
      finish();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
