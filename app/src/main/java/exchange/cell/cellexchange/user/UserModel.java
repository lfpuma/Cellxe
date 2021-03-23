package exchange.cell.cellexchange.user;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import exchange.cell.cellexchange.info.ConditionModel;
import exchange.cell.cellexchange.info.CountryModel;
import exchange.cell.cellexchange.info.FreeZoneModel;
import exchange.cell.cellexchange.info.RegionModel;
import exchange.cell.cellexchange.info.TraderType;
import exchange.cell.cellexchange.info.UserGroupModel;
import exchange.cell.cellexchange.info.UserPackageModel;

/**
 * Created by Alexander on 06.10.17.
 */

public class UserModel implements Serializable {

    private int id;
    @SerializedName("full_name")
    private String fullName;
    private String email;
    @SerializedName("registered_trading_name")
    private String registeredTradingName;
    @SerializedName("trading_license_number")
    private String tradingLicenseNumber;
    @SerializedName("trading_license_valid_from")
    private String tradingLicenseValidFrom;
    @SerializedName("trading_license_valid_till")
    private String tradingLicenseValidTill;
    @SerializedName("business_profile")
    private String businessProfile;
    @SerializedName("trader_type")
    private TraderType traderType;
    private CountryModel country;
    private RegionModel region;
    private FreeZoneModel freeZone;
    private String mobile;
    @SerializedName("office_phone")
    private String officePhone;
    @SerializedName("office_mobile")
    private String officeMobile;
    @SerializedName("user_group")
    private UserGroupModel userGroup;
    private String photo;
    private String cover;
    @SerializedName("trading_license")
    private String tradingLicense;
    private String bill;
    @SerializedName("package")
    private UserPackageModel packageModel;

    public String getBusinessProfile() {
        return businessProfile;
    }

    public void setBusinessProfile(String businessProfile) {
        this.businessProfile = businessProfile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegisteredTradingName() {
        return registeredTradingName;
    }

    public void setRegisteredTradingName(String registeredTradingName) {
        this.registeredTradingName = registeredTradingName;
    }

    public String getTradingLicenseNumber() {
        return tradingLicenseNumber;
    }

    public void setTradingLicenseNumber(String tradingLicenseNumber) {
        this.tradingLicenseNumber = tradingLicenseNumber;
    }

    public String getTradingLicenseValidFrom() {
        return tradingLicenseValidFrom;
    }

    public void setTradingLicenseValidFrom(String tradingLicenseValidFrom) {
        this.tradingLicenseValidFrom = tradingLicenseValidFrom;
    }

    public String getTradingLicenseValidTill() {
        return tradingLicenseValidTill;
    }

    public void setTradingLicenseValidTill(String tradingLicenseValidTill) {
        this.tradingLicenseValidTill = tradingLicenseValidTill;
    }

    public TraderType getTraderType() {
        return traderType;
    }

    public void setTraderType(TraderType traderType) {
        this.traderType = traderType;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CountryModel getCountry() {
        return country;
    }

    public void setCountry(CountryModel country) {
        this.country = country;
    }

    public RegionModel getRegion() {
        return region;
    }

    public void setRegion(RegionModel region) {
        this.region = region;
    }

    public FreeZoneModel getFreeZone() {
        return freeZone;
    }

    public void setFreeZone(FreeZoneModel freeZone) {
        this.freeZone = freeZone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getOfficePhone() {
        return officePhone;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }

    public String getOfficeMobile() {
        return officeMobile;
    }

    public void setOfficeMobile(String officeMobile) {
        this.officeMobile = officeMobile;
    }

    public UserGroupModel getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(UserGroupModel userGroup) {
        this.userGroup = userGroup;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getTradingLicense() {
        return tradingLicense;
    }

    public void setTradingLicense(String tradingLicense) {
        this.tradingLicense = tradingLicense;
    }

    public String getBill() {
        return bill;
    }

    public void setBill(String bill) {
        this.bill = bill;
    }

    public UserPackageModel getPackageModel() {
        return packageModel;
    }

    public void setPackageModel(UserPackageModel packageModel) {
        this.packageModel = packageModel;
    }

    public String getUserInfo() {
        TraderType traderType = getTraderType();
        CountryModel countryModel = getCountry();
        String userInfoText = "";
        if (traderType != null) userInfoText += traderType.getTitle() + " | ";
        if (countryModel != null) userInfoText += countryModel.getTitle();
        return userInfoText;
    }
}
