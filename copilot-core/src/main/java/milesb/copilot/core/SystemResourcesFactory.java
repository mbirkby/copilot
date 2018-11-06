package milesb.copilot.core;

import milesb.copilot.core.common.AudioResources;
import milesb.copilot.core.common.FileResources;

public interface SystemResourcesFactory {
    FileResources createFileResources();
    AudioResources createAudioResources();
}
