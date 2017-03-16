package sourabh.ichiapp.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import sourabh.ichiapp.R;
import sourabh.ichiapp.data.ServiceCategoryData;
import sourabh.ichiapp.helper.Const;

/**
 * Created by Downloader on 2/23/2017.
 */

public class ServiceCategoryAdaptor extends BaseAdapter{

    private Context mContext;
    private ArrayList<Class> serviceCategoriesDataArrayList;
    ServiceCategoryData serviceCategoryData = null;
    // Constructor
    public ServiceCategoryAdaptor(Context context, ArrayList<Class> serviceCategoriesDataArrayList){

        mContext = context;
        this.serviceCategoriesDataArrayList=serviceCategoriesDataArrayList;
    }

    @Override
    public int getCount() {
        return serviceCategoriesDataArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return serviceCategoriesDataArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        try {
             serviceCategoryData = (ServiceCategoryData) Class.forName(Const.ClassNameServiceCategoriesData).cast(serviceCategoriesDataArrayList.get(position));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        View gridViewAndroid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            gridViewAndroid = new View(mContext);
            gridViewAndroid = inflater.inflate(R.layout.gridview_item, null);
            TextView textViewAndroid = (TextView) gridViewAndroid.findViewById(R.id.android_gridview_text);
          //  ImageView imageViewAndroid = (ImageView) gridViewAndroid.findViewById(R.id.android_gridview_image);
            textViewAndroid.setText(serviceCategoryData.getName());
//            imageViewAndroid.setImageResource(gridViewImageId[i]);




        } else {
            gridViewAndroid = (View) convertView;
        }





        return gridViewAndroid; 
    }
}
