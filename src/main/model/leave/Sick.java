package model.leave;

import java.time.LocalDate;

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


    public String displayLeave() {
        return this.dateOfLeave.toString() + " " + this.comments;
    }

    public LocalDate getDateOfLeave() {
        return dateOfLeave;
    }

    public String getComments() {
        return comments;
    }

    public void setDateOfLeave(LocalDate dateOfLeave) {
        this.dateOfLeave = dateOfLeave;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
