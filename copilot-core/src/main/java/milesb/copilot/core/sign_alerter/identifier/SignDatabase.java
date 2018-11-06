package milesb.copilot.core.sign_alerter.identifier;

import java.util.List;

public interface SignDatabase {

	List<Descriptor> getDescriptors();

	SignType getSignType(int signTypeId);

	DescriptorGenerator getDescriptorGenerator();

}
