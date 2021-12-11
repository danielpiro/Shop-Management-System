package Bussiness_Layer.TransportationPackage;

import Bussiness_Layer.BussinessDataAccess;
import DataLayer.DalObjects.DalDelivery;
import DataLayer.DalObjects.DalDeliveryDoc;
import DataLayer.DalObjects.DalLocation;
import Facade_Layer.FacadeObjects.DeliveryDocFacade;
import Facade_Layer.FacadeObjects.DeliveryFacade;
import Facade_Layer.FacadeObjects.LocationFacade;


import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Delivery {
    private int id;
    private LocalDate departureDay;
    private int shift;
    private int truckNum;
    private Location source;
    private List<Location> destination;
    private List<DeliveryDoc> docs;
    private double truckWeight;
    private String driverName;
    private int driverID;

    public Delivery(int id, LocalDate departureDay, int shift, int truckNum, Location source, List<Location> destination, List<DeliveryDoc> deliveryDocs, double truckWeight, String driverName, int driverID) {
        this.id = id;
        this.departureDay = departureDay;
        this.shift = shift;
        this.truckNum = truckNum;
        this.driverName = driverName;
        this.source = source;
        this.truckWeight = truckWeight;
        this.destination = destination;
        this.docs = deliveryDocs;
        this.setDriverID(driverID);
    }

    //  private int id;
    //    private LocalDate departureDay;
    //    private int truckNum;
    //    private String source; // have to convert
    //    private List<DalLocation> destination;
    //    private List<DalDeliveryDoc> docs;
    //    private double truckWeight;
    //    private String driverName;
    //    private int driverID;
    //    private int shift;
    //    private int approved;

    public Delivery(DalDelivery dalDelivery) throws SQLException {
        this.id = dalDelivery.getId();
        this.shift = dalDelivery.getShift();
        this.departureDay = dalDelivery.getDepartureDay();
        this.truckNum = dalDelivery.getTruckNum();
        this.source = Objects.requireNonNull(BussinessDataAccess.getBTD()).getLocation(dalDelivery.getSource());
        for (DalLocation loc : dalDelivery.getDestination()) {
            this.destination.add(new Location(loc.getId(), loc.getAddress(), loc.getPhone(), loc.getAddress()));
        }
//        for (DalDeliveryDoc doc : dalDelivery.getDocs()) {
//            this.docs.add(new DeliveryDoc(doc));
//        }
        this.truckWeight = dalDelivery.getTruckWeight();
        this.driverID = dalDelivery.getDriverID();
        this.driverName = dalDelivery.getDriverName();
    }


    public DeliveryDoc getDeliveryDocById(int id) throws Exception {
        for (DeliveryDoc deliveryDoc : docs) {
            if (deliveryDoc.getId() == id) {
                return deliveryDoc;
            }
        }
        BussinessDataAccess bussinessDataAccess = BussinessDataAccess.getBTD();
        DeliveryDoc deliveryDoc = bussinessDataAccess.getDeliveryDoc(id);
        if (deliveryDoc != null)
            return deliveryDoc;
        throw new Exception("The id :" + id + " of the doc not exist!");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDepartureDay() {
        return departureDay;
    }

    public void setDepartureDay(LocalDate departureDay) {
        this.departureDay = departureDay;
    }

    public int getShift() {
        return shift;
    }

    public void setShift(int shift) {
        this.shift = shift;
    }

    public List<Location> getDestination() {
        return destination;
    }

    public void setDestination(List<Location> destination) {
        this.destination = destination;
    }

    public List<DeliveryDoc> getDocs() {
        return docs;
    }

    public void setDocs(List<DeliveryDoc> docs) {
        this.docs = docs;
    }

    public double getTruckWeight() {
        return truckWeight;
    }

    public void setTruckWeight(double truckWeight) {
        this.truckWeight = truckWeight;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public int getTruckNum() {
        return truckNum;
    }

    public void setTruckNum(int truckNum) {
        this.truckNum = truckNum;
    }

    public Location getSource() {
        return source;
    }

    public void setSource(Location source) {
        this.source = source;
    }

    public int getDriverID() {
        return driverID;
    }

    public void setDriverID(int driverID) {
        this.driverID = driverID;
    }

    public String toString() {
        return "DeliveryFacade{" +
                "id='" + id + '\'' +
                ", departureDay=" + departureDay +
                ", shift=" + shift +
                ", truckNum=" + truckNum +
                ", driverID=" + driverID +
                ", driverName=" + driverName +
                ", source=" + source.toString() +
                ", truckWeight=" + truckWeight +
                ", delivery destination List : " + printDestination() +
                ", delivery docs List" + printDocs() +
                '}';
    }

    private String printDestination() {
        StringBuilder out = new StringBuilder();
        for (Location contact : destination) {
            out.append(contact.toString()).append('\n');
        }
        return out.toString();
    }

    private String printDocs() {
        StringBuilder out = new StringBuilder();
        for (DeliveryDoc contact : docs) {
            out.append(contact.toString()).append('\n');
        }
        return out.toString();
    }
}
