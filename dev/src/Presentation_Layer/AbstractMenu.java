package Presentation_Layer;


import Facade_Layer.InventoryFacade;
import Facade_Layer.SuppliersFacade;

import java.util.Scanner;

public abstract class  AbstractMenu {

    protected SuppliersFacade facade;
    protected Scanner scanner;
    protected InventoryFacade _af;

    public AbstractMenu(SuppliersFacade facade, Scanner scanner, InventoryFacade iFacade){
        this.facade = facade;
        this.scanner = scanner;
        this._af = iFacade;
    }

    public abstract void printMenu();

    public Integer getNextInt(){
        int result;
        try{
            result = Integer.parseInt(scanner.nextLine());
            return result;
        }
        catch (Exception e){
            System.out.println("Invalid input, not an integer number");
        }
        return null;
    }
}
