package model.leave;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface Leave {

    String displayLeave();

    LocalDate getDateOfLeave();

    void setComments(String comments);
}
