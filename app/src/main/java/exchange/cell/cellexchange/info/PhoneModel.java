package exchange.cell.cellexchange.info;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Alexander on 09.10.17.
 */

public class PhoneModel extends RealmObject implements Serializable {

    @PrimaryKey
    private int id;
    @SerializedName("make_id")
    private int makeId;
    private String title;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    @Override
    public String toString() {
        return getTitle();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PhoneModel that = (PhoneModel) o;

        return getId() == that.getId();

    }

    @Override
    public int hashCode() {
        return getId();
    }
}
