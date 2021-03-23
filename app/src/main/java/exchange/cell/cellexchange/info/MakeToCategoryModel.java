package exchange.cell.cellexchange.info;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Alexander on 09.10.17.
 */

public class MakeToCategoryModel extends RealmObject implements Serializable {

    @PrimaryKey
    private int id;
    @SerializedName("make_id")
    private int makeId;
    @SerializedName("category_id")
    private int categoryId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMakeId() {
        return makeId;
    }

    public void setMakeId(int makeId) {
        this.makeId = makeId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public String toString() {
        return "MakeToCategoryModel{" +
                "id=" + id +
                ", makeId=" + makeId +
                ", categoryId=" + categoryId +
                '}';
    }
}
