package sourabh.ichiapp.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Movie;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

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
import sourabh.ichiapp.data.AdSliderData;
import sourabh.ichiapp.data.ServiceProviderData;
import sourabh.ichiapp.helper.CommonUtilities;
import sourabh.ichiapp.helper.Const;
import sourabh.ichiapp.helper.JsonSeparator;

public class ServiceProvidersActivity extends AppCompatActivity {

    Context context;

    @BindView(R.id.list)
    ListView listView;


    private ServiceProvidersAdaptor serviceProvidersAdaptor;
    private List<ServiceProviderData> serviceProviderDataList = new ArrayList<ServiceProviderData>();
    String city_id = "1";
    String category_id = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_providers);
        ButterKnife.bind(this);
        context = getApplicationContext();

        category_id = String.valueOf(this.getIntent().getIntExtra(Const.KEY_CATEGORY_ID,1));
        city_id = "1";

        serviceProvidersAdaptor = new ServiceProvidersAdaptor(this, serviceProviderDataList,null);
        listView.setAdapter(serviceProvidersAdaptor);

        getServiceProviders(city_id,category_id);
    }

    void getServiceProviders(String city_id,String category_id)
    {

        String url = AppConfig.URL_GET_SERVICEPROVIDERS+city_id+"/"+category_id;



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
                                JSONArray adsArr =  CommonUtilities.getArrayFromJsonObj(registerResponceJson, Const.KEY_SERVICE_PROVIDERS);

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