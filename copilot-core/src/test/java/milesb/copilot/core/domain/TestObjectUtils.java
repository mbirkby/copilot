package milesb.copilot.core.domain;

import static org.junit.Assert.*;

import org.junit.Test;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Size;

import milesb.copilot.core.domain.ObjectUtils;

public class TestObjectUtils {
   @Test
   public void testRectToCentrePoint() {
	   Rect rect = new Rect(5,7,11,13);
	   Point centre = ObjectUtils.rectToCentrePoint(rect);
	   assertEquals("Incorrect x",10.0,centre.x,0.05);
	   assertEquals("Incorrect y",13.0,centre.y,0.05);
   }
   
   @Test
   public void testCentrePointToRect() {
	   Point centre = new Point(10.0,13.0);
	   Size size = new Size(11,13);
	   
	   Rect rect = ObjectUtils.centrePointToRect(centre, size);
	   assertEquals("Incorrect top left x", 5.0, rect.tl().x, 0.05);
	   assertEquals("Incorrect top left y", 7.0, rect.tl().y, 0.05);
   }
   
}
