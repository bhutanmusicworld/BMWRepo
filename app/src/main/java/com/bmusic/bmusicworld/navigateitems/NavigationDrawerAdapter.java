package com.bmusic.bmusicworld.navigateitems;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bmusic.bmusicworld.R;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;




public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.MyViewHolder>{
        List<NavDrawerItem> data= Collections.emptyList();
private LayoutInflater inflater;
private Context context;



public NavigationDrawerAdapter(Context context, List<NavDrawerItem> data){
        this.context=context;
        inflater=LayoutInflater.from(context);
        this.data=data;
        }

public void delete(int position){
        data.remove(position);
        notifyItemRemoved(position);
        }


@Override
public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view=inflater.inflate(R.layout.nav_drawer_row,parent,false);
        MyViewHolder holder=new MyViewHolder(view);
        return holder;
        }

@Override
public void onBindViewHolder(MyViewHolder holder,int position){
        NavDrawerItem current=data.get(position);
        holder.title.setText(current.getTitle());
          holder.view.setImageResource(current.getIcon());
    //Picasso.with(context).load(current.getIcon()).into(holder.view);
        }

@Override
public int getItemCount(){
        return data.size();
        }

class MyViewHolder extends RecyclerView.ViewHolder {
    TextView title;
    ImageView view;


    public MyViewHolder(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.title);
        view = (ImageView) itemView.findViewById(R.id.icon);

    }
}
}
