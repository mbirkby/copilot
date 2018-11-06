package milesb.copilot.core.sign_alerter.identifier;

public interface DescriptorGeneratorFactory {
	DescriptorGenerator createDescriptorGenerator(int featureDetectorId, int descriptorExtractorId);

}
