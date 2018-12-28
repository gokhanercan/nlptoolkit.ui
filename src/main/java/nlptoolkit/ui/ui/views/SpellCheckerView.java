package nlptoolkit.ui.ui.views;

import Corpus.Sentence;
import Dictionary.Word;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import nlptoolkit.ui.services.NLPService;

import java.util.ArrayList;

import static nlptoolkit.ui.ui.views.Configs.*;
import static nlptoolkit.ui.ui.views.Utils.bindButtonToFile;

public class SpellCheckerView extends NLPView {

    private String fileContent = "";

    @Override
    public String GetScreenName() {
        return "Spell Checker";
    }

    public SpellCheckerView() {

        final VerticalLayout wrapperLayout = new VerticalLayout();
        final HorizontalLayout row1 = new HorizontalLayout();
        row1.setSizeFull();
        TextArea txtInput = new TextArea("Write Sentence For SpellChecker:", Configs.SPELL_CHECKER_INITIAL_TEXT);
        txtInput.setRows(10);
        txtInput.setSizeFull();
        TextArea txtOutput = new TextArea("Output for SpellChecker");
        txtOutput.setSizeFull();
        txtOutput.setRows(10);
        row1.addComponents(txtInput, txtOutput);

        Button btn1 = new Button("SpellChecker");
        btn1.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        Button btn2 = new Button("NGramSpellChecker");
        Button downloadButton = new Button("Download Results");
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addComponents(btn1, btn2, downloadButton);

        wrapperLayout.addComponents(row1, buttonLayout);
        btn1.addClickListener(e -> {
            spellCheck(txtInput, txtOutput, false);
            bindButtonToFile(downloadButton, Configs.SPELL_CHECKER_DOWNLOAD_FILENAME, fileContent);
        });
        btn2.addClickListener(e -> {
            spellCheck(txtInput, txtOutput, true);
            bindButtonToFile(downloadButton, Configs.SPELL_CHECKER_DOWNLOAD_FILENAME, fileContent);
                }
        );

        addComponent(wrapperLayout);
    }

    private void spellCheck(TextArea txtInput, TextArea txtOutput, boolean useNgram) {
        NLPService nlpService = new NLPService();
        txtOutput.clear();

        String input = txtInput.getValue().replace("\n", " ").trim();
        input = input.substring(0, Math.min(Configs.SPELL_CHECKER_CHAR_LIMIT, input.length()));
        ArrayList<Sentence> inputSentences = nlpService.TurkishSplitter(input);
        StringBuilder downloadString = new StringBuilder();
        StringBuilder outputToShow = new StringBuilder();
        for (Sentence inputSentence : inputSentences) {
            for (Word inputWord : inputSentence.getWords()) {
                Sentence corrected;
                if (useNgram) {
                    corrected = nlpService.NGramSpellChecker(new Sentence(inputWord.toString()));
                } else {
                    corrected = nlpService.SpellChecker(new Sentence(inputWord.toString()));
                }
                String outputWord = corrected.toString();
                outputToShow.append(outputWord).append(" ");
                String[] alignments = getAlignment(inputWord.toString(), outputWord);
                downloadString.append(alignments[0]).append(MAPPING_SEPARATOR).append(alignments[1]).append(WORD_SEPARATOR);
            }
            outputToShow.append(NEWLINE_SEPARATOR);
            downloadString.append(NEWLINE_SEPARATOR);
        }
        txtOutput.setValue(outputToShow.toString());
        fileContent = downloadString.toString();
    }

    private String[] getAlignment(String word1, String word2) {
        int l1, l2;
        l1 = word1.length() + 1;
        l2 = word2.length() + 1;
        int[][] distanceTable = new int[l1][l2];

        int gapX, gapY, sub;
        for (int i = 0; i < l1; i++) {
            distanceTable[i][0] = i;
        }

        for (int j = 0; j < l2; j++) {
            distanceTable[0][j] = j;
        }

        for (int i = 1; i < l1; i++) {
            for (int j = 1; j < l2; j++) {
                //Cost if no gap inserted
                if (Character.toLowerCase(word1.charAt(i - 1)) == Character.toLowerCase(word2.charAt(j - 1))) {
                    sub = distanceTable[i - 1][j - 1];
                } else {
                    sub = distanceTable[i - 1][j - 1] + 1;
                }
                gapX = distanceTable[i][j - 1] + 1;    //Cost if insert gap in x
                gapY = distanceTable[i - 1][j] + 1;    //Cost if insert gap in y
                distanceTable[i][j] = Math.min(Math.min(gapX, gapY), sub);
            }
        }

        StringBuilder sbX = new StringBuilder(word1), sbY = new StringBuilder(word2);
        int i = l1 - 1, j = l2 - 1;

        //Iterate until back at origin [0,0]
        while (i != 0 && j != 0) {
            if (distanceTable[i][j] == distanceTable[i][j - 1] + 1) {            //Move left 1
                sbX.insert(i, '-');
                j--;
            } else if (distanceTable[i][j] == distanceTable[i - 1][j] + 1) {    //Move up 1
                sbY.insert(j, '-');
                i--;
            } else {    //Move diagnally
                i--;
                j--;
            }
            if (i == 0 && j > 0) {    //At the X boundary, move down 1
                sbX.insert(0, '-');
                i++;
            } else if (i > 0 && j == 0) {  //At the Y boundary, move right 1
                sbY.insert(0, '-');
                j++;
            }
        }

        return new String[]{sbX.toString(), sbY.toString()};
    }
}
