package nlptoolkit.ui.ui.views;

import Corpus.Sentence;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import nlptoolkit.ui.services.NLPService;

import java.util.ArrayList;

public class SentenceSplitterView extends NLPView {

    @Override
    public String GetScreenName() {
        return "Tokenizer/Sentence Splitter";
    }

    public SentenceSplitterView() {

        //Layout
        final VerticalLayout layout = new VerticalLayout();
        final HorizontalLayout row1 = new HorizontalLayout();
        row1.setSizeFull();
        TextArea txtInput = new TextArea("Enter any piece of text to tokenize/split to sentences", "Öğrencilerin ana dillerini okuma, yazma, anlama ve " +
                "yorumlama yeteneklerinin tespit edilmesi, eğitim sisteminin " +
                "hemen her aşamasında birincil ölçme ve seçme yöntemi olarak " +
                "kullanılmaktadır. Ülkemizde de ilköğretimden, yüksek lisans " +
                "seviyesine kadar öğrencinin okuduğunu anlaması ve " +
                "yorumlamasını ölçümleyen Türkçe çoktan seçmeli sorularının " +
                "sorulması değişmeyen bir uygulamadır. Bu çalışmada " +
                "sunduğumuz 973 soruluk veri kümesi ile, ilgili DDİ " +
                "modellerinin gelişimini raporlayan dışsal (extrinsic) bir " +
                "ölçümleme veri kümesi oluşturulmuştur. Buna ek olarak, " +
                "oluşturduğumuz veri kümesinin, çeşitli yaş gruplarından çeşitli " +
                "zorluk seviyelerinde sınavlara giren öğrencilerin bu soruları " +
                "çözme başarıları ile modelleri karşılaştırabilme imkanının " +
                "sağlanması hedeflenmiştir.");
        txtInput.setRows(15);
        txtInput.setSizeFull();
        TextArea txtOutput = new TextArea("Space and new line separated Tokens/Sentences:");
        txtOutput.setSizeFull();
        txtOutput.setRows(15);
        Button btn = new Button("Tokenize/Split");
        btn.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        row1.addComponents(txtInput,txtOutput);
        layout.addComponent(row1);
        layout.addComponents(btn);

        NLPService nlpService = new NLPService();
        btn.addClickListener(e -> {
            txtOutput.clear();
            String inputText = txtInput.getValue().trim();
            ArrayList<Sentence> splittedSentences = nlpService.TurkishSplitter(inputText);
            for (Sentence ss : splittedSentences) {
                txtOutput.setValue(txtOutput.getValue() + ss + "\n\n");
            }
            //lblMessage.setValue("Input text splitted into " + splittedSentences.size() + " sentences.");
        });

        addComponent(layout);
    }
}
