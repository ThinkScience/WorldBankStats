
package com.sportchamps.worldbankstats.model.data.pojo.worldgdp;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WorldGdp {

    @SerializedName("countriesGdpData")
    @Expose
    private List<CountriesGdpData> countriesGdpData = null;

    public List<CountriesGdpData> getCountriesGdpData() {
        return countriesGdpData;
    }

    public void setCountriesGdpData(List<CountriesGdpData> countriesGdpData) {
        this.countriesGdpData = countriesGdpData;
    }

}
