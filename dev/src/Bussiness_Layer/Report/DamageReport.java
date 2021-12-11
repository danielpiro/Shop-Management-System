package Bussiness_Layer.Report;


import Bussiness_Layer.Item.Item;

import java.util.Map;

public class DamageReport extends Report {

    public DamageReport(Map<Item,Integer> itemList) //TODO: Location???
    {
        super(itemList);
    }

    @Override
    public String toString() {
        if (itemsInReport.isEmpty())
//        ListIterator<Item> iterator = _itemsInReport.listIterator();
//        if(!iterator.hasNext())
            return "No Damaged items to display";
        String str = "Damage Report:\n";
            for (Map.Entry<Item,Integer> entry : itemsInReport.entrySet())
                str = str + "Number of damaged or expired items of item "+entry.getKey().getItemName()+
                        " is "+entry.getValue();


        return str;

    }
}
