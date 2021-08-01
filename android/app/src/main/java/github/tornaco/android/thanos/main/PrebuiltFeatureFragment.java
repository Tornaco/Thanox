package github.tornaco.android.thanos.main;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

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
import github.tornaco.android.thanox.module.notification.recorder.NRListActivity;
import github.tornaco.practice.honeycomb.locker.ui.start.LockerStartActivity;
import github.tornaco.thanos.android.module.profile.RuleListActivity;
import github.tornaco.thanos.android.ops.ops.by.ops.AllOpsListActivity;
import github.tornaco.thanos.android.ops.ops.remind.RemindOpsActivity;

public class PrebuiltFeatureFragment extends NavFragment
    implements OnTileClickListener, OnTileLongClickListener, OnHeaderClickListener {
  private FragmentPrebuiltFeaturesBinding prebuiltFeaturesBinding;
  private NavViewModel navViewModel;

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
  }

  @SuppressWarnings("ConstantConditions")
  private void setupViewModel() {
    navViewModel = obtainViewModel(getActivity());
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
    switch (tile.getId()) {
      case R.id.id_one_key_clear:
        if (ThanosApp.isPrc() && !DonateSettings.isActivated(requireContext())) {
          Toast.makeText(
                  requireContext(), R.string.module_donate_donated_available, Toast.LENGTH_SHORT)
              .show();
          return;
        }
        navViewModel.cleanUpBackgroundTasks();
        break;
      case R.id.id_background_start:
        StartRestrictActivity.start(getActivity());
        break;
      case R.id.id_background_restrict:
        BackgroundRestrictActivity.start(getActivity());
        break;
      case R.id.id_clean_task_removal:
        CleanUpOnTaskRemovedActivity.start(getActivity());
        break;
      case R.id.id_apps_manager:
        SuggestedAppsActivity.start(getActivity());
        break;
      case R.id.id_screen_on_notification:
        ScreenOnNotificationActivity.start(getActivity());
        break;
      case R.id.id_notification_recorder:
        NRListActivity.start(getActivity());
        break;
      case R.id.id_trampoline:
        ActivityTrampolineActivity.start(getActivity());
        break;
      case R.id.id_profile:
        RuleListActivity.start(getActivity());
        break;
      case R.id.id_smart_standby:
        if (ThanosApp.isPrc() && !DonateSettings.isActivated(getActivity())) {
          Toast.makeText(
                  getActivity(), R.string.module_donate_donated_available, Toast.LENGTH_SHORT)
              .show();
          break;
        }
        SmartStandbyV2Activity.start(getActivity());
        break;
      case R.id.id_smart_freeze:
        SmartFreezeActivity.start(getActivity());
        break;
      case R.id.id_privacy_cheat:
        DataCheatActivity.start(getActivity());
        break;
      case R.id.id_ops_by_ops:
        AllOpsListActivity.start(getActivity());
        break;
      case R.id.id_task_blur:
        RecentTaskBlurListActivity.start(getActivity());
        break;
      case R.id.id_op_remind:
        RemindOpsActivity.start(getActivity());
        break;
      case R.id.id_app_lock:
        if (ThanosApp.isPrc() && !DonateSettings.isActivated(requireContext())) {
          Toast.makeText(
                  requireContext(), R.string.module_donate_donated_available, Toast.LENGTH_SHORT)
              .show();
          return;
        }
        LockerStartActivity.start(getActivity());
        break;
      case R.id.id_infinite_z:
        InfiniteZActivity.start(getActivity());
        break;
      case R.id.id_plugins:
        PluginListActivity.start(getActivity());
        break;
    }
  }

  @Override
  public void onLongClick(@NonNull Tile tile, @NonNull View view) {
    if (tile.getId() == R.id.id_one_key_clear) {
      showOneKeyBoostPopMenu(view);
    }
  }

  private void showOneKeyBoostPopMenu(@NonNull View view) {
    PopupMenu popupMenu = new PopupMenu(Objects.requireNonNull(getActivity()), view);
    popupMenu.inflate(R.menu.one_key_boost_pop_menu);
    popupMenu.setOnMenuItemClickListener(
        new PopupMenu.OnMenuItemClickListener() {
          @Override
          public boolean onMenuItemClick(MenuItem item) {
            if (item.getItemId() == R.id.create_shortcut) {
              OneKeyBoostShortcutActivity.ShortcutHelper.addShortcut(getActivity());
              return true;
            }
            if (item.getItemId() == R.id.broadcast_intent_shortcut) {
              new AlertDialog.Builder(Objects.requireNonNull(getActivity()))
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
                                  getActivity(),
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
    if (ThanosApp.isPrc() && !DonateSettings.isActivated(getActivity())) {
      Toast.makeText(getActivity(), R.string.module_donate_donated_available, Toast.LENGTH_SHORT)
          .show();
      return;
    }
    ProcessManageActivity.start(getActivity());
  }
}
