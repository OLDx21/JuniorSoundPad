
import javafx.beans.property.SimpleStringProperty;

public class Infoclient {
    private  SimpleStringProperty name;
    private  SimpleStringProperty lengh;
    private SimpleStringProperty bind;

static {
    System.loadLibrary("Dll2");
}

    public Infoclient(String name, String lengh, String  bind) {
        this.lengh = new SimpleStringProperty(lengh);
        this.bind = new SimpleStringProperty(bind);
        this.name = new SimpleStringProperty(name);



    }
    public Infoclient(){

    }
    public static void Goes(){

        printOne("GOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
    }

    native public static void printOne(String response);


    public SimpleStringProperty getname(){return name;}
    public void setName(String name){this.name=new SimpleStringProperty(name);}

    public SimpleStringProperty getlengh(){return lengh;}
    public void setlengh(String lengh){this.lengh=new SimpleStringProperty(lengh);}

    public SimpleStringProperty getbind(){return bind;}
    public void setbind(String bind){this.bind=new SimpleStringProperty(bind);}


}