package DataLayer.DalObjects;

import Bussiness_Layer.EmployeePackage.RegularWorker;
import Bussiness_Layer.Job;
import Bussiness_Layer.ShiftPackage.Shift;
import Bussiness_Layer.Utility;
import org.w3c.dom.events.UIEvent;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class DalShift {
    private int type;
    private int day;
    private int managerId;
    private Date date;
    private Map<Integer, Job> regularWorkerList;
    private int barnchId;


    public DalShift(int type, int day, int managerId, Map<DalRegularWorker, Job> regularWorkerList) {
        this.type = type;
        this.day = day;
        this.managerId = managerId;
    }

    public DalShift() {
        regularWorkerList = new HashMap<>();
    }

    public Map<Integer, Job> getRegularWorkerList() {
        return regularWorkerList;
    }

    public DalShift(Shift shift, Date date) {
        this.day = shift.getDay();
        this.type = Utility.checkShift(shift.getType());
        this.managerId = shift.getShiftManager().getId();
        this.barnchId = shift.getBranchID();
        this.date = date;
        regularWorkerList = shift.getRegularWorkerMap();
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getManagerId() {
        return managerId;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }

    public Date getDate() {
        return date;
    }

    /*public void setRegularWorkerList(Map<RegularWorker, Job> regularWorkerList) {
        this.regularWorkerList = regularWorkerList;
    }
    */

    public void addRegularWorker(int id, String job) {
        regularWorkerList.putIfAbsent(new Integer(id), Utility.parseJob(job));
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getBarnchId() {
        return barnchId;
    }

    public void setBarnchId(int barnchId) {
        this.barnchId = barnchId;
    }
}
