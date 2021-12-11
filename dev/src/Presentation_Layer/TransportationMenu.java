package Presentation_Layer;


import Bussiness_Layer.ShiftPackage.ShiftType;
import Bussiness_Layer.TransportationPackage.Delivery;
import Bussiness_Layer.Tuple;
import DataLayer.DalObjects.DBController.DalController;
import Facade_Layer.FacadeObjects.*;
import Facade_Layer.Response;
import Facade_Layer.ResponseT;
import Facade_Layer.TransportationFacade;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class TransportationMenu {

    public TransportationMenu(TransportationFacade transportationFacade) {
    }

    public boolean mainMenu() {
        Scanner in = new Scanner(System.in);
        try {
            while (true) {
                System.out.println("Welcome to SuperLee Delivery System\n" +
                        "0) Logout\n" +
                        "1) Deliveries Menu \n" +
                        "2) Trucks Menu \n" +
                        "3) Locations Menu");
                int input = in.nextInt();
                switch (input) {
                    case 0:
                        return true;
                    case 1:
                        printDeliveryMenu();
                        break;
                    case 2:
                        printTruckMenu();
                        break;
                    case 3:
                        printLocationMenu();
                        break;
                    default:
                        System.out.println("please enter a valid input!");
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println("please enter a number");
            mainMenu();
        }
        return false;
    }

    public void printDeliveryMenu() {
        Scanner in = new Scanner(System.in);
        try {
            while (true) {
                System.out.println(
                        "0) Return \n" +
                                "1) Create New Delivery \n" +
                                "2) Remove Existent Delivery \n" +
                                "3) Create New Delivery Document \n" +
                                "4) Remove Existent Delivery Document \n" +
                                "5) Update Existent Delivery Document \n" +
                                "6) Print Delivery\n" +
                                "7) Create all Deliveries\n" +
                                "8) Print Delivery Document\n"+
                                "9) Cancel Delivery Request");
                int input = in.nextInt();
                switch (input) {
                    case 0:
                        return;
                    case 1:
                        addNewDelivery();
                        break;
                    case 2:
                        removeDelivery();
                        break;
                    case 3:
                        addNewDeliveryDoc();
                        break;
                    case 4:
                        removeDeliveryDocument();
                        break;
                    case 5:
                        printUpdateDeliveryDocumentMenu();
                        break;
                    case 6:
                        printDelivery();
                        break;
                    case 7:
                        createAllDeliveries();
                        break;
                    case 8:
                        printDeliveryDoc();
                        break;
                    case 9:
                        cancelDelivery();
                        break;
                    default:
                        System.out.println("enter valid option!");
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println("please enter a number\n");
            printDeliveryMenu();
        }
    }

    private void cancelDelivery() throws SQLException {
        Scanner in = new Scanner(System.in);
        System.out.println("please enter Delivery id to cancel: ");
        int id = in.nextInt();
        DalController.getDalController().setDeletePermissionLG(id);
    }


    private void createAllDeliveries() {
        try {
            List<ResponseT<Delivery>> deliveries = TransportationFacade.getInstance().createAllDeliveries();
            for (ResponseT<Delivery> del : deliveries) {
                if (del.isErrorOccurred())
                    System.out.println(del.errorMessage);
                else
                    System.out.println(del.getValue().toString() + "\n\n");
            }
        } catch (Exception e) {
            System.out.println("exception");
        }
    }

    private void printDeliveryDoc() {
        Scanner in = new Scanner(System.in);
        System.out.println("enter delivery id");
        int deliverID = in.nextInt();
        System.out.println("enter delivery document id");
        int id = in.nextInt();
        try {
            ResponseT<DeliveryDocFacade> responseT = TransportationFacade.getInstance().getDeliveryDoc(id, deliverID);
            if (responseT.isErrorOccurred())
                System.out.println(responseT.errorMessage);
            else
                System.out.println(responseT.getValue());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void printDelivery() {
        Scanner in = new Scanner(System.in);
        System.out.println("enter delivery id");
        int id = in.nextInt();
        try {
            ResponseT<DeliveryFacade> responseT = TransportationFacade.getInstance().getDeliveryLast(id);
            if (responseT.isErrorOccurred())
                System.out.println(responseT.errorMessage);
            else
                System.out.println(responseT.getValue());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void removeDeliveryDocument() {
        Scanner in = new Scanner(System.in);
        try {
            System.out.println("please enter delivery document id to remove");
            int id = in.nextInt();
            System.out.println("please enter delivery id to remove ");
            int deliverID = in.nextInt();
            Response response = TransportationFacade.getInstance().removeDeliveryDoc(id, deliverID);
            if (response.isErrorOccurred())
                System.out.println(response.errorMessage);
            else
                System.out.println("the delivery document removed successfully");
        } catch (Exception e) {
            System.out.print("one of the inputs was incorrect , please enter again\n");
            removeDeliveryDocument();
        }
    }

    private void addNewDeliveryDoc() {
        Scanner in = new Scanner(System.in);
        try {
            System.out.println("please enter delivery id");
            int deliveryID = in.nextInt();
            System.out.println("please enter document id");
            int docID = in.nextInt();
            System.out.println("please enter document destination");
            int dest = in.nextInt();
            List<SuppliesFacade> suppliesFacadeList = new ArrayList<>();
            while (true) {
                System.out.println("please enter the supply id ");
                int id = in.nextInt();
                SuppliesFacade suppliesFacade = TransportationFacade.getInstance().getSupply(id).getValue();
                boolean flag = false;
                for (SuppliesFacade sup : suppliesFacadeList) {
                    if (sup.id == suppliesFacade.id) {
                        System.out.println("the supply already in the list of supplies");
                        flag = true;
                    }
                }
                if (!flag)
                    suppliesFacadeList.add(suppliesFacade);
                System.out.println("would you like to insert another destination? \n" +
                        "enter \"y\" to enter new document or \"n\" to continue");
                String choice = in.next();
                if (choice.charAt(0) == 'n')
                    break;
            }
            ResponseT<DeliveryDocFacade> responseT = TransportationFacade.getInstance().addDocument(docID, dest, suppliesFacadeList, deliveryID);
            if (responseT.isErrorOccurred())
                System.out.println(responseT.errorMessage);
            else
                System.out.println("the document created successfully");
        } catch (Exception e) {
            System.out.print("one of the inputs was incorrect , please enter again\n");
            addNewDeliveryDoc();
        }
    }

    private void removeDelivery() {
        Scanner in = new Scanner(System.in);
        try {
            System.out.println("enter the delivery id you want to remove ");
            int id = in.nextInt();
            Response response = TransportationFacade.getInstance().removeDelivery(id);
            if (response.isErrorOccurred())
                System.out.println(response.errorMessage);
            else
                System.out.println("the requested delivery was removed successfully");
        } catch (Exception e) {
            System.out.print("one of the inputs was incorrect , please enter again\n");
            removeDelivery();
        }
    }

    private void addNewDelivery() {
        Scanner in = new Scanner(System.in);
        try {
            System.out.println("please enter the date in format YYYY-MM-DD or YYYYMMDD for example : 2020-02-02 or 20200202");
            System.out.println("please enter the departure day ");
            String departureDayString = in.next();
            LocalDate departureDay = !departureDayString.contains("-") ? LocalDate.parse(departureDayString.substring(0, 4) + "-" + departureDayString.substring(4, 6) + "-" + departureDayString.substring(6)) : LocalDate.parse(departureDayString);
            System.out.println("please enter the day of the week of the delivery , from 1 to 7");
            int day = in.nextInt() - 1;
            System.out.println("please enter 0 for morning shift or 1 for night shift");
            int shift = in.nextInt();
            System.out.println("please enter the truck license number ");
            int truckLicense = in.nextInt();
            System.out.println("please enter source id");
            int sourceID = in.nextInt();
            ResponseT<LocationFacade> source = TransportationFacade.getInstance().getLocation(sourceID);
            List<LocationFacade> locationFacadeList = new ArrayList<>();
            while (true) {
                System.out.println("please enter the destination id ");
                int id = in.nextInt();
                ResponseT<LocationFacade> location = TransportationFacade.getInstance().getLocation(id);
                locationFacadeList.add(location.getValue());
                System.out.println("would you like to insert another destination? \n" +
                        "enter \"y\" to enter new document or \"n\" to continue");
                char choice = in.next().charAt(0);
                if (choice == 'n')
                    break;
            }
            System.out.println("please enter the truck weight ");
            double truckWeight = in.nextDouble();
            Response response = TransportationFacade.getInstance().requestDeliveryManual(departureDay, truckLicense, source.getValue(), locationFacadeList, truckWeight, day, shift);
            if (response.isErrorOccurred())
                System.out.println(response.errorMessage);
            else
                System.out.println("created successfully");

        } catch (Exception e) {
            System.out.print("one of the inputs was incorrect , please enter again\n");
            addNewDelivery();
        }
    }

    private void printUpdateDeliveryDocumentMenu() {
        Scanner in = new Scanner(System.in);
        try {
            while (true) {
                System.out.println(
                        "0) Return \n" +
                                "1) Add New Supply To Document \n" +
                                "2) Remove Existent Supply From Document ");

                int input = in.nextInt();

                switch (input) {
                    case 0:
                        return;
                    case 1:
                        addNewSupplyToDocument();
                        break;
                    case 2:
                        removeSupplyFromDocument();
                        break;
                    default:
                        System.out.println("enter valid option!");
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println("please enter a number");
            printUpdateDeliveryDocumentMenu();
        }
    }

    private void removeSupplyFromDocument() {
        Scanner in = new Scanner(System.in);
        try {
            System.out.println("enter delivery id");
            int id = in.nextInt();
            System.out.println("enter document id");
            int docID = in.nextInt();
            System.out.println("enter supply id");
            int supplyID = in.nextInt();
            Response response = TransportationFacade.getInstance().removeSupplyFromDocument(id, docID, supplyID);
            if (response.isErrorOccurred())
                System.out.println(response.errorMessage);
            else
                System.out.println("supply removed successfully");
        } catch (Exception e) {
            System.out.print("one of the inputs was incorrect , please enter again\n");
            removeSupplyFromDocument();
        }

    }

    private void addNewSupplyToDocument() {
        Scanner in = new Scanner(System.in);
        try {
            System.out.println("enter delivery id");
            int id = in.nextInt();
            System.out.println("enter document id");
            int docID = in.nextInt();
            System.out.println("enter supply id");
            int supplyID = in.nextInt();
            Response response = TransportationFacade.getInstance().addNewSupplyToDoc(id, docID, supplyID);
            if (response.isErrorOccurred())
                System.out.println(response.errorMessage);
            else
                System.out.println("supply added successfully");
        } catch (Exception e) {
            System.out.print("one of the inputs was incorrect , please enter again\n");
            addNewSupplyToDocument();
        }
    }

    public void printTruckMenu() {
        Scanner in = new Scanner(System.in);
        try {
            while (true) {
                System.out.println(
                        "0) Return \n" +
                                "1) Create New Truck \n" +
                                "2) Remove Existent Truck \n" +
                                "3) Update Existent Truck \n" +
                                "4) Print Truck Details");
                int input = in.nextInt();
                switch (input) {
                    case 0:
                        return;
                    case 1:
                        addNewTruck();
                        break;
                    case 2:
                        removeTruck();
                        break;
                    case 3:
                        printTruckUpdateMenu();
                        break;
                    case 4:
                        printTruck();
                        break;
                    default:
                        System.out.println("enter valid option!");
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println("please enter a number");
            printTruckMenu();
        }
    }

    private void printTruck() {
        Scanner in = new Scanner(System.in);
        System.out.println("enter truck license id");
        int id = in.nextInt();
        try {
            ResponseT<TruckFacade> responseT = TransportationFacade.getInstance().getTruck(id);
            if (responseT.isErrorOccurred())
                System.out.println(responseT.errorMessage);
            else
                System.out.println(responseT.getValue());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void removeTruck() {
        Scanner in = new Scanner(System.in);
        try {
            System.out.println("please enter truck license number ");
            int truckID = in.nextInt();
            Response response = TransportationFacade.getInstance().removeTruck(truckID);
            if (response.isErrorOccurred())
                System.out.println(response.errorMessage);
            else
                System.out.println("the truck removed successfully");
        } catch (Exception e) {
            System.out.print("one of the inputs was incorrect , please enter again\n");
            removeTruck();
        }

    }

    private void addNewTruck() {
        Scanner in = new Scanner(System.in);
        try {
            System.out.println("please enter truck license number ");
            int license = in.nextInt();
            System.out.println("please enter truck model ");
            String model = in.next();
            System.out.println("please enter truck net weight ");
            double netWeight = in.nextDouble();
            System.out.println("please enter truck max weight ");
            double maxWeight = in.nextDouble();
            ResponseT<TruckFacade> responseT = TransportationFacade.getInstance().addTruck(license, model, netWeight, maxWeight);
            if (responseT.isErrorOccurred())
                System.out.println(responseT.errorMessage);
            else
                System.out.println("created new truck successfully");
        } catch (Exception e) {
            System.out.print("one of the inputs was incorrect , please enter again\n");
            addNewTruck();
        }
    }

    private void printTruckUpdateMenu() {
        Scanner in = new Scanner(System.in);
        try {
            while (true) {
                System.out.println(
                        "0) Return \n" +
                                "1) Update Truck Max Weight ");
                int input = in.nextInt();
                switch (input) {
                    case 0:
                        return;
                    case 1:
                        updateTruckMaxWeight();
                        break;
                    default:
                        System.out.println("please enter valid input ");
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println("please enter a number");
            printTruckUpdateMenu();
        }
    }

    private void updateTruckMaxWeight() {
        Scanner in = new Scanner(System.in);
        try {
            System.out.println("please enter truck license number ");
            int license = in.nextInt();
            System.out.println("enter truck max weight ");
            double maxWeight = in.nextDouble();
            Response response = TransportationFacade.getInstance().updateTruckMaxWeight(license, maxWeight);
            if (response.isErrorOccurred())
                System.out.println(response.errorMessage);
            else
                System.out.println("update max weight successfully");
        } catch (Exception e) {
            System.out.print("one of the inputs was incorrect , please enter again\n");
            updateTruckMaxWeight();
        }

    }

    public void printLocationMenu() {
        Scanner in = new Scanner(System.in);
        try {
            while (true) {
                System.out.println(
                        "0) Return \n" +
                                "1) Create New Location \n" +
                                "2) Remove Existent Location \n" +
                                "3) Update Existent Location \n" +
                                "4) Print Location Details");
                int input = in.nextInt();
                switch (input) {
                    case 0:
                        return;
                    case 1:
                        addNewLocation();
                        break;
                    case 2:
                        removeLocation();
                        break;
                    case 3:
                        printLocationUpdateMenu();
                        break;
                    case 4:
                        printLocation();
                        break;

                    default:
                        System.out.println("enter valid option!");
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println("please enter a number");
            printLocationMenu();
        }
    }

    private void printLocation() {
        Scanner in = new Scanner(System.in);
        System.out.println("enter location id");
        int id = in.nextInt();
        try {
            ResponseT<LocationFacade> responseT = TransportationFacade.getInstance().getLocation(id);
            if (responseT.isErrorOccurred())
                System.out.println(responseT.errorMessage);
            else
                System.out.println(responseT.getValue());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void removeLocation() {
        Scanner in = new Scanner(System.in);
        try {
            System.out.println("please enter location ID ");
            int id = in.nextInt();
            Response response = TransportationFacade.getInstance().removeLocation(id);
            if (response.isErrorOccurred())
                System.out.println("the location cannot be removed " + response.errorMessage);
            else
                System.out.println("the location removed successfully");
        } catch (Exception e) {
            System.out.print("one of the inputs was incorrect , please enter again\n");
            removeLocation();
        }
    }

    private void addNewLocation() {
        Scanner in = new Scanner(System.in);
        try {
            System.out.println("please enter location ID");
            String idConvert = in.nextLine();
            int id = Integer.parseInt(idConvert);
            System.out.println("please enter address ");
            String address = in.nextLine();
            System.out.println("please enter phone ");
            String phone = in.nextLine();
            System.out.println("please enter contact name ");
            String contactName = in.nextLine();
            System.out.println("section divided into 3\n" +
                    "1) south\n" +
                    "2) middle\n" +
                    "3) north");
            System.out.println("please enter section ");
            String sectionConvert = in.nextLine();
            int section = Integer.parseInt(sectionConvert);
            while (true) {
                if (!(section == 1 || section == 2 || section == 3)) {
                    System.out.println("please enter valid section ");
                    section = in.nextInt();
                } else
                    break;
            }
            ResponseT<LocationFacade> responseT = TransportationFacade.getInstance().addLocation(id, address, phone, contactName, section);
            if (responseT.isErrorOccurred())
                System.out.println(responseT.errorMessage);
            else
                System.out.println("location added successfully");
        } catch (Exception e) {
            System.out.print("one of the inputs was incorrect , please enter again\n");
            addNewLocation();
        }
    }

    private void printLocationUpdateMenu() {
        Scanner in = new Scanner(System.in);
        try {
            while (true) {
                System.out.println(
                        "0) Return \n" +
                                "1) Update Address \n" +
                                "2) Update Phone \n" +
                                "3) Update Contact Name ");
                int input = in.nextInt();
                switch (input) {
                    case 0:
                        return;
                    case 1:
                        updateLocationAddress();
                        break;
                    case 2:
                        updateLocationPhone();
                        break;
                    case 3:
                        updateLocationContactName();
                        break;
                    default:
                        System.out.println("enter valid option!");
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println("please enter a number");
            printLocationUpdateMenu();
        }
    }

    private void updateLocationContactName() {
        Scanner in = new Scanner(System.in);
        try {
            System.out.println("enter location ID ");
            int id = Integer.parseInt(in.nextLine());
            System.out.println("enter contact name ");
            String name = in.nextLine();
            Response response = TransportationFacade.getInstance().updateLocationContactName(id, name);
            if (response.isErrorOccurred())
                System.out.println(response.errorMessage);
            else
                System.out.println("location contact name update successfully");
        } catch (Exception e) {
            System.out.print("one of the inputs was incorrect , please enter again\n");
            updateLocationContactName();
        }
    }

    private void updateLocationPhone() {
        Scanner in = new Scanner(System.in);
        try {
            System.out.println("enter location ID ");
            int id = in.nextInt();
            System.out.println("enter phone ");
            String phone = in.next();
            Response response = TransportationFacade.getInstance().updateLocationPhone(id, phone);
            if (response.isErrorOccurred())
                System.out.println(response.errorMessage);
            else
                System.out.println("update phone number successfully");
        } catch (Exception e) {
            System.out.print("one of the inputs was incorrect , please enter again\n");
            updateLocationPhone();
        }
    }

    private void updateLocationAddress() {
        Scanner in = new Scanner(System.in);
        try {
            System.out.println("enter location ID ");
            int id = in.nextInt();
            System.out.println("enter address ");
            String address = in.next();
            Response response = TransportationFacade.getInstance().updateLocationAddress(id, address);
            if (response.isErrorOccurred())
                System.out.println(response.errorMessage);
            else
                System.out.println("update address successfully");
        } catch (Exception e) {
            System.out.print("one of the inputs was incorrect , please enter again\n");
            updateLocationAddress();
        }

    }

}
