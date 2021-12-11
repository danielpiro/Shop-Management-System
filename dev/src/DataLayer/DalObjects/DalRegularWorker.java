package DataLayer.DalObjects;

import Bussiness_Layer.EmployeePackage.BankInfo;
import Bussiness_Layer.EmployeePackage.Driver;
import Bussiness_Layer.EmployeePackage.Employee;
import Bussiness_Layer.EmployeePackage.RegularWorker;
import Bussiness_Layer.Job;
import Bussiness_Layer.ShiftPackage.Shift;
import Bussiness_Layer.TransportationPackage.Location;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class DalRegularWorker extends DalEmployee {
    private boolean[][] personalConstraints;
    private boolean shiftManager;
    private List<Job> certifiedWorks;
    private Map<Date, Shift> personalShiftRec;

    public DalRegularWorker(String f_Name, String l_Name, int id, int wage, double monthlyDayOffs, double monthlySickDays, int bankInfo, String bankBranch, int account, int advancedStudyFunds, int location) {
        super(f_Name, l_Name, id, wage, monthlyDayOffs, monthlySickDays, bankInfo, bankBranch, account, advancedStudyFunds, location);
    }

    public DalRegularWorker(String f_Name, String l_Name, int id, int wage, double monthlyDayOffs, double monthlySickDays, int bankInfo, String bankBranch, int account, int advancedStudyFunds, int location, boolean[][] personalConstraints, boolean shiftManager, List<Job> certifiedWorks, Map<Date, Shift> personalShiftRec) {
        super(f_Name, l_Name, id, wage, monthlyDayOffs, monthlySickDays, bankInfo, bankBranch, account, advancedStudyFunds, location);
        this.personalConstraints = personalConstraints;
        this.shiftManager = shiftManager;
        this.certifiedWorks = certifiedWorks;
        this.personalShiftRec = personalShiftRec;
    }
    public DalRegularWorker(DalEmployee dalEmployee){
        super(dalEmployee.getF_Name(), dalEmployee.getL_Name(), dalEmployee.getId(), dalEmployee.getWage(), dalEmployee.getMonthlyDayOffs(),dalEmployee.getMonthlySickDays(), dalEmployee.getBankID(), dalEmployee.getBranch(), dalEmployee.getAccountNumber(), dalEmployee.getAdvancedStudyFunds(), dalEmployee.getLocation());
    }

    public DalRegularWorker(Employee employee, boolean[][] personalConstraints, boolean shiftManager, List<Job> certifiedWorks, Map<Date, Shift> personalShiftRec) {
        super(employee);
        this.personalConstraints = personalConstraints;
        this.shiftManager = shiftManager;
        this.certifiedWorks = certifiedWorks;
        this.personalShiftRec = personalShiftRec;
    }

    public DalRegularWorker(boolean[][] personalConstraints, boolean shiftManager, List<Job> certifiedWorks, Map<Date, Shift> personalShiftRec) {
        this.personalConstraints = personalConstraints;
        this.shiftManager = shiftManager;
        this.certifiedWorks = certifiedWorks;
        this.personalShiftRec = personalShiftRec;
    }

    public DalRegularWorker() {
    }

    public DalRegularWorker(RegularWorker regularWorker) {
        super(regularWorker.getF_Name(), regularWorker.getL_Name(), regularWorker.getId(), regularWorker.getWage(), regularWorker.getMonthlyDayOffs(), regularWorker.getMonthlySickDays(), regularWorker.getBankInfo().getBankId(), regularWorker.getBankInfo().getBranch(), regularWorker.getBankInfo().getAccountNumber(), regularWorker.getAdvancedStudyFunds(), regularWorker.getBranch());
        this.certifiedWorks = regularWorker.getCertifiedWorks();
        this.personalShiftRec = regularWorker.getPersonalShiftRec();
        this.personalConstraints = regularWorker.getPersonalConstraints();
        this.shiftManager = regularWorker.isShiftManager();
    }

    public DalRegularWorker(Driver driver) {
        super(driver.getF_Name(), driver.getL_Name(), driver.getId(), driver.getWage(), driver.getMonthlyDayOffs(), driver.getMonthlySickDays(), driver.getBankInfo().getBankId(), driver.getBankInfo().getBranch(), driver.getBankInfo().getAccountNumber(), driver.getAdvancedStudyFunds(), driver.getBranch());
        this.certifiedWorks = driver.getCertifiedWorks();
        this.personalShiftRec = driver.getPersonalShiftRec();
        this.personalConstraints = driver.getPersonalConstraints();
        this.shiftManager = driver.isShiftManager();
    }

    public boolean[][] getPersonalConstraints() {
        return personalConstraints;
    }

    public void setPersonalConstraints(boolean[][] personalConstraints) {
        this.personalConstraints = personalConstraints;
    }

    public boolean isShiftManager() {
        return shiftManager;
    }

    public void setShiftManager(boolean shiftManager) {
        this.shiftManager = shiftManager;
    }

    public List<Job> getCertifiedWorks() {
        return certifiedWorks;
    }

    public void setCertifiedWorks(List<Job> certifiedWorks) {
        this.certifiedWorks = certifiedWorks;
    }

    public Map<Date, Shift> getPersonalShiftRec() {
        return personalShiftRec;
    }

    public void setPersonalShiftRec(Map<Date, Shift> personalShiftRec) {
        this.personalShiftRec = personalShiftRec;
    }
}
