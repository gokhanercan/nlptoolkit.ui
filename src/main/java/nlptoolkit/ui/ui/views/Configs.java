package nlptoolkit.ui.ui.views;

class Configs {
    static final String NEWLINE_SEPARATOR = System.getProperty("line.separator");
    static final String WORD_SEPARATOR = "|";
    static final String MAPPING_SEPARATOR = "=>";

    static final String INITIAL_SENTENCE_SPLITTER_TEXT = "Öğrencilerin ana dillerini okuma, yazma, anlama ve " +
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
            "sağlanması hedeflenmiştir.";
    static final int SENTENCE_SPLITTER_CHAR_LIMIT = 10000;

    static final String MAD_INITIAL_TEXT = "Gözlükçü dükkanına gittim .";
    static final String MAD_DOWNLOAD_FILENAME = "morphologicalAnalysisResults.txt";
    static final int MAD_CHAR_LIMIT = 1000;

    static final String ASCIIFIER_INITIAL_TEXT = "öğretmen\nşaşırmak\nIstanbul\nİstanbul\nitiraz\nüzüm\n";
    static final String ASCIIFIER_DOWNLOAD_FILENAME = "asciifierResults.txt";
    static final int ASCIIFIER_CHAR_LIMIT = 10000;

    static final String SPELL_CHECKER_INITIAL_TEXT = "İstanbül'da lokilde gezdik." +
            "\nİstanbül'da lokilde gezmedik.";
    static final String SPELL_CHECKER_DOWNLOAD_FILENAME = "spellCheckerResults.txt";
    static final int SPELL_CHECKER_CHAR_LIMIT = 10000;
}
