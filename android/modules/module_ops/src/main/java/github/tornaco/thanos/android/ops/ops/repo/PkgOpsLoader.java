package github.tornaco.thanos.android.ops.ops.repo;

import android.content.Context;

import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.core.secure.ops.AppOpsManager;
import github.tornaco.android.thanos.core.util.PkgUtils;
import github.tornaco.thanos.android.ops.model.Op;
import github.tornaco.thanos.android.ops.model.OpGroup;
import github.tornaco.thanos.android.ops.model.Ops;
import github.tornaco.thanos.android.ops.model.OpsTemplate;

public class PkgOpsLoader {
    public static final int FILTER_ALL = 0;
    public static final int FILTER_ALLOWED = 1;
    public static final int FILTER_DENIED = 2;

    public List<OpGroup> getPkgOps(Context context, AppInfo appInfo, int filter) {
        ThanosManager thanos = ThanosManager.from(context);
        Map<OpsTemplate, OpGroup> opsCategoryOpGroupMap = new HashMap<>();
        Set<String> permissions = Sets.newHashSet(PkgUtils.getAllDeclaredPermissions(context, appInfo.getPkgName()));
        int numOp = AppOpsManager._NUM_OP;
        if (thanos.isServiceInstalled()) {
            AppOpsManager ops = thanos.getAppOpsManager();
            for (int op = 0; op < numOp; op++) {
                int mode = ops.checkOperationNonCheck(op, appInfo.getUid(), appInfo.getPkgName());

                // Check filter.
                if (filter == FILTER_ALLOWED && mode == AppOpsManager.MODE_IGNORED) continue;
                if (filter == FILTER_DENIED && mode == AppOpsManager.MODE_ALLOWED) continue;

                String perm = AppOpsManager.opToPermission(op);
                boolean show = perm == null
                        || permissions.contains(perm)
                        || appInfo.isDummy()
                        || mode == AppOpsManager.MODE_IGNORED;
                if (show) {
                    OpsTemplate template = Ops.templateOfOp(op);
                    if (template != null) {
                        OpGroup group = opsCategoryOpGroupMap.get(template);
                        if (group == null) group = new OpGroup(template, new ArrayList<>());

                        if (mode != AppOpsManager.MODE_ERRORED) {
                            Op opm = Ops.toOp(context, op, mode);
                            if (opm != null) {
                                group.getOpList().add(opm);
                            }
                        }
                        opsCategoryOpGroupMap.put(template, group);
                    }
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
