package Facade_Layer.FacadeObjects;

import java.util.HashMap;
import java.util.List;

public class SectionFacade {
    public HashMap<String, List<LocationFacade>> areas;

    public SectionFacade() {
        areas = new HashMap<>();
    }
}
