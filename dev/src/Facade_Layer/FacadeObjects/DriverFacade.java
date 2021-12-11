package Facade_Layer.FacadeObjects;


import Bussiness_Layer.EmployeePackage.BankInfo;
import Bussiness_Layer.Job;

import java.util.List;

public class DriverFacade extends FacadeRegularWorker {

    public DriverFacade(String f_Name, String l_Name, int id, int wage, double monthlyDayOff, double monthlySickDays, BankInfo bankInfo, int advancedStudyFunds, int branch, boolean isSM , List<Job> jobList) {
        super(f_Name, l_Name, id, wage, monthlyDayOff, monthlySickDays, bankInfo, advancedStudyFunds, branch,isSM,jobList);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
