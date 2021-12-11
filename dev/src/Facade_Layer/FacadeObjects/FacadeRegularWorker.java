package Facade_Layer.FacadeObjects;

import Bussiness_Layer.EmployeePackage.BankInfo;
import Bussiness_Layer.EmployeePackage.RegularWorker;
import Bussiness_Layer.Job;
import Bussiness_Layer.ShiftPackage.ShiftType;
import Bussiness_Layer.Temporary_data_structure;
import Bussiness_Layer.Utility;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FacadeRegularWorker extends FacadeEmployee {

    private boolean[][] personalConstraints;
    private boolean shiftManager;
    private List<Job> certifiedWorks;
    private Map<Date, FacadeShift> personalShiftRec;

    public FacadeRegularWorker(String f_Name, String l_Name, int id,
                               int wage, double monthlyDayOff, double monthlySickDays, BankInfo bankInfo, int advancedStudyFunds, int branch, boolean shiftManager,
                               List<Job> certifiedWorks) {
        super(f_Name, l_Name, id, wage, monthlyDayOff, monthlySickDays, bankInfo, advancedStudyFunds, branch);
        this.shiftManager = shiftManager;
        personalConstraints = new boolean[7][2]; //constraints initialized to false;
        this.certifiedWorks = certifiedWorks;
        personalShiftRec = new HashMap<>();
    }

    public boolean[][] getPersonalConstraints() {
        return personalConstraints;
    }

    public FacadeRegularWorker(String f_Name, String l_Name, int id,
                               int wage, double monthlyDayOff, double monthlySickDays, BankInfo bankInfo, int advancedStudyFunds, int branch, boolean shiftManager,
                               List<Job> certifiedWorks, boolean[][] personalConstraints, Object object) {
        super(f_Name, l_Name, id, wage, monthlyDayOff, monthlySickDays, bankInfo, advancedStudyFunds, branch);
        this.certifiedWorks = certifiedWorks;
        personalShiftRec = new HashMap<>();
        this.shiftManager = shiftManager;
        if (object instanceof Temporary_data_structure) {
            this.personalConstraints = new boolean[7][2];
            this.personalConstraints = personalConstraints; //constraints initialized to false;
        } else {
            personalConstraints = new boolean[7][2]; //constraints initialized to false;
        }
    }

    public FacadeRegularWorker(RegularWorker shiftManager) {
        super(shiftManager.getF_Name(), shiftManager.getL_Name(), shiftManager.getId(), shiftManager.getWage(), shiftManager.getMonthlyDayOffs(), shiftManager.getMonthlySickDays(), shiftManager.getBankInfo(), shiftManager.getAdvancedStudyFunds(), shiftManager.getBranch());
        this.shiftManager = shiftManager.isShiftManager();
        this.certifiedWorks = shiftManager.getCertifiedWorks();
        personalConstraints = shiftManager.getPersonalConstraints();
        personalShiftRec = new HashMap<>();
    }

    /*
    public String thisWeekShifts(){
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
     */
    /*
    public String nextWeekShifts(){
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
     */
    /*
    public String getSpecificShift(Date date){
        FacadeShift shift = personalShiftRec.get(date);
        if (shift != null){
            return shift.toString();
        }
        return "you have not worked in the aforementioned date.";
    }
     */
    private String weekShiftSideFunc(LocalDate localDate) {
        String toRet = "";
        for (int i = 0; i < 7; i++, localDate = localDate.plusDays(1)) {
            FacadeShift shift = personalShiftRec.get(Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            if (shift != null) {
                toRet += localDate + ": " + shift.toString() + '\n';
            }
        }
        return toRet;
    }

    public boolean option1(boolean[][] processedConstraints) {//Change Multiple constraint
        return changeConstraintsMultiple(processedConstraints);
    }

    public String showConstraints() {
        String output = "";
        for (int i = 0; i < 7; i++) {
            output += Utility.dayOfWeek(i) + ":  shift: " + Utility.shifttype(0) + ", can work?  " + personalConstraints[i][0] + " | ";
            output += " shift: " + Utility.shifttype(1) + ", can work?  " + personalConstraints[i][1] + '\n';
        }
        return output;
    }

    public boolean option2(int dayOfWeek, int shiftType, boolean constraint) {//Change single Constraint
        return changeConstraintsSingle(dayOfWeek, shiftType, constraint);
    }


    public boolean checkAvailability(int day, ShiftType shift) {
        int iShift = Utility.checkShift(shift);
        return personalConstraints[day][iShift];
    }


    private boolean changeConstraintsMultiple(boolean[][] constraints) {
        personalConstraints = constraints;
        return true;
    }

    private boolean changeConstraintsSingle(int day, int shift, boolean constraint) {
        try {
            personalConstraints[day][shift] = constraint;
        } catch (RuntimeException e) {
            return false;
        }
        return true;
    }

    public List<Job> getCertifiedWorks() {
        return certifiedWorks;
    }

    public boolean isShiftManager() {
        return shiftManager;
    }

    public Map<Date, FacadeShift> getPersonalShiftRec() {
        return personalShiftRec;
    }

    public boolean checkIfOccupiedInDate(Date date) {
        return personalShiftRec.get(date) == null;
    }

    public void insertShiftRecord(Date date, FacadeShift shift) {
        personalShiftRec.putIfAbsent(date, shift);
    }

    public void removeShift(FacadeShift shift) {
        for (Map.Entry<Date, FacadeShift> run : personalShiftRec.entrySet()) {
            FacadeShift temp = run.getValue();
            if (temp.cmp(shift)) {
                personalShiftRec.remove(run.getKey());
                break;
            }
        }
    }

    @Override
    public String toString() {
        return "FacadeRegularWorker{" +super.toString()+
                "certifiedWorks=" + certifiedWorks +
                '}';
    }
}
