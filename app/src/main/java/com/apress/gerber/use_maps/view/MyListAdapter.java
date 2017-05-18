package com.apress.gerber.use_maps.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.apress.gerber.use_maps.AddOverlayActivity;
import com.apress.gerber.use_maps.R;
import com.apress.gerber.use_maps.view.recyclerview.CommentActivity;

import java.util.List;

/**
 * Created by ShineLu on 2017/1/22.
 */

public class MyListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    List<MyListSet> mylistset;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    public MyListAdapter(Context context, List<MyListSet> mylistset) {
        this.context = context;
        this.mylistset=mylistset;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = null;
        //    ★ 2. 根据viewType来确定加载那个布局，返回哪一个ViewHolder
        switch (viewType) {
            case TYPE_ITEM:
                view = inflater.inflate(R.layout.item_mylist, parent, false);
                return new BodyHolder(view);
            case TYPE_FOOTER:
                view = inflater.inflate(R.layout.item_foot, parent, false);
                return new FootHolder(view);
        }
        return null;

    }



    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof BodyHolder) {
            ((BodyHolder) holder).mylist_title.setText(mylistset.get(position).getMylist_title());
            ((BodyHolder) holder).ivImage.setImageDrawable(mylistset.get(position).getIvImage());

        }

    }
    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }
    @Override
    public int getItemCount() {
        return mylistset.size();
    }


    class BodyHolder extends RecyclerView.ViewHolder {

        ImageView ivImage;
        TextView mylist_title;
        ImageButton forward;

      public BodyHolder(View itemView) {
            super(itemView);
            ivImage=(ImageView)itemView.findViewById(R.id.ivImage);
            mylist_title=(TextView)itemView.findViewById(R.id.mylist_title);

            forward=(ImageButton)itemView.findViewById(R.id.forward);
            forward.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    /*
                    Intent i = new Intent();
                    i.setClass(context, AddOverlayActivity.class);
                    //启动
                    context.startActivity(i);
                    */
                }
            });
            }
        }
    static class FootHolder extends RecyclerView.ViewHolder {

        public FootHolder(View view) {
            super(view);
        }
    }
    }
