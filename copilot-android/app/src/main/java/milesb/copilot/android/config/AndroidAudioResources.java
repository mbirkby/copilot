package milesb.copilot.android.config;

import android.media.AudioManager;
import android.media.ToneGenerator;

import milesb.copilot.core.common.AudioResources;


public class AndroidAudioResources implements AudioResources {
    private  ToneGenerator toneGenerator;
	
	AndroidAudioResources() {
    	toneGenerator=new ToneGenerator(AudioManager.STREAM_ALARM, 100);
    	 
    }


	@Override
	public void beep() {
	    toneGenerator.startTone(ToneGenerator.TONE_CDMA_ABBR_ALERT, 100);
	}

}

