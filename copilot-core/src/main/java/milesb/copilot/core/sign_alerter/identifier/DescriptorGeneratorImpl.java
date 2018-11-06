package milesb.copilot.core.sign_alerter.identifier;

import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.FeatureDetector;


public class DescriptorGeneratorImpl implements DescriptorGenerator {

    private FeatureDetector featureDetector;
    private DescriptorExtractor descriptorExtractor;

    DescriptorGeneratorImpl(FeatureDetector featureDetector, DescriptorExtractor descriptorExtractor) {
        this.featureDetector = featureDetector;
        this.descriptorExtractor = descriptorExtractor;
    }

    public Mat calcDescriptors(Mat greyImage) {

        MatOfKeyPoint keypoints = new MatOfKeyPoint();

        featureDetector.detect(greyImage, keypoints);

        Mat descriptors = new Mat();

        descriptorExtractor.compute(greyImage, keypoints, descriptors);

        return descriptors;

    }

}
