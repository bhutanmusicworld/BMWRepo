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
import com.bmusic.bmusicworld.model.Track;

import java.util.ArrayList;


public class SectionListDataAdapter extends RecyclerView.Adapter<SectionListDataAdapter.SingleItemRowHolder> {

    private ArrayList<Track> itemsList;
    private Context mContext;
    String i,song;
    customListeners customListners;
    Track singleItem;
    int s;
    customListener customListner;
    public interface customListeners {
        public void onmyButtonListner(int position,ArrayList<Track> itemsList /*int value*/);
    }

    public void setonmyButtonListner(customListeners listener) {
        this.customListners = listener;
    }
    public interface customListener {
        public void onButtonListner(int position, String value);
    }

    public void setCustomButtonListner(customListener listener) {
        this.customListner = listener;
    }

    public SectionListDataAdapter(Context context, ArrayList<Track> itemsList) {
        this.itemsList = itemsList;
        this.mContext = context;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_single_card, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, int position) {
s=position;
        singleItem = itemsList.get(position);
               //  song=singleItem.getStreamUrl();
        holder.tvTitle.setText(singleItem.getTitle());
        Picasso.with(mContext).load(singleItem.getSongimg()).into(holder.itemImage);
        holder.itemImage.setTag(position);
        holder.itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int position = (Integer) v.getTag();

                if (customListners != null) {
                    customListners.onmyButtonListner(position,itemsList);
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

//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                   // int position = (Integer) v.getTag();
//                    if (customListner != null) {
//                        customListner.onButtonListner(s, String.valueOf(singleItem.getId()));
//                    }
//                    String id=singleItem.getId();
//                    String title=singleItem.getTitle();
//                    Intent intent=new Intent(mContext, com.techmitram.bmusic.playlist.MainActivity.class);
//                    intent.putExtra("albumid",id);
//                    intent.putExtra("title",title);
//                    mContext.startActivity(intent);
////
//                  //  Toast.makeText(v.getContext(),song, Toast.LENGTH_SHORT).show();
//
//                }
//            });


        }

    }

}
