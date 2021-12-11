package Bussiness_Layer;


import Bussiness_Layer.EmployeePackage.*;
import Bussiness_Layer.ShiftPackage.RecommendedLineUp;
import Bussiness_Layer.ShiftPackage.Shift;
import Bussiness_Layer.ShiftPackage.ShiftType;
import Bussiness_Layer.TransportationPackage.*;
import DataLayer.DalObjects.*;
import DataLayer.DalObjects.DBController.DalController;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BussinessDataAccess {
    private static BussinessDataAccess thisOne;
    private static DalController dataTb;


    private BussinessDataAccess() {
    }

    public static BussinessDataAccess getBTD() {
        try {
            if (thisOne == null) {
                thisOne = new BussinessDataAccess();
                BussinessDataAccess.dataTb = DalController.getDalController();
            }
            return thisOne;
        } catch (Exception e) {
            return null;
        }
    }

    public void set() {
        dataTb.initialize();
    }
    //Location
//-------------------------------------------------------------------------------------------------------------

    public Location getLocation(String address) {
        DalLocation dalLocation = dataTb.getDalLocation(address);
        if (dalLocation == null)
            return null;
        return new Location(dalLocation);
    }

    public Location getLocationByID(int id) {
        DalLocation dalLocation = dataTb.getDalLocationByID(id);
        if (dalLocation == null)
            return null;
        return new Location(dalLocation);
    }

    public boolean addDalLocation(Location location) {
        return dataTb.addDalLocation(new DalLocation(location));
    }


    public boolean updateLocation(Location location) {
        return dataTb.updateDalLocation(new DalLocation(location));
    }

    public boolean removeLocation(int id) {
        return dataTb.removeDalLocation(id);
    }

    //Delivery
//--------------------------------------------------------------------------------------------------------------


    public Delivery getDelivery(int deliveryID) {
        try {
            DalDelivery dalDelivery = dataTb.getDalDelivery(deliveryID);
            if (dalDelivery == null)
                return null;
            return new Delivery(dalDelivery);
        } catch (Exception e) {
            return null;
        }
    }

    public Delivery getRequestDelivery(int deliveryID) {
        try {
            DalDelivery dalDelivery = dataTb.getRequestDalDelivery(deliveryID, 0);
            if (dalDelivery == null)
                return null;
            return new Delivery(dalDelivery);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean updateDelivery(Delivery delivery, int approved) {
        return dataTb.updateDalDelivery(new DalDelivery(delivery, approved));
    }

    public void removeDelivery(int deliveryID) {
        dataTb.removeDalDelivery(deliveryID);
    }

    public void removeDest(int delID) {
        dataTb.removeDalDest(delID);
    }

    public boolean addDelivery(Delivery delivery, int approved) {
        return dataTb.addDalDelivery(new DalDelivery(delivery, approved));
    }

    public boolean addDest(int deliveryID, String address) {
        return dataTb.addDestination(deliveryID, address);
    }


    public List<Delivery> getDeliveryRequest() {
        List<DalDelivery> dalDeliveryRequest = dataTb.getDalDeliveryRequest();
        try {
            List<Delivery> deliveryList = new ArrayList<>();
            if (dalDeliveryRequest == null) {
                assert false;
                for (DalDelivery delivery : dalDeliveryRequest) {
                    deliveryList.add(new Delivery(delivery));
                }
            }
            return deliveryList;
        } catch (Exception e) {
            return null;
        }
    }


    //Supply
//----------------------------------------------------------------------------------------


    public Supplies getSupplies(int supplyID) {
        DalSupply dalSupply = dataTb.getDalSupply(supplyID);
        if (dalSupply == null)
            return null;
        return new Supplies(dalSupply);
    }

    //Truck
//-----------------------------------------------------------------------------------------

    public boolean addTruck(Truck truck) {
        return dataTb.addDalTruck(new DalTruck(truck));
    }

    public boolean removeTruck(int licenseNum) {
        return dataTb.removeDalTruck(licenseNum);
    }

    public Truck getTruck(int licenseNum) {
        DalTruck dalTruck = dataTb.getDalTruck(licenseNum);
        if (dalTruck == null)
            return null;
        return new Truck(dalTruck);
    }

    public void updateTruck(Truck truck) {
        dataTb.updateDalTruck(new DalTruck(truck));
    }

    //DeliveryDoc
//-------------------------------------------------------------------------------------------

    public boolean addDeliveryDoc(DeliveryDoc deliveryDoc, int deliveryID) {
        return dataTb.addDalDeliveryDoc(new DalDeliveryDoc(deliveryDoc, deliveryID));
    }

    public void removeDeliveryDoc(int docID) {
        dataTb.removeDeliveryDocs(docID);
    }

    public void removeDoc(int docID, int deliveryID) {
        dataTb.removeDeliveryDoc(docID, deliveryID);
    }

    public boolean removeSup(int docID, int supID) {
        return dataTb.removeSup(docID, supID);
    }

    public static void setDataTb(DalController dataTb) {
        BussinessDataAccess.dataTb = dataTb;
    }

    public DeliveryDoc getDeliveryDoc(int docID) {
        DalDeliveryDoc dalDeliveryDoc = dataTb.getDeliveryDoc(docID);
        return dalDeliveryDoc == null ? null : new DeliveryDoc(dalDeliveryDoc);
    }

    public boolean addSupplyToDoc(int docID, int supply) {
        return dataTb.addDalSuppliesToDoc(docID, supply);
    }

//Section
//---------------------------------------------------------------------------------------------


    public boolean addLocationToSection(String areaName, String location) {
        return dataTb.addLocationToSection(areaName, location);
    }

    public Section loadSections() {
        return new Section(dataTb.getSections());
    }
//TMP\HRM
//----------------------------------------------------------------------------------------------

    public boolean addTMP_HRM(HumanResourceManager humanResourceManager) {
        DalEmployee employee = new DalEmployee(humanResourceManager);
        return dataTb.addDalTPM_HRM(employee, "h");
    }

    public boolean addTMP_HRM(StoreManager storeManager) { //todo
        DalEmployee employee = new DalEmployee(storeManager);
        return dataTb.addDalTPM_HRM(employee, "m");
    }

    public TransportationManager getTransportationManager(int id) {
        return new TransportationManager(dataTb.getTPM_HRM(id));
    }

    public HumanResourceManager getHumanResourceManager(int id) {
        return new HumanResourceManager(dataTb.getTPM_HRM(id));
    }

    public boolean addTMP_HRM(TransportationManager transportationManager) {
        DalEmployee employee = new DalEmployee(transportationManager);
        return dataTb.addDalTPM_HRM(employee, "t");
    }

    //Shift
//-------------------------------------------------------------------------------------------------

    public boolean addShift(Shift shift, Date date) {
        DalShift dalShift = new DalShift(shift, date);
        return dataTb.addShift(dalShift);
    }

    public Shift getShift(ShiftType type, Date date, int branchId) {
        Tuple<Date, ShiftType> typeTuple = new Tuple<>(date, type);
        DalShift dalShift = dataTb.getShift(branchId, typeTuple);
        if (dalShift != null) {
            return new Shift(dalShift);
        } else return null;
    }

    //RegularWorker
//----------------------------------------------------------------------------------------------------

    public boolean addRegularWorker(RegularWorker regularWorker) {
        DalRegularWorker dalRegularWorker = new DalRegularWorker(regularWorker);
        return dataTb.addDalRegWorker(dalRegularWorker, "r");
    }

    public boolean addDriver(Driver driver) {
        DalRegularWorker dalRegularWorker = new DalRegularWorker(driver);
        return dataTb.addDalRegWorker(dalRegularWorker, "d");
    }

    public boolean addStorageKeeper(StorageKeeper storageKeeper) {
        DalRegularWorker dalRegularWorker = new DalRegularWorker(storageKeeper);
        return dataTb.addDalRegWorker(dalRegularWorker, "s");
    }

    public Employee getEmployee(int id) {
        String type = dataTb.getEmployeeType(id);
        switch (type) {
            case "r":
                return getRegularWorker(id);
            case "t":
                return getTransportationManager(id);
            case "h":
                return getHumanResourceManager(id);
            case "d":
                return getDriver(id);
            case "k":
                return getStorageKeeper(id);
            case "m":
                return getManager(id);
            default:
                return null;

        }
    }

    private Employee getManager(int id) {
        return new StoreManager(dataTb.getTPM_HRM(id));
    }

    public RegularWorker getRegularWorker(int id) {
        return new RegularWorker(dataTb.getRegularWorker(id));
    }

    public Driver getDriver(int id) {
        return new Driver(dataTb.getRegularWorker(id));
    }

    public StorageKeeper getStorageKeeper(int id) {
        return new StorageKeeper(dataTb.getRegularWorker(id));
    }


    //recomendedLineUp
//------------------------------------------------------------------------------------------------
    public boolean addRecommendedLineUp(RecommendedLineUp recommendedLineUp) {
        try {
            DalRecommendedLineUp dalRecommendedLineUp = new DalRecommendedLineUp(recommendedLineUp);
            return dataTb.addRecommendLineup(dalRecommendedLineUp);
        } catch (Exception e) {
            return false;
        }
    }

    public RecommendedLineUp getRecommendedLineUp() {
        try {
            return new RecommendedLineUp(dataTb.getRecommendedLineUp());
        } catch (Exception e) {
            return null;
        }
    }

    //todo
    public void addDriverShift(int id, Job job, Tuple<Date, ShiftType> typeTuple) {
        dataTb.addDriverShift(typeTuple, id, job);
    }

    public List<Integer> getDriverShift(Tuple<Date, ShiftType> typeTuple) {
        return dataTb.getDriverShft(typeTuple);
    }


    //================employe====================


    public List<Employee> loadEmployees() {
        List<Integer> ids = dataTb.loadEmployees();
        List<Employee> Employees = new ArrayList<>();
        for (Integer id : ids) {
            Employee toAdd = getEmployee(id);
            if (toAdd != null)
                Employees.add(toAdd);
        }
        return Employees;
    }

    public void updateEmployee(Employee employee) {
        DalEmployee dalEmployee = new DalEmployee(employee);
        dataTb.updateDalTPM_HRM(dalEmployee);
    }
}

