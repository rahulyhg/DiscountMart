package sourabh.ichiapp.data;

import java.util.List;

/**
 * Created by Sourabh on 3/17/2017.
 */

public class RetailerCategoryData {

    private Integer id;
    private String name;
    private String image;
    private Integer position;
    private Integer status;
    private String createdOn;

    private List<RetailerCategoryData> subcategories = null;

    public List<RetailerCategoryData> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<RetailerCategoryData> subcategories) {
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
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
