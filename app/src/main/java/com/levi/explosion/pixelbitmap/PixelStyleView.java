package com.levi.explosion.pixelbitmap;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.levi.explosion.R;
import com.levi.explosion.common.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wei.liu
 * on 2018/12/24.
 */
public class PixelStyleView extends View {

    private Context context;

    private Bitmap[] mBitmaps;
    private int curBitmapIndex;

    /**
     * 结束的flag
     */
    private boolean isOK;
    private List<Ball> mBalls = new ArrayList<>();
    private Paint mPaint;
    /**
     * 格子大小
     */
    private float pixelSize = 5;
    /**
     * 粒子运动时刻
     */
    private long mRunTime;
    /**
     * 时间流
     */
    private ValueAnimator mAnimator;

    public interface EndCallBack {
        void end();
    }

    private EndCallBack endCallBack;

    public void setEndCallBack(EndCallBack endCallBack) {
        this.endCallBack = endCallBack;
    }

    public PixelStyleView(Context context) {
        this(context, null);
    }

    public PixelStyleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        long time = System.currentTimeMillis();
        initCommon();

        init();
        Log.e("Levi_LOG", " : " + (System.currentTimeMillis() - time));
    }

    private void init() {
        initPixelSize(mBitmaps[curBitmapIndex]);
        initBalls(mBitmaps[curBitmapIndex]);
    }

    private void initPixelSize(Bitmap bitmap) {
        float bitmapWidth = bitmap.getWidth();
        float screenWidth = Utils.getPhoneWidth(context);
        if (bitmapWidth >= screenWidth) {
            pixelSize = 1;
        } else {
            //宽度充满屏幕
            pixelSize = screenWidth / bitmapWidth;
        }
    }

    private void initCommon() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inSampleSize = 4;

        mBitmaps = new Bitmap[]{
                BitmapFactory.decodeResource(getResources(), R.drawable.images, options),
                BitmapFactory.decodeResource(getResources(), R.drawable.aaa, options),
                BitmapFactory.decodeResource(getResources(), R.drawable.abb, options),
                BitmapFactory.decodeResource(getResources(), R.drawable.abc, options)
        };

        //初始化时间流ValueAnimator
        mAnimator = ValueAnimator.ofFloat(0, 1);
        mAnimator.setRepeatCount(-1);
        mAnimator.setDuration(2000);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //更新小球位置
                updateBall();
                invalidate();
            }
        });
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (endCallBack != null) {
                    endCallBack.end();
                }
            }
        });
    }

    private void initBalls(Bitmap bitmap) {
        mBalls.clear();
        for (int i = 0; i < bitmap.getWidth(); i++) {
            for (int j = 0; j < bitmap.getHeight(); j++) {
                Ball ball = new Ball();
                ball.x = i * pixelSize + pixelSize / 2;
                ball.y = j * pixelSize + pixelSize / 2;
                ball.vX = (float) (Math.pow(-1, Math.ceil(Math.random() * 1001)) * 40 * Math.random());
                ball.vY = rangeInt(-15, 35);
                ball.aY = 0.98f;
                int color = bitmap.getPixel(i, j);
                ball.color = color;
                ball.born = System.currentTimeMillis();
                mBalls.add(ball);
            }
        }
    }

    private static final String TAG = PixelStyleView.class.getSimpleName();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        long time = System.currentTimeMillis();
        for (Ball ball : mBalls) {
            mPaint.setColor(ball.color);
            if (curBitmapIndex == 1 || curBitmapIndex == 2) {

                float half = pixelSize / 2;
                float x = ball.x - pixelSize / 2;
                float y = ball.y - pixelSize / 2;
                Path mPath = new Path();
                mPath.reset();

                // top left
                mPath.moveTo(x, y + half * 0.73f);
                // top right
                mPath.lineTo(x + half * 2, y + half * 0.73f);
                // bottom left
                mPath.lineTo(x + half * 0.38f, y + half * 1.9f);
                // top
                mPath.lineTo(x + half, y);
                // bottom right
                mPath.lineTo(x + half * 1.62f, y + half * 1.9f);
                // top left
                mPath.lineTo(x, y + half * 0.73f);

                mPath.close();
                canvas.drawPath(mPath, mPaint);
            } else {

                // 圆形像素
                canvas.drawCircle(ball.x, ball.y, pixelSize / 2, mPaint);
            }

            // 正方形像素
            //                canvas.drawRect(ball.x - pixelSize / 2, ball.y - pixelSize / 2, ball.x + pixelSize / 2,
            //                        ball.y + pixelSize / 2, mPaint);

        }
        canvas.restore();

        Log.e(TAG, "onDraw: " + (System.currentTimeMillis() - time));
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //记录点击时间
            mRunTime = System.currentTimeMillis();
            mAnimator.start();
        }
        return true;
    }

    /**
     * 更新小球
     */
    private void updateBall() {
        for (int i = 0; i < mBalls.size(); i++) {
            Ball ball = mBalls.get(i);
            if (System.currentTimeMillis() - mRunTime > 2000) {
                mBalls.remove(i);
            }
            if (mBalls.isEmpty()) {//表示本张已结束
                if (curBitmapIndex == mBitmaps.length - 1) {
                    mAnimator.end();
                    return;
                }
                curBitmapIndex++;
                initPixelSize(mBitmaps[curBitmapIndex]);
                initBalls(mBitmaps[curBitmapIndex]);

                isOK = true;

                invalidate();
                mRunTime = System.currentTimeMillis();
                mAnimator.pause();
            }
            if (isOK) {//如果本张结束---返回掉
                isOK = false;
                return;
            }
            ball.x += ball.vX;
            ball.y += ball.vY;
            ball.vY += ball.aY;
            ball.vX += ball.aX;
        }
    }

    /**
     * 获取范围随机整数：如 rangeInt(1,9)
     *
     * @param s 前数(包括)
     * @param e 后数(包括)
     * @return 范围随机整数
     */
    public static int rangeInt(int s, int e) {
        int max = Math.max(s, e);
        int min = Math.min(s, e) - 1;
        return (int) (min + Math.ceil(Math.random() * (max - min)));
    }
}
