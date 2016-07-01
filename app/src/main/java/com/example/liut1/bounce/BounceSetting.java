package com.example.liut1.bounce;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liut1 on 6/29/16.
 */
public class BounceSetting {
    public interface invalidateBitmap{
        public void setInvalidate();
    }
    private Bitmap mBitmap;
    //定义两个常量，这两个常量指定该图片横向、纵向上都被划分为20格。
    private final int WIDTH = 40;
    private final int HEIGHT = 40;
    //记录该图片上包含441个顶点
    private final int COUNT = (WIDTH + 1) * (HEIGHT + 1);
    //定义一个数组，保存Bitmap上的21 * 21个点的座标
    private final float[] verts = new float[COUNT * 2];
    //定义一个数组，记录Bitmap上的21 * 21个点经过扭曲后的座标
    //对图片进行扭曲的关键就是修改该数组里元素的值。
    private final float[] orig = new float[COUNT * 2];
    private List<List<Integer>> circle = new ArrayList<>();
    private List<Float> bouncePoint = new ArrayList<>();
    private List<Float> diffDistance = new ArrayList<>();
    private List<Float> radius = new ArrayList<>();
    private int sequence[] = new int[]{3,6,9,12,15,12,9,6,3,0,-3,-6,-9,-12,-15,-12,-9,-6,-3,0};
    private int sequenceNum = 0;
    public BounceSetting(){

    }
    public Bitmap getmBitmap(){
        return mBitmap;
    }
    public void setBitmap(Bitmap bitmap){
        mBitmap = bitmap;
        setBitmapInfo();
    }
    public float[] getVerts(){
        return verts;
    }
    public int getBitmapWidth(){
        return WIDTH;
    }
    public int getBitmapHeight(){
        return HEIGHT;
    }
    public void setBitmapInfo(){
        float bitmapWidth = mBitmap.getWidth();
        float bitmapHeight = mBitmap.getHeight();
//        Log.e("=============","w="+bitmapWidth+":h="+bitmapHeight);
        int index = 0;
        for (int y = 0; y <= HEIGHT; y++)
        {
            float fy = bitmapHeight * y / HEIGHT;
            for (int x = 0; x <= WIDTH; x++)
            {
                float fx = bitmapWidth * x / WIDTH;
                    /*
                     * 初始化orig、verts数组。
                     * 初始化后，orig、verts两个数组均匀地保存了21 * 21个点的x,y座标
                     */
                orig[index * 2 + 0] = verts[index * 2 + 0] = fx;
                orig[index * 2 + 1] = verts[index * 2 + 1] = fy;
                index += 1;
            }
        }
    }
    public void bounceOnce(){
        for(int i = 0;i<circle.size();i++){
            for(int j=0;j<circle.get(i).size();j+=2) {
                if(sequenceNum > sequence.length-1){
                    sequenceNum = 0;
                }
                verts[circle.get(i).get(j + 1)] = orig[circle.get(i).get(j + 1)] + sequence[sequenceNum];

            }
        }
        sequenceNum++;
    }
    public void setCirclePoints(float cx, float cy, float cr){
        if(circle.size() >= 3){
            return;
        }
        bouncePoint.add(cx);
        bouncePoint.add(cy);
//        bouncePoint.add(cr);
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < COUNT * 2; i += 2)
        {
            float dx = cx - orig[i + 0];
            float dy = cy - orig[i + 1];
            float dd = dx * dx + dy * dy;
            //计算每个座标点与当前点（cx、cy）之间的距离
            float d = (float)Math.sqrt(dd);
            //对verts数组（保存bitmap上21 * 21个点经过扭曲后的座标）重新赋值
            if(d < cr){
                list.add(i);
                list.add(i+1);
            }
        }
        circle.add(list);
    }
    public void setCirclePointsClear(){
        while (circle.size() > 0){
            circle.remove(0);
        }
    }
    public void setDiffDistance(float maxDiff){
        diffDistance.clear();
        for(int i=1;i<11;i++){
            diffDistance.add(0-maxDiff/i);
        }
        diffDistance.add((float) 0);
        for(int i=11;i>0;i--){
            diffDistance.add(maxDiff/i);
        }
    }
    public List<Float> getDiffDistance(){
        return diffDistance;
    }
}
