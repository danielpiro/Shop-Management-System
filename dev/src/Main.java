import Presentation_Layer.MainMenu;
import Facade_Layer.ApplicationFacade;
import Facade_Layer.TransportationFacade;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws Exception {
        ApplicationFacade applicationFacade = ApplicationFacade.getInstance();
        BufferedReader scanner = new BufferedReader(new InputStreamReader(System.in));
        TransportationFacade transportationFacade = TransportationFacade.getInstance();
        MainMenu menu = new MainMenu(applicationFacade,transportationFacade,scanner);
        menu.deploy();
    }
}

