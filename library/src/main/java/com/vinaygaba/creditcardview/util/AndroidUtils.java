package com.vinaygaba.creditcardview.util;

import android.os.Build;

/**
 * Created by Gregory on 6/30/2015.
 */
public class AndroidUtils {

    //SDK 14 Version 4.0
    public final static boolean icsOrBetter(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    private AndroidUtils(){}
}
