package sourabh.ichiapp.data;

import sourabh.ichiapp.app.AppConfig;

import static android.R.attr.banner;

public class GenericData {

    private Integer id;
    private Integer category_id;
    private Integer city_id;
    private String name;
    private String address;
    private String phone;
    private String image;
    private Integer status;
    private String created_at;
    private String city_name;
    private String banner1;
    private String banner2;
    private String banner3;

    private String description;


    // Fields only used for retailers
    private Integer offer_category_id;
    private String offer_name;


    public Integer getOffer_category_id() {
        return offer_category_id;
    }

    public void setOffer_category_id(Integer offer_category_id) {
        this.offer_category_id = offer_category_id;
    }

    public String getOffer_name() {
        return offer_name;
    }

    public void setOffer_name(String offer_name) {
        this.offer_name = offer_name;
    }

    public Integer getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Integer category_id) {
        this.category_id = category_id;
    }

    public Integer getCity_id() {
        return city_id;
    }

    public void setCity_id(Integer city_id) {
        this.city_id = city_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getBanner1() {
        return AppConfig.IMAGES_BASE+banner1;
    }

    public void setBanner1(String banner1) {
        this.banner1 = banner1;
    }

    public String getBanner2() {
        return AppConfig.IMAGES_BASE+banner2;
    }

    public void setBanner2(String banner2) {
        this.banner2 = banner2;
    }

    public String getBanner3() {
        return AppConfig.IMAGES_BASE+banner3;
    }

    public void setBanner3(String banner3) {
        this.banner3 = banner3;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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