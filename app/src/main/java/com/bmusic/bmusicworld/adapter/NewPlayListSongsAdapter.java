package com.bmusic.bmusicworld.adapter;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.bmusic.bmusicworld.payment.PaymentScreenPremium;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.squareup.picasso.Picasso;
import com.bmusic.bmusicworld.R;
import com.bmusic.bmusicworld.downloads.CheckForSDCard;
import com.bmusic.bmusicworld.model.Track;

import com.bmusic.bmusicworld.sessionmagement.SessionManagement;

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
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import dm.audiostreamer.MediaMetaData;
import es.dmoral.toasty.Toasty;

/**
 * Created by wafa on 8/23/2017.
 */
public class NewPlayListSongsAdapter extends BaseAdapter
{

    private List<MediaMetaData> musicList;
    private Context mContext;
    private LayoutInflater inflate;

    private DisplayImageOptions options;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    private ColorStateList colorPlay;
    private ColorStateList colorPause;




    private Context context;
    private ArrayList<Track> TasksResponseList;
    ListView listView;
    int id = 1;
    private int layoutResourceId;
    public RequestQueue requestQueue;
    customButtonListener customListner;
    custom downloadsong;
    int pos;
    String name;

    SessionManagement session;
    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder build;


    public NewPlayListSongsAdapter(Context context, List<MediaMetaData> authors) {
        this.musicList = authors;
        this.mContext = context;
        this.inflate = LayoutInflater.from(context);
        this.colorPlay = ColorStateList.valueOf(context.getResources().getColor(R.color.md_black_1000));
        this.colorPause = ColorStateList.valueOf(context.getResources().getColor(R.color.md_blue_grey_500_75));
        this.options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.bg_default_album_art)
                .showImageForEmptyUri(R.drawable.bg_default_album_art)
                .showImageOnFail(R.drawable.bg_default_album_art).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
    }

    public void refresh(List<MediaMetaData> musicList) {
        if (this.musicList != null) {
            this.musicList.clear();
        }
        this.musicList.addAll(musicList);
        notifyDataSetChanged();
    }

    public void notifyPlayState(MediaMetaData metaData) {
        if (this.musicList != null && metaData != null) {
            int index = this.musicList.indexOf(metaData);
            //TODO SOMETIME INDEX RETURN -1 THOUGH THE OBJECT PRESENT IN THIS LIST
            if (index == -1) {
                for (int i = 0; i < this.musicList.size(); i++) {
                    if (this.musicList.get(i).getMediaId().equalsIgnoreCase(metaData.getMediaId())) {
                        index = i;
                        break;
                    }
                }
            }
            if (index > 0 && index < this.musicList.size()) {
                this.musicList.set(index, metaData);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
//        int count = TasksResponseList.size();
//        return count;
        return musicList.size();
    }

    @Override
    public Object getItem(int i) {
        return musicList.get(i);
    }

    @Override
    public long getItemId(int i) {
       // return position;
        return 0;
    }







//    public NewPlayListSongsAdapter(@NonNull Context context, @LayoutRes int resource) {
//        super(context, resource);
//    }
//
    public interface customButtonListener {
        public void onButtonClickListner(int position, String value);
    }

    public void setCustomButtonListner(customButtonListener listener) {
        this.customListner = listener;
    }
    public interface custom{
        public void onCancle(int position, String value);
    }
    public void setCancleButtonListner(custom listener) {
        this.downloadsong = listener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        ViewHolder mViewHolder;
        if (convertView == null) {
            mViewHolder = new ViewHolder();
            convertView = inflate.inflate(R.layout.all_playlistitems, null);
           // vi = inflater.inflate(R.layout.all_playlistitems, parent, false);

            mViewHolder.songimage = (ImageView) convertView.findViewById(R.id.img_mediaArt);
            mViewHolder.playState = (ImageView) convertView.findViewById(R.id.img_playState);
            mViewHolder.fullname = (TextView) convertView.findViewById(R.id.text_mediaTitle);
            mViewHolder.MediaDesc = (TextView) convertView.findViewById(R.id.text_mediaDesc);
            mViewHolder.menu = (ImageButton) convertView.findViewById(R.id.menu);

            convertView.setTag(mViewHolder);
        }
        else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
      //  final   Track m = TasksResponseList.get(position);
        MediaMetaData media = musicList.get(position);


        mViewHolder.fullname.setText(media.getMediaTitle());
        mViewHolder.MediaDesc.setText(media.getMediaArtist());
        mViewHolder.playState.setImageDrawable(getDrawableByState(mContext, media.getPlayState()));
        String mediaArt = media.getMediaArt();
        imageLoader.displayImage(mediaArt, mViewHolder.songimage, options, animateFirstListener);


        mViewHolder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (downloadsong != null) {
                    downloadsong.onCancle(position, String.valueOf(musicList.get(position).getMediaId()));
                }
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listItemListener != null) {
                    listItemListener.onItemClickListener(musicList.get(position));
                }
            }
        });
        return convertView;
    }

    public interface viewinterface{

    }

    public class ViewHolder {
        TextView fullname;
        TextView MediaDesc;
        TextView duration;
        ImageView playState;
        ImageView download,songimage;
        ImageButton menu;
        // ImageView image;
    }
    private Drawable getDrawableByState(Context context, int state) {
        switch (state) {
            case PlaybackStateCompat.STATE_NONE:
                Drawable pauseDrawable = ContextCompat.getDrawable(context, R.drawable.bigpauses);
                DrawableCompat.setTintList(pauseDrawable, colorPlay);
                return pauseDrawable;
            case PlaybackStateCompat.STATE_PLAYING:
                AnimationDrawable animation = (AnimationDrawable) ContextCompat.getDrawable(context, R.drawable.equalizer);
                DrawableCompat.setTintList(animation, colorPlay);
                animation.start();
                return animation;
            case PlaybackStateCompat.STATE_PAUSED:
                Drawable playDrawable = ContextCompat.getDrawable(context, R.drawable.equalizer);
                DrawableCompat.setTintList(playDrawable, colorPause);
                return playDrawable;
            default:
                Drawable noneDrawable = ContextCompat.getDrawable(context, R.drawable.bigpauses);
                DrawableCompat.setTintList(noneDrawable, colorPlay);
                return noneDrawable;
        }
    }




    //new adapter method

    private class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

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
                    FadeInBitmapDisplayer.animate(imageView, 200);
                    displayedImages.add(imageUri);
                }
            }
            progressEvent(view, true);
        }

    }

    private static void progressEvent(View v, boolean isShowing) {
        try {
            RelativeLayout rl = (RelativeLayout) ((ImageView) v).getParent();
            ProgressBar pg = (ProgressBar) rl.findViewById(R.id.pg);
            pg.setVisibility(isShowing ? View.GONE : View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setListItemListener(ListItemListener listItemListener) {
        this.listItemListener = listItemListener;
    }

    public ListItemListener listItemListener;

    public interface ListItemListener {
        void onItemClickListener(MediaMetaData media);
    }

}