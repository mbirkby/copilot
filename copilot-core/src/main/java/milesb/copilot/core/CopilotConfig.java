package milesb.copilot.core;

import org.opencv.core.Size;

public class CopilotConfig {
	private static CopilotConfig copilotConfig;

	private SystemResources systemResources;
	private Size frameSize;

	private CopilotConfig() {

	}

	public SystemResources getSystemResources() {
		return systemResources;
	}

	public void setSystemResources(SystemResources systemResources) {
		this.systemResources = systemResources;
	}

	public Size getFrameSize() {
		return new Size(frameSize.width, frameSize.height);
	}

	public void setFrameSize(Size size) {
		frameSize = new Size(size.width, size.height);
	}

	public static CopilotConfig getInstance() {
		if (copilotConfig == null) {
			copilotConfig = new CopilotConfig();
		}

		return copilotConfig;
	}
}
