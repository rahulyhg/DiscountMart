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
import sourabh.ichiapp.adaptors.ServiceProvidersAdaptor;
import sourabh.ichiapp.app.AppConfig;
import sourabh.ichiapp.app.CustomRequest;
import sourabh.ichiapp.data.GenericCategoryData;
import sourabh.ichiapp.data.ServiceProviderData;
import sourabh.ichiapp.helper.CommonUtilities;
import sourabh.ichiapp.helper.Const;
import sourabh.ichiapp.helper.JsonSeparator;

public class RetailersActivity extends AppCompatActivity {


    Context context;

    @BindView(R.id.list)
    ListView listView;


    private ServiceProvidersAdaptor serviceProvidersAdaptor;
    private List<ServiceProviderData> serviceProviderDataList = new ArrayList<ServiceProviderData>();
    String city_id = "1";
    String category_id = "",offer_image="";
    GenericCategoryData genericCategoryData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailers);

        ButterKnife.bind(this);
        context = getApplicationContext();



        genericCategoryData = (GenericCategoryData) getIntent().getExtras().getSerializable(Const.KEY_OFFER_DATA);


        category_id = genericCategoryData.getId().toString();
        offer_image = genericCategoryData.getImage();

        city_id = "1";

        serviceProvidersAdaptor = new ServiceProvidersAdaptor(this, serviceProviderDataList, genericCategoryData);
        listView.setAdapter(serviceProvidersAdaptor);

        getRetailers(city_id,category_id);



    }

    void getRetailers(String city_id, String category_id)
    {

        String url = AppConfig.URL_GET_RETAILERS+city_id+"/"+category_id;



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
                            }else{

                                JSONObject registerResponceJson = js.getData() ;
                                JSONArray adsArr =  CommonUtilities.getArrayFromJsonObj(registerResponceJson, Const.KEY_RETAILERS);

                                setList(CommonUtilities.getObjectsArrayFromJsonArray(adsArr,ServiceProviderData.class));

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
                serviceProviderDataList.add( (ServiceProviderData) Class.forName(Const.ClassNameServiceProviderData).cast(classArrayList.get(i)));

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


        }
        serviceProvidersAdaptor.notifyDataSetChanged();
    }

}
