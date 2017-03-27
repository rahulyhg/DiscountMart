package sourabh.ichiapp.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import sourabh.ichiapp.R;
import sourabh.ichiapp.adaptors.CartAdaptor;
import sourabh.ichiapp.adaptors.ProductsAdaptor;
import sourabh.ichiapp.app.AppConfig;
import sourabh.ichiapp.app.CustomRequest;
import sourabh.ichiapp.data.AddressData;
import sourabh.ichiapp.data.GenericCategoryData;
import sourabh.ichiapp.data.GlobaDataHolder;
import sourabh.ichiapp.data.Money;
import sourabh.ichiapp.data.ProductData;
import sourabh.ichiapp.data.ProductVarientData;
import sourabh.ichiapp.fragments.ItemPickerDialogFragment;
import sourabh.ichiapp.helper.CommonUtilities;
import sourabh.ichiapp.helper.Const;
import sourabh.ichiapp.helper.JsonSeparator;
import sourabh.ichiapp.helper.SessionManager;

public class CartActivity extends AppCompatActivity implements ItemPickerDialogFragment.OnItemSelectedListener  {
    private boolean resumeHasRun = false;


    Context context;

    @BindView(R.id.list)
    ListView listView;

    @BindView(R.id.txtTotalAmt)
    TextView txtTotalAmt;

    @BindView(R.id.txtSavedAmt)
    TextView txtSavedAmt;

    @BindView(R.id.checkout)
    LinearLayout checkout;

    public static final int ADD_ADDRESS_ACTIVITY = 123456;

    private CartAdaptor cartAdaptor;
    private List<ProductVarientData> productVarientDataList = new ArrayList<ProductVarientData>();

    GenericCategoryData genericCategoryData;
    private BigDecimal checkoutAmount = new BigDecimal(BigInteger.ZERO);
    private BigDecimal savingAmount = new BigDecimal(BigInteger.ZERO);

    SessionManager sessionManager;
    int selected_address_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        ButterKnife.bind(this);
        context = getApplicationContext();

        cartAdaptor = new CartAdaptor(this,
                CartActivity.this,
                GlobaDataHolder.getGlobaDataHolder().getShoppingList());
        listView.setAdapter(cartAdaptor);

        txtTotalAmt.setText(Money.rupees(BigDecimal.ZERO).toString());

        for (ProductVarientData product : GlobaDataHolder.getGlobaDataHolder()
                .getShoppingList()) {

            updateCheckOutAmount(
                    BigDecimal.valueOf(Double.valueOf(product.getPrice()*Integer.parseInt(product.getQuantity()))),
                    BigDecimal.valueOf((product.getMrp()-(product.getPrice()))*Integer.parseInt(product.getQuantity())),
                    true);
        }


        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getAddresses();
            }
        });

        sessionManager = new SessionManager(context);

    }
    public void updateCheckOutAmount(BigDecimal amount,BigDecimal saving, boolean increment) {

        if (increment) {
            checkoutAmount = checkoutAmount.add(amount);
            savingAmount = savingAmount.add(saving);
        } else {
            if (checkoutAmount.signum() == 1)
                checkoutAmount = checkoutAmount.subtract(amount);
                    savingAmount = savingAmount.subtract(saving);

        }

        txtTotalAmt.setText(Money.rupees(checkoutAmount).toString());
        txtSavedAmt.setText(Money.rupees(savingAmount).toString());

        if(txtTotalAmt.getText().equals(Money.rupees(BigDecimal.ZERO).toString())){
            finish();
        }
    }

    void getAddresses(){


        String user_id=sessionManager.getUserId();

        String url = AppConfig.URL_GET_ADDRESSES+user_id;



        RequestQueue requestQueue = Volley.newRequestQueue(context);

        CustomRequest jsObjRequest   = new CustomRequest(this,true, Request.Method.GET, url, CommonUtilities.buildBlankParams(), CommonUtilities.buildGuestHeaders(),

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        JsonSeparator js = new JsonSeparator(context,response);

                        //  Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();


                        try {
                            if(js.isError()){
                                Toast.makeText(context, js.getMessage(), Toast.LENGTH_LONG).show();

                                if(js.getMessage().equals(Const.MSG_NO_ADDRESS_AVAILABLE)){
                                    setList(CommonUtilities.getObjectsArrayFromJsonArray(new JSONArray(), AddressData.class));

                                }
                            }else{

                                JSONObject registerResponceJson = js.getData() ;
                                JSONArray adsArr =  CommonUtilities.getArrayFromJsonObj(registerResponceJson, Const.KEY_ADDRESSES);

                                setList(CommonUtilities.getObjectsArrayFromJsonArray(adsArr, AddressData.class));

                            }
                        } catch (JSONException e) {
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

    void setList(ArrayList<Class> classArrayList) {



        ArrayList<ItemPickerDialogFragment.Item> pickerItems = new ArrayList<>();
        ArrayList<AddressData> addressDataArrayList = new ArrayList<>();

        for (int i = 0; i<classArrayList.size();i++) {

            try {
                addressDataArrayList.add((AddressData)Class.forName(Const.ClassNameAddressData).cast(classArrayList.get(i)));

                pickerItems.add(new ItemPickerDialogFragment.Item(
                        addressDataArrayList.get(i).getFname()+" "+addressDataArrayList.get(i).getLname()
                        +"\n"+
                        "Address : "+addressDataArrayList.get(i).getAddress()
                                +"\n"+
                        "Phone : "+addressDataArrayList.get(i).getPhone()
                                +"\n"+
                                "Pincode : "+addressDataArrayList.get(i).getPincode(), addressDataArrayList.get(i).getId()));


            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


        }


        ItemPickerDialogFragment dialog = ItemPickerDialogFragment.newInstance(
                "Choose Address",
                pickerItems,
                -1
        );
        dialog.show(getFragmentManager(), "ItemPicker");


//onPickButtonClick();



    }

@Override
    public void onItemSelected(ItemPickerDialogFragment fragment, ItemPickerDialogFragment.Item item, int index) {
        selected_address_id = item.getIntValue();

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    //Yes button clicked

                    placeOrder(selected_address_id);
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    };

    AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
    builder.setMessage("Do you want to place order?").setPositiveButton("Yes", dialogClickListener)
            .setNegativeButton("No", dialogClickListener).show();



    }
    @Override
    public void onResume(){
        super.onResume();
        if (!resumeHasRun) {
            resumeHasRun = true;
            return;
        }

        checkout.performClick();
    }

    void placeOrder(int selected_address_id){


        Map<String, String> params = new HashMap<>();
        params.put("products",CommonUtilities.getJsonStringFromObject(GlobaDataHolder.getGlobaDataHolder().getShoppingList()));
        params.put("address_id",String.valueOf(selected_address_id));



        RequestQueue requestQueue = Volley.newRequestQueue(context);

        CustomRequest jsObjRequest   = new CustomRequest(context,
                false,
                Request.Method.POST,
                AppConfig.URL_PLACE_ORDER,
                params, CommonUtilities.buildHeaders(context),

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        JsonSeparator js = new JsonSeparator(context,response);

                        //  Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();

                        try {
                            if(js.isError()){
                                Toast.makeText(context, js.getMessage(), Toast.LENGTH_LONG).show();
                            }else{

                                //JSONObject registerResponceJson = js.getData() ;
                                Toast.makeText(context, js.getMessage(), Toast.LENGTH_LONG).show();



                                GlobaDataHolder.getGlobaDataHolder()
                                        .getShoppingList().clear();

                                finish();






                            }
                        } catch (JSONException e) {
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
//        AppController.getInstance().addToRequestQueue(jsObjRequest);


        requestQueue.add(jsObjRequest);


    }

}
