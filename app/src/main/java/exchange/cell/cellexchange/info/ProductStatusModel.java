package exchange.cell.cellexchange.info;

import java.io.Serializable;

/**
 * Created by Alexander on 06.10.17.
 */

public class ProductStatusModel implements Serializable {

    private int id;
    private String title;

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
}
