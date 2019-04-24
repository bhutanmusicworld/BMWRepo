package com.bmusic.bmusicworld.mediaservice;

/**
 * Created by wafa on 8/30/2017.
 */


import java.io.IOException;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bmusic.bmusicworld.R;
import com.bmusic.bmusicworld.demo.Utilities;
import com.bmusic.bmusicworld.model.Track;

public class MyMediaPlayer extends MediaPlayer {

    //private static MyMediaPlayer myMediaPlayer;
    private static volatile MyMediaPlayer myMediaPlayer = null;
    private Handler mHandler = new Handler();
    private TextView songCurrentDurationLabel;
    private Utilities utils;
    private TextView songTotalDurationLabel;

    public static enum MP_STATE {
        PLAYING, PAUSED, STOPPED
    }

    private MP_STATE state;

//    public static synchronized MyMediaPlayer getInstance() {
//        if (myMediaPlayer == null) {
//            myMediaPlayer = new MyMediaPlayer();
//        }
//        return myMediaPlayer;
//    }

    public static MyMediaPlayer getInstance() {
        MyMediaPlayer localInstance = myMediaPlayer;
        if (localInstance == null) {
            synchronized (MyMediaPlayer.class) {
                localInstance = myMediaPlayer;
                if (localInstance == null) {
                    myMediaPlayer = localInstance = new MyMediaPlayer();
                }
            }
        }
        return localInstance;
    }

    private MyMediaPlayer() {
        super();
        state = MP_STATE.STOPPED;
    }

    @Override
    public void start() {
        state = MP_STATE.PLAYING;
        super.start();
    }

    @Override
    public void pause() {
        state = MP_STATE.PAUSED;
        super.pause();
    }

    @Override
    public void stop() {
        state = MP_STATE.STOPPED;
        super.stop();
        //super.reset();
    }

    //edit
    @Override
    public void prepareAsync() throws IllegalStateException {
        super.prepareAsync();
    }

    @Override
    public void prepare() throws IOException, IllegalStateException {
        super.prepare();
    }

    @Override
    public void setOnCompletionListener(OnCompletionListener listener) {
        super.setOnCompletionListener(listener);
    }

    //edit


    @Override
    public void reset() {
        super.reset();
    }

    @Override
    public void release() {
        super.release();
    }

    @Override
    public int getCurrentPosition() {
        return super.getCurrentPosition();
    }

    @Override
    public int getDuration() {
        return super.getDuration();
    }

    @Override
    public void seekTo(int msec) throws IllegalStateException {
        super.seekTo(msec);
    }
    @Override
    public void setOnPreparedListener(OnPreparedListener listener) {
        super.setOnPreparedListener(listener);
    }

    @Override
    public void setOnBufferingUpdateListener(OnBufferingUpdateListener listener) {
        super.setOnBufferingUpdateListener(listener);
    }

    /**
     * Update timer on seekbar
     * */
    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    /**
     * Background Runnable thread
     * */

    private Runnable mUpdateTimeTask = new Runnable() {

        public void run() {
            try {
                utils = new Utilities();
                long totalDuration = myMediaPlayer.getDuration();
                long currentDuration = myMediaPlayer.getCurrentPosition();

                // Displaying Total Duration time
                songTotalDurationLabel.setText(""+utils.milliSecondsToTimer(totalDuration));
                // Displaying time completed playing
                songCurrentDurationLabel.setText(""+utils.milliSecondsToTimer(currentDuration));

                // Updating progress bar
                int progress = (int)(utils.getProgressPercentage(currentDuration, totalDuration));
                //Log.d("Progress", ""+progress);
               // songProgressBar.setProgress(progress);

                // Running this thread after 100 milliseconds
                mHandler.postDelayed(this, 100);

            } catch (final Exception e) {
                e.printStackTrace();

            }
        }
    };

    public void playLocalFile(Context c, Uri filepath) {
        if (!state.equals(MP_STATE.STOPPED)) {
            myMediaPlayer.stop();
        }
        setAudioStreamType(AudioManager.STREAM_MUSIC);
        setOnPreparedListener(new MyOnPreparedListener());
        try {
            setDataSource(c, filepath);
            prepareAsync();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playRemoteFile(String url) {
        if (!state.equals(MP_STATE.STOPPED)) {
            myMediaPlayer.stop();
        }
        setAudioStreamType(AudioManager.STREAM_MUSIC);
        myMediaPlayer.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                myMediaPlayer.start();
            }
        });
      // setOnCompletionListener(new MyOnCompletionListener());
        try {
            myMediaPlayer.setDataSource(url);
            myMediaPlayer.prepareAsync();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void playMp3(String _link, Context context, final ProgressBar loader, final ImageView mPlayerControl){
        if (!state.equals(MP_STATE.STOPPED)) {
            myMediaPlayer.pause();
        }
        myMediaPlayer.reset();
//        final ProgressDialog progressDialog = ProgressDialog.show(context,
//                "Loading Song", " Please wait");
        //  Progressbar.setVisibility(View.VISIBLE);
        myMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            myMediaPlayer.setDataSource(_link);
            myMediaPlayer.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mp, int percent) {
                    if (percent < 0 || percent > 100) {
                        //System.out.println("Doing math: (" + (Math.abs(percent)-1)*100.0 + " / " +Integer.MAX_VALUE+ ")" );
                        percent = (int) Math.round((((Math.abs(percent)-1)*100.0/Integer.MAX_VALUE)));
                        // progressBar.setProgress(percent);
                    }
                    Log.i("Buffer!" , " " +percent);
                    System.out.println("Buffer:" +percent);
                }
            });
            myMediaPlayer.setOnPreparedListener(new OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    loader.setVisibility(View.GONE);
                    mPlayerControl.setVisibility(View.VISIBLE);
                    mPlayerControl.setImageResource(R.drawable.bigpauses);
//                    if (progressDialog != null && progressDialog.isShowing()){
//                        progressDialog.dismiss();
//                    }
                    mp.start();
                }
            });
            Track track=new Track();
            track.setIsbackplay(true);
            //mediaPlayer.prepare(); // might take long! (for buffering, etc)   //@@
            myMediaPlayer.prepareAsync();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block///
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void playFile(String url, final Context context) {
        if (!state.equals(MP_STATE.STOPPED)) {
            myMediaPlayer.stop();
        }
        myMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        final ProgressDialog progressDialog = ProgressDialog.show(context,
                "", "");
        progressDialog.setContentView(R.layout.loder);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myMediaPlayer.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if (progressDialog != null && progressDialog.isShowing()){
                    progressDialog.dismiss();
                }

                mp.start();
            }
        });

       // setOnPreparedListener(new MyOnPreparedListener());
        Track track=new Track();
        track.setIsbackplay(true);

        try {
            //setDataSource(url);
            myMediaPlayer.reset();
            myMediaPlayer.setDataSource(url);
            myMediaPlayer.prepareAsync();

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void playSINGLEFile(final String url, Context context) {
        if (!state.equals(MP_STATE.STOPPED)) {
            myMediaPlayer.stop();
        }
        myMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        final ProgressDialog progressDialog = ProgressDialog.show(context,
                "Loading Song", " Please wait");
        myMediaPlayer.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if (progressDialog != null && progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                mp.start();

            }

        });

        try {
            //setDataSource(url);
            myMediaPlayer.reset();
            myMediaPlayer.setDataSource(url);
           // prepareAsync();
            myMediaPlayer.prepare();

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized MP_STATE getState() {
        return state;
    }


    private class MyOnPreparedListener implements OnPreparedListener {
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            myMediaPlayer.start();
        }
    }



    //CUSTOM METHOD


}
