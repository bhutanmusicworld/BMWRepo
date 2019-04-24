package com.bmusic.bmusicworld.utility;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bmusic.bmusicworld.AppController;
import com.bmusic.bmusicworld.R;
import com.bmusic.bmusicworld.model.PlayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utility {
    private Context context;
    private static Dialog dialog;
    private static Boolean firstTime = null;
    ArrayList myList;
    public  static int count=0;

    public Utility(Context context) {
        this.context = context;
        dialog=new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loder_dilog);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    //Connection Check In Android......
    public static boolean isOnline(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager)context.
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;

        if (connMgr != null) {

            networkInfo = connMgr.getActiveNetworkInfo();
        }

        return (networkInfo != null && networkInfo.isConnected());
    }

    // show message in snack bar..
    public static void showMessage(View view, String text, int duration) {
        final Snackbar snackbar = Snackbar.make(view, text, duration);
        View sbView = snackbar.getView();
        TextView textView = (TextView)sbView.findViewById(android

                .support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.setAction("RETRY", new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                snackbar.dismiss();
            }
        });
        // Changing message text color
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }

    //Tost Message For Success......
    public static void message(Context context,String message,int gravity){

        // Layout Inflater for inflating custom view
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // inflate the layout over view
        View layout = inflater.inflate(R.layout.toast_layout,null);


        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(message);

        Toast toast = new Toast(context);// Get Toast Context
        toast.setGravity(gravity, 0, 150);// Set
        // Toast
        // gravity
        // and
        // Fill
        // Horizoontal

        toast.setDuration(Toast.LENGTH_LONG);// Set Duration
        toast.setView(layout); // Set Custom View over toast
        toast.show();// Finally show toast
    }

    //Show Progress Dialog...
    public void showDialog(){

        dialog.show();
    }

    //Cancle Progress Dialog...
    public void cancleDialog(){
        dialog.dismiss();
    }
    public static boolean isFirstTime(Context context) {
        if (firstTime == null) {
            SharedPreferences mPreferences = context.getSharedPreferences("first_time", Context.MODE_PRIVATE);
            firstTime = mPreferences.getBoolean("firstTime", true);
            if (firstTime) {
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putBoolean("firstTime", false);
                editor.commit();
            }
        }
        return firstTime;
    }

    public ArrayList playlist(final String userid) {

        myList = new ArrayList<>();
        String playlisturl = "http://bmusicworld.com/api/users/playlist";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, playlisturl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // loading.dismiss();
                        Log.e("response===", response.toString());
                        try {
                            //Creating the json object from the response
                            JSONObject jsonResponse = new JSONObject(response);
                            String usrStatus = jsonResponse.getString("status");
                            if (jsonResponse.getString("status").equalsIgnoreCase("success")) {

                                JSONArray array = jsonResponse.getJSONArray("messages");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject obj = array.getJSONObject(i);
                                    PlayList list = new PlayList();
                                    String pid = obj.getString("playlist_id");
                                    String pname = obj.getString("playlist_name");
                                    String pimage = obj.getString("playlist_image");
                                    list.setPlaylistid(pid);
                                    list.setPlaylistname(pname);
                                    list.setPlaylistimage(pimage);
                                    myList.add(list);
                                }
                            } else {
                                Toast.makeText(context, jsonResponse.getString("messages"), Toast.LENGTH_LONG).show();
                            }
                            //adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // loading.dismiss();
                        // Toast.makeText(MainActivity2.this, error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", userid);
                Log.e("requesting", "Posting params: " + params.toString());
                return params;
            }
        };
        //Adding request the the queue
        AppController.getInstance().addToRequestQueue(stringRequest);
        return myList;
    }



}
