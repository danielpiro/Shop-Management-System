package DataLayer.DalObjects;

import Bussiness_Layer.Job;
import Bussiness_Layer.ShiftPackage.RecommendedLineUp;
import Bussiness_Layer.TransportationPackage.Location;
import Bussiness_Layer.Utility;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class DalRecommendedLineUp {
    private Map<AtomicInteger, Map<Job, AtomicInteger>[][]> updaterecommendedLineUp;
    private Map<Job, AtomicInteger>[][] recommendedLineUpDrivers;

    public DalRecommendedLineUp() {
        initRecommendedLineUpDrivers();
        initUpdateRecommendedLineUp();
    }

    public DalRecommendedLineUp(RecommendedLineUp recommendedLineUp) {
        this.updaterecommendedLineUp = recommendedLineUp.getUpdaterecommendedLineUp();
        this.recommendedLineUpDrivers = recommendedLineUp.getRecommendedLineUpDrivers();
    }


    public Map<AtomicInteger, Map<Job, AtomicInteger>[][]> getUpdaterecommendedLineUp() {
        return updaterecommendedLineUp;
    }

    public Map<Job, AtomicInteger>[][] getRecommendedLineUpDrivers() {
        return recommendedLineUpDrivers;
    }

    private void initUpdateRecommendedLineUp() {
        updaterecommendedLineUp = new HashMap<>();
        for(int k=1;k<10;k++){
            Map<Job, AtomicInteger>[][] temp = new HashMap[7][2];
            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < 2; j++) {
                    temp[i][j] = new HashMap<>();
                }
            }
            updaterecommendedLineUp.putIfAbsent(new AtomicInteger(k * 111), temp);
        }
    }

    private void initRecommendedLineUpDrivers() {
        recommendedLineUpDrivers = new HashMap[7][2];
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 2; j++) {
                recommendedLineUpDrivers[i][j] = new HashMap<>();
            }
        }
    }

    public void add(int i, String shiftType, String job1, int numberOfEmployee, int branchID) {
        Job job = Utility.parseJob(job1);
        int j = Utility.checkShift(Utility.getshiftType(shiftType));
        for (Map.Entry<AtomicInteger, Map<Job, AtomicInteger>[][]> entry : updaterecommendedLineUp.entrySet()) {
            if (entry.getKey().intValue() == branchID) {
                Map<Job, AtomicInteger> temp =entry.getValue()[i][j];
                temp.putIfAbsent(job,new AtomicInteger(numberOfEmployee));
                break;
            }
        }
    }

    public void add(int i, String shiftType, String job1, int numberOfEmployee) {
        Job job = Utility.getLicense(job1);
        int j = Utility.checkShift(Utility.getshiftType(shiftType));
        recommendedLineUpDrivers[i][j].putIfAbsent(job, new AtomicInteger(numberOfEmployee));
    }
}
