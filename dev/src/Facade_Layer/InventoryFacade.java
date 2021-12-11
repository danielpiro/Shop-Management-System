package Facade_Layer;

import Bussiness_Layer.Category.CategoryController;
import Bussiness_Layer.Item.Item;
import Bussiness_Layer.Item.ItemController;
import Bussiness_Layer.Location.Location;
import Bussiness_Layer.Location.StorageLocation;
import Bussiness_Layer.Location.StoreLocation;
import Bussiness_Layer.Report.DamageReport;
import Bussiness_Layer.Report.InfoReport;
import Bussiness_Layer.Report.ReportMaker;
import Bussiness_Layer.Report.ShortageReport;
import Bussiness_Layer.TransportationPackage.Delivery;
import DataLayer.DalObjects.*;
import DataLayer.DalObjects.DBController.DalController;
import Facade_Layer.FacadeObjects.DeliveryFacade;

import java.rmi.NoSuchObjectException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeoutException;

public class InventoryFacade {
    private ReportMaker _rp;
    private ItemController _ic;
    private CategoryController _cc;
    private int _branchID;

    public InventoryFacade(int branchID) throws SQLException {
        _rp = new ReportMaker();
        _ic = new ItemController(branchID);
        _cc = new CategoryController();
        _branchID = branchID;
        loadControllers(branchID);
    }

    private void loadControllers(int branchID) {
        _cc.LoadCategories();
        _ic.LoadItems(branchID);
    }

    public ShortageReport CreateShortageReport(String s, SuppliersFacade Saf) throws SQLException {
        Map<Item, Integer> itemList = _ic.getShortageItems();
        Map<Integer,Integer> weights = new HashMap<>();
        if (!itemList.isEmpty()) {
            Map<Integer, Integer> IdList = new HashMap<Integer, Integer>();
            itemList.keySet().stream().forEach(x -> IdList.put(x.getId(), x.getMinimumQuantity()+200));
            itemList.keySet().forEach(x-> weights.put(x.getId(),x.getWeight()*IdList.get(x.getId())));
            Saf.createShortageOrder(IdList, _branchID, weights);

        }

        return _rp.CreateShortageReport(itemList);
    }

    public DamageReport CreateDamageReport(String s) {
        Map<Item, Integer> dmgItems = _ic.get_dmgItems();
        return _rp.CreateDamageReport(dmgItems);
    }

    public InfoReport CreateInfoReport(String s) {
        Map<Item, List<SaleInfo>> mapItem = null;
        if (s.isEmpty() || s == null || s == "") {
            mapItem = _ic.getItemsByCategory(null);
            return _rp.CreateInfoReport(mapItem, null);
        } else {
            List<String> catLst = new ArrayList<String>();
            try {
                int w = Integer.parseInt(s);
                catLst.add(s);
                mapItem = _ic.getItemsByCategory(w);

            } catch (Exception e) //means real category
            {
                catLst = _cc.getAllSubCategory(s);
                if(catLst.isEmpty()) {
                    List<String> noCat = new ArrayList<String>();
                    noCat.add(s);
                    return _rp.CreateInfoReport(mapItem, noCat);
                }
                mapItem = _ic.getItemsByCategory(catLst);

            }
            return _rp.CreateInfoReport(mapItem, catLst);

        }
    }
     public String getName(int serialNumber){
        try {
            Item item = _ic.getItem(serialNumber);
            return item.getItemName();
        }
        catch(NoSuchObjectException e)
        {
            return null;
        }
     }

    public Response CreateItem(List<String> inputList, List<String> categoryList) {
      //  List<String> inputList = ConvertStringToList(s);
        if (!_cc.checkIfTreeExists(categoryList))
            return new Response("Category tree does not exists");

        try {
            inputList.add(categoryList.get(categoryList.size() - 1)); //represent lowest category
            _ic.createItem(inputList);
        } catch (Exception e) {
            if (e instanceof NumberFormatException)
                return new Response(e.getMessage());
            return new Response(e.getMessage());
        }
        return new Response("success");
    }

    public Response DeleteItem(String s) //By ID maybe???
    {
        int id = -1;
        try {
            if (isInteger(s))
                id = Integer.parseInt(s);
        } catch (Exception e) {
            return new Response("invalid ID number");
        }
        try {
            _ic.deleteItem(id);
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
        return new Response("success");

    }

    public Response UpdateAmountOfItem(List<String> inputList) throws NoSuchObjectException//TODO: check if loc is empty || =0
    {
        //List<String> inputList = ConvertStringToList(s);
        int id = -1;
        int amount = 0;
        Location loc;
        ListIterator<String> iterator = inputList.listIterator();
        //checks for item's id
        try {
            if (iterator.hasNext()) {
                String temp = iterator.next();
                id = getIntFromString(temp);
            } else {
                throw new ArrayIndexOutOfBoundsException("input is empty");
            }
        } catch (Exception e) {
            if (e instanceof ArrayIndexOutOfBoundsException)
                return new Response(e.getMessage());
            return new Response("invalid ID");
        }

        //checks for the amount to add\remove
        try {
            if (iterator.hasNext()) {
                String temp = iterator.next();
                amount = getIntFromString(temp);
            } else {
                throw new ArrayIndexOutOfBoundsException("input is bad at amount");
            }
        } catch (Exception e) {
            if (e instanceof ArrayIndexOutOfBoundsException)
                return new Response(e.getMessage());
            return new Response("invalid amount");
        }
        //checks if its Storage or in Store
        if (iterator.hasNext()) {
            String locType = iterator.next();
            int aisle = -1;
            int shelf = -1;
            String temp = "";
            try {
                if (iterator.hasNext())
                    temp = iterator.next();
                else throw new ArrayIndexOutOfBoundsException("invalid input at location");
                aisle = getIntFromString(temp);
                if (iterator.hasNext())
                    temp = iterator.next();
                else throw new ArrayIndexOutOfBoundsException("invalid input at location");
                shelf = getIntFromString(temp);

            } catch (Exception e) {
                if (e instanceof ArrayIndexOutOfBoundsException)
                    return new Response(e.getMessage());
                return new Response("invalid aisle or shelf");
            }
            if (locType.equals("store"))
                loc = new StoreLocation(aisle, shelf);
            else if (locType.equals("storage"))
                loc = new StorageLocation(aisle, shelf);
            else throw new ArrayIndexOutOfBoundsException("invalid location type");

            //make DATE
            String dateString = "";
            Date date = null;
            try {
                if (iterator.hasNext())
                    dateString = iterator.next();
                else {
                    throw new ArrayIndexOutOfBoundsException("invalid input at date");
                }
                date = MakeDateFromString(dateString);
            } catch (Exception e) {
                if (e instanceof ArrayIndexOutOfBoundsException)
                    return new Response(e.getMessage());
                return new Response("invalid date");
            }

            try {
                _ic.updateAmountOfItem(id, amount, loc, date);
            } catch (Exception e) {
                return new Response(e.getMessage());
            }
        }
        return new Response("success\n");
    }

    public Response UpdateSupplementDate(List<String> inputList) {
        int id = -1;
        Date date = null;
        ListIterator<String> iterator = inputList.listIterator();
        try {
            if (iterator.hasNext())
                id = getIntFromString(iterator.next());
            else {
                throw new ArrayIndexOutOfBoundsException("invalid input at ID");
            }
        } catch (Exception e) {
            if (e instanceof NumberFormatException) {
                return new Response("invalid ID");
            }
            return new Response(e.getMessage());
        }
        try {
            if (iterator.hasNext())
                date = MakeDateFromString(iterator.next());

            else {
                throw new ArrayIndexOutOfBoundsException("invalid input at date");
            }
            Date now = new Date();
            if (date.before(now))
                throw new TimeoutException("this date already passed");
            _ic.updateSupplementDate(id, date);
        } catch (Exception e) {
            if (e instanceof ClassCastException) {
                return new Response("ID does not exist");
            }
            return new Response(e.getMessage());

        }
        return new Response("success");
    }

    public Response AddSale(List<String> inputList) {
        int id = -1;
        double priceBought = -1;
        double priceSold = -1;
        int amount = 0;
        ListIterator<String> iterator = inputList.listIterator();
        //check id
        try {
            if (iterator.hasNext())
                id = getIntFromString(iterator.next());
            else {
                throw new ArrayIndexOutOfBoundsException("invalid input at ID");
            }
        } catch (Exception e) {
            if (e instanceof NumberFormatException)
                return new Response("invalid ID");
            return new Response(e.getMessage());
        }

        //check price bought
        try {
            if (iterator.hasNext())
                priceBought = Double.parseDouble(iterator.next());
            else {
                throw new ArrayIndexOutOfBoundsException("invalid input at Price bought");
            }
        } catch (Exception e) {
            if (e instanceof NumberFormatException)
                return new Response("invalid price bought");
            return new Response(e.getMessage());
        }

        //check amount to update
        try {
            if (iterator.hasNext())
                amount = getIntFromString(iterator.next());

            else {
                throw new ArrayIndexOutOfBoundsException("invalid input at amount");
            }

        } catch (Exception e) {
            if (e instanceof NumberFormatException)
                return new Response("invalid amount");
            return new Response(e.getMessage());
        }
        String dateString = "";
        Date date = null;
        try {
            if (iterator.hasNext())
                dateString = iterator.next();
            else {
                throw new ArrayIndexOutOfBoundsException("invalid input at date");
            }
            date = MakeDateFromString(dateString);
        } catch (Exception e) {
            if (e instanceof ArrayIndexOutOfBoundsException)
                return new Response(e.getMessage());
            return new Response("invalid date");
        }
        Item item = null;
        try {
            item = _ic.getItem(id);
        } catch (NoSuchObjectException e) {
            return new Response("item does not exists by id");
        }
        priceSold = item.get_current_price();
        SaleInfo si = new SaleInfo(id, amount, priceBought, priceSold, date);
        _ic.saveSale(si);

        return new Response("success");
    }

    public Response AddDamagedItem(List<String> inputList) {
        int id = -1;
        int amount = -1;
        ListIterator<String> iterator = inputList.listIterator();
        try {
            if (iterator.hasNext())
                id = getIntFromString(iterator.next());
            else {
                throw new ArrayIndexOutOfBoundsException("invalid input at ID");
            }
        } catch (Exception e) {
            if (e instanceof NumberFormatException)
                return new Response("invalid ID");
            return new Response(e.getMessage());
        }
        try {
            if (iterator.hasNext())
                amount = getIntFromString(iterator.next());
            else {
                throw new ArrayIndexOutOfBoundsException("invalid input at amount");
            }
        } catch (Exception e) {
            if (e instanceof NumberFormatException)
                return new Response("invalid amount");
            return new Response(e.getMessage());
        }
        try {
            _ic.addDamageItems(id, amount);
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
        return new Response("success");
    }

    private int getIntFromString(String s) {
        return Integer.parseInt(s);

    }

    private List<String> ConvertStringToList(String s) {
        return new ArrayList<String>(Arrays.asList(s.split(" ")));
    }

    private Date MakeDateFromString(String s) {
        List<String> list = new ArrayList<String>(Arrays.asList(s.split("-")));
        if (list.size() != 3) {
            throw new ArrayIndexOutOfBoundsException("Bad Date");
        }
        int day = getIntFromString(list.get(0));
        if (day <= 0 || day > 31)
            throw new ArrayIndexOutOfBoundsException("Bad Day in date");
        int month = getIntFromString(list.get(1));
        if (month <= 0 || month > 12)
            throw new ArrayIndexOutOfBoundsException("Bad Month in date");
        int year = getIntFromString(list.get(2));
        return parseDate(s);
    }

    private boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    private Date parseDate(String date) {
        try {
            return new SimpleDateFormat("dd-MM-yyyy").parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    public Response CreateCategoryTree(List<String> catLst) {
        if (_cc.AddNewCategory(catLst))
            return new Response("success");
        else return new Response("Category tree already exists");
    }

    public Response changeItemName(List<String> inputList) {
        int id = -1;
        try {
            if (isInteger(inputList.get(0)))
                id = Integer.parseInt(inputList.get(0));
        } catch (Exception e) {
            return new Response("invalid ID number");
        }
        try {
            if (inputList.size() == 2)
                _ic.updateName(id, inputList.get(1));
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
        return new Response("success");
    }

    public Response changeManName(List<String> inputList) {
        int id = -1;
        try {
            if (isInteger(inputList.get(0)))
                id = Integer.parseInt(inputList.get(0));
        } catch (Exception e) {
            return new Response("invalid ID number");
        }
        try {
            if (inputList.size() == 2)
                _ic.updateManName(id, inputList.get(1));
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
        return new Response("success");
    }

    public Response changeMinQunatity(List<String> inputList) {
        int id = -1;
        int min = -1;

        try {
            if (isInteger(inputList.get(0)))
                id = Integer.parseInt(inputList.get(0));
        } catch (Exception e) {
            return new Response("invalid ID number");
        }

        try {
            if (inputList.size() == 2 && isInteger(inputList.get(1)))
                min = Integer.parseInt(inputList.get(1));
        } catch (Exception e) {
            return new Response("invalid min amount number");
        }

        try {
            _ic.updateMinQuantity(id, min);
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
        return new Response("success");
    }

    public Response changeWeight(List<String> inputList) {
        int id = -1;
        int weight = -1;

        try {
            if (isInteger(inputList.get(0)))
                id = Integer.parseInt(inputList.get(0));
        } catch (Exception e) {
            return new Response("invalid ID number");
        }

        try {
            if (inputList.size() == 2 && isInteger(inputList.get(1)))
                weight = Integer.parseInt(inputList.get(1));
        } catch (Exception e) {
            return new Response("invalid weight number");
        }

        try {
            _ic.updateWeight(id, weight);
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
        return new Response("success");
    }

    public Response changeCurrentPrice(List<String> inputList) {
        int id = -1;
        double price = -1;

        try {
            if (isInteger(inputList.get(0)))
                id = Integer.parseInt(inputList.get(0));
        } catch (Exception e) {
            return new Response("invalid ID number");
        }

        try {
            if (inputList.size() == 2)
                price = Double.parseDouble(inputList.get(1));
        } catch (Exception e) {
            return new Response("invalid price");
        }

        try {
            _ic.updateCurrentPrice(id, price);
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
        return new Response("success");
    }

    public Response changeLocation(List<String> inputList) {
        if(inputList.size()!=7)
            return new Response("invalid location to change - size is not 7");
        int id = -1;
        String locType = inputList.get(1);
        int aisle = -1;
        int shelf = -1;
        try {
            if (isInteger(inputList.get(0)))
                id = Integer.parseInt(inputList.get(0));
        } catch (Exception e) {
            return new Response("invalid ID number");
        }

        try {
            if (isInteger(inputList.get(2)))
                aisle = Integer.parseInt(inputList.get(2));
        } catch (Exception e) {
            return new Response("invalid aisle number");
        }

        try {
            if (isInteger(inputList.get(3)))
                shelf = Integer.parseInt(inputList.get(3));
        } catch (Exception e) {
            return new Response("invalid shelf number");
        }
        Location oldLoc = null;
        if(locType.equals("store"))
            oldLoc = new StoreLocation(aisle,shelf);
        else if(locType.equals("storage"))
            oldLoc = new StorageLocation(aisle,shelf);
        if(oldLoc == null)
            return new Response("invalid location type");

        //*******new location now********
        locType = inputList.get(4);
        try {
            if (isInteger(inputList.get(5)))
                aisle = Integer.parseInt(inputList.get(5));
        } catch (Exception e) {
            return new Response("invalid aisle number");
        }

        try {
            if (isInteger(inputList.get(6)))
                shelf = Integer.parseInt(inputList.get(6));
        } catch (Exception e) {
            return new Response("invalid shelf number");
        }
        Location newLoc = null;
        if(locType.equals("store"))
            newLoc = new StoreLocation(aisle,shelf);
        else if(locType.equals("storage"))
            newLoc = new StorageLocation(aisle,shelf);
        if(newLoc == null)
            return new Response("invalid location type");
        try {
            _ic.updateLocation(id, oldLoc,newLoc);
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
        return new Response("success");
    }

    public Response changeSingleCategory(List<String> inputList)
    {
        if(inputList.size()!=2 || inputList.get(0)==null || inputList.get(0).equals("") ||
                inputList.get(1)==null || inputList.get(1).equals(""))
            return new Response("For some reason input to change category is more then 2 or invalid");
        String oldCat = inputList.get(0);
        String newCat = inputList.get(1);
        if(!_cc.updateSingleCategory(oldCat,newCat))
            return new Response(oldCat + " does not exists");
        _ic.changeAllItemsCategory(oldCat, newCat);
        return new Response("Great Success");
    }

    public Response addCategoryWithManySons(String fatherCat, List<String> catList) {
        if(fatherCat == null)
            _cc.InsertFatherCategoryWithSons(catList);
        else
            _cc.InsertCategoryWithSons(fatherCat, catList);
        return new Response("Great Success");

    }

    public Response DeleteSingleCategory(String s)
    {
        if(_ic.DoesCategoryExists(s))
            return new Response("Can't delete category. Item's exists with this category");
        _cc.deleteCategory(s);
        return new Response("Great Success");
    }

    public Response addFullCategoryTree(List<String> catList2) {
        if(catList2.isEmpty())
            return new Response("ban zona aich harshima rika?!?!");
        _cc.AddNewCategory(catList2);
        return new Response("HUMONGOUS SUCCESS");
    }

    public int get_branchID(){
        return _branchID;
    }

    public Integer get_weight(int item_id){
        try{
            Item i = _ic.getItem(item_id);
            return i.getWeight();
        }catch (NoSuchObjectException e) {
            return null;
        }
    }

    public void refresh()
    {
        _ic.RemoverExpiredFromCurrent();
    }

    public void updateNextSupDateByID(int id, LocalDate date) throws NoSuchObjectException {
        ZoneId defaultZoneId = ZoneId.systemDefault();
        Date myDate = Date.from(date.atStartOfDay(defaultZoneId).toInstant());
        _ic.updateSupplementDate(id,myDate);
    }

    public void cancelRequest(String id) throws SQLException {
        int realID = getIntFromString(id);
        DalController.getDalController().setDeletePermissionIN(realID);
    }


//    //returns Item's ID and amount to add
//    public Map<Integer,Integer> addItemsFromDelivery(List<String> inputList) throws SQLException, NoSuchObjectException {
//        int deliveryID = getIntFromString(inputList.get(0));
//        DalDelivery delivery = DalController.getDalController().getDalDelivery(deliveryID);
//        List<DalSupply> dItems = null;
//        for(int i = 0; i<delivery.getDestination().size(); i++)
//        {
//            DalLocation loc = delivery.getDestination().get(i);
//            if(loc.getId() == _branchID) {
//                dItems = delivery.getDocs().get(i).getSupplies();
//                break;
//            }
//        }
//        if(dItems == null)
//            throw new NullPointerException("branch id was not found in this delivery");//return new Response("branch id was not found in this delivery");
//        if(dItems.isEmpty())
//            throw new EmptyStackException();//return new Response("No items arrived in this delivery");
//        Map<Integer,Integer> itemsMap = new HashMap<Integer,Integer>();
//        for(DalSupply dItem : dItems){
//            //TODO check if amount > 0
//            itemsMap.putIfAbsent(dItem.getId(),-1); //TODO: 10 is zakif - ASK DANIEL & BAR!
//        }
//        return itemsMap;
//
//    }
}
