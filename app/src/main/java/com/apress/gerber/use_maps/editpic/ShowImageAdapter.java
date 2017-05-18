package com.apress.gerber.use_maps.editpic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.apress.gerber.use_maps.R;
import com.apress.gerber.use_maps.editpic.chosepiture.util.chosepiture;


/**
 * Created by asus on 2017/1/19.
 */

public class ShowImageAdapter extends BaseAdapter {
    private String[] data;
    private Context context;
    private ImageView imageView;
    public ShowImageAdapter(Context context, String[] data)
    {
        this.data=data;
        this.context=context;
    }
    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public Object getItem(int i) {
        return data[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view==null) {
            view = LayoutInflater.from(context).inflate(R.layout.show_item, null);
            imageView=(ImageView)view.findViewById(R.id.show_image);
            chosepiture.getInstance(3, chosepiture.Type.LIFO).loadImage(data[i], imageView);
        }
        return view;
    }
}
