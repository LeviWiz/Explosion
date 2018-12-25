package com.levi.explosion;

import android.os.Bundle;
import android.view.View;

import com.levi.explosion.common.BaseActivity;
import com.levi.explosion.pixelbitmap.PixelStyleView;

/**
 * @author wei.liu
 */
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PixelStyleView pixelStyleView  = findViewById(R.id.psv);
        pixelStyleView.setEndCallBack(new PixelStyleView.EndCallBack() {
            @Override
            public void end() {
                findViewById(R.id.tv).setVisibility(View.VISIBLE);
            }
        });
    }
}
