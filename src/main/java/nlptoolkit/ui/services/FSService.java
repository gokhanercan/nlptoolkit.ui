package nlptoolkit.ui.services;

import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServlet;

import javax.servlet.ServletContext;
import java.io.File;

/*
Manages file system(FS) dependencies.
*/
public class FSService {

    private ServletContext _ServletContext = VaadinServlet.getCurrent().getServletContext();
    private String RESOURCES_FOLDER = "Resources";

    private String GetRootContextPath() {
        return VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
    }
    /*
    Root resource path of all files used by our framework.
    */

    public String GetResourceFilesRootPath() {
        return GetRootContextPath() + File.separatorChar + File.separatorChar + RESOURCES_FOLDER + File.separatorChar + File.separatorChar;
    }
}
