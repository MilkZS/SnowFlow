package com.milkzs.android.snowflow;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

public class SnowFlow extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private String TAG = "SnowFlow";
    private boolean DBG = true;

    private boolean ifRun = true;
    private Canvas mCanvas;
    private SurfaceHolder surfaceHolder;

    private Random random = new Random();


    private int width;
    private int height;

    private int countSnow = 1;
    private int addSnow = 2;
    private int sumSnow = 50;
    private ArrayList<Snow> snowArrayList = new ArrayList<>();
    private Paint mPaint;
    private Bitmap drawBitmap;

    public SnowFlow(Context context) {
        super(context);
        init();
    }

    public SnowFlow(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        width = 200;
        Bitmap oldPic = BitmapFactory.decodeResource(getResources(), R.drawable.snow_1);
        drawBitmap = zoomImg(oldPic, 50, 50);

        snowArrayList.clear();
        snowArrayList.add(new Snow.Builder()
                .addsnow_x(getRandom(width))
                .addsnow_y(getRandom())
                .build());
        mPaint = new Paint();
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    private int getRandom() {
        return getRandom(1);
    }

    private int getRandom(int bound) {
        Log.d(TAG,"random bound is : " + bound);
        return random.nextInt(bound);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        ifRun = true;
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        ifRun = false;
    }

    @Override
    public void run() {
        while (ifRun) {
            changeCoordinate();
            drawView();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void changeCoordinate() {
        boolean bool = false;
        for (int i = 0; i < countSnow; i++) {
            Snow snow = snowArrayList.get(i);
            if (snow.getSnow_y() >= height) {
                snow.setSnow_y(getRandom());
                snow.setSnow_x(getRandom(width));
                bool = true;
                continue;
            }
            snow.setSnow_y(snow.getSnow_y() + snow.getSpeed());
        }
        if (!bool) {
            countSnow++;
            if(snowArrayList.size() < countSnow){
                snowArrayList.add(new Snow.Builder()
                        .addsnow_x(getRandom(width))
                        .addsnow_y(getRandom())
                        .build());
            }
        }
    }

    /**
     * draw view using canvas.
     */
    private void drawView() {
        mCanvas = surfaceHolder.lockCanvas();

        for (int i = 0; i < countSnow; i++) {
            Snow snow = snowArrayList.get(i);
            mCanvas.drawBitmap(drawBitmap, snow.getSnow_x(), snow.getSnow_y(), mPaint);
        }

        surfaceHolder.unlockCanvasAndPost(mCanvas);
    }

    public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }
}
