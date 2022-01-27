package model;

import com.sun.corba.se.spi.orbutil.threadpool.Work;

import java.util.Date;
import java.util.ArrayList;

public class Employee {

    private Date anniversary;
    private Role role;
    private int yearsOfService;
    private String name;
    private WorkHours workHours;

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