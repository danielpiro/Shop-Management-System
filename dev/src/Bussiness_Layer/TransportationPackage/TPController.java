package Bussiness_Layer.TransportationPackage;

import Bussiness_Layer.BussinessDataAccess;

import java.nio.file.LinkOption;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TPController {
    private List<Delivery> deliveries;
    private List<Delivery> requested;
    private final List<Location> locations;
    private final Section section;
    private List<Truck> trucks;
    private final List<Supplies> supplies;
    private static BussinessDataAccess bussinessDataAccess = null;


    public List<Delivery> getRequested() {
        return requested;
    }

    public void clearRequested() {
        requested = new ArrayList<>();
    }

    public TPController() throws SQLException {
        requested = new ArrayList<>();
        setDeliveries(new ArrayList<>());
        locations = new ArrayList<>();
        section = new Section();
        setTrucks(new ArrayList<>());
        supplies = new ArrayList<>();
        getSection().addNewArea("South");
        getSection().addNewArea("Middle");
        getSection().addNewArea("North");
        getSupplies().add(new Supplies(1, "milk",2));
        getSupplies().add(new Supplies(2, "chocolate",3));
        getSupplies().add(new Supplies(3, "cornflakes",4));
        getSupplies().add(new Supplies(4, "pickles",4));
        getSupplies().add(new Supplies(5, "bread",5));
        getSupplies().add(new Supplies(6, "pasta",5));
        getSupplies().add(new Supplies(7, "Cigarettes",5));
        bussinessDataAccess = BussinessDataAccess.getBTD();
    }

    //add new delivery to the system
    public Delivery addDelivery(LocalDate departureDay, int shift, int truckNum, Location source, List<Location> destinations, List<DeliveryDoc> deliveryDocs, double truckWeight, String driverName, int driverId) throws Exception {
        validateTruckWeight(truckNum, truckWeight);
        checkTruckAvailability(truckNum, departureDay, shift);
        Delivery delivery = new Delivery(getDeliveries().size() + 1, departureDay, shift, truckNum, source, destinations, deliveryDocs, truckWeight, driverName, driverId);
        getDeliveries().add(delivery);
        return delivery;
    }

    private void checkTruckWeight(int truckID, double weightToCheck) throws Exception {
        Truck truck = getTruck(truckID);
        if (truck.getMaxWeight() < weightToCheck)
            throw new Exception("This truck max weight is " + truck.getMaxWeight() + " the weight of supplies are over weight!");
    }

    public void requestDelivery(Delivery delivery) {
        requested.add(delivery);
    }


    //check delivery truck
    private void checkTruckAvailability(int truckNum, LocalDate departureDay, int shift) throws Exception {
        for (Delivery delivery : getDeliveries()) {
            if (delivery.getTruckNum() == truckNum)
                if (delivery.getDepartureDay().isEqual(departureDay))
                    if (delivery.getShift() == shift)
                        throw new Exception("The truck is not available!");
        }
    }

    //add new delivery documentation
    public DeliveryDoc addDocumentation(int id, int dest, List<Supplies> supplies, int deliveryID) throws Exception {
        for (Delivery delivery : getDeliveries()) {
            for (DeliveryDoc deliveryDoc : delivery.getDocs()) {
                if (deliveryDoc.getId() == id) {
                    throw new Exception("The id :" + id + " is already in use!");
                }
            }
        }
        boolean check = false;
        for (Location location : getLocations()) {
            if (location.getId() == dest) {
                check = true;
                break;
            }
        }
        if (!check)
            throw new Exception("The location not exist!");
        DeliveryDoc deliveryDoc = new DeliveryDoc(id, getLocation(dest).getAddress(), supplies);
        Delivery findDelivery = null;
        for (Delivery delivery : getRequested()) {
            if (delivery.getId() == deliveryID) {
                findDelivery = delivery;
                break;
            }
        }
        assert findDelivery != null;
        List<DeliveryDoc> a = findDelivery.getDocs();
        a.add(deliveryDoc);
        for (Delivery delivery : getRequested()) {
            if (delivery.getId() == id) {
                delivery.setDocs(a);
                break;
            }
        }
        return deliveryDoc;
    }

    public Supplies getSupply(int id) throws Exception {
        for (Supplies supplies1 : getSupplies()) {
            if (supplies1.getId() == id)
                return supplies1;
        }
        Supplies sup = bussinessDataAccess.getSupplies(id);
        if (sup != null)
            return sup;
        throw new Exception("The id :" + id + " is invalid");
    }


    //check valid weight
    private void validateTruckWeight(int truckNum, double truckWeight) throws Exception {
        for (Truck truck : getTrucks()) {
            if (truck.getLicenseNum() == truckNum)
                if (truck.getMaxWeight() < truckWeight || truck.getNetoWeight() > truckWeight)
                    throw new Exception("The truck weight :" + truckWeight + " is invalid!");
        }
    }

    public void updateDeliveryTruckWeight(int deliveryID, double newWeight) throws Exception {
        timeCheck(deliveryID);
        validateTruckWeight(getDeliveryByID(deliveryID).getTruckNum(), newWeight);
        getDeliveryByID(deliveryID).setTruckWeight(newWeight);

    }

    public void timeCheck(int deliveryID) throws Exception {
        if (getDeliveryByID(deliveryID).getDepartureDay().isBefore(LocalDate.now()))
            throw new Exception("cant change the delivery beacuse time!");
    }


    public void removeDelivery(int id) throws Exception {
        for (Delivery delivery : getRequested()) {
            if (delivery.getId() == id) {
                getRequested().remove(delivery);
                break;
            }
        }
        Delivery del = bussinessDataAccess.getRequestDelivery(id);
        if (del != null) {
            bussinessDataAccess.removeDelivery(id);
            return;
        }
        assert false;
        for (DeliveryDoc doc : del.getDocs()) {
            bussinessDataAccess.removeDeliveryDoc(doc.getId());
        }
        for (Location location : del.getDestination()) {
        }
        throw new Exception("Not found the deliver id :" + id);
    }

    //update departure day
//    public void updateDeliveryTime(int deliveryID, LocalDate departureDay, LocalTime departureTime, LocalTime arrivalTime, int truckNum) throws Exception {
//        timeCheck(deliveryID);
//        checkTruckAvailability(truckNum, departureDay, departureTime, arrivalTime);
//        for (Delivery delivery : getDeliveries()) {
//            if (delivery.getDepartureDay().equals(departureDay))
//                if ((delivery.getDepartureTime().isBefore(departureTime)) && (delivery.getArrivalTime().isAfter(departureTime)) || ((arrivalTime.isAfter(delivery.getDepartureTime()) && (arrivalTime.isBefore(delivery.getArrivalTime())))))
//                    throw new Exception("The new lead time is not appropriate!");
//        }
//        Delivery delivery = getDelivery(deliveryID);
//        delivery.setArrivalTime(arrivalTime);
//        delivery.setDepartureDay(departureDay);
//        delivery.setDepartureDay(departureDay);
//        changeDelivery(delivery);
//    }

    //help function that remove the old delivry hours with the new one
    private void changeDelivery(Delivery delivery) {
        for (Delivery delivery1 : getDeliveries()) {
            if (delivery1.getId() == delivery.getId()) {
                getDeliveries().remove(delivery1);
                getDeliveries().add(delivery);
                break;
            }
        }
    }


    //get delivery by ID
    public Delivery getDelivery(int id) throws Exception {
        for (Delivery delivery : getDeliveries()) {
            if (delivery.getId() == id)
                return delivery;
        }
        Delivery delivery = bussinessDataAccess.getDelivery(id);
        if (delivery != null)
            return delivery;
        throw new Exception("could not find the requested delivery!");
    }


    //Add location
    public Location addLocation(int id, String name, String phone, String contactName, String secName) throws Exception {
        for (Location l : getLocations()) {
            if (l.getId() == id)
                throw new Exception("The id is already exist!");
            if (l.getAddress().equals(name))
                throw new Exception("The address : " + name + " is already exist!");
            if (l.getPhone().equals(phone))
                throw new Exception("The phone is already exist for location " + l.getAddress() + " so please the number to relevant one!");
        }
        Location location = new Location(id, name, phone, contactName);
        getSection().addLocation(secName, location);
        getLocations().add(location);
        return location;
    }

    //add supply to doc
    public void addSupplyToDoc(int deliveryID, int docID, int supplies) throws Exception {
        timeCheck(deliveryID);
        getDeliveryByID(deliveryID).getDeliveryDocById(docID).addNewSupply(getSupplyById(supplies));

    }

    private Supplies getSupplyById(int id) throws Exception {
        for (Supplies supplies1 : getSupplies()) {
            if (supplies1.getId() == id) {
                return supplies1;
            }
        }
        Supplies sup = bussinessDataAccess.getSupplies(id);
        if (sup != null) {
            supplies.add(sup);
            return sup;
        }
        throw new Exception("The id :" + id + " is not exist!");
    }

    //remove supply from doc
    public void removeSupplyFromDoc(int deliveryID, int docID, int supplies) throws Exception {
        timeCheck(deliveryID);
        getDeliveryByID(deliveryID).getDeliveryDocById(docID).removeSupplies(supplies);
    }

    //update truck of the delivery
    public void updateTruckOfTheDelivery(int deliveryId, int truckNum) throws Exception {
        Delivery delivery = getDeliveryByID(deliveryId);
        checkTruckAvailability(truckNum, delivery.getDepartureDay(), delivery.getShift());
        checkTruckWeight(truckNum, delivery.getTruckWeight());
    }


    //Remove location
    public void removeLocation(int id) throws Exception {
        for (Location location : getLocations()) {
            if (location.getId() == id) {
                getSection().removeLocation(location);
                getLocations().remove(location);
            }
        }
        if (!bussinessDataAccess.removeLocation(id))
            throw new Exception("The id :" + id + " of the location is not exist!");
    }

    //remove delivery documentation
    public void removeDeliveryDoc(int idDoc, int deliveryID) throws Exception {
        timeCheck(deliveryID);
        for (DeliveryDoc deliveryDoc : getDeliveryByID(deliveryID).getDocs()) {
            if (deliveryDoc.getId() == idDoc) {
                List<DeliveryDoc> listOfDocs = getDeliveryByID(deliveryID).getDocs();
                listOfDocs.remove(deliveryDoc);
                getDeliveryByID(deliveryID).setDocs(listOfDocs);
            }
        }
        bussinessDataAccess.removeDoc(idDoc, deliveryID);
    }


    public Delivery getDeliveryByID(int id) throws Exception {
        for (Delivery delivery : getRequested()) {
            if (delivery.getId() == id)
                return delivery;
        }
        for (Delivery delivery : getDeliveries()) {
            if (delivery.getId() == id)
                return delivery;
        }
        Delivery delivery = bussinessDataAccess.getRequestDelivery(id);
        if (delivery != null)
            return delivery;
        throw new Exception("The delivery id :" + id + " not exist!");
    }


    //help function to search the delivery documentation by his id
    public DeliveryDoc getDeliveryDocById(int idDoc, int deliverID) throws Exception {
        for (DeliveryDoc deliveryDoc : getDeliveryByID(deliverID).getDocs()) {
            if (deliveryDoc.getId() == idDoc) {
                return deliveryDoc;
            }
        }
        DeliveryDoc deliveryDoc = bussinessDataAccess.getDeliveryDoc(idDoc);
        if (deliveryDoc != null) {
            return deliveryDoc;
        }
        throw new Exception("The id :" + idDoc + " is not found!");
    }

    //help function to search the delivery documentation by his id
    public List<DeliveryDoc> getDeliveryDocList(int idDelivery) throws Exception {
        for (Delivery delivery : getDeliveries()) {
            if (delivery.getId() == idDelivery) {
                return delivery.getDocs();
            }
        }
        throw new Exception("The id :" + idDelivery + " is not found!");
    }

    //Update name of location
    public void updateNameOfLocation(int id, String newName) throws Exception {
        Location location1 = bussinessDataAccess.getLocationByID(id);
        if (location1 != null)
            bussinessDataAccess.updateLocation(new Location(location1.getId(), newName, location1.getPhone(), location1.getContactName()));
        Location location = getLocation(id);
        for (Location loc : getLocations()) {
            if (loc.getAddress().equals(newName))
                throw new Exception("The name :" + newName + " is already used, please choose other name!");
        }
        location.setAddress(newName);


    }

    public void checkWeightLicnese(String license, double maxWeight) throws Exception {
        if ((license.equals("A") & maxWeight > 5) || (license.equals("B") & maxWeight > 10) || (license.equals("C") & maxWeight > 15) || (license.equals("D") & maxWeight > 20))
            throw new Exception("The license not good!");
    }

    public String checkWeightLicnese2(double maxWeight) throws Exception {
        if (maxWeight <= 5)
            return "A";
        else if (maxWeight <= 10)
            return "B";
        else if (maxWeight <= 15)
            return "C";
        else if (maxWeight <= 20)
            return "D";
        else
            throw new Exception("Illegal weight!!!");
    }

    //set phone number to location
    public void updatePhoneOfLocation(int id, String phone) throws Exception {
        Location location1 = bussinessDataAccess.getLocationByID(id);
        if (location1 != null)
            bussinessDataAccess.updateLocation(new Location(location1.getId(), location1.getAddress(), phone, location1.getContactName()));
        Location location = getLocation(id);
        for (Location loc : getLocations()) {
            if (loc.getPhone().equals(phone))
                throw new Exception("The phone :" + phone + " is already used, please choose other phone!");
        }
        location.setPhone(phone);
    }

    //set name of contact of location
    public void updateContactName(int id, String newName) throws Exception {
        Location location1 = bussinessDataAccess.getLocationByID(id);
        if (location1 != null)
            bussinessDataAccess.updateLocation(new Location(location1.getId(), location1.getAddress(), location1.getPhone(), newName));
        getLocation(id).setContactName(newName);
    }

    //get truck by id truck
    public Truck getTruck(int idTruck) throws Exception {
        for (Truck truck: trucks){
           if(truck.getLicenseNum()==idTruck)
               return truck;
        }
        Truck truck1 = bussinessDataAccess.getTruck(idTruck);
        if (truck1 != null) {
            trucks.add(truck1);
            return truck1;
        }
        throw new Exception("The id " + idTruck + " is not invalid!");
    }


    //Get location
    public Location getLocation(int id) throws Exception {
        for (Location l : getLocations()) {
            if (l.getId() == id)
                return l;
        }
        Location location = bussinessDataAccess.getLocationByID(id);
        if (location != null)
            return location;
        throw new Exception("The id :" + id + " is not exist!");
    }


    //add new truck to the trucks list
    public Truck addTruck(int licenseNum, String model, double netoWeight, double maxWeight) throws Exception {
        if (maxWeight < netoWeight)
            throw new Exception("truck weight is invalid!");
        for (Truck truck : getTrucks()) {
            if (truck.getLicenseNum() == licenseNum)
                throw new Exception("truck already exist in the system!");
        }
        Truck truck1 = bussinessDataAccess.getTruck(licenseNum);
        if (truck1 == null) {
            Truck truck = new Truck(licenseNum, model, netoWeight, maxWeight);
            getTrucks().add(truck);
            bussinessDataAccess.addTruck(truck);
            return truck;
        } else {
            throw new Exception("truck already exist in the system!");
        }
    }

    //remove truck from the trucks list
    public void removeTruck(int licenseNum) throws Exception {
        // for (requested request : getRequested())
        for (Truck truck : getTrucks()) {
            if (truck.getLicenseNum() == licenseNum) {
                getTrucks().remove(truck);
                break;
            }
        }
        boolean check= bussinessDataAccess.removeTruck(licenseNum);
        if(!check)
           throw new Exception("could not removed the current truck");
    }


    //update existing truck max weight
    public void updateTruckMaxWeight(int licenseNum, double maxWeight) throws Exception {
        Truck truck1 = bussinessDataAccess.getTruck(licenseNum);
        if (truck1 != null && maxWeight >= truck1.getNetoWeight()) {
            truck1.setMaxWeight(maxWeight);
            bussinessDataAccess.updateTruck(truck1);
            for (Truck truck : getTrucks()) {
                if (truck.getLicenseNum() == licenseNum) {
                    if (maxWeight < truck.getNetoWeight())
                        throw new Exception("neto weight cannot exceed max weight!");
                    truck.setMaxWeight(maxWeight);
                    return;
                }
            }
        }
        throw new Exception("could not find the requested truck to update!");
    }


    //get section Name
    public String getNameSection(int id) throws Exception {
        return getSection().getName(id);
    }

    public List<Truck> getTrucks() {
        return trucks;
    }

    public void setTrucks(List<Truck> trucks) {
        this.trucks = trucks;
    }

    public List<Delivery> getDeliveries() {
        return deliveries;
    }

    public void setDeliveries(List<Delivery> deliveries) {
        this.deliveries = deliveries;
    }


    public List<Location> getLocations() {
        return locations;
    }

    public Section getSection() {
        return section;
    }

    public List<Supplies> getSupplies() {
        return supplies;
    }

    public void approveDelivery(Delivery del) {
        deliveries.add(del);
    }

    public Delivery getLastDeliveryByID(int id) throws Exception {
        return this.getDeliveryByID(id);
    }
}
