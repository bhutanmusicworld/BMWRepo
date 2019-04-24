package com.bmusic.bmusicworld.register;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
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
import com.bmusic.bmusicworld.MainActivity;
import com.bmusic.bmusicworld.demo.Config;
import com.bmusic.bmusicworld.model.User;
import com.bmusic.bmusicworld.sessionmagement.SharedPrefManager;
import com.bmusic.bmusicworld.utility.Utility;
import com.hbb20.CountryCodePicker;
import com.bmusic.bmusicworld.R;
import com.bmusic.bmusicworld.sessionmagement.SessionManagement;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

/**
 * Created by wafa on 8/17/2017.
 */

public class MobileLogin extends AppCompatActivity  {
    EditText Userno,pass,editTextGetCarrierNumber,et;
    Button login;
    CountryCodePicker ccpGetNumber,cp;
    String mail,num;
    TextView fp;
    int otpCode;
    boolean istrue=false;
    Utility utility;
    Dialog dialog;
    private RequestQueue requestQueue;
    SessionManagement session;
    String login_url="http://bmusicworld.com/api/users/login",userpass;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mob);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        session=new SessionManagement(this);
        utility=new Utility(this);
        requestQueue = Volley.newRequestQueue(this);
       // Userno=(EditText)findViewById(R.id.Userno);
        editTextGetCarrierNumber = (EditText)findViewById(R.id.editText_getCarrierNumber);
        ccpGetNumber = (CountryCodePicker) findViewById(R.id.ccp_getFullNumber);
        pass=(EditText)findViewById(R.id.editTextPassword);
        fp=(TextView) findViewById(R.id.fp);
        fp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showForgotdialog();
            }
        });
        login=(Button)findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });


    }

    private void validate() {
        userpass=pass.getText().toString().trim();

      if(editTextGetCarrierNumber.getText().toString().length()==0)
       {
           editTextGetCarrierNumber.setError("Please enter  mobile number");
       }
      else if (!checkNumber())
      {
          editTextGetCarrierNumber.setError("Please enter valid number");
      }
        if (userpass.length()==0)
        {
            pass.setError("please enter your password");
            return;
        }
        else if(TextUtils.isEmpty(userpass) || userpass.length() < 6)
        {
            pass.setError("You must have 6 characters in your password");
            return;
        }
       else
        {
            if (Config.isInternetOn(getApplicationContext())) {
                loginApp(userpass);
            }else {
                Toasty.warning(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
            }

        }


    }
    private void validate2() {


        if(et.getText().toString().length()==0)
        {
            et.setError("Please enter  mobile number");
        }
        else if (!checkNumber2())
        {
            et.setError("Please enter valid number");
        }
        else
        {
            if (Config.isInternetOn(getApplicationContext())) {
                forgetPassword(cp.getFullNumber());
                dialog.dismiss();
            }else {
                Toasty.warning(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
            }

        }


    }
    private boolean checkNumber()
    {

        ccpGetNumber.registerCarrierNumberEditText(editTextGetCarrierNumber);
        ccpGetNumber.setPhoneNumberValidityChangeListener(new CountryCodePicker.PhoneNumberValidityChangeListener() {
            @Override
            public void onValidityChanged(boolean isValidNumber) {
                if (isValidNumber) {
                    num=ccpGetNumber.getFullNumber();

                   istrue=isValidNumber;

                } else {
                    editTextGetCarrierNumber.setError("please enter valid no.");
                }
            }
        });
        return istrue;
    }
    private boolean checkNumber2()
    {

        cp.registerCarrierNumberEditText(et);
        cp.setPhoneNumberValidityChangeListener(new CountryCodePicker.PhoneNumberValidityChangeListener() {
            @Override
            public void onValidityChanged(boolean isValidNumber) {
                if (isValidNumber) {
                    //fnum=cp.getFullNumber();

                   istrue=isValidNumber;

                } else {
                    et.setError("please enter valid no.");
                }
            }
        });
        return istrue;
    }

    private void loginApp(final String userpass) {
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
                                Intent i2=new Intent(MobileLogin.this, MainActivity.class);
                                // i2.putExtra("userName",userName);
                                startActivity(i2);
                            }
                            else {
                                Toasty.error(getApplicationContext(),jsonResponse.get("messages").toString(),Toast.LENGTH_LONG).show();

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
                        Toasty.error(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email_id", ccpGetNumber.getFullNumber());
                params.put("password",userpass);
                Log.e("requesting", "Posting params: " + params.toString());
                return params;
            }
        };
        //Adding request the the queue
        requestQueue.add(stringRequest);
    }
    public void showForgotdialog() {
         dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.forgot_dailog);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
         et = (EditText) dialog.findViewById(R.id.et);
         cp = ccpGetNumber = (CountryCodePicker) dialog.findViewById(R.id.cp);;

       Button ok = (Button) dialog.findViewById(R.id.ok);
       Button cancel = (Button) dialog.findViewById(R.id.cancel);
         ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  validate2();
            }

        });
         cancel.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 dialog.dismiss();             }
         });
         dialog.show();
    }
    public void forgetpass(View v)
    {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Forget Password"); //Set Alert dialog title here
        alert.setMessage("Enter Your Phone No. Here"); //Message here
        final Boolean[] wantToCloseDialog = new Boolean[1];
        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
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
    private void forgetPassword(final String no) {
        utility.showDialog();
        //String forget_url="http://bmusicworld.com/api/users/forgot_password";
        String forget_url="http://bmusicworld.com/api/users/forgot_password_new";
        //String forget_url="http://bmusicworld.com/api/users/forgot_password";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,forget_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        utility.cancleDialog();
                        Log.e("forgot response", response.toString());
                        try {
                            //Creating the json object from the response
                            JSONObject jsonResponse = new JSONObject(response);
                            String    usrStatus=jsonResponse.getString("status");
                            if(jsonResponse.getString("status").equalsIgnoreCase("success")){


                                JSONObject userdata=jsonResponse.getJSONObject("responce");
                                String otp=userdata.getString("OTP_code");
                                String userId=userdata.getString("user_id");
                                showOTPDialog(otp,userId);
                            }
                            else {
                                Toasty.warning(MobileLogin.this, jsonResponse.get("messages").toString(), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(MobileLogin.this, error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", no);
                params.put("para_type", "phone");
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
    public void showOTPDialog(final String Code,final String id) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dilog_otp);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        final EditText otp = (EditText) dialog.findViewById(R.id.otp);
        final Button btn_find = (Button) dialog.findViewById(R.id.btn_find);
        final Button btn_cancle = (Button) dialog.findViewById(R.id.btn_cancle);
        btn_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (otp.getText().length() == 0) {
                    otp.setError("Enter Otp");
                    otp.requestFocus();
                } else {
                    if (otp.getText().toString().equalsIgnoreCase(Code)) {

                        setNewPass(id);
                        dialog.dismiss();
                    } else {
                        Toasty.warning(getApplicationContext(), " Incorrect OTP", Toast.LENGTH_LONG).show();

                    }

                }
            }
        });
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otpCode = 0;
                dialog.dismiss();
            }
        });
        dialog.show();

    }
    public void setNewPass(final String id) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dilog_new_pass);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        final EditText otp = (EditText) dialog.findViewById(R.id.otp);
        final Button btn_find = (Button) dialog.findViewById(R.id.btn_find);
        final Button btn_cancle = (Button) dialog.findViewById(R.id.btn_cancle);
        btn_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (otp.getText().length() == 0) {
                    otp.setError("Enter password");
                    otp.requestFocus();
                } else if(otp.getText().length() <6)
                {
                    otp.setError("Your password must be 6 digit");
                    otp.requestFocus();
                }
                else {
                        setPassword(id,otp.getText().toString());
                        dialog.dismiss();
                    }

            }
        });
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otpCode = 0;
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private void setPassword(final String uId,final String pass) {
        utility.showDialog();
        //String forget_url="http://bmusicworld.com/api/users/forgot_password";
        String forget_url="http://bmusicworld.com/api/users/update_password";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,forget_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        utility.cancleDialog();
                        Log.e("new pass response", response.toString());
                        try {
                            //Creating the json object from the response
                            JSONObject jsonResponse = new JSONObject(response);
                            if(jsonResponse.getString("status").equalsIgnoreCase("success")){

                                Toasty.success(MobileLogin.this, jsonResponse.get("messages").toString(), Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toasty.warning(MobileLogin.this, jsonResponse.get("messages").toString(), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(MobileLogin.this, error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id",uId);
                params.put("new_pass",pass);
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

}
