package Bussiness_Layer;

import Bussiness_Layer.EmployeePackage.RegularWorker;
import Bussiness_Layer.ShiftPackage.ShiftType;

import java.io.BufferedReader;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import static java.time.temporal.TemporalAdjusters.next;

public class Utility {

    public static LocalDate getUpcomingDate(DayOfWeek day) {
        LocalDate shiftDate = LocalDate.now().with(next(day));
        return shiftDate;
    }

    public  static LocalDate incrementLocalDate(LocalDate localDate){
        localDate = localDate.plusDays(1);
        return localDate;
    }

    public static boolean compareLocalDate(LocalDate A, LocalDate B) {
        ZoneId defaultZoneId = ZoneId.systemDefault();
        return Date.from(A.atStartOfDay(defaultZoneId).toInstant()).equals(Date.from(B.atStartOfDay(defaultZoneId).toInstant()));
    }

    public static ShiftType getShiftType(int i) {
        switch (i) {
            case 0:
                return ShiftType.Day;
            case 1:
                return ShiftType.Night;
            default:
                return null;
        }
    }

    public static ShiftType getshiftType(String i) {
        i = i.toLowerCase();
        switch (i) {
            case "day":
                return ShiftType.Day;
            case "night":
                return ShiftType.Night;
            default:
                return null;
        }
    }

    public static int checkShiftStr(String shift) throws Exception {
        int shiftType = -1;
        shift = shift.toLowerCase();
        switch (shift) {
            case "day":
                shiftType = 0;
                break;
            case "night":
                shiftType = 1;
                break;
            default:
                throw new Exception("Invalid input of shift! Please retry entering a valid input");
        }
        return shiftType;
    }

    public static String shifttype(int i) {
        if (i == 0)
            return "Day";
        if (i == 1)
            return "Night";
        return null;
    }

    public static int checkDayStr(String day) throws Exception {
        day = day.toLowerCase();
        int dayOfWeek = -1;
        switch (day) {
            case "sunday":
                dayOfWeek = 0;
                break;
            case "monday":
                dayOfWeek = 1;
                break;
            case "tuesday":
                dayOfWeek = 2;
                break;
            case "wednesday":
                dayOfWeek = 3;
                break;
            case "thursday":
                dayOfWeek = 4;
                break;
            case "friday":
                dayOfWeek = 5;
                break;
            case "saturday":
                dayOfWeek = 6;
                break;
            default:
                throw new Exception("Invalid input of day! please retry entering a valid input");
        }
        return dayOfWeek;
    }

    public static int checkShift(ShiftType shift) {
        int shiftType = -1;
        switch (shift) {
            case Day:
                shiftType = 0;
                break;
            case Night:
                shiftType = 1;
                break;
            default:
                System.out.println("Invalid input of shift! Please retry entering a valid input");
                break;
        }
        return shiftType;
    }

    public static int checkDay(DayOfWeek day) {
        int dayOfWeek = -1;
        switch (day) {
            case SUNDAY:
                dayOfWeek = 0;
                break;
            case MONDAY:
                dayOfWeek = 1;
                break;
            case TUESDAY:
                dayOfWeek = 2;
                break;
            case WEDNESDAY:
                dayOfWeek = 3;
                break;
            case THURSDAY:
                dayOfWeek = 4;
                break;
            case FRIDAY:
                dayOfWeek = 5;
                break;
            case SATURDAY:
                dayOfWeek = 6;
                break;
            default:
                System.out.println("Invalid input of day! please retry entering a valid input");
        }
        return dayOfWeek;
    }

    public static boolean decipherConstraint(String cons) throws Exception {
        cons = cons.toLowerCase();
        boolean isCan = (cons.equals("can")) ? true : false;
        boolean isCannot = (cons.equals("cannot")) ? true : false;
        if (isCan) {
            return true; //meaning can work
        } else if (isCannot) {
            return false; //meaning can work
        } else {
            throw new Exception("invalid constraint input");
        }
    }

    public static Job getLicense(String s) {
        s = s.toLowerCase();
        switch (s) {
            case "a":
                return Job.DriverLicenseA;
            case "b":
                return Job.DriverLicenseB;
            case "c":
                return Job.DriverLicenseC;
            case "d":
                return Job.DriverLicenseD;
            case "driverlicensea":
                return Job.DriverLicenseA;
            case "driverlicenseb":
                return Job.DriverLicenseB;
            case "driverlicensec":
                return Job.DriverLicenseC;
            case "driverlicensed":
                return Job.DriverLicenseD;
            default:
                return null;
        }
    }

    public static Job parseAnyJob(String s){
        s=s.toLowerCase();
        s.trim();
        switch (s){
            case "usher":
                return Job.Usher;
            case "cashier":
                return Job.Cashier;
            case "storekeeper":
                return Job.StoreKeeper;
            case "guard":
                return Job.Guard;
            case "driverlicensea":
                return Job.DriverLicenseA;
            case "driverlicenseb":
                return Job.DriverLicenseB;
            case "driverlicensec":
                return Job.DriverLicenseC;
            case "driverlicensed":
                return Job.DriverLicenseD;
            default:
                return null;
        }

    }

    public static DayOfWeek dayOfWeek(int i) {
        DayOfWeek output = null;
        switch (i) {
            case 0:
                output = DayOfWeek.SUNDAY;
                break;
            case 1:
                output = DayOfWeek.MONDAY;
                break;
            case 2:
                output = DayOfWeek.TUESDAY;
                break;
            case 3:
                output = DayOfWeek.WEDNESDAY;
                break;
            case 4:
                output = DayOfWeek.THURSDAY;
                break;
            case 5:
                output = DayOfWeek.FRIDAY;
                break;
            case 6:
                output = DayOfWeek.SATURDAY;
        }
        return output;
    }

    public static DayOfWeek dateTodDayOfWeek(Date date) {
        return DayOfWeek.of(date.getDay());
    }

    public static Job parseJob(String s) {
        s = s.toLowerCase();
        switch (s) {
            case "usher":
                return Job.Usher;
            case "cashier":
                return Job.Cashier;
            case "guard":
                return Job.Guard;
            case "storekeeper":
                return Job.StoreKeeper;
        }
        return null;
    }

    public static Job parseLicense(String s) {
        s = s.toLowerCase().trim();//todo:check
        switch (s) {
            case "a":
                return Job.DriverLicenseA;
            case "b":
                return Job.DriverLicenseB;
            case "c":
                return Job.DriverLicenseC;
            case "d":
                return Job.DriverLicenseD;
        }
        return null;
    }

    public static Integer scanInt(Scanner scanner) {
        int toRet;
        toRet = scanner.nextInt();
        scanner.nextLine();
        return toRet;

    }

    /*
        public static Double scanDouble(BufferedReader scanner){
            Double toRet;
            try {
                toRet = scanner.nextDouble();
                return toRet;
            }
            catch (Exception e){
                return null;
            }
        }
     */
    public static String getLicenseFromJob(Job s) {
        switch (s) {
            case DriverLicenseA:
                return "A";
            case DriverLicenseB:
                return "B";
            case DriverLicenseC:
                return "C";
            case DriverLicenseD:
                return "D";
            default:
                return null;
        }
    }


    public static Integer scanInt(BufferedReader scanner) {
        String toProccess;
        int toRet;
        while (true) {
            try {
                toProccess = scanner.readLine();
                toRet = Integer.parseInt(toProccess);
                return toRet;
            } catch (Exception e) {
                System.out.println("invalid input, please try again.");
                continue;
            }
        }
    }


    public static Double scanDouble(BufferedReader scanner) {
        String toProccess;
        Double toRet;
        while (true) {
            try {
                toProccess = scanner.readLine();
                toRet = Double.parseDouble(toProccess);
                return toRet;
            } catch (Exception e) {
                System.out.println("invalid input, please try again.");
                continue;
            }
        }
    }

    public static Branch parseBranch(String input) {//todo: branch fix
        input = input.toLowerCase();
        switch (input) {
            case "haifa":
                return Branch.Usher;
            case "beer sheva":
                return Branch.Cashier;
            case "dimona":
                return Branch.StoreKeeper;
            case "tel aviv":
                return Branch.Guard;
            case "eilat":
                return Branch.Driver;
        }
        return null;
    }

    public static boolean isDriver(RegularWorker rw) {
        return rw.getCertifiedWorks().contains(Job.DriverLicenseA) || rw.getCertifiedWorks().contains(Job.DriverLicenseB) || rw.getCertifiedWorks().contains(Job.DriverLicenseC) || rw.getCertifiedWorks().contains(Job.DriverLicenseD);
    }

    public static List<Job> allPossibleLicense(List<Job> license) {
        List<Job> possibleLicense = new ArrayList<>();
        possibleLicense.add(Job.DriverLicenseA);
        possibleLicense.add(Job.DriverLicenseB);
        possibleLicense.add(Job.DriverLicenseC);
        possibleLicense.add(Job.DriverLicenseD);
        if (license.contains(Job.DriverLicenseA)) {
            return possibleLicense;
        } else if (license.contains(Job.DriverLicenseB)) {
            possibleLicense.remove(Job.DriverLicenseA);
        } else if (license.contains(Job.DriverLicenseC)) {
            possibleLicense.remove(Job.DriverLicenseA);
            possibleLicense.remove(Job.DriverLicenseB);
        } else if (license.contains(Job.DriverLicenseD)) {
            possibleLicense.remove(Job.DriverLicenseA);
            possibleLicense.remove(Job.DriverLicenseB);
            possibleLicense.remove(Job.DriverLicenseC);
        } else {
            System.out.println("not a license");
            return new ArrayList<>();
        }
        return possibleLicense;
    }

    public static List<Job> allPossibleLicense(Job license) {
        List<Job> possibleLicense = new ArrayList<>();
        possibleLicense.add(Job.DriverLicenseA);
        possibleLicense.add(Job.DriverLicenseB);
        possibleLicense.add(Job.DriverLicenseC);
        possibleLicense.add(Job.DriverLicenseD);
        if (license == Job.DriverLicenseA) {
            return possibleLicense;
        } else if (license == Job.DriverLicenseB) {
            possibleLicense.remove(Job.DriverLicenseA);
        } else if (license == Job.DriverLicenseC) {
            possibleLicense.remove(Job.DriverLicenseA);
            possibleLicense.remove(Job.DriverLicenseB);
        } else if (license == Job.DriverLicenseD) {
            possibleLicense.remove(Job.DriverLicenseA);
            possibleLicense.remove(Job.DriverLicenseB);
            possibleLicense.remove(Job.DriverLicenseC);
        } else {
            System.out.println("not a license");
            return new ArrayList<>();
        }
        return possibleLicense;
    }

    public static LocalDate dateToLocal(Date dateToConvert) {
        LocalDate localDate =dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return localDate;
    }

    public static LocalDate DateSQLToLocal(java.sql.Date date) {
        return date.toLocalDate();
    }

    public static Date localToDate(LocalDate dateToConvert) {
        ZoneId defaultZoneId = ZoneId.systemDefault();
        return Date.from(dateToConvert.atStartOfDay(defaultZoneId).toInstant());
    }

}