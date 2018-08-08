package nlptoolkit.ui.application;

import Dictionary.TurkishWordComparator;
import Dictionary.TxtDictionary;
import MorphologicalAnalysis.FsmMorphologicalAnalyzer;
import Wrappers.Dilbaz.DilbazWrapper;
import Wrappers.IExternalMorphologicalAnalyzer;
import Wrappers.ITU.ITUWebWrapper;
import Wrappers.Sak.SakOfflineWrapper;
import Wrappers.TRMorph.TRMorphWrapper;
import Wrappers.Zemberek.ZemberekWrapper;
import com.vaadin.server.VaadinServlet;
import nlptoolkit.ui.services.ExternalMorphologicalAnalyzerTypes;
import nlptoolkit.ui.services.FSService;

import javax.servlet.ServletContext;
import java.util.Hashtable;

/*TODO:Build with a real IOC container.*/
public class AppContainer {

    private VaadinServlet _Servlet = null;
    public VaadinServlet getServlet(){
        if (_Servlet == null) _Servlet = VaadinServlet.getCurrent();
        return _Servlet;
    }
    public ServletContext getContext(){
        return getServlet().getServletContext();
    }

    private static AppContainer _AppContainer = null;
    public static AppContainer CreateInstance(){
        if (_AppContainer == null){         //TODO: lock it or use appserver annotations for singleton behaviour. Jersey.gerServiceLocator() var. https://jersey.java.net/apidocs/2.12/jersey/org/glassfish/jersey/server/ApplicationHandler.html
            _AppContainer = new AppContainer();
            _AppContainer.loadConfiguration();
        }
        return _AppContainer;
    }

    private FSService _FSService = new FSService();

    private FsmMorphologicalAnalyzer _Analyzer = null;          //TODO: Make it singleton across screens. not only this instance. https://trello.com/c/epBpGk1R/38-make-shared-services-singleton-across-views
    private FsmMorphologicalAnalyzer getAnalyzer(){
        if (_Analyzer == null){     //supports only single instance for now!
            _Analyzer = new FsmMorphologicalAnalyzer(_FSService.GetResourceFilesRootPath() + "turkish_finite_state_machine.xml",
                    new TxtDictionary(_FSService.GetResourceFilesRootPath() + "turkish_dictionary.txt",new TurkishWordComparator()));
        }
        return _Analyzer;
    }

    public FsmMorphologicalAnalyzer GetAnalyzer(){
        return getAnalyzer();
    }

    public void loadConfiguration(){
    }

    private Hashtable<String,IExternalMorphologicalAnalyzer> _MAIndex = new Hashtable<>();
    public IExternalMorphologicalAnalyzer ResolveExternalMorphologicalAnalyzer(ExternalMorphologicalAnalyzerTypes type){
        IExternalMorphologicalAnalyzer ma = _MAIndex.get(type.toString());
        if(ma == null) {
            ma = CreateExternalMorphologicalAnalyzer(type);
            _MAIndex.put(type.toString(),ma);
        }
        return ma;
    }
    protected IExternalMorphologicalAnalyzer CreateExternalMorphologicalAnalyzer(ExternalMorphologicalAnalyzerTypes type){
        switch (type){
            case TRMorph:
                return new TRMorphWrapper(getContext().getInitParameter("TRMorphRootFolder") ,getContext().getInitParameter("TRMorphLauncherFileName"));
            case Zemberek:
                return new ZemberekWrapper();
            case ITUWeb:
                return new ITUWebWrapper();
            case Dilbaz:
                return new DilbazWrapper(getAnalyzer());
            case SakOffline:
                String filePath = _FSService.GetResourceFilesRootPath() + getContext().getInitParameter("SakOfflineFilePath");
                return new SakOfflineWrapper(filePath);
            default:
                throw new RuntimeException("No such MA!");
        }
    }

}
