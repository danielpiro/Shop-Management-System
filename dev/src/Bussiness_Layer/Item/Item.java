package Bussiness_Layer.Item;

import Bussiness_Layer.Location.Location;

import java.nio.file.AccessDeniedException;
import java.rmi.NoSuchObjectException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Item {
    private ItemInfo info;
    private Date _nextSupplement;
    private List<Location> _locationList;
    private int _currentQuantity;
    private int _numOfDmgItems;
    private int _expiredItemNum;


    public Item(ItemInfo info) {//, Date _nextSupplement, int _currentQuantity) {
        this.info = info;
        this._nextSupplement = null;
        this._locationList = new ArrayList<>();
        this._currentQuantity = 0;
        this._numOfDmgItems = 0;
        this._expiredItemNum = 0;
    }

    public void setMinQuantity(int min)
    {
        this.info.set_minimumQuantity(min);
    }

    public void setWeight(int w)
    {
        this.info.set_weight(w);
    }

    public void setName(String name)
    {
        this.info.set_itemName(name);
    }

    public void setManName(String ManName)
    {
        this.info.set_manufacturerName(ManName);
    }

    public void set_expiredItemNum(int _expiredItemNum) {
        this._expiredItemNum = _expiredItemNum;
    }

    public void setCurrentPrice(double currentPrice) {
        this.info.setCurrentPrice(currentPrice);
    }

    public int getWeight()
    {
        return info.get_weight();
    }

    //return true if loc exists
    public boolean checkLocation(Location loc) {
        ListIterator<Location> iterator = _locationList.listIterator();
        while (iterator.hasNext()) {
            if (iterator.next().isEqual(loc))
                return true;
        }
        return false;
    }

    public String getItemName() {
        return info.get_itemName();
    }

    public Location updateLocation(int amount, Location loc) throws NoSuchObjectException, AccessDeniedException {

        ListIterator<Location> iterator = _locationList.listIterator();
        boolean locIsFound = false;
        Location locToRet = null;
        while (!locIsFound && iterator.hasNext()) {
            Location currLoc = iterator.next();
            if (currLoc.isEqual(loc)) {
                locIsFound = true;
                if (currLoc.getAmount() + amount < 0) {
                    throw new AccessDeniedException("not enough items in that location");
                }
                currLoc.AddAmount(amount);
                locToRet = currLoc;
                if (currLoc.getAmount() == 0) {
                    deleteLocation(currLoc);
                    return null;
                }
            }
        }
        if (!locIsFound && amount > 0) {
            loc.AddAmount(amount);
            insertNewLocation(loc);
            locToRet = loc;
            return locToRet;
        } else {
            //in case we want to remove items from location that isnt exists
            if (!locIsFound && amount <= 0)
                throw new AccessDeniedException("location does not exist");
        }
        return locToRet;

    }

    //adds new location to item
    private void insertNewLocation(Location loc) {
        _locationList.add(loc);
    }

    //deletes a location
    public void deleteLocation(Location loc) throws NoSuchObjectException {

        List<Location> newLocList = new ArrayList<Location>();
        boolean flag = false;
        for(Location element : this._locationList)
        {
            if(!element.isEqual(loc))
                newLocList.add(element);
            else
                flag = true;
        }
        if (!flag)
            throw new NoSuchObjectException("Item does not exists in this location");
        this._locationList = newLocList;

        }


    //updates amount of items
    public void updateCurrentQuantity(int amount) {
        _currentQuantity = _currentQuantity + amount;
    }

    //updates next supplement date
    public void updateNextSupplement(Date date) {
        _nextSupplement = date;
    }

    public void update_numOfDmgItems(int _numOfDmgItems) {
        if (this._numOfDmgItems + _numOfDmgItems < 0)
            throw new InputMismatchException("You are trying to remove " +
                    _numOfDmgItems + " damaged items when there are only " + this._numOfDmgItems + " damaged items");
        this._numOfDmgItems = this._numOfDmgItems + _numOfDmgItems;
    }

    public int get_numOfDmgItems() {
        return _numOfDmgItems;
    }

    public int getMinimumQuantity() {
        return info.get_minimumQuantity();
    }

    @Override
    public String toString() {
        String str = info.toString();
        str = str + "\r\nCurrent Quantity: " + (_currentQuantity) + "\r\nNumber of Damaged/Expired Items: " + (_numOfDmgItems+_expiredItemNum) +
                "\r\nLocations:";
        int i = 1;
        ListIterator<Location> iterator = _locationList.listIterator();
        if (!iterator.hasNext())
            str = str + "\r\nItems is not in store or storage";
        else {
            while (iterator.hasNext()) {
                str = str + "\r\n" + i + ". " + iterator.next().toString();
                i++;
            }
        }

        if (_nextSupplement != null) {
            DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            String time = df.format(_nextSupplement);
            str = str + "\r\nNext Supplement Date: " + time + "\n";
        }
        else
            str = str + "\r\nNext Supplement Date: no orders has been made yet\n";
        return str;

    }

    public boolean compareTo(Item item) {
        return this.getId() == item.getId();
    }

    public int getId() {
        return info.get_id();
    }

    public Date get_nextSupplement() {
        return _nextSupplement;
    }

    public int get_currentQuantity() {
        return this._currentQuantity;
    }

    public String get_manufacturerName(){
        return this.info.getManufacturerName();
    }

    public double get_current_price(){ return this.info.getCurrentPrice();}

    public String getCategoryName()
    {
        return info.getCatName();
    }

    public boolean isLocationEmpty()
    {
        return _locationList.isEmpty();
    }

    public void addNewLoc(Location loc)
    {
        this._locationList.add(loc);
    }

    public int getAmountInLocation(Location loc) {
        ListIterator<Location> iterator = _locationList.listIterator();
        while (iterator.hasNext()) {
            Location temp = iterator.next();
            if (temp.isEqual(loc))
                return temp.getAmount();
        }
        return -1;
    }

    public void setCategoryName(String name)
    {
        this.info.setSubCat(name);
    }

}