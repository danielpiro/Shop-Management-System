package Bussiness_Layer.Item;


import Bussiness_Layer.Location.Location;
import DataLayer.DalObjects.*;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.nio.file.AccessDeniedException;
import java.rmi.NoSuchObjectException;
import java.sql.SQLException;
import java.util.*;

public class ItemController {

    private Map<Integer, Item> _itemsByID;
    private ItemHandler _iHandler;
    private DateHandler _dHandler;
    private LocationHandler _lHandler;
    private SaleHandler _sHandler;
    private int _currBranch;

    public ItemController(int branchID) throws SQLException {
        _itemsByID = new HashMap<Integer, Item>();
        _currBranch = branchID;
        _iHandler = new ItemHandler(_currBranch);
        _dHandler = new DateHandler(_currBranch);
        _lHandler = new LocationHandler(_currBranch);
        _sHandler = new SaleHandler(_currBranch);
        CreateTables();
    }
    private void CreateTables() throws SQLException {
        _iHandler.createItemsTable();
        _dHandler.createDateTable();
        _lHandler.createLocationTable();
        _sHandler.createSaleTable();
    }
    //
    public void createItem(List<String> itemInfo) {
        ItemInfo newItemInfo = new ItemInfo(itemInfo);
        Item i = new Item(newItemInfo);
        if(!_itemsByID.containsKey(i.getId())) {
            _itemsByID.putIfAbsent(newItemInfo.get_id(), i);
            _iHandler.insert(i);
        }
    }

    //deletes item from list
    public void deleteItem(int id) throws NoSuchObjectException //By ID maybe???
    {
        checkIfValidID(id);
        _iHandler.deleteAllByID(id);
        _lHandler.deleteAllByID(id);
        _sHandler.deleteAllByID(id);
        _dHandler.deleteAllByID(id);
        _itemsByID.remove(id);

    }

    //we can tell if to add or delete location from this function.
    //insert\delete location happens in Item
    public void updateAmountOfItem(int id, int amount, Location loc, Date date) throws NoSuchObjectException, AccessDeniedException//TODO: check if loc is empty || =0
    {
        //check if item exists

        Item i = null;
        if(_itemsByID.get(id)!=null)
            i = _itemsByID.get(id);
        else throw new ClassCastException("ID does not exist");


        for (Map.Entry<Integer,Item> entry : _itemsByID.entrySet())
        {
            if(entry.getValue().checkLocation(loc)&& entry.getKey().intValue()!=id)
            {
                throw new KeyAlreadyExistsException("Other Item is in aisle number "+
                            loc.getAisleNum()+" and shelf number "+loc.getShelfNum());
            }
        }
        boolean newLocFlag = i.checkLocation(loc);

        Location newLoc = i.updateLocation(amount, loc);
        i.updateCurrentQuantity(amount);
        _itemsByID.put(id, i);
        _iHandler.update(i);
        if(newLoc!=null) {
            if (!newLocFlag)
                _lHandler.insert(newLoc, i.getId());
            else _lHandler.update(newLoc);
        }
        else
            _lHandler.delete(loc);
        List<ItemDate> itemDates = _dHandler.getExpDateByID(i.getId());
        boolean flagFound = false;
        for(int j =0; j<itemDates.size();j++) {
            if (itemDates.get(j).getDate().compareTo(date) == 0) {
                flagFound = true;
                itemDates.get(j).addAmount(amount);
                if(itemDates.get(j).getAmount() ==0) {
                    _dHandler.delete(itemDates.get(j));
                    break;
                }
                _dHandler.update(itemDates.get(j));
                break;
            }
        }
        if(!flagFound)
            _dHandler.insert(new ItemDate(date,i.getId(),amount));
    }

    public void updateSupplementDate(int id, Date date) throws NoSuchObjectException {
        checkIfValidID(id);
        _itemsByID.get(id).updateNextSupplement(date);
        _iHandler.update(_itemsByID.get(id));

    }

    public Item getItem(int id) throws NoSuchObjectException {
        checkIfValidID(id);
        return _itemsByID.get(id);

    }

    private void checkIfValidID(int id) throws NoSuchObjectException {
        if(!_itemsByID.containsKey(id))
        {
            throw new NoSuchObjectException("invalid id");
        }
    }

    public Map<Integer, Item> get_itemsByID() {
        return _itemsByID;
    }

    public void LoadItems(int branchID) {
        _itemsByID = _iHandler.loadItems();
    }

    public Map<Item, Integer> getShortageItems() {
        Map<Integer,Integer> id_countMAP =_iHandler.getShortageItems();
        return convert_id_to_Item(id_countMAP);
    }

    //return map of <Item,amount of expired&dmged items>
    public Map<Item, Integer> get_dmgItems() {
        Map<Integer,Integer> dmgId  =_iHandler.get_dmgItems();
        Map<Item,Integer> dmgItems =  convert_id_to_Item(dmgId);
        Map<Item,Integer> expiredItems = convert_id_to_Item(_dHandler.getIDByExpDate());
        for (Map.Entry<Item,Integer> entry : dmgItems.entrySet()) {
            if(!expiredItems.containsKey(entry.getKey()))
                expiredItems.putIfAbsent(entry.getKey(),entry.getValue());
            else
            {
                Integer temp = expiredItems.get(entry.getKey());
                expiredItems.replace(entry.getKey(),entry.getValue() + temp);
            }
        }

        return expiredItems;

    }

    private Map<Item,Integer> convert_id_to_Item(Map<Integer,Integer> myMap){
        Map<Item,Integer> converterMap = new HashMap<Item,Integer>();
        for (Map.Entry<Integer,Integer> entry : myMap.entrySet()) {
            converterMap.putIfAbsent(_itemsByID.get(entry.getKey()),entry.getValue());
        }
        return converterMap;
    }


    public Map<Item, List<SaleInfo>> getItemsByCategory(List<String> lst) {
        Map<Item, List<SaleInfo>> myMap = new HashMap<Item,List<SaleInfo>>();
        if (lst == null || lst.isEmpty()){
            for (Map.Entry<Integer,Item> entry : _itemsByID.entrySet()) {
                List<SaleInfo> sales = new ArrayList<SaleInfo>();
                sales = _sHandler.getItemsSales(entry.getKey());
                myMap.putIfAbsent(entry.getValue(),sales);
            }
        }
        else
        {
            for (Map.Entry<Integer,Item> entry : _itemsByID.entrySet()) {
                for(int i = 0; i<lst.size(); i++) {
                    if (entry.getValue().getCategoryName().equals(lst.get(i))) {
                        List<SaleInfo> sales = new ArrayList<SaleInfo>();
                        sales = _sHandler.getItemsSales(entry.getKey());
                        myMap.putIfAbsent(entry.getValue(), sales);
                        break;
                    }
                }
            }
        }

        return myMap;
    }

    public Map<Item, List<SaleInfo>> getItemsByCategory(int weight) {
        Map<Item, List<SaleInfo>> myMap = new HashMap<Item, List<SaleInfo>>();
        for (Map.Entry<Integer,Item> entry : _itemsByID.entrySet()) {
            if (entry.getValue().getWeight() == weight) {
                List<SaleInfo> sales = new ArrayList<SaleInfo>();
                sales = _sHandler.getItemsSales(entry.getKey());
                myMap.putIfAbsent(entry.getValue(),sales);
            }
        }

        return myMap;
    }

    public void saveSale(SaleInfo si) {
        _sHandler.insert(si);

    }

    public void addDamageItems(int id, int amount)
    {
        Item item = this._itemsByID.get(id);
        item.update_numOfDmgItems(amount);
        _iHandler.update(item);
    }

    public void updateName(int id, String name) throws NoSuchObjectException {
        Item item = getItem(id);
        item.setName(name);
        _iHandler.update(item);
    }

    public void updateManName(int id, String ManName) throws NoSuchObjectException {
        Item item = getItem(id);
        item.setManName(ManName);
        _iHandler.update(item);
    }

    public void updateWeight(int id, int weight) throws NoSuchObjectException {
        Item item = getItem(id);
        item.setWeight(weight);
        _iHandler.update(item);
    }

    public void updateMinQuantity(int id, int minAmount) throws NoSuchObjectException {
        Item item = getItem(id);
        item.setMinQuantity(minAmount);
        _iHandler.update(item);
    }

    public void updateCurrentPrice(int id, double price) throws NoSuchObjectException {
        Item item = getItem(id);
        item.setCurrentPrice(price);
        _iHandler.update(item);
    }

    public void updateLocation(int id, Location oldLoc, Location newLoc) throws NoSuchObjectException {
        Item item = getItem(id);
        if(item.checkLocation(oldLoc) && !item.checkLocation(newLoc))
        {
            for (Map.Entry<Integer,Item> entry : _itemsByID.entrySet())
            {
                if(entry.getValue().checkLocation(newLoc))
                {
                    throw new KeyAlreadyExistsException("Other Item is in aisle number "+
                            newLoc.getAisleNum()+" and shelf number "+newLoc.getShelfNum());
                }
            }
            _lHandler.update(oldLoc,newLoc);
            newLoc.setAmount(item.getAmountInLocation(oldLoc));
            item.deleteLocation(oldLoc);
            item.addNewLoc(newLoc);
        }
    }

    public void changeAllItemsCategory(String oldCat, String newCat)
    {
        for (Map.Entry<Integer,Item> entry : _itemsByID.entrySet()) {
            if(entry.getValue().getCategoryName().equals(oldCat))
            {
                entry.getValue().setCategoryName(newCat);
                _iHandler.update(entry.getValue());
            }
        }
    }

    public boolean DoesCategoryExists(String name)
    {
        for (Map.Entry<Integer,Item> entry : _itemsByID.entrySet())
            if(entry.getValue().getCategoryName().equals(name))
                return true;
            return false;

    }

    public void RemoverExpiredFromCurrent() {
        Map<Item,Integer> expiredItems = convert_id_to_Item(_dHandler.getIDByExpDate());
        for (Map.Entry<Item,Integer> entry : expiredItems.entrySet()) {
            entry.getKey().set_expiredItemNum(entry.getValue());

        }
    }
}
