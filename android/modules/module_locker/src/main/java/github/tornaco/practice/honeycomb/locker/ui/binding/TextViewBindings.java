package github.tornaco.practice.honeycomb.locker.ui.binding;

import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.Observable;

import github.tornaco.practice.honeycomb.locker.R;
import github.tornaco.practice.honeycomb.locker.ui.setup.SetupViewModel;
import github.tornaco.practice.honeycomb.locker.ui.verify.VerifyViewModel;

public class TextViewBindings {

    @BindingAdapter("android:tips")
    public static void bindTextView(TextView textView, SetupViewModel setupViewModel) {
        textView.setText(R.string.module_locker_setup_step_draw_or_input);
        setupViewModel.setupError.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                textView.setText(R.string.module_locker_setup_step_err_mismatch);
            }
        });
        setupViewModel.stage.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (setupViewModel.setupError.get()
                        == SetupViewModel.SETUP_ERROR_KEY_NONE) {
                    textView.setText(setupViewModel.stage.get()
                            == SetupViewModel.SetupStage.First ?
                            R.string.module_locker_setup_step_draw_or_input
                            : R.string.module_locker_setup_step_draw_or_input_confirm);
                }
            }
        });
    }

    @BindingAdapter("android:tips")
    public static void bindTextView(TextView textView, VerifyViewModel setupViewModel) {
        setupViewModel.failCount.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                textView.setText(R.string.module_locker_setup_verify_err_mismatch);
            }
        });
        setupViewModel.verified.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                textView.setText(R.string.module_locker_setup_verify_pass);
            }
        });
    }
}
