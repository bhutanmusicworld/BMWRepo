package com.bmusic.bmusicworld.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import com.bmusic.bmusicworld.adapter.PlayListAdapter;
import com.bmusic.bmusicworld.base.BaseFragment;
import com.bmusic.bmusicworld.demo.Config;
import com.bmusic.bmusicworld.model.PlayList;
import com.bmusic.bmusicworld.register.MainLogin;
import com.bmusic.bmusicworld.sessionmagement.SessionManagement;
import com.bmusic.bmusicworld.utility.Utility;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
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
public class PlaylistFragment extends BaseFragment implements PlayListAdapter.customButtonListener{


    @BindView(R.id.textRefreash)
    TextView textRefreash;
    @BindView(R.id.openlogin)
    TextView openlogin;
    @BindView(R.id.list_empty)
    FrameLayout list_empty;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.list_empty2)
    FrameLayout list_empty2;
    @BindView(R.id.list_empty3)
    FrameLayout list_empty3;
    @BindView(R.id.mylist)
    ListView listView;
    @BindView(R.id.frame)
    RelativeLayout frame;
    Unbinder unbinder;

    PlayListAdapter adapter;
    private String playlisturl="http://bmusicworld.com/api/users/playlist";
    private ArrayList<PlayList> movieList;
    private RequestQueue requestQueue;
    private TextView addTitle,addDescription;
    private ImageView addImage;
    private ImageView addInstall,addInstall2;
    SessionManagement session;
    String userid;
    String Atitle, Adescri, Aid,Aimage,Aurl;
    Utility utility;

    public static PlaylistFragment newInstance() {
        return new PlaylistFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = Volley.newRequestQueue(getActivity());

        session = new SessionManagement(getActivity());
        utility=new Utility(getActivity());
        userid = session.getUserId();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.act_main, container, false);
        unbinder = ButterKnife.bind(this, view);
        addTitle = (TextView) view.findViewById(R.id.add_title);
        addDescription = (TextView)view.findViewById(R.id.add_description);
        addImage = (ImageView) view.findViewById(R.id.add_logo);
        addInstall = (ImageView) view.findViewById(R.id.install);
        addInstall2 = (ImageView) view.findViewById(R.id.install2);
        if (Config.isInternetOn(getActivity())) {
            getFooterAdd();
        }
        openlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(), MainLogin.class);
                startActivity(i);
            }
        });
        movieList = new ArrayList<>();
        adapter = new PlayListAdapter(getActivity(),R.layout.mlistadapter,movieList);
        adapter.setCustomButtonListner(this);
        if (userid != null && !userid.isEmpty()) {
            // doSomething
            if (Config.isInternetOn(getActivity())) {
                playlist(userid);
            }else {
                list_empty3.setVisibility(View.VISIBLE);
                Toasty.warning(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
            }

        } else
        {
            //Set Base Adapter
            Toasty.warning(getActivity(),"Please Login First",Toast.LENGTH_LONG).show();
            list_empty.setVisibility(View.VISIBLE);
        }
        listView.setAdapter(adapter);

        return view;
    }
    private void playlist(final String id) {
        utility.showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,playlisturl,
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
                                JSONArray array=jsonResponse.getJSONArray("messages");
                                for (int i=0;i<array.length();i++)
                                {
                                    JSONObject obj=array.getJSONObject(i);
                                    PlayList list = new PlayList();
                                    // movie.setThumbnailUrl(obj.getString("img_name"));
                                    String pid=obj.getString("playlist_id");
                                    String pname=obj.getString("playlist_name");
                                    String pimage=obj.getString("playlist_image");
                                    String scount=obj.getString("songcount");
                                    // String link=obj.getString("");
                                    list.setPlaylistid(pid);
                                    list.setPlaylistname(pname);
                                    list.setPlaylistimage(pimage);
                                    list.setSongcount(scount);
                                    movieList.add(list);
                                }

                            }
                            else {
                                list_empty2.setVisibility(View.VISIBLE);
                                Toasty.warning(getActivity(),"please make your Playlist",Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        utility.cancleDialog();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id",id);
                Log.e("requesting", "Posting params: " + params.toString());
                return params;
            }
        };
        //Adding request the the queue
        requestQueue.add(stringRequest);
    }
    @Override
    protected String getTitle() {
        return "Playlist";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onButtonClickListner(int position, String value) {

        Bundle bundle = new Bundle();
        bundle.putString("playlistid", value);
        add(PlayListSongFragment.newInstance(bundle));



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
                                Picasso.with(getActivity()).load(Aimage).into(addImage);
                                blinkText();
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
                        utility.cancleDialog();
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
                        utility.cancleDialog();
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
                    public void onErrorResponse(VolleyError error) {
                        utility.cancleDialog();
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
