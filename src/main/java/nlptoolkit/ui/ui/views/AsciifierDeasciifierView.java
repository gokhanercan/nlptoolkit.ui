package nlptoolkit.ui.ui.views;

import Corpus.Sentence;
import Dictionary.Word;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import nlptoolkit.ui.nlp.DeascifierTypes;
import nlptoolkit.ui.services.NLPService;

import java.util.ArrayList;

import static nlptoolkit.ui.ui.views.Configs.*;
import static nlptoolkit.ui.ui.views.Utils.bindButtonToFile;

public class AsciifierDeasciifierView extends NLPView {


    @Override
    public String GetScreenName() {
        return "Asciifier / Deasciifier";
    }

    public AsciifierDeasciifierView() {

        //Layout
        final VerticalLayout containerLayout = new VerticalLayout();
        final HorizontalLayout textAreas = new HorizontalLayout();
        final VerticalLayout inputArea = new VerticalLayout();
        textAreas.setSizeFull();
        TextArea txtInput = new TextArea("Please type words/sentences to asciify up to " +
                Configs.ASCIIFIER_CHAR_LIMIT + " characters:", Configs.ASCIIFIER_INITIAL_TEXT);
        txtInput.setRows(10);
        txtInput.setSizeFull();

        Button btnAsciify = new Button("Asciify");
        btnAsciify.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        inputArea.addComponents(txtInput, btnAsciify);

        final VerticalLayout outputArea = new VerticalLayout();
        TextArea txtOutput = new TextArea("Asciified results/inputs for words/sentences up to " +
                Configs.ASCIIFIER_CHAR_LIMIT + " chracters:");
        txtOutput.setSizeFull();
        txtOutput.setRows(10);

        Button btnDeasciify = new Button("Deasciify");
        ComboBox cmbDeasciifier = new ComboBox();
        cmbDeasciifier.setItems("Simple", "NGram");
        cmbDeasciifier.setValue("Simple");
        cmbDeasciifier.setEmptySelectionAllowed(false);
        cmbDeasciifier.setTextInputAllowed(false);

        final HorizontalLayout btnOutputArea = new HorizontalLayout();
        btnOutputArea.addComponents(cmbDeasciifier, btnDeasciify);
        outputArea.addComponents(txtOutput, btnOutputArea);

        textAreas.addComponents(inputArea, outputArea);
        textAreas.setMargin(false);

        final HorizontalLayout buttonsLayout = new HorizontalLayout();
        Button btnReset = new Button("Reset");
        Button downloadButton = new Button("Download Mappings");
        buttonsLayout.addComponents(btnReset, downloadButton);

        containerLayout.addComponents(textAreas, buttonsLayout);
        containerLayout.setComponentAlignment(buttonsLayout, Alignment.MIDDLE_CENTER);
        containerLayout.setMargin(false);

        //Action
        NLPService nlpService = new NLPService();
        btnAsciify.addClickListener(e -> {
            txtOutput.clear();
            String input = txtInput.getValue().replace("\n", " ").trim();
            input = input.substring(0, Math.min(Configs.ASCIIFIER_CHAR_LIMIT, input.length()));
            ArrayList<Sentence> inputSentences = nlpService.TurkishSplitter(input);
            StringBuilder downloadString = new StringBuilder();
            StringBuilder outputToShow = new StringBuilder();
            for (Sentence inputSentence : inputSentences) {
                for (Word inputWord : inputSentence.getWords()) {
                    String outputWord = nlpService.Asciify(new Sentence(inputWord.toString())).toString();
                    outputToShow.append(outputWord).append(" ");
                    downloadString.append(inputWord.toString()).append(MAPPING_SEPARATOR).append(outputWord).append(WORD_SEPARATOR);
                }
                outputToShow.append(NEWLINE_SEPARATOR);
                downloadString.append(NEWLINE_SEPARATOR);
            }
            txtOutput.setValue(outputToShow.toString());
            bindButtonToFile(downloadButton, Configs.ASCIIFIER_DOWNLOAD_FILENAME, downloadString.toString());
        });
        btnDeasciify.addClickListener(e -> {
            txtInput.clear();
            DeascifierTypes type;        //support only two implementation
            if (cmbDeasciifier.getSelectedItem().toString().equals("Simple")) {
                type = DeascifierTypes.Simple;
            } else {
                type = DeascifierTypes.Ngram;
            }
            String input = txtOutput.getValue().replace("\n", " ").trim();
            input = input.substring(0, Math.min(Configs.ASCIIFIER_CHAR_LIMIT, input.length()));
            ArrayList<Sentence> inputSentences = nlpService.TurkishSplitter(input);
            StringBuilder downloadString = new StringBuilder();
            StringBuilder outputToShow = new StringBuilder();
            for (Sentence inputSentence : inputSentences) {
                for (Word inputWord : inputSentence.getWords()) {
                    String outputWord = nlpService.Deasciify(new Sentence(inputWord.toString()), type).toString();
                    outputToShow.append(outputWord).append(" ");
                    downloadString.append(inputWord.toString()).append(MAPPING_SEPARATOR).append(outputWord).append(WORD_SEPARATOR);
                }
                outputToShow.append(NEWLINE_SEPARATOR);
                downloadString.append(NEWLINE_SEPARATOR);
            }
            txtInput.setValue(outputToShow.toString());
            bindButtonToFile(downloadButton, Configs.ASCIIFIER_DOWNLOAD_FILENAME, downloadString.toString());
        });
        btnReset.addClickListener(e -> {
            txtOutput.clear();
//            txtMappings.clear();
            txtInput.setValue(Configs.ASCIIFIER_INITIAL_TEXT);
        });

        addComponent(containerLayout);
    }
}
