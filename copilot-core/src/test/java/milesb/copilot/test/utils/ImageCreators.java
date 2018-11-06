package milesb.copilot.test.utils;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;

public class ImageCreators {
   
    	public static Mat cross() {
    		Mat redCross = new Mat(120, 100, CvType.CV_8UC3);
    		
    		Core.line(redCross, new Point(0,0), new Point(99,119), new Scalar(0,0,255), 4);
    		Core.line(redCross, new Point(99,0), new Point(0,119), new Scalar(0,0,255), 4);
    		
    		return redCross;
    	}
  
}
