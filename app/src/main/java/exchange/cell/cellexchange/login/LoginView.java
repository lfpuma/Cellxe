package exchange.cell.cellexchange.login;

/**
 * Created by Alexander on 04.10.17.
 */

public interface LoginView {

    void onShowProgress();
    void onHideProgress();
    void onEmailValidationFailed();
    void onPasswordValidationFailed();
    void onLoginSuccess();
    void onLoginFailed();
    void onForgotPasswordSuccess();
    void onForgotPasswordFailed();

}
