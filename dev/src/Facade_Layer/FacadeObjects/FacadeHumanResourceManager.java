package Facade_Layer.FacadeObjects;

import Bussiness_Layer.EmployeePackage.BankInfo;
import Bussiness_Layer.EmployeePackage.HumanResourceManager;

public class FacadeHumanResourceManager extends FacadeEmployee {

    public FacadeHumanResourceManager(String f_Name, String l_Name, int id,
                                      int wage, double monthlyDayOff, double monthlySickDays, BankInfo bankInfo, int advancedStudyFunds, int workplace) {
        super(f_Name, l_Name, id, wage, monthlyDayOff, monthlySickDays, bankInfo, advancedStudyFunds, workplace);
    }

    public FacadeHumanResourceManager(HumanResourceManager shiftManager) {
        super(shiftManager.getF_Name(), shiftManager.getL_Name(), shiftManager.getId(), shiftManager.getWage(), shiftManager.getMonthlyDayOffs(), shiftManager.getMonthlySickDays(), shiftManager.getBankInfo(), shiftManager.getAdvancedStudyFunds(), shiftManager.getBranch());
    }
    /*
    public String thisWeekShifts() throws Exception {
        String toRet;
        if(LocalDate.now().getDayOfWeek() == DayOfWeek.SUNDAY) {
            toRet =  weekShiftSideFunc(LocalDate.now());
        }
        else {
            toRet = weekShiftSideFunc(LocalDate.now().with(TemporalAdjusters.previous( DayOfWeek.SUNDAY)));
        }
        if(toRet.length() != 0){
            return toRet;
        }
        return "No shifts this week.";
    }

    public String nextWeekShifts() throws Exception {
        if(!(LocalDate.now().getDayOfWeek() == DayOfWeek.THURSDAY || LocalDate.now().getDayOfWeek() == DayOfWeek.FRIDAY || LocalDate.now().getDayOfWeek() == DayOfWeek.SATURDAY)){
            return "Shifts haven't been made yet, please wait until Thursday.";
        }
        String toRet;
        if(LocalDate.now().getDayOfWeek() == DayOfWeek.SUNDAY) {
            toRet =  weekShiftSideFunc(LocalDate.now());
        }
        else {
            toRet = weekShiftSideFunc(LocalDate.now().with(TemporalAdjusters.next( DayOfWeek.SUNDAY)));
        }
        if(toRet.length() != 0){
            return toRet;
        }
        return "No shifts for next week.";
    }

    public String getSpecificShift(Date date) throws Exception {
        String toRet = "";
        Shift shift = ShiftController.getInstance().getShift(new Tuple<>(date, ShiftType.Day));
        if (shift != null){
            toRet +=shift.toString();
        }
        shift = ShiftController.getInstance().getShift(new Tuple<>(date, ShiftType.Day));
        if (shift != null){
            toRet += shift.toString();
        }
        if (toRet.length() == 0) {
            toRet = "No shifts on this date.";
        }
        return toRet;
    }

    private String weekShiftSideFunc(LocalDate localDate) throws Exception {
        String toRet = "";
        Shift shift;
        for(int i = 0; i < 7; i++,localDate = localDate.plusDays(1)){
            shift = ShiftController.getInstance().getShift(new Tuple<>((Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())), ShiftType.Day));
            if(shift != null){
                toRet += localDate+": "+ shift.toString()+'\n';
            }
            shift = ShiftController.getInstance().getShift(new Tuple<>((Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())), ShiftType.Night));
            if(shift != null){
                toRet += localDate+": "+ shift.toString()+'\n';;
            }
        }
        return toRet;
    }
     */
}
