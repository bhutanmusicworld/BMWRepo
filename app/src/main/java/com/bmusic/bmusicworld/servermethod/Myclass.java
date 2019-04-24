package com.bmusic.bmusicworld.servermethod;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bmusic.bmusicworld.R;
import com.bmusic.bmusicworld.base.CustomeActivity;
import com.bmusic.bmusicworld.downloads.CheckForSDCard;
import com.bmusic.bmusicworld.model.PlayList;
import com.bmusic.bmusicworld.model.SongData;
import com.bmusic.bmusicworld.model.Track;
import com.bmusic.bmusicworld.payment.PaymentScreenPremium;
import com.bmusic.bmusicworld.utility.DownloadReceiver;
import com.bmusic.bmusicworld.utility.DownloadService;
import com.bmusic.bmusicworld.utility.DownloadSingleReceiver;
import com.bmusic.bmusicworld.utility.Utility;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

/**
 * Created by wafa on 8/22/2017.
 */

public class Myclass extends CustomeActivity {
    int id = 1;
    public  static Context mContext;
    String dsong;
    String title;
    String alldownloadurlcat="http://bmusicworld.com/api/users/download_all_songs_cat";
    String downloadurl="http://bmusicworld.com/api/users/download_single_song";
    String alldownloadurl="http://bmusicworld.com/api/users/download_all_songs";
    String addurl="http://bmusicworld.com/api/users/addto_playlist",pid,playlistid,uid;
    private ArrayList<PlayList> movieList = new ArrayList<PlayList>();
    public Utility utility;

    public  static ArrayList<Track> all_songs;

    public   Myclass()
    {

    }

    public RequestQueue requestQueue;

    public void createPlay(final String name, final String userid, final String createlist_url, final Context context, final String sid) {
        requestQueue = Volley.newRequestQueue(context);
       // final ProgressDialog loading = ProgressDialog.show(this, "Registering", "Please wait...", false, false);
        //Again creating the string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST,createlist_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                      //  loading.dismiss();
                        Log.e("response", response.toString());
                        try {
                            //Creating the json object from the response
                            JSONObject jsonResponse = new JSONObject(response);
                            String    usrStatus=jsonResponse.getString("status");

                            if(jsonResponse.getString("status").equalsIgnoreCase("success")){
                                JSONArray array=jsonResponse.getJSONArray("messages");
                                for (int i=0;i<array.length();i++)
                                {
                                    JSONObject obj=array.getJSONObject(i);
                                    PlayList play = new PlayList();
                                    String title=obj.getString("playlist_name");
                                    pid=obj.getString("id");
                                    playlistid = pid;
                                    play.setPlaylistid(pid);
                                    play.setPlaylistname(title);
                                    movieList.add(play);
                                    addtoplaylist(pid,sid,userid,addurl,context);
                                    Log.e("response", pid+title);
                                }

                               // addtoplaylist(playlistid,songid,uid,url,mContext);
                            }
                            else {
                                Log.e("messages", jsonResponse.get("messages").toString());
                                //Toast.makeText(CategoryList.this, jsonResponse.get("messages").toString(), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                      //  loading.dismiss();
                        Log.e("messages",error.getMessage());
                        //Toast.makeText(CategoryList.this, error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("playlist_name", name);
                params.put("user_id",userid);
                Log.e("requesting", "Posting params: " + params.toString());
                return params;
            }
        };
        //Adding request the the queue
        requestQueue.add(stringRequest);
    }



        public void addtoplaylist (final String playlistid, final String songid, final String userid, final String createlist_url, final Context context){
            requestQueue = Volley.newRequestQueue(context);
            // final ProgressDialog loading = ProgressDialog.show(this, "Registering", "Please wait...", false, false);
            //Again creating the string request
            StringRequest stringRequest = new StringRequest(Request.Method.POST, createlist_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //  loading.dismiss();
                            Log.e("response on playlist", response.toString());
                            try {
//                                //Creating the json object from the response
                                JSONObject jsonResponse = new JSONObject(response);
                                String ustatus=jsonResponse.getString("status");

                                if (ustatus.equalsIgnoreCase("success")) {
                                    Toast.makeText(context, jsonResponse.getString("messages"), Toast.LENGTH_LONG).show();

                                } else {
                                    Log.e("messages", jsonResponse.get("messages").toString());
                                    Toast.makeText(Myclass.this, jsonResponse.get("messages").toString(), Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //  loading.dismiss();
                            Log.e("messages", error.getMessage());
                            //Toast.makeText(CategoryList.this, error.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("song_id", songid);
                    params.put("user_id", userid);
                    params.put("playlist_id", playlistid);
                    Log.e("requesting", "Posting params: " + params.toString());
                    return params;
                }
            };
            //Adding request the the queue
            requestQueue.add(stringRequest);
        }
    private void startDownload(){

        Intent intent = new Intent(this,DownloadService.class);
        startService(intent);

    }
    public void downloadsong(final String songids, final String uid, final Context context) {
        mContext=context;
       // utility=new Utility(context);
      //  utility.showDialog();
        requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,downloadurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       // utility.cancleDialog();
                        Log.e("response===", response.toString());
                        try {
                            //Creating the json object from the response
                            JSONObject jsonResponse = new JSONObject(response);
                            String    usrStatus=jsonResponse.getString("status");
                            String    msg=jsonResponse.getString("messages");
                            if(jsonResponse.getString("status").equalsIgnoreCase("success")){
                                JSONArray array=jsonResponse.getJSONArray("messages");
                                for (int i=0;i<array.length();i++)
                                {
                                    JSONObject obj=array.getJSONObject(i);
                                    String title=obj.getString("songs_title");
                                    dsong=obj.getString("song_name");
                                    Intent intent = new Intent(context, com.bmusic.bmusicworld.servermethod.DownloadService.class);
                                    intent.putExtra("url", dsong);
                                    intent.putExtra("name", title);
                                      context.startService(intent);
                                }

                            }
                            else {
                                Log.e("error===", msg);
                                 Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                                Intent i= new Intent(context, PaymentScreenPremium.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(i);
                                finish();
                               // add(PaymentScreenPremiumFragment.newInstance());
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // adapter.notifyDataSetChanged();
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
                params.put("user_id",uid);
                params.put("song_id",songids);
                Log.e("requesting", "Posting params: " + params.toString());
                return params;
            }
        };
        //Adding request the the queue
        requestQueue.add(stringRequest);
    }
    public void alldownloadsongalbum(final String albumid, final String uid, final Context context) {
        mContext=context;
        all_songs=new ArrayList<Track>();
        requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,alldownloadurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Track movie;
                        Log.e("response===", response.toString());
                        try {
                            //Creating the json object from the response
                            JSONObject jsonResponse = new JSONObject(response);
                            String    usrStatus=jsonResponse.getString("status");
                            String    msg=jsonResponse.getString("messages");
                            if(jsonResponse.getString("status").equalsIgnoreCase("success")){
                                JSONArray array=jsonResponse.getJSONArray("messages");
                                for (int i=0;i<array.length();i++)
                                {
                                    JSONObject obj=array.getJSONObject(i);
                                     title=obj.getString("songs_title");
                                    dsong=obj.getString("song_name");
                                    movie = new Track();
                                    movie.setTitle(title);
                                    movie.setStreamUrl(dsong);

//                                    MyAllDownloadingTask m = new MyAllDownloadingTask();
//                                    m.execute(dsong, title);
                                    all_songs.add(movie);

                                }
                                startService(Utility.count);
                            }
                            else {
                                Log.e("error==", msg);
                                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                                Intent i= new Intent(context, PaymentScreenPremium.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(i);
                                finish();
                              //  add(PaymentScreenPremiumFragment.newInstance());
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // adapter.notifyDataSetChanged();
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
                params.put("album_id",albumid);
                params.put("user_id",uid);
                Log.e("requesting", "Posting params: " + params.toString());
                return params;
            }
        };
        //Adding request the the queue
        requestQueue.add(stringRequest);
    }


    @Override
    protected ActionBarDrawerToggle getDrawerToggle() {
        return null;
    }

    @Override
    protected DrawerLayout getDrawer() {
        return null;
    }


    public void alldownloadsongcat(final String catid, final String uid, final Context context) {
        mContext=context;
        all_songs=new ArrayList<Track>();
        requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,alldownloadurlcat,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Track movie;
                        //loading.dismiss();
                        Log.e("response===", response.toString());
                        try {
                            //Creating the json object from the response
                            JSONObject jsonResponse = new JSONObject(response);
                            String    usrStatus=jsonResponse.getString("status");
                            String    msg=jsonResponse.getString("messages");
                            if(jsonResponse.getString("status").equalsIgnoreCase("success")){
                                JSONArray array=jsonResponse.getJSONArray("messages");
                                for (int i=0;i<array.length();i++)
                                {
                                    JSONObject obj=array.getJSONObject(i);
                                     movie = new Track();

                                    title=obj.getString("songs_title");
                                    dsong=obj.getString("song_name");
                                    Log.e("dsong",dsong);
                                    movie.setTitle(title);
                                    movie.setStreamUrl(dsong);

//                                    MyAllDownloadingTask m = new MyAllDownloadingTask();
//                                    m.execute(dsong, title);
                                    all_songs.add(movie);

                                }
                                startService(0);

                            }
                            else {
                                Log.e("error==", msg);
                                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                                Intent i= new Intent(context, PaymentScreenPremium.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(i);
                                finish();
                              //  add(PaymentScreenPremiumFragment.newInstance());
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // adapter.notifyDataSetChanged();
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
                params.put("user_id",uid);
                params.put("cat_id",catid);
                Log.e("requesting", "Posting params: " + params.toString());
                return params;
            }
        };
        //Adding request the the queue
        requestQueue.add(stringRequest);
    }

    public static void startService(int pos){
        Intent intent = new Intent(mContext, com.bmusic.bmusicworld.servermethod.DownloadService2.class);
        if (pos<=all_songs.size()){
            intent.putExtra("url", all_songs.get(pos).getStreamUrl());
            intent.putExtra("name", all_songs.get(pos).getTitle());
            mContext.startService(intent);
            }
         else {
                mContext.stopService(intent);
                Utility.count=0;
            }


    }
}
