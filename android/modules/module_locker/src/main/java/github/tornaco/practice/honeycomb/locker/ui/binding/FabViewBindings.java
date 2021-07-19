package github.tornaco.practice.honeycomb.locker.ui.binding;

import androidx.databinding.BindingAdapter;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import github.tornaco.practice.honeycomb.locker.R;
import github.tornaco.practice.honeycomb.locker.ui.setup.SetupViewModel;

public class FabViewBindings {

    @BindingAdapter("android:lockerFabAction")
    public static void bindFabAction(FloatingActionButton fab,
                                     SetupViewModel setupViewModel) {
        fab.setImageResource(R.drawable.module_locker_ic_arrow_forward_white_24dp);
        fab.setOnClickListener(view -> {
            setupViewModel.onInputConfirm();
            fab.setImageResource(setupViewModel.stage.get()
                    == SetupViewModel.SetupStage.First ? R.drawable.module_locker_ic_arrow_forward_white_24dp
                    : R.drawable.module_locker_ic_check_white_24dp);
        });
    }
}
