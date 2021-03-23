package exchange.cell.cellexchange.splash;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.github.florent37.viewanimator.ViewAnimator;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import exchange.cell.cellexchange.R;
import exchange.cell.cellexchange.acitivty.BaseActivity;
import exchange.cell.cellexchange.login.LoginActivity;
import exchange.cell.cellexchange.post.PostsActivity;

/**
 * Created by Alexander on 04.10.17.
 */

public class SplashActivity extends BaseActivity implements SplashView {

    @BindView(R.id.tv_powered)
    TextView tvPowered;

    @Inject
    SplashPresenter splashPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        splashPresenter.checkLogin();

        ViewAnimator.animate(tvPowered)
                .alpha(.3f, 1)
                .duration(600)
                .repeatMode(2)
                .repeatCount(-1)
                .start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        splashPresenter.detach();
    }

    @Override
    public void onShowPosts() {
        Intent intent = new Intent(this, PostsActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onShowAuthorization() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
