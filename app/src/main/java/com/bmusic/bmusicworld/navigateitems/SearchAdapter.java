package com.bmusic.bmusicworld.navigateitems;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.bmusic.bmusicworld.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import dm.audiostreamer.MediaMetaData;


public class SearchAdapter extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<MediaMetaData> worldpopulationlist = null;
    private ArrayList<MediaMetaData> arraylist;
   OnRecyclerViewItemClickListener listener;

    public SearchAdapter(Context context, List<MediaMetaData> worldpopulationlist) {
        mContext = context;
        this.worldpopulationlist = worldpopulationlist;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<MediaMetaData>();
        this.arraylist.addAll(worldpopulationlist);
    }
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener)
    {
        this.listener = listener;
    }
    public class ViewHolder {
        TextView rank;
        TextView country;
        TextView population;
        CardView card_view;
    }

    @Override
    public int getCount() {
        return worldpopulationlist.size();
    }

    @Override
    public MediaMetaData getItem(int position) {
        return worldpopulationlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.search_item, null);
            // Locate the TextViews in listview_item.xml
            holder.rank = (TextView) view.findViewById(R.id.title);
            holder.country = (TextView) view.findViewById(R.id.subtitle);
            holder.card_view=(CardView)view.findViewById(R.id.card_view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.rank.setText(worldpopulationlist.get(position).getMediaTitle());
        holder.country.setText(worldpopulationlist.get(position).getMediaArtist());

        holder.card_view.setTag( position);
        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer pos = (Integer) holder.card_view.getTag();
                listener.onRecyclerViewItemClicked(pos, -1);
            }
        });
        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        worldpopulationlist.clear();
        if (charText.length() == 0) {
            worldpopulationlist.addAll(arraylist);
        }
        else
        {
            for (MediaMetaData wp : arraylist)
            {
                if (wp.getMediaTitle().toLowerCase(Locale.getDefault()).contains(charText)|| wp.getMediaArtist().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    worldpopulationlist.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
