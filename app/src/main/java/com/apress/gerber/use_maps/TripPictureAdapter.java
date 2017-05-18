package com.apress.gerber.use_maps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;


public class TripPictureAdapter extends BaseAdapter {

    private Context context;
    public List<View> imageViews;

    public TripPictureAdapter(Context context) {
        this.context = context;
        this.imageViews = TripActivity.imageViews;
    }


    @Override
    public int getCount() {
        return Math.max(1, imageViews.size() + 1);
    }

    @Override
    public View getItem(int position) {
        return imageViews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_trip_gridview, null);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.item_trip_imageView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (position == imageViews.size()) {
            viewHolder.imageView.setImageResource(R.drawable.addpicture);
        } else {
            viewHolder.imageView.setImageDrawable(((ImageView)imageViews.get(position)).getDrawable());
        }
        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
    }
}
