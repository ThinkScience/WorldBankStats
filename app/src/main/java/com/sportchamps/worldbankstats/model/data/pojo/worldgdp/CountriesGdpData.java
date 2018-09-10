
package com.sportchamps.worldbankstats.model.data.pojo.worldgdp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CountriesGdpData {

    @SerializedName("indicator")
    @Expose
    private Indicator indicator;
    @SerializedName("country")
    @Expose
    private Country country;
    @SerializedName("countryiso3code")
    @Expose
    private String countryiso3code;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("value")
    @Expose
    private Double value;
    @SerializedName("unit")
    @Expose
    private String unit;
    @SerializedName("obs_status")
    @Expose
    private String obsStatus;
    @SerializedName("decimal")
    @Expose
    private Integer decimal;

    public Indicator getIndicator() {
        return indicator;
    }

    public void setIndicator(Indicator indicator) {
        this.indicator = indicator;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getCountryiso3code() {
        return countryiso3code;
    }

    public void setCountryiso3code(String countryiso3code) {
        this.countryiso3code = countryiso3code;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getObsStatus() {
        return obsStatus;
    }

    public void setObsStatus(String obsStatus) {
        this.obsStatus = obsStatus;
    }

    public Integer getDecimal() {
        return decimal;
    }

    public void setDecimal(Integer decimal) {
        this.decimal = decimal;
    }

}
