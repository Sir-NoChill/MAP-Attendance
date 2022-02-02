package model;

import model.leave.Leave;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class Employee {

    private Date anniversary;
    private Role role;
    private int yearsOfService;
    private String name;
    private WorkHours workHours;
    private List<Leave> leaveTaken;

    //TODO
    public void employee(String name, Date anniversary, Role role, WorkHours workHours) {
        this.anniversary = anniversary;
        this.name = name;
        this.role = role;
        this.workHours = workHours;
    }

    //TODO
    public void takeLeave(ArrayList<Date> dates, Leave leaveType) {
        // Stub//
    }

    //TODO
    public Date getAnniversary() {
        return anniversary; //stub
    }

    //TODO
    public Role getRole() {
        return role; //stub
    }

    //TODO
    public String getName() {
        return name; //stub
    }

    //TODO
    public int getYearsOfService() {
        return yearsOfService;
    }

    //TODO
    public WorkHours getWorkHours() {
        return workHours;
    }
}