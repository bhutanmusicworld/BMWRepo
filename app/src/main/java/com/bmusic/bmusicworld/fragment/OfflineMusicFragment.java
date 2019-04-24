package com.bmusic.bmusicworld.fragment;


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.bmusic.bmusicworld.R;
import com.bmusic.bmusicworld.base.BaseFragment;
import com.bmusic.bmusicworld.demo.Config;
import com.bmusic.bmusicworld.demo.Utilities;
import com.bmusic.bmusicworld.downloads.SongsManager;
import com.bmusic.bmusicworld.mediaservice.MyMediaPlayer;
import com.bmusic.bmusicworld.playlist.FinalSongFragment;
import com.bmusic.bmusicworld.sessionmagement.SessionManagement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dm.audiostreamer.AudioStreamingManager;


/**
 * A simple {@link Fragment} subclass.
 */
public class OfflineMusicFragment extends BaseFragment implements MyMediaPlayer.OnCompletionListener, SeekBar.OnSeekBarChangeListener {

    public static OfflineMusicFragment newInstance(Bundle bundle) {

        OfflineMusicFragment newFragment = new OfflineMusicFragment();
        newFragment.setArguments(bundle);

        return newFragment;
    }
    @BindView(R.id.textRefreash)
    TextView textRefreash;
    @BindView(R.id.list_empty)
    FrameLayout list_empty;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.song_img)
    ImageView songimgs;
    @BindView(R.id.songTitle1)
    TextView songTitleLabel;
    @BindView(R.id.rbg)
    RelativeLayout rbg;
    @BindView(R.id.allplay)
    Button allplay;
    @BindView(R.id.btnPlaylist)
    Button btnPlaylist;
    @BindView(R.id.butn)
    LinearLayout butn;
    @BindView(R.id.btnRepeat)
    ImageButton btnRepeat;
    @BindView(R.id.btnShuffle)
    ImageButton btnShuffle;
    @BindView(R.id.songCurrentDurationLabel)
    TextView songCurrentDurationLabel;
    @BindView(R.id.songTotalDurationLabel)
    TextView songTotalDurationLabel;
    @BindView(R.id.timerDisplay)
    LinearLayout timerDisplay;
    @BindView(R.id.songProgressBar)
    SeekBar songProgressBar;
    @BindView(R.id.btnPrevious)
    ImageButton btnPrevious;
    @BindView(R.id.btnBackward)
    ImageButton btnBackward;
    @BindView(R.id.loader)
    ProgressBar loader;
    @BindView(R.id.btnPlay)
    ImageButton btnPlay;
    @BindView(R.id.btnForward)
    ImageButton btnForward;
    @BindView(R.id.btnNext)
    ImageButton btnNext;
    @BindView(R.id.player_footer_bg)
    LinearLayout playerFooterBg;
    @BindView(R.id.songThumbnail)
    LinearLayout songThumbnail;
    @BindView(R.id.search)
    EditText search;
    @BindView(R.id.mysearch)
    Button mysearch;
    @BindView(R.id.searchlayout)
    RelativeLayout searchlayout;
    @BindView(R.id.navigation)
    BottomNavigationView navigation;
    Unbinder unbinder;

    private Handler mHandler = new Handler();
    ;
    private SongsManager songManager;
    private Utilities utils;


    private int seekForwardTime = 5000; // 5000 milliseconds
    private int seekBackwardTime = 5000; // 5000 milliseconds
    private int currentSongIndex = 0;
    private boolean isShuffle = false;
    private boolean isRepeat = false;
    private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
    private RequestQueue requestQueue;
    SessionManagement session;
    BroadcastReceiver receiver;

    private static final int STORAGE_PERMISSION_CODE =10 ;
    MediaMetadataRetriever metaRetriver;
    byte[] art;
    int index=0;
    // Media Player
    MyMediaPlayer mediaPlayer;
    private AudioStreamingManager streamingManager;
    public static OfflineMusicFragment newInstance() {
        return new OfflineMusicFragment();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle !=null)
        {
            index=   bundle.getInt("index");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.offlineplayer_frag, container, false);
        unbinder = ButterKnife.bind(this, view);

        requestStoragePermission();
        AudioStreamingManager.getInstance(getActivity()).cleanupPlayer(true,true);
        mediaPlayer = MyMediaPlayer.getInstance();
        songManager = new SongsManager();
        utils = new Utilities();

        // Listeners
        songProgressBar.setOnSeekBarChangeListener(this); // Important

        // Getting all songs list
        // songsList = songManager.getPlayList();
        songsList = songManager.getFolder();
//        // By default play first song
        if(songsList != null && !songsList.isEmpty()) {
            mediaPlayer.setOnCompletionListener(this); // Important
            if (index==0)
            {
                playSong(0);
            }else
            {
                playSong(index);
            }
        }
        else {
            songThumbnail.setVisibility(View.GONE);
            list_empty.setVisibility(View.VISIBLE);
        }
        /**
         * Play button click event
         * plays a song and changes button to pause image
         * pauses a song and changes button to play image
         * */
        btnPlay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(songsList != null && !songsList.isEmpty()) {
                    // check for already playing
                    if (mediaPlayer.isPlaying()) {
                        if (mediaPlayer != null) {
                            mediaPlayer.pause();
                            // Changing button image to play button
                            btnPlay.setImageResource(R.drawable.bigplays);
                        }
                    } else {
                        // Resume song
                        if (mediaPlayer != null) {
                            mediaPlayer.start();
                            // Changing button image to pause button
                            btnPlay.setImageResource(R.drawable.bigpauses);
                        }
                    }
                }

            }
        });

        /**
         * Forward button click event
         * Forwards song specified seconds
         * */
        btnForward.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // get current song position
                if(songsList != null && !songsList.isEmpty()) {
                    int currentPosition = mediaPlayer.getCurrentPosition();
                    // check if seekForward time is lesser than song duration
                    if (currentPosition + seekForwardTime <= mediaPlayer.getDuration()) {
                        // forward song
                        mediaPlayer.seekTo(currentPosition + seekForwardTime);
                    } else {
                        // forward to end position
                        mediaPlayer.seekTo(mediaPlayer.getDuration());
                    }
                }
            }
        });

        /**
         * Backward button click event
         * Backward song to specified seconds
         * */
        btnBackward.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(songsList != null && !songsList.isEmpty()) {
                    // get current song position
                    int currentPosition = mediaPlayer.getCurrentPosition();
                    // check if seekBackward time is greater than 0 sec
                    if (currentPosition - seekBackwardTime >= 0) {
                        // forward song
                        mediaPlayer.seekTo(currentPosition - seekBackwardTime);
                    } else {
                        // backward to starting position
                        mediaPlayer.seekTo(0);
                    }  }

            }
        });

        /**
         * Next button click event
         * Plays next song by taking currentSongIndex + 1
         * */
        btnNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(songsList != null && !songsList.isEmpty()) {
                    // check if next song is there or not
                    if (currentSongIndex < (songsList.size() - 1)) {
                        playSong(currentSongIndex + 1);
                        currentSongIndex = currentSongIndex + 1;
                    } else {
                        // play first song
                        playSong(0);
                        currentSongIndex = 0;
                    }

                }}

        });

        /**
         * Back button click event
         * Plays previous song by currentSongIndex - 1
         * */
        btnPrevious.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (currentSongIndex > 0) {
                    playSong(currentSongIndex - 1);
                    currentSongIndex = currentSongIndex - 1;
                } else {
                    // play last song
                    if (songsList != null && !songsList.isEmpty()) {
                        playSong(songsList.size() - 1);
                        currentSongIndex = songsList.size() - 1;
                    }
                }
            }
        });

        /**
         * Button Click event for Repeat button
         * Enables repeat flag to true
         * */
        btnRepeat.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (isRepeat) {
                    isRepeat = false;
                    Toast.makeText(getActivity(), "Repeat is OFF", Toast.LENGTH_SHORT).show();
                    btnRepeat.setImageResource(R.drawable.repeat);
                } else {
                    // make repeat to true
                    isRepeat = true;
                    Toast.makeText(getActivity(), "Repeat is ON", Toast.LENGTH_SHORT).show();
                    // make shuffle to false
                    isShuffle = false;
                    btnRepeat.setImageResource(R.drawable.repeatpress);
                    btnShuffle.setImageResource(R.drawable.shuffle);
                }
            }
        });

        /**
         * Button Click event for Shuffle button
         * Enables shuffle flag to true
         * */
        btnShuffle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (isShuffle) {
                    isShuffle = false;
                    Toast.makeText(getActivity(), "Shuffle is OFF", Toast.LENGTH_SHORT).show();
                    btnShuffle.setImageResource(R.drawable.shuffle);
                } else {
                    // make repeat to true
                    isShuffle = true;
                    Toast.makeText(getActivity(), "Shuffle is ON", Toast.LENGTH_SHORT).show();
                    // make shuffle to false
                    isRepeat = false;
                    btnShuffle.setImageResource(R.drawable.shufflepress);
                    btnRepeat.setImageResource(R.drawable.repeat);
                }
            }
        });

        /**
         * Button Click event for Play list click event
         * Launches list activity which displays list of songs
         * */
        btnPlaylist.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                add(DownloadsFragment.newInstance());
                //goToFragmentArg(new Downloads(),true);

            }
        });



        allplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "All Songs Plays In Queue", Toast.LENGTH_LONG).show();
            }
        });
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                view.findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.action_item1:
                                add(HomeFragment.newInstance());
                                //selectedFragment = ItemOneFragment.newInstance();
                                break;
                            case R.id.action_item3:
                                add(PlaylistFragment.newInstance());
                                break;
                        }
                        return true;
                    }
                });
        return view;
    }

    @Override
    protected String getTitle() {
        return "OfflineMusic";
    }


    /**
     * Function to play a song
     *
     * @param songIndex - index of song
     */
    public void playSong(int songIndex) {
        if(songsList != null && !songsList.isEmpty()) {
            // Play song
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(songsList.get(songIndex).get("songPath"));
                mediaPlayer.prepare();
                mediaPlayer.start();
                // Displaying Song title
                String songTitle = songsList.get(songIndex).get("songTitle");
                String img = songsList.get(songIndex).get(MediaStore.Audio.Albums.ALBUM_ART);
                songTitleLabel.setText(songTitle);
                // albumdata();
                metaRetriver = new MediaMetadataRetriever();
                metaRetriver.setDataSource(songsList.get(songIndex).get("songPath"));
                try {
                    art = metaRetriver.getEmbeddedPicture();
                    Bitmap songImage = BitmapFactory.decodeByteArray(art, 0, art.length);
                    songimgs.setImageBitmap(songImage);
//                    Log.e("duration",metaRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
//                    Log.e("album",metaRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
//                    Log.e("ARTIST",metaRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
//                    Log.e("GENRE",metaRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE));

//        album.setText(metaRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
//        artist.setText(metaRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
//        genre.setText(metaRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE));
                } catch (Exception e)

                {
                    songimgs.setBackgroundColor(Color.GRAY);
                }
                // Picasso.with(getActivity()).load(img).into(songimgs);
                // Changing Button Image to pause image
                btnPlay.setImageResource(R.drawable.bigpauses);

                // set Progress bar values
                songProgressBar.setProgress(0);
                songProgressBar.setMax(100);

                // Updating progress bar
                updateProgressBar();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            Toast.makeText(getActivity(),"No Songs Available.Please Downloads First",Toast.LENGTH_LONG).show();
        }
    }

    /**
     *
     * */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {

    }

    /**
     * When user starts moving the progress handler
     */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // remove message Handler from updating progress bar
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    /**
     * When user stops moving the progress hanlder
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
        int totalDuration = mediaPlayer.getDuration();
        int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);

        // forward or backward to certain seconds
        mediaPlayer.seekTo(currentPosition);

        // update timer progress again
        updateProgressBar();
    }
    /**
     * On Song Playing completed
     * if repeat is ON play same song again
     * if shuffle is ON play random song
     */
    @Override
    public void onCompletion(MediaPlayer arg0) {

        // check for repeat is ON or OFF
        if (isRepeat) {
            // repeat is on play same song again
            playSong(currentSongIndex);
        } else if (isShuffle) {
            // shuffle is on - play a random song
            Random rand = new Random();
            currentSongIndex = rand.nextInt((songsList.size() - 1) - 0 + 1) + 0;
            playSong(currentSongIndex);
        } else {
            // no repeat or shuffle ON - play next song
            if (currentSongIndex < (songsList.size() - 1)) {
                playSong(currentSongIndex + 1);
                currentSongIndex = currentSongIndex + 1;
            } else {
                // play first song
                playSong(0);
                currentSongIndex = 0;
            }
        }
    }
    /**
     * Update timer on seekbar
     */
    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    /**
     * Background Runnable thread
     */
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            long totalDuration = mediaPlayer.getDuration();
            long currentDuration = mediaPlayer.getCurrentPosition();
            // Displaying Total Duration time
            songTotalDurationLabel.setText("" + utils.milliSecondsToTimer(totalDuration));
            // Displaying time completed playing
            songCurrentDurationLabel.setText("" + utils.milliSecondsToTimer(currentDuration));

            // Updating progress bar
            int progress = (int) (utils.getProgressPercentage(currentDuration, totalDuration));
            //Log.d("Progress", ""+progress);
            songProgressBar.setProgress(progress);

            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100);
        }
    };
    public void requestStoragePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                songsList = songManager.getFolder();
                // By default play first song
                playSong(0);
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(getActivity(), "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
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
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        unbinder.unbind();
//    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
