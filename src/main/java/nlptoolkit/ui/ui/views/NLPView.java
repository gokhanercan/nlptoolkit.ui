package nlptoolkit.ui.ui.views;

import com.vaadin.navigator.View;
import com.vaadin.ui.VerticalLayout;
import nlptoolkit.ui.ui.NavigatorUI;

public abstract class NLPView extends VerticalLayout implements View {

    //TODO: Do something on the layout to set the screen name

    public NLPView(NavigatorUI navigatorUI) {
        this.NavigatorUI = navigatorUI;
    }
    public NLPView() {
    }

    public NavigatorUI NavigatorUI;

    public abstract String GetScreenName();
}
