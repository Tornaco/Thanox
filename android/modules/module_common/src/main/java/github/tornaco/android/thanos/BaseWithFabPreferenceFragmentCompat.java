package github.tornaco.android.thanos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceFragmentCompat;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.Objects;

import github.tornaco.android.thanos.module.common.R;
import lombok.Getter;

public abstract class BaseWithFabPreferenceFragmentCompat extends PreferenceFragmentCompat {

    @Getter
    private ExtendedFloatingActionButton fab;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        this.fab = Objects.requireNonNull(view).findViewById(R.id.fab);
        hideFab();
        return view;
    }

    protected void showFab() {
        if (getFab() != null) getFab().show();
    }

    protected void hideFab() {
        if (getFab() != null) getFab().hide();
    }
}
