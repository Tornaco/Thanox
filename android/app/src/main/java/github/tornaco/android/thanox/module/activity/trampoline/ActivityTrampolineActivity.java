package github.tornaco.android.thanox.module.activity.trampoline;

import static github.tornaco.android.thanos.module.compose.common.LocalSimpleStorageHelperKt.REQUEST_CODE_PICK_TRAMP_EXPORT_PATH;
import static github.tornaco.android.thanos.module.compose.common.LocalSimpleStorageHelperKt.REQUEST_CODE_PICK_TRAMP_IMPORT_PATH;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.PopupMenu;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.anggrayudi.storage.SimpleStorageHelper;
import com.anggrayudi.storage.file.FileFullPath;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.app.component.ComponentReplacement;
import github.tornaco.android.thanos.core.pm.ComponentNameBrief;
import github.tornaco.android.thanos.core.util.DateUtils;
import github.tornaco.android.thanos.databinding.ModuleActivityTrampolineActivityBinding;
import github.tornaco.android.thanos.support.AppFeatureManager;
import github.tornaco.android.thanos.support.ThanoxAppContext;
import github.tornaco.android.thanos.theme.ThemeActivity;
import github.tornaco.android.thanos.util.ActivityUtils;
import github.tornaco.android.thanos.widget.SwitchBar;

public class ActivityTrampolineActivity extends ThemeActivity
        implements ActivityTrampolineItemClickListener {
    private static String sExportComponentKeyOrNull = null;

    private ModuleActivityTrampolineActivityBinding binding;
    private TrampolineViewModel viewModel;

    private final SimpleStorageHelper storageHelper = new SimpleStorageHelper(this);

    public static void start(Context context) {
        ActivityUtils.startActivity(context, ActivityTrampolineActivity.class);
    }

    @Override
    public Context getApplicationContext() {
        return new ThanoxAppContext(super.getApplicationContext());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ModuleActivityTrampolineActivityBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        setupView();
        setupViewModel();

        storageHelper.setOnFileCreated((code, documentFile) -> {
            viewModel.exportToFile(documentFile, sExportComponentKeyOrNull);
            return null;
        });
        storageHelper.setOnFileSelected((code, documentFiles) -> {
            if (documentFiles == null || documentFiles.isEmpty()) return null;
            //noinspection SequencedCollectionMethodCanBeUsed
            DocumentFile firstFile = documentFiles.get(0);
            viewModel.importFromFile(firstFile);
            return null;
        });
    }

    private void setupView() {
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // List.
        binding.replacements.setLayoutManager(new LinearLayoutManager(this));
        binding.replacements.setAdapter(new ActivityTrampolineAdapter(this));
        binding.swipe.setOnRefreshListener(() -> viewModel.start());
        binding.swipe.setColorSchemeColors(getResources().getIntArray(github.tornaco.android.thanos.module.common.R.array.common_swipe_refresh_colors));


        // Search.
        binding.searchView.setOnQueryTextListener(
                new MaterialSearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        viewModel.setSearchText(query);
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        viewModel.setSearchText(newText);
                        return true;
                    }
                });

        binding.searchView.setOnSearchViewListener(
                new MaterialSearchView.SearchViewListener() {
                    @Override
                    public void onSearchViewShown() {
                        binding.toolbarLayout.setTitleEnabled(false);
                        binding.appbar.setExpanded(false, true);
                    }

                    @Override
                    public void onSearchViewClosed() {
                        viewModel.clearSearchText();
                        binding.toolbarLayout.setTitleEnabled(true);
                    }
                });

        // Switch.
        onSetupSwitchBar(binding.switchBarContainer.switchBar);

        binding.fab.setOnClickListener(v -> {
            ThanosManager.from(getApplicationContext())
                    .ifServiceInstalled(thanosManager -> showAddReplacementDialog(null, null, null, false));
        });
    }

    private void onSetupSwitchBar(SwitchBar switchBar) {
        switchBar.setOnLabel(getString(github.tornaco.android.thanos.res.R.string.common_switchbar_title_format,
                getString(github.tornaco.android.thanos.res.R.string.module_activity_trampoline_app_name)));
        switchBar.setOffLabel(getString(github.tornaco.android.thanos.res.R.string.common_switchbar_title_format,
                getString(github.tornaco.android.thanos.res.R.string.module_activity_trampoline_app_name)));
        switchBar.setChecked(getSwitchBarCheckState());
        switchBar.addOnSwitchChangeListener(this::onSwitchBarCheckChanged);
    }

    private boolean getSwitchBarCheckState() {
        return ThanosManager.from(getApplicationContext()).isServiceInstalled()
                && ThanosManager.from(getApplicationContext()).getActivityStackSupervisor()
                .isActivityTrampolineEnabled();
    }

    private void onSwitchBarCheckChanged(MaterialSwitch switchBar, boolean isChecked) {
        ThanosManager.from(getApplicationContext())
                .ifServiceInstalled(thanosManager -> thanosManager.getActivityStackSupervisor()
                        .setActivityTrampolineEnabled(isChecked));
    }

    private void setupViewModel() {
        viewModel = obtainViewModel(this);
        viewModel.start();

        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        binding.executePendingBindings();
    }

    private void showAddReplacementDialog(String from, String to, String note, boolean canDelete) {
        View layout = LayoutInflater.from(this).inflate(R.layout.module_activity_trampoline_comp_replace_editor, null, false);

        final AppCompatEditText fromEditText = layout.findViewById(R.id.from_comp);
        fromEditText.setFilters(new InputFilter[]{new EmojiExcludeFilter()});
        fromEditText.setText(from);

        final AppCompatEditText toEditText = layout.findViewById(R.id.to_comp);
        toEditText.setFilters(new InputFilter[]{new EmojiExcludeFilter()});
        toEditText.setText(to);

        final AppCompatEditText noteEditText = layout.findViewById(R.id.note);
        noteEditText.setText(note);

        AlertDialog d = new MaterialAlertDialogBuilder(ActivityTrampolineActivity.this)
                .setTitle(canDelete
                        ? github.tornaco.android.thanos.res.R.string.module_activity_trampoline_edit_dialog_title
                        : github.tornaco.android.thanos.res.R.string.module_activity_trampoline_add_dialog_title)
                .setView(layout)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, (dialog, which) ->
                        onRequestAddNewReplacement(
                                fromEditText.getEditableText().toString(),
                                toEditText.getEditableText().toString(),
                                noteEditText.getEditableText().toString()))
                .setNegativeButton(android.R.string.cancel, null)
                .create();
        if (canDelete) {
            d.setButton(DialogInterface.BUTTON_NEUTRAL,
                    getString(github.tornaco.android.thanos.res.R.string.module_activity_trampoline_add_dialog_delete),
                    (dialog, which) -> onRequestDeleteNewReplacement(from, to));
        }
        d.show();
    }

    private void onRequestDeleteNewReplacement(String f, String t) {
        if (TextUtils.isEmpty(f) || TextUtils.isEmpty(t) || TextUtils.isEmpty(f.trim()) || TextUtils.isEmpty(t.trim())) {
            showComponentEmptyTips();
            return;
        }
        ComponentNameBrief fromCompName = ComponentNameBrief.unflattenFromString(f);
        if (fromCompName == null) {
            showComponentFromInvalidTips();
            return;
        }
        ComponentNameBrief toCompName = ComponentNameBrief.unflattenFromString(t);
        if (toCompName == null) {
            showComponentToInvalidTips();
            return;
        }
        viewModel.onRequestRemoveNewReplacement(fromCompName, toCompName);
    }

    private void onRequestAddNewReplacement(String f, String t, String note) {
        if (TextUtils.isEmpty(f) || TextUtils.isEmpty(t) || TextUtils.isEmpty(f.trim()) || TextUtils.isEmpty(t.trim())) {
            showComponentEmptyTips();
            return;
        }
        ComponentNameBrief fromCompName = ComponentNameBrief.unflattenFromString(f);
        if (fromCompName == null) {
            showComponentFromInvalidTips();
            return;
        }
        ComponentNameBrief toCompName = ComponentNameBrief.unflattenFromString(t);
        if (toCompName == null) {
            showComponentToInvalidTips();
            return;
        }
        viewModel.onRequestAddNewReplacement(fromCompName, toCompName, note);
    }

    private void showComponentFromInvalidTips() {
        Toast.makeText(
                        ActivityTrampolineActivity.this,
                        github.tornaco.android.thanos.res.R.string.module_activity_trampoline_add_invalid_from_component
                        , Toast.LENGTH_LONG)
                .show();
    }

    private void showComponentToInvalidTips() {
        Toast.makeText(
                        ActivityTrampolineActivity.this,
                        github.tornaco.android.thanos.res.R.string.module_activity_trampoline_add_invalid_to_component
                        , Toast.LENGTH_LONG)
                .show();
    }

    private void showComponentEmptyTips() {
        Toast.makeText(
                        ActivityTrampolineActivity.this,
                        github.tornaco.android.thanos.res.R.string.module_activity_trampoline_add_empty_component
                        , Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void onItemClick(@NonNull View view, @NonNull ComponentReplacement replacement) {
        showItemPopMenu(view, replacement);
    }

    private void showItemPopMenu(@NonNull View anchor, @NonNull ComponentReplacement replacement) {
        PopupMenu popupMenu = new PopupMenu(thisActivity(), anchor);
        popupMenu.inflate(R.menu.module_activity_trampoline_menu_trampoline_item);
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_edit) {
                showAddReplacementDialog(replacement.from.flattenToString(), replacement.to.flattenToString(), replacement.note, true);
                return true;
            }
            if (item.getItemId() == R.id.action_export) {
                onRequestExport(replacement.from.flattenToString());
                return true;
            }
            return false;
        });
        popupMenu.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.module_activity_trampoline_menu_trampoline, menu);
        MenuItem item = menu.findItem(github.tornaco.android.thanos.module.common.R.id.action_search);
        binding.searchView.setMenuItem(item);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_export) {
            onRequestExport(null);
            return true;
        }

        if (item.getItemId() == R.id.action_import) {
            onRequestImport();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (closeSearch()) {
            return;
        }
        super.onBackPressed();
    }

    protected boolean closeSearch() {
        if (binding.searchView.isSearchOpen()) {
            binding.searchView.closeSearch();
            return true;
        }
        return false;
    }

    // Null means all.
    private void onRequestExport(@Nullable String componentReplacementKey) {
        String[] items = getResources().getStringArray(github.tornaco.android.thanos.res.R.array.module_common_export_selections);
        AlertDialog dialog = new MaterialAlertDialogBuilder(this)
                .setTitle(github.tornaco.android.thanos.res.R.string.module_activity_trampoline_title_export_comp_replacements)
                .setSingleChoiceItems(items, -1,
                        (d, which) -> {
                            d.dismiss();
                            if (which == 0) {
                                exportToClipboard(componentReplacementKey);
                            } else {
                                AppFeatureManager.INSTANCE.withSubscriptionStatus(this, isSub -> {
                                    if (isSub) {
                                        String expFileNameWithExt = "Replacements-" + DateUtils.formatForFileName(System.currentTimeMillis()) + ".json";
                                        sExportComponentKeyOrNull = componentReplacementKey;
                                        storageHelper.createFile(
                                                "application/json",
                                                expFileNameWithExt,
                                                null,
                                                REQUEST_CODE_PICK_TRAMP_EXPORT_PATH
                                        );
                                    } else {
                                        AppFeatureManager.INSTANCE.showSubscribeDialog(ActivityTrampolineActivity.this);
                                    }
                                    return null;
                                });
                            }
                        }).create();
        dialog.show();
    }

    private void exportToClipboard(@Nullable String componentReplacementKey) {
        viewModel.exportToClipboard(componentReplacementKey);
    }

    private void onRequestImport() {
        String[] items = getResources().getStringArray(github.tornaco.android.thanos.res.R.array.module_common_import_selections);
        AlertDialog dialog = new MaterialAlertDialogBuilder(this)
                .setTitle(github.tornaco.android.thanos.res.R.string.module_activity_trampoline_title_import_comp_replacements)
                .setSingleChoiceItems(items, -1,
                        (d, which) -> {
                            d.dismiss();
                            if (which == 0) {
                                importFromClipboard();
                            } else {
                                AppFeatureManager.INSTANCE.withSubscriptionStatus(this, isSub -> {
                                    if (isSub) {
                                        storageHelper.openFilePicker(
                                                REQUEST_CODE_PICK_TRAMP_IMPORT_PATH,
                                                false,
                                                (FileFullPath) null,
                                                "application/json"
                                        );
                                    } else {
                                        AppFeatureManager.INSTANCE.showSubscribeDialog(ActivityTrampolineActivity.this);
                                    }
                                    return null;
                                });
                            }
                        }).create();
        dialog.show();
    }

    private void importFromClipboard() {
        viewModel.importFromClipboard();
    }

    public static TrampolineViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelProvider.AndroidViewModelFactory factory = ViewModelProvider.AndroidViewModelFactory
                .getInstance(activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(TrampolineViewModel.class);
    }

    private static class EmojiExcludeFilter implements InputFilter {

        @Override
        public CharSequence filter(CharSequence source,
                                   int start,
                                   int end,
                                   Spanned dest,
                                   int dstart,
                                   int dend) {
            for (int i = start; i < end; i++) {
                int type = Character.getType(source.charAt(i));
                if (type == Character.SURROGATE || type == Character.OTHER_SYMBOL) {
                    return "";
                }
            }
            return null;
        }
    }
}
