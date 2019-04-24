package com.bmusic.bmusicworld.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.bmusic.bmusicworld.adapter.GetArtist;
import com.bmusic.bmusicworld.base.BaseFragment;
import com.bmusic.bmusicworld.demo.Config;
import com.bmusic.bmusicworld.model.Track;
import com.bmusic.bmusicworld.sessionmagement.SessionManagement;
import com.bmusic.bmusicworld.utility.Utility;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
public class ArtistFragment extends BaseFragment {
    String artisturl = "http://bmusicworld.com/api/users/get_all_artist";
    @BindView(R.id.textRefreash)
    TextView textRefreash;
    @BindView(R.id.list_empty)
    FrameLayout list_empty;
    @BindView(R.id.mylist)
    ListView listView;
    Unbinder unbinder;

    GetArtist adp;
    private ArrayList<Track> mGridlist = new ArrayList<>();
    private RequestQueue requestQueue;
    Utility utility;
    String Atitle, Adescri, Aid,Aimage,Aurl,userid;
    private TextView addTitle,addDescription;
    private ImageView addImage;
    private ImageView addInstall,addInstall2;
    private SessionManagement session;



    public static ArtistFragment newInstance() {
        return new ArtistFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.all_artist, container, false);
        unbinder = ButterKnife.bind(this, view);
        session = new SessionManagement(getActivity());
        userid = session.getUserId();
        utility=new Utility(getActivity());
        addTitle = (TextView) view.findViewById(R.id.add_title);
        addDescription = (TextView)view.findViewById(R.id.add_description);
        addImage = (ImageView) view.findViewById(R.id.add_logo);
        addInstall = (ImageView) view.findViewById(R.id.install);
        addInstall2 = (ImageView) view.findViewById(R.id.install2);
        requestQueue = Volley.newRequestQueue(getActivity());
        if (Config.isInternetOn(getActivity())) {
            // do something
            getFooterAdd();
            MyTask views=new MyTask();
            views.execute(artisturl);
        } else {
            // do something else
            list_empty.setVisibility(View.VISIBLE);
            Toasty.warning(getActivity(),"No Internet Connection Available", Toast.LENGTH_LONG).show();
        }
        adp = new  GetArtist(getActivity(), R.layout.artist_list, mGridlist);
        listView.setAdapter(adp);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String search_artist=mGridlist.get(position).getArtAtWork();
                if (Config.isInternetOn(getActivity())) {

                    Bundle bundle = new Bundle();
                    bundle.putString("data",search_artist);
                    add(SearchExpandFragment.newInstance(bundle));
                }else {
                    // do something else
                    list_empty.setVisibility(View.VISIBLE);
                    Toasty.warning(getActivity(),"No Internet Connection Available", Toast.LENGTH_LONG).show();
                }
            }
        });



        return view;
    }
    class MyTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            utility.showDialog();
        }
        @Override
        protected String doInBackground(String... params) {
            String strUrl = params[0];
            String result = "";
            try {
                URL url = new URL(strUrl);
                HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
                httpCon.setRequestMethod("GET");
                httpCon.connect();
                int respCode = httpCon.getResponseCode();
                if (respCode == HttpURLConnection.HTTP_OK) {
                    InputStream is = httpCon.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader reader = new BufferedReader(isr);
                    //get all lines of servlet o/p
                    while (true) {
                        String str = reader.readLine();
                        if (str == null)
                            break;
                        result = result + str;
                    }
                }

            } catch (Exception ex) {
                Log.e("http error", ex.toString());
            }
            return result;
        }
        @Override
        protected void onPostExecute(String result) {
            utility.cancleDialog();
            Log.e("artist list", result);
            if (result != null) {
                try {
                    JSONObject Obj = new JSONObject(result);

                    JSONArray jsonArray = Obj.getJSONArray("messages");
                    System.out.println("_________________________" + jsonArray.length());

                    //get all object from array
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonobj = jsonArray.getJSONObject(i);
                        String id = jsonobj.getString("id");
                        String title = jsonobj.getString("artist_name");
                        Track m = new Track();
                        m.setId(id);
                        m.setArtAtWork(title);
                        mGridlist.add(m);

                    }
                    //  adp.setGridData(list);
                    adp.notifyDataSetChanged();
                    //  sqlite();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
    protected String getTitle() {
        return "Artist List";
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
                                });
                                addInstall2.setOnClickListener(new View.OnClickListener() {
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
