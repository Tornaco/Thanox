package github.tornaco.practice.honeycomb.locker.ui.setup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Observable;
import androidx.fragment.app.Fragment;

import java.util.Objects;

import github.tornaco.android.thanos.core.app.activity.ActivityStackSupervisor;
import github.tornaco.practice.honeycomb.locker.R;
import github.tornaco.practice.honeycomb.locker.databinding.ModuleLockerPatternLockSetupFragmentBinding;

public class PatternLockSetupFragment extends Fragment {

    private SetupViewModel setupViewModel;
    private ModuleLockerPatternLockSetupFragmentBinding patternLockSetupFragmentBinding;

    public static PatternLockSetupFragment newInstance() {
        return new PatternLockSetupFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        patternLockSetupFragmentBinding = ModuleLockerPatternLockSetupFragmentBinding.inflate(inflater, container, false);
        setupViewModel = SetupActivity.obtainViewModel(Objects.requireNonNull(getActivity()));
        patternLockSetupFragmentBinding.setViewmodel(setupViewModel);
        return patternLockSetupFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupViewModel.setupComplete.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (getActivity() != null) {
                    getActivity().finish();
                }
            }
        });
        setupViewModel.start(ActivityStackSupervisor.LockerMethod.PATTERN);
        setupViewModel.setupComplete.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (getActivity() != null) {
                    Toast.makeText(getActivity().getApplicationContext(),
                            R.string.module_locker_setup_complete, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        setupViewModel.cancel();
    }
}
