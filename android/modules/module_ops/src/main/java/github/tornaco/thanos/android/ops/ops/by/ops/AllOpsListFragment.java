package github.tornaco.thanos.android.ops.ops.by.ops;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.switchmaterial.SwitchMaterial;

import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.widget.SwitchBar;
import github.tornaco.thanos.android.ops.R;
import github.tornaco.thanos.android.ops.databinding.ModuleOpsLayoutAllOpsBinding;
import github.tornaco.thanos.android.ops.model.Op;
import github.tornaco.thanos.android.ops.ops.OpItemClickListener;

public class AllOpsListFragment extends Fragment {

    private ModuleOpsLayoutAllOpsBinding binding;
    private AllOpsListViewModel viewModel;

    public static AllOpsListFragment newInstance() {
        return new AllOpsListFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ModuleOpsLayoutAllOpsBinding.inflate(LayoutInflater.from(requireContext()));
        setupView();
        setupViewModel();
        return binding.getRoot();
    }

    private void setupView() {
        binding.toolbar.setTitle(getString(R.string.module_ops_feature_title_ops_app_list));

        binding.apps.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.apps.setAdapter(new AllOpsListAdapter(new OpItemClickListener() {
            @Override
            public void onOpItemClick(@NonNull Op op, @NonNull View anchor) {
                OpsAppListActivity.start(requireActivity(), op);
            }
        }));

        binding.swipe.setOnRefreshListener(() -> viewModel.start());
        binding.swipe.setColorSchemeColors(
                getResources().getIntArray(github.tornaco.android.thanos.module.common.R.array.common_swipe_refresh_colors));

        // Switch.
        onSetupSwitchBar(binding.switchBarContainer.switchBar);

        binding.toolbar.inflateMenu(R.menu.module_ops_list);
        binding.toolbar.setOnMenuItemClickListener(item -> {
            if (R.id.action_reset_all_modes == item.getItemId()) {
                new MaterialAlertDialogBuilder(requireActivity())
                        .setTitle(R.string.module_ops_title_reset_ops_mode_for_all)
                        .setMessage(R.string.common_dialog_message_are_you_sure)
                        .setPositiveButton(android.R.string.ok, (dialog, which) ->
                                ThanosManager.from(requireContext())
                                        .ifServiceInstalled(thanosManager ->
                                                thanosManager.getAppOpsManager().resetAllModes("*")))
                        .setNegativeButton(android.R.string.cancel, null)
                        .show();
                return true;
            }
            return false;
        });
    }

    protected void onSetupSwitchBar(SwitchBar switchBar) {
        switchBar.setOnLabel(getString(github.tornaco.android.thanos.module.common.R.string.common_switchbar_title_format,
                getString(R.string.module_ops_feature_title_ops_app_list)));
        switchBar.setOffLabel(getString(github.tornaco.android.thanos.module.common.R.string.common_switchbar_title_format,
                getString(R.string.module_ops_feature_title_ops_app_list)));
        switchBar.setChecked(getSwitchBarCheckState());
        switchBar.addOnSwitchChangeListener(this::onSwitchBarCheckChanged);
    }

    protected boolean getSwitchBarCheckState() {
        return ThanosManager.from(requireContext())
                .isServiceInstalled()
                && ThanosManager.from(requireContext())
                .getAppOpsManager().isOpsEnabled();
    }

    protected void onSwitchBarCheckChanged(SwitchMaterial switchBar, boolean isChecked) {
        ThanosManager.from(requireContext())
                .ifServiceInstalled(thanosManager -> thanosManager.getAppOpsManager()
                        .setOpsEnabled(isChecked));
    }

    private void setupViewModel() {
        viewModel = obtainViewModel(requireActivity());
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        binding.executePendingBindings();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.start();
    }

    public static AllOpsListViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelProvider.AndroidViewModelFactory factory = ViewModelProvider.AndroidViewModelFactory
                .getInstance(activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(AllOpsListViewModel.class);
    }
}
