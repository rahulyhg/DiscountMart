package sourabh.ichiapp.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import sourabh.ichiapp.R;
import sourabh.ichiapp.app.AppConfig;
import sourabh.ichiapp.app.CustomRequest;
import sourabh.ichiapp.helper.CommonUtilities;
import sourabh.ichiapp.helper.Const;
import sourabh.ichiapp.helper.JsonSeparator;
import sourabh.ichiapp.helper.SessionManager;

import static sourabh.ichiapp.R.id.email;
import static sourabh.ichiapp.R.id.fname;
import static sourabh.ichiapp.R.id.lname;
import static sourabh.ichiapp.R.id.phone;

public class RegisterActivity extends AppCompatActivity {
    @BindView(fname) EditText txtFname;
    @BindView(lname) EditText txtLname;
    @BindView(phone) EditText txtPhone;
    @BindView(email) EditText txtEmail;
    @BindView(R.id.password) EditText txtPassword;
    @BindView(R.id.cpassword) EditText txtCpassword;
    @BindView(R.id.btnRegister) Button btnRegister;
    @BindView(R.id.btnLinkToLoginScreen)Button btnLinkToLogin;

    Context context;
    JsonSeparator js;
    SessionManager sessionManager;
    int REQUEST_READ_PHONE_STATE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        context = getApplicationContext();

        ButterKnife.bind(this);
        sessionManager = new SessionManager(context);
//        Button btnRegister = (Button) findViewById(R.id.btnRegister);


        requestPermission();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {




                if(CommonUtilities.validateName(txtFname).equals("OK") &&
                        CommonUtilities.validateName(txtLname).equals("OK") &&
                        CommonUtilities.validatePhoneNumberLength(txtPhone).equals("OK") &&

                        CommonUtilities.validatePassword(txtPassword, txtCpassword).equals("OK")
                )
                {

                    String fname = txtFname.getText().toString().trim();
                    String lname = txtLname.getText().toString().trim();
                    String phone = txtPhone.getText().toString().trim();
                    String password = txtPassword.getText().toString().trim();
                    String email = txtEmail.getText().toString().trim();
                    String imei = CommonUtilities.getIMEI(context);


                    Map<String, String> params = new HashMap<String, String>();
                    params.put("fname",fname);
                    params.put("lname",lname);
                    params.put("phone",phone);
                    params.put("password",password);
                    params.put("email",email);
                    params.put("imei",imei);
                    params.put("gcm_regid","gcm_regid");


                    registerUser(context,AppConfig.URL_REGISTER,
                            params,CommonUtilities.buildGuestHeaders());
                }



            }
        });


        // Link to Login Screen
        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(i);
                finish();
            }
        });



    }


    void requestPermission(){

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
        } else {
            //TODO
        }

    }


//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case REQUEST_READ_PHONE_STATE:
//                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//                    //TODO
//                }
//                break;
//
//            default:
//                break;
//        }
//    }

    void registerUser(final Context con, String url ,
                      Map<String, String> params, Map<String, String> headers){







        RequestQueue requestQueue = Volley.newRequestQueue(con);

        CustomRequest jsObjRequest   = new CustomRequest(con,false, Request.Method.POST, url, params, headers,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        JsonSeparator js = new JsonSeparator(con,response);

                        //  Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();

                        try {
                            if(js.isError()){
                                Toast.makeText(con, js.getMessage(), Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(con, js.getMessage(), Toast.LENGTH_LONG).show();

                                JSONObject registerResponceJson = js.getData() ;


                                JSONObject userObj = registerResponceJson.getJSONObject(Const.KEY_USER);
                                //sessionManager.setUserInfo();



                                sessionManager.setUserInfo(
                                        userObj.getString(Const.KEY_ID),
                                        userObj.getString(Const.KEY_FNAME),
                                        userObj.getString(Const.KEY_LNAME),
                                        userObj.getString(Const.KEY_PHONE),
                                        userObj.getString(Const.KEY_EMAIL),
                                        userObj.getString(Const.KEY_API_KEY),
                                        userObj.getString(Const.KEY_GCM_REGID),
                                        userObj.getString(Const.KEY_IMEI),
                                        userObj.getString(Const.KEY_MEMBERSHIP),
                                        userObj.getString(Const.KEY_STATUS),
                                        userObj.getString(Const.KEY_CREATED_AT)



                                        );


                                gotoHome();

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


                        Toast.makeText(con, error.toString(), Toast.LENGTH_LONG).show();

                    }
                });
//        AppController.getInstance().addToRequestQueue(jsObjRequest);


        requestQueue.add(jsObjRequest);




    }

    public void gotoHome(){


        sessionManager.setLogin(true);

        Intent intent = new Intent(RegisterActivity.this,
                HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
