package Presentation_Layer;

import Bussiness_Layer.Report.Report;
import Facade_Layer.InventoryFacade;
import Facade_Layer.Response;
import Facade_Layer.SuppliersFacade;

import java.rmi.NoSuchObjectException;
import java.sql.SQLException;
import java.util.*;

public class MainInventoryMenu extends AbstractMenu{
    private InventoryFacade af;
    private SuppliersFacade _Saf;

    public MainInventoryMenu(InventoryFacade Iaf, SuppliersFacade Saf)
    {
        super(Saf,new Scanner(System.in),Iaf);
        af = Iaf;
        _Saf = Saf;

    }
    public void Start() throws NoSuchObjectException, SQLException {
        //while(iterator.hasNext())
        //   af.CreateItem(iterator.next());
        Scanner sc = new Scanner(System.in);
        String num;
        String str;
        String reportnum;
        Boolean flag;
        while(true) {
            Response response = null;
            flag = true;
            printMenu();
            str = "";
            List<String> inputList = new ArrayList<String>();
            num = sc.nextLine();
            switch (num) {

                case "1":
                    System.out.println("to create an new item you will need to specify the following information:\n\n" +
                            "enter item's name:");
                    //str += sc.nextLine();
                    inputList.add(sc.nextLine());
                    System.out.println("enter item's ID");
                    //str += " " + sc.nextLine();
                    inputList.add(sc.nextLine());
                    System.out.println("enter manufacturer's Name:");
                    //str += " " + sc.nextLine();
                    inputList.add(sc.nextLine());
                    System.out.println("enter minimum Quantity");
                    //str += " " + sc.nextLine();
                    inputList.add(sc.nextLine());
                    System.out.println("enter weight by grams:");
                    //str += " " + sc.nextLine();
                    inputList.add(sc.nextLine());
                    List<String> categories = new ArrayList<String>();
                    System.out.println("enter father category name:");
                    categories.add(sc.nextLine());
                    System.out.println("enter sub category, press q if done:");
                    String qFlag = sc.nextLine();
                    while(!qFlag.equals("q")) {
                        categories.add(qFlag);
                        System.out.println("enter sub category, press q if done:");
                        qFlag = sc.nextLine();
                    }
                    response = af.CreateItem(inputList,categories);
                    break;

                case "2":
                    System.out.println("to delete an item from the inventory you will need to specify item's id\n\n" +
                            "enter item's ID:");
                    str += sc.nextLine();
                    response = af.DeleteItem(str);
                    Response f_response = _Saf.removeItem(Integer.parseInt(str));
                    break;

                case "3":
                    while(1==1) {
                        inputList = new ArrayList<String>();
                        System.out.println("to update the amount of an item you will need to specify the following information:\n\n" +
                                "enter item's ID:");
                        //str += sc.nextLine();
                        inputList.add(sc.nextLine());
                        System.out.println("enter amount:");
                        //str += " " + sc.nextLine();
                        inputList.add(sc.nextLine());
                        //sc.close();
                        response = addItemsToLocationsInStore(inputList);
                        sc = new Scanner(System.in);
                        while(!response.errorMessage.equals("success\n"))
                        {
                            System.out.println(response.errorMessage + "\nTry again.");
                            inputList = new ArrayList<String>();
                            System.out.println("to update the amount of an item you will need to specify the following information:\n\n" +
                                    "enter item's ID:");
                            //str += sc.nextLine();
                            inputList.add(sc.nextLine());
                            System.out.println("enter amount:");
                            //str += " " + sc.nextLine();
                            inputList.add(sc.nextLine());
                            //sc.close();
                            response = addItemsToLocationsInStore(inputList);
                            sc = new Scanner(System.in);
                        }
                        System.out.println("Would you like to add another item? y/n");
                        String cont = sc.nextLine();
                        if(cont.equals("n"))
                            break;
                    }
                    System.out.println(af.CreateShortageReport("",_Saf).toString());
                    break;

                case "4":
                    System.out.println("to update supplement date of next shipment please enter the following information:\n\n" +
                            "enter item's ID:");
                    //str += sc.nextLine();
                    inputList.add(sc.nextLine());
                    System.out.println("enter date (dd-mm-yyyy):");
                    //str += " " + sc.nextLine();
                    inputList.add(sc.nextLine());
                    response = af.UpdateSupplementDate(inputList);
                    break;

                case "5":
                    System.out.println("please choose the type of report you would like to create:\n" +
                            "1) Damage Report\n" +
                            "2) Info Report\n" +
                            "3) Shortage Report");
                    reportnum = sc.nextLine();
                    Report r = null;
                    af.refresh();
                    switch (reportnum){
                        case "1":
                            r = af.CreateDamageReport(str);//empty str
                            break;
                        case "2":
                            System.out.println("to create Info Report you need to specify which category you want to be in the report" +
                                    "<category> or click enter to get all items info");
                            str += sc.nextLine();
                            r = af.CreateInfoReport(str);
                            break;
                        case "3":
                            r = af.CreateShortageReport(str,_Saf);//empty str
                            break;

                    }
                    System.out.println(r.toString());
                    break;
                case "6":
                    System.out.println("please enter the following info to make a sale:\n\n" +
                            "enter item's ID:");
                    //str += sc.nextLine();
                    inputList.add(sc.nextLine());
                    System.out.println("enter supplier's price:");
                    //str += " " + sc.nextLine();
                    inputList.add(sc.nextLine());
                    System.out.println("enter amount of items that was sold:");
                    //str += " " + sc.nextLine();
                    inputList.add(sc.nextLine());
                    System.out.println("enter expired date of the item (dd-mm-yyyy)");
                    //str += " " + sc.nextLine();
                    inputList.add(sc.nextLine());
                    response = af.AddSale(inputList);
                    break;

                case "7":
                    System.out.println("please enter the following info to report a damaged item:\n\n" +
                            "enter item's ID:");
                    inputList.add(sc.nextLine());//str += sc.nextLine();
                    System.out.println("enter amount of items that are damaged:");
                    inputList.add(sc.nextLine());//str += " " + sc.nextLine();
                    response = af.AddDamagedItem(inputList);
                    break;
                case "8":
                    System.out.println("Please choose item or category: i/c");
                    String op = sc.nextLine();
                    switch (op) {
                        case "i":
                            System.out.println("Please Enter the item's ID:");
                            inputList.add(sc.nextLine());//str += sc.nextLine();
                            System.out.println();
                            /*
                            0- name
                            1 - weight
                            2 - minimum Quantity
                            3 - manufacturerName
                            4 - location
                             */
                            System.out.println("Please enter option number you'd like to change for your item:\n" +
                                    "1) Name\n" +
                                    "2) Weight\n" +
                                    "3) Minimum Quantity\n" +
                                    "4) Manufacturer Name\n" +
                                    "5) Location\n" +
                                    "6) Current Price");
                            String op2 = sc.nextLine();
                            switch (op2) {
                                case "1":
                                    System.out.println("Please enter item's new name:");
                                    inputList.add(sc.nextLine());//str += " " + sc.nextLine();
                                    response = af.changeItemName(inputList);
                                    break;
                                case "2":
                                    System.out.println("Please enter item's new weight:");
                                    inputList.add(sc.nextLine());//str += " " + sc.nextLine();
                                    response = af.changeWeight(inputList);
                                    break;
                                case "3":
                                    System.out.println("Please enter item's new minimum quantity:");
                                    inputList.add(sc.nextLine());//str += " " + sc.nextLine();
                                    response = af.changeMinQunatity(inputList);
                                    break;
                                case "4":
                                    System.out.println("Please enter item's new Manufacturer Name:");
                                    inputList.add(sc.nextLine());//str += " " + sc.nextLine();
                                    response = af.changeManName(inputList);
                                    break;
                                case "5":
                                    System.out.println("Please enter the location you'd like to change <store/storage>");
                                    inputList.add(sc.nextLine());//str += " " + sc.nextLine();
                                    System.out.println("enter old aisle number");
                                    inputList.add(sc.nextLine());//str += " " + sc.nextLine();
                                    System.out.println("enter old shelf number");
                                    inputList.add(sc.nextLine());//str += " " + sc.nextLine();
                                    System.out.println("Please enter the NEW location you'd like to change <store/storage>");
                                    inputList.add(sc.nextLine());//str += " " + sc.nextLine();
                                    System.out.println("enter new aisle number");
                                    inputList.add(sc.nextLine());//str += " " + sc.nextLine();
                                    System.out.println("enter new shelf number");
                                    inputList.add(sc.nextLine());//str += " " + sc.nextLine();
                                    response = af.changeLocation(inputList);
                                    break;
                                case "6":
                                    System.out.println("Please enter item's new price:");
                                    inputList.add(sc.nextLine());//str += " " + sc.nextLine();
                                    response = af.changeCurrentPrice(inputList);
                                    break;
                                default:
                                    break;
                            }
                            break;

                        case "c":
                            List<String> catLst = new ArrayList<String>();
                            System.out.println("Please enter category name you'd like to change:");
                            catLst.add(sc.nextLine());
                            System.out.println("Please enter NEW category name:");
                            catLst.add(sc.nextLine());
                            response = af.changeSingleCategory(catLst);
                            break;

                    }
                    break;
                case "9":
                    System.out.println("Please enter option number:\n" +
                            "1) Add a new category and it's subs categories.\n" +
                            "2) Add a queue of categories.\n" +
                            "3) Delete category.");
                    String op3 = sc.nextLine();
                    switch(op3)
                    {
                        case "1":
                            List<String> catList = new ArrayList<String>();
                            System.out.println("Enter new category name");
                            catList.add(sc.nextLine());
                            String fatherCat = "";
                            System.out.println("Enter it's father category. press enter '!' if no father category exists");
                            fatherCat = sc.nextLine();
                            boolean whileFlag = true;
                            String tempCat = "";
                            while(whileFlag)
                            {
                                System.out.println("Enter a sub category and press enter. press '!' when done");
                                tempCat = sc.nextLine();
                                if(!tempCat.equals("!"))
                                    catList.add(tempCat);
                                else
                                    whileFlag = false;
                            }
                            if(fatherCat.equals("!"))
                                response = af.addCategoryWithManySons(null,catList);
                            else
                                response = af.addCategoryWithManySons(fatherCat,catList);
                            break;
                        case "2"://From zero or totally new - OFFFFFF
                            List<String> catList2 = new ArrayList<String>();
                            System.out.println("Enter category name");
                            catList2.add(sc.nextLine());
                            boolean whileFlag2 = true;
                            String tempCat2 = "";
                            while(whileFlag2)
                            {
                                System.out.println("Enter sub category and press enter. press '!' when done");
                                tempCat2 = sc.nextLine();
                                if(!tempCat2.equals("!"))
                                    catList2.add(tempCat2);
                                else
                                    whileFlag2 = false;
                            }
                            response = af.addFullCategoryTree(catList2);
                            break;
                        case "3":
                            System.out.println("Enter the category name you'd like to delete");
                            str += sc.nextLine();
                            response = af.DeleteSingleCategory(str);
                            break;
                    }
                    break;
                case "10":
                    return;

                default:
                    System.out.println("you have entered a wrong character");
            }
            if (response != null)
                System.out.println(response.errorMessage);
            System.out.println("--------------------------------------------\n\n");
        }
    }

    private Response addItemsToLocationsInStore(List<String> inputList) throws NoSuchObjectException {
        Scanner sc = new Scanner(System.in);
        System.out.println("enter location (write 'storage' for storage or type 'store' for store)");
        //str += " " + sc.nextLine();
        inputList.add(sc.nextLine());
        System.out.println("enter aisle number");
        inputList.add(sc.nextLine());
        System.out.println("enter shelf number");
        inputList.add(sc.nextLine());
        System.out.println("enter expired date of the item (dd-mm-yyyy)");
        //str += " " + sc.nextLine();
        inputList.add(sc.nextLine());
        //sc.close();
        return af.UpdateAmountOfItem(inputList);
    }

    /**
     *
     * @param itemsList - list of item's id
     * @return
     * @throws NoSuchObjectException
     */
    protected Response checkItemsFromDelivery(List<Integer> itemsList) throws NoSuchObjectException {
        //List<String> inputList = new ArrayList<String>();
        System.out.println("Please check the delivered items!");
        for (Integer entry: itemsList) {
            System.out.println("For item id: "+entry);
            System.out.println("Enter the amount arrived:");
            int totalAmount = getValidPositiveIntFromUser();
            System.out.println("Enter the number of damaged/expired items arrived - enter 0 if none");
            int amountToAdd = -1;
            while(amountToAdd < 0)
            {
                int userInput = getValidPositiveIntFromUser();
                amountToAdd = totalAmount - userInput;
                if(amountToAdd < 0)
                    System.out.println("Error - number of damaged items is bigger then total amount. Please enter a valid number.");

            }
            List<String> inputList = new ArrayList<String>();
            inputList.add(String.valueOf(entry));
            inputList.add(String.valueOf(amountToAdd));
            Response r =  addItemsToLocationsInStore(inputList);
            while(!r.errorMessage.equals("success\n"))
            {
                System.out.println(r.errorMessage + "\nTry again.");
                inputList = new ArrayList<String>();
                inputList.add(String.valueOf(entry));
                inputList.add(String.valueOf(amountToAdd));
                r =  addItemsToLocationsInStore(inputList);
            }

        }

        return new Response("Success. Added Items from Delivery\n");

    }

    private int getValidPositiveIntFromUser() {
        Scanner sc = new Scanner(System.in);
        int opUser = -1;
        try{
            opUser = sc.nextInt();
        }
        catch (Exception e) {
            System.out.println("Error - invalid number. Enter a valid number:");
            //sc.close();
            return getValidPositiveIntFromUser();
        }
        if(opUser < 0)
        {
            System.out.println("Number is negative. Enter a valid number:");
            //sc.close();
            return getValidPositiveIntFromUser();
        }
        //sc.close();
        return opUser;
    }


    public void printMenu()
    {
        System.out.println("welcome to inventory management system (IMS) please choose your action:\n" +
                "1) Add new item/s to the inventory\n" +
                "2) Delete item from inventory\n" +
                "3) Update stock of an item\n" +
                "4) Update Supplement Date\n" +
                "5) Create a report\n" +
                "6) add Sale\n" +
                "7) Report a damaged Item\n" +
                "8) Update Info\n" + //update category or item
                "9) Category\n" +
                "10) EXIT");
    }

    private int getIntFromString(String s) {
        return Integer.parseInt(s);

    }



}