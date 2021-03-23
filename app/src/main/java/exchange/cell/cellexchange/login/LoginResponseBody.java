package exchange.cell.cellexchange.login;

import com.google.gson.annotations.SerializedName;

import exchange.cell.cellexchange.user.UserModel;

/**
 * Created by Alexander on 06.10.17.
 */

public class LoginResponseBody {

    private String token;
    @SerializedName("user_id")
    private int userId;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
