package medousa.direct.feature;

import medousa.direct.utils.MyDirectGraphVars;

import java.util.Map;
import java.util.Objects;

public class MyDirectGraphVaraibleNameToIdMapper {

    private int variableCnt = 0;
    private String variableSign = "x";

    public MyDirectGraphVaraibleNameToIdMapper() {}
    public String mapVariableToID(String variableItemSet) {
        String encodedVariableItemSet = "";
        String [] variables = variableItemSet.split(",");
        for (String variable : variables) {
            if (!MyDirectGraphVars.variableToIdMap.containsValue(variable)) {
                MyDirectGraphVars.variableToIdMap.put(this.variableCnt++ + this.variableSign, variable);
            }
        }
        for (String variable : variables) {
            if (MyDirectGraphVars.variableToIdMap.containsValue(variable)) {
                for (Map.Entry<String, String> entry : MyDirectGraphVars.variableToIdMap.entrySet()) {
                    if (Objects.equals(entry.getValue(), variable)) {
                        if (encodedVariableItemSet.length() == 0) {
                            encodedVariableItemSet = entry.getKey();
                        } else {
                            encodedVariableItemSet += "," + entry.getKey();
                        }
                        break;
                    }
                }
            }
        }
        return encodedVariableItemSet;
    }
}