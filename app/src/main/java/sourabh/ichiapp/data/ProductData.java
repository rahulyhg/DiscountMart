package sourabh.ichiapp.data;

import java.io.Serializable;

import sourabh.ichiapp.app.AppConfig;

/**
 * Created by Sourabh on 3/6/2017.
 */


public class ProductData implements Serializable {

    private Integer id;
    private Integer category_id;
    private Integer retailer_id;
    private String name;
    private String image1;
    private String image2;
    private String image3;

    private String description;
    private String quantity;
    private Double mrp;
    private Double price;
    private Double member_price;

    private Integer status;
    private String createdOn;


    public String getImage2() {
        return AppConfig.IMAGES_BASE+image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return AppConfig.IMAGES_BASE+image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }



    public Double getMemberPrice() {
        return member_price;
    }

    public void setMemberPrice(Double memberPrice) {
        this.member_price = member_price;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Integer category_id) {
        this.category_id = category_id;
    }

    public Integer getRetailer_id() {
        return retailer_id;
    }

    public void setRetailer_id(Integer retailer_id) {
        this.retailer_id = retailer_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage1() {
        return AppConfig.IMAGES_BASE+ image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public Double getMrp() {
        return mrp;
    }

    public void setMrp(Double mrp) {
        this.mrp = mrp;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
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
