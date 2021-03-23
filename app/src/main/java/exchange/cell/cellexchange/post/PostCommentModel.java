package exchange.cell.cellexchange.post;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

import butterknife.BindView;
import exchange.cell.cellexchange.user.UserModel;

/**
 * Created by Alexander on 09.10.17.
 */

public class PostCommentModel implements Serializable {

    private int id;
    private String message;
    @SerializedName("created_at")
    private Date createdAt;
    @SerializedName("user")
    private UserModel userModel;
    @SerializedName("post_id")
    private int postId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PostCommentModel that = (PostCommentModel) o;

        return getId() == that.getId();

    }

    @Override
    public int hashCode() {
        return getId();
    }

    @Override
    public String toString() {
        return getMessage();
    }
}
