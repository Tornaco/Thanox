package github.tornaco.android.thanos.main;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Objects;

import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.ThanosApp;
import github.tornaco.android.thanos.app.donate.DonateSettings;
import github.tornaco.android.thanos.apps.SuggestedAppsActivity;
import github.tornaco.android.thanos.core.T;
import github.tornaco.android.thanos.core.util.ClipboardUtils;
import github.tornaco.android.thanos.dashboard.DashboardCardAdapter;
import github.tornaco.android.thanos.dashboard.OnHeaderClickListener;
import github.tornaco.android.thanos.dashboard.OnTileClickListener;
import github.tornaco.android.thanos.dashboard.OnTileLongClickListener;
import github.tornaco.android.thanos.dashboard.Tile;
import github.tornaco.android.thanos.databinding.FragmentPrebuiltFeaturesBinding;
import github.tornaco.android.thanos.infinite.InfiniteZActivity;
import github.tornaco.android.thanos.notification.ScreenOnNotificationActivity;
import github.tornaco.android.thanos.power.SmartFreezeActivity;
import github.tornaco.android.thanos.power.SmartStandbyV2Activity;
import github.tornaco.android.thanos.privacy.DataCheatActivity;
import github.tornaco.android.thanos.process.ProcessManageActivity;
import github.tornaco.android.thanos.start.BackgroundRestrictActivity;
import github.tornaco.android.thanos.start.StartRestrictActivity;
import github.tornaco.android.thanos.task.CleanUpOnTaskRemovedActivity;
import github.tornaco.android.thanos.task.RecentTaskBlurListActivity;
import github.tornaco.android.thanox.module.activity.trampoline.ActivityTrampolineActivity;
import github.tornaco.android.thanox.module.notification.recorder.ui.NotificationRecordActivity;
import github.tornaco.practice.honeycomb.locker.ui.start.LockerStartActivity;
import github.tornaco.thanos.android.module.profile.RuleListActivity;
import github.tornaco.thanos.android.ops.OpsBottomNavActivity;
import github.tornaco.thanos.android.ops.ops.remind.RemindOpsActivity;

public class PrebuiltFeatureFragment extends NavFragment
        implements OnTileClickListener, OnTileLongClickListener, OnHeaderClickListener {
    private FragmentPrebuiltFeaturesBinding prebuiltFeaturesBinding;
    private NavViewModel navViewModel;

    private Handler uiHandler;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.uiHandler = new Handler(Looper.getMainLooper());
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        prebuiltFeaturesBinding = FragmentPrebuiltFeaturesBinding.inflate(inflater, container, false);
        setupView();
        setupViewModel();
        return prebuiltFeaturesBinding.getRoot();
    }

    private void setupView() {
        prebuiltFeaturesBinding.features.setLayoutManager(new GridLayoutManager(getContext(), 1));
        prebuiltFeaturesBinding.features.setAdapter(new DashboardCardAdapter(this, this, this));
        prebuiltFeaturesBinding.swipe.setColorSchemeColors(getResources().getIntArray(
                github.tornaco.android.thanos.module.common.R.array.common_swipe_refresh_colors));
        prebuiltFeaturesBinding.swipe.setOnRefreshListener(() -> navViewModel.start());
    }

    @SuppressWarnings("ConstantConditions")
    private void setupViewModel() {
        navViewModel = obtainViewModel(requireActivity());
        prebuiltFeaturesBinding.setViewmodel(navViewModel);
        prebuiltFeaturesBinding.executePendingBindings();
    }

    private static NavViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelProvider.AndroidViewModelFactory factory =
                ViewModelProvider.AndroidViewModelFactory.getInstance(activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(NavViewModel.class);
    }

    @Override
    public void onClick(@NonNull Tile tile) {
        if (tile.getId() == R.id.id_one_key_clear) {
            if (ThanosApp.isPrc() && !DonateSettings.isActivated(requireContext())) {
                Toast.makeText(
                        requireContext(), R.string.module_donate_donated_available, Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            navViewModel.cleanUpBackgroundTasks();
            // Delay 1.5s to refresh
            uiHandler.postDelayed(() -> navViewModel.start(), 1500);
        } else if (tile.getId() == R.id.id_background_start) {
            StartRestrictActivity.start(requireActivity());
        } else if (tile.getId() == R.id.id_background_restrict) {
            BackgroundRestrictActivity.start(requireActivity());
        } else if (tile.getId() == R.id.id_clean_task_removal) {
            CleanUpOnTaskRemovedActivity.start(requireActivity());
        } else if (tile.getId() == R.id.id_apps_manager) {
            SuggestedAppsActivity.start(requireActivity());
        } else if (tile.getId() == R.id.id_screen_on_notification) {
            ScreenOnNotificationActivity.start(requireActivity());
        } else if (tile.getId() == R.id.id_notification_recorder) {
            NotificationRecordActivity.Starter.start(requireActivity());
        } else if (tile.getId() == R.id.id_trampoline) {
            ActivityTrampolineActivity.start(requireActivity());
        } else if (tile.getId() == R.id.id_profile) {
            RuleListActivity.start(requireActivity());
        } else if (tile.getId() == R.id.id_smart_standby) {
            if (ThanosApp.isPrc() && !DonateSettings.isActivated(requireActivity())) {
                Toast.makeText(
                        requireActivity(), R.string.module_donate_donated_available, Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            SmartStandbyV2Activity.start(requireActivity());
        } else if (tile.getId() == R.id.id_smart_freeze) {
            SmartFreezeActivity.start(requireActivity());
        } else if (tile.getId() == R.id.id_privacy_cheat) {
            DataCheatActivity.start(requireActivity());
        } else if (tile.getId() == R.id.id_ops_by_ops) {
            OpsBottomNavActivity.Starter.start(requireActivity());
        } else if (tile.getId() == R.id.id_task_blur) {
            RecentTaskBlurListActivity.start(requireActivity());
        } else if (tile.getId() == R.id.id_op_remind) {
            RemindOpsActivity.start(requireActivity());
        } else if (tile.getId() == R.id.id_app_lock) {
            if (ThanosApp.isPrc() && !DonateSettings.isActivated(requireContext())) {
                Toast.makeText(
                        requireContext(), R.string.module_donate_donated_available, Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            LockerStartActivity.start(requireActivity());
        } else if (tile.getId() == R.id.id_infinite_z) {
            InfiniteZActivity.start(requireActivity());
        } else if (tile.getId() == R.id.id_plugins) {
            PluginListActivity.start(requireActivity());
        }
    }

    @Override
    public void onLongClick(@NonNull Tile tile, @NonNull View view) {
        if (tile.getId() == R.id.id_one_key_clear) {
            showOneKeyBoostPopMenu(view);
        }
    }

    private void showOneKeyBoostPopMenu(@NonNull View view) {
        PopupMenu popupMenu = new PopupMenu(Objects.requireNonNull(requireActivity()), view);
        popupMenu.inflate(R.menu.one_key_boost_pop_menu);
        popupMenu.setOnMenuItemClickListener(
                new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.create_shortcut) {
                            OneKeyBoostShortcutActivity.ShortcutHelper.addShortcut(requireActivity());
                            return true;
                        }
                        if (item.getItemId() == R.id.broadcast_intent_shortcut) {
                            new MaterialAlertDialogBuilder(Objects.requireNonNull(requireActivity()))
                                    .setTitle(getString(R.string.menu_title_broadcast_intent_shortcut))
                                    .setMessage(T.Actions.ACTION_RUNNING_PROCESS_CLEAR)
                                    .setPositiveButton(
                                            R.string.menu_title_copy,
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    ClipboardUtils.copyToClipboard(
                                                            Objects.requireNonNull(getContext()),
                                                            "one-ley-boost-intent",
                                                            T.Actions.ACTION_RUNNING_PROCESS_CLEAR);
                                                    Toast.makeText(
                                                            requireActivity(),
                                                            R.string.common_toast_copied_to_clipboard,
                                                            Toast.LENGTH_SHORT)
                                                            .show();
                                                }
                                            })
                                    .setCancelable(true)
                                    .show();
                            return true;
                        }
                        return false;
                    }
                });
        popupMenu.show();
    }

    @Override
    public void onClick() {
        if (ThanosApp.isPrc() && !DonateSettings.isActivated(requireActivity())) {
            Toast.makeText(requireActivity(), R.string.module_donate_donated_available, Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        ProcessManageActivity.start(requireActivity());
    }
}
