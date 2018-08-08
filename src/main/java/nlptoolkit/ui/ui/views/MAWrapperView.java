package nlptoolkit.ui.ui.views;

import Dictionary.Word;
import Syntax.Markers.MorphologyHtmlMarker;
import Wrappers.IExternalMorphologicalAnalyzer;
import Wrappers.WordAnalysis;

import com.google.common.collect.Sets;
import com.vaadin.data.ValueProvider;
import com.vaadin.event.ShortcutAction;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.components.grid.GridSelectionModel;
import com.vaadin.ui.components.grid.HeaderRow;
import com.vaadin.ui.renderers.ComponentRenderer;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.renderers.Renderer;
import javafx.scene.layout.Pane;
import nlptoolkit.ui.services.ExternalMorphologicalAnalyzerTypes;
import nlptoolkit.ui.services.NLPService;
import nlptoolkit.ui.ui.models.MAWrapperModel;
import org.apache.commons.lang3.StringUtils;
import org.omg.CORBA.ObjectHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.LinkedBlockingDeque;

public class MAWrapperView extends NLPView {

    final String _InitialSentences = "okudum";  //TODO: Get from querystring
    Label LblResult;
    private VerticalLayout Layout;

    @Override
    public String GetScreenName() {
        return "MAWrappers";
    }

    public MAWrapperView() {

        Layout = new VerticalLayout();
        addComponent(Layout);
        final HorizontalLayout console = new HorizontalLayout();

        console.setSizeFull();
        console.setMargin(false);
        Layout.addComponent(console);
        TextField txtInput = new TextField(null, _InitialSentences);
        txtInput.setSizeFull();
        txtInput.setResponsive(true);
        Button btnAnalyze = new Button("Analyze");
        btnAnalyze.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        LblResult = new Label("", ContentMode.PREFORMATTED);

        //MATypes.
        ComboBox cmbMATypes = new ComboBox();
        cmbMATypes.setItems("-All-","Dilbaz","TRMorph","ITUWeb","Zemberek","SakOffline");     //TODO: Do not respublish ITUWeb due to the licensing restrictions!
        cmbMATypes.setValue("-All-");
        cmbMATypes.setEmptySelectionAllowed(false);
        cmbMATypes.setTextInputAllowed(false);

        console.addComponents(txtInput,cmbMATypes,btnAnalyze);
        console.setExpandRatio(txtInput,100);       //textbox'a en fazla genişleme yetkisini veriyor.

        //Grid
        Grid<IExternalMorphologicalAnalyzer> grid = new Grid<>();

        NLPService nlpService = new NLPService();
        btnAnalyze.addClickListener(e -> {
            ClearAnalysis();
            String word = txtInput.getValue();
            String maTypeStr = cmbMATypes.getValue().toString();
            if (maTypeStr.equals("-All-")){
                ArrayList<MAWrapperModel> models = BuildModels(nlpService,word);
                //BindGrid(models);
                BindGridLayout(models);
            }
            else{
                ExternalMorphologicalAnalyzerTypes maType = ExternalMorphologicalAnalyzerTypes.valueOf(maTypeStr);
                WordAnalysis wordAnalysis = nlpService.MorphologicallyAnalyzeExternally(word, maType);
                LblResult.setValue(org.apache.commons.lang3.StringUtils.join(wordAnalysis.Results,"\n"));
                Layout.addComponent(LblResult);
            }
        });
    }

    protected ArrayList<MAWrapperModel> BuildModels(NLPService nlpService, String word){
        ArrayList<MAWrapperModel> models = new ArrayList<>();
        int maIndex = 0;
        for (Object obj : ExternalMorphologicalAnalyzerTypes.values()) {
            MAWrapperModel model = new MAWrapperModel(obj.toString());

            try {
                String val = obj.toString();
                ExternalMorphologicalAnalyzerTypes maType = ExternalMorphologicalAnalyzerTypes.valueOf(val);
                WordAnalysis analysis = nlpService.MorphologicallyAnalyzeExternally(word,maType);
                String text = org.apache.commons.lang3.StringUtils.join(analysis.Results,"</br>");
                if (StringUtils.isEmpty(text)) text = "N/A";
                String markedText = MarkText(text);
                model.Text = markedText;        //TODO:
                model.Index = maIndex;
            }
            catch (Exception ex){
                model.Text = "Err";
                model.IsError = true;
            }
            finally {
                maIndex++;
            }
            models.add(model);
        }
        return models;
    }

    private GridLayout GridLayout = null;
    private Grid<MAWrapperModel> Grid;

    /*We are using GridLayout instead of Grid due to the lack of support of dynamic row sizes of grids. https://vaadin.com/forum/thread/16943882*/
    private void BindGrid(ArrayList<MAWrapperModel> models){
        boolean isFirstRender = Grid == null;
        if(!isFirstRender) removeComponent(Grid);
        Grid<MAWrapperModel> grid = new Grid<>();

        //Set grid
        grid.setItems(models);
        grid.setSizeFull();
        grid.setResponsive(true);
        addComponent(grid); //TODO: Manage holder.
        //col1
        grid.addColumn(MAWrapperModel::getName).setId("1").setCaption("NAME");

        //col2
        com.vaadin.ui.Grid.Column<MAWrapperModel,Label> cName = grid.addColumn(new ValueProvider<MAWrapperModel, Label>() {
            @Override
            public Label apply(MAWrapperModel maWrapperModel) {
                return new Label("gokhan<br>ercan",ContentMode.HTML);
            }
        });
        cName.setRenderer(new ComponentRenderer());
        //renderer.encode(new Label("gokhan<br><ercan"));
        //cName.setRenderer(renderer);
        //com.vaadin.ui.components.grid
        //cName.setRenderer(new HtmlRenderer());

        grid.setCaptionAsHtml(true);
        Grid = grid;
    }

    private GridLayout BindGridLayout(ArrayList<MAWrapperModel> models){
        boolean isFirstRender = GridLayout == null;
        if(!isFirstRender) {
            removeComponent(GridLayout);
            Layout.removeComponent(GridLayout);
        }

        GridLayout grid = new GridLayout(2,5);
        grid.setSpacing(false);
        grid.setMargin(new MarginInfo(true,false,false,false));
        grid.addStyleName("outlined");
        grid.setSizeFull();
        grid.setResponsive(true);

        int rowIndex = 0;
        for (MAWrapperModel model : models) {
            //header
            Label header = new Label(model.Name);
            header.addStyleName("bold");
            grid.addComponent(header,0,rowIndex);
            grid.setColumnExpandRatio(0,10);

            //parse
            Label lblText = new Label(model.Text,ContentMode.HTML);
            lblText.addStyleName("parse");
            VerticalLayout vRow = new VerticalLayout();
            vRow.setMargin(false);
            vRow.setSizeFull();
            vRow.setResponsive(true);
            vRow.addComponents(lblText,new Label("</br>",ContentMode.HTML));
            grid.addComponent(vRow,1,rowIndex);
            grid.setColumnExpandRatio(1,100);

            rowIndex++;
        }
        GridLayout = grid;
        Layout.addComponent(grid);
        return grid;
    }

    protected String MarkText(String text){
        MorphologyHtmlMarker marker = new MorphologyHtmlMarker("pos","span", Sets.newHashSet("verb","noun","adv","adj","pnoun"));
        return marker.MarkMatchingMTags(text);
    }

    private void ClearAnalysis(){
        Layout.removeComponent(LblResult);
        LblResult.setValue("");
        if(Grid != null) {
            removeComponent(Grid);
            Grid = null;
        }
    }
}
