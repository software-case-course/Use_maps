package com.apress.gerber.use_maps.editpic.chosepiture.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.LruCache;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Created by asus on 2016/4/11.
 * 加载图片
 */
public class chosepiture {
    private static  chosepiture mInstance;

    /**
     * 图片缓存的核心对象
     */
    private LruCache<String,Bitmap> mLruCache;

    /**
     * 线程池
     */
    private ExecutorService mThreadPool;
    private  static final int DEAFULT_THREAD_COUNT=1;

    /**
     * 队列的调度方式
     */
    private Type mType=Type.FIF0;//改了啦啦啦啦啦拉了拉

    /**
     * 任务队列
     */
    private LinkedList<Runnable>mTaskQueue;

    /**
     * 后台轮询线程
     */
    private Thread mPoolThread;
    private Handler mPoolThreadHandler;

    /**
     * ul的Handler访问
     */
    private Handler mUIHandler;
    private  Semaphore mSemaphoremPoolThreadHandler=new Semaphore(0);//为了防止 mPoolThreadHandler未初始化就在addtask中被调用，这个是并发线程
    private Semaphore mSemaphoremThreadPool;
    public enum Type//调度方式的选择
    {
        FIF0,LIFO;
    }
    private  chosepiture(int threadCount, Type type)
    {
        init(threadCount,type);


    }

    /**
     * 初始化
     * @return
     */
    private void init(int threadCount,Type type)
    {
        //后台轮询线程
        mPoolThread=new Thread()
        {
            @Override
            public void run()
            {
                Looper.prepare();
                mPoolThreadHandler=new Handler() {
                    @Override
                    public void handleMessage(Message msg)
                    {
                        //线程池去取出一个任务进行执行
                        mThreadPool.execute(getTask());
                        try {
                            mSemaphoremThreadPool.acquire();
                        } catch (InterruptedException e) {

                        }
                    }

                };
                //释放一个信号量
                mSemaphoremPoolThreadHandler.release();
                Looper.loop();
            };
        };
        mPoolThread.start();
        //获取我们应用的最大可用内存
        int maxMemory=(int)Runtime.getRuntime().maxMemory();
        int cacheMemory=maxMemory/8;
        mLruCache=new LruCache<String,Bitmap>(cacheMemory){
            @Override
            protected int sizeOf(String key,Bitmap value)
            {
                return value.getRowBytes()*value.getHeight();
            }
        };
        //创建线程池
        mThreadPool= Executors.newFixedThreadPool(threadCount);
        mTaskQueue=new LinkedList<Runnable>();
        mType=type;
        mSemaphoremThreadPool=new Semaphore(threadCount);
    }

    /**
     * 从任务队列取出一个方法
     * @return
     */
    private Runnable getTask()
    {
        if(mType==Type.FIF0)
        {
            return mTaskQueue.removeFirst();
        }
        else if(mType==Type.LIFO)
        {
            return mTaskQueue.removeLast();
        }
        return null;
    }
    public static chosepiture getInstance(int threadCount,Type type)//getmInstance????加载一开始的进程
    {
        if(mInstance==null)
        {
            synchronized (chosepiture.class)
            {
                if(mInstance==null)
                {
                    mInstance=new chosepiture(threadCount,type);
                }
            }
        }
        return mInstance;

    }
    public void loadBitmap( final String path,final ImageView imageview) {
        imageview.setTag(path);
        if (mUIHandler == null) {
            mUIHandler = new Handler() {
                public void handleMessage(Message msg) {
                    //获取得到图片，为imageview回调设置图片
                    ImgBeanHolder holder = (ImgBeanHolder) msg.obj;
                    Bitmap bm = holder.bitmap;
                    ImageView imageview = holder.imageView;
                    String path = holder.path;
                    //将path与getTag存储路径进行比较
                    if (imageview.getTag().toString().equals(path)) {

                        imageview.setImageBitmap(bm);
                    }

                }

                ;
            };
        }
        //根据path在缓存中获取bitmap
        Bitmap bm = getBitmapFromLruCache(path);
        if (bm != null) {
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
            if (resizedBitmap != bm && bm != null && !bm.isRecycled()) {

                refreshBitmap(path, imageview, resizedBitmap);


            } else {
                addTasks(new Runnable() {
                    @Override
                    public void run() {
                        //加载图片
                        //图片的压缩
                        //1.获得图片需要显示的大小
                        ImageSize imageSize = getImageViewSize(imageview);
                        //2.压缩图片
                        Bitmap bm = decodeSampleBitmapFromPath(path, imageSize.width, imageSize.height);
                        //3.把图片加入到缓存里
                        addBitmapToLruCache(path, bm);
                        refreshBitmap(path, imageview, bm);
                        mSemaphoremThreadPool.release();
                    }
                });
            }
        }
    }
    /**
     * 通过path获得图片
     * @param path
     * @param imageview
     */
    public void loadImage( final String path,final ImageView imageview)
    {
        imageview.setTag(path);
        if(mUIHandler==null)
        {
            mUIHandler=new Handler() {
                public void handleMessage(Message msg)
                {
                    //获取得到图片，为imageview回调设置图片
                    ImgBeanHolder holder=(ImgBeanHolder)msg.obj;
                    Bitmap bm=holder.bitmap;
                    ImageView imageview=holder.imageView;
                    String path=holder.path;
                    //将path与getTag存储路径进行比较
                    if(imageview.getTag().toString().equals(path))
                    {
                        imageview.setImageBitmap(bm);
                    }

                };
            };
        }
        //根据path在缓存中获取bitmap
        Bitmap bm=getBitmapFromLruCache(path);
        if(bm!=null)
        {
            refreshBitmap(path, imageview, bm);
        }
        else
        {
            addTasks(new Runnable() {
                @Override
                public void run() {
                    //加载图片
                    //图片的压缩
                    //1.获得图片需要显示的大小
                    ImageSize imageSize=getImageViewSize(imageview);
                    //2.压缩图片
                    Bitmap bm=decodeSampleBitmapFromPath(path,imageSize.width,imageSize.height);
                    //3.把图片加入到缓存里
                    addBitmapToLruCache(path,bm);
                    refreshBitmap(path,imageview,bm);
                    mSemaphoremThreadPool.release();
                }
            });
        }
    }
    private void refreshBitmap(final String path,final ImageView imageview,Bitmap bm)
    {
        addBitmapToLruCache(path, bm);
        Message message=Message.obtain();
        ImgBeanHolder holder=new ImgBeanHolder();
        holder.bitmap=bm;
        holder.path=path;
        holder.imageView=imageview;
        message.obj=holder;
        mUIHandler.sendMessage(message);
    }
    /**
     * 将图片加入到LruCache
     * @param path
     * @param bm
     */
    protected void addBitmapToLruCache(String path,Bitmap bm)
    {
        if(getBitmapFromLruCache(path)==null)
        {
            if(bm!=null)
                mLruCache.put(path,bm);
        }
    }
    /**
     * g根据图片需要显示的宽和高对图片进行压缩
     * @param
     * @return
     */
    protected Bitmap decodeSampleBitmapFromPath(String path,int width,int height)
    {
        //获得图片的宽和高，并不把图片加载到内存中
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;//不加载图片，只显示宽和高
        BitmapFactory.decodeFile(path,options);
        options.inSampleSize=caculateInSampleSize(options,width,height);
        //使用获得到的inSampleSize再次解析图片
        options.inJustDecodeBounds=false;
        Bitmap bitmap=BitmapFactory.decodeFile(path,options);
        return  bitmap;
    }

    /**
     * g根据需求的宽和高以及图片实际的宽和高计算SampleSize
     * @param
     * @return
     */
    private int caculateInSampleSize(BitmapFactory.Options options,int reqWidth,int reqHeight)
    {
        int width=options.outWidth;
        int height=options.outHeight;
        int inSampleSize=1;
        if(width>reqWidth||height>reqHeight)
        {
            int widthRadio=Math.round(width*1.0f/reqWidth);
            int heightRadio=Math.round(height*1.0f/reqHeight);
            inSampleSize=Math.max(widthRadio,heightRadio);//这个也可以改成min。max是为了压缩更大，min是为了不失真
        }
        return inSampleSize;
    }

    /**
     * 根据imageView获得适当的压缩宽和高
     * @param imageView
     * @return
     */
    protected ImageSize getImageViewSize(ImageView imageView)
    {
        ImageSize imageSize=new ImageSize();
        DisplayMetrics displayMetrics=imageView.getContext().getResources().getDisplayMetrics();
        ViewGroup.LayoutParams lp=imageView.getLayoutParams();
        int width=imageView.getWidth();//获得imageview的实际宽度
        int height=imageView.getHeight();//获得imageview的实际高度
        if(width<=0)
        {
            width=lp.width;//获得imageview在layout中声明的宽度
        }
        if(width<=0)
        {
            width=getImageViewFieldValue(imageView,"mMAXwidth");//检查最大值
        }
        if(width<=0)
        {
            width=displayMetrics.widthPixels;//屏幕的宽度
        }
        if(height<=0)
        {
            height=lp.height;//获得imageview在layout中声明的高度
        }
        if(height<=0)
        {
            height=getImageViewFieldValue(imageView, "mMAXheight");//检查最大值
        }
        if(height<=0)
        {
            height=displayMetrics.heightPixels;//屏幕的高度
        }
        imageSize.width=width;
        imageSize.height=height;
        return imageSize;
    }

    /**
     * 通过反射获取imageview的某个属性值
     * @param object
     * @param fieldName
     * @return
     */
    private static int getImageViewFieldValue(Object object,String fieldName)
    {
        int value=0;
        try {
            Field field=ImageView.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            int fieldvalue=field.getInt(object);
            if(fieldvalue>0 && fieldvalue<Integer.MAX_VALUE)
            {
                value=fieldvalue;
            }
        } catch (Exception e) {

        }
        return value;
    }
    private synchronized void addTasks(Runnable runnable)
    {
        mTaskQueue.add(runnable);
        // if(mPoolThreadHandler==null) wait();
        try
        {
            if(mPoolThreadHandler==null)
                mSemaphoremPoolThreadHandler.acquire();
        }catch(InterruptedException e)
        {

        }
        mPoolThreadHandler.sendEmptyMessage(0x110);

    }
    /**
     * 根据path在缓存中获取bitmap
     * @param key
     * @return
     */
    private Bitmap getBitmapFromLruCache(String key)
    {
        return mLruCache.get(key);
    }
    private class ImageSize
    {
        int width;
        int height;
    }
    private class ImgBeanHolder
    {
        Bitmap bitmap;
        ImageView imageView;
        String path;
    }
}

