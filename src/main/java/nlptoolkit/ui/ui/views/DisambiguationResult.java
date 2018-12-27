package nlptoolkit.ui.ui.views;

import com.vaadin.icons.VaadinIcons;

public class DisambiguationResult implements Comparable {
    private static int ID_COUNTER = 0;
    private String word, analysis;
    private boolean isDisambiguatedAnalysis;
    private int ID;

    public DisambiguationResult(String word, String analysis, boolean isTrueSense) {
        this.word = word.trim();
        this.analysis = analysis.trim();
        this.isDisambiguatedAnalysis = isTrueSense;
        this.ID = ID_COUNTER;
        ID_COUNTER++;
    }

    public String getWord() {
        return this.word;
    }

    String getAnalysis() {
        return this.analysis;
    }

    boolean isDisambiguatedAnalysis() {
        return this.isDisambiguatedAnalysis;
    }

    String getIconHtml() {
        if (this.isDisambiguatedAnalysis) {
            return VaadinIcons.CHECK.getHtml();
        }
        return VaadinIcons.CLOSE_SMALL.getHtml();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof DisambiguationResult) {
            return word.equals(((DisambiguationResult) other).getWord()) &&
                    analysis.equals(((DisambiguationResult) other).getAnalysis());
        }
        return false;
    }

    @Override
    public int hashCode() {
        // Defined for set operations.
        return 1;
    }

    @Override
    public int compareTo(Object other) {
        if (other instanceof DisambiguationResult) {
            return this.ID - ((DisambiguationResult) other).ID;
        }
        return 0;
    }
}
