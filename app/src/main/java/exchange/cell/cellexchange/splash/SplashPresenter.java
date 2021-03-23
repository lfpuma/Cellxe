package exchange.cell.cellexchange.splash;

import android.os.Handler;

import javax.inject.Inject;

import exchange.cell.cellexchange.presenter.BasePresenter;
import exchange.cell.cellexchange.user.UserManager;

/**
 * Created by Alexander on 04.10.17.
 */

public class SplashPresenter extends BasePresenter<SplashView> {

    private UserManager userManager;

    @Inject
    public SplashPresenter(SplashView splashView, UserManager userManager) {
        super(splashView);
        this.userManager = userManager;
    }

    public void checkLogin() {
        new Handler().postDelayed(() -> {
            if (userManager.isLoggedIn()) {
                if (isAttach()) getView().onShowPosts();
            } else {
                if (isAttach()) getView().onShowAuthorization();
            }
        }, 2400);
    }


}
