package Facade_Layer.FacadeObjects;


import Bussiness_Layer.TransportationPackage.Delivery;
import Bussiness_Layer.TransportationPackage.DeliveryDoc;
import Bussiness_Layer.TransportationPackage.Location;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DeliveryFacade {

    public int id;
    public LocalDate departureDay;
    public int shift;
    public int truckNum;
    public LocationFacade source;
    public List<LocationFacade> destination;
    public List<DeliveryDocFacade> docs;


    public double truckWeight;
    public String driverName;
    private int driverID;

    public DeliveryFacade(int id, LocalDate departureDay, int shift, int truckNum, LocationFacade source, List<LocationFacade> destination, List<DeliveryDocFacade> deliveryDocs, double truckWeight, String driverName, int driverID) {
        this.id = id;
        this.shift = shift;
        this.departureDay = departureDay;
        this.truckNum = truckNum;
        this.driverName = driverName;
        this.source = source;
        this.truckWeight = truckWeight;
        this.destination = destination;
        this.docs = deliveryDocs;
        this.driverID = driverID;

    }

    public DeliveryFacade(Delivery delivery) {
        this.id = delivery.getId();
        this.departureDay = delivery.getDepartureDay();
        this.shift = delivery.getShift();
        this.truckNum = delivery.getTruckNum();
        this.driverName = delivery.getDriverName();
        this.driverID = delivery.getDriverID();
        this.source = new LocationFacade(delivery.getSource());
        this.truckWeight = delivery.getTruckWeight();
        destination = new ArrayList<>();
        docs = new ArrayList<>();
        for (Location location : delivery.getDestination()) {
            this.destination.add(new LocationFacade(location));
        }
        for (DeliveryDoc deliveryDoc : delivery.getDocs())
            this.docs.add(new DeliveryDocFacade(deliveryDoc, id));
    }

    private String printDestination() {
        StringBuilder out = new StringBuilder();
        for (LocationFacade contact : destination) {
            out.append(contact.toString()).append('\n');
        }
        return out.toString();
    }

    private String printDocs() {
        StringBuilder out = new StringBuilder();
        for (DeliveryDocFacade contact : docs) {
            out.append(contact.toString()).append('\n');
        }
        return out.toString();
    }

    @Override
    public String toString() {
        return "DeliveryFacade{" +
                "id=" + id +
                ", departureDay=" + departureDay +
                ", shift=" + shift +
                ", truckNum=" + truckNum +
                ", source=" + source +
                ", destination=" + printDestination() +
                ", docs=" + printDocs() +
                ", truckWeight=" + truckWeight +
                ", driverName='" + driverName + '\'' +
                ", driverID=" + driverID +
                '}';
    }
}
