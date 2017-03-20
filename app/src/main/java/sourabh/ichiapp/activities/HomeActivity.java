package sourabh.ichiapp.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.context.IconicsLayoutInflater;
import com.mikepenz.itemanimators.AlphaCrossFadeAnimator;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.ExpandableDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondarySwitchDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryToggleDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.SwitchDrawerItem;
import com.mikepenz.materialdrawer.model.ToggleDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sourabh.ichiapp.R;
import sourabh.ichiapp.app.AppConfig;
import sourabh.ichiapp.app.CustomRequest;
import sourabh.ichiapp.data.AdSliderData;
import sourabh.ichiapp.data.GlobaDataHolder;
import sourabh.ichiapp.data.ProductData;
import sourabh.ichiapp.data.RetailerCategoryData;
import sourabh.ichiapp.fragments.ShopFragment;
import sourabh.ichiapp.fragments.ServicesFragment;
import sourabh.ichiapp.fragments.DiscountFragment;
import sourabh.ichiapp.helper.CommonUtilities;
import sourabh.ichiapp.helper.Const;
import sourabh.ichiapp.helper.JsonSeparator;
import sourabh.ichiapp.helper.PreferenceHelper;
import sourabh.ichiapp.helper.SessionManager;
import sourabh.ichiapp.helper.TinyDB;

import static sourabh.ichiapp.R.id.toolbar;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {



    private AccountHeader headerResult = null;
    private Drawer result = null;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tabs)
    TabLayout tabLayout;

    Context context;
    Button BtnCartCount;
    String cart_count;

    SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        ButterKnife.bind(this);
        context = getApplicationContext();


        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        sessionManager =  new SessionManager(this);

        if (!sessionManager.isLoggedIn()) {
            logoutUser();
        }else{


            GlobaDataHolder.getGlobaDataHolder().setShoppingList(
                    new TinyDB(getApplicationContext()).getListObject(
                            PreferenceHelper.MY_CART_LIST_LOCAL, ProductData.class));


            updateCartCount(GlobaDataHolder.getGlobaDataHolder().getShoppingList().size());

            setupDrawer();
            getAdSliders();
        }



//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();
//
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);






    }

    private void setupViewPager(ViewPager viewPager,
                                ArrayList<Class> adSliderDataArrayList,
                                ArrayList<Class> retailerCategoriesArrayList)
    {




        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());


        adapter.addFragment(new ShopFragment(adSliderDataArrayList), "Home Delivery Products");
        adapter.addFragment(new DiscountFragment(adSliderDataArrayList ,retailerCategoriesArrayList), "Big Discount Coupons");
        adapter.addFragment(new ServicesFragment(adSliderDataArrayList), "Service Providers");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }



    void getAdSliders(){

        String url = AppConfig.URL_GET_ADSLIDERS;



        RequestQueue requestQueue = Volley.newRequestQueue(context);

        CustomRequest jsObjRequest   = new CustomRequest(this,true, Request.Method.GET, url, CommonUtilities.buildBlankParams(), CommonUtilities.buildGuestHeaders(),

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
                                JSONArray adsArr =  CommonUtilities.getArrayFromJsonObj(registerResponceJson,Const.KEY_ADS);

                                JSONArray retailerCatArr =  CommonUtilities.getArrayFromJsonObj(registerResponceJson,Const.KEY_RETAILER_CATEGORIES);

                                setTabs(CommonUtilities.getObjectsArrayFromJsonArray(adsArr,AdSliderData.class),
                                        CommonUtilities.getObjectsArrayFromJsonArray(retailerCatArr,RetailerCategoryData.class));

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

    void setTabs(ArrayList<Class> adSliderDataArrayList, ArrayList<Class> retailerCategoriesArrayList)
    {

        setupViewPager(viewPager,adSliderDataArrayList,retailerCategoriesArrayList);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.home, menu);
//        return true;

        menu.clear();
        getMenuInflater().inflate(R.menu.home, menu);

        if(GlobaDataHolder.getGlobaDataHolder().getShoppingList().size() > 0){

            MenuItem item = menu.findItem(R.id.cart_count);
            MenuItemCompat.setActionView(item, R.layout.cart_update_count);
            View view = MenuItemCompat.getActionView(item);
            BtnCartCount = (Button)view.findViewById(R.id.btn_cart_count);
            BtnCartCount.setText(String.valueOf(cart_count));


            BtnCartCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(),CartActivity.class));
                }
            });
        }


        return true;
    }
    private void updateCartCount(int count){
        cart_count = count+"";
        supportInvalidateOptionsMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.cart_count) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_retailers) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Store Shopping Cart in DB
        new TinyDB(getApplicationContext()).putListObject(
                PreferenceHelper.MY_CART_LIST_LOCAL, GlobaDataHolder
                        .getGlobaDataHolder().getShoppingList());
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateCartCount(GlobaDataHolder.getGlobaDataHolder().getShoppingList().size());

    }

    private void logoutUser() {

        sessionManager.clearAll();
        sessionManager.setLogin(false);


        // Launching the login activity
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    void setupDrawer() {


        PrimaryDrawerItem home = new PrimaryDrawerItem().
                                            withName("Home").
                                        //withDescription(R.string.drawer_item_compact_header_desc).
                                        withIcon(new IconicsDrawable(this)
                                                .icon(FontAwesome.Icon.faw_home)
//                                                .color(Color.RED)
                                                .sizeDp(24)).
                                        withIdentifier(1);



        PrimaryDrawerItem profile = new PrimaryDrawerItem().
                withName("Profile").
                //withDescription(R.string.drawer_item_compact_header_desc).
                        withIcon(new IconicsDrawable(this)
                        .icon(GoogleMaterial.Icon.gmd_account_circle)
//                                                .color(Color.RED)
                        .sizeDp(24)).
                        withIdentifier(3);




        SecondaryDrawerItem about_us = new SecondaryDrawerItem()
                                            .withIdentifier(2)
                                            .withName("About Us")
                .withIcon(new IconicsDrawable(this)
                        .icon(GoogleMaterial.Icon.gmd_assignment)
//                                                .color(Color.RED)
                        .sizeDp(24))
                .withIdentifier(4);


        SecondaryDrawerItem contact_us = new SecondaryDrawerItem()
                .withIdentifier(2)
                .withName("Contact Us")
                .withIcon(new IconicsDrawable(this)
                        .icon(GoogleMaterial.Icon.gmd_contact_mail)
//                                                .color(Color.RED)
                        .sizeDp(24))
                .withIdentifier(5);



        PrimaryDrawerItem logout = new PrimaryDrawerItem().
                withName("Log Out").
                //withDescription(R.string.drawer_item_compact_header_desc).
                        withIcon(new IconicsDrawable(this)
                        .icon(CommunityMaterial.Icon.cmd_logout)
//                                                .color(Color.RED)
                        .sizeDp(24)).
                        withIdentifier(6).
                        withSelectable(false);


        ExpandableDrawerItem retailers =  new ExpandableDrawerItem().withName("Retailers").
                                withIcon(new IconicsDrawable(this)
                                        .icon(FontAwesome.Icon.faw_list_alt)
//                                                .color(Color.RED)
                                        .sizeDp(24)).
        withIdentifier(2).
                withSelectable(false).
                withSubItems
                        (
                                new SecondaryDrawerItem()
                                        .withName("CollapsableItem")
                                        .withLevel(2)
//                                                    .withIcon(new IconicsDrawable(this)
//                                                            .icon(FontAwesome.Icon.faw_list_alt)
////                                                .color(Color.RED)
//                                                            .sizeDp(24))
                                        .withIdentifier(2001),

                                new SecondaryDrawerItem().
                                        withName("CollapsableItem 2").
                                        withLevel(2).
//                                                        withIcon(GoogleMaterial.Icon.gmd_8tracks).
                                        withIdentifier(2002)
                        );







        PrimaryDrawerItem retailers_home = new PrimaryDrawerItem().
                withName("Retailer's Home").
                //withDescription(R.string.drawer_item_compact_header_desc).
                        withIcon(new IconicsDrawable(this)
                        .icon(CommunityMaterial.Icon.cmd_home_variant)
//                                                .color(Color.RED)
                        .sizeDp(24)).
                        withIdentifier(7);



        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.gradient_black_bg)
                .addProfiles(
                        new ProfileDrawerItem().
                                withName(sessionManager.getFname()+" "+sessionManager.getLname()).
                                withEmail(sessionManager.getPhone()).
                                withTextColor(getResources().getColor(R.color.white))
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();


        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withSliderBackgroundColor(getResources().getColor(R.color.md_blue_grey_100))
                .withHasStableIds(true)
                .withItemAnimator(new AlphaCrossFadeAnimator())
                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
                .addDrawerItems(
                            home, retailers, profile,
                        new DividerDrawerItem(),
                        about_us, contact_us,
                         new DividerDrawerItem(),
                        logout

        )
                .withShowDrawerOnFirstLaunch(true)
                .addStickyDrawerItems(retailers_home)
//                .withShowDrawerUntilDraggedOpened(true)


                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        //check if the drawerItem is set.
                        //there are different reasons for the drawerItem to be null
                        //--> click on the header
                        //--> click on the footer
                        //those items don't contain a drawerItem

                        if (drawerItem != null) {
                            Intent intent = null;
                            if (drawerItem.getIdentifier() == 1) {
//                                intent = new Intent(HomeActivity.this, CompactHeaderDrawerActivity.class);
                            } else if (drawerItem.getIdentifier() == 2) {
//                                intent = new Intent(DrawerActivity.this, ActionBarActivity.class);
                            } else if (drawerItem.getIdentifier() == 3) {
                                intent = new Intent(HomeActivity.this, ProfileActivity.class);
                            } else if (drawerItem.getIdentifier() == 4) {
                                intent = new Intent(HomeActivity.this, AboutUsActivity.class);
                            } else if (drawerItem.getIdentifier() == 5) {
                                intent = new Intent(HomeActivity.this, ContactUsActivity.class);
                            } else if (drawerItem.getIdentifier() == 6) {
                                    logoutUser();
                            } else if (drawerItem.getIdentifier() == 7) {
//                                intent = new Intent(HomeActivity.this, FullscreenDrawerActivity.class);
                            }
                            if (intent != null) {
                                HomeActivity.this.startActivity(intent);
                            }
                        }

                        return false;
                    }
                })




                .build();




    }

















}
