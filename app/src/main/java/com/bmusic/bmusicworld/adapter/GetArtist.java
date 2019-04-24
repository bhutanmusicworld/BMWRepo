package com.bmusic.bmusicworld.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bmusic.bmusicworld.R;
import com.bmusic.bmusicworld.model.Track;

import java.util.ArrayList;

/**
 * Created by wafa on 8/19/2017.
 */

public class GetArtist  extends ArrayAdapter<Track>
{
    private Context context;
    private ArrayList<Track> TasksResponseList;
    ListView listView;
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

    public GetArtist(Activity activity, int myview, ArrayList<Track> listUsers)
    {
        super(activity,myview,listUsers);
        this.context = activity;
        this.layoutResourceId = myview;
        this.TasksResponseList = listUsers;
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
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        // TODO Auto-generated method stub
        View vi = convertView;
        if (convertView == null) {

            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            vi = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.fullname = (TextView) vi.findViewById(R.id.track_title);

            holder.songimage = (ImageView) vi.findViewById(R.id.track_image);

            vi.setTag(holder);
        } else
            holder = (ViewHolder) vi.getTag();
        final   Track m = TasksResponseList.get(position);
        holder.fullname.setText(m.getArtAtWork());

        return vi;
    }


    public class ViewHolder {
        TextView fullname;
        ImageView download,songimage;

    }


}