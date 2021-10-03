package github.tornaco.android.thanos.apps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;

import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.common.AppListModel;
import github.tornaco.android.thanos.common.CommonAppListFilterActivity;
import github.tornaco.android.thanos.common.CommonAppListFilterAdapter;
import github.tornaco.android.thanos.common.CommonAppListFilterViewModel;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.core.pm.PackageManager;
import github.tornaco.android.thanos.core.pm.PackageSet;
import github.tornaco.android.thanos.core.util.Optional;
import github.tornaco.android.thanos.picker.AppPickerActivity;
import github.tornaco.android.thanos.util.ActivityUtils;
import github.tornaco.android.thanos.widget.QuickDropdown;
import github.tornaco.android.thanos.widget.SwitchBar;
import util.CollectionUtils;
import util.ObjectsUtils;

public class PackageSetEditorActivity extends CommonAppListFilterActivity {

  private static final String KEY_PACKAGE_SET_ID = "package_set_id";
  private static final int REQ_PICK_APPS = 0x100;

  private PackageSet packageSet;
  private CommonAppListFilterAdapter adapter;

  public static void start(Activity activity, String id, int reqCode) {
    Bundle data = new Bundle();
    data.putString(KEY_PACKAGE_SET_ID, id);
    ActivityUtils.startActivityForResult(activity, PackageSetEditorActivity.class, reqCode, data);
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    resolveIntent();
    super.onCreate(savedInstanceState);
  }

  private void resolveIntent() {
    Optional.ofNullable(getIntent()).ifPresent(intent -> {
      ThanosManager thanos = ThanosManager.from(getApplicationContext());
      PackageManager pm = thanos.getPkgManager();
      packageSet = pm.getPackageSetById(intent.getStringExtra(KEY_PACKAGE_SET_ID));
    });
  }

  @Override
  protected void onSetupSwitchBar(SwitchBar switchBar) {
    super.onSetupSwitchBar(switchBar);
    switchBar.hide();
  }

  @Override
  protected void onSetupFilter(Chip filterAnchor) {
    super.onSetupFilter(filterAnchor);
    filterAnchor.setVisibility(View.GONE);
    setTitle(getTitleString());
  }

  @Override
  protected void onSetupFab(ExtendedFloatingActionButton fab) {
    fab.setText(null);
    fab.setIconResource(R.drawable.module_common_ic_add_fill);
    fab.show();
    fab.setOnClickListener(
        v -> {
          ArrayList<String> exclude = Lists.newArrayList(packageSet.getPkgNames());
          AppPickerActivity.start(thisActivity(), REQ_PICK_APPS, exclude);
        });
  }

  @Override
  protected String getTitleString() {
    if (packageSet != null) {
      return packageSet.getLabel();
    }
    return super.getTitleString();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (REQ_PICK_APPS == requestCode && resultCode == RESULT_OK && data != null && data
        .hasExtra("apps")) {
      List<AppInfo> appInfos = data.getParcelableArrayListExtra("apps");

      if (!CollectionUtils.isNullOrEmpty(appInfos)) {
        ThanosManager thanos = ThanosManager.from(getApplicationContext());
        PackageManager pm = thanos.getPkgManager();
        for (AppInfo appInfo : appInfos) {
          packageSet.addPackage(appInfo.getPkgName());
          pm.addToPackageSet(appInfo.getPkgName(), packageSet.getId());
        }
        viewModel.start();
      }
    }
  }

  @NonNull
  @Override
  protected CommonAppListFilterViewModel.ListModelLoader onCreateListModelLoader() {
    return index -> {
      if (packageSet == null || packageSet.getPackageCount() == 0) {
        return Lists.newArrayList();
      }
      ThanosManager thanos = ThanosManager.from(getApplicationContext());
      if (!thanos.isServiceInstalled()) {
        return Lists.newArrayList();
      }
      List<AppListModel> res = new ArrayList<>();
      PackageManager pm = thanos.getPkgManager();
      for (String pkg : packageSet.getPkgNames()) {
        AppInfo appInfo = pm.getAppInfo(pkg);
        if (appInfo == null) {
          continue;
        }
        res.add(new AppListModel(appInfo));
      }
      Collections.sort(res);
      return res;
    };
  }

  @Override
  protected CommonAppListFilterAdapter onCreateCommonAppListFilterAdapter() {
    adapter = new CommonAppListFilterAdapter(
        (appInfo, itemView) -> QuickDropdown.show(
            thisActivity(),
            itemView,
            index -> {
              if (index == 0) {
                return getString(R.string.title_package_delete_set);
              }
              return null;
            },
            id -> {
              if (id == 0) {
                ThanosManager.from(thisActivity())
                    .getPkgManager()
                    .removeFromPackageSet(appInfo.getPkgName(), packageSet.getId());
                packageSet.removePackage(appInfo.getPkgName());
                adapter.removeItem(
                    input -> ObjectsUtils.equals(input.appInfo.getPkgName(), appInfo.getPkgName()));
              }
            }));
    return adapter;
  }

  @Override
  protected LayoutManager onCreateLayoutManager() {
    return new GridLayoutManager(thisActivity(), 2);
  }
}
