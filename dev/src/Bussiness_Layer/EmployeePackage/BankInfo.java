package Bussiness_Layer.EmployeePackage;

public class BankInfo {

    private int bankId;
    private String branch;
    private int accountNumber;

    public BankInfo(int bankId, String branch, int accountNumber){
        this.bankId = bankId;
        this.branch = branch;
        this.accountNumber = accountNumber;
    }

    public BankInfo(BankInfo bankInfo) {
        this.bankId = bankInfo.bankId;
        this.accountNumber = bankInfo.accountNumber;
        this.branch = bankInfo.branch;
    }

    public int getBankId() {
        return bankId;
    }

    public String getBranch() {
        return branch;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

}
