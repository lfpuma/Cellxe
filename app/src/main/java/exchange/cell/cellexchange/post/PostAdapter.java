package exchange.cell.cellexchange.post;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.transition.TransitionManager;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.format.DateFormat;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import exchange.cell.cellexchange.R;
import exchange.cell.cellexchange.user.UserModel;

/**
 * Created by Alexander on 01.10.17.
 */

public class PostAdapter extends BaseAdapter {

    private UserClickListener userClickListener;
    private LikeClickListener likeClickListener;
    private CommentClickListener commentClickListener;
    private ShareClickListener shareClickListener;
    private Context context;
    private List<PostModel> postModels = new ArrayList<>();

    public PostAdapter(@NonNull Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return postModels == null ? 0 : postModels.size();
    }

    @Override
    public PostModel getItem(int position) {
        return postModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public Context getContext() {
        return context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        PostViewHolder postViewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_post_layout, parent, false);
            postViewHolder = new PostViewHolder();
            postViewHolder.rootLayout = convertView.findViewById(R.id.root_layout);
            postViewHolder.ivPhoto = convertView.findViewById(R.id.iv_photo);
            postViewHolder.ivAvatar = convertView.findViewById(R.id.iv_avatar);
            postViewHolder.tvName = convertView.findViewById(R.id.tv_trading_name);
            postViewHolder.tvUserInfo = convertView.findViewById(R.id.tv_user_info);
            postViewHolder.tvProduct = convertView.findViewById(R.id.tv_product);
            postViewHolder.tvProductInfo = convertView.findViewById(R.id.tv_product_info);
            postViewHolder.tvProductDescription = convertView.findViewById(R.id.tv_product_description);
            postViewHolder.tvTime = convertView.findViewById(R.id.tv_time);
            postViewHolder.tvAction = convertView.findViewById(R.id.tv_action);
            postViewHolder.btnLike = convertView.findViewById(R.id.btn_like);
            postViewHolder.btnComment = convertView.findViewById(R.id.btn_comment);
            postViewHolder.btnShare = convertView.findViewById(R.id.btn_share);
            postViewHolder.profileClicker = convertView.findViewById(R.id.profile_clicker);
            convertView.setTag(postViewHolder);
        } else {
            postViewHolder = (PostViewHolder) convertView.getTag();
        }

        PostModel postModel = getItem(position);
        if (postModel != null) {
            UserModel userModel = postModel.getUser();

            TransitionManager.beginDelayedTransition(postViewHolder.rootLayout);
            ConstraintLayout.LayoutParams ivPhotoLP = (ConstraintLayout.LayoutParams) postViewHolder.ivPhoto.getLayoutParams();
            ivPhotoLP.height = postModel.getPhoto() == null || postModel.getPhoto().isEmpty()
                    ? 0 : getContext().getResources().getDimensionPixelSize(R.dimen.post_photo_height);
            postViewHolder.ivPhoto.setLayoutParams(ivPhotoLP);
            postViewHolder.ivPhoto.setImageURI(postModel.getPhoto());
            postViewHolder.ivAvatar.setImageURI(userModel.getPhoto());
            postViewHolder.tvName.setText(userModel.getFullName());
            postViewHolder.tvUserInfo.setText(userModel.getUserInfo());
            postViewHolder.tvProduct.setText(postModel.getAdTitle());
            postViewHolder.tvProductInfo.setText(postModel.getProductInfo());
            postViewHolder.tvProductDescription.setText(postModel.getDescription());
            postViewHolder.tvTime.setText(DateFormat.format("HH:mm dd.MM.yyyy", postModel.getCreatedAt()));
            postViewHolder.tvAction.setText(PostManager.interestedIn(getContext(), postModel));
            postViewHolder.tvAction.setTextColor(ContextCompat.getColor(getContext(), postModel.getInterestInId() == 1
                    ? R.color.colorOrange : R.color.colorPrimary));
            postViewHolder.profileClicker.setOnClickListener(v -> {
                if (userClickListener != null) userClickListener.onUserClick(userModel);
            });

            postViewHolder.btnLike.setTextColor(ContextCompat.getColor(getContext(),
                    postModel.isLiked() ? R.color.colorPrimary : R.color.colorBlack));
            Spannable btnLikeText = new SpannableString("  " + getContext().getString(R.string.like));
            btnLikeText.setSpan(new ImageSpan(getContext(), postModel.isLiked() ? R.drawable.ic_liked : R.drawable.ic_like,
                    ImageSpan.ALIGN_BOTTOM), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            postViewHolder.btnLike.setText(btnLikeText);

            Spannable btnCommentText = new SpannableString("  " + getContext().getString(R.string.comment));
            btnCommentText.setSpan(new ImageSpan(getContext(), R.drawable.ic_comment,
                    ImageSpan.ALIGN_BOTTOM), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            postViewHolder.btnComment.setText(btnCommentText);

            Spannable btnShareText = new SpannableString("  " + getContext().getString(R.string.share));
            btnShareText.setSpan(new ImageSpan(getContext(), R.drawable.ic_share,
                    ImageSpan.ALIGN_BOTTOM), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            postViewHolder.btnShare.setText(btnShareText);

            postViewHolder.btnLike.setOnClickListener(v -> {
                if (likeClickListener != null) likeClickListener.onLikeClick(postModel);
            });

            postViewHolder.btnComment.setOnClickListener(v -> {
                if (commentClickListener != null) commentClickListener.onCommentClickListener(postModel);
            });

            postViewHolder.btnShare.setOnClickListener(v -> {
                if (shareClickListener != null) shareClickListener.onShareClickListener(postModel);
            });

            TransitionManager.endTransitions(postViewHolder.rootLayout);

        }

        return convertView;
    }

    public void addItems(List<PostModel> postModels) {
        addItems(postModels, this.postModels.size());
    }

    public void addItems(List<PostModel> postModels, int position) {
        this.postModels.addAll(position, postModels);
        notifyDataSetChanged();
    }

    public void setItems(List<PostModel> postModels) {
        this.postModels = postModels;
        notifyDataSetChanged();
    }

    public void clear() {
        this.postModels.clear();
        notifyDataSetChanged();
    }

    public void add(PostModel postModel) {
        add(postModels.size(), postModel);
    }

    public void add(int index, PostModel postModel) {
        this.postModels.add(index, postModel);
        notifyDataSetChanged();
    }

    public void update(PostModel postModel) {
        update(postModel, indexOf(postModel));
    }

    public void update(PostModel postModel, int position) {
        if (position != -1) {
            this.postModels.set(position, postModel);
            notifyDataSetChanged();
        }
    }

    public void addOrUpdate(PostModel postModel, int positionForAdd) {
        int index = indexOf(postModel);
        if (index == -1) {
            this.postModels.add(positionForAdd, postModel);
        } else {
            update(postModel, index);
        }
    }

    public int indexOf(PostModel postModel) {
        for (int i = 0; i < postModels.size(); i++) {
            PostModel postInList = postModels.get(i);
            if (postInList != null && postModel.getId() == postInList.getId()) {
                return i;
            }
        }
        return -1;
    }

    public List<PostModel> getPostModels() {
        return postModels;
    }

    private class PostViewHolder {
        ConstraintLayout rootLayout;
        SimpleDraweeView ivPhoto;
        SimpleDraweeView ivAvatar;
        TextView tvName;
        TextView tvUserInfo;
        TextView tvProduct;
        TextView tvProductInfo;
        TextView tvProductDescription;
        TextView tvTime;
        TextView tvAction;
        Button btnLike;
        Button btnComment;
        Button btnShare;
        View profileClicker;
    }

    public interface UserClickListener {
        void onUserClick(UserModel userModel);
    }

    public interface LikeClickListener {
        void onLikeClick(PostModel postModel);
    }

    public interface CommentClickListener {
        void onCommentClickListener(PostModel postModel);
    }

    public interface ShareClickListener {
        void onShareClickListener(PostModel postModel);
    }

    public void setUserClickListener(UserClickListener userClickListener) {
        this.userClickListener = userClickListener;
    }

    public void setLikeClickListener(LikeClickListener likeClickListener) {
        this.likeClickListener = likeClickListener;
    }

    public void setCommentClickListener(CommentClickListener commentClickListener) {
        this.commentClickListener = commentClickListener;
    }

    public void setShareClickListener(ShareClickListener shareClickListener) {
        this.shareClickListener = shareClickListener;
    }
}
