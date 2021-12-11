package Bussiness_Layer.EmployeePackage;


import Bussiness_Layer.ShiftPackage.Shift;
import DataLayer.DalObjects.DalEmployee;
import Facade_Layer.FacadeObjects.FacadeTPM;

import java.util.Date;

public class TransportationManager extends Employee {

    protected TransportationManager(String f_Name, String l_Name, int id, int wage, double monthlyDayOff, double monthlySickDays, BankInfo bankInfo, int advancedStudyFunds, int branch) {
        super(f_Name, l_Name, id, wage, monthlyDayOff, monthlySickDays, bankInfo, advancedStudyFunds, branch);
    }

    //TODO: need to implement
    public TransportationManager(FacadeTPM facadeHumanResourceManager) {  //create new HumanResourceManager
        super(facadeHumanResourceManager.getF_Name(), facadeHumanResourceManager.getL_Name(), facadeHumanResourceManager.getId(), facadeHumanResourceManager.getWage(), facadeHumanResourceManager.getMonthlyDayOffs(), facadeHumanResourceManager.getMonthlySickDays(), facadeHumanResourceManager.getBankInfo(), facadeHumanResourceManager.getAdvancedStudyFunds(), facadeHumanResourceManager.getBranch());
    }

    public TransportationManager(DalEmployee dalEmployee) {  //create new HumanResourceManager
        super(dalEmployee.getF_Name(), dalEmployee.getL_Name(), dalEmployee.getId(), dalEmployee.getWage(), dalEmployee.getMonthlyDayOffs(), dalEmployee.getMonthlySickDays(), new BankInfo(dalEmployee.getBankID(), dalEmployee.getBranch(), dalEmployee.getAccountNumber()), dalEmployee.getAdvancedStudyFunds(), dalEmployee.getLocation());
    }

    @Override
    public String thisWeekShifts() throws Exception {
        return null;
    }

    @Override
    public String nextWeekShifts() throws Exception {
        return null;
    }

    @Override
    public Shift getSpecificShift(Date date, int i) throws Exception {
        return null;
    }
}
