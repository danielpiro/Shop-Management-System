package Presentation_Layer;


import Facade_Layer.FacadeObjects.FacadeOrder;
import Facade_Layer.InventoryFacade;
import Facade_Layer.Response;
import Facade_Layer.ResponseT;
import Facade_Layer.SuppliersFacade;

import java.rmi.NoSuchObjectException;
import java.util.*;

public class OrderMenu extends AbstractMenu {

    private int branch_id;

    public OrderMenu(SuppliersFacade facade, Scanner scanner, int branch_id, InventoryFacade _if) {
        super(facade, scanner,_if);
        this.branch_id = branch_id;
    }

    @Override
    public void printMenu() {
        String input = "";
        while (!input.toLowerCase().equals("q")) {
            System.out.println("ORDER MENU");
            System.out.println("1)Place New Order\n2)View Order\n3)Remove Order\n4)Close Order\n" +
                    "TO Return to previous Menu Q");
            input = scanner.nextLine();
            //default -> System.out.println("Unsupported key operation " + input + ". Please try again");
            switch (input) {
                case "1":
                    placeAnOrder();
                    break;
                case "2":
                    getOrder();
                    break;
                case "3":
                    removeOrder();
                    break;
                case "4":
                    closeOrder();
            }
        }
    }

    private void closeOrder() {
        System.out.println("Please enter order id to close:");
        int orderId = getNextInt();
        List<ResponseT<Integer>> items = facade.closeOrder(orderId, branch_id);
        List<Integer> items_to_update = new ArrayList<>();
        for (ResponseT<Integer> r: items) {
            if (r.isErrorOccurred())
                System.out.println(r.errorMessage);
            else
                items_to_update.add(r.getValue());

        }
        MainInventoryMenu menu = new MainInventoryMenu(_af,facade);
        try {
            menu.checkItemsFromDelivery(items_to_update);
        } catch (NoSuchObjectException e) {
            e.printStackTrace();
        }


    }

    private void placeAnOrder() {
        System.out.print("Please enter Supplier B.N. to make order from: ");
        int supplierBN = Integer.parseInt(scanner.nextLine());
        Map<Integer, Integer> itemAndQuantities = new HashMap<>();
        String input = "";
        int catalogNumber, Quantities;
        System.out.println("Please enter the items for the order:");
        while (!input.equalsIgnoreCase("q")) {
            System.out.print("Item Catalog Number: ");
            catalogNumber = getNextInt();
            System.out.print("Quantities: ");
            Quantities = getNextInt();
            itemAndQuantities.put(catalogNumber, Quantities);
            System.out.println("To quit adding items press q");
            input = scanner.nextLine();
        }
        Response response = facade.placeOrder(supplierBN, itemAndQuantities, branch_id);
        if (response.isErrorOccurred())
            System.out.println("Couldn't place this order: " + response.errorMessage);
        else
            System.out.println("Done");
    }

    private void getOrder() {
        System.out.println("1) Get Order by I.D\n2) Get Order by date");
        switch (scanner.nextLine()) {
            case "1": {
                System.out.print("Order I.D.: ");
                ResponseT<FacadeOrder> response = facade.getOrder(getNextInt());
                if (response.isErrorOccurred())
                    System.out.println(response.errorMessage);
                else {
                    FacadeOrder order = response.getValue();
                    order.print();
                    if (order.branch_id == branch_id) { //user is allowed to edit order only if it is from its own branch
                        System.out.println("To Change Order Press E");
                        if (scanner.nextLine().equalsIgnoreCase("e")) {
                            changeOrder(order);
                        }
                    }
                }
            }
            break;
            case "2": {
                System.out.print("Enter Supplier B.N.: ");
                int supplierBN = getNextInt();
                System.out.println("Enter Date to Search:  ");
                System.out.print("Enter year:  ");
                String date = scanner.nextLine();
                System.out.print("Enter Month:  ");
                date += "-" + scanner.nextLine();
                System.out.print("Enter day:  ");
                date += "-" + scanner.nextLine();
                System.out.println();
                List<ResponseT<FacadeOrder>> responseTList = facade.getOrders(supplierBN, date);
                for (ResponseT<FacadeOrder> response : responseTList) {
                    if (response.isErrorOccurred())
                        System.out.println(response.errorMessage);
                    else {
                        response.getValue().print();
                        System.out.println("To Change Order Press E, to return to previous menu press Q, to proceed the next order, press any key");
                        String input = scanner.nextLine();
                        if (input.equalsIgnoreCase("e"))
                            changeOrder(response.getValue());
                        else if (input.equalsIgnoreCase("q"))
                            break;
                    }
                }
            }
            break;
        }
    }

    private void removeOrder() {
        System.out.print("Enter Order I.D. to Remove: ");
        Response response = facade.removeOrder(getNextInt(), branch_id);
        if (response.isErrorOccurred())
            System.out.println("Could Not Remove this Order: " + response.errorMessage);
        else
            System.out.println("Done.");
    }

    private void changeOrder(FacadeOrder order) {
        System.out.println("To Update Quantities Press 1\nTo Add Items Press 2");
        String input = scanner.nextLine();
        switch (input) {
            case "1":
                System.out.println("CHANGING QUANTITIES MENU");
                System.out.println("To return Press Q");
                if (scanner.nextLine().equalsIgnoreCase("q"))
                    return;
                input = "";
                while (!input.equalsIgnoreCase("q")) {
                    System.out.print("Enter Item Serial Number to Change Quantities to: ");
                    int serialNumber = getNextInt();
                    System.out.print("Enter New Quantity: ");
                    int quantity = getNextInt();
                    Response response = facade.updateQuantities(order.id, order.supplierBN, quantity, serialNumber);
                    if (response.isErrorOccurred())
                        System.out.println("Could Not Change: " + response.errorMessage);

                    System.out.println("Done. To End changing press q");
                    input = scanner.nextLine();
                }
                break;
            case "2":
                System.out.println("ADDING ITEM Menu");
                System.out.println("To return Press Q");
                if (scanner.nextLine().equalsIgnoreCase("q"))
                    return;
                input = "";
                while (!input.equalsIgnoreCase("q")) {
                    System.out.print("Enter Item Serial Number: ");
                    int serialNumber = getNextInt();
                    System.out.print("Enter Quantity: ");
                    int quantity = getNextInt();
                    Response response = facade.addItemToOrder(order.id, order.supplierBN, serialNumber, quantity);
                    if (response.isErrorOccurred())
                        System.out.println("Could Not Change: " + response.errorMessage);

                    System.out.println("Done. To End changing press q");
                    input = scanner.nextLine();

                }
                break;
        }
    }

}
