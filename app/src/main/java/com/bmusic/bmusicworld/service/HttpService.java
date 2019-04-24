package com.bmusic.bmusicworld.service;
import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bmusic.bmusicworld.MainActivity;
import com.bmusic.bmusicworld.model.User;
import com.bmusic.bmusicworld.sessionmagement.SessionManagement;
import com.bmusic.bmusicworld.sessionmagement.SharedPrefManager;
import com.bmusic.bmusicworld.utility.Config;
import com.bmusic.bmusicworld.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

/**
 * Created by Ravi on 04/04/15.
 */
public class HttpService extends IntentService {

    private static String TAG = HttpService.class.getSimpleName();
    private Utility utility;
    private RequestQueue requestQueue;
    private SessionManagement session;
    public HttpService() {
        super(HttpService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String otp = intent.getStringExtra("otp");

            String password = intent.getStringExtra("password");
            verifyOtp(otp,password);
        }
    }

    /**
     * Posting the OTP to server and activating the user
     *
     * @param otp otp received in the SMS
     */
    private void verifyOtp(final String otp,final String password) {
        session=new SessionManagement(this);
      final User user=  SharedPrefManager.getInstance(getApplicationContext()).getUser();

        requestQueue = Volley.newRequestQueue(this);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Config.URL_VERIFY_OTP, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, response.toString());
              //  utility.cancleDialog();
                try {

                    JSONObject responseObj = new JSONObject(response);

                    // Parsing json object response
                    // response will be a json object
                   boolean error = true;
                   // String message = responseObj.getString("message");

                    if (responseObj.getString("status").equalsIgnoreCase("success")){
                        Toasty.success(getApplicationContext(),responseObj.get("messages").toString(),Toast.LENGTH_LONG).show();
                        JSONObject userdata=responseObj.getJSONObject("user_id");
                        String userId=userdata.getString("id");
                        session.setLogin(true);
                        session.setUserId(userId);
                        User user = new User(
                                userId,
                                userdata.getString("first_name"),
                                userdata.getString("phone_no"),
                                userdata.getString("email_id"),
                                userdata.getString("user_type"),
                                userdata.getString("dob"),
                                userdata.getString("created_date"));
                        //storing the user in shared preferences
                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                        Intent intent = new Intent(HttpService.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    } else {
                        Toast.makeText(getApplicationContext(), responseObj.get("messages").toString(), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("otp_code", otp);
              //  params.put("id", id);
                params.put("password", password);
                params.put("first_name", user.getUserName());
                params.put("phone_no", user.getMobileNo());
                params.put("email_id", user.getEmail());
                //params.put("user_type", id);
                params.put("dob", user.getDob());

                Log.e(TAG, "Posting params: " + params.toString());
                return params;
            }

        };
        requestQueue.add(strReq);
        // Adding request to request queue
    }

}
