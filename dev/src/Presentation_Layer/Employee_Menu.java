package Presentation_Layer;

import Bussiness_Layer.*;

import Bussiness_Layer.EmployeePackage.BankInfo;
import Bussiness_Layer.Report.Report;
import Bussiness_Layer.ShiftPackage.ShiftType;
import DataLayer.DalObjects.DBController.DalController;
import DataLayer.DalObjects.DalLocation;
import Facade_Layer.*;
import Facade_Layer.FacadeObjects.FacadeEmployee;
import Facade_Layer.FacadeObjects.LocationFacade;

import java.io.BufferedReader;
import java.io.IOException;
import java.rmi.NoSuchObjectException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Employee_Menu {
    private ApplicationFacade facade;
    private BufferedReader scanner;
    private Scanner scannerS = new Scanner(System.in);
    boolean load = false;
    private InventoryFacade Iaf; //todo
    private SuppliersFacade Saf; //todo

    public Employee_Menu(ApplicationFacade facade, BufferedReader scanner) {
        this.facade = facade;
        this.scanner = scanner;

    }

    private void firstTime() throws Exception {
        if (!load) {
            System.out.println("Hello, in order to prepare an existing information system, you must insert 'data'");
            String str = scanner.readLine();
            if (str.equals("data")) {
                //new Temporary_data_structure();
                load = true;
            } else {
                HR_Manager_registration_protocol();
            }
        } else System.out.println("Data already loaded.");
    }

    private void HR_Manager_registration_protocol() throws IOException {
        System.out.println("no data has been loaded, at least one Hr manager is required to be registered in order to proceed." +
                "\nAn HR Manager registration protocol will take place:");
        registerWorker(false, 0);
    }

    public void deploy() throws Exception {
        boolean exit = false;
        while (!exit) {
            System.out.println("Choose what you want to do:");
            for (int i = 1; i <= Options.getFirstOts().length; i++) {
                System.out.println((i) + ") " + Options.getFirstOts()[i - 1]);
            }
            int op = UtilP.scanInt(scanner);
            switch (op) {
                case 1:
                    firstTime();
                    break;
                case 2:
                    logIn();
                    break;
                case 3:
                    exit = true;
                    break;
            }
        }
    }

    private void logIn() throws Exception {
        if (!load) {
            HR_Manager_registration_protocol();
            load = true;
        }
        boolean logged = false;
        int id = -1;
        while (!logged) {
            System.out.println("In order to Login, please insert your ID:");
            id = UtilP.scanInt(scanner);
            if (id == -1)
                break;
            Response response = Login(id);
            if (!response.isErrorOccurred())
                logged = true;
            else System.out.println(response.errorMessage);
        }
        if (id != -1) {
            boolean done = false;
            System.out.println("Hello " + facade.getFName());
            if (facade.isHRManager()) {
                while (!done)
                    done = deployHRMenu();
            } else {
                while (!done)
                    done = deployRegWorkMenu();
            }
        }
    }


    Response Login(int id) {
        return facade.Login(id);
    }

    ////////////////// HR Manager Menus and functions
    public boolean deployHRMenu() throws Exception {
        boolean done = false;
        int option = 0;
        while (!done) {
            System.out.println("Choose an option from the listed below:");
            for (int i = 1; i <= Options.getHROpts().length; i++) {
                System.out.println((i) + ") " + Options.getHROpts()[i - 1]);
            }
            try {
                option = UtilP.scanInt(scanner);
            } catch (Exception e) {
                System.out.println("Invalid option, please try again.");
                deployHRMenu();
            }
            if (option > Options.getHROpts().length || option < 1) {
                System.out.println("Invalid option, please try again.");
                continue;
            }
            switch (option) {
                case 1:
                    upDateShift();
                    break;
                case 2:
                    System.out.println("Please enter the ID of the worker you would like to update:");
                    int id = UtilP.scanInt(scanner);
                    if (checkIfExists(id)) {
                        deployChangeInfoMenu(id);
                    } else {
                        System.out.println("invalid ID, please try again with a valid ID\n\n");
                        break;
                    }
                    break;
                case 3:
                    System.out.println(facade.thisWeekShifts());
                    break;
                case 4:
                    System.out.println(facade.nextWeekShifts());
                    break;
                case 5:
                    getASpecificShift();
                    break;
                case 6:
                    registerWorkerMenu();
                    break;
                case 7:
                    get_number_of_employees_on_shift();
                    break;
                case 8:
                    change_in_the_number_of_employees_on_shift();
                    break;
                case 9:
                    get_number_of_employee_for_job_on_shift();
                    break;
                case 10:
                    createShifts();
                    break;
                case 11:
                    System.out.println(facade.getEmployeelist());
                    break;
                case 12:
                    System.out.println(facade.getDriverList());
                    break;
                case 13:
                    FireEmployee();
                    break;
                case 14:
                    System.out.println("Please enter delivery id to cancel: ");
                    int DelID = UtilP.scanInt(scanner);
                    facade.cancelRequest(DelID);
                    break;
                case 15:
                    facade.logout();
                    done = true;
                    break;
            }
        }
        return true;
    }

    private boolean deployChangeInfoMenu(int id) throws IOException {
        while (true) {
            System.out.println("Choose the personal Information from the listed below:");
            for (int i = 1; i <= Options.getChangeInfoOpts().length; i++) {
                System.out.println((i) + ") " + Options.getChangeInfoOpts()[i - 1]);
            }
            int option = UtilP.scanInt(scanner);
            if (option > Options.getChangeInfoOpts().length || option < 1) {
                System.out.println("Invalid option, please try again.\n\n");
                continue;
            }
            boolean done = false;
            while (!done) {
                try {
                    done = changeWorkersInfo(id, option);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return done;
        }
    }


    private boolean changeWorkersInfo(int id, int option) throws IOException {
        if (facade.isActive().isErrorOccurred()) {
            System.out.println("It is mandatory to connect to the system before changing constraints!");
            return false;
        }
        /*if (facade.isHRManager()) {
            System.out.println("Human Resource manager has no constraints");
            return false;
        }
         */
        FacadeEmployee subject;
        try {
            subject = facade.getEmployeeById(id);
        } catch (Exception e) {
            System.out.println("No such employee, please retry");
            return false;
        }

        switch (option) {
            case 1:
                System.out.println("Enter the updated first name for the Employee");
                String name = scanner.readLine();
                facade.changeEmployeeF_Name(singleInputDecipher(name), subject);
                break;
            case 2:
                System.out.println("Enter the updated last name for the Employee");
                name = scanner.readLine();
                facade.changeEmployeeL_Name(singleInputDecipher(name), subject);
                break;
            case 3:
                System.out.println("Enter the updated wage for the Employee");
                int wage = UtilP.scanInt(scanner);
                facade.changeEmployeeWage(wage, subject);
                break;
            case 4:
                System.out.println("Enter the updated amount of monthly days off for the Employee");
                Double monthlyDaysOff = UtilP.scanDouble(scanner);
                facade.changeMonthlyDayOffs(monthlyDaysOff, subject);
                break;
            case 5:
                System.out.println("Enter the updated amount of monthly sick days for the Employee");
                Double monthlySickDays = UtilP.scanDouble(scanner);
                facade.changeMonthlySickDays(monthlySickDays, subject);
                break;
            case 6:
                System.out.println("Enter the updated bank information of the Employee");
                System.out.println("Please follow the following format:\n{Bank ID} {Branch} {Account number}");
                String bankInfoStr = scanner.readLine();
                String[] tokens = bankInfoStr.split(" ");
                if (tokens.length < 3) {
                    throw new IllegalArgumentException("Invalid Arguments provided, please follow the given format");//todo:
                }
                BankInfo bankInfo = new BankInfo(Integer.parseInt(tokens[0]), tokens[1], Integer.parseInt(tokens[2]));
                facade.changeBankInfo(bankInfo, subject);
                break;
            case 7:
                System.out.println("Enter the updated amount of advanced study funds for the Employee");
                int newFundsAmount = UtilP.scanInt(scanner);
                facade.changeAdvancedStudyFunds(newFundsAmount, subject);
                break;
            case 8:
                break;
            default:
                return false;
        }
        return true;
    }

    private String FireEmployee() throws Exception {
        int id;
        System.out.println("Enter the ID of the employee you want to Fire");
        id = UtilP.scanInt(scanner);
        if (checkIfExists(id)) {
            //if(facade.checkthisweekShift(id)&facade.checknextweekShift(id))
            facade.FireEmployee(id);
            return "Employee register to up coming shift";
        } else return "Employee don't exists";
    }

    private String singleInputDecipher(String toProc) {
        String[] tokens = toProc.split(" ");
        return tokens[0];
    }

    public boolean deployRegWorkMenu() throws Exception {
        boolean done = false;
        int option = 0;
        while (!done) {
            System.out.println("Choose an option from the listed below:");
            for (int i = 1; i <= Options.getRWOpts().length; i++) {
                System.out.println((i) + ") " + Options.getRWOpts()[i - 1]);
            }
            try {
                option = UtilP.scanInt(scanner);
            } catch (Exception e) {
                System.out.println("Invalid option, please try again.");
                deployRegWorkMenu();
            }
            if (option > Options.getRWOpts().length || option < 1) {
                System.out.println("Invalid option, please try again.");
                return false;
            }
            switch (option) {
                case 2:
                    boolean temp = false;
                    while (!temp) {
                        temp = deployChangeConstraintsMenu();
                    }
                    break;
                case 3:
                    System.out.println(facade.thisWeekShifts());
                    break;
                case 4:
                    System.out.println(facade.nextWeekShifts());
                    break;
                case 5:
                    getASpecificShift();
                    break;
                case 6:
                    facade.logout();
                    done = true;
                    //deploy();
                    break;
                case 1:
                    System.out.println(facade.showConstraints());
                    break;
            }
        }
        return true;
    }

    private void getASpecificShift() throws Exception {
        Date date = shiftDate();
        ShiftType shiftType = shiftType();
        Response response = facade.getSpecificShift(date, shiftType);
        if (response instanceof ResponseT) {
            System.out.println(((ResponseT) response).getValue().toString());
            return;
        }
        System.out.println("Shift not exist. please try again.\n");
    }

    private boolean deployChangeConstraintsMenu() throws Exception {
        while (true) {
            Response isActive = facade.isActive();
            if (isActive.isErrorOccurred()) {
                System.out.println("It is mandatory to connect to the system before changing constraints!");
                return false;
            }
            if (facade.isHRManager()) {
                System.out.println("Human Resource manager has no constraints");
            }
            System.out.println("Choose an option from the listed below:");
            for (int i = 1; i <= Options.getChangeConstraintsOpts().length; i++) {
                System.out.println((i) + ") " + Options.getChangeConstraintsOpts()[i - 1]);
            }
            int option = UtilP.scanInt(scanner);
            if (option == Options.getChangeConstraintsOpts().length)
                if (option > Options.getChangeConstraintsOpts().length || option < 1) {
                    System.out.println("Invalid option, please try again.\n\n");
                    continue;
                }
            if (option == 1)
                updateAllShiftConstrtion();
            else if (option == 2)
                updateSpecificShiftConstraints();
            return true;
        }
    }

    private void updateSpecificShiftConstraints() throws Exception {
        while (true) {
            System.out.println("the format should be as followed:\n" + "(Day of the week), (Day or Night shift), (Can or Cannot work)");
            System.out.println("An example for a valid input: Sunday Night Cannot");
            //scanner.readLine();
            String formatted = scanner.readLine();
            String processed[] = formatted.split(" ");
            boolean constraint;
            int dayOfWeek, shiftType;
            try {
                dayOfWeek = Utility.checkDayStr(processed[0]);
                shiftType = Utility.checkShiftStr(processed[1]);
                constraint = Utility.decipherConstraint(processed[2]);
                Response re = facade.changeConstraints(dayOfWeek, shiftType, constraint);
                if (re.isErrorOccurred())
                    System.out.println(re.errorMessage);
                else System.out.println("Action completed");
                return;
            } catch (Exception e) {
                System.out.println("Invalid ID, please retry");
            }
        }
    }

    private void updateAllShiftConstrtion() throws IOException {
        final int numberOfShiftsInWeek = 14;
        System.out.println(facade.showConstraints());
        System.out.println("Dear Employee, please Note that the shifts are decided and made at Thursday 00:01\b" +
                "please make sure you have all your constraints set correctly before the specified deadline");
        System.out.println("the format should be as followed:\n" +
                "{(Sunday morning:) t or f, (Sunday night:) t or f, (Monday morning:) t or f, (Monday night:) t or f" +
                " (Tuesday morning:) t or f, (Tuesday night:) t or f, (Wednesday morning:) t or f, (Wednesday night:) t or f," +
                " (Thursday morning:) t or f, (Thursday night:) t or f, (Friday morning:) t or f, (Friday night:) t or f" +
                " (Saturday morning:) t or f, (Saturday night:) t or f}");
        System.out.println("For example for a valid input: { t, t, f, t, f, t, t, t, f, f, f, f, t, f}");
        //scanner.readLine();
        String buffer = scanner.readLine();
        buffer = buffer.replace("{", "");
        buffer = buffer.replace("}", "");
        String stringConsntraints[] = buffer.split(",");
        boolean[][] processedConstraints = new boolean[7][2];

        if (stringConsntraints.length != numberOfShiftsInWeek) {
            System.out.println("Invalid input, please follow the format above! please retry entering a valid input");
            return;
        }

        for (int i = 0; i < numberOfShiftsInWeek; i++) {
            processedConstraints[i][0] = Boolean.valueOf(stringConsntraints[i].trim());
            processedConstraints[i][1] = Boolean.valueOf(stringConsntraints[i + 1].trim());
        }
        Response re = facade.changeConstraints(processedConstraints);
        if (re.isErrorOccurred())
            System.out.println(re.errorMessage);
        else System.out.println("Action completed");
    }

    private boolean checkIfExists(int id) {
        if (facade.empExists(id).equals("Exists"))
            return true;
        else return false;
    }

    private void change_in_the_number_of_employees_on_shift() throws Exception {
        int dayNumber = -1;
        System.out.println("Enter a selected day");
        String day = scanner.readLine();
        try {
            dayNumber = Utility.checkDayStr(day);
        } catch (Exception e) {
            System.out.println(e.toString().substring(21));
            change_in_the_number_of_employees_on_shift();
        }
        ShiftType shiftType = shiftType();
        Job job = getJob();
        while (job == null) {
            System.out.println("Invalid input of job! please retry entering a valid input");
            job = getJob();
        }
        System.out.println("Enter the new sum of employee of " + job + " in this shift");
        int input;
        try {
            input = UtilP.scanInt(scanner);
        } catch (Exception e) {
            System.out.println("Invalid input");
            return;
        }
        System.out.println("input Enter: " + input);
        if (dayNumber != -1) {
            facade.change_in_the_number_of_employees_on_shift(dayNumber, shiftType, job, input);
        }
    }

    private void get_number_of_employee_for_job_on_shift() throws Exception {
        System.out.println("Enter a selected day");
        String day = scanner.readLine();
        int dayNumber = Utility.checkDayStr(day);
        ShiftType shiftType = shiftType();
        Job job = getJob();
        System.out.println("Amount of " + job + " is " + facade.get_number_of_employee_for_job_on_shift(dayNumber, shiftType, job));
    }

    private void get_number_of_employees_on_shift() throws IOException {
        System.out.println("Enter a selected day");
        String day = scanner.readLine();
        int dayNumber = 0;
        try {
            dayNumber = Utility.checkDayStr(day);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ShiftType shiftType = shiftType();
        System.out.println(facade.get_number_of_employees_on_shift(dayNumber, shiftType));
    }

    private void createShifts() throws Exception {
        if (!facade.createShift()) {
            System.out.println("Note that shifts have been created but some are incomplete");
            deployHRMenu();
        }
    }

    private Job getJob() throws IOException {
        System.out.println("Select the type of work you want (Cashier, Guard, StoreKeeper or Usher)");
        String input = scanner.readLine();
        return Utility.parseJob(input);
    }

    private LocationFacade getLocation() throws IOException {//todo: get location
        return new LocationFacade(UtilP.scanInt(scanner), "", "", "");
    }

    private Date shiftDate() {
        System.out.println("Enter Date with \"dd/MM/yyyy\" format");
        try {
            String strinput = scanner.readLine();
            return new SimpleDateFormat("dd/MM/yyyy").parse(strinput);
        } catch (Exception e) {
            System.out.println("Not right \"dd/MM/yyyy\" format.");
            return shiftDate();
        }
    }

    private ShiftType shiftType() throws IOException {
        System.out.println("Enter shift type, 'Day' or 'Night'");
        String type = scanner.readLine().toLowerCase();
        while (!(type.equals("day") | type.equals("night"))) {
            System.out.println("Enter shift type, 'Day' or 'Night'");
            type = scanner.readLine().toLowerCase();
        }
        ShiftType shiftType;
        if (type.equals("day"))
            shiftType = ShiftType.Day;
        else shiftType = ShiftType.Night;
        return shiftType;
    }

    private void upDateShift() throws Exception {
        int upDateShiftOp = -1;
        int id = -1;
        Job job;
        Date date = shiftDate();
        ShiftType shiftType = shiftType();
        Response res = facade.getShift(date, shiftType);
        if (!res.isErrorOccurred()) {
            upDateShiftOp = upDateShiftOp();
            switch (upDateShiftOp) {
                case 1:
                    System.out.println(facade.upDateShift(date, shiftType, upDateShiftOp).errorMessage);
                    System.out.println("Enter the ID of the employee you want to remove from the shift");
                    id = UtilP.scanInt(scanner);
                    Response response = facade.remove(date, shiftType, id);
                    if (response.isErrorOccurred())
                        System.out.println(response.errorMessage);
                    break;
                case 2:
                    //System.out.println("Select the type of work you want to add to the shift (Cashier, Guard, StoreKeeper, Usher)");
                    job = getJob();
                    System.out.println(facade.getEmployeesForJob(job, date, shiftType).errorMessage);
                    System.out.println("Enter the ID of the employee you want add to the shift");
                    id = UtilP.scanInt(scanner);
                    Response responses = facade.add(date, shiftType, id, job);
                    if (responses.isErrorOccurred())
                        System.out.println(responses.errorMessage);
                    break;
                case 3:
                    System.out.println(facade.getShiftManager(date, shiftType).errorMessage);
                    System.out.println("Select the employee ID you want to place in place of the current shift supervisor");
                    id = UtilP.scanInt(scanner);
                    Response responsess = facade.replce(date, shiftType, id);
                    if (responsess.isErrorOccurred())
                        System.out.println(responsess.errorMessage);
                    break;

            }
        }
    }

    private int upDateShiftOp() {
        System.out.println("What Do you want to change? \n 1) remove worker \n 2) add worker\n 3) Replace shiftManager");
        try {
            int input = UtilP.scanInt(scanner);
            while (input < 1 | input > 3) {
                System.out.println("out of bounds.\nWhat Do you want to change? \n 1) remove worker \n 2) add worker\n 3) Replace shiftManager");
                input = UtilP.scanInt(scanner);
            }
            return input;
        } catch (Exception e) {
            System.out.println("Invalid input, please try again");
            return upDateShiftOp();
        }
    }

    private boolean registerWorkerMenu() throws IOException {
        System.out.println("Choose an option from the listed below:");
        while (true) {
            for (int i = 1; i <= Options.getRegisterWorkerOpts().length; i++) {
                System.out.println((i) + ") " + Options.getRegisterWorkerOpts()[i - 1]);
            }
            int option = UtilP.scanInt(scanner);
            if (option > Options.getRegisterWorkerOpts().length || option < 1) {
                System.out.println("Invalid option, please try again.");
                return false;
            }
            switch (option) {
                case 1:
                    registerWorker(true, 0); //regularWorker
                    break;
                case 2:
                    registerWorker(false, 0); //HRM
                    break;
                case 3:
                    registerWorker(true, 1); //Driver
                    break;
                case 4:
                    registerWorker(false, 1); //TPM
                    break;
                case 5:
                    registerWorker(true, 2); //Storagekeeper
                    break;
                case 6:
                    registerStoreManager();
                    break;
                default: return true;
            }
        }
    }

    private void registerStoreManager() throws IOException {
        boolean succeeded = false;
        System.out.println("please enter the first name of the employee");
        String fName = scanner.readLine();
        System.out.println("please enter the last name of the employee");
        String lName = scanner.readLine();
        Integer id = -1;
        Integer wage = -1;
        Double monthlyDaysOff = -1.0;
        Double monthlySickDays = -1.0;
        Integer bankId = -1;
        String branch;
        Integer accNumb = -1;
        Integer advStudyFunds = -1;
        System.out.println("please enter the ID of the employee");
        while (!succeeded) {
            id = UtilP.scanInt(scanner);
            if (id != null) {
                succeeded = true;
            } else {
                System.out.println("Invalid ID, please retry");
            }
        }
        System.out.println("please enter the wage of the employee");
        succeeded = false;
        while (!succeeded) {
            wage = UtilP.scanInt(scanner);
            if (wage != null) {
                succeeded = true;
            } else {
                System.out.println("Invalid wage, please retry");
            }
        }
        System.out.println("please enter the amount of monthly days off of the employee");
        succeeded = false;
        while (!succeeded) {
            monthlyDaysOff = UtilP.scanDouble(scanner);
            if (monthlyDaysOff != null) {
                succeeded = true;
            } else {
                System.out.println("Invalid days off, please retry");
            }
        }

        System.out.println("please enter monthly sick days of the Employee");
        succeeded = false;
        while (!succeeded) {
            monthlySickDays = UtilP.scanDouble(scanner);
            if (monthlySickDays != null) {
                succeeded = true;
            } else {
                System.out.println("Invalid sick days, please retry");
            }
        }

        System.out.println("please enter bank ID of the Employee");
        succeeded = false;
        while (!succeeded) {
            bankId = UtilP.scanInt(scanner);
            if (bankId != null) {
                succeeded = true;
            } else {
                System.out.println("Invalid bank ID, please retry");
            }
        }
        System.out.println("please enter branch of the bank of the Employee");
        branch = scanner.readLine();
        System.out.println("please enter account number of the Employee");
        succeeded = false;
        while (!succeeded) {
            accNumb = UtilP.scanInt(scanner);
            if (accNumb != null) {
                succeeded = true;
            } else {
                System.out.println("Invalid account number, please retry");
            }
        }

        System.out.println("please enter advanced study funds of the Employee");
        succeeded = false;
        while (!succeeded) {
            advStudyFunds = UtilP.scanInt(scanner);
            if (advStudyFunds != null) {
                succeeded = true;
            } else {
                System.out.println("Invalid advanced study funds, please retry");
            }
        }
        try {
            facade.registerSManager(fName, lName, id, wage, monthlyDaysOff, monthlySickDays, bankId,
                    branch, accNumb, advStudyFunds);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void registerWorker(boolean isRegularWorker, int employeOpcode) throws IOException {//todo: complete Driver \ TPM register
        ////opcode 0 - Regular worker, 1 - delivery worker, 2 - Storage keeper
        boolean succeeded = false;
        System.out.println("please enter the first name of the employee");
        String fName = scanner.readLine();
        System.out.println("please enter the last name of the employee");
        String lName = scanner.readLine();
        Integer id = -1;
        Integer wage = -1;
        Double monthlyDaysOff = -1.0;
        Double monthlySickDays = -1.0;
        Integer bankId = -1;
        String branch;
        Integer accNumb = -1;
        Integer advStudyFunds = -1;
        System.out.println("please enter the ID of the employee");
        while (!succeeded) {
            id = UtilP.scanInt(scanner);
            if (id != null) {
                succeeded = true;
            } else {
                System.out.println("Invalid ID, please retry");
            }
        }
        System.out.println("please enter the wage of the employee");
        succeeded = false;
        while (!succeeded) {
            wage = UtilP.scanInt(scanner);
            if (wage != null) {
                succeeded = true;
            } else {
                System.out.println("Invalid wage, please retry");
            }
        }
        System.out.println("please enter the amount of monthly days off of the employee");
        succeeded = false;
        while (!succeeded) {
            monthlyDaysOff = UtilP.scanDouble(scanner);
            if (monthlyDaysOff != null) {
                succeeded = true;
            } else {
                System.out.println("Invalid days off, please retry");
            }
        }

        System.out.println("please enter monthly sick days of the Employee");
        succeeded = false;
        while (!succeeded) {
            monthlySickDays = UtilP.scanDouble(scanner);
            if (monthlySickDays != null) {
                succeeded = true;
            } else {
                System.out.println("Invalid sick days, please retry");
            }
        }

        System.out.println("please enter bank ID of the Employee");
        succeeded = false;
        while (!succeeded) {
            bankId = UtilP.scanInt(scanner);
            if (bankId != null) {
                succeeded = true;
            } else {
                System.out.println("Invalid bank ID, please retry");
            }
        }
        System.out.println("please enter branch of the bank of the Employee");
        branch = scanner.readLine();
        System.out.println("please enter account number of the Employee");
        succeeded = false;
        while (!succeeded) {
            accNumb = UtilP.scanInt(scanner);
            if (accNumb != null) {
                succeeded = true;
            } else {
                System.out.println("Invalid account number, please retry");
            }
        }

        System.out.println("please enter advanced study funds of the Employee");
        succeeded = false;
        while (!succeeded) {
            advStudyFunds = UtilP.scanInt(scanner);
            if (advStudyFunds != null) {
                succeeded = true;
            } else {
                System.out.println("Invalid advanced study funds, please retry");
            }
        }
        try {
            if (!(isRegularWorker) &&  employeOpcode == 0) {
                int Location = -1;
                System.out.println("please enter the store branch of HRM");
                succeeded = false;
                while (!succeeded) {
                    Location = UtilP.scanInt(scanner);
                    if (Location == 111 || Location == 222 || Location == 333 || Location == 444 || Location == 555 || Location == 666 || Location == 777 || Location == 888 || Location == 999)
                        succeeded = true;
                    else
                        System.out.println("Invalid bank ID, please retry");
                }
                facade.registerHRManager(fName, lName, id, wage, monthlyDaysOff, monthlySickDays, bankId,
                        branch, accNumb, advStudyFunds, Location);
            } else if (isRegularWorker &  employeOpcode == 1) {
                List<Job> jobs = getLicense();
                facade.registerDriver(fName, lName, id, wage, monthlyDaysOff, monthlySickDays, bankId,
                        branch, accNumb, advStudyFunds, false, jobs);
            } else if(!(isRegularWorker) &&  employeOpcode == 1){
                facade.registerTPManager(fName, lName, id, wage, monthlyDaysOff, monthlySickDays, bankId,
                        branch, accNumb, advStudyFunds);
            }
            else if((isRegularWorker) &&  employeOpcode == 2){
                List<Job> jobs = new ArrayList<>();
                jobs.add(Job.StoreKeeper);
                facade.registerStorageKeeper(fName, lName, id, wage, monthlyDaysOff, monthlySickDays, bankId,
                        branch, accNumb, advStudyFunds, false, jobs);
            } else{
                System.out.println("please type 'yes' if the is a qualified shift manager.");
                String strIsMNG = scanner.readLine();
                boolean isMNG = (strIsMNG.toLowerCase().equals("yes")) ? true : false;
                List<Job> jobs = getJobs();
                facade.registerRegWorker(fName, lName, id, wage, monthlyDaysOff, monthlySickDays, bankId,
                        branch, accNumb, advStudyFunds, isMNG, jobs);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private List<Job> getJobs() throws IOException {
        System.out.println("please insert the jobs of the employee: (Usher, Cashier, Guard)");
        List<Job> jobList = new LinkedList<>();
        boolean done = false;
        while (true) {
            Job checkJob = Utility.parseJob(scanner.readLine().trim());
            if (checkJob != null) {
                jobList.add(checkJob);
                break;
            } else {
                System.out.println("Invalid Job, please try again");
            }
        }
        while (!done) {
            System.out.println("please insert the jobs of the employee: (Usher, Cashier, Guard, Storekeeper, Driver)");
            System.out.println("if all the jobs have been input, type 'done' to complete");
            String check = scanner.readLine().trim().toLowerCase();
            if (check.equals("done")) {
                done = true;
                break;
            }
            Job checkJob = Utility.parseJob(check);
            if (checkJob != null) {
                jobList.add(checkJob);
            } else {
                System.out.println("Invalid Job, please try again");
            }
        }
        return jobList;
    }


    private List<Job> getLicense() throws IOException {
        System.out.println("please insert the type of the Driver License of the Driver: (A, B, C, D)");
        List<Job> jobList = new LinkedList<>();
        while (true) {
            Job checkJob = Utility.parseLicense(scanner.readLine());
            if (checkJob != null) {
                jobList.add(checkJob);
                break;
            } else {
                System.out.println("Invalid License, please try again");
            }
        }
        return jobList;
    }

    public String getName() {
        return facade.getFName();
    }

    public boolean isHRManager() {
        return facade.isHRManager();
    }

    public boolean isTPManager() {
        return facade.isTPManager();
    }

    public boolean isStorageKeeper(){ return facade.isStorageKeeper();}

    public boolean deployStorageKeeperMenu() throws Exception {
        int branchID = facade.getLocation();
        Iaf = new InventoryFacade(branchID);
        Saf = new SuppliersFacade(Iaf);
        DalController.getDalController().MakeBetterModuleQuerry();
        boolean done=false;
        while(!done) {
                System.out.println("Choose an option from the listed below:");
                for (int i = 1; i <= Options.getSKOpts().length; i++) {
                    System.out.println((i) + ") " + Options.getSKOpts()[i - 1]);
                }

                //System.out.println("AAAAAAAAAAAAAAAAAAAAAA");
            switch (scannerS.nextLine()) {
                case "1":
                    MainSupplierMenu mainSupplierMenu = new MainSupplierMenu(scannerS, Iaf, Saf);
                    mainSupplierMenu.printMenu();
                    break;
                case "2":
                    MainInventoryMenu mu = new MainInventoryMenu(Iaf, Saf);
                    try {
                        mu.Start();
                    } catch (NoSuchObjectException e) {
                        e.printStackTrace();
                    }
                    break;
                case "4":
                    boolean temp = false;
                    while (!temp) {
                        temp = deployChangeConstraintsMenu();
                    }
                    break;
                case "5":
                    System.out.println(facade.thisWeekShifts());
                    break;
                case "6":
                    System.out.println(facade.nextWeekShifts());
                    break;
                case "7":
                    getASpecificShift();
                    break;
                case "8":
                    System.out.println("Please enter delivery id to cancel: ");
                    String id = scannerS.nextLine();
                    Iaf.cancelRequest(id);
                    break;
                case "9":
                    facade.logout();
                    done = true;
                    //deploy();
                    break;
                case "3":
                    System.out.println(facade.showConstraints());
                    break;
                default:
                    return false;
            }
        }
        return true;
    }


    public boolean isSManger() { return facade.isSManager();}

    public boolean deploySMenu() throws Exception {
        boolean done = false;
        int option = 0;
        while (!done) {
            System.out.println("Choose an option from the listed below:");
            for (int i = 1; i <= Options.getSMOpts().length; i++) {
                System.out.println((i) + ") " + Options.getSMOpts()[i - 1]);
            }
            try {
                option = UtilP.scanInt(scanner);
            } catch (Exception e) {
                System.out.println("Invalid option, please try again.");
                deployHRMenu();
            }
            if (option > Options.getSMOpts().length || option < 1) {
                System.out.println("Invalid option, please try again.");
                continue;
            }
            switch (option) {
                case 1:
                    System.out.println(facade.thisWeekShifts());
                    break;
                case 2:
                    System.out.println(facade.nextWeekShifts());
                    break;
                case 3:
                    getASpecificShift();
                    break;
                case 4:
                    get_number_of_employees_on_shift();
                    break;
                case 5:
                    get_number_of_employee_for_job_on_shift();
                    break;
                case 6:
                    System.out.println(facade.getEmployeelist());
                    break;
                case 7:
                    MainSupplierMenu mainSupplierMenu = new MainSupplierMenu(scannerS, Iaf, Saf);
                    mainSupplierMenu.printMenu();
                    break;
                case 8: //todo
                    Scanner sc = new Scanner(System.in);
                    String num;
                    String str = null;
                    String reportnum;
                    Boolean flag;
                    System.out.println("please choose the type of report you would like to create:\n" +
                            "1) Damage Report\n" +
                            "2) Info Report\n" +
                            "3) Shortage Report");
                    reportnum = sc.nextLine();
                    Report r = null;
                    Iaf.refresh();
                    switch (reportnum){
                        case "1":
                            r = Iaf.CreateDamageReport(str);//empty str
                            break;
                        case "2":
                            System.out.println("to create Info Report you need to specify which category you want to be in the report" +
                                    "<category> or click enter to get all items info");
                            str += sc.nextLine();
                            r = Iaf.CreateInfoReport(str);
                            break;
                        case "3":
                            r = Iaf.CreateShortageReport(str,Saf);//empty str
                            break;

                    }
                    System.out.println(r.toString());
                    break;
                case 9:
                    System.out.println(facade.getDriverList());
                    break;
                case 10:
                    facade.logout();
                    done = true;
                    break;
            }
        }
        return true;
    }
}
