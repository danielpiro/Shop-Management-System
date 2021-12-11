package DataLayer.DalObjects;

import Bussiness_Layer.TransportationPackage.Delivery;
import Bussiness_Layer.TransportationPackage.DeliveryDoc;
import Bussiness_Layer.TransportationPackage.Location;

import java.time.LocalDate;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class DalDelivery {
    private int id;
    private LocalDate departureDay;
    private int truckNum;
    private String source; // have to convert
    private List<DalLocation> destination;
    private List<DalDeliveryDoc> docs;
    private double truckWeight;
    private String driverName;
    private int driverID;
    private int shift;
    private int approved;


    public DalDelivery() {
    }

    public DalDelivery(Delivery delivery, int approved) {
        this.setId(delivery.getId());
        this.setDepartureDay(delivery.getDepartureDay());
        this.setTruckNum(delivery.getTruckNum());
        this.setTruckWeight(delivery.getTruckWeight());
        this.setSource(delivery.getSource().getAddress());
        this.destination = new LinkedList<>();
        for (Location del : delivery.getDestination()) {
            destination.add(new DalLocation(del));
        }
        this.docs = new LinkedList<>();
        for (DeliveryDoc doc : delivery.getDocs()) {// TODO: 12/05/2021
            docs.add(new DalDeliveryDoc(doc, doc.getId()));
        }
        this.setDriverID(delivery.getDriverID());
        this.setDriverName(delivery.getDriverName());
        this.setShift(delivery.getShift());
        this.approved = approved;
    }

    public DalDelivery(int id, LocalDate departureDay, int truckNum, String source, List<DalLocation> destination, List<DalDeliveryDoc> docs, double truckWeight, String driverName, int driverID, int shift, int approved) {
        this.setId(id);
        this.setDepartureDay(departureDay);
        this.setTruckNum(truckNum);
        this.setSource(source);
        this.setDestination(destination);
        this.setDocs(docs);
        this.setTruckWeight(truckWeight);
        this.setDriverName(driverName);
        this.setDriverID(driverID);
        this.setShift(shift);
        this.setApproved(approved);

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getApproved() {
        return approved;
    }

    public void setApproved(int approved) {
        this.approved = approved;
    }

    public LocalDate getDepartureDay() {
        return departureDay;
    }

    public void setDepartureDay(LocalDate departureDay) {
        this.departureDay = departureDay;
    }

    public int getTruckNum() {
        return truckNum;
    }

    public void setTruckNum(int truckNum) {
        this.truckNum = truckNum;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public List<DalLocation> getDestination() {
        return destination;
    }

    public void setDestination(List<DalLocation> destination) {
        this.destination = destination;
    }

    public List<DalDeliveryDoc> getDocs() {
        return docs;
    }

    public void setDocs(List<DalDeliveryDoc> docs) {
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

    public int getDriverID() {
        return driverID;
    }

    public void setDriverID(int driverID) {
        this.driverID = driverID;
    }

    public int getShift() {
        return shift;
    }

    public void setShift(int shift) {
        this.shift = shift;
    }
}