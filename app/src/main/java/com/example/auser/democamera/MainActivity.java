package com.example.auser.democamera;

import android.content.Intent;
import android.hardware.Camera;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    Camera mCmaera;
    CameraView mCameraView;
    FrameLayout mFrameLayout;
    LinearLayout mCameraLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFrameLayout=(FrameLayout) getLayoutInflater().inflate(R.layout.activity_main,null);//找出根源樹物件
        mCameraLayout=mFrameLayout.findViewById(R.id.cameraLayout);
        mCameraView=new CameraView(this);
        mCameraLayout.addView(mCameraView);

        setContentView(mFrameLayout);
//        mCmaera=Camera.open();
//        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCameraView!=null){
            mCameraView.stopPreview();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCmaera=mCameraView.getmCamera();
        if (mCameraView==null) {
            mCmaera=mCameraView.getmCamera();
            if (mCmaera == null) {
                mCameraView.createdCamera();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public  void onShowPhoto(View target){
        Log.d("MyCamera","onShowPhoto start1");
        Intent intent=new Intent(Intent.ACTION_VIEW);
        Log.d("MyCamera","onShowPhoto start2");
        File file=new File("/sdcard/picture.jpg");
        Log.d("MyCamera","onShowPhoto start3");
        intent.setDataAndType(Uri.fromFile(file),"image/*");
        Log.d("MyCamera","onShowPhoto start4");
        startActivity(intent);
        Log.d("MyCamera","onShowPhoto start5");

    }

    public void onTakePhoto(View target){
        mCmaera=mCameraView.getmCamera();
        mCmaera.takePicture(camShutterCallback,camRawDataCallback,camJpegCallback);
    }


    private Camera.ShutterCallback camShutterCallback = new Camera.ShutterCallback() {
        public void onShutter() {
            System.out.println("onShutter()");
            // 通知使用者已完成拍照,例如發出一個模擬快門的聲音。
        }
    };

    private Camera.PictureCallback camRawDataCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            System.out.println("camRawDataCallback");
            // 接收原始的影像資料。
        }
    };

    private Camera.PictureCallback camJpegCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            System.out.println("camJpegCallback");
            // 接收壓縮成jpeg格式的影像資料。
            FileOutputStream outStream = null;
            try {
                //date = getDate();
                outStream = new FileOutputStream("/sdcard/picture.jpg");
                outStream.write(data);
                outStream.close();
            } catch (IOException e) {
                Toast.makeText(MainActivity.this, "影像檔儲存錯誤！", Toast.LENGTH_SHORT).show();
            }

            if (mCmaera != null)
                mCmaera.startPreview();
        }
    };
}
