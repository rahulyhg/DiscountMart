package sourabh.ichiapp.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sourabh.ichiapp.R;
import sourabh.ichiapp.adaptors.ProductsAdaptor;
import sourabh.ichiapp.data.GenericCategoryData;
import sourabh.ichiapp.data.GlobaDataHolder;
import sourabh.ichiapp.data.ProductData;

public class CartActivity extends AppCompatActivity {


    Context context;

    @BindView(R.id.list)
    ListView listView;


    private ProductsAdaptor productsAdaptor;
    private List<ProductData> productDataList = new ArrayList<ProductData>();

    GenericCategoryData genericCategoryData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        ButterKnife.bind(this);
        context = getApplicationContext();

        productsAdaptor = new ProductsAdaptor(this, GlobaDataHolder.getGlobaDataHolder().getShoppingList());
        listView.setAdapter(productsAdaptor);


    }
}
