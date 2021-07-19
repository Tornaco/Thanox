package github.tornaco.practice.honeycomb.locker.ui.verify;

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

import github.tornaco.android.common.util.ApkUtil;
import github.tornaco.practice.honeycomb.locker.R;
import github.tornaco.practice.honeycomb.locker.databinding.ModuleLockerPatternLockVerifyFragmentBinding;

public class PatternLockVerifyFragment extends Fragment {

    private VerifyViewModel verifyViewModel;
    private boolean shouldHidePwdView = false;
    private ModuleLockerPatternLockVerifyFragmentBinding patternLockVerifyFragmentBinding;

    public static PatternLockVerifyFragment newInstance() {
        return new PatternLockVerifyFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        patternLockVerifyFragmentBinding = ModuleLockerPatternLockVerifyFragmentBinding.inflate(inflater, container, false);
        patternLockVerifyFragmentBinding.navBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() == null) return;
                Objects.requireNonNull(getActivity()).onBackPressed();
            }
        });
        verifyViewModel = VerifyActivity.obtainViewModel(Objects.requireNonNull(getActivity()));
        patternLockVerifyFragmentBinding.setViewmodel(verifyViewModel);
        patternLockVerifyFragmentBinding.label.setText(getString(R.string.module_locker_verify_input_password,
                ApkUtil.loadNameByPkgName(getActivity(), verifyViewModel.pkg)));
        setHasOptionsMenu(true);

        patternLockVerifyFragmentBinding.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!verifyViewModel.isFingerPrintSupportAndEnabled()) {
                    return;
                }
                shouldHidePwdView = !shouldHidePwdView;
                updatePwdViewHiddenState();
            }
        });

        shouldHidePwdView = verifyViewModel.isFingerPrintSupportAndEnabled();
        updatePwdViewHiddenState();

        patternLockVerifyFragmentBinding.forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), R.string.module_locker_pwd_unresetable_message, Toast.LENGTH_LONG).show();
            }
        });

        return patternLockVerifyFragmentBinding.getRoot();
    }

    private void updatePwdViewHiddenState() {
        if (shouldHidePwdView) {
            patternLockVerifyFragmentBinding.patternContainer.setVisibility(View.GONE);
            patternLockVerifyFragmentBinding.forget.setVisibility(View.GONE);
        } else {
            patternLockVerifyFragmentBinding.patternContainer.setVisibility(View.VISIBLE);
            patternLockVerifyFragmentBinding.forget.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        verifyViewModel.verified.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (getActivity() != null) {
                    getActivity().finish();
                }
            }
        });
        verifyViewModel.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        verifyViewModel.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        verifyViewModel.resume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        verifyViewModel.cancel();
    }
}
