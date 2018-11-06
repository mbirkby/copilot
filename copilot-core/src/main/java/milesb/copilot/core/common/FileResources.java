package milesb.copilot.core.common;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface FileResources {
    InputStream getInputStream(String name) throws IOException;
    OutputStream getOutputStream(String name) throws IOException;
    
}
