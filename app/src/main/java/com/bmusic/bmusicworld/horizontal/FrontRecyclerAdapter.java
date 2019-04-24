package com.bmusic.bmusicworld.horizontal;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bmusic.bmusicworld.R;
import com.bmusic.bmusicworld.model.Track;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import dm.audiostreamer.MediaMetaData;


public class FrontRecyclerAdapter extends RecyclerView.Adapter<FrontRecyclerAdapter.ItemRowHolder> implements SongListAdapter.customListeners {

    private ArrayList<SectionDataModel> dataList;
    private Context mContext;
    int d;
    customListener customListner;
    butnmorelistener morelistener;
    ArrayList singleSectionItems;
    private List<MediaMetaData> search_musicList = null;
    private ArrayList<MediaMetaData> musicList;
    private ArrayList<MediaMetaData> itemsList;

    @Override
    public void onmyButtonListner(int position, MediaMetaData itemsList, ArrayList<MediaMetaData> itemList) {
        if (customListner != null) {
            musicList=itemList;
            itemsList=itemsList;

            // ArrayList<MediaMetaData> v=itemsList/*dataList.get(position).getAllItemsInSection()*/;
            customListner.onButtonsListner(position, itemsList,itemList);
            //  Toast.makeText(mContext,value,Toast.LENGTH_LONG).show();
        }
    }

    public void notifyPlayState(MediaMetaData metaData) {
        if (this.itemsList != null && metaData != null) {
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
//
//    public void refresh(List<MediaMetaData> musicList) {
//        if (this.musicList != null) {
//            this.musicList.clear();
//        }
//        this.musicList.addAll(musicList);
//        notifyDataSetChanged();
//    }


    public interface customListener {
        public void onButtonsListner(int position, MediaMetaData value, ArrayList<MediaMetaData> itemList);
    }
    public interface butnmorelistener {
        public void onmorelistener(String position, String value);

    }
    public  void setMorelistener(butnmorelistener morelistener)
    {
        this.morelistener=morelistener;

    }
    public void setButtonsListner(customListener listener) {
        this.customListner = listener;
    }


    public FrontRecyclerAdapter(Context context, ArrayList<SectionDataModel> dataList) {
        this.dataList = dataList;
        this.mContext = context;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // d=i;
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cat_items, null);
        ItemRowHolder mh = new ItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(ItemRowHolder itemRowHolder, final int i) {
        d=i;
        final String sectionName = dataList.get(i).getHeaderTitle();
        // final  String sectionid=dataList.get(i).getheaderId();

        singleSectionItems = dataList.get(i).getAllItemsInSection2();
        this.search_musicList = singleSectionItems;
        itemRowHolder.itemTitle.setText(sectionName);

        SongListAdapter itemListDataAdapter = new SongListAdapter(mContext, singleSectionItems);

        itemRowHolder.recycler_view_list.setHasFixedSize(true);
        itemRowHolder.recycler_view_list.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        itemListDataAdapter.setonmyButtonListner(this);
        itemRowHolder.recycler_view_list.setAdapter(itemListDataAdapter);

        itemRowHolder.btnMores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String vs= String.valueOf(dataList.get(i).getHeaderId());
                String title=dataList.get(i).getHeaderTitle();
                if (morelistener != null) {
                    morelistener.onmorelistener(vs, title);
                }
//                    Intent i=new Intent(mContext, CategoryList.class);
//                    i.putExtra("catid",vs);
//                     i.putExtra("title",title);
//                    mContext.startActivity(i);
            }

        });
       /* Glide.with(mContext)
                .load(feedItem.getImageURL())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .error(R.drawable.bg)
                .into(feedListRowHolder.thumbView);*/
    }
    //    @Override
//    public void onButtonListner(int position, String value) {
//        if (customListner != null) {
//            customListner.onButtonsListner(position, value);
//        }
    // }
    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView itemTitle;

        protected RecyclerView recycler_view_list;

        // protected Button btnMore;
        protected TextView btnMores;

        public ItemRowHolder(View view) {
            super(view);

            this.itemTitle = (TextView) view.findViewById(R.id.itemTitle);
            this.recycler_view_list = (RecyclerView) view.findViewById(R.id.recycler_view_list);
            //  this.btnMore= (Button) view.findViewById(R.id.btnMore);
            this.btnMores= (TextView) view.findViewById(R.id.btnMores);

        }

    }


    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        search_musicList.clear();
        if (charText.length() == 0) {
            search_musicList.addAll(musicList);
        }
        else
        {
            for (MediaMetaData wp : musicList)
            {
                if (wp.getMediaTitle().toLowerCase(Locale.getDefault()).contains(charText)|| wp.getMediaArtist().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    search_musicList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
