package sourabh.ichiapp.adaptors;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import sourabh.ichiapp.R;
import sourabh.ichiapp.activities.CartActivity;
import sourabh.ichiapp.activities.ProductActivity;
import sourabh.ichiapp.app.AppController;
import sourabh.ichiapp.data.GlobaDataHolder;
import sourabh.ichiapp.data.Money;
import sourabh.ichiapp.data.ProductData;
import sourabh.ichiapp.data.ProductVarientData;
import sourabh.ichiapp.helper.Const;

import static sourabh.ichiapp.helper.Const.KEY_PRODUCT_VARIENTS_DATA;

/**
 * Created by Downloader on 2/24/2017.
 */

public class ProductsAdaptor extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<ProductData> productsDataList;
    private ArrayList<ProductVarientData> productVarientDataList =  new ArrayList<>();



    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    ProductData m;
    int position;
    private Context context;

    public ProductsAdaptor(Activity activity,
                           Context context,
                           List<ProductData> productsDataList,
                           Boolean isCart) {
        this.activity = activity;
        this.productsDataList = productsDataList;
        this.context = context;

    }




    @Override
    public int getCount() {
        return productsDataList.size();
    }

    @Override
    public Object getItem(int location) {
        return productsDataList.get(location);
    }

    @Override
    public long getItemId(int position)  {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        final String name,description, quantity, price;

        this.position = position;
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.product_list_row, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        RelativeLayout list_layout = (RelativeLayout) convertView
                .findViewById(R.id.list_layout);
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.product_thumb);
        TextView txtName = (TextView) convertView.findViewById(R.id.item_name);
//        TextView txtDescription = (TextView) convertView.findViewById(R.id.item_short_desc);


        TextView txtPrice = (TextView) convertView.findViewById(R.id.item_price);

        TextView txtMemberPrice = (TextView) convertView.findViewById(R.id.item_member_price);

        TextView txtAddItem = (TextView) convertView.findViewById(R.id.add_item);
        final TextView txtItemAmount = (TextView) convertView.findViewById(R.id.item_amount);

        TextView txtRemoveItem = (TextView) convertView.findViewById(R.id.remove_item);

        m = productsDataList.get(position);

        name = m.getName();
        description = m.getDescription();

        productVarientDataList = (ArrayList<ProductVarientData>) m.getProduct_varients();
        ProductVarientData firstProductVarientData = productVarientDataList.get(0);


        String mrp = Money.rupees(
                BigDecimal.valueOf(Double.valueOf(firstProductVarientData.getMrp()
                        ))).toString()
                + "  ";

        String discounted_price = Money.rupees(
                BigDecimal.valueOf(Double.valueOf(firstProductVarientData.getPrice()
                ))).toString();

        String member_price = Money.rupees(
                BigDecimal.valueOf(Double.valueOf(firstProductVarientData.getMember_price()
                ))).toString();

         member_price = "<font color='"+context.getResources().getColor(R.color.colorPrimary)+"'>"+member_price+"</font>";


        String costString = discounted_price+"   "+ mrp;

        txtPrice.setText(costString, TextView.BufferType.SPANNABLE);
        txtMemberPrice.setText(Html.fromHtml(member_price), TextView.BufferType.SPANNABLE);

        Spannable spannable = (Spannable) txtPrice.getText();

        spannable.setSpan(new StrikethroughSpan(), mrp.length(),
                costString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);



        // thumbnail image
        thumbNail.setImageUrl(m.getImage1(), imageLoader);

        txtName.setText(name);
//        txtDescription.setText(description);

        if (GlobaDataHolder.getGlobaDataHolder()
                .getShoppingList().contains(m)) {


//            txtItemAmount.setText(GlobaDataHolder.getGlobaDataHolder()
//                    .getShoppingList().get(GlobaDataHolder.getGlobaDataHolder()
//                            .getShoppingList().indexOf(m)).getQuantity());

        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle args = new Bundle();
                args.putSerializable(KEY_PRODUCT_VARIENTS_DATA,(Serializable)productVarientDataList);

                activity.startActivity(new Intent(activity, ProductActivity.class)
                            .putExtra(KEY_PRODUCT_VARIENTS_DATA, args )
                            .putExtra(Const.KEY_PRODUCT_DATA,productsDataList.get(position)));
            }
        });



// Commented to hide plus minus buttons functionality


//        txtAddItem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                txtItemAmount.setText(String.valueOf(Integer.parseInt(txtItemAmount.getText().toString())+1));
//
//
//                ProductData tempObj = (productsDataList).get(position);
//
//                //current object
//
//
//                //if current object is lready in shopping list
//                if (GlobaDataHolder.getGlobaDataHolder()
//                        .getShoppingList().contains(tempObj)) {
//
//
//                    //get position of current item in shopping list
//                    int indexOfTempInShopingList = GlobaDataHolder
//                            .getGlobaDataHolder().getShoppingList()
//                            .indexOf(tempObj);
//
//                    // increase quantity of current item in shopping list
//                    if (Integer.parseInt(tempObj.getQuantity()) == 0) {
//
////                        ((ECartHomeActivity) getContext())
////                                .updateItemCount(true);
//
//                    }
//
//
//                    // update quanity in shopping list
//                    GlobaDataHolder
//                            .getGlobaDataHolder()
//                            .getShoppingList()
//                            .get(indexOfTempInShopingList)
//                            .setQuantity(
//                                    String.valueOf(Integer
//                                            .valueOf(tempObj
//                                                    .getQuantity()) + 1));
//
//
//                    //update checkout amount
//                    if(isCart){
//
//                        ((CartActivity) getContext()).updateCheckOutAmount(
//                                BigDecimal
//                                        .valueOf(Double
//                                                .valueOf(m
//                                                        .getPrice())),
//
//                                BigDecimal.valueOf((m.getMrp()-(m.getPrice()))),
//
//
//                                true);
//                    }
//
//
//                    // update current item quanitity
////                    holder.quanitity.setText(m.getQuantity());
//
//                } else {
//
////                    ((ECartHomeActivity) getContext()).updateItemCount(true);
//
//                    tempObj.setQuantity(String.valueOf(1));
//
////                    holder.quanitity.setText(m.getQuantity());
//
//                    GlobaDataHolder.getGlobaDataHolder()
//                            .getShoppingList().add(tempObj);
//
//                    if(isCart) {
//
//
//                        ((CartActivity) getContext()).updateCheckOutAmount(
//                                BigDecimal
//                                        .valueOf(Double
//                                                .valueOf(m
//                                                        .getPrice())),
//                                BigDecimal.valueOf((m.getMrp()-(m.getPrice()))),
//
//                                true);
//
//                    }
//                }
//
//
//
//
//            }
//        });
//
//        txtRemoveItem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                txtItemAmount.setText(String.valueOf(Integer.parseInt(txtItemAmount.getText().toString())-1));
//
//
//
//                ProductData tempObj = (productsDataList).get(position);
//
//                if (GlobaDataHolder.getGlobaDataHolder().getShoppingList()
//                        .contains(tempObj)) {
//
//                    int indexOfTempInShopingList = GlobaDataHolder
//                            .getGlobaDataHolder().getShoppingList()
//                            .indexOf(tempObj);
//
//                    if (Integer.valueOf(tempObj.getQuantity()) != 0) {
//
//                        GlobaDataHolder
//                                .getGlobaDataHolder()
//                                .getShoppingList()
//                                .get(indexOfTempInShopingList)
//                                .setQuantity(
//                                        String.valueOf(Integer.valueOf(tempObj
//                                                .getQuantity()) - 1));
//
//                        if(isCart) {
//                            ((CartActivity) getContext()).updateCheckOutAmount(
//                                    BigDecimal
//                                            .valueOf(Double
//                                                    .valueOf(m
//                                                            .getPrice())),
//                                    BigDecimal.valueOf((m.getMrp()-(m.getPrice()))),
//
//                                    false);
//                        }
////
////                        holder.quanitity.setText(GlobaDataHolder
////                                .getGlobaDataHolder().getShoppingList()
////                                .get(indexOfTempInShopingList).getQuantity());
////
////                        Utils.vibrate(getContext());
//
//                        if (Integer.valueOf(GlobaDataHolder
//                                .getGlobaDataHolder().getShoppingList()
//                                .get(indexOfTempInShopingList).getQuantity()) == 0) {
//
//                            GlobaDataHolder.getGlobaDataHolder()
//                                    .getShoppingList()
//                                    .remove(indexOfTempInShopingList);
//
//                            notifyDataSetChanged();
//
////                            ((ECartHomeActivity) getContext())
////                                    .updateItemCount(false);
//
//                        }
//
//                    }
//
//                } else {
//
//                }
//            }
//        });




//        list_layout.setOnClickListener(new View.OnClickListener() {
//                                     @Override
//                                     public void onClick(View v) {
//
//                                        Intent i = new Intent(activity, RetailerProfileAndCouponActivity.class);
//
//                                         i.putExtra(Const.KEY_RETAILERS,new Gson().toJson(productsDataList.get(position)));
//                                         i.putExtra(Const.KEY_OFFER_DATA,new Gson().toJson(offerCategoryData));
//                                         activity.startActivity(i);
//                                     }
//                                 }
//        );



//        // txtPhone
//        String genreStr = "";
//        for (String str : m.getGenre()) {
//            genreStr += str + ", ";
//        }
//        genreStr = genreStr.length() > 0 ? genreStr.substring(0,
//                genreStr.length() - 2) : genreStr;
//        txtPhone.setText(genreStr);

        // release year
//        year.setText(String.valueOf(m.getPhone()));

        return convertView;
    }

    private CartActivity getContext() {
        // TODO Auto-generated method stub
        return (CartActivity) context;
    }
}
