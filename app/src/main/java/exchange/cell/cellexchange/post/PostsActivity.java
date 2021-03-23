package exchange.cell.cellexchange.post;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.transition.TransitionManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding2.widget.RxAdapterView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import exchange.cell.cellexchange.acitivty.BaseActivity;
import exchange.cell.cellexchange.R;
import exchange.cell.cellexchange.info.CountryModel;
import exchange.cell.cellexchange.profile.ProfileActivity;
import exchange.cell.cellexchange.utils.ListViewPaginator;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by Alexander on 01.10.17.
 */

public class PostsActivity extends BaseActivity implements PostsView {

    public static final int NEW_POST_ACTIVITY_REQUEST_CODE = 987;
    public static final int POST_DETAILS_ACTIVITY_REQUEST_CODE = 988;

    @BindView(R.id.coordinator)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.app_bar)
    AppBarLayout appBarLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.list_view)
    ListView listView;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.iv_clear_search)
    ImageView ivClearSearch;
    @BindView(R.id.fab_new_post)
    FloatingActionButton fabNewPost;
    @BindView(R.id.tv_empty)
    TextView tvEmpty;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.tv_error)
    TextView tvError;
    @BindView(R.id.iv_filter)
    ImageView ivFilter;
    @BindView(R.id.progress)
    ProgressBar progress;

    @BindDimen(R.dimen.clear_button_size)
    int clearButtonSize;
    @BindDimen(R.dimen.search_edit_text_default_end_padding)
    int defaultEndPadding;

    private PostAdapter postAdapter;

    @Inject
    PostsPresenter postsPresenter;

    private CountryModel selectedCountry = new CountryModel();

    private ListViewPaginator listViewPaginator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        postAdapter = new PostAdapter(this);
        listView.setAdapter(postAdapter);
        RxAdapterView.itemClickEvents(listView)
                .subscribe(adapterViewItemClickEvent -> {
                    int position = adapterViewItemClickEvent.position();
                    PostModel postModel = postAdapter.getItem(position);
                    Intent intent = new Intent(this, PostActivity.class);
                    intent.putExtra("post", postModel);
                    startActivityForResult(intent, POST_DETAILS_ACTIVITY_REQUEST_CODE);
                });

        RxTextView.afterTextChangeEvents(etSearch)
                .debounce(400, TimeUnit.MILLISECONDS)
                .map(textViewAfterTextChangeEvent -> textViewAfterTextChangeEvent.editable().toString())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(text -> {
                    listViewPaginator.setCurrentPage(listViewPaginator.getFirstPage());
                    listViewPaginator.setLoadMoreEnabled(true);
                    ivClearSearch.setVisibility(text.isEmpty() ? View.GONE : View.VISIBLE);
                    CardView.LayoutParams etSearchLayoutParams = (CardView.LayoutParams) etSearch.getLayoutParams();
                    etSearchLayoutParams.setMarginEnd(text.isEmpty() ? defaultEndPadding : clearButtonSize + defaultEndPadding);
                    loadNextPage();
                }, Throwable::printStackTrace);

        KeyboardVisibilityEvent.setEventListener(this, isOpen -> {
            int keyboardHeight = KeyboardVisibilityEvent.keyboardHeight(PostsActivity.this);
            TransitionManager.beginDelayedTransition(coordinatorLayout);
            fabNewPost.setVisibility(isOpen ? View.GONE : View.VISIBLE);
            CoordinatorLayout.LayoutParams tvEmptyLP = (CoordinatorLayout.LayoutParams) tvEmpty.getLayoutParams();
            tvEmptyLP.bottomMargin = keyboardHeight;
            tvEmpty.setLayoutParams(tvEmptyLP);
            CoordinatorLayout.LayoutParams tvErrorLP = (CoordinatorLayout.LayoutParams) tvError.getLayoutParams();
            tvEmptyLP.bottomMargin = keyboardHeight;
            tvError.setLayoutParams(tvErrorLP);
            TransitionManager.endTransitions(coordinatorLayout);
        });

        postAdapter.setUserClickListener(userModel -> {
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra("user_id", userModel.getId());
            startActivity(intent);
        });

        postAdapter.setLikeClickListener(postModel -> {
            postsPresenter.like(postModel.getId(), !postModel.isLiked());
        });

        postAdapter.setCommentClickListener(postModel -> {
            Intent intent = new Intent(this, PostActivity.class);
            intent.putExtra("post", postModel);
            intent.putExtra("comment", true);
            startActivityForResult(intent, POST_DETAILS_ACTIVITY_REQUEST_CODE);
        });

        postAdapter.setShareClickListener(postModel -> PostManager.sharePost(PostsActivity.this, postModel));

        listViewPaginator = ListViewPaginator.create(listView)
                .setLoadMoreView(LayoutInflater.from(this).inflate(R.layout.footer_loading, listView, false))
                .setListener(this::loadNextPage);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            listViewPaginator.setCurrentPage(listViewPaginator.getFirstPage());
            listViewPaginator.setLoadMoreEnabled(false);
            loadNextPage();
        });

        postsPresenter.listenNewPosts();


    }


    @Override
    public void onUpdatePost(PostModel postModel) {
        postAdapter.update(postModel);
    }

    @Override
    protected void onDestroy() {
        postsPresenter.detach();
        postsPresenter.stopListenNewPosts();
        super.onDestroy();
    }

    @OnClick(R.id.iv_profile)
    public void onProfileClick() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.iv_filter)
    public void onFilterClick() {
        postsPresenter.showFilter();
    }

    @OnClick(R.id.fab_new_post)
    public void onNewPostClick() {
        Intent intent = new Intent(this, NewPostActivity.class);
        startActivityForResult(intent, NEW_POST_ACTIVITY_REQUEST_CODE);
    }

    @OnClick(R.id.iv_clear_search)
    public void onClearSearchClick() {
        etSearch.setText("");
        UIUtil.hideKeyboard(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case NEW_POST_ACTIVITY_REQUEST_CODE:
                    PostModel newPost = (PostModel) data.getSerializableExtra("post");
                    if (newPost != null)
                        postAdapter.add(0, newPost);
                    break;
                case POST_DETAILS_ACTIVITY_REQUEST_CODE:
                    PostModel postModel = (PostModel) data.getSerializableExtra("post");
                    if (postModel != null)
                        postAdapter.update(postModel);
                    break;
            }
        }
    }

    @Override
    public void onNewPostReceived(PostModel postModel) {
        postAdapter.addOrUpdate(postModel, 0);
    }

    @Override
    public void onShowLoading() {
        if (listViewPaginator.getCurrentPage() > listViewPaginator.getFirstPage()) {
            listViewPaginator.setLoadMoreShow(true);
        } else {
            swipeRefreshLayout.setRefreshing(true);
        }
    }

    @Override
    public void onLoadSuccess(List<PostModel> postModels) {
        TransitionManager.beginDelayedTransition(coordinatorLayout);
        tvError.setVisibility(View.GONE);
        tvEmpty.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);
        progress.setVisibility(View.GONE);
        if (listViewPaginator.getCurrentPage() == listViewPaginator.getFirstPage()) {
            swipeRefreshLayout.setRefreshing(false);
            postAdapter.clear();
            listViewPaginator.setLoadMoreEnabled(true);
        } else {
            listViewPaginator.setLoadMoreShow(false);
        }
        postAdapter.addItems(postModels);
        TransitionManager.endTransitions(coordinatorLayout);
        listViewPaginator.setCurrentPage(listViewPaginator.getCurrentPage() + 1);
    }

    @Override
    public void onShowError() {
        if (listViewPaginator.getCurrentPage() == listViewPaginator.getFirstPage()) {
            swipeRefreshLayout.setRefreshing(false);
            postAdapter.clear();
            tvError.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.GONE);
            listView.setVisibility(View.GONE);
            progress.setVisibility(View.GONE);
        } else {
            Toast.makeText(this, R.string.message_error_loading, Toast.LENGTH_SHORT).show();
            listViewPaginator.setLoadMoreShow(false);
        }

    }

    @Override
    public void onShowEmpty() {
        if (listViewPaginator.getCurrentPage() == listViewPaginator.getFirstPage()) {
            swipeRefreshLayout.setRefreshing(false);
            postAdapter.clear();
            tvError.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            progress.setVisibility(View.GONE);
        } else {
            listViewPaginator.setLoadMoreShow(false);
        }
        listViewPaginator.setLoadMoreEnabled(false);

    }

    @Override
    public void onShowFilter(List<CountryModel> countryModels) {
        ArrayAdapter<CountryModel> countryModelArrayAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, countryModels);
        int position = countryModelArrayAdapter.getPosition(selectedCountry);
        new AlertDialog.Builder(this)
                .setSingleChoiceItems(countryModelArrayAdapter, position, (dialog, which) -> {
                    dialog.dismiss();
                    listViewPaginator.setCurrentPage(listViewPaginator.getFirstPage());
                    listViewPaginator.setLoadMoreEnabled(true);
                    selectedCountry = countryModelArrayAdapter.getItem(which);
                    swipeRefreshLayout.setRefreshing(true);
                    loadNextPage();
                    ivFilter.setImageResource(selectedCountry.getId() != -1 ? R.drawable.ic_filter_selected : R.drawable.ic_filter);
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    public void onShowLoadingDialog() {
        showProgress();
    }

    @Override
    public void onHideLoadingDialog() {
        hideProgress();
    }


    public synchronized void loadNextPage() {
        postsPresenter.getNextPage(listViewPaginator.getCurrentPage(),
                etSearch.getText().toString(),
                selectedCountry == null || selectedCountry.getId() == -1 ? null : String.valueOf(selectedCountry.getId()));
    }


}
