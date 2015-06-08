
package pl.wro.mm.materialweather.image;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;

public class Photos {

    @Expose
    private Integer page;
    @Expose
    private String pages;
    @Expose
    private Integer perpage;
    @Expose
    private String total;
    @Expose
    private List<Photo_> photo = new ArrayList<Photo_>();

    /**
     * 
     * @return
     *     The page
     */
    public Integer getPage() {
        return page;
    }

    /**
     * 
     * @param page
     *     The page
     */
    public void setPage(Integer page) {
        this.page = page;
    }

    /**
     * 
     * @return
     *     The pages
     */
    public String getPages() {
        return pages;
    }

    /**
     * 
     * @param pages
     *     The pages
     */
    public void setPages(String pages) {
        this.pages = pages;
    }

    /**
     * 
     * @return
     *     The perpage
     */
    public Integer getPerpage() {
        return perpage;
    }

    /**
     * 
     * @param perpage
     *     The perpage
     */
    public void setPerpage(Integer perpage) {
        this.perpage = perpage;
    }

    /**
     * 
     * @return
     *     The total
     */
    public String getTotal() {
        return total;
    }

    /**
     * 
     * @param total
     *     The total
     */
    public void setTotal(String total) {
        this.total = total;
    }

    /**
     * 
     * @return
     *     The photo
     */
    public List<Photo_> getPhoto() {
        return photo;
    }

    /**
     * 
     * @param photo
     *     The photo
     */
    public void setPhoto(List<Photo_> photo) {
        this.photo = photo;
    }

}
