package github.tornaco.thanos.android.ops.ops.repo;

import android.content.Context;

import com.elvishew.xlog.XLog;

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

public class AllOpsLoader {

    public List<OpGroup> getAllOps(Context context) {
        ThanosManager thanos = ThanosManager.from(context);
        Map<OpsTemplate, OpGroup> opsCategoryOpGroupMap = new HashMap<>();
        int numOp = AppOpsManager._NUM_OP;
        if (thanos.isServiceInstalled()) {
            for (int op = 0; op < numOp; op++) {
                OpsTemplate template = Ops.templateOfOp(op);
                XLog.v("template for op: %s %s", template, op);
                if (template != null) {
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
