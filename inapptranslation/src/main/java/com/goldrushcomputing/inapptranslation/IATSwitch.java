package com.goldrushcomputing.inapptranslation;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.Switch;

/**
 * Created by Takamitsu Mizutori on 2017/04/01.
 */

@RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class IATSwitch extends Switch {

    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public IATSwitch(){
        super(null);
    }

    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public IATSwitch(Context context){
        super(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public IATSwitch(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public IATSwitch(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
    }


    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void requestLayout() {
        try {
            java.lang.reflect.Field mOnLayout = Switch.class.getDeclaredField("mOnLayout");
            mOnLayout.setAccessible(true);
            mOnLayout.set(this, null);
            java.lang.reflect.Field mOffLayout = Switch.class.getDeclaredField("mOffLayout");
            mOffLayout.setAccessible(true);
            mOffLayout.set(this, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.requestLayout();
    }
}
