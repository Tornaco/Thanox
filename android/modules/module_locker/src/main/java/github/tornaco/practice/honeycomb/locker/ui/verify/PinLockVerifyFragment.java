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
import github.tornaco.practice.honeycomb.locker.databinding.ModuleLockerPinLockVerifyFragmentBinding;

public class PinLockVerifyFragment extends Fragment {

    private VerifyViewModel verifyViewModel;
    private boolean shouldHidePwdView = false;
    private ModuleLockerPinLockVerifyFragmentBinding pinLockVerifyFragmentBinding;

    public static PinLockVerifyFragment newInstance() {
        return new PinLockVerifyFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        pinLockVerifyFragmentBinding = ModuleLockerPinLockVerifyFragmentBinding.inflate(inflater, container, false);
        pinLockVerifyFragmentBinding.navBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() == null) return;
                Objects.requireNonNull(getActivity()).onBackPressed();
            }
        });
        pinLockVerifyFragmentBinding.pinLockView.attachIndicatorDots(pinLockVerifyFragmentBinding.indicatorDots);
        verifyViewModel = VerifyActivity.obtainViewModel(Objects.requireNonNull(getActivity()));
        pinLockVerifyFragmentBinding.setViewmodel(verifyViewModel);
        pinLockVerifyFragmentBinding.label.setText(getString(R.string.module_locker_verify_input_password,
                ApkUtil.loadNameByPkgName(getActivity(), verifyViewModel.pkg)));
        setHasOptionsMenu(true);

        pinLockVerifyFragmentBinding.icon.setOnClickListener(new View.OnClickListener() {
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

        pinLockVerifyFragmentBinding.forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), R.string.module_locker_pwd_unresetable_message, Toast.LENGTH_LONG).show();
            }
        });

        return pinLockVerifyFragmentBinding.getRoot();
    }

    private void updatePwdViewHiddenState() {
        if (shouldHidePwdView) {
            pinLockVerifyFragmentBinding.patternContainer.setVisibility(View.GONE);
            pinLockVerifyFragmentBinding.forget.setVisibility(View.GONE);
        } else {
            pinLockVerifyFragmentBinding.patternContainer.setVisibility(View.VISIBLE);
            pinLockVerifyFragmentBinding.forget.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        verifyViewModel.verified.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (getActivity() == null) return;
                Objects.requireNonNull(getActivity()).finish();
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
