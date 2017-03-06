package sourabh.ichiapp.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
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
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sourabh.ichiapp.R;
import sourabh.ichiapp.adaptors.ProductsAdaptor;
import sourabh.ichiapp.app.AppConfig;
import sourabh.ichiapp.app.CustomRequest;
import sourabh.ichiapp.data.GenericCategoryData;
import sourabh.ichiapp.data.ProductData;
import sourabh.ichiapp.helper.CommonUtilities;
import sourabh.ichiapp.helper.Const;
import sourabh.ichiapp.helper.JsonSeparator;

public class ProductsActivity extends AppCompatActivity {


    Context context;

    @BindView(R.id.list)
    ListView listView;


    private ProductsAdaptor productsAdaptor;
    private List<ProductData> productDataList = new ArrayList<ProductData>();
    String retailer_id = "1";
    String category_id = "",offer_image="";
    GenericCategoryData genericCategoryData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        ButterKnife.bind(this);
        context = getApplicationContext();



        genericCategoryData = (GenericCategoryData) getIntent().getExtras().getSerializable(Const.KEY_PRODUCT_CATEGORY_DATA);


        category_id = genericCategoryData.getId().toString();
        offer_image = genericCategoryData.getImage();

        retailer_id = "all";

        productsAdaptor = new ProductsAdaptor(this, productDataList);
        listView.setAdapter(productsAdaptor);

        getProducts(category_id,retailer_id);


    }

    void getProducts(String category_id, String retailer_id)
    {

        String url = AppConfig.URL_GET_PRODUCTS+category_id+"/"+retailer_id;



        RequestQueue requestQueue = Volley.newRequestQueue(context);

        CustomRequest jsObjRequest   = new CustomRequest(this,true, Request.Method.GET, url, CommonUtilities.buildBlankParams(), CommonUtilities.buildHeaders(),

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        JsonSeparator js = new JsonSeparator(context,response);

                        //  Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();


                        try {
                            if(js.isError()){
                                Toast.makeText(context, js.getMessage(), Toast.LENGTH_LONG).show();
                            }else{

                                JSONObject registerResponceJson = js.getData() ;
                                JSONArray adsArr =  CommonUtilities.getArrayFromJsonObj(registerResponceJson, Const.KEY_PRODUCTS);

                                setList(CommonUtilities.getObjectsArrayFromJsonArray(adsArr,ProductData.class));

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

    void setList(ArrayList<Class> classArrayList)
    {
        for (int i = 0; i<classArrayList.size();i++) {

            try {
                productDataList.add( (ProductData) Class.forName(Const.ClassNameProductData).cast(classArrayList.get(i)));

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


        }
        productsAdaptor.notifyDataSetChanged();
    }
}
