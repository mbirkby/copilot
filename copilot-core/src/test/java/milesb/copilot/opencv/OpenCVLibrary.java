package milesb.copilot.opencv;

import org.opencv.core.Core;

public class OpenCVLibrary {
    private static boolean loadedLibrary = false;
    
    public static void loadLibrary() {
    	if (!loadedLibrary) {
    		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    		loadedLibrary = true;
    	}
    }
}
