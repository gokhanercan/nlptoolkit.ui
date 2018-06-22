package nlptoolkit.ui.ui.views;

import Corpus.Sentence;
import com.vaadin.ui.*;
import nlptoolkit.ui.services.NLPService;

import java.util.ArrayList;

public class SentenceSplitterView extends NLPView {

    @Override
    public String GetScreenName() {
        return "Sentence Splitter";
    }

    public SentenceSplitterView() {

        //Layout
        final VerticalLayout layout = new VerticalLayout();
        final HorizontalLayout row1 = new HorizontalLayout();
        row1.setSizeFull();
        TextArea txtInput = new TextArea("SentenceSplitter: Please enter any piece of text","Öğrencilerin ana dillerini okuma, yazma, anlama ve " +
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
        txtInput.setRows(20);
        txtInput.setSizeFull();
        TextArea txtOutput = new TextArea("SentenceSplitter Output:");
        txtOutput.setSizeFull();
        txtOutput.setRows(20);
        Button btn = new Button("Split");
        //TextArea txtMessage = new TextArea();
        Label lblMessage = new Label();
        row1.addComponents(txtInput,txtOutput);
        layout.addComponent(row1);
        layout.addComponents(btn);
        layout.addComponents(lblMessage);

        //Action
        NLPService nlpService = new NLPService();
        btn.addClickListener(e -> {
            txtOutput.clear();
            String inputText = txtInput.getValue();
            ArrayList<Sentence> splittedSentences = nlpService.TurkishSplitter(inputText);
            for (Sentence ss : splittedSentences) {
                txtOutput.setValue(txtOutput.getValue() + ss + "\n\n");
            }
            lblMessage.setValue("Input text splitted into " + splittedSentences.size() + " sentences.");
        });

        addComponent(layout);
    }
}
