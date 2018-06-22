package nlptoolkit.ui.ui.views;

import Corpus.Sentence;
import MorphologicalAnalysis.FsmParse;
import MorphologicalAnalysis.FsmParseList;
import com.vaadin.sass.internal.parser.ParseException;
import com.vaadin.ui.*;
import nlptoolkit.ui.services.NLPService;
import nlptoolkit.ui.services.dto.DisambiguatedAnalysisDTO;

import java.util.ArrayList;

public class MorphologicalAnalyzerDisambiguatorView extends NLPView {

    final String _InitialSentences = "Gözlükçü dükkanına gittim .";
    HorizontalLayout WordsContainer = new HorizontalLayout();       //yan yana dizer.

    @Override
    public String GetScreenName() {
        return "Morphological Analyzer/Disambiguator";
    }

    public MorphologicalAnalyzerDisambiguatorView() {

        final VerticalLayout layout = new VerticalLayout();
        TextField txtInput = new TextField("Type sentence to morphologically analyze:", _InitialSentences);
        txtInput.setSizeFull();
        Button btnAnalyze = new Button("Analyze");
        Button btnDisambiguate = new Button("Analyze & Disambiguate");

        layout.addComponents(txtInput,btnAnalyze);
        //layout.addComponent(btnDisambiguate);
        layout.addComponent(WordsContainer);

        NLPService nlpService = new NLPService();
        btnAnalyze.addClickListener(e -> {
            ClearAnalysis();
            Sentence s = new Sentence(txtInput.getValue());
            ArrayList<FsmParseList> words = nlpService.MorphologicalAnalysis(s);
            for (FsmParseList word : words) {
                DrawWordParses(word,null);
            }
        });

        //Incomplete!
//        btnDisambiguate.addClickListener(e -> {
//            ClearAnalysis();
//            Sentence s = new Sentence(txtInput.getValue());
//            DisambiguatedAnalysisDTO dto = nlpService.DisambiguatedAnalysis(s);
//            int wIndex = 0;
//            for (FsmParseList word : dto.Analysis) {
//                FsmParse correct = dto.Disambiguated.get(wIndex);
//                DrawWordParses(word,correct);
//                wIndex++;
//            }
//        });

        //styles.
        addComponent(layout);
        WordsContainer.setSizeUndefined();
    }

    private void ClearAnalysis(){
        WordsContainer.removeAllComponents();
    }

    private void DrawWordParses(FsmParseList parseList, FsmParse correctParse){
        VerticalLayout wordColumn = new VerticalLayout();
        String surface = parseList.getFsmParse(0).getSurfaceForm();
        Label lblTitle = new Label(surface);
        lblTitle.setId("wordSurface");
        wordColumn.addComponent(lblTitle);
        for (int i = 0; i <parseList.size(); i++) {
            FsmParse parse = parseList.getFsmParse(i);
            Label lblWord = new Label(parse.toString());
            wordColumn.addComponent(lblWord);
            WordsContainer.addComponent(wordColumn);

            //Flag correct parse
            if (correctParse != null){
                if (correctParse == parse) lblWord.addStyleName("correctParse");
            }
        }
    }
}
