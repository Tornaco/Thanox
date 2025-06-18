package github.tornaco.android.thanox.module.activity.trampoline;

import android.view.View;

import androidx.annotation.NonNull;

import github.tornaco.android.thanos.core.app.component.ComponentReplacement;

public interface ActivityTrampolineItemClickListener {
    void onItemClick(@NonNull View view, @NonNull ComponentReplacement replacement);
}
