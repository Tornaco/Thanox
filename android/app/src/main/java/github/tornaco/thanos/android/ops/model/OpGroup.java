package github.tornaco.thanos.android.ops.model;

import androidx.annotation.NonNull;

import java.util.List;

public class OpGroup implements Comparable<OpGroup> {
    private OpsTemplate opsTemplate;
    private List<Op> opList;

    public OpGroup(OpsTemplate opsTemplate, List<Op> opList) {
        this.opsTemplate = opsTemplate;
        this.opList = opList;
    }

    public boolean isEmpty() {
        return opList == null || opList.isEmpty();
    }

    @Override
    public int compareTo(@NonNull OpGroup opGroup) {
        return Integer.compare(opsTemplate.sort, opGroup.opsTemplate.sort);
    }

    public OpsTemplate getOpsTemplate() {
        return this.opsTemplate;
    }

    public List<Op> getOpList() {
        return this.opList;
    }

    public String toString() {
        return "OpGroup(opsTemplate=" + this.getOpsTemplate() + ", opList=" + this.getOpList() + ")";
    }
}
