package nlptoolkit.ui.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import nlptoolkit.ui.ui.views.*;

import java.util.Hashtable;

@Theme("mytheme")
public class NavigatorUI extends UI {
    private Navigator _Navigator;

    public Hashtable<String,NLPView> NLPViews = new Hashtable<>();

    protected void RegisterNLPView(NLPView view){
        view.NavigatorUI = this;
        NLPViews.put(view.GetScreenName(),view);
    }

    @Override
    protected void init(VaadinRequest request) {
        getPage().setTitle("Nlptoolkit.UI");

        //Register views.
        RegisterNLPView(new MorphologicalAnalyzerDisambiguatorView());
        RegisterNLPView(new MAWrapperView());
        RegisterNLPView(new SyllabificationView());
        RegisterNLPView(new SentenceSplitterView());
        RegisterNLPView(new AsciifierDeasciifierView());
        RegisterNLPView(new SpellCheckerView());

        // Create a navigator to control the views
        _Navigator = new Navigator(this, this);

        //Main View.
        MainView mainView = new MainView(_Navigator,this);
        _Navigator.addView("Main",mainView);
        _Navigator.navigateTo("Main");
    }
}