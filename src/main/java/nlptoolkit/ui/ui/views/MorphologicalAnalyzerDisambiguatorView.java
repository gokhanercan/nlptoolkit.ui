package nlptoolkit.ui.ui.views;

import Corpus.Sentence;
import MorphologicalAnalysis.FsmParseList;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import nlptoolkit.ui.services.NLPService;

import java.util.ArrayList;

import static nlptoolkit.ui.ui.views.Utils.NEWLINE_SEPARATOR;
import static nlptoolkit.ui.ui.views.Utils.bindButtonToFile;

public class MorphologicalAnalyzerDisambiguatorView extends NLPView {
    private String INITIAL_SENTENCES = "Gözlükçü dükkanına gittim .";
    private final String DOWNLOAD_FILENAME = "morphologicalAnalysisResults.txt";
    private final String SEPARATOR = "|";
    @Override
    public String GetScreenName() {
        return "Morphological Analyzer/Disambiguator";
    }

    public MorphologicalAnalyzerDisambiguatorView() {

        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(false);
        HorizontalLayout console = new HorizontalLayout();
        console.setSizeFull();
        console.setResponsive(true);
        TextField txtInput = new TextField("Type sentence to morphologically analyze:", INITIAL_SENTENCES);

        txtInput.setSizeFull();
        txtInput.setResponsive(true);

        Button btnAnalyze = new Button("Analyze");
        btnAnalyze.setSizeUndefined();
        console.addComponent(txtInput);
        console.setExpandRatio(txtInput, 100);       //textbox'a expand etme şansı veriyor.

        NLPService nlpService = new NLPService();
        Grid<MorphologicalResult> resultsGrid = new Grid<>("Results");
        resultsGrid.setSizeFull();
        resultsGrid.setHeightByRows(1);
        Button downloadButton = new Button("Download Results");
        layout.addComponents(console, btnAnalyze, resultsGrid, downloadButton);

        btnAnalyze.addClickListener(e -> {
            resultsGrid.removeAllColumns();
            ArrayList<MorphologicalResult> results = new ArrayList<>();
            StringBuilder fileContent = new StringBuilder();
            Sentence s = new Sentence(txtInput.getValue());
            ArrayList<FsmParseList> parseList = nlpService.MorphologicalAnalysis(s);
            for (FsmParseList analysis : parseList) {
                if (analysis.size() == 0) {
                    results.add(new MorphologicalResult("ERROR", "ERROR"));
                    fileContent.append("ERROR").append(SEPARATOR).append(NEWLINE_SEPARATOR);
                } else {
                    String surface = analysis.getFsmParse(0).getSurfaceForm();
                    for (int j = 0; j < analysis.size(); j++) {
                        String morphResult = analysis.getFsmParse(j).toString();
                        results.add(new MorphologicalResult(surface, morphResult));
                        fileContent.append(surface).append(SEPARATOR).append(morphResult).append(NEWLINE_SEPARATOR);
                    }
                }

            }

            resultsGrid.setItems(results);
            resultsGrid.addColumn(MorphologicalResult::getWord).setCaption("Word");
            resultsGrid.addColumn(MorphologicalResult::getResult).setCaption("Analysis");
            resultsGrid.setHeightByRows(results.size());
            bindButtonToFile(downloadButton, DOWNLOAD_FILENAME, fileContent.toString());
        });
        btnAnalyze.setClickShortcut(ShortcutAction.KeyCode.ENTER);

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
    }

    private class MorphologicalResult {
        String word, result;

        MorphologicalResult(String word, String result) {
            this.word = word;
            this.result = result;
        }

        String getWord() {
            return this.word;
        }

        String getResult() {
            return this.result;
        }

    }
}
