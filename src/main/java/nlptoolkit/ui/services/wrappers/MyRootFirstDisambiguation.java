package nlptoolkit.ui.services.wrappers;

import AnnotatedSentence.AnnotatedWord;
import Corpus.Sentence;

import Dictionary.Word;
import MorphologicalAnalysis.FsmParse;
import MorphologicalAnalysis.FsmParseList;
import MorphologicalDisambiguation.DisambiguationCorpus;
import MorphologicalDisambiguation.RootFirstDisambiguation;
import Ngram.InterpolatedSmoothing;
import Ngram.LaplaceSmoothing;
import Ngram.NGram;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

//RootFirstDisambiguation ve NaiveDisambiguation filesystem path'ine direk bağlı olduklarına yeni tipler ile onları ezmeden resource root path'ini veremedim.
public class MyRootFirstDisambiguation extends MyNaiveDisambiguation {

    String RootGramsFolder;

    public MyRootFirstDisambiguation(String rootGramsFolder) {
        super(rootGramsFolder);
        RootGramsFolder = rootGramsFolder;
    }

    protected NGram<Word> wordBiGramModel;
    protected NGram<Word> igBiGramModel;

    public void train(DisambiguationCorpus corpus) {
        Word[] words = new Word[2];
        Word[] igs = new Word[2];
        this.wordUniGramModel = new NGram(1);
        this.wordBiGramModel = new NGram(2);
        this.igUniGramModel = new NGram(1);
        this.igBiGramModel = new NGram(2);

        for(int i = 0; i < corpus.sentenceCount(); ++i) {
            Sentence sentence = corpus.getSentence(i);

            for(int j = 0; j < sentence.wordCount(); ++j) {
                AnnotatedWord word = (AnnotatedWord)sentence.getWord(j);
                words[0] = word.getParse().getWordWithPos();
                this.wordUniGramModel.addNGram(words);
                igs[0] = new Word(word.getParse().getTransitionList());
                this.igUniGramModel.addNGram(igs);
                if(j + 1 < sentence.wordCount()) {
                    words[1] = ((AnnotatedWord)sentence.getWord(j + 1)).getParse().getWordWithPos();
                    this.wordBiGramModel.addNGram(words);
                    igs[1] = new Word(((AnnotatedWord)sentence.getWord(j + 1)).getParse().getTransitionList());
                    this.igBiGramModel.addNGram(igs);
                }
            }

            if(i > 0 && i % 5000 == 0) {
                System.out.println("Trained " + i + " of sentences of " + corpus.sentenceCount());
            }
        }

        this.wordUniGramModel.calculateNGramProbabilities(new LaplaceSmoothing());
        this.igUniGramModel.calculateNGramProbabilities(new LaplaceSmoothing());
        this.wordBiGramModel.calculateNGramProbabilities(new InterpolatedSmoothing(new LaplaceSmoothing()));
        this.igBiGramModel.calculateNGramProbabilities(new InterpolatedSmoothing(new LaplaceSmoothing()));
    }

    protected double getWordProbability(Word word, ArrayList<FsmParse> correctFsmParses, int index) {
        return index != 0 && correctFsmParses.size() == index?this.wordBiGramModel.getProbability(new Word[]{((FsmParse)correctFsmParses.get(index - 1)).getWordWithPos(), word}):this.wordUniGramModel.getProbability(new Word[]{word});
    }

    protected double getIgProbability(Word word, ArrayList<FsmParse> correctFsmParses, int index) {
        return index != 0 && correctFsmParses.size() == index?this.igBiGramModel.getProbability(new Word[]{new Word(((FsmParse)correctFsmParses.get(index - 1)).getTransitionList()), word}):this.igUniGramModel.getProbability(new Word[]{word});
    }

    protected Word getBestRootWord(FsmParseList fsmParseList) {
        double bestProbability = -2.147483647E9D;
        Word bestWord = null;

        for(int j = 0; j < fsmParseList.size(); ++j) {
            Word word = fsmParseList.getFsmParse(j).getWordWithPos();
            Word ig = new Word(fsmParseList.getFsmParse(j).getTransitionList());
            double wordProbability = this.wordUniGramModel.getProbability(new Word[]{word});
            double igProbability = this.igUniGramModel.getProbability(new Word[]{ig});
            double probability = wordProbability * igProbability;
            if(probability > bestProbability) {
                bestWord = word;
                bestProbability = probability;
            }
        }

        return bestWord;
    }

    protected FsmParse getParseWithBestIgProbability(FsmParseList parseList, ArrayList<FsmParse> correctFsmParses, int index) {
        FsmParse bestParse = null;
        double bestProbability = -2.147483647E9D;

        for(int j = 0; j < parseList.size(); ++j) {
            Word ig = new Word(parseList.getFsmParse(j).getTransitionList());
            double probability = this.getIgProbability(ig, correctFsmParses, index);
            if(probability > bestProbability) {
                bestParse = parseList.getFsmParse(j);
                bestProbability = probability;
            }
        }

        return bestParse;
    }

    public ArrayList<FsmParse> disambiguate(FsmParseList[] fsmParses) {
        ArrayList correctFsmParses = new ArrayList();

        for(int i = 0; i < fsmParses.length; ++i) {
            Word bestWord = this.getBestRootWord(fsmParses[i]);
            fsmParses[i].reduceToParsesWithSameRootAndPos(bestWord);
            FsmParse bestParse = this.getParseWithBestIgProbability(fsmParses[i], correctFsmParses, i);
            if(bestParse != null) {
                correctFsmParses.add(bestParse);
            }
        }
        return correctFsmParses;
    }

    public void saveModel() {
        super.saveModel();
        this.wordBiGramModel.save("words.2gram");
        this.igBiGramModel.save("igs.2gram");
    }

    public void loadModel() {
        super.loadModel();

        try {
            FileInputStream inFile = new FileInputStream(RootGramsFolder + "words.2gram");
            ObjectInputStream inObject = new ObjectInputStream(inFile);
            this.wordBiGramModel = (NGram)inObject.readObject();
            inFile = new FileInputStream(RootGramsFolder + "igs.2gram");
            inObject = new ObjectInputStream(inFile);
            this.igBiGramModel = (NGram)inObject.readObject();
        } catch (IOException | ClassNotFoundException var4) {
            var4.printStackTrace();
        }

    }

}
