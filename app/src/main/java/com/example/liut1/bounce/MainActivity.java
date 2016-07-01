package com.example.liut1.bounce;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{
    private BounceView mBounceView;
    private float x, y;
    private int threadTime = 50;
    private BounceDetectManager bounceDetectManager;
    private Button buttonSave, buttonClear, buttonStart, buttonSelect, buttonAdd, buttonDelete;
    private List<BounceViewSelect> mBounceViewSelect = new ArrayList<>();
    private Context context = this;
    private Boolean threadStartFlag = false;
    private Boolean BounceStartFlag = false;
    private int sensorCallbackCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBounceView = (BounceView)findViewById(R.id.bounce_view);
        initSensor();
        initBounceClick();
        initBounceSelectView();
        bounceThread();

    }
    private void bounceThread(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    if(BounceStartFlag) {
                        Message message = new Message();
                        message.what = 1;
                        mainHandler.sendMessage(message);
                    }
                    try {
                        if(BounceStartFlag) {
                            Thread.sleep(threadTime);
                        }else {
                            Thread.sleep(500);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    private void initBounceClick(){
        buttonSave  = (Button)findViewById(R.id.point_save);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i=0;i<mBounceViewSelect.size();i++) {
                    if(mBounceViewSelect.get(i).getVisibility() == View.VISIBLE) {
                        mBounceView.setBouncePoint(mBounceViewSelect.get(i).getPointX(),
                                mBounceViewSelect.get(i).getPointY(),
                                mBounceViewSelect.get(i).getRadius());
                        mBounceViewSelect.get(i).setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
        buttonClear  = (Button)findViewById(R.id.point_clear);
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                threadStartFlag = false;
                mBounceView.setBouncePointClear();
                for (int i=0;i<mBounceViewSelect.size();i++) {
                    if(mBounceViewSelect.get(i).getVisibility() == View.VISIBLE) {
                        mBounceView.setBouncePoint(mBounceViewSelect.get(i).getPointX(),
                                mBounceViewSelect.get(i).getPointY(),
                                mBounceViewSelect.get(i).getRadius());
                        mBounceViewSelect.get(i).setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
        buttonStart  = (Button)findViewById(R.id.bounce_start);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                threadStartFlag = true;
            }
        });
        buttonSelect  = (Button)findViewById(R.id.bounce_select);
        buttonSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
		        /* 开启Pictures画面Type设定为image */
                intent.setType("image/*");
		        /* 使用Intent.ACTION_GET_CONTENT这个Action */
                intent.setAction(Intent.ACTION_GET_CONTENT);
		        /* 取得相片后返回本画面 */
                startActivityForResult(intent, 1);
            }
        });
        buttonAdd  = (Button)findViewById(R.id.bounce_add);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBounceSelectView();
            }
        });
        buttonDelete  = (Button)findViewById(R.id.bounce_minus);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBounceSelectView();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            Log.e("uri", uri.toString());
            ContentResolver cr = this.getContentResolver();
            try {
                Bitmap bitmap1 = BitmapFactory.decodeStream(cr.openInputStream(uri));
                Bitmap bitmap = mBounceView.resize(bitmap1);
                mBounceView.setmBitmap(bitmap);
            } catch (FileNotFoundException e) {
                Log.e("Exception", e.getMessage(),e);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void onResume(){
        super.onResume();
        bounceDetectManager.BounceSensorRegistor();
    }
    @Override
    public void onPause(){
        super.onPause();
        bounceDetectManager.BounceSensorUnregister();
    }
    public void initSensor(){
        bounceDetectManager = new BounceDetectManager(new BounceDetect.BounceDetectCallback() {
            @Override
            public void doSuccess(float x, float y, float z) {
                if(threadTime > 80){
                    BounceStartFlag = false;
                }
                sensorCallbackCount++;
                if(sensorCallbackCount > 10) {
                    threadTime++;
                    sensorCallbackCount = 0;
                }
//                Log.e("=======", "x="+x+" y="+y+" z="+z);
                if(x > 20 || x < -20 || y > 20 || y < -20 || z > 20 || z < -20)
                {
                    threadTime = 15;
                    if(threadStartFlag) {
                        BounceStartFlag = true;
                    }
                }
            }

            @Override
            public void doFailed() {

            }
        },this,SENSOR_SERVICE);
    }
    public void initBounceSelectView(){
        BounceViewSelect bounceViewSelect;
        bounceViewSelect = (BounceViewSelect)findViewById(R.id.bounce_view_select1);
        bounceViewSelect.setVisibility(View.INVISIBLE);
        mBounceViewSelect.add(bounceViewSelect);
        bounceViewSelect = (BounceViewSelect)findViewById(R.id.bounce_view_select2);
        bounceViewSelect.setVisibility(View.INVISIBLE);
        mBounceViewSelect.add(bounceViewSelect);
        bounceViewSelect = (BounceViewSelect)findViewById(R.id.bounce_view_select3);
        bounceViewSelect.setVisibility(View.INVISIBLE);
        mBounceViewSelect.add(bounceViewSelect);
    }
    public void addBounceSelectView(){
        for(int i=0;i<mBounceViewSelect.size();i++){
            if(mBounceViewSelect.get(i).getVisibility() == View.INVISIBLE){
                mBounceViewSelect.get(i).setVisibility(View.VISIBLE);
                break;
            }
        }
    }
    public void deleteBounceSelectView(){
        for(int i=0;i<mBounceViewSelect.size();i++){
            if(mBounceViewSelect.get(i).getVisibility() == View.VISIBLE){
                mBounceViewSelect.get(i).setVisibility(View.INVISIBLE);
                break;
            }
        }
    }
    public Handler mainHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            mBounceView.setBounceOnce();
            return false;
        }
    });
}
