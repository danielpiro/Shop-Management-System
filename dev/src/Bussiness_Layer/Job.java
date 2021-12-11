package Bussiness_Layer;

import java.util.Comparator;

public enum Job {
    Cashier(0),
    Guard(0),
    StoreKeeper(0),
    Usher(0),
    DriverLicenseA(5),
    DriverLicenseB(10),
    DriverLicenseC(15),
    DriverLicenseD(20);

    private final int licenseWeight;
    Job(int license){
        licenseWeight = license;
    }
    int getLicenseWeight(){ return licenseWeight; }
    public static Comparator<Job> licenseCompare = new Comparator<Job>() {
        public int compare(Job d1, Job d2) {
            return d1.getLicenseWeight() - d2.getLicenseWeight();
        }
    };
    public static boolean isEqual(Job A ,Job B){return licenseCompare.compare(A, B) == 0;}
    public static boolean isBigger(Job A ,Job B){
        return licenseCompare.compare(A, B) > 0;
    }
    public static Job plus1(Job A){
        if (!(licenseCompare.compare(A, Cashier) > 0))
            return A;

        else if ((licenseCompare.compare(A, DriverLicenseA) == 0))
            return DriverLicenseB;

        else if ((licenseCompare.compare(A, DriverLicenseB) == 0))
            return DriverLicenseC;

        else if ((licenseCompare.compare(A, DriverLicenseC) == 0))
            return DriverLicenseD;

        else
            return DriverLicenseD;
    }
}
