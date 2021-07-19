package github.tornaco.practice.honeycomb.locker;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import github.tornaco.practice.honeycomb.locker.ui.setup.SetupViewModel;
import github.tornaco.practice.honeycomb.locker.ui.verify.VerifyViewModel;

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    @SuppressLint("StaticFieldLeak")
    private static volatile ViewModelFactory INSTANCE;

    private Application application;

    private ViewModelFactory(Application application) {
        this.application = application;
    }

    public static ViewModelFactory getInstance(Application application) {

        if (INSTANCE == null) {
            synchronized (ViewModelFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ViewModelFactory(application);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    @NonNull
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(VerifyViewModel.class)) {
            //noinspection unchecked
            return (T) new VerifyViewModel(application);
        }
        if (modelClass.isAssignableFrom(SetupViewModel.class)) {
            //noinspection unchecked
            return (T) new SetupViewModel(application);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
