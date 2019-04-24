package com.bmusic.bmusicworld.fragment;


import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import com.bmusic.bmusicworld.MainActivity;
import com.bmusic.bmusicworld.R;
import com.bmusic.bmusicworld.base.BackButtonSupportFragment;
import com.bmusic.bmusicworld.base.BaseFragment;
import com.bmusic.bmusicworld.demo.Config;
import com.bmusic.bmusicworld.horizontal.FrontRecyclerAdapter;
import com.bmusic.bmusicworld.horizontal.SectionDataModel;
import com.bmusic.bmusicworld.mediaservice.MyMediaPlayer;
import com.bmusic.bmusicworld.playlist.FinalSongFragment;
import com.bmusic.bmusicworld.register.MainLogin;
import com.bmusic.bmusicworld.servermethod.Myclass;
import com.bmusic.bmusicworld.sessionmagement.SessionManagement;
import com.bmusic.bmusicworld.slidinguppanel.SlidingUpPanelLayout;
import com.bmusic.bmusicworld.utility.Utility;
import com.bmusic.bmusicworld.widgets.CircleImageView;
import com.bmusic.bmusicworld.widgets.LineProgress;
import com.bmusic.bmusicworld.widgets.PlayPauseView;
import com.bmusic.bmusicworld.widgets.Slider;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dm.audiostreamer.AudioStreamingManager;
import dm.audiostreamer.CurrentSessionCallback;
import dm.audiostreamer.Logger;
import dm.audiostreamer.MediaMetaData;
import es.dmoral.toasty.Toasty;


public class HomeFragment extends BaseFragment implements BackButtonSupportFragment,Slider.OnValueChangedListener,CurrentSessionCallback {
    @BindView(R.id.textRefreash)
    TextView textRefreash;
    @BindView(R.id.list_empty)
    FrameLayout listEmpty;
    @BindView(R.id.my_recycler_view)
    RecyclerView myRecyclerView;
    @BindView(R.id.search_item)
    ListView searchItem;
    @BindView(R.id.image_songAlbumArtBlur)
    ImageView image_songAlbumArtBlur;
    @BindView(R.id.image_songAlbumArt)
    CircleImageView image_songAlbumArt;
    @BindView(R.id.pgCenterCircle)
    ProgressBar pgCenterCircle;
    @BindView(R.id.relative)
    RelativeLayout relative;
    @BindView(R.id.text_songName)
    TextView text_songName;
    @BindView(R.id.text_songAlb)
    TextView text_songAlb;
    @BindView(R.id.layout_media_info)
    LinearLayout layoutMediaInfo;
    @BindView(R.id.plays)
    Button plays;
    @BindView(R.id.download)
    Button download;
    @BindView(R.id.butn)
    LinearLayout butn;
    @BindView(R.id.slidepanel_time_progress)
    TextView time_progress_slide;
    @BindView(R.id.slidepanel_time_total)
    TextView time_total_slide;
    @BindView(R.id.audio_progress_control)
    Slider audioPg;
    @BindView(R.id.btn_backward)
    ImageView btn_backward;
    @BindView(R.id.btn_play)
    PlayPauseView btn_play;
    @BindView(R.id.pgPlayPause)
    ProgressBar pgPlayPause;
    @BindView(R.id.pgPlayPauseLayout)
    RelativeLayout pgPlayPauseLayout;
    @BindView(R.id.btn_forward)
    ImageView btn_forward;
    @BindView(R.id.img_bottom_albArt)
    ImageView img_bottom_albArt;
    @BindView(R.id.txt_bottom_SongName)
    TextView txt_bottom_SongName;
    @BindView(R.id.txt_bottom_SongAlb)
    TextView txt_bottom_SongAlb;
    @BindView(R.id.lineProgress)
    LineProgress lineProgress;
    @BindView(R.id.progres)
    ProgressBar progres;
    @BindView(R.id.slidepanel_time_progress_bottom)
    TextView time_progress_bottom;
    @BindView(R.id.slidepanel_time_total_bottom)
    TextView time_total_bottom;
    @BindView(R.id.progres_complete)
    LinearLayout progres_complete;
    @BindView(R.id.bottombar_img_Favorite)
    ImageView bottombarImgFavorite;
    @BindView(R.id.bottombar_moreicon)
    ImageView bottombarMoreicon;
    @BindView(R.id.rel_bottombar_moreicon)
    RelativeLayout relBottombarMoreicon;
    @BindView(R.id.slideBottomView)
    RelativeLayout slideBottomView;
//    @BindView(R.id.dragView)
//    RelativeLayout dragView;
    @BindView(R.id.sliding_layout)
    SlidingUpPanelLayout mLayout;
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
   // RelativeLayout slideBottomView;
    private boolean consumingBackPress = true;

    BroadcastReceiver receiver;
    SessionManagement session;
    Utility utility;
    String userid,song_id;
    String url2 ="http://bmusicworld.com/api/users/get_all_cat_songs";
    private RequestQueue requestQueue;
    FrontRecyclerAdapter adapter;
    private ArrayList<SectionDataModel> allSampleData;
    private AudioStreamingManager streamingManager;
    private MediaMetaData currentSong;
    private MyMediaPlayer   mediaPlayer;
    private List<MediaMetaData> listOfSongs = new ArrayList<MediaMetaData>();
    private String Aid,Atitle,Adiscription,Aurl,Aimage;

    private boolean isExpand = false;

    private DisplayImageOptions options;
    int counter;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    AlertDialog alertDialog;
    public Button install,install2;

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.content_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        session = new SessionManagement(getActivity());
        utility=new Utility(getActivity());
        userid=session.getUserId();
        requestQueue = Volley.newRequestQueue(getActivity());
        getFullAdd();
        allSampleData = new ArrayList<SectionDataModel>();



        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getActivity()));
        boolean value=isConnectingToInternet();
        if ("true".equalsIgnoreCase(String.valueOf(value))) {
            getFullAdd();
            // do something
            MyViewTask viewtask = new MyViewTask();
            viewtask.execute(url2);
        } else {
            // do something else
            listEmpty.setVisibility(View.VISIBLE);
            Toasty.warning(getActivity(),"No Internet Connection Available",Toast.LENGTH_LONG).show();
        }
        configAudioStreamer();
        uiInitialization(view);
        loadMusicData();
        return view;
    }
    private void configAudioStreamer() {
        streamingManager = AudioStreamingManager.getInstance(getActivity());
        //Set PlayMultiple 'true' if want to playing sequentially one by one songs
        // and provide the list of songs else set it 'false'
        streamingManager.setPlayMultiple(true);
        streamingManager.setMediaList(listOfSongs);
        //If you want to show the Player Notification then set ShowPlayerNotification as true
        //and provide the pending intent so that after click on notification it will redirect to an activity
        streamingManager.setShowPlayerNotification(true);
        streamingManager.setPendingIntentAct(getNotificationPendingIntent());
    }

    private void uiInitialization(View view) {
        pgPlayPauseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                return;
            }
        });
        changeButtonColor(btn_backward);
        changeButtonColor(btn_forward);
//
//        slideBottomView = (RelativeLayout)view.findViewById(R.id.slideBottomView);
        slideBottomView.setVisibility(View.VISIBLE);
        slideBottomView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
            }
        });
        audioPg.setMax(0);
        audioPg.setOnValueChangedListener(this);

        mLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                if (slideOffset == 0.0f) {
                    isExpand = false;
                    slideBottomView.setVisibility(View.VISIBLE);
                    //slideBottomView.getBackground().setAlpha(0);
                } else if (slideOffset > 0.0f && slideOffset < 1.0f) {
                    //slideBottomView.getBackground().setAlpha((int) slideOffset * 255);
                } else {
                    //slideBottomView.getBackground().setAlpha(100);
                    isExpand = true;
                    slideBottomView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPanelExpanded(View panel) {
                isExpand = true;
            }

            @Override
            public void onPanelCollapsed(View panel) {
                isExpand = false;
            }

            @Override
            public void onPanelAnchored(View panel) {
            }

            @Override
            public void onPanelHidden(View panel) {
            }
        });

        this.options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.bg_default_album_art)
                .showImageForEmptyUri(R.drawable.bg_default_album_art)
                .showImageOnFail(R.drawable.bg_default_album_art).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
    }
    private void loadMusicData() {

        //configAudioStreamer();
        checkAlreadyPlaying();

    }
    @Override
    protected String getTitle() {
        return "Songs";
    }
    private void changeButtonColor(ImageView imageView) {
        try {
            int color = Color.BLACK;
            imageView.setColorFilter(Color.parseColor("#ffda27"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.download, R.id.btn_forward, R.id.btn_backward, R.id.btn_play,R.id.plays})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_forward:
                streamingManager.onSkipToNext();
                break;
            case R.id.btn_backward:
                streamingManager.onSkipToPrevious();
                break;
            case R.id.btn_play:
                if (currentSong != null) {
                    playPauseEvent(view);
                }
                break;
            case R.id.plays:
                Toasty.success(getActivity(),"All songs Plays in Queue",Toast.LENGTH_LONG).show();
                break;
            case R.id.download:
                if(session.isLoggedIn()){
                    if (streamingManager.isPlaying()) {
                        Myclass m = new Myclass();
                        if (song_id!=null) {
                            Log.e("id", song_id);
                            //  Log.e("title", media.getMediaTitle());
                            m.downloadsong(song_id, userid,getActivity());
                        }
                    }
                    else {
                        Toasty.info(getActivity(),"Please Wait Song Loaded...",Toast.LENGTH_LONG).show();
                    }
                }else
                {
                    Intent i=new Intent(getActivity(),MainLogin.class);
                    startActivity(i);
                }
                break;
        }

    }
    class MyViewTask extends AsyncTask<String,Void,String> implements  FrontRecyclerAdapter.customListener, FrontRecyclerAdapter.butnmorelistener {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            utility.showDialog();
            // linlaHeaderProgress.setVisibility(View.VISIBLE);
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

                    JSONArray categorydata=object.getJSONArray("categories");
                    for (int i=0;i<categorydata.length();i++)
                    {
                        JSONObject obj=categorydata.getJSONObject(i);

                        // movie.setThumbnailUrl(obj.getString("img_name"));
                        String   catid=obj.getString("id");

                        // String image=obj.getString("img_name");
                    }
                    JSONObject mess= object.getJSONObject("messages");

                    JSONArray oldsong= mess.getJSONArray("B Music Charts");
                    createData("21","B Music Charts",doParse(oldsong));



                    JSONArray djmusic=mess.getJSONArray("New & Hot");
                    createData("20","New & Hot",doParse(djmusic));



                    JSONArray classic=mess.getJSONArray("Top 50");
                    createData("22","Top 50",doParse(classic));


                    JSONArray Rig=mess.getJSONArray("Charts by genre");
                    createData("16","Charts by genre",doParse(Rig));


                    JSONArray Newn=mess.getJSONArray("Rigser");
                    createData("19","Rigser",doParse(Newn));


                    JSONArray Music=mess.getJSONArray("English");
                    createData("18","English",doParse(Music));



                    JSONArray Top=mess.getJSONArray("Classics");
                    createData("17","Classics",doParse(Top));





                    if(allSampleData!=null && allSampleData.size()>0) {
                        // adapter = new FrontRecyclerAdapter(context, new ArrayList<MediaMetaData>());
                        adapter = new FrontRecyclerAdapter(getActivity(), allSampleData);
                        Log.e("list_title",""+allSampleData);
                        myRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                        adapter.setButtonsListner(this);
                        adapter.setMorelistener(this);

                        myRecyclerView.setAdapter(adapter);

                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }            }
        }

        @Override
        public void onmorelistener(String position, String value) {
           // checkAlreadyPlaying();


            Bundle bundle = new Bundle();
            bundle.putString("catid",position);
            bundle.putString("title",value);
            add(FinalSongFragment.newInstance(bundle));


        }

        @Override
        public void onButtonsListner(int position, MediaMetaData media, ArrayList<MediaMetaData> itemList) {
            listOfSongs = itemList;
            //song_id= media.getMediaId();
            if (session.isLoggedIn()) {
                configAudioStreamer();
                checkAlreadyPlaying();
                mediaPlayer = MyMediaPlayer.getInstance();
                if (mediaPlayer.isPlaying())
                {
                    mediaPlayer.stop();
                   // mediaPlayer.release();
                    //mediaPlayer.release();
                }
                playSong(media);
            } else{
                Intent i = new Intent(getActivity(), MainLogin.class);
                startActivity(i);
            }

        }
    }


    private PendingIntent getNotificationPendingIntent() {
        Intent intent = new Intent(getActivity(), HomeFragment.class);
        intent.setAction("openplayer");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent mPendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, 0);
        return mPendingIntent;
    }
    private  ArrayList<MediaMetaData>  doParse(JSONArray jsonArray){
        ArrayList<MediaMetaData> trackList = new ArrayList<>();
        try {
            if(jsonArray==null || jsonArray.length()==0)
                return null;
            for (int q=0;q<jsonArray.length();q++){
                JSONObject latestsonglist=jsonArray.getJSONObject(q);
                MediaMetaData track = new MediaMetaData();
                String id=latestsonglist.getString("id");
                track.setMediaId(id);
                String name=latestsonglist.getString("songs_title");
                track.setMediaTitle(name);
                String albumimage=latestsonglist.getString("song_image");
                track.setMediaArt(albumimage);
                String song=latestsonglist.getString("song_name");
                track.setMediaUrl(song);
                String length=latestsonglist.getString("songs_length");

                String lable=latestsonglist.getString("lable");
                track.setMediaAlbum(lable);
                String artist=latestsonglist.getString("music_by");
                track.setMediaArtist(artist);
                String[] units =length.split(":");
                //will break the string up into an array
                int minutes = Integer.parseInt(units[0]);
                // int minutes = Integer.parseInt(units[0]); //first element
                int seconds = Integer.parseInt(units[1]);
                int duration = 60 * minutes + seconds; //add up our values
                String time = String.valueOf(duration); //mm:ss
                track.setMediaDuration(time);
                trackList.add(track);

            }

            return trackList;
        }catch (Exception e){
            e.printStackTrace();

            return  trackList;

        }
    }


    private void createData(String id,String title, ArrayList<MediaMetaData> trackArrayList){

        if(trackArrayList!=null){
            SectionDataModel dataModel = new SectionDataModel();
            dataModel.setHeaderTitle(title);
            dataModel.setHeaderId(id);
            dataModel.setAllItemsInSection2(trackArrayList);
            allSampleData.add(dataModel);
        }
    }

    private void checkAlreadyPlaying() {
        if (streamingManager.isPlaying()) {
            currentSong = streamingManager.getCurrentAudio();
            song_id=currentSong.getMediaId();
            if (currentSong != null) {
                currentSong.setPlayState(streamingManager.mLastPlaybackState);
                showMediaInfo(currentSong);
                notifyAdapter(currentSong);
            }
        }
    }
    private void showMediaInfo(MediaMetaData media) {
        currentSong = media;
        audioPg.setValue(0);
        audioPg.setMin(0);
        audioPg.setMax(Integer.valueOf(media.getMediaDuration()) * 1000);
        setPGTime(0);
        setMaxTime();
        loadSongDetails(media);
    }
    private void loadSongDetails(MediaMetaData metaData) {
        text_songName.setText(metaData.getMediaTitle());
        text_songAlb.setText(metaData.getMediaArtist());
        txt_bottom_SongName.setText(metaData.getMediaTitle());
        txt_bottom_SongAlb.setText(metaData.getMediaArtist());

        imageLoader.displayImage(metaData.getMediaArt(), image_songAlbumArtBlur, options, animateFirstListener);
        imageLoader.displayImage(metaData.getMediaArt(), image_songAlbumArt, options, animateFirstListener);
        imageLoader.displayImage(metaData.getMediaArt(), img_bottom_albArt, options, animateFirstListener);
    }
    private void setPGTime(int progress) {
        try {
            String timeString = "00.00";
            int linePG = 0;
            currentSong = streamingManager.getCurrentAudio();
            if (currentSong != null && progress != Long.parseLong(currentSong.getMediaDuration())) {
                timeString = DateUtils.formatElapsedTime(progress / 1000);
                Long audioDuration = Long.parseLong(currentSong.getMediaDuration());
                linePG = (int) (((progress / 1000) * 100) / audioDuration);
            }
            time_progress_bottom.setText(timeString);
            time_progress_slide.setText(timeString);
            lineProgress.setLineProgress(linePG);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void setMaxTime() {
        try {
            String timeString = DateUtils.formatElapsedTime(Long.parseLong(currentSong.getMediaDuration()));
            time_total_bottom.setText(timeString);
            time_total_slide.setText(timeString);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }
    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingStarted(String imageUri, View view) {
            progressEvent(view, false);
        }

        @Override
        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
            progressEvent(view, true);
        }

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 1000);
                    displayedImages.add(imageUri);
                }
            }
            progressEvent(view, true);
        }

    }
    @Override
    public void updatePlaybackState(int state) {

        if (state==6)
        {
            progres.setVisibility(View.VISIBLE);
            progres_complete.setVisibility(View.GONE);
        }
        else if (state==2)
        {
           // progres.setVisibility(View.VISIBLE);
            progres_complete.setVisibility(View.VISIBLE);
        }
        else if (state==3)
        {
            progres.setVisibility(View.GONE);
            progres_complete.setVisibility(View.VISIBLE);
        }else
        {

        }
        Logger.e("updatePlaybackState: ", "" + state);
        switch (state) {
            case PlaybackStateCompat.STATE_PLAYING:
                pgPlayPauseLayout.setVisibility(View.INVISIBLE);
                btn_play.Play();
                if (currentSong != null) {
                    currentSong.setPlayState(PlaybackStateCompat.STATE_PLAYING);
                    notifyAdapter(currentSong);
                }
                break;
            case PlaybackStateCompat.STATE_PAUSED:
                pgPlayPauseLayout.setVisibility(View.INVISIBLE);
                btn_play.Pause();
                if (currentSong != null) {
                    currentSong.setPlayState(PlaybackStateCompat.STATE_PAUSED);
                    notifyAdapter(currentSong);
                }
                break;
            case PlaybackStateCompat.STATE_NONE:
                currentSong.setPlayState(PlaybackStateCompat.STATE_NONE);
                notifyAdapter(currentSong);
                break;
            case PlaybackStateCompat.STATE_STOPPED:
                pgPlayPauseLayout.setVisibility(View.INVISIBLE);
                btn_play.Pause();
                audioPg.setValue(0);
                if (currentSong != null) {
                    currentSong.setPlayState(PlaybackStateCompat.STATE_NONE);
                    notifyAdapter(currentSong);
                }
                break;
            case PlaybackStateCompat.STATE_BUFFERING:
                pgPlayPauseLayout.setVisibility(View.VISIBLE);
                if (currentSong != null) {
                    currentSong.setPlayState(PlaybackStateCompat.STATE_NONE);
                    notifyAdapter(currentSong);
                }
                break;
        }
    }

    @Override
    public void playSongComplete() {
        String timeString = "00.00";
        time_total_bottom.setText(timeString);
        time_total_slide.setText(timeString);
        time_progress_bottom.setText(timeString);
        time_progress_slide.setText(timeString);
        lineProgress.setLineProgress(0);
        audioPg.setValue(0);
    }

    @Override
    public void currentSeekBarPosition(int progress) {
        audioPg.setValue(progress);
        setPGTime(progress);
    }

    @Override
    public void playCurrent(int indexP, MediaMetaData currentAudio) {

        song_id= currentAudio.getMediaId();;
        String title= currentAudio.getMediaTitle();
        Log.e("Current_index",song_id);
        Log.e("Current_title",title);
        Log.e("Current_url",currentAudio.getMediaUrl());
        showMediaInfo(currentAudio);
        notifyAdapter(currentAudio);
    }

    @Override
    public void playNext(int indexP, MediaMetaData currentAudio) {
        song_id= currentAudio.getMediaId();
        String title= currentAudio.getMediaTitle();
        Log.e("Current_index",song_id);
        Log.e("Current_title",title);
        if (Aid!=null)
        showAdd();
        showMediaInfo(currentAudio);
    }

    @Override
    public void playPrevious(int indexP, MediaMetaData currentAudio) {
        song_id= currentAudio.getMediaId();
        String title= currentAudio.getMediaTitle();;
        if (Aid!=null)
        showAdd();
        showMediaInfo(currentAudio);
    }
    @Override
    public void onValueChanged(int value) {

        streamingManager.onSeekTo(value);
        streamingManager.scheduleSeekBarUpdate();
    }

    public void notifyAdapter(MediaMetaData media) {
        if (adapter!=null)
        {
         adapter.notifyPlayState(media);
          }

    }

    private void playPauseEvent(View v) {
        if (streamingManager.isPlaying()) {
            streamingManager.onPause();
            ((PlayPauseView) v).Pause();
        } else {
            streamingManager.onPlay(currentSong);
            ((PlayPauseView) v).Play();
        }
    }

    private void playSong(MediaMetaData media) {
        if (streamingManager != null) {
            streamingManager.onPlay(media);
            showMediaInfo(media);
        }
    }
    private static void progressEvent(View v, boolean isShowing) {
        try {
            View parent = (View) ((ImageView) v).getParent();
            ProgressBar pg = (ProgressBar) parent.findViewById(R.id.pg);
            if (pg != null)
                pg.setVisibility(isShowing ? View.GONE : View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //Check if internet is present or not
    private boolean isConnectingToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager
                .getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        else {
            return false;
        }
    }
    @Override
    public boolean onBackPressed() {
        if (isExpand)
        {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            return true;
            //consumed
        } else if (consumingBackPress)
        {
            callAlert();
            consumingBackPress = false;
            return true;

        }
        return false; //delegated
    }

    private void goToFragmentArg(Fragment fragment, boolean addToBackStack, Bundle bundle) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        fragment.setArguments(bundle);
        transaction.replace(R.id.frame, fragment).commit();

    }
    private void callAlert()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.AlertDialogStyle);
        builder.setIcon(R.drawable.login);
        builder.setTitle("Close Application");
        builder.setMessage("Do you want to exit?");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {

                AudioStreamingManager.getInstance(getActivity()).cleanupPlayer(true,true);
                MyMediaPlayer.getInstance().release();
                MainActivity.getInstance().finish();
            }

        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                consumingBackPress = true;

                dialog.dismiss();

            }
        });
        alertDialog = builder.create();
        alertDialog.getWindow().getAttributes().windowAnimations=R.style.CustomAnimations_slide;
        alertDialog.show();
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            if (streamingManager != null) {
                Log.e("s","1");
                streamingManager.subscribesCallBack(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onResume() {
        super.onResume();


        Log.e("s","2");
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver,
                new IntentFilter(Config.PUSH_NOTIFY));

    }
    @Override
    public void onPause() {
        super.onPause();

        Log.e("s","3");
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);

    }

    public void showAdd() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.a_big);
       dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        final TextView title = (TextView) dialog.findViewById(R.id.title);
        final TextView descri = (TextView) dialog.findViewById(R.id.description);
        final Button close = (Button) dialog.findViewById(R.id.close);
         install = (Button) dialog.findViewById(R.id.install);
         install2 = (Button) dialog.findViewById(R.id.install2);

        final ImageView logo = (ImageView) dialog.findViewById(R.id.logo);
        final ImageView bigImg = (ImageView) dialog.findViewById(R.id.big_img);

        title.setText(Atitle);
        descri.setText(Adiscription);
        Picasso.with(getActivity()).load(Aimage).into(logo);
        Picasso.with(getActivity()).load(Aimage).into(bigImg);
        blinkText();
        install.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                installAdd(Aid);
                Intent i=new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(Aurl));
                startActivity(i);
                }

        });
        bigImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                installAdd(Aid);
                Intent i=new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(Aurl));
                startActivity(i);
                }

        });
        install2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                installAdd(Aid);
                Intent i=new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(Aurl));
                startActivity(i);
                }

        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

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




    @Override
    public void onStop() {
        try {
            if (streamingManager != null) {
                Log.e("s","4");
                streamingManager.unSubscribeCallBack();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onStop();
    }
    @Override
    public void onDestroy() {
        try {
            if (streamingManager != null) {
                Log.e("s","5");
                streamingManager.unSubscribeCallBack();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
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
                                   Adiscription=userdata.getString("description");
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
                        utility.cancleDialog();
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
                        if (install.getVisibility()==View.VISIBLE){

                            install.setVisibility(View.GONE);
                            install2.setVisibility(View.VISIBLE);
                        }else {

                            install2.setVisibility(View.GONE);
                            install.setVisibility(View.VISIBLE);

                        }
                        blinkText();
                    }
                });
            }}).start();
    }
}
