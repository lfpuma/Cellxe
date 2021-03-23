package exchange.cell.cellexchange.user;

import java.util.List;
import java.util.Optional;

import exchange.cell.cellexchange.login.LoginResponseBody;
import io.reactivex.Flowable;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Alexander on 06.10.17.
 */

public interface UserApiService {

    @FormUrlEncoded
    @POST("login")
    Flowable<LoginResponseBody> login(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("register")
    Flowable<UserModel> register(@Field("email") String email,
                                 @Field("password") String password, @Field("full_name") String fullName,
                                 @Field("country_id") int countryId,
                                 @Field("zone_id") int zoneId, @Field("mobile") String mobile,
                                 @Field("is_freezone") boolean isFreeZone, @Field("freezone_id") String freezoneId);

    @FormUrlEncoded
    @POST("forgotPassword")
    Flowable<ResponseBody> forgotPassword(@Field("email") String email);

    @FormUrlEncoded
    @POST("firebaseToken")
    Flowable<UserModel> sendFirebaseToken(@Field("firebase_token") String firebaseToken);

    @GET(".")
    Flowable<List<UserModel>> getUsers();

    @GET("{id}")
    Flowable<UserModel> getUser(@Path("id") int id);
}
