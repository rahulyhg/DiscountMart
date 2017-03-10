package sourabh.ichiapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import sourabh.ichiapp.R;
import sourabh.ichiapp.activities.ProductsActivity;
import sourabh.ichiapp.adaptors.GenericCategoryAdaptor;
import sourabh.ichiapp.app.AppConfig;
import sourabh.ichiapp.app.CustomRequest;
import sourabh.ichiapp.data.AdSliderData;
import sourabh.ichiapp.data.GenericCategoryData;
import sourabh.ichiapp.helper.CommonUtilities;
import sourabh.ichiapp.helper.Const;
import sourabh.ichiapp.helper.JsonSeparator;


public class ShopFragment extends Fragment {



    @BindView(R.id.slider)
    SliderLayout sliderShow;

    @BindView(R.id.grid_view)
    GridView gridView;

    View view;


    ArrayList<Class> adSliderDataArrayList;

    Context context;




    public ShopFragment() {
        // Required empty public constructor
    }

    public ShopFragment(ArrayList<Class> adSliderDataArrayList) {
        // Required empty public constructor

        this.adSliderDataArrayList = adSliderDataArrayList;
        adSliderDataArrayList.size();


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_shop, container, false);


        ButterKnife.bind(this, view);

        showAdSlider();

        context = getActivity();

        getShoppingCategories();

        return view;
    }


    void getShoppingCategories(){

        String url = AppConfig.URL_GET_SHOPPING_CATEGORIES;



        RequestQueue requestQueue = Volley.newRequestQueue(context);

        CustomRequest jsObjRequest   = new CustomRequest(context,true, Request.Method.GET, url, CommonUtilities.buildBlankParams(), CommonUtilities.buildGuestHeaders(),

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
                                JSONArray array =  CommonUtilities.getArrayFromJsonObj(registerResponceJson,Const.KEY_CATEGORIES);

                                setShoppingCategories(CommonUtilities.getObjectsArrayFromJsonArray(array,GenericCategoryData.class));

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

    void setShoppingCategories(final ArrayList<Class> offerCategoriesDataList){


        gridView.setAdapter(new GenericCategoryAdaptor(context,offerCategoriesDataList,true));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                try {
                    GenericCategoryData genericCategoryData = (GenericCategoryData) Class.forName(Const.ClassNameOfferCategoryData).cast(offerCategoriesDataList.get(position));


                    ArrayList<GenericCategoryData> subcategories = (ArrayList<GenericCategoryData>) genericCategoryData.getSubcategories();
                    if(subcategories.size()>0){

                        //showAlertDialog(subcategories);

                        FragmentManager fm = getFragmentManager();
                        SubCategoryDialogFragment dialogFragment = new SubCategoryDialogFragment (subcategories);
                        dialogFragment.show(fm, "Sample Fragment");


                    }else{
                        // Sending image id to FullScreenActivity
                        Intent i = new Intent(context, ProductsActivity.class);
                        // passing array index

                       // String strProductCatData = CommonUtilities.getJsonStringFromObject(genericCategoryData);
                        // i.putExtra(Const.KEY_OFFER_DATA, strOfferData);
                        i.putExtra(Const.KEY_PRODUCT_CATEGORY_DATA, genericCategoryData);


                        startActivity(i);
                    }

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }


            }
        });


    }




    void showAdSlider(){


        for (int i = 0; i<adSliderDataArrayList.size();i++) {

            try {
                AdSliderData adSliderData = (AdSliderData) Class.forName(Const.ClassNameAdSliderData).cast(adSliderDataArrayList.get(i));
                sliderShow.addSlider(new  TextSliderView(getActivity()).image(adSliderData.getImage()));

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


        }

    }


}







