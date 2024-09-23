package github.tornaco.thanos.android.module.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.core.pm.Pkg;
import github.tornaco.android.thanos.core.profile.GlobalVar;
import github.tornaco.android.thanos.core.util.TextWatcherAdapter;
import github.tornaco.android.thanos.picker.AppPickerActivity;
import github.tornaco.android.thanos.theme.ThemeActivity;
import github.tornaco.android.thanos.util.ActivityUtils;
import github.tornaco.android.thanos.util.TypefaceHelper;
import github.tornaco.thanos.android.module.profile.databinding.ModuleProfileGlobalVarEditorBinding;
import util.CollectionUtils;
import util.ObjectsUtils;

public class GlobalVarEditorActivity extends ThemeActivity {

    private static final int REQ_PICK_APPS = 0x100;

    private ModuleProfileGlobalVarEditorBinding binding;
    private String originalContent;
    @Nullable
    private GlobalVar globalVar
            = new GlobalVar(
            "",
            Lists.newArrayList());

    public static void start(Context context, GlobalVar globalVar) {
        Bundle data = new Bundle();
        data.putParcelable("var", globalVar);
        ActivityUtils.startActivity(context, GlobalVarEditorActivity.class, data);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (resolveIntent()) {
            binding = ModuleProfileGlobalVarEditorBinding.inflate(LayoutInflater.from(this));
            setContentView(binding.getRoot());
            initView();
        }
    }

    private boolean resolveIntent() {
        if (getIntent() != null && getIntent().hasExtra("var")) {
            GlobalVar extra = getIntent().getParcelableExtra("var");
            if (extra != null) {
                globalVar = extra;
            }
        }
        if (globalVar == null) {
            return false;
        }
        originalContent = globalVar.listToJson();
        return true;
    }

    private void initView() {
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setTitle(globalVar == null ? github.tornaco.android.thanos.res.R.string.module_profile_rule_new : github.tornaco.android.thanos.res.R.string.module_profile_rule_edit);

        if (globalVar != null) {
            checkRuleAndUpdateTips(globalVar.listToJson());
        }

        binding.codeView.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                String current = getCurrentEditingContent();
                if (!TextUtils.isEmpty(current)) {
                    checkRuleAndUpdateTips(current);
                }
            }
        });

        binding.toolbarTitle.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable editable) {
                super.afterTextChanged(editable);
                globalVar.setName(getCurrentEditingTitle());
            }
        });

        binding.editorActionsToolbar.inflateMenu(R.menu.module_profile_var_actions);
        binding.editorActionsToolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_save_apply) {
                if (TextUtils.isEmpty(getCurrentEditingContent())) {
                    return false;
                }
                if (TextUtils.isEmpty(getCurrentEditingTitle())) {
                    return false;
                }
                List<String> stringList = GlobalVar.listFromJson(getCurrentEditingContent());
                if (stringList == null) {
                    new MaterialAlertDialogBuilder(thisActivity())
                            .setMessage(github.tornaco.android.thanos.res.R.string.module_profile_editor_save_check_error)
                            .setCancelable(true)
                            .setPositiveButton(android.R.string.ok, null)
                            .show();
                    return false;
                }
                ThanosManager.from(getApplicationContext())
                        .ifServiceInstalled(thanosManager ->
                                thanosManager
                                        .getProfileManager()
                                        .addGlobalRuleVar(globalVar.getName(),
                                                stringList.toArray(new String[0])));
                finish();
                return true;
            }
            if (item.getItemId() == R.id.action_text_size_inc) {
                binding.codeView.setTextSize(TypedValue.COMPLEX_UNIT_PX, binding.codeView.getTextSize() + 5f);
                return true;
            }
            if (item.getItemId() == R.id.action_text_size_dec) {
                binding.codeView.setTextSize(TypedValue.COMPLEX_UNIT_PX, binding.codeView.getTextSize() - 5f);
                return true;
            }
            if (R.id.action_delete == item.getItemId()) {
                onRequestDelete();
                return true;
            }
            return false;
        });

        binding.codeView.setText(originalContent);
        binding.setLifecycleOwner(this);
        binding.executePendingBindings();

        binding.codeView.setTypeface(TypefaceHelper.jetbrainsMonoRegular(thisActivity()));

        setTitle(globalVar.getName());
    }

    private String getCurrentEditingContent() {
        if (binding.codeView.getText() == null) return "";
        return binding.codeView.getText().toString().trim();
    }

    private String getCurrentEditingTitle() {
        if (binding.toolbarTitle.getText() == null) return "";
        return binding.toolbarTitle.getText().toString().trim();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        binding.editorActionsToolbar.getMenu().findItem(R.id.action_delete).setVisible(globalVar != null);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.module_profile_var_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (R.id.action_pick_app == item.getItemId()) {
            ArrayList<String> exclude = GlobalVar.listFromJson(getCurrentEditingContent());
            if (exclude == null) exclude = new ArrayList<>(0);
            ArrayList<Pkg> pkgs = exclude.stream().map(Pkg::systemUserPkg).collect(Collectors.toCollection(Lists::newArrayList));
            AppPickerActivity.start(thisActivity(), REQ_PICK_APPS, pkgs);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected boolean onHomeMenuSelected() {
        onRequestAbort();
        return true;
    }

    @Override
    public void onBackPressed() {
        onRequestAbort();
    }

    private void onRequestAbort() {
        if (ObjectsUtils.equals(originalContent, getCurrentEditingContent())) {
            finish();
            return;
        }
        new MaterialAlertDialogBuilder(thisActivity())
                .setTitle(github.tornaco.android.thanos.res.R.string.module_profile_editor_discard_dialog_title)
                .setMessage(github.tornaco.android.thanos.res.R.string.module_profile_editor_discard_dialog_message)
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> finish())
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private void onRequestDelete() {
        new MaterialAlertDialogBuilder(thisActivity())
                .setTitle(github.tornaco.android.thanos.res.R.string.module_profile_editor_delete_dialog_title)
                .setMessage(github.tornaco.android.thanos.res.R.string.module_profile_editor_delete_dialog_message)
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    if (globalVar != null) {
                        ThanosManager.from(getApplicationContext())
                                .getProfileManager()
                                .removeGlobalRuleVar(globalVar.getName());
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private void checkRuleAndUpdateTips(String rule) {
        if (GlobalVar.listFromJson(rule) != null) {
            binding.checkIndicator.setImageResource(R.drawable.module_profile_ic_rule_valid_green_fill);
        } else {
            binding.checkIndicator.setImageResource(R.drawable.module_profile_ic_rule_invalid_red_fill);
        }
    }

    @Override
    public void setTitle(int titleId) {
        // super.setTitle(titleId);
        setTitleInternal(getString(titleId));
    }

    @Override
    public void setTitle(CharSequence title) {
        // super.setTitle(title);
        setTitleInternal(title);
    }

    private void setTitleInternal(CharSequence title) {
        if (binding == null) return;
        binding.toolbarTitle.setText(title);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQ_PICK_APPS == requestCode && resultCode == RESULT_OK && data != null && data.hasExtra("apps")) {
            List<AppInfo> appInfos = data.getParcelableArrayListExtra("apps");

            List<String> stringList = new ArrayList<>();
            CollectionUtils.consumeRemaining(appInfos, appInfo -> stringList.add(appInfo.getPkgName()));

            List<String> editorList = GlobalVar.listFromJson(getCurrentEditingContent());
            if (editorList != null) {
                stringList.addAll(editorList);
            }

            Objects.requireNonNull(globalVar).setStringList(stringList);
            binding.codeView.setText(globalVar.listToJson());
        }
    }
}
