package github.tornaco.thanos.android.ops.ops.remind;

import androidx.annotation.NonNull;

import github.tornaco.thanos.android.ops.model.Op;

public interface OpItemSwitchChangeListener {
    void onOpItemSwitchChanged(@NonNull Op op, boolean checked);
}
