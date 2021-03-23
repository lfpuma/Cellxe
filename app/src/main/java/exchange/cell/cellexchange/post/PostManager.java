package exchange.cell.cellexchange.post;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import exchange.cell.cellexchange.CellExchangeApplication;
import exchange.cell.cellexchange.R;
import exchange.cell.cellexchange.user.UserModel;
import io.reactivex.Flowable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Alexander on 01.10.17.
 */
@Singleton
public class PostManager {

    public static final int POSTS_PAGE_SIZE = 25;

    private CellExchangeApplication cellExchangeApplication;
    private PostApiService postApiService;

    @Inject
    public PostManager(CellExchangeApplication cellExchangeApplication, PostApiService postApiService) {
        this.cellExchangeApplication = cellExchangeApplication;
        this.postApiService = postApiService;
    }
    public Flowable<List<PostModel>> getPosts(int page) {
        return getPosts(null, null, null, page);
    }

    public Flowable<List<PostModel>> getPosts(String keyword, String countryId, String userId, int page) {
        return getPosts(keyword, countryId, userId, page, POSTS_PAGE_SIZE);
    }

    public Flowable<List<PostModel>> getPosts(String keyword, String countryId, String userId, int page, int pageSize) {
        return postApiService.getPosts(keyword, countryId, userId, page, pageSize);
    }

    public Flowable<PostModel> getPost(int id) {
        return postApiService.getPost(id);
    }

    public Flowable<PostModel> like(int postId, boolean action) {
        return postApiService.like(postId, action);
    }

    public static void sharePost(Activity activity, PostModel postModel) {
        String shareText = "Post by " + postModel.getUser().getFullName() + "\n";
        shareText += postModel.getProductInfo() + "\n";
        shareText += "Join Cell.Exchange for more details.\n";
        shareText += "http://cell.exchange/product/detail/" + postModel.getId();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, shareText);
        activity.startActivity(Intent.createChooser(intent, "Share post"));
    }

    public static String interestedIn(Context context, PostModel postModel) {
        return postModel.getInterestInId() == 1 ? context.getString(R.string.wts) : postModel.getInterestInId() == 2
                ? context.getString(R.string.wtb) : context.getString(R.string.service);
    }

    public Flowable<PostModel> create(int interestedInId, int productCategoryId, int makeId, int modelId, String modelNumber,
                                      int stockTypeId, String color, String storageCapacity, int productConditionId,
                                      int specificationId, int qty, String description, Uri imageUri) {

        Map<String, RequestBody> requestBodyMap = new HashMap<>();
        requestBodyMap.put("interested_in_id", RequestBody.create(okhttp3.MultipartBody.FORM, String.valueOf(interestedInId)));
        requestBodyMap.put("product_category_id", RequestBody.create(okhttp3.MultipartBody.FORM, String.valueOf(productCategoryId)));
        requestBodyMap.put("make_id", RequestBody.create(okhttp3.MultipartBody.FORM, String.valueOf(makeId)));
        requestBodyMap.put("model_id", RequestBody.create(okhttp3.MultipartBody.FORM, String.valueOf(modelId)));
        requestBodyMap.put("model_number", RequestBody.create(okhttp3.MultipartBody.FORM, modelNumber));
        requestBodyMap.put("stock_type_id", RequestBody.create(okhttp3.MultipartBody.FORM, String.valueOf(stockTypeId)));
        requestBodyMap.put("color", RequestBody.create(okhttp3.MultipartBody.FORM, color));
        requestBodyMap.put("storage_capacity", RequestBody.create(okhttp3.MultipartBody.FORM, storageCapacity));
        requestBodyMap.put("product_condition_id", RequestBody.create(okhttp3.MultipartBody.FORM, String.valueOf(productConditionId)));
        requestBodyMap.put("specification_id", RequestBody.create(okhttp3.MultipartBody.FORM, String.valueOf(specificationId)));
        requestBodyMap.put("qty", RequestBody.create(okhttp3.MultipartBody.FORM, String.valueOf(qty)));
        requestBodyMap.put("description", RequestBody.create(okhttp3.MultipartBody.FORM, description));

        MultipartBody.Part imagePart = null;
        if (imageUri != null) {
            File imageFile = new File(imageUri.getPath());
            RequestBody requestImage =
                    RequestBody.create(
                            MediaType.parse("image/*"),
                            imageFile
                    );
            imagePart = MultipartBody.Part.createFormData("photo", imageFile.getName(), requestImage);
        }

        return postApiService.create(requestBodyMap, imagePart);
    }

    public Flowable<List<PostCommentModel>> getComments(int postId) {
        return postApiService.getComments(postId);
    }

    public Flowable<PostCommentModel> getComment(int id) {
        return postApiService.getComment(id);
    }

    public Flowable<PostCommentModel> sendComment(int postId, String message) {
        return postApiService.sendComment(postId, message);
    }

}
