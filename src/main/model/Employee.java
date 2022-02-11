package model;

import model.leave.Holiday;
import model.leave.Leave;
import model.leave.LeaveType;
import model.leave.Sick;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashSet;
import java.util.Set;

public class Employee {

    private LocalDate anniversary;
    private Role role;
    private int yearsOfService;
    private String name;
    private WorkHours workHours;    //Enum of the number of hours worked per day, translated to float in the UI
    private Set<Leave> leaveTaken;
    private String supervisor;
    private String department;
    private double holidayLeft;      //Holiday left at any temporal state
    private double holidayAccrual;   //Holiday accrued per month
    private double sickLeaveLeft;    //Sick leave left at any temporal state
    private final double sickLeaveAccrual = 6; //Sick leave accrued per month

    //TODO overload the constructor to accept more different date formats?
    public Employee(LocalDate anniversary, Role role, String name, WorkHours workHours,
                    String supervisor, String department) {
        this.anniversary = anniversary;
        this.role = role;
        this.name = name;
        this.workHours = workHours;
        this.leaveTaken = new HashSet<>();
        this.supervisor = supervisor;
        this.holidayLeft = 0;
        this.sickLeaveLeft = sickLeaveAccrual;
        autoSetYearsOfService();
        this.holidayAccrual = 0;
        this.department = department; //TODO make the department an Enum??
        autoSetHolidayAccrualRate();
    }

    public Employee(String anniversary, Role role, String name, WorkHours workHours,
                    String supervisor, String department) {
        this.anniversary = LocalDate.parse(anniversary);
        this.role = role;
        this.name = name;
        this.workHours = workHours;
        this.supervisor = supervisor; //TODO maybe make this of type employee?
        this.department = department;
        this.leaveTaken = new HashSet<>();
        this.holidayLeft = 0;
        this.sickLeaveLeft = sickLeaveAccrual;
        autoSetYearsOfService();
        autoSetHolidayAccrualRate();
    }

    //REQUIRES:
    //MODIFIES: this
    //EFFECTS: creates an instance of Leave with appropriate information
    //         appends an instance of Leave to leaveTaken
    //         reduces the appropriate type of leave left to the employee
    public void takeLeave(LocalDate date, LeaveType leaveType, String comment) {
        Leave leave;
        if (leaveType == LeaveType.SICK) {
            leave = new Sick(date, comment);
            sickLeaveLeft -= 1;
        } else {
            leave = new Holiday(date, comment); //Should probably figure out how to throw an exception here
            holidayLeft -= 1;
        }
        this.leaveTaken.add(leave);
    }

    //TODO
    //REQUIRES:
    //MODIFIES: this
    //EFFECTS:  appends an instance of Leave to leaveTaken
    public void takeLeave(String date, LeaveType leaveType, String comment) {
        Leave leave;
        LocalDate date1 = LocalDate.parse(date);
        if (leaveType == LeaveType.SICK) {
            leave = new Sick(date1, comment);
            sickLeaveLeft -= 1;
            //TODO make the method more general
        } else {
            leave = new Holiday(date1, comment); //Should probably figure out how to throw an exception here
            holidayLeft -= 1;
        }
        this.leaveTaken.add(leave);
    }

    //REQUIRES: holidayAccrued must be non-negative
    //MODIFIES: this
    //EFFECTS:  Changes the number of holiday available to this
    public void accrueHoliday() { //Don't date assholes
        this.holidayLeft += holidayAccrual;
    }

    //REQUIRES: sickDaysAccrued must be non-negative
    //MODIFIES: this
    //EFFECTS:  Changes the number of sickDays available to this
    public void accrueSickDays() {
        this.sickLeaveLeft = sickLeaveAccrual;
    }

    //REQUIRES: yearsOFService be non-negative
    //MODIFIES: this
    //EFFECTS: Sets an employee holiday accrual rate
    //note: employee accrual rate must happen after years of service is changed.
    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    public void autoSetHolidayAccrualRate() {
        switch (this.yearsOfService) {
            case 0:
                this.holidayAccrual = 0;
                break;
            case 1:
                this.holidayAccrual = 10;
                break;
            case 2:
            case 3:
            case 4:
                this.holidayAccrual = 15;
                break;
            case 5:
                this.holidayAccrual = 16;
                break;
            case 6:
            case 7:
                this.holidayAccrual = 17;
                break;
            case 8:
                this.holidayAccrual = 20;
                break;
            case 9:
                this.holidayAccrual = 21;
                break;
            case 10:
                this.holidayAccrual = 22;
                break;
            default:
                this.holidayAccrual = 23;
        }
    }

    //REQUIRES: anniversary be now or in the past
    //MODIFIES: this
    //EFFECTS: sets the years of service of an employee based on their anniversary
    //note: this is done in zero based indexing
    public void autoSetYearsOfService() {
        this.yearsOfService = Period.between(this.anniversary,LocalDate.now()).getYears();
    }

    //TODO create method display leave for employee


    // public double getSickLeaveAccrual() { //Removed,as sick leave accrual is a constant
    //     return sickLeaveAccrual;
    // }

    //TODO Tesing
    //REQUIRES: leaveTaken be non-empty
    //EFFECTS:  Displays all leave taken by employee according to displayLeave
    public String displayLeave() {
        for (Leave leave :
                this.leaveTaken) {
            leave.displayLeave();
        }
        return null;//FIXME how do I test this
    }

    //TODO testing
    //EFFECTS:  Returns an instance of leave with matching date.
    public Leave searchLeave(String date) {
        LocalDate realDate = LocalDate.parse(date);
        for (Leave leave: this.leaveTaken) {
            if (leave.getDateOfLeave().equals(realDate)) {
                return leave;
            }
        }
        return null;
    }

    public LocalDate getAnniversary() {
        return anniversary;
    }

    public Role getRole() {
        return role;
    }

    public int getYearsOfService() {
        return yearsOfService;
    }

    public String getName() {
        return name;
    }

    public WorkHours getWorkHours() {
        return workHours;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public String getDepartment() {
        return department;
    }

    public double getHolidayLeft() {
        return holidayLeft;
    }

    public double getHolidayAccrual() {
        return holidayAccrual;
    }

    public double getSickLeaveLeft() {
        return sickLeaveLeft;
    }

    public Set<Leave> getLeaveTaken() {
        return leaveTaken;
    }

    //public void setSickLeaveAccrual(double sickLeaveAccrual) {//TODO reimplement in GUI later?
    //    this.sickLeaveAccrual = sickLeaveAccrual;
    //}

    public void setAnniversary(LocalDate anniversary) {
        this.anniversary = anniversary;
    }

    public void setAnniversary(String anniversary) {
        this.anniversary = LocalDate.parse(anniversary);
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWorkHours(WorkHours workHours) {
        this.workHours = workHours;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setHolidayLeft(double holidayLeft) {
        this.holidayLeft = holidayLeft;
    }

    public void setSickLeaveLeft(double sickLeaveLeft) {
        this.sickLeaveLeft = sickLeaveLeft;
    }

    public void setHolidayAccrual(double holidayAccrual) {
        this.holidayAccrual = holidayAccrual;
    }
}