package exchange.cell.cellexchange.post;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import javax.inject.Inject;

import exchange.cell.cellexchange.CellExchangeApplication;
import exchange.cell.cellexchange.firebase.FirebaseMessagingService;
import exchange.cell.cellexchange.info.CountryModel;
import exchange.cell.cellexchange.info.InfoManager;
import exchange.cell.cellexchange.presenter.BasePresenter;
import exchange.cell.cellexchange.scope.ActivityScope;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Alexander on 06.10.17.
 */
@ActivityScope
public class PostsPresenter extends BasePresenter<PostsView> {

    private InfoManager infoManager;
    private PostManager postManager;
    private CellExchangeApplication cellExchangeApplication;

    @Inject
    public PostsPresenter(PostsView postsView, InfoManager infoManager, PostManager postManager, CellExchangeApplication cellExchangeApplication) {
        super(postsView);
        this.infoManager = infoManager;
        this.postManager = postManager;
        this.cellExchangeApplication = cellExchangeApplication;
    }

    public void getNextPage(int nextPage) {
        getNextPage(nextPage, null, null);
    }

    public void getNextPage(int nextPage, String search) {
        getNextPage(nextPage, search, null);
    }

    public void getNextPage(int nextPage, String search, String countryId) {
        postManager.getPosts(search, countryId, null, nextPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(subscription -> {
                    if (isAttach()) getView().onShowLoading();
                })
                .subscribe(postModels -> {
                    if (postModels.size() > 0) {
                        if (isAttach()) getView().onLoadSuccess(postModels);
                    } else {
                        if (isAttach()) getView().onShowEmpty();
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                    if (isAttach()) getView().onShowError();
                });
    }


    public void showFilter() {
        infoManager.getCountries()
                .map(countryModels -> {
                    countryModels.add(0, new CountryModel());
                    return countryModels;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(countryModels -> {
                    if (isAttach()) getView().onShowFilter(countryModels);
                }, Throwable::printStackTrace);
    }

    public void listenNewPosts() {
        cellExchangeApplication.registerReceiver(newPostsReceiver, new IntentFilter(FirebaseMessagingService.NEW_POST_ACTION));
    }

    public void stopListenNewPosts() {
        cellExchangeApplication.unregisterReceiver(newPostsReceiver);
    }

    private BroadcastReceiver newPostsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            PostModel postModel = (PostModel) intent.getSerializableExtra("post");
            if (postModel != null) {
                if (isAttach()) getView().onNewPostReceived(postModel);
            }
        }
    };

    public void like(int postId, boolean action) {
        postManager.like(postId, action)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(subscription -> {
                    if (isAttach()) getView().onShowLoadingDialog();
                })
                .doOnTerminate(() -> {
                    if (isAttach()) getView().onHideLoadingDialog();
                })
                .subscribe(postModel -> {
                    if (isAttach()) getView().onUpdatePost(postModel);
                }, Throwable::printStackTrace);
    }



}
