package nlptoolkit.ui.ui.views;

import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Button;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class Utils {
    static final String NEWLINE_SEPARATOR = System.getProperty("line.separator");

    private static HashMap<Button, FileDownloader> lastDownloaders = new HashMap<>();

    static void bindButtonToFile(Button downloadButton, String filename, String fileContent) {

        FileDownloader lastDownloader = lastDownloaders.getOrDefault(downloadButton, null);
        if (lastDownloader != null) {
            downloadButton.removeExtension(lastDownloader);
        }
        File export = new File(filename);
        try {
            FileWriter writer = new FileWriter(export);
            writer.write(fileContent);
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        StreamResource downloadResource = createFileResource(export);
        downloadResource.setFilename(filename);
        FileDownloader fileDownloader = new FileDownloader(downloadResource);
        if (downloadButton.getExtensions().contains(fileDownloader)) {
            downloadButton.removeExtension(fileDownloader);
        }
        fileDownloader.extend(downloadButton);
        lastDownloaders.put(downloadButton, fileDownloader);
    }

    private static StreamResource createFileResource(File file) {
        StreamResource sr = new StreamResource((StreamResource.StreamSource) () -> {
            try {
                return new FileInputStream(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }, null);
        sr.setCacheTime(0);
        return sr;
    }


}
