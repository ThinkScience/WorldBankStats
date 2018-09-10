
package com.sportchamps.worldbankstats.model.data.pojo.worlddata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Country {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("iso2Code")
    @Expose
    private String iso2Code;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("region")
    @Expose
    private Region region;
    @SerializedName("adminregion")
    @Expose
    private Adminregion adminregion;
    @SerializedName("incomeLevel")
    @Expose
    private IncomeLevel incomeLevel;
    @SerializedName("lendingType")
    @Expose
    private LendingType lendingType;
    @SerializedName("capitalCity")
    @Expose
    private String capitalCity;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("latitude")
    @Expose
    private String latitude;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIso2Code() {
        return iso2Code;
    }

    public void setIso2Code(String iso2Code) {
        this.iso2Code = iso2Code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public Adminregion getAdminregion() {
        return adminregion;
    }

    public void setAdminregion(Adminregion adminregion) {
        this.adminregion = adminregion;
    }

    public IncomeLevel getIncomeLevel() {
        return incomeLevel;
    }

    public void setIncomeLevel(IncomeLevel incomeLevel) {
        this.incomeLevel = incomeLevel;
    }

    public LendingType getLendingType() {
        return lendingType;
    }

    public void setLendingType(LendingType lendingType) {
        this.lendingType = lendingType;
    }

    public String getCapitalCity() {
        return capitalCity;
    }

    public void setCapitalCity(String capitalCity) {
        this.capitalCity = capitalCity;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

}
