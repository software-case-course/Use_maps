package com.apress.gerber.use_maps.view.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.apress.gerber.use_maps.AddOverlayActivity;

import com.apress.gerber.use_maps.Public_User;
import com.apress.gerber.use_maps.R;
import com.apress.gerber.use_maps.Routes;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;


public class InformationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    public String thisrid;
    private ImageView headimage;
    List<InformationSet> informationSet;
    //刷新加载
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
  private CardView cardView;

    public InformationAdapter(Context context, List<InformationSet> informationSet) {
        this.context = context;
        this.informationSet = informationSet;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = null;

        //    ★ 2. 根据viewType来确定加载那个布局，返回哪一个ViewHolder
        switch (viewType) {
            case TYPE_ITEM:
                ////////////////////////////////////////////////////////////////
                view = inflater.inflate(R.layout.item_information, parent, false);
                return new BodyHolder(view);
            case TYPE_FOOTER:
                view = inflater.inflate(R.layout.item_foot1, parent, false);
                return new FootHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof BodyHolder) {
            ((BodyHolder) holder).tvLoaction.setText(informationSet.get(position).getTvLoaction());
            //((BodyHolder) holder).ivImageView.setImageDrawable(informationSet.get(position).getIvImageView());
            //((BodyHolder) holder).author_head.setImageDrawable(informationSet.get(position).getAuthor_head());//////////////
            new Thread(new Runnable() {
                @Override
                public void run() {
                    headimage=null;
                    headimage= ((BodyHolder) holder).author_head;
                    new AnotherTask().execute(informationSet.get(position).getAuthor_head_url());
                }
            }).start();
            ((BodyHolder)holder).author_id.setText(""+informationSet.get(position).getAuthor_id());
            ((BodyHolder) holder).btnLike.setImageResource(informationSet.get(position).isBtnLike() ? R.drawable.like_on : R.drawable.like_off);
            ((BodyHolder) holder).tvLikeCount.setText(""+informationSet.get(position).getTvLikeCount());
            ((BodyHolder) holder).tvCommentCount.setText(""+informationSet.get(position).getTvCommentCount());
            ((BodyHolder) holder).tvDate.setText(""+informationSet.get(position).getTvDate());
            //((BodyHolder)holder).commentbutton.setImageDrawable(informationSet.get(position).getCommentbutton());

            setListener(((BodyHolder) holder), position);
        }
    }

    /*异步加载图片*/
    private  class AnotherTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected void onPostExecute(Bitmap result) {
            //对UI组件的更新操作
           // BodyHolder.author_head.setImageBitmap(result);
            if(result!=null)
                headimage.setImageBitmap(result);
            else
                headimage.setImageResource(R.drawable.avater_default);
            //显示
        }
        @Override
        protected Bitmap doInBackground(String... params) {
            //耗时的操作
            Bitmap  bitmap;
            if(params[0]!=null)
                 bitmap = getheadpic(params[0]);
            else
                 bitmap=null;
            return bitmap;
        }

    }
    /**
     * 根据URL获取Bitmap
     * */
    public Bitmap getheadpic(final String url){
        Bitmap bitmap = null;

        URL myFileURL;
        try {
            myFileURL = new URL(url);

            //获得连接
            HttpURLConnection conn = (HttpURLConnection) myFileURL.openConnection();
            //设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
            conn.setConnectTimeout(6000);
            //连接设置获得数据流
            conn.setDoInput(true);
            //不使用缓存
            conn.setUseCaches(false);
            //这句可有可无，没有影响
            //conn.connect();
            //得到数据流
            InputStream is = conn.getInputStream();
            //解析得到图片
            bitmap = BitmapFactory.decodeStream(is);
            bitmap=toRoundBitmap(bitmap);
            //关闭数据流
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }



        return bitmap;
    }
    /**
     * 把bitmap转成圆形
     * */
    public Bitmap toRoundBitmap(Bitmap bitmap){
        int width=bitmap.getWidth();
        int height=bitmap.getHeight();
        int r=0;
        //取最短边做边长
        if(width<height){
            r=width;
        }else{
            r=height;
        }
        //构建一个bitmap
        Bitmap backgroundBm=Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        //new一个Canvas，在backgroundBmp上画图
        Canvas canvas=new Canvas(backgroundBm);
        Paint p=new Paint();
        //设置边缘光滑，去掉锯齿
        p.setAntiAlias(true);
        RectF rect=new RectF(0, 0, r, r);
        //通过制定的rect画一个圆角矩形，当圆角X轴方向的半径等于Y轴方向的半径时，
        //且都等于r/2时，画出来的圆角矩形就是圆形
        canvas.drawRoundRect(rect, r/2, r/2, p);
        //设置当两个图形相交时的模式，SRC_IN为取SRC图形相交的部分，多余的将被去掉
        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //canvas将bitmap画在backgroundBmp上
        canvas.drawBitmap(bitmap, null, rect, p);
        return backgroundBm;
    }


    private void setListener(final BodyHolder holder, final int position) {
        holder.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (informationSet.get(position).isBtnLike() == false) {
                    holder.btnLike.setImageResource(R.drawable.like_on);
                    //////////////////////点赞数加
                    BmobRelation relation = new BmobRelation();
                    relation.add(BmobUser.getCurrentUser(Public_User.class));
                    Routes route = new Routes();
                    route.increment("Likenumber");
                    route.setLiker(relation);
                    route.update(informationSet.get(position).getrouteId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                Integer oldlike =informationSet.get(position).getTvLikeCount();
                                informationSet.get(position).setTvLikeCount(oldlike+1);
                                ((BodyHolder) holder).tvLikeCount.setText(""+informationSet.get(position).getTvLikeCount());
                            }
                        }
                    });
                    ////////////////////////////
                } else {
                    holder.btnLike.setImageResource(R.drawable.like_off);
                }
            }
        });

        holder.commentbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Intent i = new Intent();
                i.putExtra("thisroutid" , informationSet.get(position).getrouteId());
                i.putExtra("thisrouteUsername",informationSet.get(position).getrouteUsername());
                i.setClass(context,CommentActivity.class);
                //启动
                context.startActivity(i);
            }
        });

        holder.ivImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent();
                i.setClass(context, CommentActivity.class);
                //启动
                context.startActivity(i);

            }
        });
    }



    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }


    @Override
    public int getItemCount() {
        return informationSet.size();
    }


    class BodyHolder extends RecyclerView.ViewHolder {
        TextView tvLoaction;
        ImageButton ivImageView;
        ImageView author_head;
        TextView author_id;
        ImageButton btnLike;
        TextView tvLikeCount;
        TextView tvCommentCount;
        TextView tvDate;
        ImageView commentbutton;

        public BodyHolder(View itemView) {
            super(itemView);
            tvLoaction = (TextView) itemView.findViewById(R.id.tvLoaction);
            ivImageView = (ImageButton) itemView.findViewById(R.id.ivImageView);
            author_head = (ImageView) itemView.findViewById(R.id.author_head);
            author_id = (TextView) itemView.findViewById(R.id.author_id);
            btnLike = (ImageButton) itemView.findViewById(R.id.btnLike);
            tvLikeCount = (TextView) itemView.findViewById(R.id.tvLikeCount);
            tvCommentCount = (TextView) itemView.findViewById(R.id.tvCommentCount);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            commentbutton=(ImageButton)itemView.findViewById(R.id.commentbutton);

        }
    }
    static class FootHolder extends RecyclerView.ViewHolder {

        public FootHolder(View view) {
            super(view);
        }
    }



}
