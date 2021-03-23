package exchange.cell.cellexchange.profile;

import java.util.List;

import exchange.cell.cellexchange.post.PostModel;
import exchange.cell.cellexchange.user.UserModel;

/**
 * Created by Alexander on 06.10.17.
 */

public interface ProfileView {

    void onGetUser(UserModel userModel);
    void onLoadSuccess(List<PostModel> postModels);
    void onShowLoadingDialog();
    void onHideLoadingDialog();
    void onUpdatePost(PostModel postModel);

    void onShowLoading();

}
