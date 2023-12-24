/*
 * (C) Copyright 2022 Thanox
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package now.fortuitous.thanos.privacy;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

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
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(title);
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
