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
import sourabh.ichiapp.activities.RetailersActivity;
import sourabh.ichiapp.activities.ServiceProvidersActivity;
import sourabh.ichiapp.data.GenericCategoryData;
import sourabh.ichiapp.data.ShoppingCategoryData;
import sourabh.ichiapp.data.ServiceCategoryData;
import sourabh.ichiapp.helper.Const;

/**
 * Created by Downloader on 3/6/2017.
 */

public class SubCategoryDialogFragment extends DialogFragment
{
    ArrayList<ShoppingCategoryData> subShoppingCategoriesList = null;
    ArrayList<GenericCategoryData> subGenericCategoriesList = null;
    boolean isService= false;

    public SubCategoryDialogFragment(ArrayList<ShoppingCategoryData> subShoppingCategoriesList,
                                     ArrayList<GenericCategoryData> subGenericCategoriesList,
                                     boolean isService
    )
    {
        this.subShoppingCategoriesList = subShoppingCategoriesList;
        this.subGenericCategoriesList = subGenericCategoriesList;
        this.isService = isService;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.gridview, container, false);
        GridView gv=(GridView)rootView.findViewById(R.id.grid_view);

        if(subGenericCategoriesList == null){

            gv.setAdapter(new SubCategoryShoppingAdaptor(getActivity(), subShoppingCategoriesList));

        }else{
            gv.setAdapter(new SubGenericCategoryAdaptor(getActivity(), subGenericCategoriesList));

        }


        WindowManager.LayoutParams wmlp = getDialog().getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER;


        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                if(subGenericCategoriesList == null){

                    Intent i = new Intent(getContext(), ProductsActivity.class);
                    i.putExtra(Const.KEY_PRODUCT_CATEGORY_DATA, subShoppingCategoriesList.get(position));
                    startActivity(i);
                }



                if(isService){
                    Intent i = new Intent(getContext(), ServiceProvidersActivity.class);
                    i.putExtra(Const.KEY_GENERIC_CATEGORY_DATA, subGenericCategoriesList.get(position));
                    startActivity(i);
                }
                else{
                    Intent i = new Intent(getContext(), RetailersActivity.class);
                    i.putExtra(Const.KEY_GENERIC_CATEGORY_DATA, subGenericCategoriesList.get(position));
                    startActivity(i);
                }
            }
        });

        return rootView;
    }
}

class SubCategoryShoppingAdaptor extends BaseAdapter {

    private Context mContext;
    private List<ShoppingCategoryData> offerCategoriesDataArrayList;
    ShoppingCategoryData offerCategoriesData = null;

    // Constructor
    public SubCategoryShoppingAdaptor(
            Context context,
            List<ShoppingCategoryData> offerCategoriesDataArrayList
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
            offerCategoriesData = (ShoppingCategoryData) Class.forName(Const.ClassNameOfferCategoryData).cast(offerCategoriesDataArrayList.get(position));
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

    class SubGenericCategoryAdaptor extends BaseAdapter {

        private Context mContext;
        private List<GenericCategoryData> genericCategoryDataList;
        GenericCategoryData genericCategoryData = null;

        // Constructor
        public SubGenericCategoryAdaptor(
                Context context,
                List<GenericCategoryData> genericCategoryDataList
        ) {

            mContext = context;
            this.genericCategoryDataList = genericCategoryDataList;
        }


        @Override
        public int getCount() {
            return genericCategoryDataList.size();
        }

        @Override
        public Object getItem(int position) {
            return genericCategoryDataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            try {
                genericCategoryData = (GenericCategoryData) Class.forName(Const.ClassNameGenericCategoryData).cast(genericCategoryDataList.get(position));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            View gridViewAndroid;
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {

                gridViewAndroid = new View(mContext);


                gridViewAndroid = inflater.inflate(R.layout.gridview_item, null);

//                ImageView imageView = (ImageView) gridViewAndroid.findViewById(R.id.imageview_grid_item);
//                Picasso.with(mContext).load(genericCategoryData.getImage()).fit().into(imageView);
//


                TextView textViewAndroid = (TextView) gridViewAndroid.findViewById(R.id.android_gridview_text);
                textViewAndroid.setText(genericCategoryData.getName());


//            imageViewAndroid.setImageResource(gridViewImageId[i]);


            } else {
                gridViewAndroid = (View) convertView;
            }


            return gridViewAndroid;
        }}




