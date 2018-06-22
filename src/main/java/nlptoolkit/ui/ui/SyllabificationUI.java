package nlptoolkit.ui.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Component;
import nlptoolkit.ui.ui.views.SyllabificationView;

@Theme("mytheme")
public class SyllabificationUI extends NLPBaseUI {

    @Override
    protected Component InitNLPLayout(VaadinRequest vaadinRequest) {
        return new SyllabificationView();
    }
}
