package nlptoolkit.ui.services.wrappers;

import MorphologicalDisambiguation.NaiveDisambiguation;
import Ngram.NGram;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

public abstract class MyNaiveDisambiguation extends NaiveDisambiguation {

    String RootGramsFolder;

    public MyNaiveDisambiguation(String rootGramsFolder) {
        RootGramsFolder = rootGramsFolder;
    }

    public void loadModel() {
        try {
            FileInputStream inFile = new FileInputStream(RootGramsFolder + "words.1gram");
            ObjectInputStream inObject = new ObjectInputStream(inFile);
            this.wordUniGramModel = (NGram)inObject.readObject();
            inFile = new FileInputStream(RootGramsFolder + "igs.1gram");
            inObject = new ObjectInputStream(inFile);
            this.igUniGramModel = (NGram)inObject.readObject();
        } catch (FileNotFoundException var4) {
            var4.printStackTrace();
        } catch (ClassNotFoundException var5) {
            var5.printStackTrace();
        } catch (IOException var6) {
            var6.printStackTrace();
        }

    }

}
