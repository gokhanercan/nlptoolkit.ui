package nlptoolkit.ui.ui.views;


import com.vaadin.annotations.DesignRoot;
import com.vaadin.navigator.Navigator;
import com.vaadin.ui.*;

import java.util.ArrayList;
import java.util.Collections;

@DesignRoot
public class MainView extends NLPView {

    public Navigator Navigator;

    @Override
    public String GetScreenName() {
        return "Homepage";
    }

    // Menu navigation button listener
    class ButtonListener implements Button.ClickListener {
        String menuitem;

        public ButtonListener(String menuitem) {
            this.menuitem = menuitem;
        }

        @Override
        public void buttonClick(Button.ClickEvent event) {
            Navigator.navigateTo("Main" + "/" + menuitem);
        }
    }

    VerticalLayout menuContent;
    Panel MainPanel;

    public MainView(Navigator navigator, nlptoolkit.ui.ui.NavigatorUI navigatorUI) {
        NavigatorUI = navigatorUI;
        Navigator = navigator;
        MainPanel = new Panel();        //rename

        //Building nlpview menus
        MenuBar topMenu = new MenuBar();
        topMenu.addItem("Home", (MenuBar.Command) menuItem -> {
            ShowHomeContent();
        });
        ArrayList<String> sorted_items = Collections.list(NavigatorUI.NLPViews.keys());
        Collections.sort(sorted_items);
//        for (Map.Entry<String, NLPView> pair : sorted_pairs) {
        for (String key : sorted_items) {
            topMenu.addItem(key, (MenuBar.Command) menuItem -> {
                GotoView(key);
            });
        }

        //add
        addComponent(topMenu);
        addComponent(MainPanel);        //todo: rename.
    }

    public void ShowHomeContent() {
        Label lbl = new Label("This is an interface for quick testing of the Turkish NLP toolkit project" +
                " hosted by Olcay Taner Yıldız. For the source codes, please visit: https://github.com/olcaytaner");
        this.MainPanel.setContent(lbl);
    }

    public void GotoView(String viewName) {
        NLPView view = this.NavigatorUI.NLPViews.get(viewName);
        GotoView(view);
    }

    public void GotoView(NLPView view) {
        this.MainPanel.setContent(view);
    }

//    @Override
//    public void enter(ViewChangeListener.ViewChangeEvent event) {
//        if (event.getParameters() == null || event.getParameters().isEmpty()) {
//            MainPanel.setContent(new Label("hiç param yok."));
//        }
//        else
//        {
//            Component view = this.NavigatorUI.NLPViews.get(event.getParameters());
//            this.MainPanel.setContent(view);
//        }
//    }
}