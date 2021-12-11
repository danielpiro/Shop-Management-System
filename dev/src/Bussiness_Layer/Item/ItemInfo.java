package Bussiness_Layer.Item;

import java.util.List;
import java.util.ListIterator;

public class ItemInfo {
    private String _itemName;
    private int _id;
    private String _manufacturerName;
    private String subCat;
    private int _minimumQuantity;
    private double currentPrice;
    private int _weight;

    public ItemInfo(List<String> itemInfoList){
        /*
         *0- name
         * 1- id
         * 2- manfcture name
         * 3- min amount
         * 4- weight
         * 5...- sub category
         */
        ListIterator<String> iterator = itemInfoList.listIterator();
        this._itemName = iterator.next();//0
        String idString = iterator.next();
        try {
            this._id = Integer.parseInt(idString);//1
        }
        catch(NumberFormatException e)
        {
            throw new NumberFormatException("Invalid ID");
        }
        this._manufacturerName = iterator.next();//2

        try {
            this._minimumQuantity = isInteger(iterator.next());//3
        }catch ( NumberFormatException e)
        {
            throw new NumberFormatException("Invalid Quantity");
        }
               String weight = iterator.next();//4
        try
        {
            _weight = isInteger(weight); //4
        }catch(NumberFormatException e)
        {
            throw new NumberFormatException("Invalid weight");
        }
        this.subCat = iterator.next(); //5
        this.currentPrice = 0;
    }

    public String get_itemName() {
        return _itemName;
    }

    public int get_id() {
        return _id;
    }

    public String getManufacturerName() {
        return _manufacturerName;
    }


    public String getCatName()
    {
        return subCat;
    }

    public int get_minimumQuantity() {
        return _minimumQuantity;
    }


    private int isInteger(String str) {

        return Integer.parseInt(str);
    }

    public double getCurrentPrice(){
        return this.currentPrice;
    }

    public void set_itemName(String _itemName) {
        this._itemName = _itemName;
    }

    public void set_manufacturerName(String _manufacturerName) {
        this._manufacturerName = _manufacturerName;
    }

    public void setSubCat(String subCat) {
        this.subCat = subCat;
    }

    public void set_minimumQuantity(int _minimumQuantity) {
        this._minimumQuantity = _minimumQuantity;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public void set_weight(int _weight) {
        this._weight = _weight;
    }


@Override
  public String toString() {
        String str = "Item Info:\r\nItem ID: "+_id + "\r\nItem name: "+ _itemName +
                "\r\nManufacturer Name: "+_manufacturerName + "\r\nItem's category: "+ subCat+"\r\n";
        str = str + "\r\nMinimum amount required: "+ _minimumQuantity +"\r\nCurrent Price: "+currentPrice+"\r\nWeight: "+_weight+"\r\n";
        return str;
    }

    public int get_weight() {
        return _weight;
    }
}
