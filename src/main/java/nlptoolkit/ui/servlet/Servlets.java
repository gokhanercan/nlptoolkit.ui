package nlptoolkit.ui.servlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinServlet;
import nlptoolkit.ui.ui.AsciifierDeasciifierUI;
import nlptoolkit.ui.ui.NavigatorUI;

import javax.servlet.annotation.WebServlet;

public class Servlets {

    @WebServlet(urlPatterns = "/*", name = "NLPServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = NavigatorUI.class, productionMode = false)  //Modify class name to set your ui screen as an initial screen.      //TODO:productionmode ?
    public static class NLPServlet extends VaadinServlet {

    }
}
