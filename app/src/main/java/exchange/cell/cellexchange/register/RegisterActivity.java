package exchange.cell.cellexchange.register;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.florent37.viewanimator.ViewAnimator;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import exchange.cell.cellexchange.R;
import exchange.cell.cellexchange.acitivty.BaseActivity;
import exchange.cell.cellexchange.login.LoginActivity;
import exchange.cell.cellexchange.utils.DrawableUtils;

/**
 * Created by Alexander on 04.10.17.
 */

public class RegisterActivity extends BaseActivity {

    public static final int REGISTER_REQUEST_CODE = 989;

    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_verify_password)
    EditText etVerifyPassword;
    @BindView(R.id.btn_register)
    AppCompatButton btnRegister;
    @BindView(R.id.root_layout)
    ConstraintLayout rootLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        KeyboardVisibilityEvent.setEventListener(this, isOpen -> {

            if (isOpen) {
                int keyboardHeight = KeyboardVisibilityEvent.keyboardHeight(RegisterActivity.this);
                int screenHeight = getResources().getDisplayMetrics().heightPixels;
                Rect btnLoginRect = new Rect();
                btnRegister.getGlobalVisibleRect(btnLoginRect);
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

    @OnClick(R.id.btn_register)
    public void onRegisterClick() {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String verifyPassword = etVerifyPassword.getText().toString();
        if (email.isEmpty()) {
            etEmail.setError(getString(R.string.message_email_validation_failed), DrawableUtils.getDrawableWithIntrinsicBounds(this, R.drawable.ic_warning));
            return;
        }
        if (password.length() < 6) {
            etEmail.setError(getString(R.string.message_password_must_be_at_least_6_simbols), DrawableUtils.getDrawableWithIntrinsicBounds(this, R.drawable.ic_warning));
            return;
        }
        if (!Objects.equals(password, verifyPassword)) {
            etEmail.setError(getString(R.string.message_password_not_equals), DrawableUtils.getDrawableWithIntrinsicBounds(this, R.drawable.ic_warning));
            return;
        }
        UIUtil.hideKeyboard(this);
        rootLayout.postDelayed(() -> {
            Intent intent = new Intent(this, RegisterSecondStageActivity.class);
            intent.putExtra("email", email);
            intent.putExtra("password", password);
            startActivityForResult(intent, REGISTER_REQUEST_CODE);
        }, 300);
    }

    @OnClick(R.id.tv_have_account)
    public void onHaveAccountClick() {
        UIUtil.hideKeyboard(this);
        rootLayout.postDelayed(this::finish, 300);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REGISTER_REQUEST_CODE) {
                finish();
            }
        }
    }
}
