package milesb.copilot.android.utils;

import java.util.Date;

/**
 * Created by miles on 31/10/2018.
 */

public class CopilotTimer {
    private static final int BATCH_SIZE = 1000;

    private int frameCount;
    private int batchCount;
    private double totalAvgFrameRate;

    private Date startTime;


    public CopilotTimer() {
        frameCount = 0;
        batchCount = 0;
        totalAvgFrameRate = 0;

    }

    public void start() {
       startTime = new Date();

    }


    public void tick() {
        frameCount++;
        if (frameCount >= BATCH_SIZE) {


            double framesPerSec = batchFrameRate();


            totalAvgFrameRate += framesPerSec;
            batchCount++;

            frameCount = 0;
            startTime = new Date();

        }
    }


    private double batchFrameRate() {

        Date endTime = new Date();
        double timeInSecs = (endTime.getTime() - startTime.getTime())/1000;

        double framesPerSec;
        if (frameCount == 0) {
            framesPerSec = 0;
        }
        else  {
            framesPerSec = Double.MAX_VALUE;
            if (timeInSecs > 0) {
                framesPerSec = frameCount / timeInSecs;
            }
        }


        return framesPerSec;
    }

    public double getAvgFrameRate() {
        if (batchCount > 0) {
            return totalAvgFrameRate / batchCount;
        }
        else  {
            return batchFrameRate();
        }
    }
}
