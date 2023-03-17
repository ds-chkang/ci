package datamining.feature;

import datamining.utils.system.MyVars;

import java.util.Map;
import java.util.Objects;

public class MyVaraibleNameToIdMapper {

    private int variableCnt = 0;
    private String variableSign = "x";

    public MyVaraibleNameToIdMapper() {}
    public String mapVariableToID(String variableItemSet) {
        String encodedVariableItemSet = "";
        String [] variables = variableItemSet.split(",");
        for (String variable : variables) {
            if (!MyVars.variableToIdMap.containsValue(variable)) {
                MyVars.variableToIdMap.put(this.variableCnt++ + this.variableSign, variable);
            }
        }
        for (String variable : variables) {
            if (MyVars.variableToIdMap.containsValue(variable)) {
                for (Map.Entry<String, String> entry : MyVars.variableToIdMap.entrySet()) {
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