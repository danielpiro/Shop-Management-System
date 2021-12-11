package Facade_Layer.FacadeObjects;

import Bussiness_Layer.EmployeePackage.RegularWorker;
import Bussiness_Layer.Job;
import Bussiness_Layer.ShiftPackage.Shift;
import Bussiness_Layer.ShiftPackage.ShiftType;
import Bussiness_Layer.Utility;

import java.util.*;

public class FacadeShift {
    private ShiftType type;
    private int day;
    private FacadeRegularWorker shiftManager;
    private Map<FacadeRegularWorker, Job> regularWorkerList;
    private int locationId;

    public FacadeShift(ShiftType shiftType, int day) {
        this.type = shiftType;
        regularWorkerList = new HashMap<>();
        this.day = day;
    }

    public FacadeShift(Shift other) {
        if (other != null) {
            regularWorkerList = new HashMap<>();
            this.day = other.getDay();
            this.type = other.getType();
            this.shiftManager = new FacadeRegularWorker(other.getShiftManager());
            //Map<RegularWorker, Job> temp = other.getRegularWorkerMap();
            //  for (Map.Entry<RegularWorker, Job> entry : temp.entrySet()) {
            //      regularWorkerList.putIfAbsent(new FacadeRegularWorker(entry.getKey()), entry.getValue());
            //   }
            locationId = other.getBranchID();
        }
    }

    public boolean Registration_for_shift(FacadeRegularWorker regularWorker, Job job) {
        if (regularWorkerList.get(regularWorker) == null) {
            regularWorkerList.put(regularWorker, job);
            return true;
        }
        return false;
    }

    public String getRegularWorkerList() {
        String output = "";
        for (Map.Entry<FacadeRegularWorker, Job> run : regularWorkerList.entrySet()) {
            output += "Name: " + run.getKey().getF_Name() + " " + run.getKey().getL_Name() + " job: " + run.getValue() + " id: " + run.getKey().getId() + '\n';
        }
        return output;
    }

    public Map<FacadeRegularWorker, Job> getRegularWorkerMap() {
        return regularWorkerList;
    }

    public void addManager(FacadeRegularWorker regularWorker, Job job) {
        shiftManager = regularWorker;
        Registration_for_shift(regularWorker, job);
    }

    public FacadeRegularWorker getOtherShiftManager() {
        return shiftManager;
    }

    public Job getJob(FacadeRegularWorker regularWorker) {
        return regularWorkerList.get(regularWorker);
    }

    public boolean removeWorker(RegularWorker replace) {
        regularWorkerList.remove(replace);
        return true;
    }

    public int getDay() {
        return day;
    }

    public ShiftType getType() {
        return type;
    }

    //public RegularWorker getShiftManager(){return shiftManager;}

    @Override
    public String toString() {
        return "Shift{" +
                "type=" + type +
                ", day=" + Utility.dayOfWeek(day) +
                ", shiftManager=" + shiftManager +
                ", regularWorkerList=" + regularWorkerList +
                '}';
    }

    public boolean cmp(FacadeShift other) {
        return toString().equals(other.toString());
    }

    public int getLocationId() {
        return locationId;
    }
}
