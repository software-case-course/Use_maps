package com.apress.gerber.use_maps;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;


public class MyOrientationListener implements SensorEventListener{

    private SensorManager mSensorManager;
    private Context mcontext;
    private Sensor mSensor;
    private float lastX;//监听坐标轴x变化

    public MyOrientationListener(Context context)
    {
        this.mcontext=context;
    }

    public void start()
    {
        mSensorManager=(SensorManager)mcontext.getSystemService(Context.SENSOR_SERVICE);
        if(mSensorManager!=null)
        {//获得方向传感器
            //mSensor=mSensorManager.getDefaultSensor(SensorManager.SENSOR_DELAY_UI);
            mSensor=mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        }
        if(mSensor!=null)//获得方向传感器后，
        {
            mSensorManager.registerListener(this,mSensor,SensorManager.SENSOR_DELAY_UI);
        }
    }
    public void stop()
    {
      mSensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor arg0,int arg1)
    {

    }

    @SuppressWarnings({"deprecation"})
    @Override
    public void onSensorChanged(SensorEvent event)
    {
        if(event.sensor.getType()==Sensor.TYPE_ORIENTATION)
        {
            float x=event.values[SensorManager.DATA_X];

            if(Math.abs(x-lastX)>1.0)
            {
                if(mOnOrientationListener!=null)
                {
                    mOnOrientationListener.onOrientationChanged(x);
                }
            }
            lastX=x;
        }
    }
   private OnOrientationListener mOnOrientationListener;

    public void setOnOrientationListener(OnOrientationListener mOnOrientationListener)
    {
        this.mOnOrientationListener=mOnOrientationListener;
    }

    public interface OnOrientationListener
    {
        void onOrientationChanged(float x);
    }
}
