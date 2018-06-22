package nlptoolkit.ui.services.dto;

import MorphologicalAnalysis.FsmParse;
import MorphologicalAnalysis.FsmParseList;

import java.util.ArrayList;

public class DisambiguatedAnalysisDTO {

    public FsmParseList[] Analysis;
    public ArrayList<FsmParse> Disambiguated;

    public DisambiguatedAnalysisDTO(FsmParseList[] analysis, ArrayList<FsmParse> disambiguated) {
        Analysis = analysis;
        Disambiguated = disambiguated;
    }
}
