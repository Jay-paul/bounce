package com.example.liut1.bounce;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by liut1 on 6/30/16.
 */
public class BounceViewSelect extends View {
    //circle radius
    private float circleRadius = 150;
    private float x=500, y=500;
    private int selected = 0;//1 move, 2 resize
    private float distance=0;
    public BounceViewSelect(Context context) {
        super(context);
    }

    public BounceViewSelect(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BounceViewSelect(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BounceViewSelect(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    @Override
    public void onDraw(Canvas canvas){
//        Log.e("=======","w="+getWidth()+" h="+getHeight());
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE); //设置空心
        paint.setStrokeWidth(20); //设置圆环的宽度
        paint.setAntiAlias(true);  //消除锯齿
        canvas.drawCircle(x,y,circleRadius,paint);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
//        Log.e("============","x="+event.getX()+"  y="+event.getY());
//        Log.e("========","ACTION_MOVE          " + event.getAction());
        switch (event.getAction()){
            case MotionEvent.ACTION_MOVE:
                if(selected == 1) {
                            Log.e("======1111======","x="+x+"  y="+y);
                    setCirclePoint(event.getX(), event.getY(),circleRadius);
                }
                else if(selected == 2){

                            Log.e("=======2222=====","x="+x+"  y="+y);
                    setCirclePoint(x, y,mathDistance(event.getX(),event.getY()));
                }
//                Log.e("========","ACTION_MOVE");
                break;
            case MotionEvent.ACTION_DOWN:
                distance = mathDistance(event.getX(),event.getY());
                if(mathDistance(event.getX(),event.getY()) < circleRadius/2){
                    selected = 1;
                }
                else if(mathDistance(event.getX(),event.getY()) < circleRadius+50){
                    selected = 2;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                selected = 0;
                break;
        }
        super.onTouchEvent(event);
        if(distance > circleRadius+50) {
            return false;
        }else {
            return true;
        }
    }
    public void setCirclePoint(float x, float y, float r){
        this.x = x;
        this.y = y;
        this.circleRadius = r;
        invalidate();
    }
    private float mathDistance(float cx, float cy){
        float dd = (float) (Math.pow( Math.abs(x-cx), 2 ) + Math.pow( Math.abs(y - cy), 2 ));
        //计算每个座标点与当前点（cx、cy）之间的距离
        return (float)Math.sqrt(dd);
    }
    public float getPointX(){
        return this.x;
    }
    public float getPointY(){
        return this.y;
    }
    public float getRadius(){
        return this.circleRadius;
    }
}
