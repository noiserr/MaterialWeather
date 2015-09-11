
package pl.wro.mm.materialweather.network.data.s.photo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MapLocation {

    @Expose
    private Double lat;
    @Expose
    private Double lon;
    @SerializedName("panoramio_zoom")
    @Expose
    private Integer panoramioZoom;

    /**
     * 
     * @return
     *     The lat
     */
    public Double getLat() {
        return lat;
    }

    /**
     * 
     * @param lat
     *     The lat
     */
    public void setLat(Double lat) {
        this.lat = lat;
    }

    /**
     * 
     * @return
     *     The lon
     */
    public Double getLon() {
        return lon;
    }

    /**
     * 
     * @param lon
     *     The lon
     */
    public void setLon(Double lon) {
        this.lon = lon;
    }

    /**
     * 
     * @return
     *     The panoramioZoom
     */
    public Integer getPanoramioZoom() {
        return panoramioZoom;
    }

    /**
     * 
     * @param panoramioZoom
     *     The panoramio_zoom
     */
    public void setPanoramioZoom(Integer panoramioZoom) {
        this.panoramioZoom = panoramioZoom;
    }

}
