package Bussiness_Layer.EmployeePackage;

import Bussiness_Layer.Job;
import Bussiness_Layer.ShiftPackage.Shift;
import Bussiness_Layer.ShiftPackage.ShiftType;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.time.ZoneId;

import Bussiness_Layer.Temporary_data_structure;
import Bussiness_Layer.TransportationPackage.Location;
import Bussiness_Layer.Utility;
import DataLayer.DalObjects.DalRegularWorker;
import Facade_Layer.FacadeObjects.FacadeRegularWorker;


public class RegularWorker extends Employee {

    protected boolean[][] personalConstraints;
    protected boolean shiftManager;
    protected List<Job> certifiedWorks;
    protected Map<Date, Shift> personalShiftRec;


    public RegularWorker(String f_Name, String l_Name, int id,
                         int wage, double monthlyDayOff, double monthlySickDays, BankInfo bankInfo, int advancedStudyFunds, int location, boolean shiftManager,
                         List<Job> certifiedWorks) {
        super(f_Name, l_Name, id, wage, monthlyDayOff, monthlySickDays, bankInfo, advancedStudyFunds, location);
        this.shiftManager = shiftManager;
        personalConstraints = new boolean[7][2]; //constraints initialized to false;
        this.certifiedWorks = certifiedWorks;
        personalShiftRec = new HashMap<>();
    }

    public RegularWorker(String f_Name, String l_Name, int id,
                         int wage, double monthlyDayOff, double monthlySickDays, BankInfo bankInfo, int advancedStudyFunds, int location, boolean shiftManager,
                         List<Job> certifiedWorks, boolean[][] personalConstraints, Object object) {
        super(f_Name, l_Name, id, wage, monthlyDayOff, monthlySickDays, bankInfo, advancedStudyFunds, location);
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

    public RegularWorker(RegularWorker shiftManager) {
        super(shiftManager.f_Name, shiftManager.l_Name, shiftManager.id, shiftManager.wage, shiftManager.monthlyDayOffs, shiftManager.monthlySickDays, shiftManager.bankInfo, shiftManager.advancedStudyFunds, shiftManager.location);
        personalConstraints = new boolean[7][2];
        boolean[][] temp = shiftManager.personalConstraints;
        for (int i = 0; i < temp.length; i++) {
            for (int j = 0; j < temp[i].length; j++) {
                personalConstraints[i][j] = temp[i][j];
            }
        }
    }

    public RegularWorker(FacadeRegularWorker facadeRegularWorker) {
        super(facadeRegularWorker.getF_Name(), facadeRegularWorker.getL_Name(), facadeRegularWorker.getId(), facadeRegularWorker.getWage(), facadeRegularWorker.getMonthlyDayOffs(), facadeRegularWorker.getMonthlySickDays(), facadeRegularWorker.getBankInfo(), facadeRegularWorker.getAdvancedStudyFunds(), facadeRegularWorker.getBranch());
        personalConstraints = new boolean[7][2];
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 2; j++) {
                personalConstraints[i][j] = true;
            }
        }
        this.certifiedWorks = facadeRegularWorker.getCertifiedWorks();
    }

    public RegularWorker(DalRegularWorker dalRegularWorker) {
        super(dalRegularWorker.getF_Name(), dalRegularWorker.getL_Name(), dalRegularWorker.getId(), dalRegularWorker.getWage(), dalRegularWorker.getMonthlyDayOffs(), dalRegularWorker.getMonthlySickDays(), new BankInfo(dalRegularWorker.getBankID(), dalRegularWorker.getBranch(), dalRegularWorker.getAccountNumber()), dalRegularWorker.getAdvancedStudyFunds(), dalRegularWorker.getLocation());
        personalConstraints = dalRegularWorker.getPersonalConstraints();
        shiftManager = dalRegularWorker.isShiftManager();
        certifiedWorks = dalRegularWorker.getCertifiedWorks();
        personalShiftRec = dalRegularWorker.getPersonalShiftRec();
        if(personalShiftRec == null)
            personalShiftRec = new HashMap<>();
    }

    public boolean[][] getPersonalConstraints() {
        boolean[][] temp = new boolean[7][2];
        for (int i = 0; i < temp.length; i++) {
            for (int j = 0; j < temp[i].length; j++) {
                temp[i][j] = personalConstraints[i][j];
            }
        }
        return temp;
    }

    public String thisWeekShifts() {
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

    public String nextWeekShifts() {
        if (!(LocalDate.now().getDayOfWeek() == DayOfWeek.THURSDAY || LocalDate.now().getDayOfWeek() == DayOfWeek.FRIDAY || LocalDate.now().getDayOfWeek() == DayOfWeek.SATURDAY)) {
            return "Shifts haven't been made yet, please wait until Thursday.";
        }
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

    public Shift getSpecificShift(Date date, int i) {
        Shift shift = personalShiftRec.get(date);
        if (shift != null) {
            return shift;
        }
        return null;
    }

    private String weekShiftSideFunc(LocalDate localDate) {
        String toRet = "";
        for (int i = 0; i < 7; i++, localDate = localDate.plusDays(1)) {
            Shift shift = personalShiftRec.get(Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
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

    public Map<Date, Shift> getPersonalShiftRec() {
        return personalShiftRec;
    }

    public boolean checkIfOccupiedInDate(Date date) {
        return personalShiftRec.get(date) == null;
    }

    public void insertShiftRecord(Date date, Shift shift) {
        personalShiftRec.put(date, shift);
    }

    public void removeShift(Shift shift) {
        for (Map.Entry<Date, Shift> run : personalShiftRec.entrySet()) {
            Shift temp = run.getValue();
            if (temp.cmp(shift)) {
                personalShiftRec.remove(run.getKey());
                break;
            }
        }
    }
}
