package milesb.copilot.core.sign_alerter.identifier;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import milesb.copilot.core.domain.Sign;
import milesb.copilot.core.domain.TrackedObject;
import milesb.copilot.opencv.OpenCVLibrary;

public class TestSignIdentifier {

	private SignIdentifier signIdentifier;

	private DescriptorGenerator descriptorGenerator;
	private SignIdentifierMatcher signMatcher;
	private SignDatabase signDatabase;

	@Before
	public void setup() {
		OpenCVLibrary.loadLibrary();
		signDatabase = mock(SignDatabase.class);
		signIdentifier = new SignIdentifier(signDatabase);
		descriptorGenerator = mock(DescriptorGenerator.class);
		when(descriptorGenerator.calcDescriptors(isA(Mat.class))).thenReturn(new Mat());
		signMatcher = mock(SignIdentifierMatcher.class);

		signIdentifier.setDescriptorGenerator(descriptorGenerator);
		signIdentifier.setSignMatcher(signMatcher);
	}

	@Test
	public void testIdentifyDoesntOverwriteSignType() {
		SignType[] signTypes = { new SignType(1, "30 MPH"), new SignType(2, "40 MPH") };

		List<TrackedObject> trackedObjects = new ArrayList<>();
		Sign s1 = new Sign();
		s1.setImage(cross());

		Sign s2 = new Sign();
		s2.setImage(cross());

		s2.setImage(new Mat(100, 100, CvType.CV_8UC3));
		s1.setSignType(signTypes[0]);
		trackedObjects.add(s1);
		trackedObjects.add(s2);

		Descriptor descriptor = new Descriptor(2, new Mat());

		when(signMatcher.match(isA(Mat.class))).thenReturn(descriptor);// always return sign type 2

		when(signDatabase.getSignType(1)).thenReturn(signTypes[0]);
		when(signDatabase.getSignType(2)).thenReturn(signTypes[1]);

		signIdentifier.identify(trackedObjects);

		assertNotNull(s1.getSignType());
		assertNotNull(s2.getSignType());

		assertEquals("S1 should be unchanged -signType 1 (30 MPH)", s1.getSignType().getLabel(), "30 MPH");
		assertEquals("S2 should be assignred to signType 2 (40 MPH)", s2.getSignType().getLabel(), "40 MPH");

	}

	@Test
	public void testIdentifySignWhenNoDescriptorMatch() {
		Sign sign = new Sign();
		sign.setImage(cross());

		when(signMatcher.match(isA(Mat.class))).thenReturn(null);

		signIdentifier.identify(sign);

		assertNull(sign.getSignType());

	}

	@Test
	public void testIdentifyWhenNoSignType() {
		Sign sign = new Sign();
		sign.setImage(cross());

		when(signMatcher.match(isA(Mat.class))).thenReturn(new Descriptor(1, new Mat()));
		when(signDatabase.getSignType(1)).thenReturn(null);
		signIdentifier.identify(sign);

		assertNull(sign.getSignType());

	}

	private Mat cross() {
		Mat redCross = new Mat(120, 100, CvType.CV_8UC3);

		Core.line(redCross, new Point(0, 0), new Point(99, 119), new Scalar(0, 0, 255), 4);
		Core.line(redCross, new Point(99, 0), new Point(0, 119), new Scalar(0, 0, 255), 4);

		return redCross;
	}

	public SignType[] getSignTypes() {
		SignType[] types = { new SignType(1, "30 MPH"), new SignType(2, "40 MPH") };

		return types;
	}

}
