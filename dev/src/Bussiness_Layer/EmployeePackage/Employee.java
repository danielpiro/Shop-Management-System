package Bussiness_Layer.EmployeePackage;

import Bussiness_Layer.ShiftPackage.Shift;
import Bussiness_Layer.TransportationPackage.Location;

import java.util.Date;

public abstract class Employee {

    protected String f_Name;
    protected String l_Name;
    protected int id;
    protected int wage;
    protected double monthlyDayOffs;
    protected double monthlySickDays;
    protected BankInfo bankInfo;
    protected int advancedStudyFunds;
    protected int location;

    protected Employee(String f_Name, String l_Name, int id,
                       int wage, double monthlyDayOff, double monthlySickDays, BankInfo bankInfo, int advancedStudyFunds, int location) {
        this.f_Name = f_Name;
        this.l_Name = l_Name;
        this.id = id;
        this.wage = wage;
        this.monthlyDayOffs = monthlyDayOff;
        this.monthlySickDays = monthlySickDays;
        this.bankInfo = bankInfo;
        this.advancedStudyFunds = advancedStudyFunds;
        this.location = location;
    }

    protected void updateHRM(String f_Name, String l_Name, int id,
                             int wage, double monthlyDayOff, double monthlySickDays, BankInfo bankInfo, int advancedStudyFunds, int location) {
        this.f_Name = f_Name;
        this.l_Name = l_Name;
        this.id = id;
        this.wage = wage;
        this.monthlyDayOffs = monthlyDayOff;
        this.monthlySickDays = monthlySickDays;
        this.bankInfo = bankInfo;
        this.advancedStudyFunds = advancedStudyFunds;
        this.location = location;
    }

    public void setBranch(int location) {
        this.location = location;
    }

    public void setF_Name(String f_Name) {
        this.f_Name = f_Name;
    }

    public void setL_Name(String l_Name) {
        this.l_Name = l_Name;
    }

    public void setWage(int wage) {
        this.wage = wage;
    }

    public void setMonthlyDayOffs(double monthlyDayOffs) {
        this.monthlyDayOffs = monthlyDayOffs;
    }

    public void setMonthlySickDays(double monthlySickDays) {
        this.monthlySickDays = monthlySickDays;
    }

    public void setBankInfo(BankInfo bankInfo) {
        this.bankInfo = bankInfo;
    }

    public void setAdvancedStudyFunds(int advancedStudyFunds) {
        this.advancedStudyFunds = advancedStudyFunds;
    }

    public String getF_Name() {
        return f_Name;
    }

    public String getL_Name() {
        return l_Name;
    }

    public String toString() {
        return (f_Name + " " + l_Name + " " + id);
    }

    public int getId() {
        return id;
    }

    public double getMonthlyDayOffs() {
        return monthlyDayOffs;
    }

    public double getMonthlySickDays() {
        return monthlySickDays;
    }

    public int getAdvancedStudyFunds() {
        return advancedStudyFunds;
    }

    public int getWage() {
        return wage;
    }

    public BankInfo getBankInfo() {
        return new BankInfo(bankInfo);
    }

    public int getBranch() {
        return location;
    }

    public abstract String thisWeekShifts() throws Exception;

    public abstract String nextWeekShifts() throws Exception;

    public abstract Shift getSpecificShift(Date date, int i) throws Exception;

    public int getLocation() { return location; }
}

