package milesb.copilot.android.config;

import android.content.res.Resources;

import milesb.copilot.core.SystemResourcesFactory;
import milesb.copilot.core.common.AudioResources;
import milesb.copilot.core.common.FileResources;

public class AndroidSystemResourcesFactory implements SystemResourcesFactory {

    private Resources resources;


    public AndroidSystemResourcesFactory(Resources resources) {

        this.resources = resources;
    }


    @Override
    public FileResources createFileResources() {
        return new AndroidFileResources(resources);
    }

    @Override
    public AudioResources createAudioResources() {
        return new AndroidAudioResources() ;

    }

}

