package Presentation_Layer;


import Facade_Layer.InventoryFacade;
import Facade_Layer.SuppliersFacade;

import java.rmi.NoSuchObjectException;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        InventoryFacade Iaf=null;//todo
        SuppliersFacade Saf = new SuppliersFacade(Iaf);
        while(true) {
            System.out.println("Hello User, please choose:\n" +
                    "1) Supplier menu\n" +
                    "2) inventory menu\n" +
                    "3) exit");
            switch (scanner.nextLine()) {
                case "1":
                    MainSupplierMenu mainSupplierMenu = new MainSupplierMenu(scanner, Iaf, Saf);
                    mainSupplierMenu.printMenu();
                    break;
                case "2":
                    MainInventoryMenu mu = new MainInventoryMenu(Iaf, Saf);
                    try {
                        mu.Start();
                    } catch (NoSuchObjectException e) {
                        e.printStackTrace();
                    }
                    break;
                case "3":
                    return;

            }
        }

    }

}
