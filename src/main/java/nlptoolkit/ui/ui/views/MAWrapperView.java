package nlptoolkit.ui.ui.views;

import Wrappers.WordAnalysis;

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import nlptoolkit.ui.services.ExternalMorphologicalAnalyzerTypes;
import nlptoolkit.ui.services.NLPService;

public class MAWrapperView extends NLPView {

    final String _InitialSentences = "okudum";
    HorizontalLayout WordsContainer = new HorizontalLayout();       //yan yana dizer.
    Label LblResult;

    @Override
    public String GetScreenName() {
        return "MAWrappers";
    }

    public MAWrapperView() {

        final VerticalLayout layout = new VerticalLayout();
        TextField txtInput = new TextField("Type a word to morphologically analyze:", _InitialSentences);
        txtInput.setSizeFull();
        Button btnAnalyze = new Button("Analyze");
        LblResult = new Label("", ContentMode.PREFORMATTED);

        //MATypes.
        ComboBox cmbMATypes = new ComboBox();
        cmbMATypes.setItems("TRMorph","ITUWeb","Zemberek");     //TODO: Do not respublish ITUWeb due to the licensing restrictions!
        cmbMATypes.setValue("TRMorph");
        cmbMATypes.setEmptySelectionAllowed(false);
        cmbMATypes.setTextInputAllowed(false);

        layout.addComponents(txtInput,cmbMATypes,btnAnalyze);
        layout.addComponent(LblResult);
        //layout.addComponent(WordsContainer);

        NLPService nlpService = new NLPService();
        btnAnalyze.addClickListener(e -> {
            ClearAnalysis();
            String word = txtInput.getValue();
            String maTypeStr = cmbMATypes.getValue().toString();
            ExternalMorphologicalAnalyzerTypes maType = ExternalMorphologicalAnalyzerTypes.valueOf(maTypeStr);
            WordAnalysis wordAnalysis = nlpService.MorphologicallyAnalyzeExternally(word, maType);
            LblResult.setValue(org.apache.commons.lang3.StringUtils.join(wordAnalysis.Results,"\n"));
        });

        //styles.
        addComponent(layout);
        WordsContainer.setSizeUndefined();
    }

    private void ClearAnalysis(){
        //WordsContainer.removeAllComponents();
        LblResult.setValue("");
    }
}
