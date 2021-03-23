package exchange.cell.cellexchange.post;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.transition.TransitionManager;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import exchange.cell.cellexchange.R;
import exchange.cell.cellexchange.user.UserManager;
import exchange.cell.cellexchange.user.UserModel;

/**
 * Created by Alexander on 09.10.17.
 */

public class PostCommentAdapter extends ArrayAdapter<PostCommentModel> {

    private SharedPreferences sharedPreferences;

    public PostCommentAdapter(@NonNull Context context) {
        super(context, 0);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        CommentViewHolder commentViewHolder;
        if (convertView == null) {
            commentViewHolder = new CommentViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_post_comment_layout, parent, false);
            commentViewHolder.messageLayout = convertView.findViewById(R.id.message_layout);
            commentViewHolder.ivAvatar = convertView.findViewById(R.id.iv_avatar);
            commentViewHolder.tvName = convertView.findViewById(R.id.tv_trading_name);
            commentViewHolder.tvMessage = convertView.findViewById(R.id.tv_message);
            commentViewHolder.tvDate = convertView.findViewById(R.id.tv_date);
            convertView.setTag(commentViewHolder);
        }
        commentViewHolder = (CommentViewHolder) convertView.getTag();

        PostCommentModel postCommentModel = getItem(position);
        if (postCommentModel != null) {
            UserModel userModel = postCommentModel.getUserModel();
            int currentUserId = UserManager.getCurrentUserId(sharedPreferences);
            boolean isCurrentUser = currentUserId == userModel.getId();

            TransitionManager.beginDelayedTransition((ViewGroup) convertView);
            RelativeLayout.LayoutParams messageLayoutLP = (RelativeLayout.LayoutParams) commentViewHolder.messageLayout.getLayoutParams();
            messageLayoutLP.setMarginStart(getContext().getResources().getDimensionPixelSize(isCurrentUser ?
                    R.dimen.output_comment_left_margin : R.dimen.input_comment_left_margin));
            messageLayoutLP.setMarginEnd(getContext().getResources().getDimensionPixelSize(isCurrentUser ?
                    R.dimen.output_comment_right_margin : R.dimen.input_comment_right_margin));
            if (isCurrentUser) {
                messageLayoutLP.addRule(RelativeLayout.ALIGN_PARENT_END);
                messageLayoutLP.removeRule(RelativeLayout.ALIGN_PARENT_START);
            } else {
                messageLayoutLP.addRule(RelativeLayout.ALIGN_PARENT_START);
                messageLayoutLP.removeRule(RelativeLayout.ALIGN_PARENT_END);
            }
            commentViewHolder.messageLayout.setLayoutParams(messageLayoutLP);

            commentViewHolder.ivAvatar.setImageURI(userModel.getPhoto());
            commentViewHolder.tvName.setText(userModel.getFullName());
            commentViewHolder.tvMessage.setText(postCommentModel.getMessage());
            commentViewHolder.tvDate.setText(DateFormat.format("HH:mm dd.MM.yyyy", postCommentModel.getCreatedAt()));
            TransitionManager.endTransitions((ViewGroup) convertView);
        }


        return convertView;
    }


    private class CommentViewHolder {
        ConstraintLayout messageLayout;
        SimpleDraweeView ivAvatar;
        TextView tvName;
        TextView tvMessage;
        TextView tvDate;
    }
}
