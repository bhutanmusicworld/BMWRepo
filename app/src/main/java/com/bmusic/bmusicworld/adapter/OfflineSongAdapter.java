package com.bmusic.bmusicworld.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.bmusic.bmusicworld.R;
import com.bmusic.bmusicworld.model.Track;

import java.util.ArrayList;

/**
 * Created by TECHMIT on 2/9/2018.
 */
public class OfflineSongAdapter extends RecyclerView.Adapter<OfflineSongAdapter.MyViewHolder> {
    private final Context context;
    private ArrayList<Track> items;
    OnItemClickListener listener;


    public interface OnItemClickListener {
        public void onItemClickListener(int position, ArrayList<Track> items);
    }
    public OfflineSongAdapter(ArrayList<Track> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.offline_list_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Track item = items.get(position);
        holder.title.setText(item.getTitle());
       // holder.imageView.setImageBitmap(item.getBitmap());
        holder.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClickListener(position,items);
            }
        });
        //TODO Fill in your logic for binding the view.
    }

    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        return items.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title,time;
        ImageView imageView;
        RelativeLayout play;
        public MyViewHolder(View itemView) {
            super(itemView);
            title=(TextView)itemView.findViewById(R.id.track_title);
            imageView=(ImageView)itemView.findViewById(R.id.track_image);
            play=(RelativeLayout)itemView.findViewById(R.id.play);


        }
    }
    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.listener = listener;
    }
}