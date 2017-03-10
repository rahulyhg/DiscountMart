package sourabh.ichiapp.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
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
import sourabh.ichiapp.helper.JsonSeparator;

public class AddAddressActivity extends Activity {


    Context context;

    @BindView(R.id.fname)
    TextView txtFname;

    @BindView(R.id.lname)
    TextView txtLname;

    @BindView(R.id.address)
    TextView txtAddreess;

    @BindView(R.id.phone)
    TextView txtPhone;
    @BindView(R.id.pincode)
    TextView txtPinCode;
    @BindView(R.id.btnAddAddress)
    TextView btnAddAddress;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_address);

        ButterKnife.bind(this);
        context = getApplicationContext();


        btnAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAddress();
            }
        });
    }


    void addAddress()
    {


        Map<String, String> params = new HashMap<String, String>();
        params.put("fname",txtFname.getText().toString());
        params.put("lname",txtLname.getText().toString());
        params.put("phone",txtPhone.getText().toString());
        params.put("address",txtAddreess.getText().toString());
        params.put("pincode",txtPinCode.getText().toString());


        RequestQueue requestQueue = Volley.newRequestQueue(context);

        CustomRequest jsObjRequest   = new CustomRequest(context,
                false,
                Request.Method.POST,
                AppConfig.URL_ADD_ADDRESS,
                params, CommonUtilities.buildHeaders(context),

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        JsonSeparator js = new JsonSeparator(context,response);

                        //  Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();

                        try {
                            if(js.isError()){
                                Toast.makeText(context, js.getMessage(), Toast.LENGTH_LONG).show();
                            }else{

                                //JSONObject registerResponceJson = js.getData() ;
                                Toast.makeText(context, js.getMessage(), Toast.LENGTH_LONG).show();



                                finish();





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
//        AppController.getInstance().addToRequestQueue(jsObjRequest);


        requestQueue.add(jsObjRequest);




    }


    }

