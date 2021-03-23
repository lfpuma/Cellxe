package exchange.cell.cellexchange.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.transition.TransitionManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jakewharton.rxbinding2.widget.RxAbsListView;
import com.jakewharton.rxbinding2.widget.RxAdapterView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import exchange.cell.cellexchange.acitivty.BaseActivity;
import exchange.cell.cellexchange.R;
import exchange.cell.cellexchange.list.EndlessScrollListener;
import exchange.cell.cellexchange.login.LoginActivity;
import exchange.cell.cellexchange.post.PostActivity;
import exchange.cell.cellexchange.post.PostAdapter;
import exchange.cell.cellexchange.post.PostManager;
import exchange.cell.cellexchange.post.PostModel;
import exchange.cell.cellexchange.post.PostsActivity;
import exchange.cell.cellexchange.user.UserManager;
import exchange.cell.cellexchange.user.UserModel;
import exchange.cell.cellexchange.utils.ListViewPaginator;

/**
 * Created by Alexander on 01.10.17.
 */

public class ProfileActivity extends BaseActivity implements ProfileView {

    @BindView(R.id.coordinator)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.app_bar)
    AppBarLayout appBarLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.list_view)
    ListView listView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    SimpleDraweeView ivCover;
    SimpleDraweeView ivAvatar;
    TextView tvName;
    TextView tvTradingName;
    TextView tvRole;
    SimpleDraweeView ivFlag;
    TextView tvInfo;
    TextView tvPhone;
    TextView tvEmail;

    private FrameLayout headerProfile;
    private View footerLoading;

    private PostAdapter postAdapter;

    @Inject
    ProfilePresenter profilePresenter;

    private int userId = -1;
    private UserModel userModel;

    private ListViewPaginator listViewPaginator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        if (getIntent() != null) {
            userId = getIntent().getIntExtra("user_id", UserManager.getCurrentUserId(this));
        }

        setSupportActionBar(toolbar);
        setTitle(R.string.profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        postAdapter = new PostAdapter(this);
        listView.setAdapter(postAdapter);
        if (headerProfile == null) {
            headerProfile = (FrameLayout) LayoutInflater.from(this).inflate(R.layout.header_profile, listView, false);
            ivCover = headerProfile.findViewById(R.id.iv_cover);
            ivAvatar = headerProfile.findViewById(R.id.iv_avatar);
            tvName = headerProfile.findViewById(R.id.tv_name);
            tvTradingName = headerProfile.findViewById(R.id.tv_trading_name);
            tvRole = headerProfile.findViewById(R.id.tv_role);
            ivFlag = headerProfile.findViewById(R.id.iv_flag);
            tvInfo = headerProfile.findViewById(R.id.tv_info);
            tvPhone = headerProfile.findViewById(R.id.tv_phone);
            tvEmail = headerProfile.findViewById(R.id.tv_email);
        }

        footerLoading = LayoutInflater.from(this).inflate(R.layout.footer_loading, listView, false);
        RxAdapterView.itemClickEvents(listView)
                .subscribe(adapterViewItemClickEvent -> {
                    int position = adapterViewItemClickEvent.position();
                    PostModel postModel = postAdapter.getItem(position - 1);
                    Intent intent = new Intent(this, PostActivity.class);
                    intent.putExtra("post", postModel);
                    startActivityForResult(intent, PostsActivity.POST_DETAILS_ACTIVITY_REQUEST_CODE);
                });


        RxAbsListView.scrollEvents(listView)
                .subscribe(event -> ivCover.animate().translationY(-headerProfile.getTop()).setDuration(0).start(), Throwable::printStackTrace);

        postAdapter.setLikeClickListener(postModel -> {
            profilePresenter.like(postModel.getId(), !postModel.isLiked());
        });

        postAdapter.setCommentClickListener(postModel -> {
            Intent intent = new Intent(this, PostActivity.class);
            intent.putExtra("post", postModel);
            intent.putExtra("comment", true);
            startActivityForResult(intent, PostsActivity.POST_DETAILS_ACTIVITY_REQUEST_CODE);
        });

        postAdapter.setShareClickListener(postModel -> PostManager.sharePost(this, postModel));

        tvPhone.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", userModel.getMobile(), null));
            startActivity(intent);
        });

        tvEmail.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_EMAIL, userModel.getEmail());
            startActivity(Intent.createChooser(intent, "Send Email"));
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            listViewPaginator.setCurrentPage(listViewPaginator.getFirstPage());
            listViewPaginator.setLoadMoreEnabled(false);
            profilePresenter.getUserAndPosts(userId);
        });

        listViewPaginator = ListViewPaginator.create(listView)
                .setLoadMoreView(LayoutInflater.from(this).inflate(R.layout.footer_loading, listView, false))
                .setListener(() -> profilePresenter.getNextPostPage(userId, listViewPaginator.getCurrentPage()));

        swipeRefreshLayout.post(() -> {
            listViewPaginator.setLoadMoreEnabled(false);
            profilePresenter.getUserAndPosts(userId);
        });
    }

    @Override
    protected void onDestroy() {
        profilePresenter.detach();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        } else if (item.getTitle().equals(getString(R.string.logout))) {
            profilePresenter.logout();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (userId == UserManager.getCurrentUserId(this))
            menu.add(getString(R.string.logout));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onGetUser(UserModel userModel) {
        this.userModel = userModel;
        if (listView.getHeaderViewsCount() == 0) {
            listView.addHeaderView(headerProfile, null, false);
        }
        TransitionManager.beginDelayedTransition(headerProfile);
        ivCover.setImageURI(userModel.getCover());
        ivAvatar.setImageURI(userModel.getPhoto());
        tvName.setText(userModel.getFullName());
        tvTradingName.setText(userModel.getRegisteredTradingName());
        ivFlag.setImageURI(userModel.getCountry() == null ? null : userModel.getCountry().getImage());
        tvRole.setText(userModel.getUserInfo());
        tvInfo.setText(userModel.getBusinessProfile());
        tvPhone.setText(userModel.getMobile());
        tvEmail.setText(userModel.getEmail());
        tvEmail.setVisibility(userModel.getMobile().isEmpty() ? View.GONE : View.VISIBLE);
        tvPhone.setVisibility(userModel.getEmail().isEmpty() ? View.GONE : View.VISIBLE);
        TransitionManager.endTransitions(headerProfile);

        listViewPaginator.setLoadMoreEnabled(true);

    }

    @Override
    public void onLoadSuccess(List<PostModel> postModels) {
        if (listViewPaginator.getCurrentPage() == listViewPaginator.getFirstPage()) {
            swipeRefreshLayout.setRefreshing(false);
            listView.scrollTo(0, 0);
            postAdapter.clear();
        } else {
            listViewPaginator.setLoadMoreShow(false);
        }
        listViewPaginator.setCurrentPage(listViewPaginator.getCurrentPage() + 1);
        postAdapter.addItems(postModels);
        listViewPaginator.setLoadMoreEnabled(postModels.size() == 0);

    }

    @Override
    public void onShowLoading() {
        if (listViewPaginator.getCurrentPage() == listViewPaginator.getFirstPage()) {
            swipeRefreshLayout.setRefreshing(true);
        } else {
            listViewPaginator.setLoadMoreShow(true);
        }
    }

    @Override
    public void onShowLoadingDialog() {
        showProgress();
    }

    @Override
    public void onHideLoadingDialog() {
        hideProgress();
    }

    @Override
    public void onUpdatePost(PostModel postModel) {
        postAdapter.update(postModel);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PostsActivity.POST_DETAILS_ACTIVITY_REQUEST_CODE:
                    PostModel postModel = (PostModel) data.getSerializableExtra("post");
                    if (postModel != null)
                        postAdapter.update(postModel);
                    break;
            }
        }
    }

}
