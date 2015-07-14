
package pl.wro.mm.materialweather.network.data.photo.photo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Photo_ {

    @Expose
    private Integer height;
    @Expose
    private Double latitude;
    @Expose
    private Double longitude;
    @SerializedName("owner_id")
    @Expose
    private Integer ownerId;
    @SerializedName("owner_name")
    @Expose
    private String ownerName;
    @SerializedName("owner_url")
    @Expose
    private String ownerUrl;
    @SerializedName("photo_file_url")
    @Expose
    private String photoFileUrl;
    @SerializedName("photo_id")
    @Expose
    private Integer photoId;
    @SerializedName("photo_title")
    @Expose
    private String photoTitle;
    @SerializedName("photo_url")
    @Expose
    private String photoUrl;
    @SerializedName("upload_date")
    @Expose
    private String uploadDate;
    @Expose
    private Integer width;
    @SerializedName("place_id")
    @Expose
    private String placeId;

    /**
     * 
     * @return
     *     The height
     */
    public Integer getHeight() {
        return height;
    }

    /**
     * 
     * @param height
     *     The height
     */
    public void setHeight(Integer height) {
        this.height = height;
    }

    /**
     * 
     * @return
     *     The latitude
     */
    public Double getLatitude() {
        return latitude;
    }

    /**
     * 
     * @param latitude
     *     The latitude
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    /**
     * 
     * @return
     *     The longitude
     */
    public Double getLongitude() {
        return longitude;
    }

    /**
     * 
     * @param longitude
     *     The longitude
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    /**
     * 
     * @return
     *     The ownerId
     */
    public Integer getOwnerId() {
        return ownerId;
    }

    /**
     * 
     * @param ownerId
     *     The owner_id
     */
    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    /**
     * 
     * @return
     *     The ownerName
     */
    public String getOwnerName() {
        return ownerName;
    }

    /**
     * 
     * @param ownerName
     *     The owner_name
     */
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    /**
     * 
     * @return
     *     The ownerUrl
     */
    public String getOwnerUrl() {
        return ownerUrl;
    }

    /**
     * 
     * @param ownerUrl
     *     The owner_url
     */
    public void setOwnerUrl(String ownerUrl) {
        this.ownerUrl = ownerUrl;
    }

    /**
     * 
     * @return
     *     The photoFileUrl
     */
    public String getPhotoFileUrl() {
        return photoFileUrl;
    }

    /**
     * 
     * @param photoFileUrl
     *     The photo_file_url
     */
    public void setPhotoFileUrl(String photoFileUrl) {
        this.photoFileUrl = photoFileUrl;
    }

    /**
     * 
     * @return
     *     The photoId
     */
    public Integer getPhotoId() {
        return photoId;
    }

    /**
     * 
     * @param photoId
     *     The photo_id
     */
    public void setPhotoId(Integer photoId) {
        this.photoId = photoId;
    }

    /**
     * 
     * @return
     *     The photoTitle
     */
    public String getPhotoTitle() {
        return photoTitle;
    }

    /**
     * 
     * @param photoTitle
     *     The photo_title
     */
    public void setPhotoTitle(String photoTitle) {
        this.photoTitle = photoTitle;
    }

    /**
     * 
     * @return
     *     The photoUrl
     */
    public String getPhotoUrl() {
        return photoUrl;
    }

    /**
     * 
     * @param photoUrl
     *     The photo_url
     */
    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    /**
     * 
     * @return
     *     The uploadDate
     */
    public String getUploadDate() {
        return uploadDate;
    }

    /**
     * 
     * @param uploadDate
     *     The upload_date
     */
    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    /**
     * 
     * @return
     *     The width
     */
    public Integer getWidth() {
        return width;
    }

    /**
     * 
     * @param width
     *     The width
     */
    public void setWidth(Integer width) {
        this.width = width;
    }

    /**
     * 
     * @return
     *     The placeId
     */
    public String getPlaceId() {
        return placeId;
    }

    /**
     * 
     * @param placeId
     *     The place_id
     */
    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

}
