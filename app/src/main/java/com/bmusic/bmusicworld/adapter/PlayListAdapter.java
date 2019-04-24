package com.bmusic.bmusicworld.adapter;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.bmusic.bmusicworld.R;
import com.bmusic.bmusicworld.model.PlayList;
import com.bmusic.bmusicworld.sessionmagement.SessionManagement;

import java.util.ArrayList;

/**
 * Created by wafa on 8/23/2017.
 */

public class PlayListAdapter extends ArrayAdapter<PlayList>
{
    private Context context;
    private ArrayList<PlayList> TasksResponseList;
    ListView listView;
    int pos;
    String name;
    int id=1;
    SessionManagement session;
    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder build;
    private int layoutResourceId;
    customButtonListener customListner;
    custom downloadsong;


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

    public PlayListAdapter(Activity activity, int myview, ArrayList<PlayList> listUsers)
    {
        super(activity,myview,listUsers);
        this.context = activity;
        this.layoutResourceId = myview;
        this.TasksResponseList = listUsers;
        //  inflater = (LayoutInflater) context
        //       .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public void setGridData(ListView mlistData) {
        this.listView = mlistData;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        int count = TasksResponseList.size();
        return count;
    }

    ViewHolder holder;
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        holder = new ViewHolder();
        if (convertView == null) {

            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            vi = inflater.inflate(layoutResourceId, parent, false);
            // holder = new ViewHolder();
            holder.play_list = (RelativeLayout) vi.findViewById(R.id.play_list);
            holder.fullname = (TextView) vi.findViewById(R.id.track_title);
            holder.songcount = (TextView) vi.findViewById(R.id.songcount);
            holder.download = (ImageView) vi.findViewById(R.id.download);
            holder.menu = (ImageButton) vi.findViewById(R.id.menu);
            holder.songimage = (ImageView) vi.findViewById(R.id.track_image);

            vi.setTag(holder);
        } else
            holder = (ViewHolder) vi.getTag();
        final   PlayList m = TasksResponseList.get(position);
         holder.fullname.setText(m.getPlaylistname());
         holder.songcount.setText(m.getSongcount()+" songs");
       Picasso.with(context).load(m.getPlaylistimage()).into(holder.songimage);

        holder.play_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customListner != null) {
                    customListner.onButtonClickListner(position, String.valueOf(TasksResponseList.get(position).getPlaylistid()));
                }
            }
        });

   // }



        return vi;
    }

    public class ViewHolder {
        TextView fullname;
        TextView songcount;
        TextView status;
        ImageView download,songimage;
        ImageButton menu;
        RelativeLayout play_list;
    }


}