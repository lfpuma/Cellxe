package exchange.cell.cellexchange.info;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import exchange.cell.cellexchange.api.ApiModule;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Alexander on 06.10.17.
 */

public class CountryModel extends RealmObject implements Serializable {

    @PrimaryKey
    private int id = -1;
    private String title = "All cities";
    @SerializedName("iso_code_2")
    private String isoCode2;
    @SerializedName("iso_code_3")
    private String isoCode3;
    @SerializedName("dial_code")
    private String dialCode;
    @SerializedName("image")
    private String image;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsoCode2() {
        return isoCode2;
    }

    public void setIsoCode2(String isoCode2) {
        this.isoCode2 = isoCode2;
    }

    public String getIsoCode3() {
        return isoCode3;
    }

    public void setIsoCode3(String isoCode3) {
        this.isoCode3 = isoCode3;
    }

    public String getDialCode() {
        return dialCode;
    }

    public void setDialCode(String dialCode) {
        this.dialCode = dialCode;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return getTitle();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CountryModel that = (CountryModel) o;

        return getId() == that.getId();

    }

    @Override
    public int hashCode() {
        return getId();
    }

}
