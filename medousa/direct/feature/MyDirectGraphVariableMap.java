package medousa.direct.feature;

import java.util.HashMap;
import java.util.Map;

public class MyDirectGraphVariableMap
extends HashMap<String, String> {

    public Map<String, String> reverseMap = new HashMap<>();

    @Override
    public String put(String key, String variable) {
        reverseMap.put(variable, key);
        return super.put(key, variable);
    }

    public String getID(String variable) {
        return reverseMap.get(variable);
    }
}
