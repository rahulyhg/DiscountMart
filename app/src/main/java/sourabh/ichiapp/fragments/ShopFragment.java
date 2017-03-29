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

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import sourabh.ichiapp.R;
import sourabh.ichiapp.activities.ProductsActivity;
import sourabh.ichiapp.adaptors.ShoppingCategoryAdaptor;
import sourabh.ichiapp.data.AdSliderData;
import sourabh.ichiapp.data.ShoppingCategoryData;
import sourabh.ichiapp.helper.Const;


public class ShopFragment extends Fragment {



    @BindView(R.id.slider)
    SliderLayout sliderShow;

    @BindView(R.id.grid_view)
    GridView gridView;

    View view;


    ArrayList<Class> adSliderDataArrayList;
    ArrayList<Class> shoppingCategoriesList;

    Context context;




    public ShopFragment() {
        // Required empty public constructor
    }

    public ShopFragment(ArrayList<Class> adSliderDataArrayList,
                        ArrayList<Class> shoppingCategoriesList)
    {
        // Required empty public constructor

        this.adSliderDataArrayList = adSliderDataArrayList;
        this.shoppingCategoriesList = shoppingCategoriesList;


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
        setShoppingCategories(shoppingCategoriesList);

//        getShoppingCategories();

        return view;
    }


//    void getShoppingCategories(){
//
//        String url = AppConfig.URL_GET_SHOPPING_CATEGORIES;
//
//
//
//        RequestQueue requestQueue = Volley.newRequestQueue(context);
//
//        CustomRequest jsObjRequest   = new CustomRequest(context,true, Request.Method.GET, url, CommonUtilities.buildBlankParams(), CommonUtilities.buildGuestHeaders(),
//
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//
//                        JsonSeparator js = new JsonSeparator(context,response);
//
//                        //  Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
//
//                        try {
//                            if(js.isError()){
//                                Toast.makeText(context, js.getMessage(), Toast.LENGTH_LONG).show();
//                            }else{
//
//                                JSONObject registerResponceJson = js.getData() ;
//                                JSONArray array =  CommonUtilities.getArrayFromJsonObj(registerResponceJson,Const.KEY_SHOPPING_CATEGORIES);
//
//                                setShoppingCategories(CommonUtilities.getObjectsArrayFromJsonArray(array,ShoppingCategoryData.class));
//
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                        try {
//                            throw new IOException("Post failed with error code " + error.getMessage());
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//
//
//                        Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
//
//                    }
//                });
//
//        requestQueue.add(jsObjRequest);
//    }

    void setShoppingCategories(final ArrayList<Class> offerCategoriesDataList){


        gridView.setAdapter(new ShoppingCategoryAdaptor(context,offerCategoriesDataList));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                try {
                    ShoppingCategoryData shoppingCategoryData = (ShoppingCategoryData) Class.forName(Const.ClassNameOfferCategoryData).cast(offerCategoriesDataList.get(position));


                    ArrayList<ShoppingCategoryData> subcategories = (ArrayList<ShoppingCategoryData>) shoppingCategoryData.getSubcategories();
                    if(subcategories.size()>0){

                        //showAlertDialog(subcategories);

                        FragmentManager fm = getFragmentManager();
                        SubCategoryDialogFragment dialogFragment = new SubCategoryDialogFragment (subcategories,null,false);
                        dialogFragment.show(fm, "Sample Fragment");


                    }else{
                        // Sending image id to FullScreenActivity
                        Intent i = new Intent(context, ProductsActivity.class);
                        // passing array index

                       // String strProductCatData = CommonUtilities.getJsonStringFromObject(shoppingCategoryData);
                        // i.putExtra(Const.KEY_OFFER_DATA, strOfferData);
                        i.putExtra(Const.KEY_PRODUCT_CATEGORY_DATA, shoppingCategoryData);


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

               String img = adSliderData.getImage();
                sliderShow.addSlider(new  TextSliderView(getActivity()).image(adSliderData.getImage()));

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


        }

    }


}







