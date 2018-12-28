package nlptoolkit.ui.ui.views;

import Corpus.Sentence;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.HtmlRenderer;
import nlptoolkit.ui.services.NLPService;

import java.util.ArrayList;

import static nlptoolkit.ui.ui.views.Utils.NEWLINE_SEPARATOR;
import static nlptoolkit.ui.ui.views.Utils.bindButtonToFile;


public class MorphologicalAnalyzerDisambiguatorView extends NLPView {
    private String INITIAL_SENTENCES = "Gözlükçü dükkanına gittim .";
    private final String DOWNLOAD_FILENAME = "morphologicalAnalysisResults.txt";
    private final String SEPARATOR = "|";
    private final int CHAR_LIMIT = 1000;

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
        TextArea txtInput = new TextArea("Type one sentence to morphologically analyze up to " +
                CHAR_LIMIT + " characters:", INITIAL_SENTENCES);
        txtInput.setSizeFull();
        txtInput.setResponsive(true);
        txtInput.setRows(5);

        Button btnAnalyze = new Button("Analyze/Disambiguate");
        btnAnalyze.setSizeUndefined();
        console.addComponent(txtInput);
        console.setExpandRatio(txtInput, 100);       //textbox'a expand etme şansı veriyor.

        NLPService nlpService = new NLPService();
        Grid<DisambiguationResult> resultsGrid = new Grid<>("Disambiguation results.");
        resultsGrid.setSizeFull();
        resultsGrid.setHeightByRows(1);

        Button downloadButton = new Button("Download Results");

        layout.addComponents(console, btnAnalyze, resultsGrid, downloadButton);

        btnAnalyze.addClickListener(e -> {
            resultsGrid.removeAllColumns();
            String input = txtInput.getValue().replace("\n", " ");
            input = input.substring(0, Math.min(CHAR_LIMIT, input.length()));
            Sentence s = nlpService.TurkishSplitter(input).get(0);
            ArrayList<DisambiguationResult> results = nlpService.analyzeAndDisambiguate(s);
            resultsGrid.setItems(results);
            resultsGrid.addColumn(DisambiguationResult::getWord).setCaption("Word");
            resultsGrid.addColumn(DisambiguationResult::getAnalysis).setCaption("Analysis");
            resultsGrid.setHeightByRows(results.size());

            for (Grid.Column column : resultsGrid.getColumns()) {
                column.setSortable(false);
            }

            Grid.Column<DisambiguationResult, String> htmlColumn = resultsGrid.addColumn(DisambiguationResult::getIconHtml,
                    new HtmlRenderer());
            htmlColumn.setCaption("Disambiguated Analysis");

            StringBuilder fileContent = new StringBuilder();
            for (DisambiguationResult result : results) {
                fileContent.append(result.getWord()).append(SEPARATOR).append(result.getAnalysis()).append(SEPARATOR);
                if (result.isDisambiguatedAnalysis()) {
                    fileContent.append("+");
                } else {
                    fileContent.append("-");
                }
                fileContent.append(NEWLINE_SEPARATOR);
            }
            bindButtonToFile(downloadButton, DOWNLOAD_FILENAME, fileContent.toString());
        });
        btnAnalyze.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        addComponent(layout);
    }


}
