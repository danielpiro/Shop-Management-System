package Facade_Layer.FacadeObjects;

import Bussiness_Layer.EmployeePackage.BankInfo;
import Bussiness_Layer.EmployeePackage.Employee;

public abstract class FacadeEmployee {


    protected String f_Name;
    protected String l_Name;
    protected int id;
    protected int wage;
    protected double monthlyDayOffs;
    protected double monthlySickDays;
    protected BankInfo bankInfo;
    protected int advancedStudyFunds;
    protected int branch;

    protected FacadeEmployee(String f_Name, String l_Name, int id,
                             int wage, double monthlyDayOff, double monthlySickDays, BankInfo bankInfo, int advancedStudyFunds, int branch) {
        this.f_Name = f_Name;
        this.l_Name = l_Name;
        this.id = id;
        this.wage = wage;
        this.monthlyDayOffs = monthlyDayOff;
        this.monthlySickDays = monthlySickDays;
        this.bankInfo = bankInfo;
        this.advancedStudyFunds = advancedStudyFunds;
        this.branch = branch;
    }

    public FacadeEmployee(Employee other) {
        this.f_Name = other.getF_Name();
        this.l_Name = other.getF_Name();
        this.id = other.getId();
        this.wage = other.getWage();
        this.monthlyDayOffs = other.getMonthlyDayOffs();
        this.monthlySickDays = other.getMonthlySickDays();
        this.bankInfo = other.getBankInfo();
        this.advancedStudyFunds = other.getAdvancedStudyFunds();
    }

    public void setBranch(int branch) {
        this.branch = branch;
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
        return (f_Name + " " + l_Name);
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
        return branch;
    }

/*
    public abstract String thisWeekShifts() throws Exception;

    public abstract String nextWeekShifts() throws Exception;

    public abstract String getSpecificShift(Date date) throws Exception;
 */
}

