package Presentation_Layer;

import DataLayer.DalObjects.DBController.DalController;
import Facade_Layer.ApplicationFacade;
import Facade_Layer.Response;
import Facade_Layer.TransportationFacade;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class MainMenu {
    private Employee_Menu employee_menu;
    private TransportationMenu transportation_menu;
    private BufferedReader scanner;
    boolean load = false;


    public MainMenu(ApplicationFacade applicationFacade, TransportationFacade transportationFacade, BufferedReader buffer) {
        employee_menu = new Employee_Menu(applicationFacade,buffer);
        scanner = buffer;
        this.transportation_menu = new TransportationMenu(transportationFacade);
    }


    private void firstTime() throws Exception {
        if (!load) {
            System.out.println("Hello, in order to prepare an existing information system, you must insert 'data'");
            String str = scanner.readLine();
            if (str.equals("data")) {
                DalController.getDalController().openConnection();
                load = true;
            } else {
                HR_Manager_registration_protocol();
            }
        } else System.out.println("Data already loaded.");
    }

    private void HR_Manager_registration_protocol() throws IOException {
        System.out.println("no data has been loaded, at least one Hr manager is required to be registered in order to proceed." +
                "\nAn HR Manager registration protocol will take place:");
        employee_menu.registerWorker(false, 0);
    }

    public void deploy() throws Exception {
        scanner = new BufferedReader(new InputStreamReader(System.in));
        boolean exit = false;
        while (!exit) {
            System.out.println("Choose what you want to do:");
            for (int i = 1; i <= Options.getFirstOts().length; i++) {
                System.out.println((i) + ") " + Options.getFirstOts()[i - 1]);
            }
            int op = UtilP.scanInt(scanner);
            switch (op) {
                case 1:
                    firstTime();
                    break;
                case 2:
                    logIn();
                    break;
                case 3:
                    exit = true;
                    break;
            }
        }
    }

    private void logIn() throws Exception {
        if (!load) {
            HR_Manager_registration_protocol();
            load = true;
        }
        boolean logged = false;
        int id = -1;
        while (!logged) {
            System.out.println("In order to Login, please insert your ID:");
            id = UtilP.scanInt(scanner);
            if (id == -1)
                break;
            Response response = employee_menu.Login(id);
            if (!response.isErrorOccurred())
                logged = true;
            else System.out.println(response.errorMessage);
        }
        if (id != -1) {
            boolean done = false;
            System.out.println("Hello " + employee_menu.getName());
            if (employee_menu.isHRManager()) {
                while (!done)
                    done = employee_menu.deployHRMenu();
            } else if (employee_menu.isTPManager()) {
                while (!done)
                    done = transportation_menu.mainMenu();
                ApplicationFacade.getInstance().logout();
            } else if (employee_menu.isStorageKeeper()) {
                while (!done)//todo
                    done =employee_menu.deployStorageKeeperMenu();
            }else if (employee_menu.isSManger()) {
                while (!done)
                    done = employee_menu.deploySMenu();
            }else {
                while (!done)
                    done = employee_menu.deployRegWorkMenu();
            }
        }
    }
}
