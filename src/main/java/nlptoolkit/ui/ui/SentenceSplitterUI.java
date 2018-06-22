package nlptoolkit.ui.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Component;
import nlptoolkit.ui.ui.views.SentenceSplitterView;

@Theme("mytheme")
public class SentenceSplitterUI extends NLPBaseUI {

    @Override
    protected Component InitNLPLayout(VaadinRequest vaadinRequest) {
       return new SentenceSplitterView();
    }
}
