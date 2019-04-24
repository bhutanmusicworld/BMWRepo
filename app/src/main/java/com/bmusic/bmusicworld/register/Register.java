package com.bmusic.bmusicworld.register;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bmusic.bmusicworld.MainActivity;
import com.bmusic.bmusicworld.demo.Config;
import com.bmusic.bmusicworld.model.User;
import com.bmusic.bmusicworld.service.HttpService;
import com.bmusic.bmusicworld.sessionmagement.SharedPrefManager;
import com.bmusic.bmusicworld.utility.Utility;
import com.hbb20.CountryCodePicker;
import com.bmusic.bmusicworld.R;
import com.bmusic.bmusicworld.sessionmagement.SessionManagement;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

/**
 * Created by wafa on 8/14/2017.
 */



public class Register extends AppCompatActivity {
    EditText name,email,pass,editTextGetCarrierNumber;
    static TextView SelectedDateView ;
    boolean istrue=false;
    CountryCodePicker ccpGetNumber;
    private static final int DATE_PICKER_ID=10;
    int year,month,day;
    Button register;
    Utility utility;
    private RequestQueue requestQueue;
    String mail,ubirth,num;
    int otpCode;
    SessionManagement session;
    String register_url="http://bmusicworld.com/api/users/register";
    String otp_url="http://bmusicworld.com/api/users/check_otp_code";
   // String register_url="http://bmusicworld.com/api/users/register";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        session=new SessionManagement(this);
        utility=new Utility(this);
        name=(EditText)findViewById(R.id.name);
        editTextGetCarrierNumber = (EditText)findViewById(R.id.editText_getCarrierNumber);
        ccpGetNumber = (CountryCodePicker) findViewById(R.id.ccp_getFullNumber);
        //mob=(EditText)findViewById(R.id.num);
        email=(EditText)findViewById(R.id.mail);
        pass=(EditText)findViewById(R.id.pass);
        SelectedDateView =(TextView)findViewById(R.id.dob);


        register=(Button)findViewById(R.id.signup);
        requestQueue = Volley.newRequestQueue(this);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });
    }
    private void validate() {
        CharSequence temp_emilID=email.getText().toString();
        String userpass=pass.getText().toString().trim();
       // String usermob=mob.getText().toString().trim();
        String username= name.getText().toString().trim();
        if (username.length()==0)
        {
            name.setError("please enter your name");
        }
       else if(!isValidEmail(temp_emilID))
        {
            mail= String.valueOf(temp_emilID);
            email.requestFocus();
            email.setError("Enter Correct Mail_ID ..!!");
        }
        else if (!checkNumber())
        {
            editTextGetCarrierNumber.setError("Please enter valid number");
        }
        else if(SelectedDateView .getText().toString().matches(""))
           {
            // not null not empty
               SelectedDateView .setError("please enter your Birth date");
        }
       else if(TextUtils.isEmpty(userpass) || userpass.length() < 6)
        {
            pass.setError("You must have 6 characters in your password");
            return;

        }
        else {
            if (Config.isInternetOn(getApplicationContext())) {
                userRegister();
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
    private void userRegister() {
        utility.showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,register_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        utility.cancleDialog();
                        Log.e("response", response.toString());
                        try {
                            //Creating the json object from the response
                            JSONObject jsonResponse = new JSONObject(response);
                            if(jsonResponse.getString("status").equalsIgnoreCase("success")){
                                Toasty.success(getApplicationContext(),jsonResponse.get("messages").toString(),Toast.LENGTH_LONG).show();
                                  JSONObject userdata=jsonResponse.getJSONObject("user_id");
                            //  String userId=userdata.getString("id");
                               /* otpCode =userdata.getInt("otp_code");*/
                                //session.setLogin(true);
                               // session.setUserId(userId);
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
                                String password=pass.getText().toString();
                                /*showOTPDialog(otpCode,password);*/
                                Intent i2=new Intent(Register.this, MainLogin.class);
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

                params.put("first_name", name.getText().toString());
                params.put("email_id",email.getText().toString());
                params.put("mobile_no",ccpGetNumber.getFullNumber());
                params.put("dob",SelectedDateView .getText().toString());
                params.put("password",pass.getText().toString());
                Log.e("requesting", "Posting params: " + params.toString());
                return params;
            }
        };
        //Adding request the the queue
        requestQueue.add(stringRequest);
    }
    public void openLogin(View view)
    {
        Intent i=new Intent(Register.this,Login.class);
        startActivity(i);
    }


    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            DatePickerDialog datepickerdialog = new DatePickerDialog(getActivity(),
                    AlertDialog.THEME_HOLO_DARK,this,year,month,day);
            return datepickerdialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {

            String date=(month + 1) + "-" + day + "-" + year;
            SelectedDateView.setText(date);
        }

    }
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
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

    private void verifyOtp(String otpCode,String password) {
        if (!otpCode.isEmpty()) {
            Intent grapprIntent = new Intent(getApplicationContext(), HttpService.class);
            grapprIntent.putExtra("otp",otpCode);
            grapprIntent.putExtra("password",password);
            startService(grapprIntent);
        } else {
            Toast.makeText(getApplicationContext(), "Please enter the OTP", Toast.LENGTH_SHORT).show();
        }
    }

    public void showOTPDialog(final int Code,final String password) {
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
                String otps=String.valueOf(Code);
                if (otp.getText().length() == 0) {
                    otp.setError("Enter Otp");
                    otp.requestFocus();
                } else {
                    if (otp.getText().toString().equalsIgnoreCase(otps)) {
                        verifyOtp(otp.getText().toString().trim(),password);
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
    }
