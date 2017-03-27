package sourabh.ichiapp.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import sourabh.ichiapp.R;
import sourabh.ichiapp.app.AppConfig;
import sourabh.ichiapp.app.CustomRequest;
import sourabh.ichiapp.data.GlobaDataHolder;
import sourabh.ichiapp.data.Money;
import sourabh.ichiapp.data.ProductData;
import sourabh.ichiapp.data.ProductVarientData;
import sourabh.ichiapp.helper.CommonUtilities;
import sourabh.ichiapp.helper.Const;
import sourabh.ichiapp.helper.JsonSeparator;
import sourabh.ichiapp.helper.SessionManager;

import static sourabh.ichiapp.R.id.cart_count;

public class ProductActivity extends AppCompatActivity {

    @BindView(R.id.product_page_title_main_title)
    TextView product_page_title_main_title;
    @BindView(R.id.product_page_seller_name_text)
    TextView product_page_seller_name_text;
    @BindView(R.id.product_page_ratings_count_text)
    TextView product_page_ratings_count_text;
    @BindView(R.id.product_page_description)
    TextView product_page_description;
    @BindView(R.id.product_price)
    TextView product_price;
    @BindView(R.id.member_price)
    TextView member_price;
    @BindView(R.id.product_mrp)
    TextView product_mrp;
    @BindView(R.id.item_amount)
    TextView item_amount;
    @BindView(R.id.imgShare)
    ImageButton imgShare;
    @BindView(R.id.btnBuy)
    Button btnBuy;
    @BindView(R.id.slider_product)
    SliderLayout slider_product;

    @BindView(R.id.remove_item)
    TextView remove_item;
    @BindView(R.id.add_item)
    TextView add_item;
    @BindView(R.id.txtChooseVarient)
    TextView txtChooseVarient;
    @BindView(R.id.rateBar)
    MaterialRatingBar ratebar;

    Context context;

    SessionManager sessionManager;
    ProductData productData;

    ProductVarientData productVarientData;

    Button BtnCartCount;
    String product_cart_count;
    private ArrayList<ProductVarientData> productVarientList =  new ArrayList<>();
    List varients = new ArrayList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        ButterKnife.bind(this);

        context = getApplicationContext();
        sessionManager = new SessionManager(context);

        productVarientList = (ArrayList<ProductVarientData>) getIntent().getBundleExtra(Const.KEY_PRODUCT_VARIENTS_DATA).getSerializable(Const.KEY_PRODUCT_VARIENTS_DATA);
        productData = (ProductData) getIntent().getSerializableExtra(Const.KEY_PRODUCT_DATA);

        updateCartCount(GlobaDataHolder.getGlobaDataHolder().getShoppingList().size());


        setview(productVarientList.get(0));
    }

    public void setview(ProductVarientData productVarientDataLocal) {


        varients.clear();

        this.productVarientData = productVarientDataLocal;
        productVarientData.setName(productData.getName());
        productVarientData.setImage1(productData.getImage1());

        txtChooseVarient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseVarient();
            }
        });


        product_page_title_main_title.setText(productData.getName());
        product_page_seller_name_text.setText("Retailer");
        product_page_ratings_count_text.setText("Ratings");
        product_page_description.setText(productData.getDescription());

        if (sessionManager.isMember()) {
            Double buyPrice = (productVarientData.getMember_price());
            txtChooseVarient.setText(productVarientData.getUnit());

            for (ProductVarientData pvd: productVarientList)
            {
                varients.add(pvd.getUnit()+" : "+
                        Money.rupees(
                        BigDecimal.valueOf(Double.valueOf(buyPrice
                        ))).toString());

            }

        } else {
            Double buyPrice = (productVarientData.getPrice() );
            txtChooseVarient.setText(productVarientData.getUnit());
            for (ProductVarientData pvd: productVarientList)
            {
                varients.add(pvd.getUnit()+" : "+
                        Money.rupees(
                                BigDecimal.valueOf(Double.valueOf(buyPrice
                                ))).toString());

            }
        }

        String price = Money.rupees(
                BigDecimal.valueOf(Double.valueOf(productVarientData.getPrice()
                ))).toString()
                + "  ";

        String memberPrice = Money.rupees(
                BigDecimal.valueOf(Double.valueOf(productVarientData.getMember_price()
                ))).toString()
                + "  ";

        String mrp = Money.rupees(
                BigDecimal.valueOf(Double.valueOf(productVarientData.getMrp()
                ))).toString()
                + "  ";

        product_price.setText(price);
        member_price.setText(memberPrice);
        product_mrp.setText(mrp, TextView.BufferType.SPANNABLE);
        item_amount.setText("0");
        slider_product.addSlider(new TextSliderView(context).image(productData.getImage1()));
        member_price.setTextColor(getResources().getColor(R.color.colorPrimary));


        Spannable spannable = (Spannable) product_mrp.getText();
        spannable.setSpan(new StrikethroughSpan(), 0,
                product_mrp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        showProductSlider(CommonUtilities.getProductImagesFromProductData(productData));


        add_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int current_count = Integer.parseInt(item_amount.getText().toString());
                item_amount.setText(String.valueOf(current_count + 1));


                if (sessionManager.isMember()) {
                    Double buyPrice = (productVarientData.getMember_price() * (current_count + 1));

                    btnBuy.setText("Buy For : " +
                            Money.rupees(
                                    BigDecimal.valueOf(Double.valueOf(buyPrice
                                    ))).toString()
                            + "  ");
                } else {
                    Double buyPrice = (productVarientData.getPrice() * (current_count + 1));
                    btnBuy.setText("Buy For : " +
                            Money.rupees(
                                    BigDecimal.valueOf(Double.valueOf(buyPrice
                                    ))).toString()
                            + "  ");
                }
            }
        });


        remove_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int current_count = Integer.parseInt(item_amount.getText().toString());
                if (current_count == 0) {
                    btnBuy.setText("Buy");
                    return;
                }

                item_amount.setText(String.valueOf(current_count - 1));

                if (sessionManager.isMember()) {
                    Double buyPrice = (productVarientData.getMember_price() * (current_count - 1));
                    btnBuy.setText("Buy For : " +
                            Money.rupees(
                                    BigDecimal.valueOf(Double.valueOf(buyPrice
                                    ))).toString()
                            + "  ");
                } else {
                    Double buyPrice = (productVarientData.getPrice() * (current_count - 1));
                    btnBuy.setText("Buy For : " +
                            Money.rupees(
                                    BigDecimal.valueOf(Double.valueOf(buyPrice
                                    ))).toString()
                            + "  ");
                }

            }
        });

        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int total_quantity = Integer.parseInt(String.valueOf(item_amount.getText()));


                if (GlobaDataHolder.getGlobaDataHolder()
                        .getShoppingList().contains(productVarientData)) {


                    //get position of current item in shopping list
                    int indexOfTempInShopingList = GlobaDataHolder
                            .getGlobaDataHolder().getShoppingList()
                            .indexOf(productVarientData);

                    // increase quantity of current item in shopping list
                    if (Integer.parseInt(productVarientData.getQuantity()) == 0) {

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
                                            .valueOf(productVarientData
                                                    .getQuantity()) + total_quantity));


                    //update checkout amount
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


                    // update current item quanitity
//                    holder.quanitity.setText(m.getQuantity());

                } else {

//                    ((ECartHomeActivity) getContext()).updateItemCount(true);

                    productVarientData.setQuantity(String.valueOf(total_quantity));

//                    holder.quanitity.setText(m.getQuantity());

                    GlobaDataHolder.getGlobaDataHolder()
                            .getShoppingList().add(productVarientData);

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
                }

                updateCartCount(GlobaDataHolder.getGlobaDataHolder().getShoppingList().size());



            }




        });



        ratebar.setRating(productVarientData.getAvg_rating());
        ratebar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

                setRating(Integer.parseInt(sessionManager.getUserId()),productVarientData.getProduct_id(),v);

            }
        });




    }

    void setRating(int user_id, int product_varient_id, float rating){



        RequestQueue requestQueue = Volley.newRequestQueue(context);
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", String.valueOf(user_id));
        params.put("product_varient_id",String.valueOf(product_varient_id));
        params.put("rating", String.valueOf(rating));



        CustomRequest jsObjRequest   = new CustomRequest(ProductActivity.this,true, Request.Method.POST,
                AppConfig.URL_CREATE_RATING, params, CommonUtilities.buildHeaders(context),

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        JsonSeparator js = new JsonSeparator(context,response);

                        //  Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();

                        try {
                            if(js.isError()){
                                Toast.makeText(context, js.getMessage(), Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(context, js.getMessage(), Toast.LENGTH_LONG).show();


                            }}
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        try {
                            throw new IOException("Post failed with error code " + error.getMessage());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                        Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();

                    }
                });
        requestQueue.add(jsObjRequest);


    }

    void chooseVarient()
    {

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        //  builderSingle.setIcon(R.drawable.);
        // builderSingle.setTitle("Select Subcategory :");
        LayoutInflater li = LayoutInflater.from(this);
        // LayoutInflater inflater =getLayoutInflater();/
        View view=li.inflate(R.layout.dialog_title, null);
        TextView title = (TextView)view.findViewById(R.id.myTitle);
        title.setText("Select");
        builderSingle.setCustomTitle(view);



        // Creating multiple selection by using setMutliChoiceItem method

        String[] varientsArr = new String[varients.size()];
        varientsArr = (String[]) varients.toArray(varientsArr);




        builderSingle.setSingleChoiceItems(varientsArr, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {



            }
        });
        builderSingle.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {

                ListView lw = ((AlertDialog) dialog).getListView();
                Object checkedItem = lw.getAdapter().getItem(lw.getCheckedItemPosition());
                Toast.makeText(getApplicationContext(), ((String) checkedItem), Toast.LENGTH_SHORT).show();

                txtChooseVarient.setText(((String) checkedItem));

                setview(productVarientList.get(lw.getCheckedItemPosition()));

            }
        });
        builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getBaseContext(), "Cancel pritisnuto!", Toast.LENGTH_SHORT).show();
            }
        });


//        builderSingle.setMultiChoiceItems(varientsArr, is_checked,
//                new DialogInterface.OnMultiChoiceClickListener() {
//                    public void onClick(DialogInterface dialog,
//                                        int whichButton, boolean isChecked) {
////                        if (isChecked) {
////                        } else {
////                        }
////                        if (locationcount > 2) {
////                            Toast.makeText(context,"Please select maximum 2 holidays to proceed",Toast.LENGTH_SHORT).show();
////                            ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
////                        } else {
////                            ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
////                        }
//                    }
//                });
//
//        builderSingle.setPositiveButton("OK",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        ListView list = ((AlertDialog) dialog).getListView();
//                        // make selected item in the comma seprated string
//                        StringBuilder stringBuilder = new StringBuilder();
//                        for (int i = 0; i < list.getCount(); i++) {
//                            boolean checked = list.isItemChecked(i);
//                            if (checked) {
//
//
//                            }
//                        }
//                    }
//                });
//
//        builderSingle.setNegativeButton("Cancel",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//
//                    }
//                });
        AlertDialog alert = builderSingle.create();
        alert.show();

    }
    void refreshActivity(){
        Intent intent = getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        startActivity(intent);
    }

    void showProductSlider(ArrayList<String> productImages) {


        if (productImages != null) {


            for (int i = 0; i < productImages.size(); i++) {

                slider_product.addSlider(new DefaultSliderView(context).image(productImages.get(i)));


            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.home, menu);
//        return true;

        menu.clear();
        getMenuInflater().inflate(R.menu.product, menu);

        if (GlobaDataHolder.getGlobaDataHolder().getShoppingList().size() > 0) {

            MenuItem item = menu.findItem(R.id.product_menu_cart_count);
            MenuItemCompat.setActionView(item, R.layout.cart_update_count);
            View view = MenuItemCompat.getActionView(item);
            BtnCartCount = (Button)view.findViewById(R.id.btn_cart_count);
            BtnCartCount.setText(String.valueOf(product_cart_count));


            BtnCartCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(), CartActivity.class));
                }
            });
        }


        return true;
    }

    private void updateCartCount(int count) {
        product_cart_count = count + "";
        supportInvalidateOptionsMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == cart_count) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateCartCount(GlobaDataHolder.getGlobaDataHolder().getShoppingList().size());

    }
}




