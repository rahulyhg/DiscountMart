package sourabh.ichiapp.activities;

import android.content.Context;
import android.content.Intent;
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

import org.json.JSONArray;
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

import static sourabh.ichiapp.R.id.phone;

public class LoginActivity extends AppCompatActivity {

    @BindView(phone)
    EditText txtPhone;
    @BindView(R.id.password) EditText txtPassword;
    @BindView(R.id.btnLogin)
    Button btnLogin;

    @BindView(R.id.btnLinkToRegisterScreen)
    Button btnLinkToRegisterScreen;

    Context context;
    JsonSeparator js;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = getApplicationContext();

        ButterKnife.bind(this);

        // Session manager
        sessionManager = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (sessionManager.isLoggedIn()) {
            // User is already logged in. Take him to main activity
//            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//            startActivity(intent);
            finish();
        }


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(CommonUtilities.validatePhoneNumberLength(txtPhone).equals("OK")){

                    String phn = txtPhone.getText().toString();
                    String password = txtPassword.getText().toString();

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("phone", phn);
                    params.put("password",password);

                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Authorization", AppConfig.API_KEY_GUEST);

                    Login(context, AppConfig.URL_LOGIN,params,headers);
                }

            }
        });

        btnLinkToRegisterScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));

            }
        });


    }



    private void Login(final Context con, String url , Map<String, String> params, Map<String, String> headers){

        RequestQueue requestQueue = Volley.newRequestQueue(con);

        CustomRequest jsObjRequest   = new CustomRequest(LoginActivity.this,true, Request.Method.POST,url, params, headers,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        JsonSeparator js = new JsonSeparator(con,response);

                        //  Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();

                        try {
                            if(js.isError()){
                                Toast.makeText(con, js.getMessage(), Toast.LENGTH_LONG).show();
                            }else{
//                                Toast.makeText(con, js.getMessage(), Toast.LENGTH_LONG).show();

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





                                //ParseCities(js.getData());
                            }}
                         catch (JSONException e) {
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
        requestQueue.add(jsObjRequest);

    }

    void gotoHome(){
        sessionManager.setLogin(true);

        Intent intent = new Intent(LoginActivity.this,
                HomeActivity.class);
        startActivity(intent);
        finish();
    }



}
