package DataLayer.DalObjects;

import Bussiness_Layer.EmployeePackage.BankInfo;
import Bussiness_Layer.EmployeePackage.Employee;
import Bussiness_Layer.TransportationPackage.Location;


public class DalEmployee {


    private String f_Name;
    private String l_Name;
    private int id;
    private int wage;
    private double monthlyDayOffs;
    private double monthlySickDays;
    private int bankID;
    private String branch;
    private int accountNumber;
    private int advancedStudyFunds;
    private int location;

    public DalEmployee(String f_Name, String l_Name, int id, int wage, double monthlyDayOffs, double monthlySickDays, int bankInfo, String bankBranch, int accountNumber, int advancedStudyFunds, int location) {
        this.f_Name = f_Name;
        this.l_Name = l_Name;
        this.id = id;
        this.wage = wage;
        this.monthlyDayOffs = monthlyDayOffs;
        this.monthlySickDays = monthlySickDays;
        this.bankID = bankInfo;
        this.branch = bankBranch;
        this.accountNumber = accountNumber;
        this.advancedStudyFunds = advancedStudyFunds;
        this.location = location;
    }

    public DalEmployee(Employee employee) {
        this.id = employee.getId();
        this.location = employee.getBranch();
        this.l_Name = employee.getL_Name();
        this.monthlyDayOffs = employee.getMonthlyDayOffs();
        this.monthlySickDays = employee.getMonthlySickDays();
        this.wage = employee.getWage();
        this.f_Name = employee.getF_Name();
        this.bankID = employee.getBankInfo().getBankId();
        this.branch = employee.getBankInfo().getBranch();
        this.accountNumber = employee.getBankInfo().getAccountNumber();
        this.advancedStudyFunds = employee.getAdvancedStudyFunds();
    }

    public DalEmployee() {
    }

    public String getF_Name() {
        return f_Name;
    }

    public void setF_Name(String f_Name) {
        this.f_Name = f_Name;
    }

    public String getL_Name() {
        return l_Name;
    }

    public void setL_Name(String l_Name) {
        this.l_Name = l_Name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWage() {
        return wage;
    }

    public void setWage(int wage) {
        this.wage = wage;
    }

    public double getMonthlyDayOffs() {
        return monthlyDayOffs;
    }

    public void setMonthlyDayOffs(double monthlyDayOffs) {
        this.monthlyDayOffs = monthlyDayOffs;
    }

    public double getMonthlySickDays() {
        return monthlySickDays;
    }

    public void setMonthlySickDays(double monthlySickDays) {
        this.monthlySickDays = monthlySickDays;
    }

    public int getAdvancedStudyFunds() {
        return advancedStudyFunds;
    }

    public void setAdvancedStudyFunds(int advancedStudyFunds) {
        this.advancedStudyFunds = advancedStudyFunds;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public int getBankID() {
        return bankID;
    }

    public void setBankID(int bankID) {
        this.bankID = bankID;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }
}
