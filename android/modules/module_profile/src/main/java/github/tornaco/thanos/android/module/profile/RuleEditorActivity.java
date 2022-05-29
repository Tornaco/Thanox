package github.tornaco.thanos.android.module.profile;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.elvishew.xlog.XLog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.vic797.syntaxhighlight.SyntaxListener;

import java.util.Objects;

import github.tornaco.android.rhino.plugin.Verify;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.profile.ProfileManager;
import github.tornaco.android.thanos.core.profile.RuleAddCallback;
import github.tornaco.android.thanos.core.profile.RuleCheckCallback;
import github.tornaco.android.thanos.core.profile.RuleInfo;
import github.tornaco.android.thanos.core.util.TextWatcherAdapter;
import github.tornaco.android.thanos.theme.ThemeActivity;
import github.tornaco.android.thanos.util.ActivityUtils;
import github.tornaco.android.thanos.util.TypefaceHelper;
import github.tornaco.thanos.android.module.profile.databinding.ModuleProfileWorkflowEditorBinding;
import util.ObjectsUtils;

public class RuleEditorActivity extends ThemeActivity implements SyntaxListener {
    private ModuleProfileWorkflowEditorBinding binding;
    @Nullable
    private RuleInfo ruleInfo;
    private String originalContent = "";
    private int format;
    private boolean readOnly;

    @Verify
    public static void start(Context context, RuleInfo ruleInfo, int format, boolean readOnly) {
        Bundle data = new Bundle();
        data.putParcelable("rule", ruleInfo);
        data.putInt("format", format);
        data.putBoolean("readOnly", readOnly);
        ActivityUtils.startActivity(context, RuleEditorActivity.class, data);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (resolveIntent()) {
            binding = ModuleProfileWorkflowEditorBinding.inflate(LayoutInflater.from(this));
            setContentView(binding.getRoot());
            initView();
        }
    }

    private boolean resolveIntent() {
        ruleInfo = getIntent().getParcelableExtra("rule");
        if (ruleInfo != null) {
            originalContent = ruleInfo.getRuleString();
            format = ruleInfo.getFormat();
        } else {
            format = getIntent().getIntExtra("format", ProfileManager.RULE_FORMAT_JSON);
        }
        readOnly = getIntent().getBooleanExtra("readOnly", false);
        return format == ProfileManager.RULE_FORMAT_YAML || format == ProfileManager.RULE_FORMAT_JSON;
    }

    private void initView() {
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setTitle(ruleInfo == null ? R.string.module_profile_rule_new : R.string.module_profile_rule_edit);
        if (readOnly) {
            setTitle(ruleInfo.getName());
        }

        if (ruleInfo != null) {
            checkRuleAndUpdateTips(ruleInfo.getRuleString());
        }

        binding.editText.setEnabled(!readOnly);
        int toolbarVisible = !readOnly ? View.VISIBLE : View.GONE;
        binding.editorActionsToolbarSymbols1.setVisibility(toolbarVisible);
        binding.editorActionsToolbarSymbols2.setVisibility(toolbarVisible);
        binding.editorActionsToolbarSymbols3.setVisibility(toolbarVisible);
        binding.editorActionsToolbar.setVisibility(toolbarVisible);

        binding.editText.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                String current = getCurrentEditingContent();
                if (!TextUtils.isEmpty(current)) {
                    checkRuleAndUpdateTips(current);
                }
            }
        });


        String[] symbols = getResources().getStringArray(R.array.module_profile_symbols_1);

        // Range 100~200
        int baseId = 100;

        for (int i = 0; i < symbols.length; i++) {
            MenuItem item = binding.editorActionsToolbarSymbols1.getMenu().add(1000, baseId + i, Menu.NONE, symbols[i]);
            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }

        symbols = getResources().getStringArray(R.array.module_profile_symbols_2);
        baseId = 200;
        for (int i = 0; i < symbols.length; i++) {
            MenuItem item = binding.editorActionsToolbarSymbols2.getMenu().add(1000, baseId + i, Menu.NONE, symbols[i]);
            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }

        symbols = getResources().getStringArray(R.array.module_profile_symbols_3);
        baseId = 300;
        for (int i = 0; i < symbols.length; i++) {
            MenuItem item = binding.editorActionsToolbarSymbols3.getMenu().add(1000, baseId + i, Menu.NONE, symbols[i]);
            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }

        Toolbar.OnMenuItemClickListener symbolClickListener = item -> {
            // This is symbols.
            String s = String.valueOf(item.getTitle());
            XLog.i("Input: %s", s);
            int start = Math.max(binding.editText.getSelectionStart(), 0);
            int end = Math.max(binding.editText.getSelectionEnd(), 0);
            if (binding.editText.getText() == null) return false;
            binding.editText.getText().replace(Math.min(start, end), Math.max(start, end), s, 0, s.length());
            return true;
        };
        binding.editorActionsToolbarSymbols1.setOnMenuItemClickListener(symbolClickListener);
        binding.editorActionsToolbarSymbols2.setOnMenuItemClickListener(symbolClickListener);
        binding.editorActionsToolbarSymbols3.setOnMenuItemClickListener(symbolClickListener);

        binding.editorActionsToolbar.inflateMenu(R.menu.module_profile_rule_actions);
        binding.editorActionsToolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_save_apply) {
                if (TextUtils.isEmpty(getCurrentEditingContent())) {
                    return false;
                }
                RuleAddCallback callback = new RuleAddCallback() {
                    @Override
                    protected void onRuleAddSuccess() {
                        super.onRuleAddSuccess();
                        Toast.makeText(getApplicationContext(),
                                R.string.module_profile_editor_save_success,
                                Toast.LENGTH_LONG)
                                .show();
                        // Disable rule since it has been changed.
                        if (ruleInfo != null) {
                            ThanosManager.from(getApplicationContext())
                                    .getProfileManager()
                                    .disableRule(ruleInfo.getId());
                        }
                        finish();
                    }

                    @Override
                    protected void onRuleAddFail(int errorCode, String errorMessage) {
                        super.onRuleAddFail(errorCode, errorMessage);
                        new MaterialAlertDialogBuilder(thisActivity())
                                .setTitle(R.string.module_profile_editor_save_check_error)
                                .setMessage(errorMessage)
                                .setCancelable(true)
                                .setPositiveButton(android.R.string.ok, null)
                                .show();
                    }
                };

                if (ruleInfo != null) {
                    ThanosManager.from(getApplicationContext())
                            .getProfileManager()
                            .updateRule(ruleInfo.getId(), getCurrentEditingContent(), callback, format);
                } else {
                    ThanosManager.from(getApplicationContext())
                            .getProfileManager()
                            .addRule(getCurrentEditingContent(), callback, format);
                }

                return true;
            }
            if (item.getItemId() == R.id.action_text_size_inc) {
                binding.editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, binding.editText.getTextSize() + 5f);
                return true;
            }
            if (item.getItemId() == R.id.action_text_size_dec) {
                binding.editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, binding.editText.getTextSize() - 5f);
                return true;
            }
            if (R.id.action_delete == item.getItemId()) {
                onRequestDelete();
                return true;
            }
            if (R.id.action_paste == item.getItemId()) {
                ClipboardManager cmb = (ClipboardManager) getApplication().getSystemService(Context.CLIPBOARD_SERVICE);
                if (cmb == null || !cmb.hasPrimaryClip()) {
                    return false;
                }
                ClipData.Item dataItem = Objects.requireNonNull(cmb.getPrimaryClip()).getItemAt(0);
                if (dataItem == null) return false;
                String content = dataItem.getText().toString();
                XLog.i("Input: %s", content);
                int start = Math.max(binding.editText.getSelectionStart(), 0);
                int end = Math.max(binding.editText.getSelectionEnd(), 0);
                if (binding.editText.getText() == null) return false;
                binding.editText.getText().replace(Math.min(start, end), Math.max(start, end), content, 0, content.length());
                return true;
            }

            if (R.id.action_show_hide_symbols == item.getItemId()) {
                binding.editorActionsToolbarSymbols1.setVisibility(binding.editorActionsToolbarSymbols1.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
                binding.editorActionsToolbarSymbols2.setVisibility(binding.editorActionsToolbarSymbols2.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
                binding.editorActionsToolbarSymbols3.setVisibility(binding.editorActionsToolbarSymbols3.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
            }

            return false;
        });

        // Format.
        if (format == ProfileManager.RULE_FORMAT_JSON) {
            binding.setFormat("JSON");
        }
        if (format == ProfileManager.RULE_FORMAT_YAML) {
            binding.setFormat("YAML");
        }

        binding.setRuleInfo(ruleInfo);
        binding.setPlaceholder(null);
        binding.setLifecycleOwner(this);
        binding.executePendingBindings();

        binding.editText.setTypeface(TypefaceHelper.jetbrainsMono(thisActivity()));
        binding.lineLayout.setTypeface(TypefaceHelper.jetbrainsMono(thisActivity()));
        binding.editText.setListener(this);
        binding.editText.addSyntax(getAssets(), "json.json");
        binding.lineLayout.attachEditText(binding.editText);
        binding.editText.startHighlight(true);
        binding.editText.updateVisibleRegion();
    }

    private String getCurrentEditingContent() {
        if (binding.editText.getText() == null) return "";
        return binding.editText.getText().toString().trim();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        binding.editorActionsToolbar.getMenu().findItem(R.id.action_delete).setVisible(ruleInfo != null);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.module_profile_rule_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (R.id.action_info == item.getItemId()) {
            showTipsInfo();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showTipsInfo() {
        new MaterialAlertDialogBuilder(thisActivity())
                .setTitle(R.string.module_profile_rule_format_tips_title)
                .setMessage(R.string.module_profile_rule_format_tips_message)
                .setPositiveButton(android.R.string.ok, null)
                .show();
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
        if (ObjectsUtils.equals(getCurrentEditingContent(), originalContent)) {
            finish();
            return;
        }
        new MaterialAlertDialogBuilder(thisActivity())
                .setTitle(R.string.module_profile_editor_discard_dialog_title)
                .setMessage(R.string.module_profile_editor_discard_dialog_message)
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> finish())
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private void onRequestDelete() {
        new MaterialAlertDialogBuilder(thisActivity())
                .setTitle(R.string.module_profile_editor_delete_dialog_title)
                .setMessage(R.string.module_profile_editor_delete_dialog_message)
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    if (ruleInfo != null) {
                        ThanosManager.from(getApplicationContext())
                                .getProfileManager()
                                .deleteRule(ruleInfo.getId());
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private void checkRuleAndUpdateTips(String rule) {
        ThanosManager.from(getApplicationContext())
                .getProfileManager()
                .checkRule(rule, new RuleCheckCallback() {
                            @Override
                            protected void onInvalid(int errorCode, String errorMessage) {
                                super.onInvalid(errorCode, errorMessage);
                                XLog.w("onInvalid: %s", errorMessage);
                                binding.ruleCheckIndicator.setImageResource(R.drawable.module_profile_ic_rule_invalid_red_fill);
                            }

                            @Override
                            protected void onValid(RuleInfo ruleInfo) {
                                super.onValid(ruleInfo);
                                XLog.w("onValid: %s", format);
                                binding.ruleCheckIndicator.setImageResource(R.drawable.module_profile_ic_rule_valid_green_fill);
                            }
                        },
                        format);
    }

    @Override
    public void onLineClick(Editable editable, String text, int line) {
        // XLog.v("onLineClick");
    }

    @Override
    public void onHighlightStart(Editable editable) {
        // XLog.v("onHighlightStart");
    }

    @Override
    public void onHighlightEnd(Editable editable) {
        // XLog.v("onHighlightEnd");
    }

    @Override
    public void onError(Exception e) {
        XLog.e(e, "onError");
    }
}
