package github.tornaco.android.thanos.process;

import github.tornaco.android.thanos.core.process.RunningState;

public interface RunningItemViewClickListener {
    void onAppItemClick(RunningState.MergedItem mergedItem);
}
