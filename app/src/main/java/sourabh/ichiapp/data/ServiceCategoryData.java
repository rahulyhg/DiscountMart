package sourabh.ichiapp.data;

import java.io.Serializable;
import java.util.List;

import sourabh.ichiapp.app.AppConfig;

/**
 * Created by Downloader on 2/23/2017.
 */

public class ServiceCategoryData implements Serializable {

    private Integer id;
    private String name;
    private String image;
    private Integer status;

    private List<ServiceCategoryData> subcategories = null;

    public List<ServiceCategoryData> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<ServiceCategoryData> subcategories) {
        this.subcategories = subcategories;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return AppConfig.IMAGES_BASE+image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
