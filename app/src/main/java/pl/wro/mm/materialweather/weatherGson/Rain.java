
package pl.wro.mm.materialweather.weatherGson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Rain {

    @SerializedName("1h")
    @Expose
    private Double _1h;

    /**
     * 
     * @return
     *     The _1h
     */
    public Double get1h() {
        return _1h;
    }

    /**
     * 
     * @param _1h
     *     The 1h
     */
    public void set1h(Double _1h) {
        this._1h = _1h;
    }

}
