package model.leave;

import org.json.JSONObject;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

// Interface for all representations of leave
public interface Leave {

    String displayLeave();

    LocalDate getDateOfLeave();

    void setComments(String comments);

    Object toJson();
}
