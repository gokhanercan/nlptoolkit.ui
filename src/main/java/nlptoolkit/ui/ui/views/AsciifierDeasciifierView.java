package nlptoolkit.ui.ui.views;

import Corpus.Sentence;
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
        final VerticalLayout layout = new VerticalLayout();
        final HorizontalLayout row1 = new HorizontalLayout();
        row1.setSizeFull();
        TextArea txtInput = new TextArea("Please type words/sentences to asciify:",InitialInputs);
        txtInput.setRows(20);
        txtInput.setSizeFull();
        TextArea txtOutput = new TextArea("Asciified results for words/sentences:");
        txtOutput.setSizeFull();
        txtOutput.setRows(20);

        Button btnAsciify = new Button("Asciify");
        Button btnDeasciify = new Button("Deasciify");
        Button btnReset = new Button("Reset");

        row1.addComponents(txtInput,txtOutput);
        layout.addComponent(row1);

        ComboBox cmbDeasciifier = new ComboBox();
        cmbDeasciifier.setItems("Simple","NGram");
        cmbDeasciifier.setValue("Simple");
        cmbDeasciifier.setEmptySelectionAllowed(false);
        cmbDeasciifier.setTextInputAllowed(false);

        HorizontalLayout rowDeasciifier = new HorizontalLayout();
        rowDeasciifier.addComponents(new Label("Deasciifier:"),cmbDeasciifier,btnDeasciify);

        layout.addComponents(btnAsciify, rowDeasciifier, btnReset);
        layout.addComponents(btnReset);

        //Action
        NLPService nlpService = new NLPService();
        btnAsciify.addClickListener(e -> {
            txtOutput.clear();
            String[] lines = txtInput.getValue().split("\n");
            for (String line : lines) {
                Sentence mysentence=new Sentence(line);
                Sentence ascisentence=nlpService.Asciify(mysentence);
                String correctSentence= ascisentence.toString();
                txtOutput.setValue(txtOutput.getValue() + correctSentence + "\n");
            }
        });
        btnDeasciify.addClickListener(e -> {
            txtInput.clear();
            String[] lines = txtOutput.getValue().split("\n");
            DeascifierTypes type = cmbDeasciifier.getSelectedItem().toString() == "Simple" ? DeascifierTypes.Simple : DeascifierTypes.Ngram;        //support only two implementation
            for (String line : lines) {
                Sentence mysentence = new Sentence(line);
                Sentence outSentence = nlpService.Deasciify(mysentence,type);
                txtInput.setValue(txtInput.getValue() + outSentence + "\n");
            }
        });
        btnReset.addClickListener(e -> {
            txtOutput.clear();
            txtInput.setValue(InitialInputs);
        });

        addComponent(layout);
    }
}
