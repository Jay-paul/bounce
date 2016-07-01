package com.example.liut1.bounce;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by liut1 on 6/29/16.
 */
public class BounceView extends View{
    private Bitmap mBitmap;
    private BounceSetting bounceSetting = new BounceSetting();
    public BounceView(Context context) {
        super(context);
    }

    public BounceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BounceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BounceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    @Override
    public void onDraw(Canvas canvas){
        mBitmap = bounceSetting.getmBitmap();
        if(mBitmap == null){
            mBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.bounce2);
            if(mBitmap == null){
                return;
            }
            mBitmap = resize(mBitmap);
            bounceSetting.setBitmap(mBitmap);
        }
        canvas.drawBitmapMesh(mBitmap, bounceSetting.getBitmapWidth(), bounceSetting.getBitmapHeight(), bounceSetting.getVerts()
                , 0, null, 0, null);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        bounceSetting.setCirclePoints(event.getX(), event.getY(),150);
        return super.onTouchEvent(event);
    }
    public void setmBitmap(Bitmap bitmap){
        bounceSetting.setBitmap(bitmap);
    }
    public void setBouncePoint(float x, float y, float r){
        bounceSetting.setCirclePoints(x, y, r);
    }
    public void setBouncePointClear(){
        bounceSetting.setCirclePointsClear();
    }
    public void setBounceOnce(){
        bounceSetting.bounceOnce();
        invalidate();
    }
    public Bitmap resize(Bitmap bitmap) {
        float sx,sy;
        sx = (float)getWidth()/(float) bitmap.getWidth();
        sy = (float)getHeight()/(float) bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(sx,sy); //长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
        return resizeBmp;
    }
}
