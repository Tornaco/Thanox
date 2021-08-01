package github.tornaco.android.thanos.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import java.util.Objects;

import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.theme.ThemeActivity;

public class PluginListActivity extends ThemeActivity implements NavFragment.FragmentAttachListener {

    public static void start(Context context) {
        Intent starter = new Intent(context, PluginListActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plugin_list);
        setSupportActionBar(findViewById(R.id.toolbar));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, new PluginFragment())
                    .commitAllowingStateLoss();
        }
    }

    @Override
    public void onFragmentAttach(NavFragment fragment) {
        // No op.
    }
}
