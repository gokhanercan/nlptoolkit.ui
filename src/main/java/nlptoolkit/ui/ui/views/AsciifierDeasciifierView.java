package nlptoolkit.ui.ui.views;

import Corpus.Sentence;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import nlptoolkit.ui.nlp.DeascifierTypes;
import nlptoolkit.ui.services.NLPService;

public class AsciifierDeasciifierView extends NLPView {

    final String InitialInputs = "öğretmen\nşaşırmak\nIstanbul\nİstanbul\nitiraz\nüzüm\n";

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
        TextArea txtInput = new TextArea("Please type words/sentences to asciify:", InitialInputs);
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

        Button btnReset = new Button("Reset");

        TextField txtMappings = new TextField("Copy \"|\" separated Results From Here!");
        txtMappings.setSizeFull();

        containerLayout.addComponents(textAreas, btnReset, txtMappings);
        containerLayout.setComponentAlignment(btnReset, Alignment.MIDDLE_CENTER);
        containerLayout.setMargin(false);

//        HorizontalLayout rowDeasciifier = new HorizontalLayout();
//        rowDeasciifier.addComponents(new Label("Deasciifier:"),cmbDeasciifier,btnDeasciify);
//
//        containerLayout.addComponents(btnAsciify, rowDeasciifier, btnReset);
//        containerLayout.addComponents(btnReset);

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
            txtMappings.setValue(getMapping(txtInput, txtOutput));
        });
        btnDeasciify.addClickListener(e -> {
            txtInput.clear();
            String[] lines = txtOutput.getValue().split("\n");
            DeascifierTypes type = cmbDeasciifier.getSelectedItem().toString() == "Simple" ? DeascifierTypes.Simple : DeascifierTypes.Ngram;        //support only two implementation
            for (String line : lines) {
                Sentence mysentence = new Sentence(line);
                Sentence outSentence = nlpService.Deasciify(mysentence, type);
                txtInput.setValue(txtInput.getValue() + outSentence + "\n");
            }
            txtMappings.setValue(getMapping(txtInput, txtOutput));
        });
        btnReset.addClickListener(e -> {
            txtOutput.clear();
            txtMappings.clear();
            txtInput.setValue(InitialInputs);
        });

        addComponent(containerLayout);
    }

    public String getMapping(TextArea txtInput, TextArea txtOutput) {
        String[] inputs = txtInput.getValue().split("\n");
        String[] outputs = txtOutput.getValue().split("\n");
        if (inputs.length == outputs.length) {
            String mapping = "";
            for (int i = 0; i < inputs.length; i++) {
                mapping += inputs[i] + " <-> " + outputs[i] + "| ";
            }

            return mapping.substring(0, mapping.length() - 2);
        }
        return "";
    }
}
