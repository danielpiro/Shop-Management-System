package Bussiness_Layer.Report;


import Bussiness_Layer.Item.Item;

import java.util.Map;

public abstract class Report {
    protected Map<Item,Integer> itemsInReport;
    public Report(){}
    public Report(Map<Item, Integer> itemList){itemsInReport = itemList;}
    //return a string representation of the report
    public abstract String toString();

    public String getItemsString()
    {
        String str = "";
        if (itemsInReport.isEmpty()){
            return "No items to display";
        }
        else{
            for (Map.Entry<Item,Integer> entry : itemsInReport.entrySet())
                str = str + entry.getKey().toString() + "\r\n";
        }
        return str;
    }

}
