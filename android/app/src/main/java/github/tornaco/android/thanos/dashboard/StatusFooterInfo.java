package github.tornaco.android.thanos.dashboard;

import java.util.List;

import util.Consumer;

public class StatusFooterInfo {

    private final List<String> tips;
    private int currentTipIndex;
    private final Consumer<Integer> indexUpdateListener;

    public StatusFooterInfo(List<String> tips, int lastTipIndex, Consumer<Integer> indexUpdateListener) {
        this.tips = tips;
        this.currentTipIndex = lastTipIndex;
        this.indexUpdateListener = indexUpdateListener;
    }

    public String nextTip() {
        if (tips.isEmpty()) return null;
        if (currentTipIndex < 0 || currentTipIndex >= tips.size()) {
            currentTipIndex = 0;
        }
        String tip = tips.get(currentTipIndex);
        currentTipIndex += 1;
        indexUpdateListener.accept(currentTipIndex);
        return tip;
    }
}
