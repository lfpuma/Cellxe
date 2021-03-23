package exchange.cell.cellexchange.api;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import exchange.cell.cellexchange.CellExchangeApplication;
import exchange.cell.cellexchange.login.LoginActivity;
import exchange.cell.cellexchange.user.UserManager;
import okhttp3.Authenticator;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Alexander on 06.10.17.
 */
@Module
public class ApiModule {

    public static final String SITE_URL = "http://cell.exchange/";
    public static final String BASE_URL = SITE_URL + "api/v1/";

    public static final int TIMEOUT = 10000;

    @Provides
    @Singleton
    public OkHttpClient okHttpClient(CellExchangeApplication cellExchangeApplication) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .addInterceptor(logging)
                .addInterceptor(chain -> {
                    Request request = chain.request();
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(cellExchangeApplication);
                    String token = sharedPreferences.getString(UserManager.TOKEN_KEY, null);
                    if (token != null) {
                        request = request.newBuilder()
                                .addHeader("Token", "Bearer " + token)
                                .build();
                    }
                    Response response = chain.proceed(request);

                    if (token != null && response.code() == 401) {
                        sharedPreferences.edit()
                                .remove(UserManager.TOKEN_KEY)
                                .remove(UserManager.ID_KEY)
                                .apply();
                        Intent intent = new Intent(cellExchangeApplication, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_NEW_TASK);
                        cellExchangeApplication.startActivity(intent);
                        return null;
                    }

                    return response;
                })
                .build();
    }

    @Provides
    @Singleton
    public Retrofit.Builder retrofitBuilder(OkHttpClient okHttpClient, Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson));
    }

    @Provides
    @Singleton
    public Gson gson() {
        return new GsonBuilder()
                .setDateFormat("yyyy-MM-dd hh:mm:ss")
                .create();
    }

}
