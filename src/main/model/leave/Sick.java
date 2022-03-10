package model.leave;

import org.json.JSONObject;

import java.time.LocalDate;

// Represents sick leave
public class Sick implements Leave {

    LocalDate dateOfLeave;
    String comments;
    double timeSegments;

    public Sick(LocalDate dateOfLeave, String comments,double timeSegments) {
        this.dateOfLeave = dateOfLeave;
        this.comments = comments;
        this.timeSegments = timeSegments;
    }

    //Overload of the method to accept date strings
    public Sick(String dateOfLeave, String comments, double timeSegments) {
        this.dateOfLeave = LocalDate.parse(dateOfLeave);
        this.comments = comments;
        this.timeSegments = timeSegments;
    }

    //EFFECTS: converts leave to Json format
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("leaveDate",dateOfLeave);
        json.put("comments",comments);
        json.put("leaveType","Sick");
        json.put("timeSegments",timeSegments);

        return json;
    }

    // Method to display leave for ui
    public String displayLeave() {
        return this.dateOfLeave.toString() + " " + this.comments;
    }

    public LocalDate getDateOfLeave() {
        return dateOfLeave;
    }

    public String getComments() {
        return comments;
    }

    public String getLeaveType() {
        return LeaveType.SICK.toString();
    }

    public void setDateOfLeave(LocalDate dateOfLeave) {
        this.dateOfLeave = dateOfLeave;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
