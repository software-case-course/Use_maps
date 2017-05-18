package com.apress.gerber.use_maps.view.recyclerview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.apress.gerber.use_maps.R;

import java.util.List;


public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private Context context;
    List<CommentSet> Comments;


    public CommentAdapter(Context context, List<CommentSet> Comments) {
        this.context = context;
        this.Comments = Comments;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_comments, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvcomment.setText(Comments.get(position).getTvcomment());
        holder.ivUserImage.setImageDrawable(Comments.get(position).getIvUserImage());
        holder.username.setText(Comments.get(position).getUsername());
    }

    @Override
    public int getItemCount() {
        return Comments.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivUserImage;
        TextView tvcomment;
        TextView username;

        public ViewHolder(View itemView) {
            super(itemView);
            ivUserImage=(ImageView)itemView.findViewById(R.id.ivUser);
            tvcomment=(TextView)itemView.findViewById(R.id.tvComment);
            username=(TextView)itemView.findViewById(R.id.username);
        }
    }
}
