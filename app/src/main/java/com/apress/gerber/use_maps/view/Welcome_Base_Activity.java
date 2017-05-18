package com.apress.gerber.use_maps.view;
import android.app.Activity;

/**
 * Created by Wx on 2016/12/16.
 */

public class Welcome_Base_Activity extends Activity
{
    @Override
    protected void onPause()
    {
        super.onPause();
       // AseoZdpAseo.initType(this, AseoZdpAseo.INSERT_TYPE);
      //  AseoZdpAseo.initType(this, AseoZdpAseo.SCREEN_TYPE);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }
}
