package exchange.cell.cellexchange.post;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.transition.TransitionManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.format.DateFormat;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import exchange.cell.cellexchange.acitivty.BaseActivity;
import exchange.cell.cellexchange.R;
import exchange.cell.cellexchange.profile.ProfileActivity;
import exchange.cell.cellexchange.user.UserManager;
import exchange.cell.cellexchange.user.UserModel;

/**
 * Created by Alexander on 01.10.17.
 */

public class PostActivity extends BaseActivity implements PostView {

    private PostModel postModel;

    @BindView(R.id.coordinator)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.app_bar)
    AppBarLayout appBarLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.list_view)
    ListView listView;
    @BindView(R.id.bottom_bar)
    View bottomBar;
    @BindView(R.id.et_message)
    EditText etMessage;
    @BindView(R.id.iv_send)
    ImageView ivSend;

    private SimpleDraweeView ivPhoto;
    private SimpleDraweeView ivAvatar;
    private TextView tvName;
    private TextView tvUserInfo;
    private TextView tvProduct;
    private TextView tvProductInfo;
    private TextView tvProductDescription;
    private TextView tvTime;
    private TextView tvAction;
    private Button btnLike;
    private Button btnShare;
    private View profileClicker;

    private ConstraintLayout headerPost;
    private PostCommentAdapter postCommentAdapter;

    @Inject
    PostPresenter postPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        ButterKnife.bind(this);

        postModel = (PostModel) getIntent().getSerializableExtra("post");

        setSupportActionBar(toolbar);
        setTitle(R.string.details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        postCommentAdapter = new PostCommentAdapter(this);
        listView.setAdapter(postCommentAdapter);

        if (headerPost == null) {
            headerPost = (ConstraintLayout) LayoutInflater.from(this).inflate(R.layout.header_post, listView, false);
            ivPhoto = headerPost.findViewById(R.id.iv_photo);
            ivAvatar = headerPost.findViewById(R.id.iv_avatar);
            tvName = headerPost.findViewById(R.id.tv_trading_name);
            tvUserInfo = headerPost.findViewById(R.id.tv_user_info);
            tvProduct = headerPost.findViewById(R.id.tv_product);
            tvProductInfo = headerPost.findViewById(R.id.tv_product_info);
            tvProductDescription = headerPost.findViewById(R.id.tv_product_description);
            tvTime = headerPost.findViewById(R.id.tv_time);
            tvAction = headerPost.findViewById(R.id.tv_action);
            btnLike = headerPost.findViewById(R.id.btn_like);
            btnShare = headerPost.findViewById(R.id.btn_share);
            profileClicker = headerPost.findViewById(R.id.profile_clicker);
        }

        listView.addHeaderView(headerPost);

        invalidatePost();

        profileClicker.setOnClickListener(v -> {
            Intent intent = new Intent(PostActivity.this, ProfileActivity.class);
            intent.putExtra("user_id", postModel.getUser().getId());
            startActivity(intent);
        });

        btnLike.setOnClickListener(v -> {
            postPresenter.like(postModel.getId(), !postModel.isLiked());
        });

        btnShare.setOnClickListener(v -> PostManager.sharePost(PostActivity.this, postModel));

        postPresenter.getComments(postModel.getId());
        postPresenter.listenNewComments();

        if (getIntent().getBooleanExtra("comment", false)) {
            coordinatorLayout.postDelayed(() -> {
                etMessage.requestFocus();
                UIUtil.showKeyboard(PostActivity.this, etMessage);
            }, 500);
        }

        KeyboardVisibilityEvent.setEventListener(this, isOpen -> {
            if (isOpen) {
                listView.smoothScrollToPosition(postCommentAdapter.getCount());
            }
        });

    }

    @Override
    protected void onDestroy() {
        postPresenter.stopListenNewComments();
        postPresenter.detach();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onUpdatePost(PostModel postModel) {
        this.postModel = postModel;
        invalidatePost();
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
    public void onNewComment(PostCommentModel postCommentModel) {
        if (postCommentModel.getPostId() == postModel.getId()) {
            postCommentAdapter.add(postCommentModel);
            if (postCommentModel.getUserModel().getId() == UserManager.getCurrentUserId(this)) {
                etMessage.setText("");
            }
        }
    }

    void invalidatePost() {
        UserModel userModel = postModel.getUser();

        TransitionManager.beginDelayedTransition(headerPost);

        ConstraintLayout.LayoutParams ivPhotoLP = (ConstraintLayout.LayoutParams) ivPhoto.getLayoutParams();
        ivPhotoLP.height = postModel.getPhoto() == null || postModel.getPhoto().isEmpty() ? 0 : getResources().getDimensionPixelSize(R.dimen.post_photo_height);
        ivPhoto.setLayoutParams(ivPhotoLP);
        ivPhoto.setImageURI(postModel.getPhoto());
        ivAvatar.setImageURI(userModel.getPhoto());
        tvName.setText(userModel.getFullName());
        tvUserInfo.setText(userModel.getUserInfo());
        tvProduct.setText(postModel.getAdTitle());
        tvProductInfo.setText(postModel.getProductInfo());
        tvProductDescription.setText(postModel.getDescription());
        tvTime.setText(DateFormat.format("HH:mm dd.MM.yyyy", postModel.getCreatedAt()));
        tvAction.setText(postModel.getInterestInId() == 1 ? "WTS" : postModel.getInterestInId() == 2 ? "WTB" : "Service");
        tvAction.setTextColor(ContextCompat.getColor(this, postModel.getInterestInId() == 1
                ? R.color.colorOrange : R.color.colorPrimary));

        btnLike.setTextColor(ContextCompat.getColor(this,
                postModel.isLiked() ? R.color.colorPrimary : R.color.colorBlack));
        Spannable btnLikeText = new SpannableString("  " + getString(R.string.like));
        btnLikeText.setSpan(new ImageSpan(this, postModel.isLiked() ? R.drawable.ic_liked : R.drawable.ic_like,
                ImageSpan.ALIGN_BOTTOM), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        btnLike.setText(btnLikeText);

        Spannable btnShareText = new SpannableString("  " + getString(R.string.share));
        btnShareText.setSpan(new ImageSpan(this, R.drawable.ic_share,
                ImageSpan.ALIGN_BOTTOM), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        btnShare.setText(btnShareText);

        TransitionManager.endTransitions(headerPost);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("post", postModel);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

    @OnClick(R.id.iv_send)
    public void onSendClick() {
        UIUtil.hideKeyboard(this);
        postPresenter.sendMessage(postModel.getId(), etMessage.getText().toString());
    }

    @Override
    public void onGetComments(List<PostCommentModel> postCommentModels) {
        postCommentAdapter.clear();
        postCommentAdapter.addAll(postCommentModels);
    }

    @Override
    public void onMessageNotFill() {
        Toast.makeText(this, R.string.message_can_not_be_empty, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNewCommentSendFailed() {
        Toast.makeText(this, R.string.message_send_message_failed, Toast.LENGTH_SHORT).show();
    }
}
