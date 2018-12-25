package com.levi.explosion.common;

import android.content.Context;
import android.support.v7.widget.ViewUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.levi.explosion.R;

/**
 * Created by wei.liu
 * on 2018/12/25.
 */
public class CustomRootView extends FrameLayout {

    public CustomRootView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        View containerView = findViewById(R.id.container_view);
        containerView.setPadding(0, Utils.getStatusBarHeight(getContext()), 0, 0);
    }
}