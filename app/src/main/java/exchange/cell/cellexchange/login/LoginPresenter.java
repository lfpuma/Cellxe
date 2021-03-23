package exchange.cell.cellexchange.login;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

import org.reactivestreams.Subscription;

import javax.inject.Inject;

import exchange.cell.cellexchange.presenter.BasePresenter;
import exchange.cell.cellexchange.user.UserManager;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Alexander on 04.10.17.
 */

public class LoginPresenter extends BasePresenter<LoginView> {

    private UserManager userManager;

    @Inject
    public LoginPresenter(LoginView loginView, UserManager userManager) {
        super(loginView);
        this.userManager = userManager;
    }

    public void login(String email, String password) {
        if (email.isEmpty()) {
            if (isAttach()) getView().onEmailValidationFailed();
            return;
        }
        if (password.isEmpty()) {
            if (isAttach()) getView().onPasswordValidationFailed();
            return;
        }
        userManager.login(email, password)
                .flatMap(loginResponseBody -> {
                    String firebaseToken = FirebaseInstanceId.getInstance().getToken();
                    userManager.setToken(loginResponseBody.getToken());
                    userManager.setCurrentUserId(loginResponseBody.getUserId());
                    return userManager.sendFirebaseToken(firebaseToken);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(subscription -> {
                    if (isAttach()) getView().onShowProgress();
                })
                .doOnTerminate(() -> {
                    if (isAttach()) getView().onHideProgress();
                })
                .subscribe(userModel -> {
                    if (isAttach()) getView().onLoginSuccess();
                }, throwable -> {
                    throwable.printStackTrace();
                    if (isAttach()) getView().onLoginFailed();
                });
    }

    public void forgotPassword(String email) {
        if (email.isEmpty()) {
            return;
        }

        userManager.forgotPassword(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(subscription -> {
                    if (isAttach()) getView().onShowProgress();
                })
                .doOnTerminate(() -> {
                    if (isAttach()) getView().onHideProgress();
                })
                .subscribe(responseBody -> {
                    if (isAttach()) getView().onForgotPasswordSuccess();
                }, throwable -> {
                    throwable.printStackTrace();
                    if (isAttach()) getView().onForgotPasswordFailed();

                });
    }


}
