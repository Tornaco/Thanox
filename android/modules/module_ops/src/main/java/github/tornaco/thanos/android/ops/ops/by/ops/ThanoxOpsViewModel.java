package github.tornaco.thanos.android.ops.ops.by.ops;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

import com.elvishew.xlog.XLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.secure.ops.AppOpsManager;
import github.tornaco.android.thanos.core.util.function.Function;
import github.tornaco.thanos.android.ops.model.Op;
import github.tornaco.thanos.android.ops.model.OpGroup;
import github.tornaco.thanos.android.ops.model.Ops;
import github.tornaco.thanos.android.ops.model.OpsTemplate;

public class ThanoxOpsViewModel extends AllOpsListViewModel {

    public ThanoxOpsViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    public Function<Context, List<OpGroup>> getOpsLoader() {
        return this::getThanoxOps;
    }

    private List<OpGroup> getThanoxOps(Context context) {
        ThanosManager thanos = ThanosManager.from(context);
        Map<OpsTemplate, OpGroup> opsCategoryOpGroupMap = new HashMap<>();
        if (thanos.isServiceInstalled()) {
            for (int op : AppOpsManager.getAllOp()) {
                OpsTemplate template = Ops.templateOfOp(op);
                XLog.v("getThanoxOps template for op: %s %s", template, op);
                if (template != null && template.equals(OpsTemplate.THANOX_TEMPLATE)) {
                    OpGroup group = opsCategoryOpGroupMap.get(template);
                    if (group == null) group = new OpGroup(template, new ArrayList<>());
                    Op opm = Ops.toOp(context, op);
                    XLog.v("opm for op: %s %s", opm, op);
                    if (opm != null) {
                        group.getOpList().add(opm);
                    }
                    opsCategoryOpGroupMap.put(template, group);
                }
            }
        }

        List<OpGroup> res = new ArrayList<>();
        for (OpGroup group : opsCategoryOpGroupMap.values()) {
            if (!group.isEmpty()) {
                res.add(group);
            }
        }
        Collections.sort(res);
        return res;
    }
}
