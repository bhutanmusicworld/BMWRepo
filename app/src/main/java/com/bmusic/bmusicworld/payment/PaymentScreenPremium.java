package com.bmusic.bmusicworld.payment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bmusic.bmusicworld.R;
import com.bmusic.bmusicworld.sessionmagement.SessionManagement;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wafa on 9/29/2017.
 */

//public class PaymentScreenPremium {

public class PaymentScreenPremium extends AppCompatActivity implements View.OnClickListener {
    Button conti;
    SessionManagement session;
    private RequestQueue requestQueue;
    TextView rate;
    private RadioGroup radioGroup,planGroup;
    RadioButton one,three,nine;
    String payment_url="http://bmusicworld.com/api/payment/paynow";
    String userid,planid,amount,merchantrequest,mid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_ment_premium);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        requestQueue = Volley.newRequestQueue(this);
        radioGroup = (RadioGroup) findViewById(R.id.group);
        planGroup = (RadioGroup) findViewById(R.id.pgroup);

        conti=(Button)findViewById(R.id.conti);
        rate=(TextView)findViewById(R.id.rate);
        session=new SessionManagement(this);
        userid=session.getUserId();
        conti.setOnClickListener(this);

        one=(RadioButton)findViewById(R.id.onemon);
        three=(RadioButton)findViewById(R.id.twomon);
        nine=(RadioButton)findViewById(R.id.threemon);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                switch (checkedId)
                {
                    case R.id.onemon:
                       // int total=100;
                     int total=99*100;
                        amount= String.valueOf(total);
                        one.setBackgroundColor(getResources().getColor(R.color.primaryColor));
                        three.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        nine.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        rate.setText("Nu. 99/month.");
                        // Toast.makeText(PayMent.this, "1 checked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.twomon:
                        int total3=297*100;
                        amount= String.valueOf(total3);
                        one.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        three.setBackgroundColor(getResources().getColor(R.color.primaryColor));
                        nine.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        rate.setText("Nu. 297/3month.");
                        // Toast.makeText(PayMent.this, "3 checked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.threemon:
                        int total9=891*100;
                        amount= String.valueOf(total9);
                        one.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        three.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        nine.setBackgroundColor(getResources().getColor(R.color.primaryColor));
                        rate.setText("Nu. 891/9month.");
                        // Toast.makeText(PayMent.this, "9 checked", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        });
        planGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                switch (checkedId)
                {
                    case R.id.freid:
                        //  planid="0";
                        // Toast.makeText(PayMent.this, "Freemium Model", Toast.LENGTH_SHORT).show();
                        // do your code
                        break;
                    case R.id.preid:
                        planid="2";
                        // Toast.makeText(PayMent.this, "Premium Model", Toast.LENGTH_SHORT).show();
                        // do your code
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {

            case R.id.conti:

                if (TextUtils.isEmpty(amount))
                {
                    Toast.makeText(PaymentScreenPremium.this,"select month for Premium Model",Toast.LENGTH_LONG).show();
                    return;
                }else {
                    userpayment(userid,amount,planid,"0","0","0");
                }
                // do your code
                break;
            default:
                break;
        }

    }


    private void userpayment(final String userid, final String amount, final String planid, final String recurperioud, final String norecur,final String recrper) {
        final ProgressDialog loading = ProgressDialog.show(this, "Sending", "Please wait...", false, false);
        //Again creating the string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST,payment_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        Log.e("response", response.toString());
                        try {
                            //Creating the json object from the response
                            JSONObject jsonResponse = new JSONObject(response);
                            String    usrStatus=jsonResponse.getString("status");
                            String msg=jsonResponse.getString("messages");
                            String resp=jsonResponse.getString("resp");
                            if(jsonResponse.getString("status").equalsIgnoreCase("success")){
                                JSONObject userdata=jsonResponse.getJSONObject("paramater");

                                merchantrequest=userdata.getString("merchantRequest");
                                mid=userdata.getString("MID");
                                Intent i=new Intent(PaymentScreenPremium.this,PayScreen.class);
                                i.putExtra("mid",mid);
                                i.putExtra("mreq",merchantrequest);
                                i.putExtra("resp",resp);
                                startActivity(i);
                            }
                            else {
                                Toast.makeText(PaymentScreenPremium.this, "You Dont have Premium Account.Please choose Premium Model.", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toast.makeText(PaymentScreenPremium.this, error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("user_id", userid);
                params.put("amount",amount);
                params.put("plan_id","2");
                params.put("recurPeriod",recurperioud);
                params.put("numberRecurring",norecur);
                params.put("recurPeriod",recrper);
                // params.put("password",norecur);
                Log.e("requesting", "Posting params: " + params.toString());
                return params;
            }
        };
        //Adding request the the queue
        requestQueue.add(stringRequest);
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

