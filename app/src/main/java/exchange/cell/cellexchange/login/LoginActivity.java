package exchange.cell.cellexchange.login;

import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputType;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.florent37.viewanimator.ViewAnimator;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;

import javax.inject.Inject;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import exchange.cell.cellexchange.R;
import exchange.cell.cellexchange.acitivty.BaseActivity;
import exchange.cell.cellexchange.post.PostsActivity;
import exchange.cell.cellexchange.register.RegisterActivity;
import exchange.cell.cellexchange.utils.DrawableUtils;

/**
 * Created by Alexander on 04.10.17.
 */

public class LoginActivity extends BaseActivity implements LoginView {

    @Inject
    LoginPresenter loginPresenter;

    @BindView(R.id.root_layout)
    ConstraintLayout rootLayout;
    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_login)
    AppCompatButton btnLogin;
    @BindView(R.id.tv_register)
    TextView tvRegister;
    @BindView(R.id.tv_forgot_password)
    TextView tvForgotPassword;
    @BindView(R.id.iv_icon_email)
    ImageView ivIconEmail;
    @BindView(R.id.iv_icon_password)
    ImageView ivIconPassword;
    @BindView(R.id.tv_sign_up_title)
    TextView tvSignUpTitle;
    @BindDimen(R.dimen.authorization_logo_margin_top)
    int logoTopMargin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        if (savedInstanceState == null) {
            ivLogo.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    ivLogo.getViewTreeObserver().removeOnPreDrawListener(this);
                    ConstraintSet constraintSet = new ConstraintSet();
                    constraintSet.clone(rootLayout);

                    ViewAnimator.animate(ivLogo)
                            .custom((view, value) -> {
                                constraintSet.setMargin(view.getId(), ConstraintSet.TOP, (int) value);
                                constraintSet.applyTo(rootLayout);
                            }, (rootLayout.getHeight() - ivLogo.getHeight()) / 2, logoTopMargin)
                            .duration(1000)
                            .andAnimate(etEmail, etPassword, btnLogin, tvRegister,
                                    tvForgotPassword, ivIconEmail, ivIconPassword,
                                    tvSignUpTitle)
                            .alpha(0, 1)
                            .duration(1000)
                            .start();

                    return true;
                }
            });
        }

        KeyboardVisibilityEvent.setEventListener(this, isOpen -> {

            if (isOpen) {
                int keyboardHeight = KeyboardVisibilityEvent.keyboardHeight(LoginActivity.this);
                int screenHeight = getResources().getDisplayMetrics().heightPixels;
                Rect btnLoginRect = new Rect();
                btnLogin.getGlobalVisibleRect(btnLoginRect);
                if (btnLoginRect.bottom + keyboardHeight > screenHeight) {
                    ViewAnimator.animate(rootLayout)
                            .translationY(0, -(screenHeight - btnLoginRect.bottom + 20))
                            .duration(300)
                            .start();
                }
            } else {
                ViewAnimator.animate(rootLayout)
                        .translationY(rootLayout.getTranslationY(), 0)
                        .duration(300)
                        .start();
            }

        });

    }

    @OnClick(R.id.btn_login)
    public void onLoginClick() {
        loginPresenter.login(etEmail.getText().toString(), etPassword.getText().toString());
    }

    @OnClick(R.id.tv_register)
    public void onRegisterClick() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tv_forgot_password)
    public void onForgotPasswordClick() {
        AppCompatEditText etEmail = new AppCompatEditText(this);
        etEmail.setHint(R.string.email);
        etEmail.setMaxLines(1);
        etEmail.setHintTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        etEmail.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        etEmail.setImeOptions(EditorInfo.IME_ACTION_DONE);
        etEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        FrameLayout view = new FrameLayout(this);
        view.addView(etEmail);
        FrameLayout.LayoutParams etEmailLP = (FrameLayout.LayoutParams) etEmail.getLayoutParams();
        etEmailLP.setMarginStart(getResources().getDimensionPixelOffset(R.dimen.dialog_view_edit_text_margin));
        etEmailLP.setMarginEnd(getResources().getDimensionPixelOffset(R.dimen.dialog_view_edit_text_margin));
        etEmail.setLayoutParams(etEmailLP);
        new AlertDialog.Builder(this, R.style.Dialog)
                .setTitle(R.string.forgot_password)
                .setMessage(R.string.forgot_password_dialog_message)
                .setView(view)
                .setPositiveButton(R.string.submit, (dialogInterface, i) -> {
                    UIUtil.hideKeyboard(LoginActivity.this);
                    btnLogin.post(() -> {
                        loginPresenter.forgotPassword(etEmail.getText().toString());
                    });
                })
                .setNegativeButton(R.string.cancel, null)
                .setOnDismissListener(dialogInterface -> UIUtil.hideKeyboard(LoginActivity.this))
                .setOnCancelListener(dialogInterface -> UIUtil.hideKeyboard(LoginActivity.this))
                .show();
    }

    @Override
    protected void onDestroy() {
        loginPresenter.detach();
        super.onDestroy();
    }

    @Override
    public void onShowProgress() {
        showProgress();
    }

    @Override
    public void onHideProgress() {
        hideProgress();
    }

    @Override
    public void onEmailValidationFailed() {
        etEmail.setError(getString(R.string.message_email_validation_failed), DrawableUtils.getDrawableWithIntrinsicBounds(this, R.drawable.ic_warning));
    }

    @Override
    public void onPasswordValidationFailed() {
        etPassword.setError(getString(R.string.message_password_validation_failed), DrawableUtils.getDrawableWithIntrinsicBounds(this, R.drawable.ic_warning));
    }

    @Override
    public void onLoginSuccess() {
        Intent intent = new Intent(this, PostsActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onLoginFailed() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.sign_in)
                .setMessage(R.string.message_login_failed)
                .setPositiveButton(R.string.ok, null)
                .show();
    }

    @Override
    public void onForgotPasswordSuccess() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.forgot_password)
                .setMessage(R.string.message_forgot_password_success)
                .setPositiveButton(R.string.ok, null)
                .show();
    }

    @Override
    public void onForgotPasswordFailed() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.forgot_password)
                .setMessage(R.string.message_reset_password_failed)
                .setPositiveButton(R.string.ok, null)
                .show();
    }
}
