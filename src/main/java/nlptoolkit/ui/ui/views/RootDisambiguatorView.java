package nlptoolkit.ui.ui.views;

import Corpus.Sentence;
import MorphologicalAnalysis.FsmParse;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import nlptoolkit.ui.services.NLPService;

import java.util.ArrayList;

public class RootDisambiguatorView extends NLPView {

    @Override
    public String GetScreenName() {
        return "RootDisambiguator";
    }

    public RootDisambiguatorView() {

        final VerticalLayout layout = new VerticalLayout();
        final HorizontalLayout row1 = new HorizontalLayout();
        row1.setSizeFull();
        TextArea txtInput = new TextArea("Write Sentence For RootDisambiguator:","İstanbül'da lokilde gezdik.");
        txtInput.setRows(10);
        txtInput.setSizeFull();
        TextArea txtOutput = new TextArea("Output for RootDisambiguator");
        txtOutput.setSizeFull();
        txtOutput.setRows(10);
        Button btn = new Button("RootDisambiguator");

        row1.addComponents(txtInput,txtOutput);
        layout.addComponent(row1);
        layout.addComponents(btn);

        NLPService nlpService = new NLPService();
        btn.addClickListener(e -> {
            txtOutput.clear();
            Sentence mysentence=new Sentence(txtInput.getValue());
            ArrayList<FsmParse> correctParses = nlpService.Disambiguate(mysentence);
            txtOutput.setValue(txtOutput.getValue() + correctParses.toString());
        });
        addComponent(layout);
    }

}
