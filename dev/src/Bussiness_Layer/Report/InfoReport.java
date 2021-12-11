package Bussiness_Layer.Report;

import Bussiness_Layer.Item.Item;
import DataLayer.DalObjects.SaleInfo;

import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class InfoReport extends Report {

    private List<String> _categoryList;
    private Map<Item,List<SaleInfo>> _mapItems;


    public InfoReport(Map<Item,List<SaleInfo>> itemsInReport)
    {
        super();
        this._categoryList = null;
        this._mapItems = itemsInReport;
    }

    public InfoReport(Map<Item,List<SaleInfo>> itemsInReport, List<String> categoryList)
    {
        super();
        this._categoryList = categoryList;
        this._mapItems = itemsInReport;
    }

    private String getCategoryString()
    {
        String str = "";
        if(_categoryList == null)
            str = "Info Report for all items in store: \r\n";
        else
        {
            str =  "Info Report for items of category: ";
            ListIterator<String> iterator = _categoryList.listIterator();
            if(!iterator.hasNext())
            {
                //TODO: ERROR SHOULD NPT BE EMPTY
            }
            else
            {
                str = str + iterator.next()+"\r\n";
                while (iterator.hasNext()){
                    str = str + "sub category: " + iterator.next() + "\r\n";
                }
//                if(iterator.hasNext())
//                {
//                    str = str + "sub category: " + iterator.next() + "\r\n";
//                    if(iterator.hasNext())
//                    {
//                        str = str + "sub sub category: "+ iterator.next() + "\r\n";
//
//                        if ((iterator.hasNext()))
//                        {
//                            //TODO: ERROR SHOULD NOT HAVE MORE category
//                        }
//                    }
//                }
            }
        }
        return str;
    }
    @Override
    public String getItemsString()
    {
        if(_mapItems == null ||_mapItems.isEmpty())
            return "no items to display";
        String str = "";
        for (Map.Entry<Item,List<SaleInfo>> entry : _mapItems.entrySet())
        {
            str = str + entry.getKey().toString();
            //str + iterator.next().toString() + "\r\n";
            if(entry.getValue() != null && !entry.getValue().isEmpty()) {
                ListIterator<SaleInfo> iterator = entry.getValue().listIterator();
                str = str +"Sales of the item:\n";
                while(iterator.hasNext())
                {
                    SaleInfo si = iterator.next();
                    str = str+ ""+si.toString()+"\n";
                }
            }
            else
            {
                str = str + "No sales have been made of this item\n\n";
            }
        }
        return str;
    }
    @Override
    public String toString() {

        String str = "";
        str = getCategoryString() + getItemsString();
        return str;

    }
}
