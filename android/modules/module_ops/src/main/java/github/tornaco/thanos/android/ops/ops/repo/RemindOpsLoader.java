package github.tornaco.thanos.android.ops.ops.repo;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.secure.ops.AppOpsManager;
import github.tornaco.thanos.android.ops.model.Op;
import github.tornaco.thanos.android.ops.model.OpGroup;
import github.tornaco.thanos.android.ops.model.Ops;
import github.tornaco.thanos.android.ops.model.OpsTemplate;

public class RemindOpsLoader {

    public List<OpGroup> getRemindOps(Context context) {
        ThanosManager thanos = ThanosManager.from(context);
        Map<OpsTemplate, OpGroup> opsCategoryOpGroupMap = new HashMap<>();
        if (thanos.isServiceInstalled()) {
            AppOpsManager ops = thanos.getAppOpsManager();
            for (int op : AppOpsManager.getAllOp()) {
                OpsTemplate template = Ops.templateOfOp(op);
                if (template != null) {
                    OpGroup group = opsCategoryOpGroupMap.get(template);
                    if (group == null) group = new OpGroup(template, new ArrayList<>());
                    if (ops.isOpRemindable(op)) {
                        Op opm = Ops.toOp(context, op, ops.isOpRemindEnabled(op));
                        if (opm != null) {
                            group.getOpList().add(opm);
                        }
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
