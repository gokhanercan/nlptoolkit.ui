package nlptoolkit.ui.ui.views;

import Syllibification.IrregularWordException;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import nlptoolkit.ui.services.NLPService;

public class SyllabificationView extends NLPView {

    @Override
    public String GetScreenName() {
        return "Syllabification";
    }       //TODO: rename.

    public SyllabificationView() {

        //Layout
        final VerticalLayout layout = new VerticalLayout();
        final HorizontalLayout row1 = new HorizontalLayout();
        row1.setSizeFull();
        TextArea txtInput = new TextArea("Please type words/sentences to syllabify:","Alıştıkları\nzîrâ\nnâdiren\natamadım\nattım\nparktan\nbağıramayacağım\nparka\ngidilemeyeceğine\nkent\nmaymunsugiller\nmuvaffakiyetsizleştiricileştiriveremeyebileceklerimizdenmişsinizcesine\nÇekoslovakyalılaştıramadıklarımızdan");
        txtInput.setRows(20);
        txtInput.setSizeFull();
        TextArea txtOutput = new TextArea("Syllabified words/sentences:");
        txtOutput.setSizeFull();
        txtOutput.setRows(20);
        Button btn = new Button("Syllabify");
        row1.addComponents(txtInput,txtOutput);
        layout.addComponent(row1);
        layout.addComponents(btn);

        //Action
        NLPService nlpService = new NLPService();
        btn.addClickListener(e -> {
            txtOutput.clear();
            String[] lines = txtInput.getValue().split("\n");
            for (String line : lines) {
                String out = null;
                try {
                    out = nlpService.Syllabify(line);
                } catch (IrregularWordException e1) {
                    e1.printStackTrace();
                    out = line;
                }
                txtOutput.setValue(txtOutput.getValue() + out + "\n");
            }
        });

        addComponent(layout);
    }
}
