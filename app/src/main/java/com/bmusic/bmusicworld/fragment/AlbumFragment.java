package com.bmusic.bmusicworld.fragment;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bmusic.bmusicworld.R;
import com.bmusic.bmusicworld.base.BaseFragment;
import com.bmusic.bmusicworld.demo.Config;
import com.bmusic.bmusicworld.horizontal.RecyclerViewDataAdapter;
import com.bmusic.bmusicworld.horizontal.SectionDataModel;
import com.bmusic.bmusicworld.model.Track;
import com.bmusic.bmusicworld.playlist.FinalSongFragment;
import com.bmusic.bmusicworld.utility.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;


/**
 * A simple {@link Fragment} subclass.
 */
public class AlbumFragment extends BaseFragment {


    @BindView(R.id.textRefreash)
    TextView textRefreash;
    @BindView(R.id.list_empty)
    FrameLayout list_empty;
    @BindView(R.id.my_recycler_view)
    RecyclerView my_recycler_view;
    @BindView(R.id.selected_track_image)
    ImageView selectedTrackImage;
    @BindView(R.id.selected_track_title)
    TextView selectedTrackTitle;
    @BindView(R.id.loader)
    ProgressBar loader;
    @BindView(R.id.player_control)
    ImageView playerControl;
    @BindView(R.id.play)
    LinearLayout play;
    @BindView(R.id.progressbar)
    ProgressBar progressbar;
    @BindView(R.id.frame)
    RelativeLayout frame;
    Unbinder unbinder;

    private RequestQueue requestQueue;
    String url2 = "http://bmusicworld.com/api/users/get_all_cat_album";
    ArrayList<SectionDataModel> allSampleData;
    Utility utility;

    public static AlbumFragment newInstance() {
        return new AlbumFragment();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.content_home2, container, false);
        unbinder = ButterKnife.bind(this, view);
        requestQueue = Volley.newRequestQueue(getActivity());
        utility=new Utility(getActivity());

        allSampleData = new ArrayList<SectionDataModel>();

        if (Config.isInternetOn(getActivity())) {
            // do something
            MyViewTask viewtask = new MyViewTask();
            viewtask.execute(url2);
        } else {
            // do something else
            list_empty.setVisibility(View.VISIBLE);
            Toasty.warning(getActivity(),"No Internet Connection Available", Toast.LENGTH_LONG).show();
        }
        return view;
    }
    class MyViewTask extends AsyncTask<String,Void,String> implements RecyclerViewDataAdapter.customButtonListener {

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
            if (result != null) {
                try {
                    allSampleData = new ArrayList<>();
                    JSONObject object= new JSONObject(result);
                    String status=object.getString("status");
                    String code=object.getString("code");
                    JSONObject mess= object.getJSONObject("messages");

                    JSONArray oldsong= mess.getJSONArray("B Music Charts");
                    createData("B Music Charts",doParse(oldsong));


                    JSONArray djmusic=mess.getJSONArray("New & Hot");
                    createData("New & Hot",doParse(djmusic));

                    JSONArray classic=mess.getJSONArray("Top 50");
                    createData("Top 50",doParse(classic));


                    JSONArray Rigser=mess.getJSONArray("Charts by genre");
                    createData("Charts by genre",doParse(Rigser));

                    JSONArray New=mess.getJSONArray("Rigser");
                    createData("Rigser",doParse(New));

                    JSONArray New1=mess.getJSONArray("English");
                    createData("English",doParse(New1));

                    JSONArray New2=mess.getJSONArray("Classics");
                    createData("Classics",doParse(New2));

                    if(allSampleData!=null && allSampleData.size()>0) {
                        RecyclerViewDataAdapter adapter = new RecyclerViewDataAdapter(getActivity(), allSampleData);
                        my_recycler_view.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                        adapter.setCustomButtonListner(this);
                        my_recycler_view.setAdapter(adapter);
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }            }
        }

        @Override
        public void onButtonClickListner(int position, ArrayList<Track> value) {
            String albmid=value.get(position).getId();
            String title=value.get(position).getTitle();

            Bundle bundle = new Bundle();
            bundle.putString("albumid",albmid);
            bundle.putString("title",title);
            add(FinalSongFragment.newInstance(bundle));
        }
    }

    private  ArrayList<Track>  doParse(JSONArray jsonArray){
        ArrayList<Track> trackList = new ArrayList<>();
        try {
            if(jsonArray==null || jsonArray.length()==0)
                return null;
            for (int q=0;q<jsonArray.length();q++){

                JSONObject latestsonglist=jsonArray.getJSONObject(q);
                Track track = new Track();
                String id=latestsonglist.getString("id");
                track.setId(id);
                String name=latestsonglist.getString("name");
                track.setTitle(name);
                String albumimage=latestsonglist.getString("album_image");
                track.setSongimg(albumimage);
                trackList.add(track);

            }
            return trackList;
        }catch (Exception e){
            e.printStackTrace();
            return  trackList;

        }



    }

    private void createData(String title, ArrayList<Track> trackArrayList){

        if(trackArrayList!=null){
            SectionDataModel dataModel = new SectionDataModel();
            dataModel.setHeaderTitle(title);
            dataModel.setAllItemsInSection(trackArrayList);
            allSampleData.add(dataModel);}
    }

    @Override
    protected String getTitle() {
        return "Albums";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
