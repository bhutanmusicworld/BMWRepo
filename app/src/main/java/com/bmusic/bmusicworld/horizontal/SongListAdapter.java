package com.bmusic.bmusicworld.horizontal;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.bmusic.bmusicworld.R;


import java.util.ArrayList;

import dm.audiostreamer.MediaMetaData;

public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.SingleItemRowHolder> {

    private ArrayList<MediaMetaData> itemsList;
    private Context mContext;
    String i,song,title;
    int s,t;
    MediaMetaData singleItem;
    customListeners customListners;
    public interface customListeners {
        public void onmyButtonListner(int position,MediaMetaData itemsList /*int value*/,ArrayList<MediaMetaData> itemList);
    }

    public void setonmyButtonListner(customListeners listener) {
        this.customListners = listener;
    }


    public SongListAdapter(Context context, ArrayList<MediaMetaData> itemsList) {
        this.itemsList = itemsList;
        this.mContext = context;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        s=i;
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_single_card, null);
        v.setTag(i);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, int position) {
        s=position;
        singleItem = itemsList.get(position);
        song=singleItem.getMediaUrl();
        title=singleItem.getMediaTitle();
        holder.tvTitle.setText(singleItem.getMediaTitle());
        Picasso.with(mContext).load(singleItem.getMediaArt()).into(holder.itemImage);
        holder.itemImage.setTag(position);
        holder.itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int position = (Integer) v.getTag();

                if (customListners != null) {
                    customListners.onmyButtonListner(position,itemsList.get(position),itemsList);
                }
            }
        });

      /* Glide.with(mContext)
                .load(feedItem.getImageURL())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .error(R.drawable.bg)
                .into(feedListRowHolder.thumbView);*/
    }



    @Override
    public int getItemCount() {
       // t= itemsList.size();
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView tvTitle;

        protected ImageView itemImage;


        public SingleItemRowHolder(View view) {
            super(view);

            this.tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            this.itemImage = (ImageView) view.findViewById(R.id.itemImage);
            i= String.valueOf(view.getId());
           /* view.setOnClickListener(new View.OnClickListener() {

                String f,m;
                @Override
                public void onClick(View v) {
                   // for (int i=0;i<5;i++){
                    int position = (Integer) v.getTag();

                    if (customListners != null) {
//                        itemsList.get(i).getStreamUrl();
//                        f = itemsList.get(i).getStreamUrl();
//                        m = itemsList.get(i).getTitle();
                     //   customListners.onmyButtonListner(t,s);
                        customListners.onmyButtonListner(position,s);
                    }
                   // }

                   //   Toast.makeText(v.getContext(),m, Toast.LENGTH_SHORT).show();

                }
            });

*/
        }

    }

}
