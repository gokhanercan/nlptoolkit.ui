package nlptoolkit.ui.ui.models;


public class MAWrapperModel {

    public String getName(){
        return Name;
    }
    public String getText(){
        return Text;
    }

    public int Index;
    public String Name;
    public String[] Expressions;
    public String Text;
    public boolean IsError = false;
    public int Miliseconds;

    public MAWrapperModel(String name, String[] expressions) {
        Name = name;
        Expressions = expressions;
    }

    public MAWrapperModel(String name) {
        Name = name;
    }

    public MAWrapperModel() {
    }
}
