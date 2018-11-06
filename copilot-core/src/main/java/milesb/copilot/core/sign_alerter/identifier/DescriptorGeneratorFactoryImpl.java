package milesb.copilot.core.sign_alerter.identifier;

import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.FeatureDetector;

public class DescriptorGeneratorFactoryImpl implements DescriptorGeneratorFactory {

	private static DescriptorGeneratorFactoryImpl descriptorGeneratorFactory;

	private DescriptorGeneratorFactoryImpl() {

	}

	@Override
	public DescriptorGenerator createDescriptorGenerator(int featureDetectorId, int descriptorExtractorId) {
		FeatureDetector featureDetector = FeatureDetector.create(featureDetectorId);
		DescriptorExtractor descriptorExtractor = DescriptorExtractor.create(descriptorExtractorId);

		return  new DescriptorGeneratorImpl(featureDetector, descriptorExtractor);

	}

	public static DescriptorGeneratorFactoryImpl getInstance() {
		if (descriptorGeneratorFactory == null) {
			descriptorGeneratorFactory = new DescriptorGeneratorFactoryImpl();
		}

		return descriptorGeneratorFactory;
	}

}
