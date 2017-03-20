package sourabh.ichiapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import sourabh.ichiapp.R;
import sourabh.ichiapp.activities.ProductsActivity;
import sourabh.ichiapp.activities.ServiceProvidersActivity;
import sourabh.ichiapp.data.GenericCategoryData;
import sourabh.ichiapp.data.RetailerCategoryData;
import sourabh.ichiapp.data.ServiceCategoryData;
import sourabh.ichiapp.helper.Const;

/**
 * Created by Downloader on 3/6/2017.
 */

public class SubCategoryDialogFragment extends DialogFragment
{
    ArrayList<GenericCategoryData> subCategoriesGeneric = null;
    ArrayList<ServiceCategoryData>subCategoriesService = null;
    ArrayList<Class>retailerCategoryDataArrayList = null;

    public SubCategoryDialogFragment(ArrayList<GenericCategoryData> subCategoriesGeneric,
                                     ArrayList<ServiceCategoryData> subCategoriesService,
                                     ArrayList<Class> retailerCategoryDataArrayList)
    {
        this.subCategoriesGeneric = subCategoriesGeneric;
        this.subCategoriesService = subCategoriesService;
        this.retailerCategoryDataArrayList = retailerCategoryDataArrayList;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.gridview, container, false);
        GridView gv=(GridView)rootView.findViewById(R.id.grid_view);

        if(subCategoriesService == null){

            gv.setAdapter(new SubCategoryGenericAdaptor(getActivity(), subCategoriesGeneric));

        }else{
            gv.setAdapter(new SubCategoryServiceAdaptor(getActivity(), subCategoriesService));

        }


        WindowManager.LayoutParams wmlp = getDialog().getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER;


        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                if(subCategoriesService == null){

                    Intent i = new Intent(getContext(), ProductsActivity.class);
                    i.putExtra(Const.KEY_PRODUCT_CATEGORY_DATA, subCategoriesGeneric.get(position));
                    startActivity(i);
                }else{
                    Intent i = new Intent(getContext(), ServiceProvidersActivity.class);
                    i.putExtra(Const.KEY_SERVICE_CATEGORY_DATA, subCategoriesService.get(position));
                    startActivity(i);
                }
            }
        });

        return rootView;
    }
}

class SubCategoryGenericAdaptor extends BaseAdapter {

    private Context mContext;
    private List<GenericCategoryData> offerCategoriesDataArrayList;
    GenericCategoryData offerCategoriesData = null;

    // Constructor
    public SubCategoryGenericAdaptor(
            Context context,
            List<GenericCategoryData> offerCategoriesDataArrayList
    ) {

        mContext = context;
        this.offerCategoriesDataArrayList = offerCategoriesDataArrayList;
    }


    @Override
    public int getCount() {
        return offerCategoriesDataArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return offerCategoriesDataArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        try {
            offerCategoriesData = (GenericCategoryData) Class.forName(Const.ClassNameOfferCategoryData).cast(offerCategoriesDataArrayList.get(position));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        View gridViewAndroid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            gridViewAndroid = new View(mContext);


            gridViewAndroid = inflater.inflate(R.layout.gridview_item_shopping, null);

            ImageView imageView = (ImageView) gridViewAndroid.findViewById(R.id.imageview_grid_item);
            Picasso.with(mContext).load(offerCategoriesData.getImage()).fit().into(imageView);
//


            TextView textViewAndroid = (TextView) gridViewAndroid.findViewById(R.id.android_gridview_text);
            textViewAndroid.setText(offerCategoriesData.getName());


//            imageViewAndroid.setImageResource(gridViewImageId[i]);


        } else {
            gridViewAndroid = (View) convertView;
        }


        return gridViewAndroid;
    }


}

    class SubCategoryServiceAdaptor extends BaseAdapter {

        private Context mContext;
        private List<ServiceCategoryData> serviceCategoryDataList;
        ServiceCategoryData serviceCategoryData = null;

        // Constructor
        public SubCategoryServiceAdaptor(
                Context context,
                List<ServiceCategoryData> serviceCategoryDataList
        ) {

            mContext = context;
            this.serviceCategoryDataList = serviceCategoryDataList;
        }


        @Override
        public int getCount() {
            return serviceCategoryDataList.size();
        }

        @Override
        public Object getItem(int position) {
            return serviceCategoryDataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            try {
                serviceCategoryData = (ServiceCategoryData) Class.forName(Const.ClassNameServiceCategoryData).cast(serviceCategoryDataList.get(position));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            View gridViewAndroid;
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {

                gridViewAndroid = new View(mContext);


                gridViewAndroid = inflater.inflate(R.layout.gridview_item, null);

                ImageView imageView = (ImageView) gridViewAndroid.findViewById(R.id.imageview_grid_item);
                Picasso.with(mContext).load(serviceCategoryData.getImage()).fit().into(imageView);
//


                TextView textViewAndroid = (TextView) gridViewAndroid.findViewById(R.id.android_gridview_text);
                textViewAndroid.setText(serviceCategoryData.getName());


//            imageViewAndroid.setImageResource(gridViewImageId[i]);


            } else {
                gridViewAndroid = (View) convertView;
            }


            return gridViewAndroid;
        }}



class RetailerCategoryAdaptor extends BaseAdapter {

    private Context mContext;
    private List<RetailerCategoryData> retailerCategoryDataList;
    RetailerCategoryData retailerCategoryData = null;

    // Constructor
    public RetailerCategoryAdaptor(
            Context context,
            List<RetailerCategoryData> retailerCategoryDataList
    ) {

        mContext = context;
        this.retailerCategoryDataList = retailerCategoryDataList;
    }


    @Override
    public int getCount() {
        return retailerCategoryDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return retailerCategoryDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        try {
            retailerCategoryData = (RetailerCategoryData) Class.forName(Const.ClassNameRetailerCategoryData).cast(retailerCategoryDataList.get(position));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        View gridViewAndroid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            gridViewAndroid = new View(mContext);


            gridViewAndroid = inflater.inflate(R.layout.gridview_item, null);

            ImageView imageView = (ImageView) gridViewAndroid.findViewById(R.id.imageview_grid_item);
            Picasso.with(mContext).load(retailerCategoryData.getImage()).fit().into(imageView);
//


            TextView textViewAndroid = (TextView) gridViewAndroid.findViewById(R.id.android_gridview_text);
            textViewAndroid.setText(retailerCategoryData.getName());


//            imageViewAndroid.setImageResource(gridViewImageId[i]);


        } else {
            gridViewAndroid = (View) convertView;
        }


        return gridViewAndroid;
    }}
