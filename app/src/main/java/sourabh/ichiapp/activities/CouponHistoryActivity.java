package sourabh.ichiapp.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
import java.util.ArrayList;
import java.util.List;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import sourabh.ichiapp.R;
import sourabh.ichiapp.app.AppConfig;
import sourabh.ichiapp.app.CustomRequest;
import sourabh.ichiapp.helper.CommonUtilities;
import sourabh.ichiapp.helper.Const;
import sourabh.ichiapp.helper.JsonSeparator;

import static android.icu.text.RelativeDateTimeFormatter.Direction.THIS;
import static sourabh.ichiapp.R.id.buttonCouponHistory;
import static sourabh.ichiapp.R.id.txPoints;
import static sourabh.ichiapp.R.id.txtEmail;
import static sourabh.ichiapp.R.id.txtFnameLname;
import static sourabh.ichiapp.R.id.txtPhone;

public class CouponHistoryActivity extends AppCompatActivity {



    private static final String[] DATA_HEADER = { "Coupon used", "Retailer", "Date" };
    ArrayList<List<String>> historyArrayList = new ArrayList<>();
    Context context;
    TableView tableView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_history);
        tableView = (TableView) findViewById(R.id.tableView);


        context = getApplicationContext();
        getUserCouponHistory();

    }


    void getUserCouponHistory(){

        String url = AppConfig.URL_GET_USER_COUPON_HISTORY;



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


                                JSONArray userCouponsArr =  CommonUtilities.getArrayFromJsonObj(registerResponceJson, Const.KEY_USER_COUPON_HISTORY);

                                setView(userCouponsArr);

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

    void setView(JSONArray jsonArray ) throws JSONException {

        List<String> row = new ArrayList<>();


        for (int i = 0 ; i< jsonArray.length(); i++){

            JSONObject jsonObject = jsonArray.getJSONObject(i);

            row.clear();
            row.add(jsonObject.getString("offer_name"));
            row.add(jsonObject.getString("retailer_name"));
            row.add(jsonObject.getString("created_at"));

            historyArrayList.add(row);

        }

        String[][] histArray = new String[historyArrayList.size()][];
        for (int i = 0; i < historyArrayList.size(); i++) {
            ArrayList<String> str_row = (ArrayList<String>) historyArrayList.get(i);
            histArray[i] = str_row.toArray(new String[str_row.size()]);
        }

        tableView.setHeaderBackgroundColor(getResources().getColor(R.color.holo_blue_dark));

        SimpleTableHeaderAdapter simpleTableHeaderAdapter = new SimpleTableHeaderAdapter(this,DATA_HEADER);
        simpleTableHeaderAdapter.setTextColor(getResources().getColor(R.color.white));
        simpleTableHeaderAdapter.setTextSize(15);


        SimpleTableDataAdapter simpleTableDataAdapter =  new SimpleTableDataAdapter(this,histArray);
        simpleTableDataAdapter.setTextSize(12);




        tableView.setHeaderAdapter(simpleTableHeaderAdapter);


        tableView.setDataAdapter(simpleTableDataAdapter);


    }
}
