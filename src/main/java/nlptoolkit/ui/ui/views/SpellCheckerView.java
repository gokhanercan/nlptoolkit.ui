package nlptoolkit.ui.ui.views;

import Corpus.Sentence;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import nlptoolkit.ui.services.NLPService;

public class SpellCheckerView extends NLPView {

    @Override
    public String GetScreenName() {
        return "Spell Checker";
    }

    public SpellCheckerView() {

        //Layout
        final VerticalLayout layout = new VerticalLayout();
        final HorizontalLayout row1 = new HorizontalLayout();
        row1.setSizeFull();
        TextArea txtInput = new TextArea("Write Sentence For SpellChecker:","İstanbül'da lokilde gezdik.");
        txtInput.setRows(10);
        txtInput.setSizeFull();
        TextArea txtOutput = new TextArea("Output for SpellChecker");
        txtOutput.setSizeFull();
        txtOutput.setRows(10);
        Button btn = new Button("SpellChecker");
        Button btn2 = new Button("NGramSpellChecker");

        row1.addComponents(txtInput,txtOutput);
        layout.addComponent(row1);
        layout.addComponents(btn);
        layout.addComponents(btn2);

        //Action
        NLPService nlpService = new NLPService();
        //nlpService.Deascşfşer("ddsa",)
        btn.addClickListener(e -> {
            txtOutput.clear();
            //String[] lines = txtInput.getValue().split(" ");
            // for (String line : lines) {
            Sentence mysentence=new Sentence(txtInput.getValue());
            Sentence ascisentence=nlpService.SpellChecker(mysentence);
            String correctSentence= ascisentence.toString();
            txtOutput.setValue(txtOutput.getValue() + correctSentence);
            // }
        });
        btn2.addClickListener(e -> {
            txtOutput.clear();
            Sentence mysentence=new Sentence(txtInput.getValue());
            Sentence ascisentence=nlpService.NGramSpellChecker(mysentence);
            String correctSentence= sentencetoString(ascisentence);
            txtOutput.setValue(txtOutput.getValue() + correctSentence);
            }
        );

        addComponent(layout);
    }
    private String sentencetoString(Sentence sentence){
        String result="";
        for(int i=0; i<sentence.wordCount(); i++){
            result+=sentence.getWord(i);
            result+=" ";
        }
        return result;
    }
}
