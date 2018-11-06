package milesb.copilot.core;

import milesb.copilot.core.common.AudioResources;
import milesb.copilot.core.common.FileResources;

public class SystemResources {
    
	private FileResources fileResources;
	private AudioResources audioResources;
		
	
	public SystemResources(SystemResourcesFactory factory) {
    	fileResources = factory.createFileResources();
    	audioResources = factory.createAudioResources();
    }
	
	
	public FileResources getFileResources() {
		return fileResources;
	}
	
	
	public AudioResources getAudioResources() {
		return audioResources;
	}
	

}
