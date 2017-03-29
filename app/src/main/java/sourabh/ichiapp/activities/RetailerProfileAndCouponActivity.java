package sourabh.ichiapp.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import sourabh.ichiapp.R;
import sourabh.ichiapp.app.AppConfig;
import sourabh.ichiapp.app.AppController;
import sourabh.ichiapp.app.CustomRequest;
import sourabh.ichiapp.data.AdSliderData;
import sourabh.ichiapp.data.ShoppingCategoryData;
import sourabh.ichiapp.data.GenericData;
import sourabh.ichiapp.helper.CommonUtilities;
import sourabh.ichiapp.helper.Const;
import sourabh.ichiapp.helper.JsonSeparator;

import static sourabh.ichiapp.R.id.address;
import static sourabh.ichiapp.R.id.phone;

public class RetailerProfileAndCouponActivity extends AppCompatActivity {


    @BindView(R.id.name)
    TextView txtName;
    @BindView(address)
    TextView txtAddress;
    @BindView(phone)
    TextView txtPhone;
    @BindView(R.id.description)
    TextView description;
    @BindView(R.id.btnRequestCoupon)
    Button btnRequestCoupon;


    //    String offer_image,
    String coupon_code, coupon_id;
    @BindView(R.id.slider_banner)
    SliderLayout slider_banner;

    Context context;
    GenericData genericData;

    boolean isService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_profile_and_coupon);
        ButterKnife.bind(this);
        context = getApplicationContext();


        genericData = CommonUtilities.getObjectFromJsonString(getIntent().getStringExtra(Const.KEY_RETAILERS), GenericData.class);

        isService = getIntent().getBooleanExtra(Const.KEY_IS_SERVICE, false);
        setViews();


    }


    void setViews() {

        String mMessage_pic_url = genericData.getBanner1();

        ImageLoader imageLoader = AppController.getInstance().getImageLoader();


        txtName.setText(genericData.getName());
        txtAddress.setText(genericData.getAddress());
        txtPhone.setText(genericData.getPhone());
        description.setVisibility(View.VISIBLE);
        description.setText(genericData.getDescription());


        if (isService)
            btnRequestCoupon.setText("Call : " + genericData.getPhone());
        else
            btnRequestCoupon.setText(genericData.getOffer_name());


        btnRequestCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (isService) {
                    requestPermissionAndCall();
                } else {
                    btnRequestCoupon.setText(genericData.getOffer_name());


                    new LovelyStandardDialog(RetailerProfileAndCouponActivity.this)
                            .setTopColorRes(R.color.holo_blue_dark)
                            .setButtonsColorRes(R.color.colorAccent)
                            .setTitle("Confirm")
                            .setMessage("Do you want to avail this coupon?")
                            .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    requestCoupon();
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .show();

                }
            }

        });


//        Picasso.with(this.context)
//                .load(offer_image)
//                .error(android.R.drawable.stat_notify_error)
//                .fit()
//                .placeholder( R.drawable.progress_animation )
//
//                .into(imageCoupon, new Callback() {
//                    @Override
//                    public void onSuccess() {
//
//                    }
//
//                    @Override
//                    public void onError() {
//
//                    }
//                });


        slider_banner.addSlider(new TextSliderView(this).image(genericData.getBanner1()));
        slider_banner.addSlider(new TextSliderView(this).image(genericData.getBanner2()));
        slider_banner.addSlider(new TextSliderView(this).image(genericData.getBanner3()));


    }

    void requestPermissionAndCall() {

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + genericData.getPhone()));
        startActivity(intent);

    }




    void requestCoupon(){


        Map<String, String> params = new HashMap<String, String>();
        params.put("id_offer", genericData.getId().toString());
        params.put("id_retailer", genericData.getId().toString());
        params.put("id_user","1");




        RequestQueue requestQueue = Volley.newRequestQueue(context);

        CustomRequest jsObjRequest   = new CustomRequest(this,true,
                Request.Method.POST,
                AppConfig.URL_CREATE_COUPON_REQUEST,
                params,
                CommonUtilities.buildGuestHeaders(),

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        JsonSeparator js = new JsonSeparator(context,response);

                        //  Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();

                        try {
                            if(js.isError()){
                                Toast.makeText(context, js.getMessage(), Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(context, js.getMessage(), Toast.LENGTH_LONG).show();


                                coupon_code = js.getData().getJSONObject(Const.KEY_COUPON).getString(Const.KEY_COUPON_CODE);
                                coupon_id = js.getData().getJSONObject(Const.KEY_COUPON).getString(Const.KEY_ID);


                                showCodeDialog("Enter Coupon Code");






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





    public void showCodeDialog( String message) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.edit_custom);
        edt.setInputType(InputType.TYPE_CLASS_NUMBER);
        dialogBuilder.setTitle(message);
        dialogBuilder.setCancelable(false);
       // dialogBuilder.setMessage("Enter text below");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //do something with edt.getText().toString();

                if(edt.getText().toString().equals(coupon_code)){
                    dialog.dismiss();

                    validateCoupon();

                }else {
                    dialog.dismiss();

                   // CommonUtilities.showAlertDialog(RetailerProfileAndCouponActivity.this,"Invalid","Coupon code incorrect",false);
                    showCodeDialog("Invalid Coupon code, Try again");
                }

            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
                dialog.dismiss();
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }




    void validateCoupon(){






        RequestQueue requestQueue = Volley.newRequestQueue(context);

        CustomRequest jsObjRequest   = new CustomRequest(this,true,
                Request.Method.PUT,
                AppConfig.URL_VALIDATE_COUPON+coupon_id,
                CommonUtilities.buildBlankParams(),
                CommonUtilities.buildGuestHeaders(),

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        JsonSeparator js = new JsonSeparator(context,response);

                        //  Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();

                        try {
                            if(js.isError()){
                                Toast.makeText(context, js.getMessage(), Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(context, js.getMessage(), Toast.LENGTH_LONG).show();
                                CommonUtilities.showAlertDialog(RetailerProfileAndCouponActivity.this,"Done","Coupon verified",true);

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
}
