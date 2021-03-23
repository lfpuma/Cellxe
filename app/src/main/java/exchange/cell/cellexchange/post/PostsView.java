package exchange.cell.cellexchange.post;

import java.util.List;

import exchange.cell.cellexchange.info.CountryModel;

/**
 * Created by Alexander on 06.10.17.
 */

public interface PostsView{

    void onLoadSuccess(List<PostModel> postModels);
    void onShowError();
    void onShowEmpty();
    void onShowFilter(List<CountryModel> countryModels);
    void onShowLoading();
    void onNewPostReceived(PostModel postModel);
    void onUpdatePost(PostModel postModel);
    void onShowLoadingDialog();
    void onHideLoadingDialog();

}
