package exchange.cell.cellexchange.presenter;

import java.lang.ref.WeakReference;

/**
 * Created by Alexander on 04.10.17.
 */

public class BasePresenter<View> {

    private WeakReference<View> view;

    public BasePresenter(View view) {
        this.view = new WeakReference<>(view);
    }

    public void detach() {
        if (view != null) {
            view.clear();
            view = null;
        }
    }

    public boolean isAttach() {
        return view != null && view.get() != null;
    }

    public View getView() {
        return view != null ? view.get() : null;
    }

}
