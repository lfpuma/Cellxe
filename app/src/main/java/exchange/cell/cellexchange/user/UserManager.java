package exchange.cell.cellexchange.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import exchange.cell.cellexchange.CellExchangeApplication;
import exchange.cell.cellexchange.login.LoginResponseBody;
import io.reactivex.Flowable;
import okhttp3.ResponseBody;

/**
 * Created by Alexander on 04.10.17.
 */
@Singleton
public class UserManager {

    public static final String TOKEN_KEY = "token";
    public static final String ID_KEY = "id";

    private CellExchangeApplication cellExchangeApplication;
    private UserApiService userApiService;
    private SharedPreferences sharedPreferences;

    @Inject
    public UserManager(CellExchangeApplication cellExchangeApplication, UserApiService userApiService) {
        this.cellExchangeApplication = cellExchangeApplication;
        this.userApiService = userApiService;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(cellExchangeApplication);
    }

    public static String getToken(SharedPreferences sharedPreferences) {
        return sharedPreferences.getString(TOKEN_KEY, null);
    }

    public String getToken() {
        return getToken(sharedPreferences);
    }

    public void setToken(String token) {
        sharedPreferences.edit().putString(TOKEN_KEY, token).apply();
    }

    public boolean isLoggedIn() {
        return getToken() != null;
    }

    public static int getCurrentUserId(Context context) {
        return getCurrentUserId(PreferenceManager.getDefaultSharedPreferences(context));
    }

    public static int getCurrentUserId(SharedPreferences sharedPreferences) {
        return sharedPreferences.getInt(ID_KEY, 0);
    }

    public int getCurrentUserId() {
        return getCurrentUserId(sharedPreferences);
    }

    public void setCurrentUserId(int id) {
        sharedPreferences.edit().putInt(ID_KEY, id).apply();
    }

    public Flowable<LoginResponseBody> login(String email, String password) {
        return userApiService.login(email, password);
    }

    public Flowable<UserModel> register(String email, String password, String fullName, int countryId,
                                        int zoneId, String mobile, boolean isFreezone, String freezoneId) {
        return userApiService.register(email, password, fullName, countryId, zoneId, mobile, isFreezone, freezoneId);
    }

    public Flowable<ResponseBody> forgotPassword(String email) {
        return userApiService.forgotPassword(email);
    }


    public Flowable<UserModel> sendFirebaseToken(String firebaseToken) {
        return userApiService.sendFirebaseToken(firebaseToken);
    }

    public Flowable<List<UserModel>> getUsers() {
        return userApiService.getUsers();
    }

    public Flowable<UserModel> getUser(int id) {
        return userApiService.getUser(id);
    }

    public Flowable<UserModel> getCurrentUser() {
        return userApiService.getUser(getCurrentUserId());
    }

    public void logout() {
        sharedPreferences.edit()
                .remove(TOKEN_KEY)
                .remove(ID_KEY)
                .apply();
    }

}
