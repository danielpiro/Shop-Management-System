package Bussiness_Layer.EmployeePackage;

import Bussiness_Layer.ShiftPackage.Shift;
import Bussiness_Layer.ShiftPackage.ShiftController;
import Bussiness_Layer.ShiftPackage.ShiftType;
import Bussiness_Layer.Tuple;
import Bussiness_Layer.Utility;
import DataLayer.DalObjects.DalEmployee;
import Facade_Layer.FacadeObjects.FacadeHumanResourceManager;
import Facade_Layer.FacadeObjects.FacadeStoreManager;

import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

public class StoreManager extends Employee {

    public StoreManager(String f_Name, String l_Name, int id,
                        int wage, double monthlyDayOff, double monthlySickDays, BankInfo bankInfo, int advancedStudyFunds, int branch) {
        super(f_Name, l_Name, id, wage, monthlyDayOff, monthlySickDays, bankInfo, advancedStudyFunds, branch);
    }

    public StoreManager(FacadeHumanResourceManager facadeHumanResourceManager) {  //create new HumanResourceManager
        super(facadeHumanResourceManager.getF_Name(), facadeHumanResourceManager.getL_Name(), facadeHumanResourceManager.getId(), facadeHumanResourceManager.getWage(), facadeHumanResourceManager.getMonthlyDayOffs(), facadeHumanResourceManager.getMonthlySickDays(), facadeHumanResourceManager.getBankInfo(), facadeHumanResourceManager.getAdvancedStudyFunds(), facadeHumanResourceManager.getBranch());
    }
    public StoreManager(FacadeStoreManager facadeHumanResourceManager) {  //create new HumanResourceManager
        super(facadeHumanResourceManager.getF_Name(), facadeHumanResourceManager.getL_Name(), facadeHumanResourceManager.getId(), facadeHumanResourceManager.getWage(), facadeHumanResourceManager.getMonthlyDayOffs(), facadeHumanResourceManager.getMonthlySickDays(), facadeHumanResourceManager.getBankInfo(), facadeHumanResourceManager.getAdvancedStudyFunds(), facadeHumanResourceManager.getBranch());
    }

    public StoreManager(DalEmployee dalEmployee) {  //create new HumanResourceManager
        super(dalEmployee.getF_Name(), dalEmployee.getL_Name(), dalEmployee.getId(), dalEmployee.getWage(), dalEmployee.getMonthlyDayOffs(), dalEmployee.getMonthlySickDays(), new BankInfo(dalEmployee.getBankID(), dalEmployee.getBranch(), dalEmployee.getAccountNumber()), dalEmployee.getAdvancedStudyFunds(), dalEmployee.getLocation());
    }

    public void updateSM(FacadeHumanResourceManager facadeHumanResourceManager) {
        updateHRM(facadeHumanResourceManager.getF_Name(), facadeHumanResourceManager.getL_Name(), facadeHumanResourceManager.getId(), facadeHumanResourceManager.getWage(), facadeHumanResourceManager.getMonthlyDayOffs(), facadeHumanResourceManager.getMonthlySickDays(), facadeHumanResourceManager.getBankInfo(), facadeHumanResourceManager.getAdvancedStudyFunds(), facadeHumanResourceManager.getBranch());
    }

    public String thisWeekShifts() throws Exception {
        String toRet;
        if (LocalDate.now().getDayOfWeek() == DayOfWeek.SUNDAY) {
            toRet = weekShiftSideFunc(LocalDate.now());
        } else {
            toRet = weekShiftSideFunc(LocalDate.now().with(TemporalAdjusters.previous(DayOfWeek.SUNDAY)));
        }
        if (toRet.length() != 0) {
            return toRet;
        }
        return "No shifts this week.";
    }

    public String nextWeekShifts() throws Exception {
        /*if(!(LocalDate.now().getDayOfWeek() == DayOfWeek.THURSDAY || LocalDate.now().getDayOfWeek() == DayOfWeek.FRIDAY || LocalDate.now().getDayOfWeek() == DayOfWeek.SATURDAY)){
            return "Shifts haven't been made yet, please wait until Thursday.";
        }*/
        String toRet;
        if (LocalDate.now().getDayOfWeek() == DayOfWeek.SUNDAY) {
            toRet = weekShiftSideFunc(LocalDate.now());
        } else {
            toRet = weekShiftSideFunc(LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.SUNDAY)));
        }
        if (toRet.length() != 0) {
            return toRet;
        }
        return "No shifts for next week.";
    }

    public Shift getSpecificShift(Date date, int i) throws SQLException {
        String toRet = "";
        Shift shift = ShiftController.getInstance().getShift(new Tuple<>(date, Utility.getShiftType(i)), getBranch());
        if (shift != null) {
            return shift;
        } else return null;
        /*shift = ShiftController.getInstance().getShift(new Tuple<>(date, ShiftType.Day));
        if (shift != null){
            toRet += shift.toString();
        }
        if (toRet.length() == 0) {
            toRet = "No shifts on this date.";
        }
        return toRet;
         */
    }

    private String weekShiftSideFunc(LocalDate localDate) throws Exception {
        String toRet = "";
        Shift shift;
        for (int i = 0; i < 7; i++, localDate = localDate.plusDays(1)) {
            shift = ShiftController.getInstance().getShift(new Tuple<>((Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())), ShiftType.Day), getBranch());
            if (shift != null) {
                toRet += localDate + ": " + shift.toString() + '\n';
            }
            shift = ShiftController.getInstance().getShift(new Tuple<>((Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())), ShiftType.Night), getBranch());
            if (shift != null) {
                toRet += localDate + ": " + shift.toString() + '\n';
                ;
            }
        }
        return toRet;
    }
}
