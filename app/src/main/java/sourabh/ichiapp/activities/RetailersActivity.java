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
import sourabh.ichiapp.adaptors.GenericListAdaptor;
import sourabh.ichiapp.app.AppConfig;
import sourabh.ichiapp.app.CustomRequest;
import sourabh.ichiapp.data.GenericCategoryData;
import sourabh.ichiapp.data.GenericData;
import sourabh.ichiapp.data.ShoppingCategoryData;
import sourabh.ichiapp.helper.CommonUtilities;
import sourabh.ichiapp.helper.Const;
import sourabh.ichiapp.helper.JsonSeparator;

public class RetailersActivity extends AppCompatActivity {


    Context context;

    @BindView(R.id.list)
    ListView listView;


    private GenericListAdaptor genericListAdaptor;
    private List<GenericData> genericDataList = new ArrayList<GenericData>();
    String city_id = "1";
    String category_id = "";
//            .offer_image="";

    String searchQuery = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailers);

        ButterKnife.bind(this);
        context = getApplicationContext();


        searchQuery = getIntent().getStringExtra(Const.KEY_SEARCH_QUERY);

        GenericCategoryData genericCategoryData = (GenericCategoryData) getIntent().getExtras().getSerializable(Const.KEY_GENERIC_CATEGORY_DATA);

        if(genericCategoryData != null){
            category_id = genericCategoryData.getId().toString();
        }

        city_id = "1";

        genericListAdaptor = new GenericListAdaptor(this, genericDataList,false);
        listView.setAdapter(genericListAdaptor);

        getRetailers(city_id,category_id, searchQuery);



    }

    void getRetailers(String city_id, String category_id, String searchQuery)
    {
        String url;

        if(searchQuery != null && !searchQuery.isEmpty())
        {
            url = AppConfig.URL_GET_RETAILERS+city_id+"/null/"+searchQuery;

        }else{
            url = AppConfig.URL_GET_RETAILERS+city_id+"/"+category_id+"/null";
        }



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

                                setList(CommonUtilities.getObjectsArrayFromJsonArray(adsArr,GenericData.class));

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
                genericDataList.add( (GenericData) Class.forName(Const.ClassNameServiceProviderData).cast(classArrayList.get(i)));

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


        }

        genericListAdaptor.notifyDataSetChanged();
    }

}
