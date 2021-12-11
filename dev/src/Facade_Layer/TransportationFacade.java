package Facade_Layer;

import Bussiness_Layer.BussinessDataAccess;
import Bussiness_Layer.EmployeePackage.Driver;
import Bussiness_Layer.Job;
import Bussiness_Layer.ShiftPackage.ShiftType;
import Bussiness_Layer.Tuple;
import Bussiness_Layer.Utility;
import Bussiness_Layer.TransportationPackage.*;
import DataLayer.DalObjects.DBController.DalController;
import DataLayer.DalObjects.DalDeliveryDoc;
import DataLayer.DalObjects.DalLocation;
import DataLayer.DalObjects.DalTruck;
import Facade_Layer.FacadeObjects.*;

import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


public class TransportationFacade {
    private static TransportationFacade transportationFacade = null;
    private final TPController tpController;
    private final ApplicationFacade workerFacade;
    private static BussinessDataAccess bussinessDataAccess = null;
    //private int canceled_id;

    private TransportationFacade() throws SQLException {
        tpController = new TPController();
        workerFacade = ApplicationFacade.getInstance();
        bussinessDataAccess = BussinessDataAccess.getBTD();
        //canceled_id = DalController.getDalController().previousRequestID()+1;
    }

    public static TransportationFacade getInstance() throws SQLException {
        if (transportationFacade == null)
            transportationFacade = new TransportationFacade();
        return transportationFacade;
    }

    //create new delivery
    public ResponseT<DeliveryFacade> addDelivery(LocalDate departureDay, int shift, int truckNum, LocationFacade source, List<LocationFacade> destinations, double truckWeight, int driverID) {
        try {
            List<Location> loc = new ArrayList<>();
            for (LocationFacade l : destinations)
                loc.add(new Location(l.id, l.address, l.phone, l.contactName));
            Location location = new Location(source.id, source.address, source.phone, source.contactName);
            List<DeliveryDoc> doc = new ArrayList<>();
            Delivery delivery = tpController.addDelivery(departureDay, shift, truckNum, location, loc, doc, truckWeight, workerFacade.getEmployeeById(driverID).getF_Name(), driverID);
            bussinessDataAccess.updateDelivery(delivery, 1);
            return new ResponseT<>(new DeliveryFacade(delivery));
        } catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }

    public Response requestDeliveryManual(LocalDate departureDay, int truckNum, LocationFacade source, List<LocationFacade> destinations, double truckWeight, int day, int shiftType) {
        try {
            String license = tpController.checkWeightLicnese2(truckWeight);
            List<Location> loc = new ArrayList<>();
            for (LocationFacade l : destinations) {
                loc.add(new Location(l.id, l.address, l.phone, l.contactName));
            }
            Location location = new Location(source.id, source.address, source.phone, source.contactName);
            List<DeliveryDoc> doc = new ArrayList<>();
            boolean check=false;
            int index=1;
            while (!check){
                Delivery delivery=bussinessDataAccess.getDelivery(index);
                if(delivery==null)
                    check=true;
                else
                    index++;
            }
            Delivery delivery = new Delivery(index, departureDay, shiftType, truckNum, location, loc, doc, truckWeight, "", -1);
            tpController.requestDelivery(delivery);
            bussinessDataAccess.addDelivery(delivery, 0);
            for (Location dest : loc) {
                bussinessDataAccess.addDest(delivery.getId(), dest.getAddress());
            }
            workerFacade.requestEmpForDelivery(Utility.getLicense(license), loc, day, shiftType);
        } catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
        return new Response();
    }

    public Response requestDelivery(LocalDate departureDay, LocationFacade source, List<Integer> destinations, int truckWeight, List<Integer> items) {
        try {
            List<DalTruck> trucks = DalController.getDalController().getTrucks();
            DalTruck chosenTruck = null;
            int weight = truckWeight/1000000;
            for (DalTruck truck : trucks) {
                if (truck.getMaxWeight() >= weight) {
                    chosenTruck = truck;
                    break;
                }
            }
            List<Supplies> supplies = new ArrayList<>();
            for (Integer item : items)
                supplies.add(new Supplies(DalController.getDalController().getItems(item)));
            String license = tpController.checkWeightLicnese2(weight);
            List<Location> loc = new ArrayList<>();
            StringBuilder dests = new StringBuilder();
            for (int locID : destinations) {
                loc.add(new Location(DalController.getDalController().getDalLocationByID(locID).getId(), DalController.getDalController().getDalLocationByID(locID).getAddress(), DalController.getDalController().getDalLocationByID(locID).getPhone(), DalController.getDalController().getDalLocationByID(locID).getContactName()));
                dests.append(DalController.getDalController().getDalLocationByID(locID).getAddress()).append(" ");
            }
            Location location = new Location(source);
            boolean check = false;
//            int index = 1;
//            while (!check) {
//                Delivery delivery = bussinessDataAccess.getDelivery(index);
//                if (delivery == null)
//                    check = true;
//                else
//                    index++;
//            }
//            assert chosenTruck != null;
            int index = DalController.getDalController().getNextDeliveryIdx();
            List<DeliveryDoc> doc = new ArrayList<>();
            doc.add(new DeliveryDoc(index,dests.toString() , supplies));
            Delivery delivery = new Delivery(index, departureDay, 0, chosenTruck.getLicenseNum(), location, loc, doc, weight, "", -1);
            DalController.getDalController().addDalDeliveryDoc(new DalDeliveryDoc(index,dests.toString(),supplies,index));
            tpController.requestDelivery(delivery);
            bussinessDataAccess.addDelivery(delivery, 0);
            for (Location dest : loc) {
                bussinessDataAccess.addDest(delivery.getId(), dest.getAddress());
            }
            int realDay = departureDay.getDayOfWeek().getValue();
            realDay+=1;
            if(realDay == 8)
                realDay = 1;

            workerFacade.requestEmpForDelivery(Utility.getLicense(license), loc, realDay, 0);
        } catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
        return new Response();
    }


    public List<ResponseT<Delivery>> createAllDeliveries() {
        List<ResponseT<Delivery>> retFin = new ArrayList<>();
        String license;
        LocalDate date = Utility.getUpcomingDate(DayOfWeek.SUNDAY);// function that brings back the date of the next date of sunday
        for (int k = 0; k < 7; k++, date = Utility.incrementLocalDate(date)) {
            for (int i = 0; i < 2; i++) {
                //List Deliveries sunday morning
                List<Driver> driversOnShift = workerFacade.getDriversInShift(date, i);
                //list drivers sunday morning
                List<Delivery> deliveriesInShift = getRequestedDeliveriesShift(date, i);
                for (Delivery del : deliveriesInShift) {
                    if (!hasStorekeeper(del.getDestination(), date, i)) {
                        retFin.add(new ResponseT<>(del, "no storekeeper! the delivery is canceled"));
                        continue;
                    }
                    try {
                        license = tpController.checkWeightLicnese2(del.getTruckWeight());
                    } catch (Exception e) {
                        retFin.add(new ResponseT<>(del, e.getMessage()));
                        continue;
                    }
                    Driver driver = getPotentDriver(license, driversOnShift);
                    if (driver == null) {
                        retFin.add(new ResponseT<>(del, "no driver is available"));
                        continue;
                    }
                    del.setDriverName(driver.getF_Name() + " " + driver.getL_Name());
                    del.setDriverID(driver.getId());
                    bussinessDataAccess.updateDelivery(del, 1);
                    tpController.approveDelivery(del);
                    retFin.add(new ResponseT<>(del));
                }
            }
        }
        tpController.clearRequested();
        return retFin;
    }


    private boolean hasStorekeeper(List<Location> Dest, LocalDate date, int shiftType) {
        for (Location loc : Dest) {
            Tuple<Date, ShiftType> typeTuple = new Tuple<>(Utility.localToDate(date), Utility.getShiftType(shiftType));
            if (!workerFacade.hasStoreKeeper(typeTuple, loc.getId())) {
                return false;
            }
        }
        return true;
    }

    private Driver getPotentDriver(String lic, List<Driver> availableDrivers) {
        Driver toRet;
        Job requiredLicense = Utility.parseLicense(lic);
        List<Driver> eligableDrivers = availableDrivers.stream().filter(Driver ->
                Job.isBigger(Driver.getCertifiedWorks().get(0), Utility.parseLicense(lic))
                        || Job.isEqual(Driver.getCertifiedWorks().get(0), Utility.parseLicense(lic))).collect(Collectors.toList());
        for (int i = 0; i < 3; i++, requiredLicense = Job.plus1(requiredLicense)) {
            assert requiredLicense != null;
            String licStr = Utility.getLicenseFromJob(requiredLicense);
            List<Driver> drivers = eligableDrivers.stream().filter(Driver -> Job.isEqual(Driver.getCertifiedWorks().get(0), Utility.parseLicense(licStr))).collect(Collectors.toList());
            if (!drivers.isEmpty()) {
                toRet = drivers.get(0);
                availableDrivers.remove(toRet);
                return toRet;
            }
        }
        return null;
    }

    private List<Delivery> getRequestedDeliveriesShift(LocalDate date, int shiftType) {
        return tpController.getRequested().stream().filter(delivery -> Utility.compareLocalDate(delivery.getDepartureDay(), date) && shiftType == delivery.getShift()).collect(Collectors.toList());
    }

    //create new document
    public ResponseT<DeliveryDocFacade> addDocument(int id, int dest, List<SuppliesFacade> supplies, int deliveryID) {
        try {
            List<Supplies> supply = new ArrayList<>();
            for (SuppliesFacade dalSupply : supplies) {
                supply.add(new Supplies(dalSupply.id, dalSupply.name , dalSupply.quantity));
            }
            DeliveryDoc deliveryDoc = tpController.addDocumentation(id, dest, supply, deliveryID);
            bussinessDataAccess.addDeliveryDoc(deliveryDoc, deliveryID);
            return new ResponseT<>(new DeliveryDocFacade(deliveryDoc, deliveryID));
        } catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }

    //add new truck
    public ResponseT<TruckFacade> addTruck(int licenseNum, String model, double netoWeight, double maxWeight) {//made
        try {
            return new ResponseT<>(new TruckFacade(tpController.addTruck(licenseNum, model, netoWeight, maxWeight)));
        } catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }

    //get supply by id
    public ResponseT<SuppliesFacade> getSupply(int id) {
        try {
            Supplies supplies = tpController.getSupply(id);
            return new ResponseT<>(new SuppliesFacade(tpController.getSupply(id)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //add new location
    public ResponseT<LocationFacade> addLocation(int id, String name, String phone, String contactName, int idNameOfSection) {//made
        try {
            Location location = tpController.addLocation(id, name, phone, contactName, tpController.getNameSection(idNameOfSection));
            bussinessDataAccess.addDalLocation(location);
            bussinessDataAccess.addLocationToSection(tpController.getNameSection(idNameOfSection), name);
            return new ResponseT<>(new LocationFacade(location));
        } catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }


    //get documentation
    public ResponseT<DeliveryDocFacade> getDeliveryDoc(int idDoc, int deliveryID) {//made
        try {
            return new ResponseT<>(new DeliveryDocFacade(tpController.getDeliveryDocById(idDoc, deliveryID), deliveryID));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Response removeSupplyFromDocument(int deliveryID, int docID, int supplies) {
        try {
            tpController.removeSupplyFromDoc(deliveryID, docID, supplies);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    //add new supply to list
    public Response addNewSupplyToDoc(int deliveryID, int docID, int supplies) {
        try {
            tpController.addSupplyToDoc(deliveryID, docID, supplies);
            bussinessDataAccess.addSupplyToDoc(docID, supplies);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    //get location
    public ResponseT<LocationFacade> getLocation(int idLocation) {
        try {
            return new ResponseT<>(new LocationFacade(tpController.getLocation(idLocation)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //get specifc documentation list
    public List<ResponseT<DeliveryDocFacade>> getDeliveryDocList(int deliveryDocID) {
        try {
            List<DeliveryDoc> deliveryDocs = tpController.getDeliveryDocList(deliveryDocID);
            List<ResponseT<DeliveryDocFacade>> deliveryDocFacade = new ArrayList<>();
            for (DeliveryDoc deliveryDoc : deliveryDocs) {
                deliveryDocFacade.add(new ResponseT<>(new DeliveryDocFacade(deliveryDoc, deliveryDocID)));
            }
            return deliveryDocFacade;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //remove delivery
    public Response removeDelivery(int id) {
        try {
            tpController.removeDelivery(id);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    //remove truck
    public Response removeTruck(int licenseNum) {
        try {
            tpController.removeTruck(licenseNum);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    //remove location
    public Response removeLocation(int id) {
        try {
            tpController.removeLocation(id);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    //remove delivery documentation
    public Response removeDeliveryDoc(int id, int deliveryID) {
        try {
            tpController.removeDeliveryDoc(id, deliveryID);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }


    //update location name
    public Response updateLocationAddress(int id, String name) {
        try {
            tpController.updateNameOfLocation(id, name);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    //update location name
    public Response updateLocationPhone(int id, String phone) {
        try {
            tpController.updatePhoneOfLocation(id, phone);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    //update location contact name
    public Response updateLocationContactName(int id, String contactName) {
        try {
            tpController.updateContactName(id, contactName);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    //update truck weight
    public Response updateTruckMaxWeight(int licenseNum, double maxWeight) {
        try {
            tpController.updateTruckMaxWeight(licenseNum, maxWeight);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public ResponseT<TruckFacade> getTruck(int idTruck) {
        try {
            return new ResponseT<>(new TruckFacade(tpController.getTruck(idTruck)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public ResponseT<DeliveryFacade> getDelivery(int id) {
        try {
            return new ResponseT<>(new DeliveryFacade(tpController.getDeliveryByID(id)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResponseT<DeliveryFacade> getDeliveryLast(int id) {
        try {
            return new ResponseT<>(new DeliveryFacade(tpController.getLastDeliveryByID(id)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<ResponseT<SuppliesFacade>> getSuppliesFacade(int deliveryID, int deliveryDocID) {
        try {
            int index = 0;
            for (DeliveryDoc deliveryDoc : tpController.getDelivery(deliveryID).getDocs())
                if (deliveryDoc.getId() == deliveryDocID)
                    index = tpController.getDelivery(deliveryID).getDocs().indexOf(deliveryDoc);
            List<Supplies> supplies = tpController.getDelivery(deliveryID).getDocs().get(index).getSupplies();
            List<ResponseT<SuppliesFacade>> suppliesFacades = new ArrayList<>();
            for (Supplies supplies1 : supplies) {
                suppliesFacades.add(new ResponseT<>(new SuppliesFacade(supplies1)));
            }
            return suppliesFacades;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
