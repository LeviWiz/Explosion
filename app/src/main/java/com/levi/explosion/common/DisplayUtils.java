package com.levi.explosion.common;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.TextView;

import java.io.IOException;
import java.lang.reflect.Method;

public class DisplayUtils {

    private static final int DEFAULT_DEVICE_SCREEN_WIDTH = 1080;
    private static final int DEFAULT_DEVICE_SCREEN_HEIGHT = 1920;

    public static float dpToPx(Context context, float dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return dp * density;
    }

    public static int dpToPx(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    public static int getNavigationBarHeight(Context context) {
        if (null == context) {
            return 0;
        }
        if (context instanceof Activity && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Activity activityContext = (Activity) context;
            DisplayMetrics metrics = new DisplayMetrics();
            activityContext.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int usableHeight = metrics.heightPixels;
            activityContext.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            int realHeight = metrics.heightPixels;
            if (realHeight > usableHeight) {
                return realHeight - usableHeight;
            } else {
                return 0;
            }
        }

        return getNavigationBarHeightUnconcerned(context);
    }

    public static int getNavigationBarHeightUnconcerned(Context context) {
        if (null == context) {
            return 0;
        }
        Resources localResources = context.getResources();
        if (!hasNavBar(context)) {
            return 0;
        }
        int i = localResources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (i > 0) {
            return localResources.getDimensionPixelSize(i);
        }
        i = localResources.getIdentifier("navigation_bar_height_landscape", "dimen", "android");
        if (i > 0) {
            return localResources.getDimensionPixelSize(i);
        }
        return 0;
    }

    public static boolean hasNavBar(Context paramContext) {
        boolean bool = true;
        String sNavBarOverride;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                Object localObject = Class.forName("android.os.SystemProperties").getDeclaredMethod("get", String.class);
                ((Method) localObject).setAccessible(true);
                sNavBarOverride = (String) ((Method) localObject).invoke(null, "qemu.hw.mainkeys");
                localObject = paramContext.getResources();
                int i = ((Resources) localObject).getIdentifier("config_showNavigationBar", "bool", "android");
                if (i != 0) {
                    bool = ((Resources) localObject).getBoolean(i);
                    if ("1".equals(sNavBarOverride)) {
                        return false;
                    }
                }
            } catch (Throwable localThrowable) {
            }
        }

        if (!ViewConfiguration.get(paramContext).hasPermanentMenuKey()) {
            return bool;
        }

        return false;
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

    public static void hideNavigationBar(Activity activity) {
        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    public static void showNavigationBarAndStatusBar(Activity activity) {
        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
    }

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

    public static String getUrlWithWidth(String url, int width) {
        return url + "?mw=" + ((width + 99) / 100 * 100);
    }

    public static void animateShow(final View view, long duration, float fromAlpha, final Runnable finishRunnable) {
        view.clearAnimation();
        view.setVisibility(View.VISIBLE);
        view.setAlpha(fromAlpha);
        view.animate().alpha(1f)
                .setDuration(duration)
                .setInterpolator(new AccelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (finishRunnable != null) {
                            finishRunnable.run();
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        view.setAlpha(1f);
                    }
                }).start();
    }

    public static void animateHide(final View view, long duration, final Runnable finishRunnable) {
        view.clearAnimation();
        view.animate().alpha(0f)
                .setDuration(duration)
                .setInterpolator(new AccelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (finishRunnable != null) {
                            finishRunnable.run();
                        }
                        view.setAlpha(0f);
                        view.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        view.setAlpha(0f);
                        view.setVisibility(View.GONE);
                    }
                }).start();
    }

    public static void setTextViewLineSpacing(TextView textView, float lineSpacing) {
        Paint.FontMetrics metrics = textView.getPaint().getFontMetrics();
        float fontHeight = metrics.bottom - metrics.top;
        float realFontHeight = Math.abs(metrics.ascent);
        float mult = realFontHeight / fontHeight;
        textView.setLineSpacing(lineSpacing, mult);
    }

    public static int adjustColorAlpha(int color, float factor) {
        int oldAlpha = Color.alpha(color);
        int alpha = Math.round(oldAlpha * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    public static int parseImageDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }
}
