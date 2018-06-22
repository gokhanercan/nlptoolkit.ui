package nlptoolkit.ui.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import nlptoolkit.ui.services.FSService;

/**
 * UI base class of nlptoolkit. TODO: Obsolete now. We need logging impl on NLPViewBase.
 */
//@Theme("mytheme")
public abstract class NLPBaseUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        VerticalLayout mainLayout = new VerticalLayout();
        Component uilayout = InitNLPLayout(vaadinRequest);
        mainLayout.addComponent(uilayout);

        //debug mode
        //boolean isProd = !VaadinService.getCurrent().getDeploymentConfiguration().isProductionMode();
        Boolean isDebugMode  = vaadinRequest.getParameter("debug") == null ? false : true;
        if (isDebugMode){
            String rootPath = new FSService().GetResourceFilesRootPath();
            mainLayout.addComponent(new Label("Absolute root context path: " + rootPath));
        }

        setContent(mainLayout);
    }

    protected abstract Component InitNLPLayout(VaadinRequest vaadinRequest);

}
