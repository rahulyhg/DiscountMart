package sourabh.ichiapp.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import sourabh.ichiapp.R;
import sourabh.ichiapp.data.GenericCategoryData;
import sourabh.ichiapp.helper.Const;

/**
 * Created by Downloader on 2/23/2017.
 */

public class GenericCategoryAdaptor extends BaseAdapter{

    private Context mContext;
    private ArrayList<Class> offerCategoriesDataArrayList;
    GenericCategoryData offerCategoriesData = null;
    Boolean isShopping;
    // Constructor
    public GenericCategoryAdaptor(
            Context context,
            ArrayList<Class> offerCategoriesDataArrayList,
            Boolean isShopping){

        mContext = context;
        this.offerCategoriesDataArrayList =offerCategoriesDataArrayList;
        this.isShopping = isShopping;
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

            if(isShopping){
                gridViewAndroid = inflater.inflate(R.layout.gridview_item_shopping, null);

                ImageView imageView = (ImageView) gridViewAndroid.findViewById(R.id.imageview_grid_item);
                Picasso.with(mContext).load(offerCategoriesData.getImage()).fit().into(imageView);
//
            }else{
                gridViewAndroid = inflater.inflate(R.layout.gridview_item, null);

            }

            TextView textViewAndroid = (TextView) gridViewAndroid.findViewById(R.id.android_gridview_text);
            textViewAndroid.setText(offerCategoriesData.getName());


//            imageViewAndroid.setImageResource(gridViewImageId[i]);




        } else {
            gridViewAndroid = (View) convertView;
        }





        return gridViewAndroid; 
    }
}
