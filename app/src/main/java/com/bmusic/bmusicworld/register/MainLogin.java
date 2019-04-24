package com.bmusic.bmusicworld.register;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import com.bmusic.bmusicworld.sessionmagement.SharedPrefManager;
import com.bmusic.bmusicworld.utility.Utility;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.bmusic.bmusicworld.R;
import com.bmusic.bmusicworld.sessionmagement.SessionManagement;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

/**
 * Created by wafa on 8/14/2017.
 */


public class MainLogin extends AppCompatActivity {
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private AccessToken accessToken ;
    private RequestQueue requestQueue;
    SessionManagement session;
    FrameLayout list_empty;
    private String fblogin_url="http://bmusicworld.com/api/users/fblogin";
    private final static String TAG = MainLogin.class.getName().toString();
    ImageView imageView;
    Utility utility;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainlogin);
        requestQueue = Volley.newRequestQueue(this);
        session=new SessionManagement(this);
        utility=new Utility(this);
        getAppKeyHash();
        FacebookSdk.sdkInitialize(getApplicationContext(), new FacebookSdk.InitializeCallback() {
            @Override
            public void onInitialized() {
                //AccessToken is for us to check whether we have previously logged in into
                //this app, and this information is save in shared preferences and sets it during SDK initialization
                accessToken = AccessToken.getCurrentAccessToken();
                if (accessToken == null) {
                    Log.d(TAG, "not log in yet");

                } else {
                    Log.d(TAG, "Logged in");
                    Intent main = new Intent(MainLogin.this,MainActivity.class);
                    main.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(main);

                }
            }
        });

        list_empty= (FrameLayout)findViewById(R.id.list_empty);
        // register a callback to respond to a login result,
        callbackManager = CallbackManager.Factory.create();

        //register access token to check whether user logged in before
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
                accessToken = newToken;
            }
        };



        accessTokenTracker.startTracking();

//Generate Hash Key, need get this key update
        // into https://developers.facebook.com/quickstarts/1584671128490867/?platform=android
        //showHashKey(this);
    }
    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);
        //Facebook login
        callbackManager.onActivityResult(requestCode, responseCode, intent);

    }

    @Override
    protected void onPause() {

        super.onPause();
    }

    protected void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
    }
    private void GetUserInfo(){
        //this code will help us to obtain information from facebook, if
        //need some other field which not show here, please refer to https://developers.facebook.com/docs/graph-api/using-graph-api/
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        // Application code
                        try{
                            //String gender = object.getString("gender");
                            String name = object.getString("name");
                            String id = object.getString("id");
                            String email=object.getString("email");
                            Log.e("data===================",email);
                            if (Config.isInternetOn(getApplicationContext())) {
                                fbloginApp(email,id,name);
                            }else {
                                Toasty.warning(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                            }
                            // LoginManager.getInstance().logOut();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email");
        request.setParameters(parameters);
        request.executeAsync();

    }

    public void fbLogin(View view)
    {
        if (Config.isInternetOn(getApplicationContext())) {
            //Set permission to use in this app
            List<String> permissionNeeds = Arrays.asList("user_friends","email","user_birthday");
            // loginButton.setReadPermissions(permissionNeeds);
            LoginManager.getInstance().logInWithReadPermissions(this, permissionNeeds);
            // LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("user_photos", "email", "public_profile" , "AccessToken"));
            // LoginManager.getInstance().logInWithPublishPermissions(this, Arrays.asList("publish_actions"));
            LoginManager.getInstance().registerCallback(callbackManager,
                    new FacebookCallback<LoginResult>()
                    {
                        @Override
                        public void onSuccess(LoginResult loginResult)
                        {
                            // App code
                            list_empty.setVisibility(View.VISIBLE);
                            accessToken = loginResult.getAccessToken();

                            GetUserInfo();
                        }

                        @Override
                        public void onCancel()
                        {
                            // App code
                        }

                        @Override
                        public void onError(FacebookException exception)
                        {
                            LoginManager.getInstance().logOut();
                            // App code
                        }
                    });
        }else {
            Toasty.warning(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }

    }





    public void openLogin(View v)
    {
        Intent i=new Intent(MainLogin.this,Login.class);
        startActivity(i);
    }
    public void openRegister(View v)
    {
        Intent i=new Intent(MainLogin.this,Register.class);
        startActivity(i);
    }
    public void openMobile(View v)
    {
        Intent i=new Intent(MainLogin.this,MobileLogin.class);
        startActivity(i);
    }

    private void fbloginApp(final String email,final String fbid,final String name) {
        utility.showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,fblogin_url,
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
                                Intent i2=new Intent(MainLogin.this, MainActivity.class);
                                startActivity(i2);
                            }
                            else{

                                LoginManager.getInstance().logOut();
                                Intent i2=new Intent(MainLogin.this, MainActivity.class);
                                startActivity(i2);
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
                        Toasty.error(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email_id",email);
                params.put("fb_auth",fbid);
                params.put("firstname",name);
                Log.e("requesting", "Posting params: " + params.toString());
                return params;
            }
        };
        //Adding request the the queue
        requestQueue.add(stringRequest);
    }
    private void getAppKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;

                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                System.out.println("HASH  " + something);
                Log.e("hashkey",something);

            }
        } catch (PackageManager.NameNotFoundException e1) {
            // TODO Auto-generated catch block
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {

            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }

}
