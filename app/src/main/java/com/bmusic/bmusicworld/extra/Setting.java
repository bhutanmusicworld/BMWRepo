package com.bmusic.bmusicworld.extra;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bmusic.bmusicworld.R;
import com.bmusic.bmusicworld.demo.Config;
import com.bmusic.bmusicworld.model.User;
import com.bmusic.bmusicworld.payment.PaymentScreenPremium;
import com.bmusic.bmusicworld.register.MainLogin;
import com.bmusic.bmusicworld.sessionmagement.SessionManagement;
import com.bmusic.bmusicworld.sessionmagement.SharedPrefManager;
import com.bmusic.bmusicworld.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

/**
 * Created by wafa on 8/29/2017.
 */

public class Setting extends AppCompatActivity {
    TextView name,dob,mob,currentplan,email,cdate,edate,edateText,sdate,sedateText;
    String userurl="http://bmusicworld.com/api/users/userdetail",uid;
    SessionManagement session;
    FrameLayout list_empty,list_empty2;
    Button button2;
    Utility utility;
    User user;
    private RequestQueue requestQueue;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile2);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        requestQueue = Volley.newRequestQueue(this);
        session=new SessionManagement(this);
        utility=new Utility(this);
        uid=session.getUserId();
        name=(TextView)findViewById(R.id.name);
        button2=(Button)findViewById(R.id.button2);
        dob=(TextView)findViewById(R.id.dob);
        cdate=(TextView)findViewById(R.id.cdate);
        edate=(TextView)findViewById(R.id.edate);
        edateText=(TextView)findViewById(R.id.edateText);
        mob=(TextView)findViewById(R.id.mob);
        currentplan=(TextView)findViewById(R.id.plan);
        email=(TextView)findViewById(R.id.email);
        sdate=(TextView)findViewById(R.id.sdate);
        sedateText=(TextView)findViewById(R.id.sedateText);
        list_empty= (FrameLayout)findViewById(R.id.list_empty);
        list_empty2= (FrameLayout)findViewById(R.id.list_empty2);
        user= SharedPrefManager.getInstance(this).getUser();
        uid =user.getId();

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            // doSomething
            if (Config.isInternetOn(this)) {
                userinfo(uid);
            } else {
                list_empty2.setVisibility(View.VISIBLE);
                Toasty.warning(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
            }
        } else {
            list_empty.setVisibility(View.VISIBLE);
        }
    }
    public void openPayment(View view)
    {
        Intent i=new Intent(Setting.this, PaymentScreenPremium.class);
        startActivity(i);
    }

    private void userinfo(final String userid) {
        utility.showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,userurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        utility.cancleDialog();
                        Log.e("response===", response.toString());
                        try {
                            //Creating the json object from the response
                            JSONObject jsonResponse = new JSONObject(response);
                            String    usrStatus=jsonResponse.getString("status");
                            if(jsonResponse.getString("status").equalsIgnoreCase("success")){
                                JSONObject userdata=jsonResponse.getJSONObject("messages");
//                               JSONArray array=jsonResponse.getJSONArray("messages");
//                                for (int i=0;i<array.length();i++)
//                                {
                                //JSONObject obj=array.getJSONObject(i);
                                String uname=userdata.getString("first_name");
                                String ueid=userdata.getString("email_id");
                                String udob=userdata.getString("dob");
                                String cphone=userdata.getString("phone_no");
                                String uplan=userdata.getString("pricing_name");
                                String ucreate=userdata.getString("created_date");
                                String subdate=userdata.getString("subscription_date");

                                name.setText(uname);
                                dob.setText(udob);
                                mob.setText(cphone);
                                email.setText(ueid);
                                cdate.setText(ucreate);


                                if (uplan.equalsIgnoreCase("Freemium Model")) {
                                    currentplan.setText(uplan);
                                    button2.setVisibility(View.VISIBLE);
                                    edateText.setVisibility(View.GONE);
                                    sedateText.setVisibility(View.GONE);
                                } else {
                                    String ecreate = userdata.getString("subscription_end_date");
                                    currentplan.setText(uplan);
                                    if (ecreate!=null)
                                    {
                                        edate.setVisibility(View.VISIBLE);
                                        sedateText.setVisibility(View.VISIBLE);
                                        sdate.setText(subdate);
                                        edate.setText(ecreate);
                                    }



                                }
                            }
                            else {
                                Toast.makeText(Setting.this,jsonResponse.getString("messages"),Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        utility.cancleDialog();
                        // Toast.makeText(MainActivity2.this, error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id",uid);
                Log.e("requesting","Posting params:" + params.toString());
                return params;
            }
        };
        int MY_SOCKET_TIMEOUT_MS = 30000;
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Adding request the the queue
        requestQueue.add(stringRequest);
        //Adding request the the queue
        requestQueue.add(stringRequest);
    }
    public void openloginScreen(View view)
    {
        Intent i=new Intent(Setting.this, MainLogin.class);
        startActivity(i);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
