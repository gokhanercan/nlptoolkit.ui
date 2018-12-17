package nlptoolkit.ui.ui.views;

import Corpus.Sentence;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import nlptoolkit.ui.nlp.DeascifierTypes;
import nlptoolkit.ui.services.NLPService;

import static nlptoolkit.ui.ui.views.Utils.NEWLINE_SEPARATOR;
import static nlptoolkit.ui.ui.views.Utils.bindButtonToFile;

public class AsciifierDeasciifierView extends NLPView {

    private final String INITIAL_INPUTS = "öğretmen\nşaşırmak\nIstanbul\nİstanbul\nitiraz\nüzüm\n";
    private final String DOWNLOAD_FILENAME = "asciifierResults.txt";
    private final String WORD_SEPARATOR = "|";
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
        TextArea txtInput = new TextArea("Please type words/sentences to asciify:", INITIAL_INPUTS);
        txtInput.setRows(10);
        txtInput.setSizeFull();

        Button btnAsciify = new Button("Asciify");
        btnAsciify.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        inputArea.addComponents(txtInput, btnAsciify);

        final VerticalLayout outputArea = new VerticalLayout();
        TextArea txtOutput = new TextArea("Asciified results for words/sentences:");
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
//        TextField txtMappings = new TextField("Copy \"|\" separated Results From Here!");
//        txtMappings.setSizeFull();

        containerLayout.addComponents(textAreas, buttonsLayout);
        containerLayout.setComponentAlignment(buttonsLayout, Alignment.MIDDLE_CENTER);
        containerLayout.setMargin(false);


        //Action
        NLPService nlpService = new NLPService();
        btnAsciify.addClickListener(e -> {
            txtOutput.clear();
            String[] lines = txtInput.getValue().split("\n");
            for (String line : lines) {
                Sentence mysentence = new Sentence(line);
                Sentence ascisentence = nlpService.Asciify(mysentence);
                String correctSentence = ascisentence.toString();
                txtOutput.setValue(txtOutput.getValue() + correctSentence + "\n");
            }
            bindButtonToFile(downloadButton, DOWNLOAD_FILENAME, getMapping(txtInput, txtOutput));
//            txtMappings.setValue(getMapping(txtInput, txtOutput));
        });
        btnDeasciify.addClickListener(e -> {
            txtInput.clear();
            String[] lines = txtOutput.getValue().split("\n");
            DeascifierTypes type = cmbDeasciifier.getSelectedItem().toString().equals("Simple") ? DeascifierTypes.Simple : DeascifierTypes.Ngram;        //support only two implementation
            for (String line : lines) {
                Sentence mysentence = new Sentence(line);
                Sentence outSentence = nlpService.Deasciify(mysentence, type);
                txtInput.setValue(txtInput.getValue() + outSentence + "\n");
            }
//            txtMappings.setValue(getMapping(txtInput, txtOutput));
            bindButtonToFile(downloadButton, DOWNLOAD_FILENAME, getMapping(txtInput, txtOutput));
        });
        btnReset.addClickListener(e -> {
            txtOutput.clear();
//            txtMappings.clear();
            txtInput.setValue(INITIAL_INPUTS);
        });

        addComponent(containerLayout);
    }

    private String getMapping(TextArea txtInput, TextArea txtOutput) {
        String[] inputs = txtInput.getValue().split("\n");
        String[] outputs = txtOutput.getValue().split("\n");
        if (inputs.length == outputs.length) {
            StringBuilder mapping = new StringBuilder();
            for (int i = 0; i < inputs.length; i++) {
                mapping.append(inputs[i]).append(WORD_SEPARATOR).append(outputs[i]).append(NEWLINE_SEPARATOR);
            }

            return mapping.toString();
        }
        return "";
    }
}
