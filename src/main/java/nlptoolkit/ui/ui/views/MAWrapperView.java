package nlptoolkit.ui.ui.views;

import Dictionary.Word;
import Wrappers.IExternalMorphologicalAnalyzer;
import Wrappers.WordAnalysis;

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import nlptoolkit.ui.services.ExternalMorphologicalAnalyzerTypes;
import nlptoolkit.ui.services.NLPService;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingDeque;

public class MAWrapperView extends NLPView {

    final String _InitialSentences = "okudum";
    //HorizontalLayout WordsContainer = new HorizontalLayout();       //yan yana dizer.
    Label LblResult;
    private VerticalLayout Layout;

    @Override
    public String GetScreenName() {
        return "MAWrappers";
    }

    public MAWrapperView() {

        Layout = new VerticalLayout();
        addComponent(Layout);
        final HorizontalLayout console = new HorizontalLayout();

        console.setSizeFull();
        console.setMargin(false);
        Layout.addComponent(console);
        TextField txtInput = new TextField(null, _InitialSentences);
        txtInput.setSizeFull();
        txtInput.setResponsive(true);
        Button btnAnalyze = new Button("Analyze");
        LblResult = new Label("", ContentMode.PREFORMATTED);

        //MATypes.
        ComboBox cmbMATypes = new ComboBox();
        cmbMATypes.setItems("-All-","Dilbaz","TRMorph","ITUWeb","Zemberek");     //TODO: Do not respublish ITUWeb due to the licensing restrictions!
        cmbMATypes.setValue("-All-");
        cmbMATypes.setEmptySelectionAllowed(false);
        cmbMATypes.setTextInputAllowed(false);

        console.addComponents(txtInput,cmbMATypes,btnAnalyze);
        console.setExpandRatio(txtInput,100);       //textbox'a en fazla genişleme yetkisini veriyor.

        //Grid
        Grid<IExternalMorphologicalAnalyzer> grid = new Grid<>();

        NLPService nlpService = new NLPService();
        btnAnalyze.addClickListener(e -> {
            ClearAnalysis();
            String word = txtInput.getValue();
            String maTypeStr = cmbMATypes.getValue().toString();
            if (maTypeStr.equals("-All-")){
                AddAnalysisGrid(nlpService,word);
            }
            else{
                ExternalMorphologicalAnalyzerTypes maType = ExternalMorphologicalAnalyzerTypes.valueOf(maTypeStr);
                WordAnalysis wordAnalysis = nlpService.MorphologicallyAnalyzeExternally(word, maType);
                LblResult.setValue(org.apache.commons.lang3.StringUtils.join(wordAnalysis.Results,"\n"));
                Layout.addComponent(LblResult);
            }

        });
    }

    private GridLayout Grid = null;
    private void AddAnalysisGrid(NLPService nlpService, String word){
        boolean isFirstRender = Grid == null;
        if(!isFirstRender) removeComponent(Grid);

        GridLayout grid = new GridLayout(4,2);
        grid.setSpacing(false);
        grid.setMargin(false);
        grid.addStyleName("outlined");
        grid.setSizeFull();
        grid.setResponsive(true);

        //headers
        int maIndex = 0;
        for (Object obj : ExternalMorphologicalAnalyzerTypes.values()) {
            String val = obj.toString();
            ExternalMorphologicalAnalyzerTypes maType = ExternalMorphologicalAnalyzerTypes.valueOf(val);
            Label header = new Label(val);
            header.addStyleName("h3");
            grid.addComponent(header,maIndex,0);
            maIndex++;
        }

        //items
        maIndex = 0;
        for (Object obj : ExternalMorphologicalAnalyzerTypes.values()) {
            String val = obj.toString();
            ExternalMorphologicalAnalyzerTypes maType = ExternalMorphologicalAnalyzerTypes.valueOf(val);
            WordAnalysis analysis = nlpService.MorphologicallyAnalyzeExternally(word,maType);
            String text = org.apache.commons.lang3.StringUtils.join(analysis.Results,"</br>");
            Label lblText = new Label(text);
            lblText.setContentMode(ContentMode.HTML);
            grid.addComponent(lblText,maIndex,1);
            maIndex++;
        }
        addComponent(grid);
        Grid = grid;
    }

    private void ClearAnalysis(){
        Layout.removeComponent(LblResult);
        LblResult.setValue("");
        if(Grid != null) {
            removeComponent(Grid);
            Grid = null;
        }
    }
}
