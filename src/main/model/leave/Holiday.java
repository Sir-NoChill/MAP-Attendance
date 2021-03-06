package model.leave;

import org.json.JSONObject;
import persistance.Writable;

import java.time.LocalDate;

// Represents holiday leave
public class Holiday implements Leave, Writable {

    private LocalDate dateOfLeave;
    private String comments;
    private double timeSegments;

    public Holiday(LocalDate dateOfLeave, String comments,double timeSegments) {
        this.dateOfLeave = dateOfLeave;
        this.comments = comments;
        this.timeSegments = timeSegments;
    }

    //Overload of method to accept strings of dates
    public Holiday(String dateOfLeave, String comments,double timeSegments) {
        this.dateOfLeave = LocalDate.parse(dateOfLeave);
        this.comments = comments;
        this.timeSegments = timeSegments;
    }

    //EFFECTS: converts leave to Json format
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("leaveDate",dateOfLeave);
        json.put("comments",comments);
        json.put("leaveType","Holiday");
        json.put("timeSegments", timeSegments);

        return json;
    }

    //EFFECTS: outputs the date and comments related to this instance of leave
    public String displayLeave() {
        return this.dateOfLeave.toString() + " " + this.comments;
    }

    public LocalDate getDateOfLeave() {
        return dateOfLeave;
    }

    public void setDateOfLeave(LocalDate dateOfLeave) {
        this.dateOfLeave = dateOfLeave;
    }

    public String getComments() {
        return comments;
    }

    public String getLeaveType() {
        return LeaveType.HOLIDAY.toString();
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
