package sourabh.ichiapp.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

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
import sourabh.ichiapp.data.OfferCategoryData;
import sourabh.ichiapp.data.ServiceProviderData;
import sourabh.ichiapp.helper.CommonUtilities;
import sourabh.ichiapp.helper.Const;
import sourabh.ichiapp.helper.JsonSeparator;

import static android.R.attr.banner;
import static android.R.attr.width;
import static sourabh.ichiapp.R.id.address;
import static sourabh.ichiapp.R.id.phone;
import static sourabh.ichiapp.helper.Const.KEY_OFFER_IMAGE;

public class RetailerProfileAndCouponActivity extends AppCompatActivity {

    @BindView(R.id.image_coupon) ImageView imageCoupon;
    @BindView(R.id.banner) ImageView banner;
    @BindView(R.id.thumbnail) NetworkImageView thumbnail;
    @BindView(R.id.name) TextView txtName;
    @BindView(address) TextView txtAddress;
    @BindView(phone) TextView txtPhone;
    @BindView(R.id.description) TextView description;

    @BindView(R.id.imageViewDescIcon) ImageView imgdesc;

    String offer_image,coupon_code,coupon_id;


    Context context;
    OfferCategoryData offerCategoryData;
    ServiceProviderData serviceProviderData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_profile_and_coupon);
        ButterKnife.bind(this);
        context = getApplicationContext();


        serviceProviderData = CommonUtilities.getObjectFromJsonString(getIntent().getStringExtra(Const.KEY_RETAILERS),ServiceProviderData.class);
        offerCategoryData = CommonUtilities.getObjectFromJsonString(getIntent().getStringExtra(Const.KEY_OFFER_DATA),OfferCategoryData.class);


        offer_image = offerCategoryData.getImage();

        setViews();



        imageCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requestCoupon();
            }
        });


    }

    void setViews(){

        String mMessage_pic_url = serviceProviderData.getBanner();

        ImageLoader imageLoader = AppController.getInstance().getImageLoader();

        Picasso.with(this.context)
                .load(mMessage_pic_url)
                .error(android.R.drawable.stat_notify_error)
                .fit()
                .placeholder( R.drawable.progress_animation )

                .into(banner, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                    }
                });



        txtName.setText(serviceProviderData.getName());
        txtAddress.setText(serviceProviderData.getAddress());
        txtPhone.setText(serviceProviderData.getPhone());
        imgdesc.setVisibility(View.VISIBLE);
        description.setVisibility(View.VISIBLE);
        description.setText(serviceProviderData.getDescription());
        thumbnail.setImageUrl(serviceProviderData.getImage(), imageLoader);


        Picasso.with(this.context)
                .load(offer_image)
                .error(android.R.drawable.stat_notify_error)
                .fit()
                .placeholder( R.drawable.progress_animation )

                .into(imageCoupon, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                    }
                });

    }

    void requestCoupon(){


        Map<String, String> params = new HashMap<String, String>();
        params.put("id_offer",offerCategoryData.getId().toString());
        params.put("id_retailer",serviceProviderData.getId().toString());
        params.put("id_user","1");




        RequestQueue requestQueue = Volley.newRequestQueue(context);

        CustomRequest jsObjRequest   = new CustomRequest(this,true,
                Request.Method.POST,
                AppConfig.URL_CREATE_COUPON_REQUEST,
                params,
                CommonUtilities.buildHeaders(),

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
                CommonUtilities.buildHeaders(),

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
