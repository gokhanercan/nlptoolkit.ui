package nlptoolkit.ui.ui.views;

import Corpus.Sentence;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import nlptoolkit.ui.services.NLPService;

import java.util.ArrayList;

public class SentenceSplitterView extends NLPView {

    @Override
    public String GetScreenName() {
        return "Tokenizer/Sentence Splitter";
    }

    public SentenceSplitterView() {

        //Layout
        final VerticalLayout layout = new VerticalLayout();
        final HorizontalLayout row1 = new HorizontalLayout();
        row1.setSizeFull();
        TextArea txtInput = new TextArea("Enter any piece of text to tokenize/split to sentences",
                Configs.INITIAL_SENTENCE_SPLITTER_TEXT);
        txtInput.setRows(15);
        txtInput.setSizeFull();
        TextArea txtOutput = new TextArea("Space and new line separated Tokens/Sentences up to " +
                Configs.SENTENCE_SPLITTER_CHAR_LIMIT + " characters:");
        txtOutput.setSizeFull();
        txtOutput.setRows(15);
        txtOutput.setReadOnly(true);
        Button btn = new Button("Tokenize/Split");
        btn.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        row1.addComponents(txtInput, txtOutput);
        layout.addComponent(row1);
        layout.addComponents(btn);

        NLPService nlpService = new NLPService();
        btn.addClickListener(e -> {
            txtOutput.clear();
            String inputText = txtInput.getValue().trim();
            inputText = inputText.substring(0, Math.min(Configs.SENTENCE_SPLITTER_CHAR_LIMIT, inputText.length()));
            ArrayList<Sentence> splittedSentences = nlpService.TurkishSplitter(inputText);
            for (Sentence ss : splittedSentences) {
                txtOutput.setValue(txtOutput.getValue() + ss + "\n\n");
            }
            //lblMessage.setValue("Input text splitted into " + splittedSentences.size() + " sentences.");
        });

        addComponent(layout);
    }
}
