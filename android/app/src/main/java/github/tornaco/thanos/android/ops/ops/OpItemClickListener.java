package github.tornaco.thanos.android.ops.ops;

import android.view.View;

import androidx.annotation.NonNull;

import github.tornaco.thanos.android.ops.model.Op;

public interface OpItemClickListener {
    void onOpItemClick(@NonNull Op op, @NonNull View anchor);
}
