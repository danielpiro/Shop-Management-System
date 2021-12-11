package DataLayer.DalObjects;

import Bussiness_Layer.TransportationPackage.Section;

import java.util.HashMap;
import java.util.List;

public class DalSection {
    private final HashMap<String, List<String>> areas;

    public DalSection(Section section) {
        this.areas = section.getAreas();
    }
    public DalSection(HashMap<String,List<String>> areas){
        this.areas = areas;
    }
    public DalSection(){
        areas = null;
    }
    public HashMap<String, List<String>> getAreas() {
        return areas;
    }
    public void addLocationToSection(String area , String location){
        areas.get(area).add(location);
    }
}
