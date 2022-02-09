package model.leave;

import java.time.LocalDate;

public class Sick implements Leave {

    LocalDate dateOfLeave;
    String comments;

    public Sick(LocalDate dateOfLeave, String comments) {
        this.dateOfLeave = dateOfLeave;
        this.comments = comments;
    }

    public String displayLeave() {
        return "";
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
