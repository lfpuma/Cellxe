package exchange.cell.cellexchange.firebase;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.util.Pair;

import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import exchange.cell.cellexchange.R;
import exchange.cell.cellexchange.post.PostActivity;
import exchange.cell.cellexchange.post.PostCommentModel;
import exchange.cell.cellexchange.post.PostManager;
import exchange.cell.cellexchange.post.PostModel;
import exchange.cell.cellexchange.post.PostsActivity;
import exchange.cell.cellexchange.user.UserManager;
import exchange.cell.cellexchange.user.UserModel;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;

/**
 * Created by Alexander on 05.07.17.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    public static final String CHANNEL_NAME = "Cell.Exchange";
    public static final String ACTION = "action";
    public static final String LIKE_ACTION = "like";
    public static final String NEW_POST_ACTION = "new_post";
    public static final String EDIT_POST_ACTION = "edit_post";
    public static final String NEW_COMMENT_ACTION = "new_comment";

    @Inject
    PostManager postManager;
    @Inject
    UserManager userManager;


    private NotificationManagerCompat notificationManager;

    @Override
    public void onCreate() {
        AndroidInjection.inject(this);
        super.onCreate();
        notificationManager = NotificationManagerCompat.from(this);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Map<String, String> data = remoteMessage.getData();

        String action = data.get(ACTION);
        if (Objects.equals(action, LIKE_ACTION)) {
            String type = data.get("type");
            String postId = data.get("post_id");
            String likedById = data.get("liked_by_id");

            Flowable.combineLatest(postManager.getPost(Integer.parseInt(postId)),
                    userManager.getUser(Integer.parseInt(likedById)), Pair::create)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(pair -> {

                        int id = (int) System.currentTimeMillis();

                        Intent notificationIntent = new Intent(this, PostActivity.class);
                        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        notificationIntent.putExtra("post", pair.first);
                        PendingIntent pendingIntent = PendingIntent.getActivity(this, id, notificationIntent, 0);

                        Notification notification = new NotificationCompat.Builder(this, CHANNEL_NAME)
                                .setContentIntent(pendingIntent)
                                .setContentTitle(getString(R.string.message_like_notification_title, type))
                                .setContentText(getString(R.string.message_like_notification_message, pair.second.getFullName(), type, pair.first.getAdTitle()))
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .build();

                        notificationManager.notify(id, notification);

                    }, Throwable::printStackTrace);



        } else if (Objects.equals(action, NEW_POST_ACTION)) {
            String newPostId = data.get("post_id");

            postManager.getPost(Integer.parseInt(newPostId))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(postModel -> {

                        int id = (int) System.currentTimeMillis();


                        Intent notificationIntent = new Intent(this, PostActivity.class);
                        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        notificationIntent.putExtra("post", postModel);
                        PendingIntent pendingIntent = PendingIntent.getActivity(this, id, notificationIntent, 0);

                        Notification notification = new NotificationCompat.Builder(this, CHANNEL_NAME)
                                .setContentIntent(pendingIntent)
                                .setContentTitle(getString(R.string.message_post_notification_title))
                                .setContentText(getString(R.string.message_post_notification_message, postModel.getUser().getFullName(), postModel.getAdTitle()))
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .build();

                        notificationManager.notify(id, notification);

                        Intent broadcastIntent = new Intent(NEW_POST_ACTION);
                        broadcastIntent.putExtra("post", postModel);
                        sendBroadcast(broadcastIntent);

                    }, Throwable::printStackTrace);

        } else if (Objects.equals(action, NEW_COMMENT_ACTION)) {
            String commentId = data.get("comment_id");

            postManager.getComment(Integer.parseInt(commentId))
                    .flatMap(comment -> postManager.getPost(comment.getPostId()),
                            Pair::create)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(pair -> {

                        if (pair.second.getUser().getId() == userManager.getCurrentUserId()) {
                            int id = (int) System.currentTimeMillis();
                            Intent notificationIntent = new Intent(this, PostActivity.class);
                            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            notificationIntent.putExtra("comment", pair.first);
                            notificationIntent.putExtra("post", pair.second);
                            PendingIntent pendingIntent = PendingIntent.getActivity(this, id, notificationIntent, 0);

                            Notification notification = new NotificationCompat.Builder(this, CHANNEL_NAME)
                                    .setContentIntent(pendingIntent)
                                    .setContentTitle(getString(R.string.message_comment_notification_title))
                                    .setContentText(getString(R.string.message_comment_notification_message,
                                            pair.first.getUserModel().getFullName(), pair.second.getAdTitle()))
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .build();

                            notificationManager.notify(id, notification);
                        }

                        Intent broadcastIntent = new Intent(NEW_COMMENT_ACTION);
                        broadcastIntent.putExtra("comment", pair.first);
                        broadcastIntent.putExtra("post", pair.second);
                        sendBroadcast(broadcastIntent);

                    }, Throwable::printStackTrace);


        }



    }
}
