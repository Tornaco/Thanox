package util;

import java.util.ArrayList;
import java.util.List;

import github.tornaco.android.thanos.core.annotation.NonNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

public class ListBatchUtils {

    @Getter
    @AllArgsConstructor
    @ToString
    public static class Batch<T> {
        @NonNull
        private List<List<T>> batches;
    }

    public static <T> Batch<T> toArrayBatch(List<T> sources, int itemCountEachBatch) {
        if (CollectionUtils.isNullOrEmpty(sources)) {
            return new Batch<>(new ArrayList<>(0));
        }
        List<List<T>> res = new ArrayList<>();
        int batchSize = sources.size() / itemCountEachBatch;
        for (int i = 0; i < batchSize; i++) {
            List<T> piece = sources.subList(0, itemCountEachBatch);
            res.add(new ArrayList<>(piece));
            sources.subList(0, itemCountEachBatch).clear();
        }
        if (!sources.isEmpty()) {
            res.add(new ArrayList<>(sources));
        }
        return new Batch<>(res);
    }
}
