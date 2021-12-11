package Bussiness_Layer.TransportationPackage;

import DataLayer.DalObjects.DalSection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Section {
    private final HashMap<String, List<String>> areas;

    public Section() {
        areas = new HashMap<>();
    }
    public Section(DalSection dalSection){
        this.areas = dalSection.getAreas();
    }
    //get hashMap
    public HashMap<String, List<String>> getAreas() {
        return areas;
    }

    // Create new section
    public void addNewArea(String areaName) {
        areas.putIfAbsent(areaName, new ArrayList<>());
    }

    // Add location to specific area
    public void addLocation(String areaName, Location location)throws Exception {
        List<String> list = areas.get(areaName);
        if(list==null)
            throw new Exception("The name: "+areaName+" of area not proper!");
        list.add(location.getAddress());
        areas.replace(areaName, areas.get(areaName), list);
    }

    //get section name
    public String getName(int id)throws Exception{
     if (id==1)
         return "South";
     if(id==2)
         return "Middle";
     if(id==3)
         return "North";
     throw new Exception("The id invalid");
    }

    //remove location
    public void removeLocation(Location location){
        List<String> north=areas.get("North");
        List<String> middle=areas.get("Middle");
        List<String> south=areas.get("South");
        north.remove(location);
        middle.remove(location);
        south.remove(location);
    }
    public void updateLocationSection(String areaName, Location loc) {
        List<String> list = areas.get(areaName);
    }


}
