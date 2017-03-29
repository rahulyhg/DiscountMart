package sourabh.ichiapp.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import sourabh.ichiapp.R;
import sourabh.ichiapp.app.AppConfig;
import sourabh.ichiapp.app.CustomRequest;
import sourabh.ichiapp.helper.CommonUtilities;
import sourabh.ichiapp.helper.Const;
import sourabh.ichiapp.helper.JsonSeparator;

import butterknife.BindView;

public class ProfileActivity extends AppCompatActivity {




    /**ButterKnife code begin**/

    @BindView(R.id.txtFnameLname)
    TextView txtFnameLname;

    @BindView(R.id.txtEmail)
    TextView txtEmail;

    @BindView(R.id.txtPhone)
    TextView txtPhone;

    @BindView(R.id.txPoints)
    TextView txPoints;

    @BindView(R.id.buttonCouponHistory)
    Button buttonCouponHistory;

    @BindView(R.id.buttonChangePassword)
    Button buttonChangePassword;

    @BindView(R.id.buttonShoppingHistory)
    Button buttonShoppingHistory;

    @BindView(R.id.txtReferCode)
    TextView txtReferCode;

    @BindView(R.id.imageShare)
    ImageView imageShare;


    @BindView(R.id.activity_profile)
    RelativeLayout activity_profile;
    /**ButterKnife code end **/

    Dialog changePasswordDialog;
    EditText old_pass,new_pass,confirm_pass;

    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        context = getApplicationContext();
        ButterKnife.bind(this);

        buttonCouponHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this,CouponHistoryActivity.class));
            }
        });

        buttonChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showChnagePasswordDialog();
            }
        });

        getUserInfo();
    }

    void showChnagePasswordDialog(){


        changePasswordDialog = new Dialog(this);
        changePasswordDialog.setContentView(R.layout.change_password_dialog);
        changePasswordDialog.setCancelable(true);

        Button btnChangePasswordButton = (Button) changePasswordDialog.findViewById(R.id.btnChangePasswordButton);
        old_pass = (EditText) changePasswordDialog.findViewById(R.id.old_pass);
        new_pass = (EditText) changePasswordDialog.findViewById(R.id.new_pass);
        confirm_pass = (EditText) changePasswordDialog.findViewById(R.id.confirm_pass);



        btnChangePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(CommonUtilities.validatePassword(new_pass,confirm_pass)){
                    changePassword(old_pass.getText().toString(),new_pass.getText().toString());

                }

            }
        });

        WindowManager.LayoutParams lp = changePasswordDialog.getWindow().getAttributes();
        lp.dimAmount = 0.5f;
        changePasswordDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        changePasswordDialog.show();
    }

    void changePassword(String old_pass, String new_pass)
    {

        String url = AppConfig.URL_UPDATE_PASSWORD;

        Map<String, String> params = new HashMap<String, String>();
        params.put("old_pass",old_pass);
        params.put("new_pass",new_pass);



        RequestQueue requestQueue = Volley.newRequestQueue(context);

        CustomRequest jsObjRequest   = new CustomRequest(this,true, Request.Method.POST, url,
                                                            params, CommonUtilities.buildHeaders(context),

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        JsonSeparator js = new JsonSeparator(context,response);

                        //  Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();


                        try {
                            if(js.isError()){
                                CommonUtilities.showLongToast(context,js.getMessage());
                            }else{
                                CommonUtilities.showLongToast(context,js.getMessage());
                                changePasswordDialog.dismiss();

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
    void getUserInfo(){


            String url = AppConfig.URL_GET_USER;



            RequestQueue requestQueue = Volley.newRequestQueue(context);

            CustomRequest jsObjRequest   = new CustomRequest(this,true, Request.Method.GET, url, CommonUtilities.buildBlankParams(), CommonUtilities.buildHeaders(context),

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


                                    JSONArray userArr =  CommonUtilities.getArrayFromJsonObj(registerResponceJson, Const.KEY_USER);

                                    setView(userArr.getJSONObject(0));

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

    void setView(JSONObject jsonObjectUser ){


        activity_profile.setVisibility(View.VISIBLE);

        try {
            String id = jsonObjectUser.getString(Const.KEY_ID);
            String fname = jsonObjectUser.getString(Const.KEY_FNAME);
            String lname = jsonObjectUser.getString(Const.KEY_LNAME);
            String phone = jsonObjectUser.getString(Const.KEY_PHONE);
            String email = jsonObjectUser.getString(Const.KEY_EMAIL);
            String points = jsonObjectUser.getString(Const.KEY_POINTS);

            String api_key = jsonObjectUser.getString(Const.KEY_API_KEY);
            String gcm_regid = jsonObjectUser.getString(Const.KEY_GCM_REGID);
            String imei = jsonObjectUser.getString(Const.KEY_IMEI);
            String membership = jsonObjectUser.getString(Const.KEY_MEMBERSHIP);
            String status = jsonObjectUser.getString(Const.KEY_STATUS);
            String created_at = jsonObjectUser.getString(Const.KEY_CREATED_AT);



            txtFnameLname.setText(fname+" "+lname);
            txtEmail.setText(email);
            txtPhone.setText(phone);
            txPoints.setText(points+" Points");









        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
