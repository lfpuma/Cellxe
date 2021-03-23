package exchange.cell.cellexchange.info;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Alexander on 06.10.17.
 */

public class RegionModel extends RealmObject implements Serializable {

    @PrimaryKey
    private int id;
    @SerializedName("country_id")
    private int countryId;
    private String title;
    private String code;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return getTitle();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RegionModel that = (RegionModel) o;

        return getId() == that.getId();

    }

    @Override
    public int hashCode() {
        return getId();
    }
}
