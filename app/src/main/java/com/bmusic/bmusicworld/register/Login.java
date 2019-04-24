package com.bmusic.bmusicworld.register;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bmusic.bmusicworld.MainActivity;
import com.bmusic.bmusicworld.R;
import com.bmusic.bmusicworld.demo.Config;
import com.bmusic.bmusicworld.model.User;
import com.bmusic.bmusicworld.sessionmagement.SessionManagement;
import com.bmusic.bmusicworld.sessionmagement.SharedPrefManager;
import com.bmusic.bmusicworld.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;


public class Login extends AppCompatActivity {
    EditText email,pass;
    Button login;
    String mail;
    private RequestQueue requestQueue;
    SessionManagement session;
    Utility utility;
    String login_url="http://bmusicworld.com/api/users/login";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_screen);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        session=new SessionManagement(this);
        utility=new Utility(this);
        requestQueue = Volley.newRequestQueue(this);
        email=(EditText)findViewById(R.id.uid);
        pass=(EditText)findViewById(R.id.editTextPassword);
        login=(Button)findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });
    }

    private void validate() {
        String userpass=pass.getText().toString().trim();
        String uemail=email.getText().toString().trim();
        CharSequence temp_emilID=email.getText().toString().trim();
        if (userpass.length()==0)
        {

            Toasty.error(getApplicationContext(), "Please enter your details", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(userpass) || userpass.length() < 6)
        {
            pass.setError("You must have 6 characters in your password");
            return;
        }
        if(!isValidEmail(temp_emilID))
        {
            mail= String.valueOf(temp_emilID);
            email.requestFocus();
            email.setError("Enter Correct Mail_ID ..!!");
            // Toast.makeText(getApplicationContext(), "Enter Correct Mail_ID", Toast.LENGTH_SHORT).show();
        }
        else {
            if (Config.isInternetOn(getApplicationContext())) {
                loginApp(uemail,userpass);
            }else {
                Toasty.warning(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
            }

        }

    }
    public static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
    private void loginApp(final String username,final String userpass) {
        utility.showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,login_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        utility.cancleDialog();
                        Log.e("response", response.toString());
                        try {
                            //Creating the json object from the response
                            JSONObject jsonResponse = new JSONObject(response);
                            String    usrStatus=jsonResponse.getString("status");
                            if(jsonResponse.getString("status").equalsIgnoreCase("success")){
                                // Toast.makeText(Login.this, jsonResponse.get("code").toString(), Toast.LENGTH_SHORT).show();
                                JSONObject userdata=jsonResponse.getJSONObject("user");
                                String userId=userdata.getString("id");
                                session.setLogin(true);
                                session.setUserId(userId);
                                User user = new User(
                                        userdata.getString("id"),
                                        userdata.getString("first_name"),
                                        userdata.getString("phone_no"),
                                        userdata.getString("email_id"),
                                        userdata.getString("user_type"),
                                        userdata.getString("dob"),
                                        userdata.getString("created_date"));
                                //storing the user in shared preferences
                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                                Intent i2=new Intent(Login.this, MainActivity.class);
                                // i2.putExtra("userName",userName);
                                startActivity(i2);
                            }
                            if(usrStatus.equalsIgnoreCase("error")) {
                                Toasty.warning(getApplicationContext(),jsonResponse.get("messages").toString(),Toast.LENGTH_LONG).show();
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
                        Toasty.warning(getApplicationContext(), error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email_id", username);
                params.put("password",userpass);
                Log.e("requesting", "Posting params: " + params.toString());
                return params;
            }
        };
        //Adding request the the queue
        requestQueue.add(stringRequest);
    }
    public void forgetpass(View v)
    {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Forget Password"); //Set Alert dialog title here
        alert.setMessage("Enter Your Email Id Here"); //Message here
        final Boolean[] wantToCloseDialog = new Boolean[1];
        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //You will get as string input data in this variable.
                // here we convert the input to a string and show in a toast.

            } // End of onClick(DialogInterface dialog, int whichButton)
        }); //End of alert.setPositiveButton
        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
                dialog.cancel();
            }
        }); //End of alert.setNegativeButton

        final AlertDialog dialog = alert.create();
        dialog.show();
        //Overriding the handler immediately after show is probably a better approach than OnShowListener as described below
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String srt = input.getEditableText().toString();
                boolean entriesvalid=false;
                if (srt.isEmpty()&&srt.length()==0)
                {
                    input.setError("ENTER EMAIL ID");
                    input.setFocusable(true);
                }else {
                    entriesvalid=true;
                    forgetPassword(srt);
                }
                if(entriesvalid)
                    dialog.dismiss();
                //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
            }
        });

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Boolean wantToCloseDialog = true;
                //Do stuff, possibly set wantToCloseDialog to true then...
                if(wantToCloseDialog)
                    dialog.dismiss();
                //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
            }
        });
    }
    private void forgetPassword(final String email) {
        //Displaying a progress dialog
        final ProgressDialog loading = ProgressDialog.show(this, "Registering", "Please wait...", false, false);
        //Again creating the string request
       // String forget_url="http://bmusicworld.com/api/users/forgot_password";
        String forget_url="http://bmusicworld.com/api/users/forgot_password_new";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,forget_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        Log.e("forgot response", response.toString());
                        try {
                            //Creating the json object from the response
                            JSONObject jsonResponse = new JSONObject(response);
                            String usrStatus=jsonResponse.getString("status");
                            if(jsonResponse.getString("status").equalsIgnoreCase("success")){
                                Toasty.success(Login.this, jsonResponse.get("messages").toString(), Toast.LENGTH_SHORT).show();
                            }
                            else
                                {
                                Toasty.warning(Login.this, jsonResponse.get("messages").toString(), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(Login.this, error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("para_type", "email");
                Log.e( "Posting params: " , params.toString());
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
