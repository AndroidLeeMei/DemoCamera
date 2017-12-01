package com.example.auser.democamera;

import android.content.Context;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

/**
 * Created by auser on 2017/12/1.
 * 若要相機功能好一點,要使用SurfaceView
 * SurfaceHolder.Callback可以管理相機的一些功能
 */
//
public class CameraView extends SurfaceView implements SurfaceHolder.Callback{
    private Camera mCamera;
    private SurfaceHolder mSurfHolder;
    private Context mContext;


    public CameraView(Context context) {
        super(context);
        mSurfHolder=getHolder();
        mSurfHolder.addCallback(this);
        mContext=context;
    }

    public void createdCamera(){
        if (mCamera!=null)
            return;
        else
            mCamera=Camera.open();
    }

    public Camera getmCamera(){
        return mCamera;
        }
    //=========code/generate/setter================================================

    public void setmCamera(Camera mCamera) {
        this.mCamera = mCamera;
    }
//========================================================
    @Override//可以把拍攝的鏡頭顯示到營幕
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        if (mCamera==null){
            mCamera=Camera.open();
        }
        refreshCamera();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        refreshCamera();
    }

    public void stopPreview(){
        if (mCamera!=null){
            mCamera.stopPreview();
            mCamera.release();
            mCamera=null;
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        stopPreview();
    }


    public void refreshCamera() {
        if (mSurfHolder.getSurface() == null || mCamera == null)
            return;

        try {
            mCamera.setPreviewDisplay(mSurfHolder);

            Camera.CameraInfo camInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(0, camInfo);


            int rotation = ((AppCompatActivity) mContext).getWindowManager().getDefaultDisplay().getRotation();
            System.out.println("rotation = " + rotation); //直立正著拿的時候rotation=0，逆時針轉90度rotation=1，依此類推
            int degrees = 0;
            switch (rotation) {
                case Surface.ROTATION_0:
                    degrees = 0;
                    break;
                case Surface.ROTATION_90:
                    degrees = 90;
                    break;
                case Surface.ROTATION_180:
                    degrees = 180;
                    break;
                case Surface.ROTATION_270:
                    degrees = 270;
                    break;
            }

            int result = (camInfo.orientation - degrees + 360) % 360;
            System.out.println(
                    "result = " + result + " , orientation = " + camInfo.orientation + " , degrees = " + degrees);
            mCamera.setDisplayOrientation(result);
            Camera.Parameters camParas = mCamera.getParameters();
            if (camParas.getFocusMode().equals(Camera.Parameters.FOCUS_MODE_AUTO)
                    || camParas.getFocusMode().equals(Camera.Parameters.FOCUS_MODE_MACRO)) {
                //mCamera.autoFocus(onCamAutoFocus);
            } else
                Toast.makeText(mContext, "照相機不支援自動對焦！", Toast.LENGTH_SHORT).show();

            mCamera.startPreview();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.d("MyCamera",e.getMessage());
//            Toast.makeText(mContext, "照相機啟始錯誤！", Toast.LENGTH_LONG).show();
        }

    }
    //Callback:回呼
    Camera.AutoFocusCallback onCamAutoFocus= new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean b, Camera camera) {

        }
    };
}
