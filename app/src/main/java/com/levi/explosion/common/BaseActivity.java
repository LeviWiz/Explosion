package com.levi.explosion.common;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created at 2018/7/24 下午7:42.
 *
 * @author yixu.wang
 */

public class BaseActivity extends AppCompatActivity {

    public static final int STATUS_BAR_STYLE_ICON_WHITE = 0;
    public static final int STATUS_BAR_STYLE_ICON_BLACK = 1;

    @IntDef({STATUS_BAR_STYLE_ICON_WHITE, STATUS_BAR_STYLE_ICON_BLACK})
    @Retention(RetentionPolicy.SOURCE)
    public @interface StatusBarStyle {
    }

    @StatusBarStyle
    private int statusBarStyle = STATUS_BAR_STYLE_ICON_WHITE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        immersiveStyleBar();
    }

    private void immersiveStyleBar() {
        int flag = View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        if (Utils.AT_LEAST_M) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            if (statusBarStyle == STATUS_BAR_STYLE_ICON_BLACK) {
                flag |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
        } else if (Utils.AT_LEAST_LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (statusBarStyle == STATUS_BAR_STYLE_ICON_BLACK) {
                getWindow().setStatusBarColor(Color.argb((int) (0.2 * 255), 0, 0, 0));
            } else {
                getWindow().setStatusBarColor(Color.TRANSPARENT);
            }
        }
        getWindow().getDecorView().setSystemUiVisibility(flag);
    }

    @StatusBarStyle
    public int getStatusBarStyle() {
        return statusBarStyle;
    }

    public void setStatusBarStyle(@StatusBarStyle int statusBarStyle) {
        this.statusBarStyle = statusBarStyle;
        immersiveStyleBar();
    }

}
