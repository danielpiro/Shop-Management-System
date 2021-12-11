package Bussiness_Layer.ShiftPackage;

import Bussiness_Layer.EmployeePackage.Employee;
import Bussiness_Layer.EmployeePackage.EmployeeController;
import Bussiness_Layer.EmployeePackage.RegularWorker;
import Bussiness_Layer.Job;
import Bussiness_Layer.Utility;
import DataLayer.DalObjects.DalShift;
import Facade_Layer.FacadeObjects.FacadeShift;

import java.util.*;

public class Shift {

    private ShiftType type;
    private int day;
    private RegularWorker shiftManager;
    private Map<Integer, Job> regularWorkerList;
    private int branchID;


    public Shift(ShiftType shiftType, int day, int location) {
        this.type = shiftType;
        regularWorkerList = new HashMap<>();
        this.day = day;
        this.branchID = location;
    }

    public Shift(FacadeShift facadeShift) { //create new Shift
        this.day = facadeShift.getDay();
        this.type = facadeShift.getType();
        this.shiftManager = new RegularWorker(facadeShift.getOtherShiftManager());
    }

    public Shift(){}

    public Shift(DalShift dalShift) {
        try {
            this.day = dalShift.getDay();
            this.branchID = dalShift.getBarnchId();
            this.type = Utility.getShiftType(dalShift.getType());
            this.regularWorkerList = dalShift.getRegularWorkerList();
            this.shiftManager = (RegularWorker) EmployeeController.getInstance().getEmployeeById(dalShift.getManagerId());
        }
        catch (Exception e){
            return;
        }
    }

    public void updateShift(FacadeShift facadeShift, Employee employee, Map<RegularWorker, Job> regularWorkerList) {
        RegularWorker regularWorker = null;
        if (employee instanceof RegularWorker)
            regularWorker = (RegularWorker) employee;
        this.day = facadeShift.getDay();
        this.type = facadeShift.getType();
        this.shiftManager = regularWorker;
        this.branchID = facadeShift.getLocationId();
        for (Map.Entry<RegularWorker, Job> entry : regularWorkerList.entrySet()) {
            regularWorkerList.putIfAbsent(entry.getKey(), entry.getValue());
        }
    }

    public boolean Registration_for_shift(RegularWorker regularWorker, Job job) {
        for (Map.Entry<Integer, Job> entry : regularWorkerList.entrySet()) {
            if (entry.getKey().intValue() == regularWorker.getId())
                return false;
        }
        regularWorkerList.putIfAbsent(new Integer(regularWorker.getId()), job);
        return true;
    }

    public List<Integer> getRegularWorkerList() {
        List<Integer> output = new ArrayList<>(regularWorkerList.keySet());
        return output;
    }

    public void addManager(RegularWorker regularWorker, Job job) {
        shiftManager = regularWorker;
        Registration_for_shift(regularWorker, job);
    }

    public Job getJob(RegularWorker regularWorker) {
        return regularWorkerList.get(regularWorker);
    }

    public boolean removeWorker(RegularWorker replace) {
        if (shiftManager.getId() != replace.getId()) {
            regularWorkerList.remove(replace);
            replace.removeShift(this);
            return true;
        }
        return false;
    }

    public int getDay() {
        return day;
    }

    public ShiftType getType() {
        return type;
    }

    public RegularWorker getShiftManager() {
        return shiftManager;
    }

    @Override
    public String toString() {
        return "Shift{" +
                "type=" + type +
                ", day=" + Utility.dayOfWeek(day) +
                ", shiftManager=" + shiftManager +
                ", regularWorkerList=" + regularWorkerList +
                '}';
    }

    public boolean cmp(Shift other) {
        return toString().equals(other.toString());
    }

    public RegularWorker getOtherShiftManager() {
        RegularWorker output = new RegularWorker(shiftManager);
        return output;
    }

    public Map<Integer, Job> getRegularWorkerMap() {
        return regularWorkerList;
    }

    public int getBranchID() {
        return branchID;
    }

    public boolean hasStoreKeeper(){
        for (Map.Entry<Integer, Job> entry : regularWorkerList.entrySet()) {
            if (entry.getValue() == Job.StoreKeeper)
                return true;
        }
        return false;
    }

}
