package sourabh.ichiapp.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Sourabh on 3/26/2017.
 */

    public class ProductVarientData implements Serializable {

    private Integer id;
    private Integer product_id;
    private String quantity;
    private Double mrp;
    private Double price;
    private Double member_price;
    private Integer status;
    private String created_on;
    private String unit;
    private Integer quantities_available;

    private float avg_rating;

    // used for cart object
    private String name;
    private String image1;


    public float getAvg_rating() {

        return avg_rating;
    }

    public void setAvg_rating(float avg_rating) {
        this.avg_rating = avg_rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getQuantities_available() {
        return quantities_available;
    }

    public void setQuantities_available(Integer quantities_available) {
        this.quantities_available = quantities_available;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Integer product_id) {
        this.product_id = product_id;
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

    public Double getMember_price() {
        return member_price;
    }

    public void setMember_price(Double member_price) {
        this.member_price = member_price;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

//    @Override
//    public int describeContents() {
//
//        return 0;
//    }

//    public ProductVarientData(Parcel parcel) {
//
//
//
//
//    }
//
//
//
//    @Override
//    public void writeToParcel(Parcel parcel, int i) {
//
//        parcel.writeInt(id);
//        parcel.writeInt(product_id);
//        parcel.writeString(quantity);
//        parcel.writeDouble(mrp);
//        parcel.writeDouble(price);
//        parcel.writeDouble(member_price);
//        parcel.writeInt(status);
//        parcel.writeString(created_on);
//
//
//    }
//
//
//    public static final Parcelable.Creator<ProductVarientData> CREATER = new Creator<ProductVarientData>() {
//        @Override
//        public ProductVarientData createFromParcel(Parcel parcel) {
//            return new ProductVarientData(parcel);
//        }
//
//        @Override
//        public ProductVarientData[] newArray(int i) {
//            return new ProductVarientData[i];
//        }
//
//    };



}

