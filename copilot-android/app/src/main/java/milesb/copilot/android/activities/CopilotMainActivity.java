package milesb.copilot.android.activities;

import android.app.Activity;
import android.content.res.Resources;

import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import org.opencv.core.Mat;
import org.opencv.core.Size;

import milesb.copilot.android.config.AndroidSystemResourcesFactory;
import milesb.copilot.android.utils.CopilotTimer;
import milesb.copilot.core.Copilot;
import milesb.copilot.core.CopilotConfig;
import milesb.copilot.core.CopilotFactory;
import milesb.copilot.core.DefaultCopilotFactory;
import milesb.copilot.core.SystemResources;
import milesb.copilot.core.SystemResourcesFactory;


public class CopilotMainActivity extends Activity implements CvCameraViewListener {


    private static final String TAG = "Copilot";


    private CameraBridgeViewBase mOpenCvCameraView;
    private Copilot copilot;


    private CopilotTimer timer;


    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {

        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i(TAG, "OpenCV loaded successfully");


                    prepareApp();
                    break;
                }

                default: {
                    super.onManagerConnected(status);
                    break;
                }

            }
        }
    };


    /**
     * Only call this when openCV hasd been sucessfully loaded - anything that requires openCV
     * can be put in this
     */
    private void prepareApp() {


        prepareConfig();


        CopilotFactory copilotFactory = new DefaultCopilotFactory();
        copilot = new Copilot(copilotFactory);
        copilot.init();

        mOpenCvCameraView.enableView();


    }

    private void prepareConfig() {
        CopilotConfig config = CopilotConfig.getInstance();
        prepareSystemResources(config);
        prepareCameraResources(config);
    }

    private void prepareSystemResources(CopilotConfig config) {


        Resources resources = this.getResources();
        SystemResourcesFactory systemResourcesFactory = new AndroidSystemResourcesFactory(resources);
        SystemResources systemResources = new SystemResources(systemResourcesFactory);

        config.setSystemResources(systemResources);
    }

    private void prepareCameraResources(CopilotConfig config) {


        int height = mOpenCvCameraView.getMeasuredHeight();
        int width = mOpenCvCameraView.getMeasuredWidth();
        Size size = new Size(width, height);

        config.setFrameSize(size);


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mOpenCvCameraView =  new JavaCameraView(this, -1);
        setContentView(mOpenCvCameraView);
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_8, this, mLoaderCallback);


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        timer = new CopilotTimer();
        timer.start();
        mOpenCvCameraView.setCvCameraViewListener(this);


    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        Log.d(TAG, "Frame rate = " + timer.getAvgFrameRate());

        if (mOpenCvCameraView != null) {
            mOpenCvCameraView.disableView();
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        timer.start();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_8, this, mLoaderCallback);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
        if (mOpenCvCameraView != null) {
            mOpenCvCameraView.disableView();
        }


    }


    @Override
    public void onCameraViewStarted(int width, int height) {
        // TODO Auto-generated method stub

    }


    @Override
    public void onCameraViewStopped() {
        // TODO Auto-generated method stub

    }


    @Override
    public Mat onCameraFrame(Mat inputFrame) {

        Mat out = copilot.processFrame(inputFrame);

        timer.tick();

        return out;
    }


}
