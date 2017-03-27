package sourabh.ichiapp.data;

import java.io.Serializable;
import java.util.List;

import sourabh.ichiapp.app.AppConfig;

/**
 * Created by Sourabh on 3/6/2017.
 */
public class ProductData implements Serializable {

private Integer id;
private Integer category_id;
private Integer distributor_id;
private String name;
private String image1;
private Object image2;
private Object image3;
private String description;
private Integer status;
private String createdOn;
private List<ProductVarientData> product_varients = null;

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

public Integer getDistributor_id() {
        return distributor_id;
        }

public void setDistributor_id(Integer distributor_id) {
        this.distributor_id = distributor_id;
        }

public String getName() {
        return name;
        }

public void setName(String name) {
        this.name = name;
        }

public String getImage1() {
        if(image1.equals("") || image1 == null){
                return AppConfig.URL_NO_IMAGE;

        }else{
                return AppConfig.IMAGES_BASE+ image1;
        }
}

public void setImage1(String image1) {
        this.image1 = image1;
        }

public String getImage2() {
        if(image1.equals("") || image1 == null){
                return AppConfig.URL_NO_IMAGE;

        }else{
                return AppConfig.IMAGES_BASE+ image2;
        }
}

public void setImage2(Object image2) {
        this.image2 = image2;
        }

public String getImage3() {
        if(image1.equals("") || image1 == null){
                return AppConfig.URL_NO_IMAGE;

        }else{
                return AppConfig.IMAGES_BASE+ image3;
        }
}

public void setImage3(Object image3) {
        this.image3 = image3;
        }

public String getDescription() {
        return description;
        }

public void setDescription(String description) {
        this.description = description;
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

public List<ProductVarientData> getProduct_varients() {
        return product_varients;
        }

public void setProduct_varients(List<ProductVarientData> product_varients) {
        this.product_varients = product_varients;
        }

        }

//public class ProductData implements Serializable {
//
//    private Integer id;
//    private Integer category_id;
//    private Integer distributor_id;
//    private String name;
//    private String image1;
//    private String image2;
//    private String image3;
//
//    private String description;
//    private String quantity;
//    private Double mrp;
//    private Double price;
//    private Double member_price;
//
//    private Integer status;
//    private String createdOn;
//
//
//    public String getImage2() {
//        return AppConfig.IMAGES_BASE+image2;
//    }
//
//    public void setImage2(String image2) {
//        this.image2 = image2;
//    }
//
//    public String getImage3() {
//        return AppConfig.IMAGES_BASE+image3;
//    }
//
//    public void setImage3(String image3) {
//        this.image3 = image3;
//    }
//
//
//
//    public Double getMember_price() {
//        return member_price;
//    }
//
//    public void setMember_price(Double memberPrice) {
//        this.member_price = member_price;
//    }
//
//    public Integer getId() {
//        return id;
//    }
//
//    public void setId(Integer id) {
//        this.id = id;
//    }
//
//    public Integer getCategory_id() {
//        return category_id;
//    }
//
//    public void setCategory_id(Integer category_id) {
//        this.category_id = category_id;
//    }
//
//    public Integer getDistributor_id() {
//        return distributor_id;
//    }
//
//    public void setDistributor_id(Integer distributor_id) {
//        this.distributor_id = distributor_id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getImage1() {
//        return AppConfig.IMAGES_BASE+ image1;
//    }
//
//    public void setImage1(String image1) {
//        this.image1 = image1;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public String getQuantity() {
//        return quantity;
//    }
//
//    public void setQuantity(String quantity) {
//        this.quantity = quantity;
//    }
//
//    public Double getMrp() {
//        return mrp;
//    }
//
//    public void setMrp(Double mrp) {
//        this.mrp = mrp;
//    }
//
//    public Double getPrice() {
//        return price;
//    }
//
//    public void setPrice(Double price) {
//        this.price = price;
//    }
//
//    public Integer getStatus() {
//        return status;
//    }
//
//    public void setStatus(Integer status) {
//        this.status = status;
//    }
//
//    public String getCreated_on() {
//        return createdOn;
//    }
//
//    public void setCreated_on(String createdOn) {
//        this.createdOn = createdOn;
//    }
//
//}
