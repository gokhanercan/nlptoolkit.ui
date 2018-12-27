package nlptoolkit.ui.services;

import Corpus.Sentence;
import Corpus.TurkishSplitter;
import Deasciifier.Deasciifier;
import Deasciifier.NGramDeasciifier;
import Deasciifier.SimpleAsciifier;
import Deasciifier.SimpleDeasciifier;
import Dictionary.TurkishWordComparator;
import Dictionary.TxtDictionary;
import Dictionary.Word;
import MorphologicalAnalysis.FsmMorphologicalAnalyzer;
import MorphologicalAnalysis.FsmParse;
import MorphologicalAnalysis.FsmParseList;
import MorphologicalDisambiguation.MorphologicalDisambiguator;
import Ngram.NGram;
import Service.MAService;
import SpellChecker.NGramSpellChecker;
import SpellChecker.SimpleSpellChecker;
import Syllibification.IrregularWordException;
import Syllibification.SyllableList;
import Wrappers.IExternalMorphologicalAnalyzer;
import Wrappers.WordAnalysis;
import nlptoolkit.ui.application.AppContainer;
import nlptoolkit.ui.nlp.DeascifierTypes;
import nlptoolkit.ui.services.wrappers.MyRootFirstDisambiguation;
import nlptoolkit.ui.ui.views.DisambiguationResult;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;


public class NLPService {

    private AppContainer getContainer() {
        return AppContainer.CreateInstance();
    }

    private FSService _FSService = new FSService();

    private FsmMorphologicalAnalyzer _Analyzer = null;
    private MorphologicalDisambiguator _Disambiguator = null;
    private NGram<Word> nGram;


    private FsmMorphologicalAnalyzer getAnalyzer() {
        if (_Analyzer == null) {     //supports only single instance for now!
            _Analyzer = new FsmMorphologicalAnalyzer(_FSService.GetResourceFilesRootPath() + "turkish_finite_state_machine.xml",
                    new TxtDictionary(_FSService.GetResourceFilesRootPath() + "turkish_dictionary.txt", new TurkishWordComparator()));
        }
        return _Analyzer;
    }

    private MorphologicalDisambiguator getDisambiguator() {
        if (_Disambiguator == null) {
            String rootFolder = _FSService.GetResourceFilesRootPath();
            _Disambiguator = new MyRootFirstDisambiguation(rootFolder);
            _Disambiguator.loadModel();
        }
        return _Disambiguator;
    }

    private NGram getNgram() {
        if (nGram == null) {
            try {
                FileInputStream inFile = new FileInputStream(_FSService.GetResourceFilesRootPath() + "words.1gram");    //TODO: Other ngrams? words2.2gram out of bound hatası alıyor.
                ObjectInputStream inObject = new ObjectInputStream(inFile);
                this.nGram = (NGram) inObject.readObject();
            } catch (FileNotFoundException var4) {
                var4.printStackTrace();
            } catch (ClassNotFoundException var5) {
                var5.printStackTrace();
            } catch (IOException var6) {
                var6.printStackTrace();
            }
        }
        return nGram;
    }

    public String Syllabify(String sentenceStr) throws IrregularWordException {

        //TurkishSyllabification syllabification = new TurkishSyllabification();
        Sentence sentence = new Sentence(sentenceStr);
        Sentence newSentence = new Sentence();
        for (Word word : sentence.getWords()) {
            SyllableList syllableList = new SyllableList(word.getName());
            //String out = syllabification.ExtractSegmentsInHypenSeperatedOrDefault(word.getName());
            String out = String.join("-", syllableList.getSyllables());
            newSentence.addWord(new Word(out));
        }
        return newSentence.toString();
    }

    public Sentence Asciify(Sentence sentence) {
        SimpleAsciifier simpleAsciifier = new SimpleAsciifier();
        return simpleAsciifier.asciify(sentence);
    }

    public Sentence Deasciify(Sentence sentence, DeascifierTypes type) {
        Deasciifier deasciifier = CreateDeasciifier(type);
        return deasciifier.deasciify(sentence);
    }

    private Deasciifier CreateDeasciifier(DeascifierTypes type) {
        switch (type) {
            case Ngram:
                return new NGramDeasciifier(getAnalyzer(), getNgram());
            case Simple:
                return new SimpleDeasciifier(getAnalyzer());
            default:
                throw new RuntimeException("Invalid deascifier!");
        }
    }

    public Sentence SpellChecker(Sentence sentence) {
        FsmMorphologicalAnalyzer analyzer = getAnalyzer();
        SimpleSpellChecker SpellChecker = new SimpleSpellChecker(analyzer);
        Sentence newSentence = new Sentence();

        newSentence = SpellChecker.spellCheck(sentence);
        return newSentence;
    }

    public Sentence NGramSpellChecker(Sentence sentence) {
        FsmMorphologicalAnalyzer analyzer = getAnalyzer();
        NGram nGram = getNgram();
        NGramSpellChecker nGramSpellChecker = new NGramSpellChecker(analyzer, nGram);
        Sentence newSentence = new Sentence();

        newSentence = nGramSpellChecker.spellCheck(sentence);
        return newSentence;
    }

    public ArrayList<Sentence> TurkishSplitter(String line) {
        TurkishSplitter TRsplitter = new TurkishSplitter();
        ArrayList<Sentence> newSentence = TRsplitter.split(line);
        return newSentence;
    }

    public FsmParseList[] MorphologicalAnalysis(Sentence sentence) {
        return getAnalyzer().morphologicalAnalysis(sentence, false);
    }

    public ArrayList<FsmParse> Disambiguate(Sentence sentence) {
        MorphologicalDisambiguator disambiguator = getDisambiguator();
        FsmMorphologicalAnalyzer analyzer = getAnalyzer();
        FsmParseList[] words = analyzer.morphologicalAnalysis(sentence, false);
        return disambiguator.disambiguate(words);
    }

    public WordAnalysis MorphologicallyAnalyzeExternally(String word, ExternalMorphologicalAnalyzerTypes maType) {      //TODO: Manage folders, dependencies etc here.. pass analyzer enum.
        MAService maService = new MAService();
        IExternalMorphologicalAnalyzer analyzer = getContainer().ResolveExternalMorphologicalAnalyzer(maType);
        return maService.AnalyzeWord(word, analyzer);
    }

    public ArrayList<DisambiguationResult> analyzeAndDisambiguate(Sentence sentence) {
        MorphologicalDisambiguator morphologicalDisambiguator = getDisambiguator();
        HashSet<DisambiguationResult> results = new HashSet<>();
        FsmParseList[] parseList = MorphologicalAnalysis(sentence);
        ArrayList<FsmParse> disambiguatedParses = morphologicalDisambiguator.disambiguate(parseList);
        // Add correct parses first and then add all
        for (FsmParse disambiguatedParse : disambiguatedParses) {
            results.add(new DisambiguationResult(disambiguatedParse.getSurfaceForm(), disambiguatedParse.toString(), true));
        }

        for (FsmParseList analysis : parseList) {
            if (analysis.size() == 0) {
                results.add(new DisambiguationResult("ERROR", "ERROR", false));
            } else {
                String surface = analysis.getFsmParse(0).getSurfaceForm();
                for (int j = 0; j < analysis.size(); j++) {
                    String morphResult = analysis.getFsmParse(j).toString();
                    results.add(new DisambiguationResult(surface, morphResult, false));
                }
            }

        }
        ArrayList<DisambiguationResult> lst = new ArrayList<>(results);
        Collections.sort(lst);
        return lst;
    }


}
