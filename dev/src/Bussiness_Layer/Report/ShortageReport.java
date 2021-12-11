package Bussiness_Layer.Report;


import Bussiness_Layer.Item.Item;

import java.util.Map;

public class ShortageReport extends Report {

    public ShortageReport(Map<Item,Integer> itemList) //TODO: Location???
    {
        super(itemList);
    }

    @Override
    public String toString()
    {
        if (itemsInReport.isEmpty())
//        ListIterator<Item> iterator = _itemsInReport.listIterator();
//        if(!iterator.hasNext())
            return "No shortage in items to display";
        String str = "";
        str = getItemsString();
        return str;

    }
}
