package github.tornaco.android.thanos.main;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
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
import github.tornaco.android.thanos.core.T;
import github.tornaco.android.thanos.core.util.ClipboardUtils;
import github.tornaco.android.thanos.dashboard.DashboardCardAdapter;
import github.tornaco.android.thanos.dashboard.OnHeaderClickListener;
import github.tornaco.android.thanos.dashboard.OnTileClickListener;
import github.tornaco.android.thanos.dashboard.OnTileLongClickListener;
import github.tornaco.android.thanos.dashboard.Tile;
import github.tornaco.android.thanos.dashboard.ViewType;
import github.tornaco.android.thanos.databinding.FragmentPrebuiltFeaturesBinding;
import github.tornaco.android.thanos.feature.access.AppFeatureManager;
import github.tornaco.android.thanos.onboarding.OnBoardingActivity;
import github.tornaco.android.thanos.process.v2.ProcessManageActivityV2;

public class PrebuiltFeatureFragment extends NavFragment
        implements OnTileClickListener, OnTileLongClickListener, OnHeaderClickListener {
    private FragmentPrebuiltFeaturesBinding prebuiltFeaturesBinding;
    private NavViewModel navViewModel;

    private Handler uiHandler;
    private PrebuiltFeatureLauncher featureLauncher;

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
        prebuiltFeaturesBinding.features.getRecycledViewPool().setMaxRecycledViews(ViewType.HEADER, 0);
        prebuiltFeaturesBinding.features.getRecycledViewPool().setMaxRecycledViews(ViewType.ITEM, 0);
        prebuiltFeaturesBinding.features.getRecycledViewPool().setMaxRecycledViews(ViewType.FOOTER, 0);

        prebuiltFeaturesBinding.features.setLayoutManager(new GridLayoutManager(getContext(), 1));
        prebuiltFeaturesBinding.features.setAdapter(new DashboardCardAdapter(this, this, this));
        prebuiltFeaturesBinding.swipe.setColorSchemeColors(getResources().getIntArray(
                github.tornaco.android.thanos.module.common.R.array.common_swipe_refresh_colors));
        prebuiltFeaturesBinding.swipe.setOnRefreshListener(() -> navViewModel.start());
    }

    private void setupViewModel() {
        navViewModel = obtainViewModel(requireActivity());
        prebuiltFeaturesBinding.setViewmodel(navViewModel);
        prebuiltFeaturesBinding.executePendingBindings();

        this.featureLauncher = new PrebuiltFeatureLauncher(requireActivity(), uiHandler, navViewModel);
    }

    private static NavViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelProvider.AndroidViewModelFactory factory =
                ViewModelProvider.AndroidViewModelFactory.getInstance(activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(NavViewModel.class);
    }

    @Override
    public void onClick(@NonNull Tile tile) {
        featureLauncher.launch(tile.getId());
    }

    @Override
    public void onLongClick(@NonNull Tile tile, @NonNull View view) {
        if (tile.getId() == PrebuiltFeatureIds.ID_ONE_KEY_CLEAR) {
            showOneKeyBoostPopMenu(view);
        } else if (tile.getId() == PrebuiltFeatureIds.ID_GUIDE) {
            OnBoardingActivity.Starter.INSTANCE.start(requireActivity());
        } else {
            showCommonTilePopMenu(view, tile);
        }
    }

    private void showOneKeyBoostPopMenu(@NonNull View view) {
        PopupMenu popupMenu = new PopupMenu(Objects.requireNonNull(requireActivity()), view);
        popupMenu.inflate(R.menu.one_key_boost_pop_menu);
        popupMenu.setOnMenuItemClickListener(
                item -> {
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
                                        (dialog, which) -> {
                                            ClipboardUtils.copyToClipboard(
                                                    requireContext(),
                                                    "one-ley-boost-intent",
                                                    T.Actions.ACTION_RUNNING_PROCESS_CLEAR);
                                            Toast.makeText(
                                                            requireActivity(),
                                                            R.string.common_toast_copied_to_clipboard,
                                                            Toast.LENGTH_SHORT)
                                                    .show();
                                        })
                                .setCancelable(true)
                                .show();
                        return true;
                    }
                    return false;
                });
        popupMenu.show();
    }

    private void showCommonTilePopMenu(@NonNull View view, @NonNull Tile tile) {
        PopupMenu popupMenu = new PopupMenu(Objects.requireNonNull(requireActivity()), view);
        popupMenu.inflate(R.menu.nav_common_feature_pop_up_menu);
        popupMenu.setOnMenuItemClickListener(
                item -> {
                    if (item.getItemId() == R.id.create_shortcut) {
                        PrebuiltFeatureShortcutActivity.ShortcutHelper.addShortcut(requireActivity(), tile);
                        return true;
                    }
                    return false;
                });
        popupMenu.show();
    }

    @Override
    public void onHeaderClick() {
        AppFeatureManager.INSTANCE.withSubscriptionStatus(requireContext(), isSubscribed -> {
            if (isSubscribed) {
                ProcessManageActivityV2.Starter.INSTANCE.start(requireActivity());
            } else {
                AppFeatureManager.INSTANCE.showDonateIntroDialog(requireActivity());
            }
            return null;
        });
    }
}
