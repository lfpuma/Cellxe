package exchange.cell.cellexchange.profile;

import android.util.Log;
import android.util.Pair;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import exchange.cell.cellexchange.post.PostManager;
import exchange.cell.cellexchange.post.PostModel;
import exchange.cell.cellexchange.presenter.BasePresenter;
import exchange.cell.cellexchange.user.UserManager;
import exchange.cell.cellexchange.user.UserModel;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.internal.operators.completable.CompletableToFlowable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Alexander on 06.10.17.
 */

public class ProfilePresenter extends BasePresenter<ProfileView> {

    private UserManager userManager;
    private PostManager postManager;

    @Inject
    public ProfilePresenter(ProfileView profileView, UserManager userManager, PostManager postManager) {
        super(profileView);
        this.userManager = userManager;
        this.postManager = postManager;
        Log.e("@@@", String.valueOf(profileView.toString()));
    }

    public void getUser(int id) {
        userManager.getUser(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userModel -> {
                    if (isAttach()) getView().onGetUser(userModel);
                }, throwable -> {
                    throwable.printStackTrace();
                });
    }

    public void getUserAndPosts(int id) {
        Flowable.combineLatest(
                userManager.getUser(id),
                postManager.getPosts(null, null, String.valueOf(id), 1),
                (BiFunction<UserModel, List<PostModel>, Pair>) Pair::create)
                .delay(2000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(subscription -> {
                    if (isAttach()) getView().onShowLoading();
                })
                .subscribe(pair -> {
                    if (isAttach()) getView().onLoadSuccess((List<PostModel>) pair.second);
                    if (isAttach()) getView().onGetUser((UserModel) pair.first);
                }, throwable -> {
                    throwable.printStackTrace();
                });
    }

    public void getNextPostPage(int userId, int page) {
        if (userId == -1) userId = userManager.getCurrentUserId();
        postManager.getPosts(null, null, String.valueOf(userId), page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(subscription -> {
                    if (isAttach()) getView().onShowLoading();
                })
                .subscribe(postModels -> {
                    if (isAttach()) getView().onLoadSuccess(postModels);
                }, throwable -> {
                    throwable.printStackTrace();
//                    if (nextPage == 1 && isAttach()) getView().onShowError();
                });
    }

    public void logout() {
        userManager.logout();
    }

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
                }, throwable -> {
                    throwable.printStackTrace();
                });
    }
}
