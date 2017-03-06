package sourabh.ichiapp.data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Downloader on 2/25/2017.
 */

public class GenericCategoryData implements Serializable {

    private Integer id;
    private String name;
    private Integer position;
    private Integer status;
    private String createdOn;
    private String image;

    private List<GenericCategoryData> subcategories = null;

    public List<GenericCategoryData> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<GenericCategoryData> subcategories) {
        this.subcategories = subcategories;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }


}
