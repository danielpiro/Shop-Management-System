package Bussiness_Layer.Report;


import Bussiness_Layer.Item.Item;
import DataLayer.DalObjects.SaleInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReportMaker {
    List<Report> listReport;
    public ReportMaker()
    {
        listReport = new ArrayList<Report>();
    }
    //******
    //at this point the list should only contains relevant items
    //******


    public InfoReport CreateInfoReport(Map<Item,List<SaleInfo>> itemMap, List<String> categoryList) {
        if (categoryList == null || categoryList.isEmpty())
            return new InfoReport(itemMap);
        return new InfoReport(itemMap,categoryList);
    }

    public ShortageReport CreateShortageReport(Map<Item,Integer> itemList)
    {
        return new ShortageReport(itemList);
    }

    public DamageReport CreateDamageReport(Map<Item,Integer> itemList)
    {
        return new DamageReport(itemList);
    }


}
