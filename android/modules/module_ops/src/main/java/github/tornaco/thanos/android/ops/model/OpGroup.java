package github.tornaco.thanos.android.ops.model;

import androidx.annotation.NonNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@Getter
@ToString
public class OpGroup implements Comparable<OpGroup> {
    private OpsTemplate opsTemplate;
    private List<Op> opList;

    public boolean isEmpty() {
        return opList == null || opList.isEmpty();
    }

    @Override
    public int compareTo(@NonNull OpGroup opGroup) {
        return Integer.compare(opsTemplate.sort, opGroup.opsTemplate.sort);
    }
}
