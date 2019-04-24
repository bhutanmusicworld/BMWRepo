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
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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
import com.bmusic.bmusicworld.R;
import com.bmusic.bmusicworld.adapter.FavListAdapter;
import com.bmusic.bmusicworld.adapter.NewPlayListSongsAdapter;
import com.bmusic.bmusicworld.base.BackButtonSupportFragment;
import com.bmusic.bmusicworld.base.BaseFragment;
import com.bmusic.bmusicworld.demo.Config;
import com.bmusic.bmusicworld.mediaservice.MyMediaPlayer;
import com.bmusic.bmusicworld.model.PlayList;
import com.bmusic.bmusicworld.model.Track;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class PlayListSongFragment extends BaseFragment implements BackButtonSupportFragment,NewPlayListSongsAdapter.custom,NewPlayListSongsAdapter.customButtonListener,CurrentSessionCallback, Slider.OnValueChangedListener {


    @BindView(R.id.textRefreash)
    TextView textRefreash;
    @BindView(R.id.list_empty2)
    FrameLayout list_empty2;
    @BindView(R.id.list_empty)
    FrameLayout list_empty;
    @BindView(R.id.mylist)
    ListView listView;
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
    LinearLayout layout_media_info;
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
    Unbinder unbinder;

    RelativeLayout slideBottomView;
    private ArrayList<Track> songlistList;

    SessionManagement session;
    Context context;
    private MediaMetaData currentSong;
    String img,stitle,smusicby,slable,srelease,userid,sid,songTitle,song_id;
    private RequestQueue requestQueue;
    FavListAdapter padapter;
    MyMediaPlayer mediaPlayer;
    NewPlayListSongsAdapter adapter;
    BroadcastReceiver receiver;
    private List<MediaMetaData> listOfSongs = new ArrayList<MediaMetaData>();
    List<MediaMetaData> listArticle=new ArrayList<MediaMetaData>();
    private ArrayList<PlayList> myList = new ArrayList<PlayList>();
    private AudioStreamingManager streamingManager;
    Utility utility;
    Button install,install2;
    private boolean isExpand = false;
    AlertDialog b;
    String Atitle, Adescri, Aid,Aimage,Aurl;
    private DisplayImageOptions options;
    private ArrayList<Track> mGridlist = new ArrayList<>();
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    String searchkey,pid;

    public static PlayListSongFragment newInstance(Bundle bundle) {

        PlayListSongFragment newFragment = new PlayListSongFragment();
        newFragment.setArguments(bundle);

        return newFragment;
    }

    public static PlayListSongFragment newInstance() {
        return new PlayListSongFragment();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle !=null)
        {
            searchkey=   bundle.getString("data");
             pid=   bundle.getString("playlistid");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.search_expand, container, false);
        slideBottomView = (RelativeLayout)view.findViewById(R.id.slideBottomView);
        unbinder = ButterKnife.bind(this, view);
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getActivity()));
        session = new SessionManagement(getActivity());
        utility=new Utility(getActivity());
        requestQueue = Volley.newRequestQueue(getActivity());
        userid=session.getUserId();
        context=getActivity();
        songlistList = new ArrayList<Track>();


        if (Config.isInternetOn(getActivity())) {
            // request for post
            getFullAdd();
           allplaylist(pid,userid);
        }else {
            Toasty.warning(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }

        configAudioStreamer();
        uiInitialization();
        loadMusicData();

        return view;
    }

    private void uiInitialization() {
        pgPlayPauseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                return;
            }
        });
        changeButtonColor(btn_backward);
        changeButtonColor(btn_forward);

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


        adapter=new NewPlayListSongsAdapter(getActivity(),listOfSongs);

//        songlist.setAdapter(adapter);

        adapter.setListItemListener(new NewPlayListSongsAdapter.ListItemListener() {
            @Override
            public void onItemClickListener(MediaMetaData media) {
                if (session.isLoggedIn()) {
                    mediaPlayer = MyMediaPlayer.getInstance();
                    if (mediaPlayer.isPlaying())
                    {
                        mediaPlayer.stop();
                        //mediaPlayer.release();
                    }
                    playSong(media);
                } else{
                    Intent i = new Intent(getActivity(), MainLogin.class);
                    startActivity(i);
                }

            }
        });
        adapter.setCancleButtonListner(this);
        adapter.setCustomButtonListner(this);
        listView.setAdapter(adapter);

        this.options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.bg_default_album_art)
                .showImageForEmptyUri(R.drawable.bg_default_album_art)
                .showImageOnFail(R.drawable.bg_default_album_art).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

    }

    private void loadMusicData() {
        checkAlreadyPlaying();
    }
    private void changeButtonColor(ImageView imageView) {
        try {
            int color = Color.BLACK;
            imageView.setColorFilter(Color.parseColor("#ffda27"));
        } catch (Exception e) {
            e.printStackTrace();
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
    private void notifyAdapter(MediaMetaData media) {
        adapter.notifyPlayState(media);
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
    @Override
    public void onDestroy() {
        try {
            if (streamingManager != null) {
                streamingManager.unSubscribeCallBack();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
    @Override
    public void updatePlaybackState(int state) {
        Logger.e("updatePlaybackState: ", "" + state);
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
    public void playCurrent(int i, MediaMetaData currentAudio) {
        song_id=currentAudio.getMediaId();
        showMediaInfo(currentAudio);
        notifyAdapter(currentAudio);
    }

    @Override
    public void playNext(int i, MediaMetaData currentAudio) {
        song_id=currentAudio.getMediaId();
        if (Aid!=null)
        showAdd();;
        showMediaInfo(currentAudio);
    }

    @Override
    public void playPrevious(int i, MediaMetaData currentAudio) {
        song_id=currentAudio.getMediaId();
        if (Aid!=null)
        showAdd();
        showMediaInfo(currentAudio);
    }

    @Override
    public void onValueChanged(int value) {
        streamingManager.onSeekTo(value);
        streamingManager.scheduleSeekBarUpdate();
    }
    private void playSong(MediaMetaData media) {
        if (streamingManager != null) {
            streamingManager.onPlay(media);
            showMediaInfo(media);
        }
    }

    @Override
    public void onButtonClickListner(int position, String value) {

    }
    @OnClick({R.id.btn_forward, R.id.btn_backward,R.id.btn_play, R.id.plays,R.id.download})
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
                        if(song_id!=null) {
                            Log.e("id", song_id);
                            //  Log.e("title", media.getMediaTitle());
                            m.downloadsong(song_id, userid, context);
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
    @Override
    public void onCancle(int position, String value) {
        if (Config.isInternetOn(getActivity())) {

            myList=new ArrayList<>();
            myList = utility.playlist(userid);
        }else {
            Toasty.warning(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }
        sid=value;
        img=songlistList.get(position).getSongimg();
        stitle=songlistList.get(position).getTitle();
        smusicby=songlistList.get(position).getArtAtWork();
        slable=songlistList.get(position).getLable();
        srelease=songlistList.get(position).getRelease();
        if (session.isLoggedIn()) {
            popup_window();
        } else{
            Intent i = new Intent(getActivity(), MainLogin.class);
            startActivity(i);
        }
    }
    public void popup_window() {

        View menuItemView = getView().findViewById(R.id.menu);
        PopupMenu popupMenu = new PopupMenu(getActivity(), menuItemView);
        popupMenu.getMenuInflater().inflate(R.menu.image_chooser_popup, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.add:
                        showChangeLangDialog();
                        break;

                    case R.id.info:
                        openSongInfo();
                        break;

                    default:
                        break;

                }
                return true;
            }
        });
        popupMenu.show();
    }
    public void showChangeLangDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dailog_createlist, null);
        dialogBuilder.setView(dialogView);
        //  final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);
        dialogBuilder.setTitle("Add To Playlist");
        ListView  listView=(ListView)dialogView.findViewById(R.id.mylist);
        Button addsong=(Button)dialogView.findViewById(R.id.addsong);
        addsong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showcreateDialog();
            }
        });

        padapter = new FavListAdapter(getActivity(),R.layout.mlistname,myList);
        // padapter.setCustomButtonListner(this);
        listView.setAdapter(padapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                PlayList plist=myList.get(i);
                String pid=plist.getPlaylistid();
                Myclass m=new Myclass();
                String add_url="http://bmusicworld.com/api/users/addto_playlist";
                Log.e("data",pid+sid+userid+add_url+context);
                m.addtoplaylist(pid,sid,userid,add_url,context);
                b.dismiss();
                //(name,userid,createlist_url,mContext,sid);
            }
        });
        b = dialogBuilder.create();
        b.show();
    }
    public void showcreateDialog() {
        b.dismiss();
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);

        dialogBuilder.setTitle("Add To Playlist");
        // dialogBuilder.setMessage("Enter text below");
        dialogBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String name= edt.getText().toString().trim();
                Myclass n=new Myclass();
                String createlist_url="http://bmusicworld.com/api/users/create_platlist";
                n.createPlay(name,userid,createlist_url,context,sid);
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }
    public void openSongInfo()
    {

        Bundle bundle = new Bundle();
        bundle.putString("stitle",stitle);
        bundle.putString("smusic",smusicby);
        bundle.putString("slable",slable);
       // bundle.putString("albumtitle",value);
        bundle.putString("srelease",srelease);
        bundle.putString("img",img);
        add(SongInfoFragment.newInstance(bundle));
    }



    @Override
    public void onResume() {
        super.onResume();
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
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
    }
    public void allplaylist (final  String playlistid, final String userid) {
        utility.showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,"http://bmusicworld.com/api/users/playlist_songs",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        utility.cancleDialog();

                        Log.e("response on aaplaylist", response.toString());
                        try {
                            //Creating the json object from the response
                            JSONObject jsonResponse = new JSONObject(response);
                            String    usrStatus=jsonResponse.getString("status");

                            if(jsonResponse.getString("status").equalsIgnoreCase("success")){
                                JSONArray array=jsonResponse.getJSONArray("messages");
                                for (int i=0;i<array.length();i++)
                                {
                                    JSONObject obj=array.getJSONObject(i);
                                    Track track=new Track();
                                    // SongData play = new SongData();
                                    // movie.setThumbnailUrl(obj.getString("img_name"));
                                    String title=obj.getString("songs_title");
                                    String image=obj.getString("img_name");
                                    String song=obj.getString("song_name");
                                    String id=obj.getString("id");
                                    String artist=obj.getString("music_by");
                                    String songlength=obj.getString("songs_length");
                                    String albumid=obj.getString("album_id");
                                    String release=obj.getString("release_date");
                                    String lable=obj.getString("lable");
                                    track.setId(id);
                                    track.setTitle(title);
                                    track.setSongimg(image);
                                    track.setStreamUrl(song);
                                    track.setLength(songlength);
                                    track.setArtAtWork(artist);
                                    track.setRelease(release);
                                    track.setLable(lable);
                                    songlistList.add(track);


                                    String[] units =songlength.split(":");
                                    //will break the string up into an array
                                    int minutes = Integer.parseInt(units[0]);

                                    // int minutes = Integer.parseInt(units[0]); //first element
                                    Log.e("time",""+minutes);
                                    int seconds = Integer.parseInt(units[1]);
                                    Log.e("time",""+seconds);//second element
                                    int duration = 60 * minutes + seconds; //add up our values


                                    String time = String.valueOf(duration); //mm:ss

                                    Log.e("time",time);
                                    MediaMetaData infoData = new MediaMetaData();
                                    infoData.setMediaId(id);
                                    infoData.setMediaUrl(song);
                                    infoData.setMediaTitle(title);
                                    infoData.setMediaArtist(artist);
                                    infoData.setMediaAlbum(lable);
                                    //infoData.setMediaComposer(musicObj.optString(""));
                                    infoData.setMediaDuration(time);
                                    infoData.setMediaArt(image);
                                    listArticle.add(infoData);
                                    Log.e("response", song);
                                }

                                // addtoplaylist(playlistid,songid,uid,url,mContext);
                            }
                            else {
                                Log.e("messages", jsonResponse.get("messages").toString());
                                //Toast.makeText(CategoryList.this, jsonResponse.get("messages").toString(), Toast.LENGTH_SHORT).show();
                            }
                            listOfSongs = listArticle;
                            adapter.refresh(listArticle);
                            configAudioStreamer();
                            checkAlreadyPlaying();
                            //adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        utility.cancleDialog();
                        Log.e("messages",error.getMessage());
                        //Toast.makeText(CategoryList.this, error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id",userid);
                params.put("playlist_id",playlistid);
                Log.e("requesting", "Posting params: " + params.toString());
                return params;
            }
        };
        //Adding request the the queue
        requestQueue.add(stringRequest);
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
    private void configAudioStreamer() {
        streamingManager = AudioStreamingManager.getInstance(context);
        //Set PlayMultiple 'true' if want to playing sequentially one by one songs
        // and provide the list of songs else set it 'false'
        streamingManager.setPlayMultiple(true);
        streamingManager.setMediaList(listOfSongs);
        //If you want to show the Player Notification then set ShowPlayerNotification as true
        //and provide the pending intent so that after click on notification it will redirect to an activity
        streamingManager.setShowPlayerNotification(true);
        streamingManager.setPendingIntentAct(getNotificationPendingIntent());
    }

    private PendingIntent getNotificationPendingIntent() {
        Intent intent = new Intent(context, HomeFragment.class);
        intent.setAction("openplayer");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent mPendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        return mPendingIntent;
    }



    @Override
    public void onStart() {
        super.onStart();
        try {
            if (streamingManager != null) {
                streamingManager.subscribesCallBack(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        try {
            if (streamingManager != null) {
                streamingManager.unSubscribeCallBack();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onStop();
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

    @Override
    protected String getTitle() {
        return "Playlist Songs";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    @Override
    public boolean onBackPressed() {
        if (isExpand) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            return true; //consumed
        }
        return false;
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
                        utility.cancleDialog();
                        Toasty.error(getActivity(),error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
        //Adding request the the queue
        requestQueue.add(stringRequest);
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
          blinkText();
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

        title.setText(Atitle);
        descri.setText(Adescri);
        Picasso.with(getActivity()).load(Aimage).into(logo);
        Picasso.with(getActivity()).load(Aimage).into(bigImg);
        install.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                installAdd(Aid);
                Intent i=new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(Aurl));
                startActivity(i); }

        });
        install2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                installAdd(Aid);
                Intent i=new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(Aurl));
                startActivity(i); }

        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

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
