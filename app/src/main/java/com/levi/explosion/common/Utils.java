package com.levi.explosion.common;

import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by wei.liu
 * on 2018/12/24.
 */
public class Utils {

    public static final boolean AT_LEAST_M = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    public static final boolean AT_LEAST_LOLLIPOP = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    public static final boolean AT_LEAST_KITKAT = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

    public static final int DEFAULT_DEVICE_SCREEN_WIDTH = 1080;
    public static final int DEFAULT_DEVICE_SCREEN_HEIGHT = 1920;

    public static int getPhoneWidth(Context context) {
        if (null == context) {
            return DEFAULT_DEVICE_SCREEN_WIDTH;
        }
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static int getPhoneHeight(Context context) {
        if (null == context) {
            return DEFAULT_DEVICE_SCREEN_HEIGHT;
        }
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * @return Status bar (top bar) height. Note that this height remains fixed even when status bar is hidden.
     */
    public static int getStatusBarHeight(Context context) {
        int height = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            height = context.getResources().getDimensionPixelSize(resourceId);
        }
        return height;
    }
}
