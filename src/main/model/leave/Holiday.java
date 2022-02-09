package model.leave;

import java.time.LocalDate;

public class Holiday implements Leave {

    private LocalDate dateOfLeave;
    private String comments;

    public Holiday(LocalDate dateOfLeave, String comments) {
        this.dateOfLeave = dateOfLeave;
        this.comments = comments;
    }

    public Holiday(String dateOfLeave, String comments) {
        this.dateOfLeave = LocalDate.parse(dateOfLeave);
        this.comments = comments;
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

    public void setComments(String comments) {
        this.comments = comments;
    }

    //TODO edit leave by referencing date (employee object?)
}
