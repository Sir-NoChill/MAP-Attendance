package model.leave;

import org.json.JSONObject;

import java.time.LocalDate;

// Represents sick leave
public class Sick implements Leave {

    LocalDate dateOfLeave;
    String comments;

    public Sick(LocalDate dateOfLeave, String comments) {
        this.dateOfLeave = dateOfLeave;
        this.comments = comments;
    }

    public Sick(String dateOfLeave, String comments) {
        this.dateOfLeave = LocalDate.parse(dateOfLeave);
        this.comments = comments;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("leaveDate",dateOfLeave);
        json.put("comments",comments);
        json.put("leaveType","Sick");

        return json;
    }


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
        return "Sick";
    }

    public void setDateOfLeave(LocalDate dateOfLeave) {
        this.dateOfLeave = dateOfLeave;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
