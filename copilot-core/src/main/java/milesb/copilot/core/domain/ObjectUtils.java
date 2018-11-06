package milesb.copilot.core.domain;

import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Size;

public class ObjectUtils {
    static Point rectToCentrePoint(Rect rect) {
    	double x = rect.x + (rect.width/2);
    	double y = rect.y + (rect.height/2);
    	
    	return new Point(x,y);
    }
    
    public static Rect centrePointToRect(Point centre, Size size) {
    	int topLeftX = (int) (centre.x) - ((int)(size.width/2));
    	int topLeftY = (int) (centre.y) - ((int)(size.height/2));
    	
    	return new Rect(topLeftX, topLeftY, (int) size.width, (int) size.height);
    }
}
