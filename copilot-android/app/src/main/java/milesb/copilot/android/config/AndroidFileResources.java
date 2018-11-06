package milesb.copilot.android.config;

import android.content.res.Resources;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import milesb.copilot.android.R;
import milesb.copilot.core.common.FileResources;

/**
 * Created by miles on 26/07/2018.
 */

public class AndroidFileResources implements FileResources {

    private Resources resources;

    AndroidFileResources(Resources resources) {

        this.resources = resources;
    }

    @Override
    public InputStream getInputStream(String s) throws IOException {
        InputStream is = null;

        if (s.equals("descriptors.dat")) {
            is = resources.openRawResource(R.raw.descriptors);
        }
        else if (s.equals("labels.txt")) {
            is = resources.openRawResource(R.raw.labels);
        }
        return is;
    }

    @Override
    public OutputStream getOutputStream(String name) throws IOException {
        return null;
    }
}
