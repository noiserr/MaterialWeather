
package pl.wro.mm.materialweather.photoGson;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Photo {

    @Expose
    private Integer count;
    @SerializedName("has_more")
    @Expose
    private Boolean hasMore;
    @SerializedName("map_location")
    @Expose
    private MapLocation mapLocation;
    @Expose
    private List<Photo_> photos = new ArrayList<Photo_>();

    /**
     * 
     * @return
     *     The count
     */
    public Integer getCount() {
        return count;
    }

    /**
     * 
     * @param count
     *     The count
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     * 
     * @return
     *     The hasMore
     */
    public Boolean getHasMore() {
        return hasMore;
    }

    /**
     * 
     * @param hasMore
     *     The has_more
     */
    public void setHasMore(Boolean hasMore) {
        this.hasMore = hasMore;
    }

    /**
     * 
     * @return
     *     The mapLocation
     */
    public MapLocation getMapLocation() {
        return mapLocation;
    }

    /**
     * 
     * @param mapLocation
     *     The map_location
     */
    public void setMapLocation(MapLocation mapLocation) {
        this.mapLocation = mapLocation;
    }

    /**
     * 
     * @return
     *     The photos
     */
    public List<Photo_> getPhotos() {
        return photos;
    }

    /**
     * 
     * @param photos
     *     The photos
     */
    public void setPhotos(List<Photo_> photos) {
        this.photos = photos;
    }

}
