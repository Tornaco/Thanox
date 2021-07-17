package github.tornaco.android.thanos.widget.section;

import android.view.View;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Adapted from https://gist.github.com/nesquena/d09dc68ff07e845cc622
 */
@SuppressWarnings("WeakerAccess")
public abstract class PagedLoadScrollListener extends RecyclerView.OnScrollListener {

    private static final String TAG = PagedLoadScrollListener.class.getSimpleName();
    private static final int DEFAULT_VISIBLE_THRESHOLD = 5;

    public interface LoadCompleteNotifier {
        /**
         * Call to notify that a load has completed, with new items present.
         */
        void notifyLoadComplete();

        /**
         * Call to notify that a load has completed, but no new items were present, and none will be forthcoming.
         */
        void notifyLoadExhausted();
    }

    private int visibleThreshold;
    private int currentPage = 0;
    private int previousTotalItemCount = 0;
    private boolean loading = false;
    private boolean loadExhausted = false;
    private StickyHeaderLayoutManager layoutManager;

    private LoadCompleteNotifier loadCompleteNotifier = new LoadCompleteNotifier() {
        @Override
        public void notifyLoadComplete() {
            loading = false;
            previousTotalItemCount = layoutManager.getItemCount();
        }

        @Override
        public void notifyLoadExhausted() {
            loadExhausted = true;
        }
    };

    public PagedLoadScrollListener(StickyHeaderLayoutManager layoutManager, int visibleThreshold) {
        this.layoutManager = layoutManager;
        this.visibleThreshold = visibleThreshold;
    }

    public PagedLoadScrollListener(StickyHeaderLayoutManager layoutManager) {
        this(layoutManager, DEFAULT_VISIBLE_THRESHOLD);
    }

    @Override
    public void onScrolled(RecyclerView view, int dx, int dy) {

        // no-op if we're loading, or exhausted
        if (loading || loadExhausted) {
            return;
        }

        // If the total item count is zero and the previous isn't, assume the
        // list is invalidated and should be reset back to initial state
        int totalItemCount = layoutManager.getItemCount();
        if (totalItemCount < previousTotalItemCount) {
            this.currentPage = 0;
            this.previousTotalItemCount = totalItemCount;
        } else if (totalItemCount > 0) {

            View lastVisibleItem = layoutManager.getBottommostChildView();
            int lastVisibleItemAdapterPosition = layoutManager.getViewAdapterPosition(lastVisibleItem);

            if ((lastVisibleItemAdapterPosition + visibleThreshold) > totalItemCount) {
                currentPage++;
                loading = true;

                view.post(new Runnable() {
                    @Override
                    public void run() {
                        onLoadMore(currentPage, loadCompleteNotifier);
                    }
                });
            }
        }
    }

    public void resetPaging() {
        currentPage = 0;
        previousTotalItemCount = 0;
        loading = false;
        loadExhausted = false;
    }

    /**
     * Override this to handle loading of new data. Each time new data is pulled in, the page counter will increase by one.
     * When your load is complete, call the appropriate method on the loadComplete callback.
     *
     * @param page         the page counter. Increases by one each time onLoadMore is called.
     * @param loadComplete callback to invoke when your load has completed.
     */
    public abstract void onLoadMore(int page, LoadCompleteNotifier loadComplete);

}
