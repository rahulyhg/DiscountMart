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
import sourabh.ichiapp.activities.RetailersActivity;
import sourabh.ichiapp.data.OfferCategoryData;
import sourabh.ichiapp.helper.CommonUtilities;
import sourabh.ichiapp.helper.Const;

/**
 * Created by Downloader on 3/6/2017.
 */

public class SubCategoryDialogFragment extends DialogFragment
{
    ArrayList<OfferCategoryData>subCategories = null;

    public SubCategoryDialogFragment(ArrayList<OfferCategoryData>subCategories)
    {
        this.subCategories = subCategories;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.gridview, container, false);
        GridView gv=(GridView)rootView.findViewById(R.id.grid_view);

        gv.setAdapter(new SubCategoryAdaptor(getActivity(),subCategories));


        WindowManager.LayoutParams wmlp = getDialog().getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER;


        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            }
        });

        return rootView;
    }
}

class SubCategoryAdaptor extends BaseAdapter {

    private Context mContext;
    private List<OfferCategoryData> offerCategoriesDataArrayList;
    OfferCategoryData offerCategoriesData = null;

    // Constructor
    public SubCategoryAdaptor(
            Context context,
            List<OfferCategoryData> offerCategoriesDataArrayList
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
            offerCategoriesData = (OfferCategoryData) Class.forName(Const.ClassNameOfferCategoryData).cast(offerCategoriesDataArrayList.get(position));
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