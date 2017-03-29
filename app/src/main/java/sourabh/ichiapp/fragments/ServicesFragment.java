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
import sourabh.ichiapp.activities.ServiceProvidersActivity;
import sourabh.ichiapp.adaptors.GenericCategoryAdaptor;
import sourabh.ichiapp.data.AdSliderData;
import sourabh.ichiapp.data.GenericCategoryData;
import sourabh.ichiapp.helper.Const;


public class ServicesFragment extends Fragment {

    @BindView(R.id.slider)
    SliderLayout sliderShow;

    @BindView(R.id.grid_view)
    GridView gridView;

    ArrayList<Class> adSliderDataArrayList;
    ArrayList<Class> genericCategoriesArrayList;

    Context context;

    public ServicesFragment() {
        // Required empty public constructor
    }

    public ServicesFragment(ArrayList<Class> adSliderDataArrayList,
                            ArrayList<Class> genericCategoriesArrayList)
    {

            // Required empty public constructor

            this.adSliderDataArrayList = adSliderDataArrayList;
            this.genericCategoriesArrayList = genericCategoriesArrayList;



    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_services, container, false);

        ButterKnife.bind(this, view);
        showAdSlider();
        context = getActivity();

       // getServiceCategories();
        setServiceCategories(genericCategoriesArrayList);
        return view;
    }

    void showAdSlider() {


        for (int i = 0; i < adSliderDataArrayList.size(); i++) {

            try {
                AdSliderData adSliderData = (AdSliderData) Class.forName(Const.ClassNameAdSliderData).cast(adSliderDataArrayList.get(i));
                sliderShow.addSlider(new TextSliderView(getActivity()).image(adSliderData.getImage()));

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


        }

    }

//    void getServiceCategories(){
//
//        String url = AppConfig.URL_GET_SERVICE_CATEGORIES;
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
//                                setServiceCategories(CommonUtilities.getObjectsArrayFromJsonArray(array,ServiceCategoryData.class));
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


    void setServiceCategories(final ArrayList<Class> genericCategoriesDataArrayList){


        gridView.setAdapter(new GenericCategoryAdaptor(context,genericCategoriesDataArrayList));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                try {
                  GenericCategoryData genericCategoryData = (GenericCategoryData) Class.forName(Const.ClassNameGenericCategoryData).cast(genericCategoriesDataArrayList.get(position));
                    ArrayList<GenericCategoryData> subcategories = (ArrayList<GenericCategoryData>) genericCategoryData.getSubcategories();


                    if(subcategories.size()>0){

                        //showAlertDialog(subcategories);

                        FragmentManager fm = getFragmentManager();
                        SubCategoryDialogFragment dialogFragment = new SubCategoryDialogFragment (null,subcategories,true);
                        dialogFragment.show(fm, "Sample Fragment");


                    }else {

                        // Sending image id to FullScreenActivity
                        Intent i = new Intent(context, ServiceProvidersActivity.class);
                        // passing array index
                        i.putExtra(Const.KEY_CATEGORY_ID, genericCategoryData.getId());
                        startActivity(i);
                    }


                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }


            }
        });


    }


}