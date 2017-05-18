package com.apress.gerber.use_maps.view.recyclerview;

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

import java.util.List;

/**
 * Created by ShineLu on 2017/3/11.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
    private Context context;
    List<MySet> myset;


    public MyAdapter(Context context, List<MySet> myset) {
        this.context = context;
        this.myset = myset;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_add, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.Mytitle.setText(myset.get(position).getMytitle());
        holder.MyImage.setImageDrawable(myset.get(position).getMyImage());
    }

    @Override
    public int getItemCount() {
        return myset.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView MyImage;
        TextView Mytitle;
        ImageButton myforward;

        public ViewHolder(View itemView) {
            super(itemView);
            MyImage=(ImageView)itemView.findViewById(R.id.Myimage);
            Mytitle=(TextView)itemView.findViewById(R.id.Mytitle);
            //按钮
            myforward=(ImageButton)itemView.findViewById(R.id.goforward);

            //
            myforward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*
                    Intent i = new Intent();
                    i.setClass(context,AddOverlayActivity.class);
                    //启动
                    context.startActivity(i);
                    */
                }
            });
        }
    }
}
