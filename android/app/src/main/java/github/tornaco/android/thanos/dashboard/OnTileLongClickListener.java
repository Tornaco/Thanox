package github.tornaco.android.thanos.dashboard;

import android.view.View;

import androidx.annotation.NonNull;

public interface OnTileLongClickListener {
    void onLongClick(@NonNull Tile tile, @NonNull View anchor);
}
