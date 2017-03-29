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
import sourabh.ichiapp.data.ShoppingCategoryData;
import sourabh.ichiapp.helper.Const;

/**
 * Created by Downloader on 2/23/2017.
 */

public class ShoppingCategoryAdaptor extends BaseAdapter{

    private Context mContext;
    private ArrayList<Class> shoppingCategoriesDataArrayList;
    ShoppingCategoryData shoppingCategories = null;
    // Constructor
    public ShoppingCategoryAdaptor(
            Context context,
            ArrayList<Class> shoppingCategoriesDataArrayList)
    {

        mContext = context;
        this.shoppingCategoriesDataArrayList = shoppingCategoriesDataArrayList;
    }

    @Override
    public int getCount() {
        return shoppingCategoriesDataArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return shoppingCategoriesDataArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        try {
             shoppingCategories = (ShoppingCategoryData) Class.forName(Const.ClassNameShoppingCategoryData).cast(shoppingCategoriesDataArrayList.get(position));
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
                Picasso.with(mContext).load(shoppingCategories.getImage()).fit().into(imageView);

            TextView textViewAndroid = (TextView) gridViewAndroid.findViewById(R.id.android_gridview_text);
            textViewAndroid.setText(shoppingCategories.getName());


//            imageViewAndroid.setImageResource(gridViewImageId[i]);




        } else {
            gridViewAndroid = (View) convertView;
        }





        return gridViewAndroid; 
    }
}
