package milesb.copilot.core.sign_alerter.renderer;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;

import milesb.copilot.core.common.processes.Renderer;
import milesb.copilot.core.domain.Sign;
import milesb.copilot.core.domain.TrackedObject;

public class SignRenderer implements Renderer {

	private static final Scalar RED = new Scalar(0, 0, 255);
	private static final Scalar BLUE = new Scalar(255, 0, 0);
	private static final Scalar BLACK = new Scalar(0, 0, 0);

	private int fontFace = Core.FONT_HERSHEY_PLAIN;
	private int fontScale = 2;

	private Scalar colour;

	@Override
	public void render(Mat image, TrackedObject trackedObject) {
		Sign sign = (Sign) trackedObject;

		colour = getColour(sign);
		drawBoundingRectangle(image, sign);
		drawLabel(image, sign);

	}

	@Override
	public void init() {

	}

	private void drawBoundingRectangle(Mat image, Sign sign) {

		Rect r = sign.getBoundingRect();
		Core.rectangle(image, r.tl(), r.br(), colour, 2);

	}

	private Scalar getColour(Sign sign) {
		Scalar colour;

		if (sign.hasAlert()) {
			colour = RED;
		} else {

			if (sign.isMatchedInCurrentFrame()) {
				colour = BLUE;
			} else {
				colour = BLACK;
			}
		}

		return colour;
	}

	private void drawLabel(Mat image, Sign sign) {

		Rect r = sign.getBoundingRect();

		double yText;

		double xText;

		if (r.tl().y > fontScale) {
			yText = r.tl().y - fontScale;
			xText = r.tl().x;

		} else {
			yText = r.br().y + fontScale;
			xText = r.tl().x;
		}

		Point textPoint = new Point(xText, yText);

		Core.putText(image, sign.getLabel(), textPoint, fontFace, fontScale, colour, 3);

	}

}
