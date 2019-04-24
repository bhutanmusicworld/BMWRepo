package com.bmusic.bmusicworld.fragment;


import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.bmusic.bmusicworld.R;
import com.bmusic.bmusicworld.adapter.OfflineSongAdapter;
import com.bmusic.bmusicworld.base.BaseFragment;
import com.bmusic.bmusicworld.demo.Config;
import com.bmusic.bmusicworld.demo.Utilities;
import com.bmusic.bmusicworld.downloads.SongsManager;
import com.bmusic.bmusicworld.extra.GridSpacingItemDecoration;
import com.bmusic.bmusicworld.model.Track;
import com.bmusic.bmusicworld.sessionmagement.SessionManagement;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;


/**
 * A simple {@link Fragment} subclass.
 */
public class DownloadsFragment extends BaseFragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    public ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
    MediaMetadataRetriever metaRetriver;
    OfflineSongAdapter offlineSongAdapter;
    byte[] art;
    ArrayList<Track> offline_list;
    String title, artAtWork, length, lable, bitmap, gener;
    Bitmap songImage;
    Track track;

    Unbinder unbinder;
    Utilities utils;
    @BindView(R.id.list_empty)
    FrameLayout list_empty;
    String Atitle, Adescri, Aid,Aimage,Aurl,userid;
    private TextView addTitle,addDescription;
    private ImageView addImage;
    private ImageView addInstall,addInstall2;
    private RequestQueue requestQueue;
    private SessionManagement session;

    public static DownloadsFragment newInstance() {
        return new DownloadsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.offline_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        addTitle = (TextView) view.findViewById(R.id.add_title);
        addDescription = (TextView)view.findViewById(R.id.add_description);
        addImage = (ImageView) view.findViewById(R.id.add_logo);
        addInstall = (ImageView) view.findViewById(R.id.install);
        addInstall2 = (ImageView) view.findViewById(R.id.install2);
        metaRetriver = new MediaMetadataRetriever();
        utils = new Utilities();
        session = new SessionManagement(getActivity());
        userid = session.getUserId();
        offline_list = new ArrayList<>();
        SongsManager plm = new SongsManager();
        this.songsList = plm.getFolder();

        if (songsList != null && !songsList.isEmpty()) {
            // looping through playlist
            for (int i = 0; i < songsList.size(); i++) {
                // creating new HashMap
                HashMap<String, String> song = songsList.get(i);

                title = songsList.get(i).get("songTitle");
                track = new Track();
                track.setTitle(title);
                offline_list.add(track);

            }
        } else {
            list_empty.setVisibility(View.VISIBLE);
        }

        offlineSongAdapter = new OfflineSongAdapter(offline_list, getActivity());
        offlineSongAdapter.setOnItemClickListener(new OfflineSongAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int position, ArrayList<Track> items) {

                Bundle bundle = new Bundle();
                bundle.putInt("index", position);
               add(OfflineMusicFragment.newInstance(bundle));
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(0), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(offlineSongAdapter);
        requestQueue = Volley.newRequestQueue(getActivity());
        if (Config.isInternetOn(getActivity())) {
            // do something
            getFooterAdd();
        }
        return view;
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    protected String getTitle() {
        return "Downloads";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void getFooterAdd() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET,"http://bmusicworld.com/api/users/Manage_add_footer",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response adds", response.toString());
                        try {
                            //Creating the json object from the response
                            JSONObject jsonResponse = new JSONObject(response);
                            if(jsonResponse.getString("status").equalsIgnoreCase("success")){
                                JSONObject userdata=jsonResponse.getJSONObject("add_detail");

                                Aid=userdata.getString("id");
                                Atitle=userdata.getString("title");
                                Adescri=userdata.getString("description");
                                Aurl=userdata.getString("url");
                                Aimage=userdata.getString("image");

                                addTitle.setText(Atitle);
                                addDescription.setText(Adescri);
                                blinkText();
                                Picasso.with(getActivity()).load(Aimage).into(addImage);
                                addInstall.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        installAdd(Aid);
                                        Intent i=new Intent(Intent.ACTION_VIEW);
                                        i.setData(Uri.parse(Aurl));
                                        startActivity(i);

                                    }
                                }); addInstall2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        installAdd(Aid);
                                        Intent i=new Intent(Intent.ACTION_VIEW);
                                        i.setData(Uri.parse(Aurl));
                                        startActivity(i);

                                    }
                                });


                            }
                            else {
                                Toasty.error(getActivity(),jsonResponse.get("messages").toString(), Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toasty.error(getActivity(),error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
        //Adding request the the queue
        requestQueue.add(stringRequest);
    }
    private void installAdd(final String aid) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,"http://bmusicworld.com/api/users/Manage_add_click",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("response post add", response.toString());
                        try {
                            //Creating the json object from the response
                            JSONObject jsonResponse = new JSONObject(response);
                            if(jsonResponse.getString("status").equalsIgnoreCase("success")){
                                Toasty.success(getActivity(),jsonResponse.get("messages").toString(),Toast.LENGTH_LONG).show();
                                //  JSONObject userdata=jsonResponse.getJSONObject("user_id");
                                //  String userId=userdata.getString("id");

                            }
                            else {
                                Toasty.error(getActivity(),jsonResponse.get("messages").toString(),Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Toasty.error(getActivity(),error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("add_id", userid);
                params.put("user_id",aid);
                Log.e("requesting", "Posting params: " + params.toString());
                return params;
            }
        };
        //Adding request the the queue
        requestQueue.add(stringRequest);
    }
    private void getFullAdd() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET,"http://bmusicworld.com/api/users/Manage_add_full",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response adds", response.toString());
                        try {
                            //Creating the json object from the response
                            JSONObject jsonResponse = new JSONObject(response);
                            if(jsonResponse.getString("status").equalsIgnoreCase("success")){
                                JSONObject userdata=jsonResponse.getJSONObject("add_detail");

                                Aid=userdata.getString("id");
                                Atitle=userdata.getString("title");
                                Adescri=userdata.getString("description");
                                Aurl=userdata.getString("url");
                                Aimage=userdata.getString("image");




                            }
                            else {
                                Toasty.error(getActivity(),jsonResponse.get("messages").toString(),Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toasty.error(getActivity(),error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
        //Adding request the the queue
        requestQueue.add(stringRequest);
    }

    private void blinkText(){
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int timeToBlink = 500;    //in ms
                try{
                    Thread.sleep(timeToBlink);
                }catch (Exception e) {

                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (addInstall.getVisibility()==View.VISIBLE){

                            addInstall.setVisibility(View.GONE);
                            addInstall2.setVisibility(View.VISIBLE);
                        }else {

                            addInstall2.setVisibility(View.GONE);
                            addInstall.setVisibility(View.VISIBLE);

                        }
                        blinkText();
                    }
                });
            }}).start();
    }
}
