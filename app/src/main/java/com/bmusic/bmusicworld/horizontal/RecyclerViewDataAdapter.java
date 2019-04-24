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



public class RecyclerViewDataAdapter extends RecyclerView.Adapter<RecyclerViewDataAdapter.ItemRowHolder> implements SectionListDataAdapter.customListeners {

    private ArrayList<SectionDataModel> dataList;
    private Context mContext;
    customButtonListener customListner;

    @Override
    public void onmyButtonListner(int position, ArrayList<Track> itemsList) {
        if (customListner != null) {
            ArrayList<Track> v=itemsList/*dataList.get(position).getAllItemsInSection()*/;
            customListner.onButtonClickListner(position, v);
            //  Toast.makeText(mContext,value,Toast.LENGTH_LONG).show();
        }
    }

    public interface customButtonListener {
        public void onButtonClickListner(int position, ArrayList<Track> value);
    }

    public void setCustomButtonListner(customButtonListener listener) {
        this.customListner = listener;
    }
    public RecyclerViewDataAdapter(Context context, ArrayList<SectionDataModel> dataList) {
        this.dataList = dataList;
        this.mContext = context;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, null);
        ItemRowHolder mh = new ItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(ItemRowHolder itemRowHolder, final int i) {

        final String sectionName = dataList.get(i).getHeaderTitle();

        ArrayList singleSectionItems = dataList.get(i).getAllItemsInSection();

        itemRowHolder.itemTitle.setText(sectionName);

        SectionListDataAdapter itemListDataAdapter = new SectionListDataAdapter(mContext, singleSectionItems);

        itemRowHolder.recycler_view_list.setHasFixedSize(true);
        itemRowHolder.recycler_view_list.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        itemRowHolder.recycler_view_list.setAdapter(itemListDataAdapter);
        itemListDataAdapter.setonmyButtonListner(this);

//        itemRowHolder.btnMore.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                Toast.makeText(v.getContext(), "click event on more, "+sectionName , Toast.LENGTH_SHORT).show();
//
//
//
//            }
//        });


//        itemRowHolder.btnMores.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (customListner != null) {
//                   String vs= String.valueOf(dataList.get(i).getHeaderTitle());
//                    customListner.onButtonClickListner(i, vs);
//                    //Toast.makeText(v.getContext(), "click event on more, "+sectionName , Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });
       /* Glide.with(mContext)
                .load(feedItem.getImageURL())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .error(R.drawable.bg)
                .into(feedListRowHolder.thumbView);*/
    }

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

}