package exchange.cell.cellexchange.utils;

import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Created by Alexander on 07.11.17.
 */

public class ListViewPaginator {

    public static final int DEFAULT_THRESHOLD = 5;
    public static final int DEFAULT_FIRST_PAGE = 1;

    private int threshold = DEFAULT_THRESHOLD;

    private ListView listView;

    private View loadMoreView;

    private int currentPage = 1;
    private int firstPage = DEFAULT_FIRST_PAGE;
    private boolean loadMoreShow = false;
    private boolean loadMoreEnabled = true;

    private Listener listener;

    private ListViewPaginator(ListView listView) {
        this.listView = listView;

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastVisibleItem = firstVisibleItem + visibleItemCount;
                if (loadMoreEnabled
                        && !loadMoreShow
                        && totalItemCount >= threshold
                        && totalItemCount <= lastVisibleItem + threshold
                        && listener != null) {
                    listener.onLoadMore();
                }
            }
        });
    }

    public static ListViewPaginator create(ListView listView) {
        return new ListViewPaginator(listView);
    }

    public ListViewPaginator setThreshold(int threshold) {
        this.threshold = threshold;
        return this;
    }

    public ListViewPaginator setListener(Listener listener) {
        this.listener = listener;
        return this;
    }

    public ListViewPaginator setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
        return this;
    }

    public ListViewPaginator setLoadMoreEnabled(boolean loadMoreEnabled) {
        this.loadMoreEnabled = loadMoreEnabled;
        return this;
    }

    public ListViewPaginator setLoadMoreView(View view) {
        this.loadMoreView = view;
        return this;
    }

    public ListViewPaginator setLoadMoreShow(boolean loadMoreShow) {
        this.loadMoreShow = loadMoreShow;
        invalidateLoadMore();
        return this;
    }

    public ListViewPaginator setFirstPage(int firstPage) {
        this.firstPage = firstPage;
        return this;
    }

    public int getFirstPage() {
        return firstPage;
    }

    private void invalidateLoadMore() {
        if (loadMoreShow) {
            listView.addFooterView(loadMoreView);
        } else {
            listView.removeFooterView(loadMoreView);
        }
    }

    public boolean isLoadMoreEnabled() {
        return loadMoreEnabled;
    }

    public boolean isLoadMoreShow() {
        return loadMoreShow;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public interface Listener {
        void onLoadMore();
    }

}
