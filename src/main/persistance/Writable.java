package persistance;

import org.json.JSONObject;

//Modeled on JsonSerializationDemo from CPSC 210
public interface Writable {
    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}
