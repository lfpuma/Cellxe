package exchange.cell.cellexchange.post;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.Collections;

import javax.inject.Inject;

import exchange.cell.cellexchange.CellExchangeApplication;
import exchange.cell.cellexchange.firebase.FirebaseMessagingService;
import exchange.cell.cellexchange.presenter.BasePresenter;
import exchange.cell.cellexchange.scope.ActivityScope;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Alexander on 09.10.17.
 */
@ActivityScope
public class PostPresenter extends BasePresenter<PostView> {

    protected PostManager postManager;
    protected CellExchangeApplication cellExchangeApplication;

    @Inject
    public PostPresenter(PostView postView, PostManager postManager, CellExchangeApplication cellExchangeApplication) {
        super(postView);
        this.postManager = postManager;
        this.cellExchangeApplication = cellExchangeApplication;
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
                }, Throwable::printStackTrace);
    }

    public void getComments(int postId) {
        postManager.getComments(postId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(postCommentModels -> {
                    if (isAttach()) getView().onGetComments(postCommentModels);
                }, Throwable::printStackTrace);
    }


    public void listenNewComments() {
        cellExchangeApplication.registerReceiver(newMessageBroadcastReceiver, new IntentFilter(FirebaseMessagingService.NEW_COMMENT_ACTION));
    }

    public void stopListenNewComments() {
        cellExchangeApplication.unregisterReceiver(newMessageBroadcastReceiver);
    }

    private BroadcastReceiver newMessageBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            PostCommentModel postCommentModel = (PostCommentModel) intent.getSerializableExtra("comment");
            if (postCommentModel != null) {
                if (isAttach()) getView().onNewComment(postCommentModel);
            }
        }
    };

    public void sendMessage(int postId, String message) {
        if (message.isEmpty()) {
            if (isAttach()) getView().onMessageNotFill();
            return;
        }
        postManager.sendComment(postId, message)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(subscription -> {
                    if (isAttach()) getView().onShowLoadingDialog();
                })
                .doOnTerminate(() -> {
                    if (isAttach()) getView().onHideLoadingDialog();
                })
                .subscribe(postCommentModel -> {
                    if (isAttach()) getView().onNewComment(postCommentModel);
                }, throwable -> {
                    throwable.printStackTrace();
                    if (isAttach()) getView().onNewCommentSendFailed();
                });
    }
}
