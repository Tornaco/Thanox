package github.tornaco.android.thanos.power;

import static com.nononsenseapps.filepicker.FilePickerActivityUtils.pickSingleDirIntent;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import com.elvishew.xlog.XLog;
import com.google.android.material.chip.Chip;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.nononsenseapps.filepicker.Utils;
import com.topjohnwu.superuser.Shell;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import github.tornaco.android.rhino.plugin.Verify;
import github.tornaco.android.thanos.BaseFragment;
import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.apps.AppDetailsActivity;
import github.tornaco.android.thanos.apps.PackageSetChooserDialog;
import github.tornaco.android.thanos.apps.PackageSetListActivity;
import github.tornaco.android.thanos.common.AppItemActionListener;
import github.tornaco.android.thanos.common.AppListModel;
import github.tornaco.android.thanos.common.sort.AppSort;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.core.pm.PackageManager;
import github.tornaco.android.thanos.core.pm.PackageSet;
import github.tornaco.android.thanos.core.pm.Pkg;
import github.tornaco.android.thanos.core.util.DateUtils;
import github.tornaco.android.thanos.core.util.OsUtils;
import github.tornaco.android.thanos.databinding.ActivitySmartFreezeAppsBinding;
import github.tornaco.android.thanos.feature.access.AppFeatureManager;
import github.tornaco.android.thanos.picker.AppPickerActivity;
import github.tornaco.android.thanos.util.DialogUtils;
import github.tornaco.android.thanos.util.IntentUtils;
import github.tornaco.android.thanos.util.ToastUtils;
import github.tornaco.android.thanos.widget.ModernAlertDialog;
import github.tornaco.android.thanos.widget.ModernProgressDialog;
import github.tornaco.permission.requester.RequiresPermission;
import github.tornaco.permission.requester.RuntimePermissions;
import io.reactivex.Completable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import rx2.android.schedulers.AndroidSchedulers;

@SuppressWarnings("UnstableApiUsage")
@RuntimePermissions
public class SmartFreezeAppListFragment extends BaseFragment {
    private static final int REQUEST_CODE_PICK_IMPORT_PATH = 0x111;
    private static final String ARG_PKG_SET = "arg.pkg.set.id";

    private SmartFreezeAppsViewModel viewModel;
    private ActivitySmartFreezeAppsBinding binding;

    public static SmartFreezeAppListFragment newInstance(@Nullable String pkgSetId) {
        SmartFreezeAppListFragment freezeAppListFragment = new SmartFreezeAppListFragment();
        Bundle data = new Bundle();
        data.putString(ARG_PKG_SET, pkgSetId);
        freezeAppListFragment.setArguments(data);
        return freezeAppListFragment;
    }

    @Nullable
    @Override
    @Verify
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ActivitySmartFreezeAppsBinding.inflate(inflater, container, false);
        createOptionsMenu();
        setupView();
        Bundle data = getArguments();
        String pkgSetId = Objects.requireNonNull(data, "Missing arg: " + ARG_PKG_SET).getString(ARG_PKG_SET, null);
        setupViewModel(pkgSetId);
        onSetupSorter(binding.sortChipContainer.sortChip);
        return binding.getRoot();
    }

    private void setupView() {
        binding.toolbar.setNavigationIcon(R.drawable.module_common_ic_arrow_back_24dp);
        binding.toolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());
        // Search.
        binding.searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
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

        binding.searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
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

        // List.
        binding.apps.setLayoutManager(new GridLayoutManager(requireContext(), 5));
        binding.apps.setAdapter(new SmartFreezeAppsAdapter(new AppItemActionListener() {
            @Override
            public void onAppItemClick(AppInfo appInfo) {
                PackageManager pm = ThanosManager.from(requireContext()).getPkgManager();
                pm.launchSmartFreezePkg(Pkg.fromAppInfo(appInfo));
                appInfo.setState(AppInfo.STATE_ENABLED);
            }

            @Override
            public void onAppItemSwitchStateChange(AppInfo appInfo, boolean checked) {

            }
        }, this::showItemPopMenu));

        binding.swipe.setOnRefreshListener(() -> viewModel.start());
        binding.swipe.setColorSchemeColors(getResources().getIntArray(github.tornaco.android.thanos.module.common.R.array.common_swipe_refresh_colors));

        binding.fab.setOnClickListener(v -> viewModel.freezeAllOnCurrentPage());
        binding.fab.setOnLongClickListener(v -> {
            viewModel.freezeAll();
            return true;
        });
    }

    @SuppressLint("RestrictedApi")
    protected void onSetupSorter(Chip sorterAnchor) {
        AppSort[] appSortArray = AppSort.values();
        AppSort currentSort = viewModel.getCurrentAppSort();
        sorterAnchor.setText(currentSort.labelRes);

        sorterAnchor.setOnClickListener(view -> {
            MenuBuilder menuBuilder = new MenuBuilder(requireActivity());
            MenuPopupHelper menuPopupHelper = new MenuPopupHelper(requireActivity(), menuBuilder, view);
            menuPopupHelper.setForceShowIcon(true);

            int reverseItemId = 10086;
            MenuItem reverseItem = menuBuilder.add(1000, reverseItemId, Menu.NONE, github.tornaco.android.thanos.module.common.R.string.common_sort_reverse);
            reverseItem.setCheckable(true);
            reverseItem.setChecked(viewModel.isSortReverse());
            reverseItem.setIcon(github.tornaco.android.thanos.module.common.R.drawable.module_common_ic_arrow_up_down_line);

            for (int i = 0; i < appSortArray.length; i++) {
                AppSort sort = appSortArray[i];
                MenuItem sortItem = menuBuilder.add(1000, i, Menu.NONE, sort.labelRes);
                boolean isSelected = viewModel.getCurrentAppSort() == sort;
                if (isSelected) {
                    sortItem.setTitle(getString(sort.labelRes) + " \uD83C\uDFAF");
                }
            }
            menuBuilder.setCallback(new MenuBuilder.Callback() {
                @Override
                public boolean onMenuItemSelected(@NonNull MenuBuilder menu, @NonNull MenuItem item) {
                    int index = item.getItemId();
                    if (index == reverseItemId) {
                        viewModel.setSortReverse(!viewModel.isSortReverse());
                        item.setChecked(viewModel.isSortReverse());
                    } else {
                        viewModel.setAppSort(appSortArray[index]);
                        sorterAnchor.setText(appSortArray[index].labelRes);
                    }
                    return true;
                }

                @Override
                public void onMenuModeChange(@NonNull MenuBuilder menu) {
                    // Noop
                }
            });
            menuPopupHelper.show();
        });
    }

    @Verify
    private void showItemPopMenu(@NonNull View anchor, @NonNull AppListModel model) {
        AppInfo appInfo = model.appInfo;
        PopupMenu popupMenu = new PopupMenu(requireActivity(), anchor);
        popupMenu.inflate(R.menu.smart_freeze_item_menu);
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_remove_from_smart_freeze) {
                viewModel.disableSmartFreeze(model);
                return true;
            }
            if (item.getItemId() == R.id.action_create_shortcut) {
                ShortcutHelper.addShortcut(requireActivity(), appInfo);
                return true;
            }
            if (item.getItemId() == R.id.action_create_shortcut_apk) {
                AppFeatureManager.INSTANCE.withSubscriptionStatus(requireContext(), isSubscribed -> {
                    if (isSubscribed) {
                        onRequestShortcutStubApk(appInfo);
                    } else {
                        AppFeatureManager.INSTANCE.showDonateIntroDialog(requireActivity());
                    }
                    return null;
                });
                return true;
            }
            if (item.getItemId() == R.id.action_apps_manager) {
                AppFeatureManager.INSTANCE.withSubscriptionStatus(requireContext(), isSubscribed -> {
                    if (isSubscribed) {
                        AppDetailsActivity.start(requireContext(), appInfo);
                    } else {
                        AppFeatureManager.INSTANCE.showDonateIntroDialog(requireActivity());
                    }
                    return null;
                });
                return true;
            }
            if (item.getItemId() == R.id.action_package_set) {
                AppFeatureManager.INSTANCE.withSubscriptionStatus(requireContext(), isSubscribed -> {
                    if (isSubscribed) {
                        new PackageSetChooserDialog(requireActivity(), appInfo.getPkgName(), changed -> {
                            if (changed) {
                                viewModel.start();
                            }
                            return null;
                        }).show();
                    } else {
                        AppFeatureManager.INSTANCE.showDonateIntroDialog(requireActivity());
                    }
                    return null;
                });
                return true;
            }
            return false;
        });
        popupMenu.show();
    }


    @Verify
    private void setupViewModel(String pkgSetId) {
        viewModel = obtainViewModel(requireActivity());
        viewModel.setPkgSetId(pkgSetId);

        binding.setViewmodel(viewModel);
        binding.setLifecycleOwner(this);
        binding.executePendingBindings();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // A little delay make ui looks smoother
        postOnUiDelayed(() -> viewModel.start(), 100);
    }

    private final ActivityResultLauncher<Intent> pickApps = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null && result.getData().hasExtra("apps")) {
            List<AppInfo> appInfos = result.getData().getParcelableArrayListExtra("apps");
            PackageSet packageSet = viewModel.getPackageSet();
            if (packageSet == null) return;
            if (packageSet.isPrebuilt()) {
                onRequestAddToSmartFreezeList(appInfos, false);
            } else {
                onRequestAddToSmartFreezeListAskIfAddToPkgSet(appInfos);
            }
        }
    });

    private final ActivityResultLauncher<Intent> chooseFileExportPackageListQBelow = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
            // Use the provided utility method to parse the result
            List<Uri> files = Utils.getSelectedFilesFromResult(result.getData());
            File file = Utils.getFileForUri(files.get(0));
            doExportPackageListChooseFileQBelow(file);
        }
    });

    private final ActivityResultLauncher<Intent> chooseFileExportPackageListQAndAbove = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
            doExportPackageListChooseFileQAndAbove(result.getData());
        }
    });

    private void onRequestAddToSmartFreezeList(List<AppInfo> appInfos, boolean alsoAddToPkgSet) {
        ModernProgressDialog progress = new ModernProgressDialog(requireActivity());
        progress.setTitle(R.string.common_text_wait_a_moment);
        viewModel.addToSmartFreezeList(appInfos, alsoAddToPkgSet, appInfo -> runOnUiThread(() -> progress.setMessage(appInfo.getAppLabel())), success -> {
            progress.dismiss();
            viewModel.start();
        });
        progress.show();
    }

    private void onRequestAddToSmartFreezeListAskIfAddToPkgSet(List<AppInfo> appInfos) {
        ModernAlertDialog dialog = new ModernAlertDialog(requireActivity());
        dialog.setDialogTitle(getString(R.string.common_fab_title_add));
        dialog.setDialogMessage(getString(R.string.message_do_you_want_to_add_apps_to_pkg_set));
        dialog.setCancelable(false);
        dialog.setNegative(getString(android.R.string.no));
        dialog.setOnNegative(() -> onRequestAddToSmartFreezeList(appInfos, false));
        dialog.setPositive(getString(R.string.common_fab_title_add));
        dialog.setOnPositive(() -> onRequestAddToSmartFreezeList(appInfos, true));
        dialog.show();
    }

    private void createOptionsMenu() {
        binding.toolbar.inflateMenu(R.menu.smart_freeze_menu);
        MenuItem item = binding.toolbar.getMenu().findItem(github.tornaco.android.thanos.module.common.R.id.action_search);
        binding.searchView.setMenuItem(item);
        binding.toolbar.setOnMenuItemClickListener(this::handleOptionsItemSelected);
    }

    @Verify
    private boolean handleOptionsItemSelected(@NonNull MenuItem item) {
        if (R.id.action_settings == item.getItemId()) {
            SmartFreezeSettingsActivity.start(requireActivity());
            return true;
        }
        if (R.id.action_package_set == item.getItemId()) {
            PackageSetListActivity.start(requireActivity());
            return true;
        }
        if (R.id.action_add == item.getItemId()) {
            onRequestAddNewApps();
            return true;
        }
        if (R.id.action_enable_all_smart_freeze == item.getItemId()) {
            AppFeatureManager.INSTANCE.withSubscriptionStatus(requireContext(), isSubscribed -> {
                if (isSubscribed) {
                    new MaterialAlertDialogBuilder(requireActivity()).setTitle(R.string.menu_title_smart_app_freeze_enable_all_smart_freeze_apps).setMessage(R.string.menu_desc_smart_app_freeze_enable_all_smart_freeze_apps).setPositiveButton(android.R.string.ok, (dialog, which) -> {
                        onRequestEnableAllSmartFreezeApps();
                    }).setNegativeButton(android.R.string.cancel, null).show();
                } else {
                    AppFeatureManager.INSTANCE.showDonateIntroDialog(requireActivity());
                }
                return null;
            });
            return true;
        }
        if (R.id.action_enable_all_smart_freeze_temp == item.getItemId()) {
            AppFeatureManager.INSTANCE.withSubscriptionStatus(requireContext(), isSubscribed -> {
                if (isSubscribed) {
                    new MaterialAlertDialogBuilder(requireActivity()).setTitle(R.string.menu_title_smart_app_freeze_enable_all_apps_smart_freeze_temp).setMessage(R.string.menu_desc_smart_app_freeze_enable_all_apps_smart_freeze_temp).setPositiveButton(android.R.string.ok, (dialog, which) -> {
                        onRequestEnableAllSmartFreezeAppsTemp();
                    }).setNegativeButton(android.R.string.cancel, null).show();
                } else {
                    AppFeatureManager.INSTANCE.showDonateIntroDialog(requireActivity());
                }
                return null;
            });
            return true;
        }
        if (R.id.action_enable_all_apps == item.getItemId()) {
            AppFeatureManager.INSTANCE.withSubscriptionStatus(requireContext(), isSubscribed -> {
                if (isSubscribed) {
                    new MaterialAlertDialogBuilder(requireActivity()).setTitle(R.string.menu_title_smart_app_freeze_enable_all_apps).setMessage(R.string.menu_desc_smart_app_freeze_enable_all_apps).setPositiveButton(android.R.string.ok, (dialog, which) -> {
                        onRequestEnableAllApps();
                    }).setNegativeButton(android.R.string.cancel, null).show();
                } else {
                    AppFeatureManager.INSTANCE.showDonateIntroDialog(requireActivity());
                }
                return null;
            });
            return true;
        }

        if (R.id.action_export_package_list == item.getItemId()) {
            AppFeatureManager.INSTANCE.withSubscriptionStatus(requireContext(), isSubscribed -> {
                if (isSubscribed) {
                    onRequestExportPackageList();
                } else {
                    AppFeatureManager.INSTANCE.showDonateIntroDialog(requireActivity());
                }
                return null;
            });
            return true;
        }

        if (R.id.action_import_package_list == item.getItemId()) {
            AppFeatureManager.INSTANCE.withSubscriptionStatus(requireContext(), isSubscribed -> {
                if (isSubscribed) {
                    onRequestImportPackageList();
                } else {
                    AppFeatureManager.INSTANCE.showDonateIntroDialog(requireActivity());
                }
                return null;
            });
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private void onRequestImportPackageList() {
        String[] items = getResources().getStringArray(R.array.module_common_import_selections);
        AlertDialog dialog = new MaterialAlertDialogBuilder(requireActivity()).setTitle(R.string.menu_title_smart_app_freeze_import_package_list).setSingleChoiceItems(items, -1, (d, which) -> {
            d.dismiss();
            if (which == 0) {
                onRequestImportPackageListFromClipBoard();
            } else {
                if (OsUtils.isTOrAbove()) {
                    SmartFreezeAppListFragmentPermissionRequester.onRequestImportPackageListFromFileTOrAboveChecked(this);
                } else {
                    SmartFreezeAppListFragmentPermissionRequester.onRequestImportPackageListFromFileTBelowChecked(this);
                }
            }
        }).create();
        dialog.show();
    }

    @RequiresPermission({Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_AUDIO, Manifest.permission.READ_MEDIA_VIDEO,})
    void onRequestImportPackageListFromFileTOrAbove() {
        IntentUtils.startFilePickerActivityForRes(this, REQUEST_CODE_PICK_IMPORT_PATH);
    }

    @RequiresPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    void onRequestImportPackageListFromFileTBelow() {
        IntentUtils.startFilePickerActivityForRes(this, REQUEST_CODE_PICK_IMPORT_PATH);
    }

    private void doImportPackageListFromFile(Intent data) {
        if (data == null) {
            XLog.e("doImportPackageListFromFile, No data.");
            return;
        }
        Uri uri = data.getData();
        if (uri == null) {
            Toast.makeText(requireActivity(), "uri == null", Toast.LENGTH_LONG).show();
            XLog.e("doImportPackageListFromFile, No uri.");
            return;
        }
        viewModel.importPackageListFromFile(uri, success -> {
            if (success) ToastUtils.ok(requireActivity());
            else ToastUtils.nook(requireActivity());
        });
    }

    private void onRequestImportPackageListFromClipBoard() {
        viewModel.importPackageListFromClipboard(success -> {
            if (success) ToastUtils.ok(requireActivity());
            else ToastUtils.nook(requireActivity());
        });
    }

    private void onRequestExportPackageList() {
        String[] items = getResources().getStringArray(R.array.module_common_export_selections);
        AlertDialog dialog = new MaterialAlertDialogBuilder(requireActivity()).setTitle(R.string.menu_title_smart_app_freeze_export_package_list).setSingleChoiceItems(items, -1, (d, which) -> {
            d.dismiss();
            if (which == 0) {
                onRequestExportPackageListToClipBoard();
            } else {
                if (OsUtils.isTOrAbove()) {
                    SmartFreezeAppListFragmentPermissionRequester.onRequestExportPackageListChooseFileTOrAboveChecked(this);
                } else {
                    SmartFreezeAppListFragmentPermissionRequester.onRequestExportPackageListChooseFileTBelowChecked(this);
                }
            }
        }).create();
        dialog.show();
    }

    private void onRequestExportPackageListToClipBoard() {
        viewModel.exportPackageListToClipBoard(() -> ToastUtils.ok(requireActivity()), throwable -> {
            XLog.e("exportPackageListToClipBoard failed", throwable);
            ToastUtils.nook(requireActivity());
        });
    }

    @RequiresPermission({Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_AUDIO, Manifest.permission.READ_MEDIA_VIDEO,})
    void onRequestExportPackageListChooseFileTOrAbove() {
        onRequestExportPackageListChooseFileQAndAbove();
    }

    @RequiresPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void onRequestExportPackageListChooseFileTBelow() {
        if (OsUtils.isQOrAbove()) {
            onRequestExportPackageListChooseFileQAndAbove();
        } else {
            onRequestExportPackageListChooseFileQBelow();
        }
    }

    private void onRequestExportPackageListChooseFileQAndAbove() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        // you can set file mime-type
        intent.setType("*/*");
        // default file name
        String expFileNameWithExt = "SmartFreeze-Apps" + DateUtils.formatForFileName(System.currentTimeMillis()) + ".json";
        intent.putExtra(Intent.EXTRA_TITLE, expFileNameWithExt);
        chooseFileExportPackageListQAndAbove.launch(intent);
    }

    private void onRequestExportPackageListChooseFileQBelow() {
        Intent intent = pickSingleDirIntent(requireActivity());
        chooseFileExportPackageListQBelow.launch(intent);
    }

    private void doExportPackageListChooseFileQBelow(File file) {
        try {
            String expFileNameWithExt = "SmartFreeze-Apps-" + DateUtils.formatForFileName(System.currentTimeMillis()) + ".json";
            File expFile = new File(file, expFileNameWithExt);
            Files.createParentDirs(expFile);
            OutputStream os = Files.asByteSink(expFile).openStream();
            viewModel.exportPackageListToFile(os, () -> ToastUtils.ok(requireActivity()), throwable -> Toast.makeText(requireActivity(), Log.getStackTraceString(throwable), Toast.LENGTH_LONG).show());
        } catch (Throwable e) {
            XLog.e("doExportPackageListChooseFileQBelow error", e);
            Toast.makeText(requireActivity(), Log.getStackTraceString(e), Toast.LENGTH_LONG).show();
        }
    }

    private void doExportPackageListChooseFileQAndAbove(Intent data) {
        if (data == null) {
            XLog.e("doExportPackageListChooseFileQAndAbove, No data.");
            return;
        }

        Uri fileUri = data.getData();

        if (fileUri == null) {
            Toast.makeText(requireContext(), "fileUri == null", Toast.LENGTH_LONG).show();
            XLog.e("doExportPackageListChooseFileQAndAbove, No fileUri.");
            return;
        }

        XLog.d("doExportPackageListChooseFileQAndAbove, fileUri == %s", fileUri);
        try {
            OutputStream os = Objects.requireNonNull(requireContext()).getContentResolver().openOutputStream(fileUri);
            viewModel.exportPackageListToFile(os, () -> ToastUtils.ok(requireActivity()), throwable -> Toast.makeText(requireActivity(), Log.getStackTraceString(throwable), Toast.LENGTH_LONG).show());
        } catch (IOException e) {
            XLog.e(e);
            Toast.makeText(requireContext(), Log.getStackTraceString(e), Toast.LENGTH_LONG).show();
        }
    }

    private void onRequestEnableAllSmartFreezeApps() {
        ModernProgressDialog progress = new ModernProgressDialog(requireActivity());
        progress.setTitle(R.string.menu_title_smart_app_freeze_enable_all_smart_freeze_apps);
        viewModel.enableAllThanoxDisabledPackages(true, appInfo -> runOnUiThread(() -> progress.setMessage(appInfo.getAppLabel())), success -> {
            progress.dismiss();
            viewModel.start();
        });
        progress.show();
    }

    private void onRequestEnableAllSmartFreezeAppsTemp() {
        ModernProgressDialog progress = new ModernProgressDialog(requireActivity());
        progress.setTitle(R.string.menu_title_smart_app_freeze_enable_all_apps_smart_freeze_temp);
        viewModel.enableAllThanoxDisabledPackages(false, appInfo -> runOnUiThread(() -> progress.setMessage(appInfo.getAppLabel())), success -> {
            progress.dismiss();
            viewModel.start();
        });
        progress.show();
    }

    private void onRequestEnableAllApps() {
        ModernProgressDialog progress = new ModernProgressDialog(requireActivity());
        progress.setTitle(R.string.menu_title_smart_app_freeze_enable_all_apps);
        viewModel.onRequestEnableAllApps(appInfo -> runOnUiThread(() -> progress.setMessage(appInfo.getAppLabel())), success -> {
            progress.dismiss();
            viewModel.start();
        });
        progress.show();
    }

    void onRequestShortcutStubApk(AppInfo appInfo) {
        View dialogView = LayoutInflater.from(requireActivity()).inflate(R.layout.dialog_create_stub_apk, null, false);
        EditText appNameView = dialogView.findViewById(R.id.app_name);
        EditText appVersionCodeView = dialogView.findViewById(R.id.app_version_code);
        EditText appVersionNameView = dialogView.findViewById(R.id.app_version_name);
        Objects.requireNonNull(appNameView).setText(appInfo.getAppLabel());
        Objects.requireNonNull(appVersionCodeView).setText(String.valueOf(appInfo.getVersionCode()));
        Objects.requireNonNull(appVersionNameView).setText(appInfo.getVersionName());

        new MaterialAlertDialogBuilder(requireActivity())
                .setView(dialogView).setTitle(R.string.menu_title_create_shortcut_apk)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    String appName = appNameView.getText().toString();
                    String appVersionName = appVersionNameView.getText().toString();
                    int appVersionCode = appInfo.getVersionCode();
                    try {
                        appVersionCode = Integer.parseInt(appVersionCodeView.getText().toString());
                    } catch (NumberFormatException ignored) {
                    }
                    viewModel.createShortcutStubApkForAsync(appInfo, appName, appVersionName, appVersionCode, file -> onShortcutApkReady(appInfo, file));
                }).show();
    }

    @SuppressLint("CheckResult")
    private void onShortcutApkReady(AppInfo appInfo, File apkFile) {
        new MaterialAlertDialogBuilder(requireActivity())
                .setTitle(R.string.menu_title_create_shortcut_apk)
                .setMessage(appInfo.getAppLabel() + "\n" + apkFile.getAbsolutePath())
                .setNegativeButton(android.R.string.cancel, null)
                .setNeutralButton("SILENT INSTALL", (dialog, which) -> {
                    ModernProgressDialog progressDialog = new ModernProgressDialog(requireActivity());
                    progressDialog.setMessage(getString(R.string.common_text_wait_a_moment));
                    progressDialog.show();
                    Completable.fromRunnable(() -> {
                                File tmpFile = new File("/data/local/tmp/" + appInfo.getPkgName() + "_proxy.apk");
                                Shell.su("cp " + apkFile.getAbsolutePath() + " " + tmpFile.getAbsolutePath()).exec();
                                XLog.w("apk path: " + tmpFile.getAbsolutePath());
                                Shell.Result installRes = Shell.su("pm install " + tmpFile.getAbsolutePath()).exec();
                                XLog.w("Install res: " + installRes);
                                Shell.su("rm " + tmpFile.getAbsolutePath()).exec();
                            }).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Action() {
                                @Override
                                public void run() throws Exception {
                                    progressDialog.dismiss();
                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable e) throws Exception {
                                    DialogUtils.showError(requireActivity(), e);
                                    XLog.e("onShortcutApkReady error", e);
                                    progressDialog.dismiss();
                                }
                            });
                })
                .setPositiveButton(R.string.title_install, (dialog, which) ->
                        viewModel.requestInstallStubApk(requireContext(), apkFile)).show();
    }

    @Override
    public boolean onBackPressed() {
        return closeSearch();
    }

    protected boolean closeSearch() {
        if (binding.searchView.isSearchOpen()) {
            binding.searchView.closeSearch();
            return true;
        }
        return false;
    }

    private void onRequestAddNewApps() {
        int size = viewModel.listModels.size();
        // Limit free to add at most 9 apps.
        if (size > 3) {
            AppFeatureManager.INSTANCE.withSubscriptionStatus(requireContext(), isSubscribed -> {
                if (isSubscribed) {
                } else {
                    AppFeatureManager.INSTANCE.showDonateIntroDialog(requireActivity());
                }
                return null;
            });
        }

        ThanosManager.from(requireContext()).ifServiceInstalled(thanosManager -> {
            ArrayList<Pkg> exclude = Lists.newArrayList(thanosManager.getPkgManager().getSmartFreezePkgs());
            pickApps.launch(AppPickerActivity.getIntent(requireContext(), exclude));
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        SmartFreezeAppListFragmentPermissionRequester.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMPORT_PATH && resultCode == Activity.RESULT_OK) {
            doImportPackageListFromFile(data);
        }
    }

    public static SmartFreezeAppsViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelProvider.AndroidViewModelFactory factory = ViewModelProvider.AndroidViewModelFactory.getInstance(activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(SmartFreezeAppsViewModel.class);
    }
}
