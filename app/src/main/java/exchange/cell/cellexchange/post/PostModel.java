package exchange.cell.cellexchange.post;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

import exchange.cell.cellexchange.info.CategoryModel;
import exchange.cell.cellexchange.info.ConditionModel;
import exchange.cell.cellexchange.info.MakeModel;
import exchange.cell.cellexchange.info.SpecificationModel;
import exchange.cell.cellexchange.info.StockTypeModel;
import exchange.cell.cellexchange.user.UserModel;

/**
 * Created by Alexander on 01.10.17.
 */

public class PostModel implements Serializable {

    private int id;
    private String reference;
    private UserModel user;
    @SerializedName("ad_title")
    private String adTitle;
    @SerializedName("interested_in_id")
    private int interestInId;
    private CategoryModel category;
    private MakeModel make;
    @SerializedName("photoSrc")
    private String photo;
    @SerializedName("model_number")
    private String modelNumber;
    @SerializedName("stock_type")
    private StockTypeModel stockType;
    private String color;
    @SerializedName("storage_capacity")
    private String storageCapacity;
    @SerializedName("product_condition")
    private ConditionModel condition;
    private SpecificationModel specification;
    private int qty;
    private String description;
    private int status;
    @SerializedName("created_at")
    private Date createdAt;
    @SerializedName("is_liked")
    private boolean isLiked;

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public String getAdTitle() {
        return adTitle;
    }

    public void setAdTitle(String adTitle) {
        this.adTitle = adTitle;
    }

    public int getInterestInId() {
        return interestInId;
    }

    public void setInterestInId(int interestInId) {
        this.interestInId = interestInId;
    }

    public CategoryModel getCategory() {
        return category;
    }

    public void setCategory(CategoryModel category) {
        this.category = category;
    }

    public MakeModel getMake() {
        return make;
    }

    public void setMake(MakeModel make) {
        this.make = make;
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public StockTypeModel getStockType() {
        return stockType;
    }

    public void setStockType(StockTypeModel stockType) {
        this.stockType = stockType;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getStorageCapacity() {
        return storageCapacity;
    }

    public void setStorageCapacity(String storageCapacity) {
        this.storageCapacity = storageCapacity;
    }

    public ConditionModel getCondition() {
        return condition;
    }

    public void setCondition(ConditionModel condition) {
        this.condition = condition;
    }

    public SpecificationModel getSpecification() {
        return specification;
    }

    public void setSpecification(SpecificationModel specification) {
        this.specification = specification;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getProductInfo() {
        String productInfo = "";
        String modelNumber = getModelNumber();
        StockTypeModel stockTypeModel = getStockType();
        String color = getColor();
        String storageCapacity = getStorageCapacity();
        ConditionModel conditionModel = getCondition();
        SpecificationModel specificationModel = getSpecification();
        int qty = getQty();
        if (modelNumber != null && !modelNumber.isEmpty()) productInfo += modelNumber + " | ";
        if (stockTypeModel != null && !stockTypeModel.getTitle().isEmpty()) productInfo += stockTypeModel.getTitle() + " | ";
        if (color != null && !color.isEmpty()) productInfo += color + " | ";
        if (storageCapacity != null && !storageCapacity.isEmpty()) productInfo += storageCapacity + "GB | ";
        if (conditionModel != null && !conditionModel.getTitle().isEmpty()) productInfo += conditionModel.getTitle() + " | ";
        if (specificationModel != null && !specificationModel.getTitle().isEmpty()) productInfo += specificationModel.getTitle() + " | ";
        if (qty != 0) productInfo += qty + " Qty";
        return productInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PostModel postModel = (PostModel) o;

        return getId() == postModel.getId();

    }

    @Override
    public int hashCode() {
        return getId();
    }

    @Override
    public String toString() {
        return "PostModel{" +
                "id=" + id +
                ", reference='" + reference + '\'' +
                ", user=" + user +
                ", adTitle='" + adTitle + '\'' +
                ", interestInId=" + interestInId +
                ", category=" + category +
                ", make=" + make +
                ", photo='" + photo + '\'' +
                ", modelNumber='" + modelNumber + '\'' +
                ", stockType=" + stockType +
                ", color='" + color + '\'' +
                ", storageCapacity='" + storageCapacity + '\'' +
                ", condition=" + condition +
                ", specification=" + specification +
                ", qty=" + qty +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", isLiked=" + isLiked +
                '}';
    }
}
