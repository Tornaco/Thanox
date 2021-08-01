package github.tornaco.android.thanos.process;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import github.tornaco.android.rhino.plugin.Verify;
import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.databinding.ActivityProcessManageBinding;
import github.tornaco.android.thanos.app.BaseTrustedActivity;
import github.tornaco.android.thanos.util.ActivityUtils;


public class ProcessManageActivity extends BaseTrustedActivity {

    public static void start(Context context) {
        ActivityUtils.startActivity(context, ProcessManageActivity.class);
    }

    private ProcessManageViewModel startManageViewModel;
    private ActivityProcessManageBinding binding;

    @Override
    @Verify
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProcessManageBinding.inflate(
                LayoutInflater.from(this), null, false);
        setContentView(binding.getRoot());
        setupView();
        setupViewModel();
    }

    private void setupView() {
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //Creating the ArrayAdapter instance having the category list
        String[] category = getResources().getStringArray(R.array.process_manage_categories);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.ghost_text_view, category);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        binding.spinner.setAdapter(arrayAdapter);
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int index, long l) {
                setTitle(getString(R.string.activity_title_process_manage, category[index]));
                startManageViewModel.setAppCategoryFilter(index);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new ProcessManageFragment())
                .commitAllowingStateLoss();
    }

    private void setupViewModel() {
        startManageViewModel = obtainViewModel(this);
        startManageViewModel.start();

        binding.setViewModel(startManageViewModel);
        binding.setLifecycleOwner(this);
        binding.executePendingBindings();
    }

    @Override
    @Verify
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (android.R.id.home == item.getItemId()) finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        binding.toolbar.setTitle(title);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startManageViewModel.start();
    }

    public static ProcessManageViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelProvider.AndroidViewModelFactory factory = ViewModelProvider.AndroidViewModelFactory
                .getInstance(activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(ProcessManageViewModel.class);
    }
}
