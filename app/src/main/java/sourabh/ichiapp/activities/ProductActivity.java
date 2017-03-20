package sourabh.ichiapp.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import java.math.BigDecimal;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import sourabh.ichiapp.R;
import sourabh.ichiapp.data.AdSliderData;
import sourabh.ichiapp.data.GlobaDataHolder;
import sourabh.ichiapp.data.Money;
import sourabh.ichiapp.data.ProductData;
import sourabh.ichiapp.helper.CommonUtilities;
import sourabh.ichiapp.helper.Const;
import sourabh.ichiapp.helper.SessionManager;

import static java.security.AccessController.getContext;
import static sourabh.ichiapp.R.id.cart_count;
import static sourabh.ichiapp.R.id.product_menu_cart_count;
import static sourabh.ichiapp.data.Money.rupees;

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


    Context context;

    SessionManager sessionManager;
    ProductData productData;
    Button BtnCartCount;
    String product_cart_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        ButterKnife.bind(this);

        context = getApplicationContext();
        sessionManager = new SessionManager(context);

        productData = (ProductData) getIntent().getSerializableExtra(Const.KEY_PRODUCT_DATA);

        updateCartCount(GlobaDataHolder.getGlobaDataHolder().getShoppingList().size());


        setview();
    }

    public void setview() {


        product_page_title_main_title.setText(productData.getName());
        product_page_seller_name_text.setText("Retailer");
        product_page_ratings_count_text.setText("Ratings");
        product_page_description.setText(productData.getDescription());


        String price = Money.rupees(
                BigDecimal.valueOf(Double.valueOf(productData.getPrice()
                ))).toString()
                + "  ";

        String memberPrice = Money.rupees(
                BigDecimal.valueOf(Double.valueOf(productData.getMemberPrice()
                ))).toString()
                + "  ";

        String mrp = Money.rupees(
                BigDecimal.valueOf(Double.valueOf(productData.getMrp()
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
                    Double buyPrice = (productData.getMemberPrice() * (current_count + 1));

                    btnBuy.setText("Buy For : " +
                            Money.rupees(
                                    BigDecimal.valueOf(Double.valueOf(buyPrice
                                    ))).toString()
                            + "  ");
                } else {
                    Double buyPrice = (productData.getPrice() * (current_count + 1));
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
                    Double buyPrice = (productData.getMemberPrice() * (current_count - 1));
                    btnBuy.setText("Buy For : " +
                            Money.rupees(
                                    BigDecimal.valueOf(Double.valueOf(buyPrice
                                    ))).toString()
                            + "  ");
                } else {
                    Double buyPrice = (productData.getPrice() * (current_count - 1));
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
                        .getShoppingList().contains(productData)) {


                    //get position of current item in shopping list
                    int indexOfTempInShopingList = GlobaDataHolder
                            .getGlobaDataHolder().getShoppingList()
                            .indexOf(productData);

                    // increase quantity of current item in shopping list
                    if (Integer.parseInt(productData.getQuantity()) == 0) {

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
                                            .valueOf(productData
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

                    productData.setQuantity(String.valueOf(total_quantity));

//                    holder.quanitity.setText(m.getQuantity());

                    GlobaDataHolder.getGlobaDataHolder()
                            .getShoppingList().add(productData);

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




