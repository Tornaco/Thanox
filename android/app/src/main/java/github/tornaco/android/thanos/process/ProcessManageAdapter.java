package github.tornaco.android.thanos.process;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elvishew.xlog.XLog;

import java.util.ArrayList;
import java.util.List;

import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.common.AppItemViewLongClickListener;
import github.tornaco.android.thanos.common.AppListModel;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.core.process.RunningState;
import github.tornaco.android.thanos.databinding.ItemProcessManageBinding;
import util.Consumer;

public class ProcessManageAdapter extends RecyclerView.Adapter<ProcessManageAdapter.VH>
        implements Consumer<List<RunningState.MergedItem>> {

    private final List<RunningState.MergedItem> processModels = new ArrayList<>();
    private final RunningItemViewClickListener listener;
    private final AppItemViewLongClickListener longClickListener;

    ProcessManageAdapter(RunningItemViewClickListener listener,
                         AppItemViewLongClickListener longClickListener) {
        this.listener = listener;
        this.longClickListener = longClickListener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(ItemProcessManageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        RunningState.MergedItem model = processModels.get(position);
        AppInfo appInfo = model.appInfo;
        holder.binding.setProcess(model);
        holder.binding.setIsLastOne(false);
        holder.binding.setListener(view -> listener.onAppItemClick(model));
        holder.binding.appItemRoot.setOnLongClickListener(v -> {
            longClickListener.onAppItemLongClick(holder.binding.appItemRoot, new AppListModel(appInfo));
            return true;
        });

        holder.binding.setBadge1Str(model.mSizeStr);
        XLog.v("mCurSizeStr: %s", model.mCurSizeStr);
        XLog.v("mSizeStr: %s", model.mSizeStr);
        XLog.v("mSize: %s", model.mSize);

        String idleBadge = holder.binding.appItemRoot.getContext().getString(R.string.badge_app_idle);
        if (appInfo.isIdle()) {
            holder.binding.setBadge2Str(idleBadge);
        } else {
            holder.binding.setBadge2Str(null);
        }

        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return processModels.size();
    }

    @Override
    public void accept(List<RunningState.MergedItem> processModels) {
        this.processModels.clear();
        this.processModels.addAll(processModels);
        notifyDataSetChanged();
    }

    static final class VH extends RecyclerView.ViewHolder {
        private ItemProcessManageBinding binding;

        VH(@NonNull ItemProcessManageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public github.tornaco.android.thanos.databinding.ItemProcessManageBinding getBinding() {
            return this.binding;
        }
    }
}
