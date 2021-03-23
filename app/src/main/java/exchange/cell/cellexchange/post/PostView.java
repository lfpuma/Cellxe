package exchange.cell.cellexchange.post;

import java.util.List;

/**
 * Created by Alexander on 09.10.17.
 */

public interface PostView {

    void onUpdatePost(PostModel postModel);
    void onShowLoadingDialog();
    void onHideLoadingDialog();
    void onNewComment(PostCommentModel postCommentModel);
    void onGetComments(List<PostCommentModel> postCommentModels);
    void onMessageNotFill();
    void onNewCommentSendFailed();
}
