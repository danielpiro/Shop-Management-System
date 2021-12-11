package Presentation_Layer;

import Bussiness_Layer.Supplier.Supplier;
import Facade_Layer.FacadeObjects.*;
import Facade_Layer.InventoryFacade;
import Facade_Layer.Response;
import Facade_Layer.ResponseT;
import Facade_Layer.SuppliersFacade;

import java.time.DayOfWeek;
import java.util.*;

public class SupplierMenu extends AbstractMenu {

    FacadeSupplier supplier;

    public SupplierMenu(SuppliersFacade facade, Scanner scanner, InventoryFacade _if) {
        super(facade, scanner, _if);
    }

    @Override
    public void printMenu() {
        System.out.println("Welcome to Supplier Menu, Please choose the desired operation:\n1)Get supplier\n2)Add new supplier\n3)Remove supplier\n4)Update supplier\n5)return the previous menu");
        switch (scanner.nextLine()) {
            case "1":
                getSupplier();
                break;
            case "2":
                addSupplierMenu();
                break;
            case "3":
                removeSupplier();
                break;
            case "4":
                setSupplier();
                break;
            case "5":
                break;
            default:
                System.out.println("unsupported operation. try again");
                printMenu();
                break;
        }

    }

    private void getBillsOfQuantities(int supplierBN) {
        ResponseT<FacadeSupplier> response = facade.getSupplier(supplierBN);
        if (response.isErrorOccurred()) {
            System.out.println(response.errorMessage);
            System.out.println("operation failed. returning to the previous menu");
            return;
        }
        FacadeSupplier facadeSupplier = response.getValue();
        facadeSupplier.getBillsOfQuantities().printBills();
        System.out.println("Please choose one of the following:\n1)continue editing the current bills of quantities\n2)return to previous menu");
        int choice = getNextInt();
        switch (choice) {
            case 1:
                editBills(supplierBN);
                break;
            case 2:
                break;
            default:
                System.out.println("unsupported operation");
                break;
        }


    }

    private void editBills(int supplierBN) {
        String in = "";
        while (!in.equalsIgnoreCase("q")) {
            try {
                System.out.println("Please choose one of the following:\n1)add discount per order\n" +
                        "2)add discount per item\n3)remove discount pet order\n4)remove discount per item\n" +
                        "5)change discount per order\n6)change discount per item\n7)add item to supplier\n8)remove item\n9)return to previous menu");
                int choice = getNextInt();
                switch (choice) {
                    case 1:
                        try {
                            System.out.println("amount:");
                            int amount = getNextInt();
                            System.out.println("discount:");
                            double discount = Double.parseDouble(scanner.nextLine());
                            Response r = facade.addDiscountPerOrder(supplierBN, amount, discount);
                            if (r.isErrorOccurred()) {
                                System.out.println(r.errorMessage);
                                System.out.println("operation failed. return to previous menu.");
                                return;
                            } else
                                System.out.println("discount added successfully");
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                            return;
                        }
                        break;
                    case 2:
                        try {
                            System.out.println("catalog number of the item:");
                            int catalogNumber = getNextInt();
                            System.out.println("amount:");
                            int amount = getNextInt();
                            System.out.println("discount:");
                            double discount = Double.parseDouble(scanner.nextLine());
                            Response r = facade.addDiscountPerItem(supplierBN, catalogNumber, amount, discount);
                            if (r.isErrorOccurred()) {
                                System.out.println(r.errorMessage);
                                System.out.println("operation failed. return to previous menu.\n");
                                return;
                            } else
                                System.out.println("discount added successfully\n");
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                            return;
                        }
                        break;

                    case 3:
                        try {
                            System.out.println("amount:");
                            int amount = getNextInt();
                            Response r = facade.removeDiscountPerOrder(supplierBN, amount);
                            if (r.isErrorOccurred()) {
                                System.out.println(r.errorMessage);
                                System.out.println("operation failed. return to previous menu");
                                return;
                            } else
                                System.out.println("discount removed successfully");
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                            return;
                        }
                        break;

                    case 4:
                        try {
                            System.out.println("catalog number of the item:");
                            int catalogNumber = getNextInt();
                            System.out.println("amount:");
                            int amount = getNextInt();
                            Response r = facade.removeDiscountPerItem(supplierBN, catalogNumber, amount);
                            if (r.isErrorOccurred()) {
                                System.out.println(r.errorMessage);
                                System.out.println("operation failed. return to previous menu");
                                return;
                            } else
                                System.out.println("discount removed successfully");
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                            return;
                        }
                        break;

                    case 5:
                        try {
                            System.out.println("amount:");
                            int amount = getNextInt();
                            System.out.println("new discount:");
                            double newDiscount = Double.parseDouble(scanner.nextLine());
                            Response r = facade.changeDiscountPerOrder(supplierBN, amount, newDiscount);
                            if (r.isErrorOccurred()) {
                                System.out.println(r.errorMessage);
                                System.out.println("Operation failed. return to previous menu.");
                                return;
                            } else
                                System.out.println("discount changed successfully");
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                            return;
                        }
                        break;

                    case 6:
                        try {
                            System.out.println("catalog number of the item");
                            int catalogNumber = getNextInt();
                            System.out.println("amount:");
                            int amount = getNextInt();
                            System.out.println("new discount:");
                            double newDiscount = Double.parseDouble(scanner.nextLine());
                            Response r = facade.changeDiscountPerItem(supplierBN, catalogNumber, amount, newDiscount);
                            if (r.isErrorOccurred()) {
                                System.out.println(r.errorMessage);
                                System.out.println("Operation failed. return to previous menu.");
                                return;
                            } else
                                System.out.println("discount changed successfully");
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                            return;
                        }
                        break;
                    case 7:
                        try {
                            System.out.println("catalog number of the item:");
                            int catalogNumber = getNextInt();
                            System.out.println("serial number of the item:");
                            int serialNumber = getNextInt();
                            System.out.println("cost:");
                            double cost = Double.parseDouble(scanner.nextLine());
                            Response r = facade.addIncludedItem(supplierBN, serialNumber, catalogNumber, cost);
                            if (r.isErrorOccurred()) {
                                System.out.println(r.errorMessage);
                                System.out.println("Operation failed. return to previous menu.");
                                return;
                            } else
                                System.out.println("item added successfully.");
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                            return;
                        }
                        break;
                    case 8:
                        try {
                            System.out.println("catalog number of the item:");
                            int catalogNumber = getNextInt();
                            Response r = facade.removeItemFromSupplier(catalogNumber, supplierBN);
                            if (r.isErrorOccurred()) {
                                System.out.println(r.errorMessage);
                                System.out.println("Operation failed. return to previous menu.");
                                return;
                            } else
                                System.out.println("item removed successfully.");
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                            return;
                        }
                        break;
                    case 9:
                        break;
                    default:
                        System.out.println("unsupported operation. returning to previous menu.");
                        return;

                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            System.out.println("to quit editing, press Q, else press any other key");
            in = scanner.nextLine();
        }
    }

    private void getContactList(int supplierBN) {
        ResponseT<FacadeSupplier> response = facade.getSupplier(supplierBN);
        if (response.isErrorOccurred()) {
            System.out.println(response.errorMessage);
            System.out.println("operation failed. returning to the previous menu");
            return;
        }
        FacadeSupplier facadeSupplier = response.getValue();
        facadeSupplier.printContacts();

        System.out.println("Please choose one of the following:\n1)continue editing the current contact list\n2)return to previous menu");
        int choice = getNextInt();
        switch (choice) {
            case 1:
                setContactList(supplierBN);
                break;
            case 2:
                break;
            default:
                System.out.println("unsupported operation");
                break;
        }
    }

    private void getSupplier() {
        try {
            while (supplier == null) {
                System.out.println("Enter supplierBN:");
                int supplierBN = getNextInt();
                ResponseT<FacadeSupplier> response = facade.getSupplier(supplierBN);
                if (response.isErrorOccurred()) {
                    System.out.println(response.errorMessage);
                    System.out.println("operation failed. returning to the previous menu");
                    return;
                } else {
                    supplier = response.getValue();
                    supplier.printSupplier();
                    System.out.println("Please enter the desired option:\n1)get supplier orders\n2)get supplier items\n3)get bills of quantities\n4)get contact list\n5)return to previous menu");
                    int choice = getNextInt();
                    switch (choice) {
                        case 1:
                            getAllOrders(supplierBN);
                            break;
                        case 2:
                            getAllItems(supplierBN);
                            break;
                        case 3:
                            getBillsOfQuantities(supplierBN);
                            break;
                        case 4:
                            getContactList(supplierBN);
                            break;
                        case 5:
                            return;
                    }

                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void getAllItems(int supplierBN) {
        List<ResponseT<FacadeSupplierItem>> items = facade.getAllItems(supplierBN);
        for (ResponseT<FacadeSupplierItem> item : items) {
            if (item.isErrorOccurred()) {
                System.out.println(item.errorMessage);
                System.out.println("operation failed. returning to the previous menu");
                return;
            }
        }
        String leftAlignFormat_int = "| %-17s | %-19d | %-18d | %-13.2f |%n";
        System.out.println("Items:");
        System.out.format("+-------------------+---------------------+--------------------+---------------+%n");
        System.out.format("| name              | serial number       | catalog number     | price         |%n");
        System.out.format("+-------------------+---------------------+--------------------+---------------+%n");
        items.forEach((x) -> {
            FacadeSupplierItem item = x.getValue();
            System.out.format(leftAlignFormat_int, item.name, item.serialNUmber, item.catalogNumber, item.price);
        });
        System.out.format("+-------------------+---------------------+--------------------+---------------+%n");


    }

    private void getAllOrders(int supplierBN) {
        List<ResponseT<FacadeOrder>> orders = facade.getAllSupplierOrders(supplierBN);
        for (ResponseT<FacadeOrder> order : orders) {
            if (order.isErrorOccurred()) {
                System.out.println(order.errorMessage);
                System.out.println("operation failed. returning to the previous ");
                return;
            } else {
                order.getValue().print();
                System.out.println();
            }
        }
    }

    private void addSupplierMenu() {
        System.out.println("ADDING SUPPLIER");
        String name;
        int supplierBN;
        String address;
        int bankAccount;
        Supplier.PaymentTerms paymentMethod;

        try {
            System.out.print("Suppler Name: ");
            name = scanner.nextLine();
            System.out.print("Supplier Business Number: ");
            supplierBN = getNextInt();
            System.out.println("Supplier's Address:");
            address = scanner.nextLine();
            System.out.print("Supplier Bank Account: ");
            bankAccount = getNextInt();
            paymentMethod = null;
            Set<DayOfWeek> termOfSupply;
            boolean needDelivery;
            while (paymentMethod == null) {
                System.out.println("Choose Payment Method:\n1) EOM30\n2) EOM60\n3) EOM90\n4) Cash ");
                switch (scanner.nextLine()) {
                    case "1":
                        paymentMethod = Supplier.PaymentTerms.EOM30;
                        break;
                    case "2":
                        paymentMethod = Supplier.PaymentTerms.EOM60;
                        break;
                    case "3":
                        paymentMethod = Supplier.PaymentTerms.EOM90;
                        break;
                    case "4":
                        paymentMethod = Supplier.PaymentTerms.cash;
                        break;
                    default:
                        System.out.println("not a legal option");
                }
            }
            System.out.println("Choose term of supply:\n1) Regular day shipment\n2) On demand shipment\n3) Self pickup");
            switch (scanner.nextLine()) {
                case "1":
                    termOfSupply = createTermOfSupply();
                    needDelivery = false;
                    break;
                case "2":
                    termOfSupply = Collections.emptySet();
                    needDelivery = false;
                    break;
                case "3":
                    termOfSupply = Collections.emptySet();
                    needDelivery = true;
                    break;
                default:
                    throw new IllegalArgumentException("not a legal option");
            }

            FacadeSupplier newSupplier = new FacadeSupplier(name, supplierBN,address, bankAccount, paymentMethod, termOfSupply, createBillOfQuantities(), createContactList(),needDelivery);
            ResponseT<FacadeSupplier> response = facade.addSupplier(newSupplier);
            if (response.isErrorOccurred()) {
                System.out.println(response.errorMessage);
                System.out.println("operation failed. returning to the previous menu");
            } else {
                supplier = response.getValue();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("To Exit Press Q, else press any other key");
            if (scanner.next().toLowerCase().charAt(0) != 'q')
                addSupplierMenu();
        }
    }

    private Set<DayOfWeek> createTermOfSupply() {
        Set<DayOfWeek> termOfSupply = new HashSet<>();
        String input = "";
        while (!input.equals("7")) {
            System.out.println("Supplier Terms of supply:\n1) Sunday\n2) Monday\n3) Tuesday\n4) Wednesday\n5) Thursday\n6) Friday\n7)Quit");
            switch (input = scanner.nextLine()) {
                case "1":
                    termOfSupply.add(DayOfWeek.SUNDAY);
                    break;
                case "2":
                    termOfSupply.add(DayOfWeek.MONDAY);
                    break;
                case "3":
                    termOfSupply.add(DayOfWeek.TUESDAY);
                    break;
                case "4":
                    termOfSupply.add(DayOfWeek.WEDNESDAY);
                    break;
                case "5":
                    termOfSupply.add(DayOfWeek.THURSDAY);
                    break;
                case "6":
                    termOfSupply.add(DayOfWeek.FRIDAY);
                    break;
                case "7":
                    break;
                default:
                    System.out.println("not a legal option");
            }
        }
        return termOfSupply;
    }

    private List<FacadeContact> createContactList() {
        List<FacadeContact> contacts = new ArrayList<>();
        System.out.println("Please enter name, phone number and email for each contact.");
        String name;
        String phoneNumber;
        String email;
        String in = "";

        while (!in.equalsIgnoreCase("q")) {
            System.out.print("Contact's Name: ");
            name = scanner.nextLine();
            System.out.print("Contact's Phone Number: ");
            phoneNumber = scanner.nextLine();
            System.out.print("Contact's Email: ");
            email = scanner.nextLine();
            FacadeContact domainContact = new FacadeContact(name, phoneNumber, email);
            contacts.add(domainContact);
            System.out.println("if finished, press Q");
            in = scanner.nextLine();
        }
        return contacts;
    }

    private FacadeBillsOfQuantities createBillOfQuantities() {

        Map<Integer, Integer> catalogToSerial = new HashMap<>();
        Map<Integer, Map<Integer, Double>> discountPerItem = new HashMap<>();
        Map<Integer, Double> discounts = new HashMap<>();
        Map<Integer, Double> includedItems = new HashMap<>();
        String in = "";
        int serialNumber, catalogNumber, amount;
        double discount, price;
        System.out.println(("Please enter the included items in the agreement. for each item, enter catalog number and serial number. When finished, press Q"));
        while (!in.equalsIgnoreCase("q")) {
            try {
                System.out.print("catalog number: ");
                catalogNumber = Integer.parseInt(scanner.nextLine());
                System.out.print("serial number: ");
                serialNumber = Integer.parseInt(scanner.nextLine());
                catalogToSerial.put(catalogNumber, serialNumber);
                System.out.print("item's price: ");
                price = Double.parseDouble(scanner.nextLine());
                includedItems.put(catalogNumber, price);

                System.out.println("Please enter discounts for the current item: if there are no discounts, press q, else, press any key");
                in = scanner.nextLine();
                while (!in.equalsIgnoreCase("q")) {
                    System.out.print("amount: ");
                    amount = getNextInt();
                    System.out.print("discount: ");
                    discount = Double.parseDouble(scanner.nextLine());
                    discounts.put(amount, discount);
                    discountPerItem.put(catalogNumber, discounts);

                    System.out.println("if finished enter discounts for the current item, press Q");
                    in = scanner.nextLine();
                }

            } catch (Exception e) {
                System.out.println((e.getMessage()));
            }
            System.out.println("if finished enter included items, press Q");
            in = scanner.nextLine();
        }

        Map<Integer, Double> discountPerOrder = getDiscountPerOrder();
        return new FacadeBillsOfQuantities(includedItems, discountPerOrder, discountPerItem, catalogToSerial);
    }

    private Map<Integer, Double> getDiscountPerOrder() {
        String in = "";
        int amount;
        double discount;
        Map<Integer, Double> discountPerOrder = new HashMap<>();
        System.out.println("Discount per orders: Please enter amount of items and then the discount. if finished or there are no more discounts - press q, else press any key");
        in = scanner.nextLine();
        while (!in.equalsIgnoreCase("q")) {
            try {
                System.out.println("amount:");
                amount = getNextInt();
                System.out.println("discount:");
                discount = Double.parseDouble(scanner.nextLine());
                discountPerOrder.put(amount, discount);
                System.out.println("if finished, press Q");

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            in = scanner.nextLine();
        }
        return discountPerOrder;
    }

    private void removeSupplier() {
        System.out.println("Please enter the supplierBN to remove:");
        try {
            int supplierBN = getNextInt();
            Response response = facade.removeSupplier(supplierBN);
            if (response.isErrorOccurred()) {
                System.out.println("operation failed. returning to the previous menu");
            } else {
                System.out.println("Supplier removed successfully.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("supplierBN format entered is wrong. Please try again.");
        }
    }

    private void setSupplier() {
        System.out.println("Please enter supplierBN to update:");
        try {
            int supplierBN = getNextInt();
            ResponseT<FacadeSupplier> response = facade.getSupplier(supplierBN);
            if (response.isErrorOccurred()) {
                System.out.println("operation failed. returning to the previous menu");
                return;
            }
            FacadeSupplier facadeSupplier = response.getValue();

            System.out.println("Please enter the requested field to change, and then it's updated value");
            System.out.println("1)name\n2)bank account\n3)terms of payment\n4)terms of supply\n");
            int choice = getNextInt();
            switch (choice) {
                case 1:
                    System.out.print("Please enter the new name: ");
                    facadeSupplier.name = scanner.nextLine();
                    break;
                case 2:
                    System.out.print("Please enter the new bank account: ");
                    facadeSupplier.bankAccount = getNextInt();
                    break;
                case 3:
                    System.out.print("Please enter the new term of payment : ");
                    System.out.println("Choose Payment Method:\n1) EOM30\n2) EOM60\n3) EOM90\n4) Cash ");
                    switch (scanner.nextLine()) {
                        case "1":
                            facadeSupplier.paymentTerms = Supplier.PaymentTerms.EOM30;
                            break;
                        case "2":
                            facadeSupplier.paymentTerms = Supplier.PaymentTerms.EOM60;
                            break;
                        case "3":
                            facadeSupplier.paymentTerms = Supplier.PaymentTerms.EOM90;
                            break;
                        case "4":
                            facadeSupplier.paymentTerms = Supplier.PaymentTerms.cash;
                            break;
                        default:
                            System.out.println("not a legal option. no changes are affected");
                    }
                    break;
                case 4:
                    System.out.print("Please enter the new term of supply: ");
                    System.out.println("Choose term of supply:\n1) Regular day shipment\n2) On demand shipment\n3) Self pickup");
                    switch (scanner.nextLine()) {
                        case "1":
                            facadeSupplier.termsOfSupply = createTermOfSupply();
                            facadeSupplier.needDelivery = false;
                            break;
                        case "2":
                            facadeSupplier.termsOfSupply = Collections.emptySet();
                            facadeSupplier.needDelivery = false;
                            break;
                        case "3":
                            facadeSupplier.termsOfSupply = Collections.emptySet();
                            facadeSupplier.needDelivery = true;
                            break;
                        default:
                            System.out.println("not a legal option. no changes are affected");
                    }
                    break;
                default:
                    System.out.println("the entered option in not supported.");
                    return;
            }
            Response r = facade.setSupplier(supplierBN, facadeSupplier);
            if (r.isErrorOccurred())
                System.out.println(response.errorMessage);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void setContactList(int supplierBN) {
        String name;
        String phoneNumber;
        String email;
        System.out.println("Please enter the requested operation:\n1)add contact\n2)remove contact\n3)update contact");
        int choice = getNextInt();
        switch (choice) {
            case 1:
                try {
                    System.out.println("contact's name:");
                    name = scanner.nextLine();
                    System.out.println("contact's phone number:");
                    phoneNumber = scanner.nextLine();
                    System.out.println("contact's email:");
                    email = scanner.nextLine();
                    Response r = facade.addContact(supplierBN, name, phoneNumber, email);
                    if (r.isErrorOccurred()) {
                        System.out.println(r.errorMessage);

                        return;
                    }
                    System.out.println("contact added successfully\n");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                break;
            case 2:
                try {
                    System.out.println("contact's name:");
                    name = scanner.nextLine();
                    Response r = facade.removeContact(supplierBN, name);
                    if (r.isErrorOccurred()) {
                        System.out.println(r.errorMessage);
                        System.out.println("operation failed. returning to the previous menu");
                        return;
                    }
                    System.out.println("contact removed successfully\n");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                break;
            case 3:
                try {
                    System.out.print("Enter name of the contact to edit:");
                    name = scanner.nextLine();
                    ResponseT<FacadeContact> r = facade.getContact(supplierBN, name);
                    if (r.isErrorOccurred())
                        System.out.println(r.errorMessage);
                    if (r.getValue() == null) {
                        System.out.println("no such contact in the contact list.");
                        break;
                    }
                    System.out.println(r.getValue());
                    System.out.print("Enter new phone number or left blank: ");
                    phoneNumber = scanner.nextLine();
                    phoneNumber = phoneNumber.isEmpty() ? r.getValue().phoneNumber : phoneNumber;
                    System.out.print("Enter new email or left blank: ");
                    email = scanner.nextLine();
                    email = email.isEmpty() ? r.getValue().email : email;
                    Response response = facade.updateContact(supplierBN, name, phoneNumber, email);
                    if (response.isErrorOccurred()) {
                        System.out.println(r.errorMessage);

                        return;
                    }
                    System.out.println("contact updated successfully\n");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                break;
        }
    }
}

