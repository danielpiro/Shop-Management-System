package Bussiness_Layer.ShiftPackage;

import Bussiness_Layer.Job;
import Bussiness_Layer.TransportationPackage.Location;
import DataLayer.DalObjects.DalRecommendedLineUp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class RecommendedLineUp {
    private Map<AtomicInteger, Map<Job, AtomicInteger>[][]> updaterecommendedLineUp;
    private Map<Job, AtomicInteger>[][] recommendedLineUpDrivers;

    public RecommendedLineUp(List<Location> branch) {
        updaterecommendedLineUp = new HashMap<>();
        initRecommendedLineUp(branch);
    }

    public RecommendedLineUp(DalRecommendedLineUp dalRecommendedLineUp) {
        updaterecommendedLineUp = dalRecommendedLineUp.getUpdaterecommendedLineUp();
        recommendedLineUpDrivers = dalRecommendedLineUp.getRecommendedLineUpDrivers();
    }

    private void initRecommendedLineUp(List<Location> locations) { //Default selection
        recommendedLineUpDrivers = new HashMap[7][2];
        for (Location location : locations) {
            Map<Job, AtomicInteger>[][] temp;
            temp = new Map[7][2];
            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < 2; j++) {
                    temp[i][j] = new HashMap<>(); //default
                    recommendedLineUpDrivers[i][j] = jobIntegerMap();
                    temp[i][j].put(Job.Cashier, new AtomicInteger(1));
                    temp[i][j].put(Job.Usher, new AtomicInteger(1));
                    temp[i][j].put(Job.StoreKeeper, new AtomicInteger(1));
                    temp[i][j].put(Job.Guard, new AtomicInteger(1));
                }
            }
            updaterecommendedLineUp.putIfAbsent(new AtomicInteger(location.getId()), temp);
        }
    }

    private Map<Job, AtomicInteger> jobIntegerMap() {
        Map<Job, AtomicInteger> output = new HashMap<>();
        output.putIfAbsent(Job.DriverLicenseA, new AtomicInteger(1));
        output.putIfAbsent(Job.DriverLicenseB, new AtomicInteger(0));
        output.putIfAbsent(Job.DriverLicenseC, new AtomicInteger(0));
        output.putIfAbsent(Job.DriverLicenseD, new AtomicInteger(0));
        return output;
    }

    public AtomicInteger getNumofEmpl(Job job, int i, int j, Location location) {
        return updaterecommendedLineUp.get(location)[i][j].get(job);
    }

    public void setNewNumber(int i, int j, Job job, int number, Location location) {
        for (Map.Entry<AtomicInteger, Map<Job, AtomicInteger>[][]> entry : updaterecommendedLineUp.entrySet()) {
            if (entry.getKey().intValue() == location.getId()) {
                entry.getValue()[i][j].get(job).set(number);
            }
        }
    }

    public String get_number_of_employees_on_shift(int i, int j, Location location) {
        String output = "";
        for (Map.Entry<Job, AtomicInteger> entry : updaterecommendedLineUp.get(location)[i][j].entrySet()) {
            Job job = entry.getKey();
            output += "Job: " + job + " Amount: " + entry.getValue().intValue() + '\n';
        }
        return output;
    }

    public Map<Job, AtomicInteger>[][] getRecommendedLineUp(int branchId) {
        Map<Job, AtomicInteger>[][] output = null;
        for (Map.Entry<AtomicInteger, Map<Job, AtomicInteger>[][]> entry : updaterecommendedLineUp.entrySet()) {
            if (entry.getKey().intValue() == branchId) {
                output = entry.getValue();
                break;
            }
        }
        return output;
    }

    public Map<AtomicInteger, Map<Job, AtomicInteger>[][]> getUpdaterecommendedLineUp() {
        return updaterecommendedLineUp;
    }

    public Map<Job, AtomicInteger>[][] getRecommendedLineUpDrivers() {
        return recommendedLineUpDrivers;
    }

    public void setUpDriver(int i, ShiftType shiftType, Job license) {
        int j = shiftType.equals(ShiftType.Day) ? 0 : 1;
        for (Map.Entry<Job, AtomicInteger> entry : recommendedLineUpDrivers[i][j].entrySet()) {
            if (entry.getKey().equals(license)) {
                AtomicInteger atomicInteger = entry.getValue();
                atomicInteger.set(atomicInteger.intValue() + 1);
                break;
            }
        }
    }

    public int getDriverNumber(int j, int k, Job driverLicense) {
        int output = 0;
        for (Map.Entry<Job, AtomicInteger> entry : recommendedLineUpDrivers[j][k].entrySet()) {
            if (entry.getKey().equals(driverLicense)) {
                output = entry.getValue().intValue();
            }
        }
        return output;
    }
}
