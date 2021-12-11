package Presentation_Layer;

import Facade_Layer.InventoryFacade;
import Facade_Layer.SuppliersFacade;

import java.util.*;

public class MainSupplierMenu extends AbstractMenu {

    public MainSupplierMenu(Scanner scanner, InventoryFacade Iaf, SuppliersFacade Saf) {
        super(Saf, scanner, Iaf);
    }


    public void printMenu() {
        while (true) {
            System.out.println("Please Choose:\n" +
                    "1) Supplier Menu\n" +
                    "2) Order Menu\n" +
                    "3) Exit");
            switch (scanner.nextLine()) {
                case "1":
                    SupplierMenu menu = new SupplierMenu(facade, scanner, _af);
                    menu.printMenu();
                    break;
                case "2":
                    new OrderMenu(facade, scanner, _af.get_branchID(), _af).printMenu();
                    break;
                case "3":
                    return;
                default:
                    System.out.println("Unsupported option. Please choose again.");
            }

        }
    }
}
