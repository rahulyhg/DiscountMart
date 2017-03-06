package sourabh.ichiapp.adaptors;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;

import java.math.BigDecimal;
import java.util.List;

import sourabh.ichiapp.R;
import sourabh.ichiapp.app.AppController;
import sourabh.ichiapp.data.GlobaDataHolder;
import sourabh.ichiapp.data.ProductData;

/**
 * Created by Downloader on 2/24/2017.
 */

public class ProductsAdaptor extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<ProductData> productsDataList;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    ProductData m;
    int position;
    public ProductsAdaptor(Activity activity,
                           List<ProductData> productsDataList
                           ) {
        this.activity = activity;
        this.productsDataList = productsDataList;
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
        TextView txtDescription = (TextView) convertView.findViewById(R.id.item_short_desc);
        TextView txtPrice = (TextView) convertView.findViewById(R.id.item_price);
        TextView txtAddItem = (TextView) convertView.findViewById(R.id.add_item);
        final TextView txtItemAmount = (TextView) convertView.findViewById(R.id.item_amount);

        TextView txtRemoveItem = (TextView) convertView.findViewById(R.id.remove_item);

        m = productsDataList.get(position);

        name = m.getName();
        description = m.getDescription();
        price = String.valueOf(m.getPrice());
        // thumbnail image
        thumbNail.setImageUrl(m.getImage(), imageLoader);

        txtName.setText(name);
        txtDescription.setText(description);
        txtPrice.setText(price);

        if (GlobaDataHolder.getGlobaDataHolder()
                .getShoppingList().contains(m)) {


            txtItemAmount.setText(GlobaDataHolder.getGlobaDataHolder()
                    .getShoppingList().get(GlobaDataHolder.getGlobaDataHolder()
                            .getShoppingList().indexOf(m)).getQuantity());

        }


        txtAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                txtItemAmount.setText(String.valueOf(Integer.parseInt(txtItemAmount.getText().toString())+1));


                ProductData tempObj = (productsDataList).get(position);

                //current object


                //if current object is lready in shopping list
                if (GlobaDataHolder.getGlobaDataHolder()
                        .getShoppingList().contains(tempObj)) {


                    //get position of current item in shopping list
                    int indexOfTempInShopingList = GlobaDataHolder
                            .getGlobaDataHolder().getShoppingList()
                            .indexOf(tempObj);

                    // increase quantity of current item in shopping list
                    if (Integer.parseInt(tempObj.getQuantity()) == 0) {

//                        ((ECartHomeActivity) getContext())
//                                .updateItemCount(true);

                    }


                    // update quanity in shopping list
                    GlobaDataHolder
                            .getGlobaDataHolder()
                            .getShoppingList()
                            .get(indexOfTempInShopingList)
                            .setQuantity(
                                    String.valueOf(Integer
                                            .valueOf(tempObj
                                                    .getQuantity()) + 1));


                    //update checkout amount
//                    ((ECartHomeActivity) getContext()).updateCheckOutAmount(
//                            BigDecimal
//                                    .valueOf(Long
//                                            .valueOf(productList
//                                                    .get(position)
//                                                    .getSellMRP())),
//                            true);

                    // update current item quanitity
//                    holder.quanitity.setText(m.getQuantity());

                } else {

//                    ((ECartHomeActivity) getContext()).updateItemCount(true);

                    tempObj.setQuantity(String.valueOf(1));

//                    holder.quanitity.setText(m.getQuantity());

                    GlobaDataHolder.getGlobaDataHolder()
                            .getShoppingList().add(tempObj);

//                    ((ECartHomeActivity) getContext()).updateCheckOutAmount(
//                            BigDecimal
//                                    .valueOf(Long
//                                            .valueOf(productList
//                                                    .get(position)
//                                                    .getSellMRP())),
//                            true);

                }




            }
        });

        txtRemoveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                txtItemAmount.setText(String.valueOf(Integer.parseInt(txtItemAmount.getText().toString())-1));



                ProductData tempObj = (productsDataList).get(position);

                if (GlobaDataHolder.getGlobaDataHolder().getShoppingList()
                        .contains(tempObj)) {

                    int indexOfTempInShopingList = GlobaDataHolder
                            .getGlobaDataHolder().getShoppingList()
                            .indexOf(tempObj);

                    if (Integer.valueOf(tempObj.getQuantity()) != 0) {

                        GlobaDataHolder
                                .getGlobaDataHolder()
                                .getShoppingList()
                                .get(indexOfTempInShopingList)
                                .setQuantity(
                                        String.valueOf(Integer.valueOf(tempObj
                                                .getQuantity()) - 1));

//                        ((ECartHomeActivity) getContext()).updateCheckOutAmount(
//                                BigDecimal.valueOf(Long.valueOf(productList
//                                        .get(position).getSellMRP())),
//                                false);
//
//                        holder.quanitity.setText(GlobaDataHolder
//                                .getGlobaDataHolder().getShoppingList()
//                                .get(indexOfTempInShopingList).getQuantity());
//
//                        Utils.vibrate(getContext());

                        if (Integer.valueOf(GlobaDataHolder
                                .getGlobaDataHolder().getShoppingList()
                                .get(indexOfTempInShopingList).getQuantity()) == 0) {

                            GlobaDataHolder.getGlobaDataHolder()
                                    .getShoppingList()
                                    .remove(indexOfTempInShopingList);

                            notifyDataSetChanged();

//                            ((ECartHomeActivity) getContext())
//                                    .updateItemCount(false);

                        }

                    }

                } else {

                }
            }
        });




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
}
