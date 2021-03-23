package exchange.cell.cellexchange.post;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Alexander on 06.10.17.
 */

public interface PostApiService {

    @GET(".")
    Flowable<List<PostModel>> getPosts(@Query("keyword") String keyword, @Query("country_id") String countryId,
                                       @Query("user_id") String userId,
                                       @Query("page") int page, @Query("per-page") int perPage);

    @GET("{id}")
    Flowable<PostModel> getPost(@Path("id") int id);

    @FormUrlEncoded
    @POST("like/")
    Flowable<PostModel> like(@Field("post_id") int postId, @Field("action") boolean action);

//    @Multipart
//    @POST("create/")
//    Flowable<PostModel> create(@Field("interested_in_id") int interestedInId, @Field("product_category_id") int productCategoryId,
//                               @Field("make_id") int makeId, @Field("model_id") int model_id, @Field("model_number") String modelNumber,
//                               @Field("stock_type_id") int stockTypeId, @Field("color") String color,
//                               @Field("storage_capacity") String storageCapacity, @Field("product_condition_id") int productConditionId,
//                               @Field("specification_id") int specificationId, @Field("qty") int qty, @Field("descp") String description,
//                               @Field("photo") String photo);

    @Multipart
    @POST("create/")
    Flowable<PostModel> create(
            @PartMap() Map<String, RequestBody> map,
            @Part MultipartBody.Part image
            );

    @GET("comments")
    Flowable<List<PostCommentModel>> getComments(@Query("post_id") int postId);

    @GET("comment")
    Flowable<PostCommentModel> getComment(@Query("id") int id);

    @FormUrlEncoded
    @POST("sendComment")
    Flowable<PostCommentModel> sendComment(@Field("post_id") int postId, @Field("message") String message);

}
