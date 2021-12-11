package Bussiness_Layer.EmployeePackage;

import Bussiness_Layer.*;
import Bussiness_Layer.TransportationPackage.Location;
import DataLayer.DalObjects.DalRegularWorker;
import Facade_Layer.FacadeObjects.DriverFacade;
import Facade_Layer.FacadeObjects.FacadeRegularWorker;

import java.util.HashMap;
import java.util.List;

public class Driver extends RegularWorker {
    public Driver(String f_Name, String l_Name, int id,
                  int wage, double monthlyDayOff, double monthlySickDays, BankInfo bankInfo, int advancedStudyFunds, int branch, boolean shiftManager,
                  List<Job> licenseList) {
        super(f_Name, l_Name, id, wage, monthlyDayOff, monthlySickDays, bankInfo, advancedStudyFunds, branch, shiftManager, licenseList);
    }

    public Driver(String f_Name, String l_Name, int id,
                  int wage, double monthlyDayOff, double monthlySickDays, BankInfo bankInfo, int advancedStudyFunds, int branch, boolean shiftManager,
                  List<Job> licenseList, boolean[][] personalConstraints, Object object) {
        super(f_Name, l_Name, id, wage, monthlyDayOff, monthlySickDays, bankInfo, advancedStudyFunds, branch, shiftManager, licenseList, personalConstraints, object);
        this.certifiedWorks = licenseList;
        personalShiftRec = new HashMap<>();
        this.shiftManager = shiftManager;
        if (object instanceof Temporary_data_structure) {
            this.personalConstraints = new boolean[7][2];
            this.personalConstraints = personalConstraints; //constraints initialized to false;
        } else {
            personalConstraints = new boolean[7][2]; //constraints initialized to false;
        }
    }

    public Driver(FacadeRegularWorker facadeRegularWorker) {
        super(facadeRegularWorker.getF_Name(), facadeRegularWorker.getL_Name(), facadeRegularWorker.getId(), facadeRegularWorker.getWage(), facadeRegularWorker.getMonthlyDayOffs(), facadeRegularWorker.getMonthlySickDays(), facadeRegularWorker.getBankInfo(), facadeRegularWorker.getAdvancedStudyFunds(), facadeRegularWorker.getBranch(), facadeRegularWorker.isShiftManager(), facadeRegularWorker.getCertifiedWorks());
        personalConstraints = new boolean[7][2];
        boolean[][] temp = facadeRegularWorker.getPersonalConstraints();
        for (int i = 0; i < temp.length; i++) {
            for (int j = 0; j < temp[i].length; j++) {
                personalConstraints[i][j] = temp[i][j];
            }
        }
    }
    public Driver(DriverFacade driverFacade) {
        super(driverFacade.getF_Name(), driverFacade.getL_Name(), driverFacade.getId(), driverFacade.getWage(), driverFacade.getMonthlyDayOffs(), driverFacade.getMonthlySickDays(), driverFacade.getBankInfo(), driverFacade.getAdvancedStudyFunds(), driverFacade.getBranch(), driverFacade.isShiftManager(), driverFacade.getCertifiedWorks());
        personalConstraints = new boolean[7][2];
        boolean[][] temp = driverFacade.getPersonalConstraints();
        for (int i = 0; i < temp.length; i++) {
            for (int j = 0; j < temp[i].length; j++) {
                personalConstraints[i][j] = temp[i][j];
            }
        }
    }

    public Job getJob(){
        return this.getCertifiedWorks().get(0);
    }
    public Driver(DalRegularWorker dalRegularWorker) {
        super(dalRegularWorker.getF_Name(), dalRegularWorker.getL_Name(), dalRegularWorker.getId(), dalRegularWorker.getWage(), dalRegularWorker.getMonthlyDayOffs(), dalRegularWorker.getMonthlySickDays(), new BankInfo(dalRegularWorker.getBankID(),dalRegularWorker.getBranch(), dalRegularWorker.getAccountNumber()), dalRegularWorker.getAdvancedStudyFunds(), dalRegularWorker.getLocation(), dalRegularWorker.isShiftManager(), dalRegularWorker.getCertifiedWorks());
        personalConstraints = dalRegularWorker.getPersonalConstraints();
        shiftManager = dalRegularWorker.isShiftManager();
        certifiedWorks = dalRegularWorker.getCertifiedWorks();
        personalShiftRec = dalRegularWorker.getPersonalShiftRec();
        if(personalShiftRec == null)
            personalShiftRec = new HashMap<>();
    }
}
