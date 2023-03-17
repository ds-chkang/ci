package datamining.feature;

import datamining.category.MyCategory;
import datamining.utils.system.MyVars;

import java.util.ArrayList;
import java.util.HashMap;

public class MyFeatureGenerator {

    MyFeatureWriter fw = new MyFeatureWriter("sequencesWithObjectIDs.txt", "sequences.txt");
    private MyVaraibleNameToIdMapper variableMapper = new MyVaraibleNameToIdMapper();
    private int itemIDIdx;
    private int itemNmIdx;
    private int objIDIdx;
    private int trIDIdx;
    private String itemIDColNm = "ITEM ID";
    private String objIDColNm = "OBJECT ID";
    private String itemNmColNm = "ITEM NAME";
    private String trIDColNm = "TRANSACTION ID";

    public MyFeatureGenerator() {
        MyVars.sequeceFeatureCount = 0;
    }

    public void generateInputFeatures(ArrayList<ArrayList<String>> dataIn) {
        setObjectIDIndex();
        setTransactionIDIndex();
        setItemIDIndex();
        setItemNameIndex();
        MyVars.totalRecords = dataIn.size();
        if (MyVars.isSupplementaryOn) {
            runWithCategorization(dataIn);
        } else {
            runWithoutCategorization(dataIn);
        }
        this.fw.close();
    }

    private void setItemIDIndex() { 
        itemIDIdx = MyVars.app.getMsgBroker().getHeaderIndex(itemIDColNm);
    }
    private void setObjectIDIndex() { 
        objIDIdx = MyVars.app.getMsgBroker().getHeaderIndex(objIDColNm);
    }
    private void setItemNameIndex() { 
        itemNmIdx = MyVars.app.getMsgBroker().getHeaderIndex(itemNmColNm);
    }
    private void setTransactionIDIndex() { 
        trIDIdx = MyVars.app.getMsgBroker().getHeaderIndex(trIDColNm);
    }
    private void runWithCategorization(ArrayList<ArrayList<String>> dataIn) {
        try {
            MyVars.itemToIdMap = new HashMap<>();
            String items = "";
            String seq = "";
            String preObjID = dataIn.get(0).get(objIDIdx);
            String preTrID = dataIn.get(0).get(trIDIdx);
            int recCnt = 0;
            for (; recCnt < dataIn.size(); recCnt++) {
                MyVars.itemToIdMap.put(
                    dataIn.get(recCnt).get(itemIDIdx),
                    dataIn.get(recCnt).get(itemNmIdx)
                );
                if (dataIn.get(recCnt).get(objIDIdx).equals(preObjID)) {
                    if (dataIn.get(recCnt).get(trIDIdx).equals(preTrID)) {
                        if (items.length() == 0) {
                            items = dataIn.get(recCnt).get(itemIDIdx);
                        } else {
                            items = items + MyVars.commaDelimeter + dataIn.get(recCnt).get(itemIDIdx);
                        }
                    }
                    else {
                        if (seq.length() == 0) {
                            seq = items;
                        } else {
                            seq = seq + MyVars.hyphenDelimeter + items;
                        }
                        items = dataIn.get(recCnt).get(itemIDIdx);
                        preTrID = dataIn.get(recCnt).get(trIDIdx);
                    }
                }
                else {
                    if (seq.length() == 0) { seq = items; }
                    else {
                        seq = seq + MyVars.hyphenDelimeter + items;
                    }
                    String catStr = appendCategories(dataIn.get(--recCnt));
                    seq = catStr + seq;
                    this.fw.addSequence(dataIn.get(recCnt).get(objIDIdx), seq);
                    MyVars.sequeceFeatureCount++;
                    items = "";
                    seq = "";
                    preObjID = dataIn.get(recCnt+1).get(objIDIdx);
                    preTrID = dataIn.get(recCnt+1).get(trIDIdx);
                }
            }
            if (seq.length() != 0) {
                seq = seq + MyVars.hyphenDelimeter + items;
            } else { seq = items; }
            seq = appendCategories(dataIn.get(dataIn.size()-1)) + seq;
            this.fw.addSequence(dataIn.get(dataIn.size()-1).get(objIDIdx), seq);
            MyVars.sequeceFeatureCount++;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (this.fw != null) {
                this.fw.close();
            }
        }
    }

    private void runWithoutCategorization(ArrayList<ArrayList<String>> dataIn) {
        try {
            MyVars.itemToIdMap = new HashMap<>();
            String items = "";
            String seq = "";
            String preObjID = dataIn.get(0).get(objIDIdx);
            String preTrID = dataIn.get(0).get(trIDIdx);
            int recCnt = 0;
            for (; recCnt < dataIn.size(); recCnt++) {
                MyVars.itemToIdMap.put(
                        dataIn.get(recCnt).get(itemIDIdx),
                        dataIn.get(recCnt).get(itemNmIdx)
                );
                if (dataIn.get(recCnt).get(objIDIdx).equals(preObjID)) {
                    if (dataIn.get(recCnt).get(trIDIdx).equals(preTrID)) {
                        if (items.length() == 0) {
                            items = dataIn.get(recCnt).get(itemIDIdx);
                        } else {
                            items = items + MyVars.commaDelimeter + dataIn.get(recCnt).get(itemIDIdx);
                        }
                    }
                    else {
                        if (seq.length() == 0) {
                            seq = items;
                        } else {
                            seq = seq + MyVars.hyphenDelimeter + items;
                        }
                        items = dataIn.get(recCnt).get(itemIDIdx);
                        preTrID = dataIn.get(recCnt).get(trIDIdx);
                    }
                }
                else {
                    if (seq.length() == 0) { seq = items; }
                    else {
                        seq = seq + MyVars.hyphenDelimeter + items;
                    }
                    this.fw.addSequence(dataIn.get(--recCnt).get(objIDIdx), seq);
                    MyVars.sequeceFeatureCount++;
                    items = "";
                    seq = "";
                    preObjID = dataIn.get(recCnt+1).get(objIDIdx);
                    preTrID = dataIn.get(recCnt+1).get(trIDIdx);
                }
            }
            if (seq.length() != 0) {
                seq = seq + MyVars.hyphenDelimeter + items;
            } else { seq = items; }
            this.fw.addSequence(dataIn.get(dataIn.size()-1).get(objIDIdx), seq);
            MyVars.sequeceFeatureCount++;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String appendCategories(ArrayList<String> lastTrLine) {
        String variableStr = "";
        for (int i = 0; i < MyVars.app.getMsgBroker().getConfigPanel().getSupplimentaryVariableTable().getRowCount(); i++) {
            if (MyVars.app.getMsgBroker().getConfigPanel().getSupplimentaryVariableTable().getValueAt(i, 0).toString().contains("SET") &&
                MyVars.app.getMsgBroker().getConfigPanel().getSupplimentaryVariableTable().getValueAt(i, 1).toString().replaceAll(" ", "").length() == 0 &&
                MyVars.app.getMsgBroker().getConfigPanel().getSupplimentaryVariableTable().getValueAt(i, 2).toString().contains("SET")) continue;
            String colNm = MyVars.app.getMsgBroker().getConfigPanel().getSupplimentaryVariableTable().getValueAt(i, 0).toString().substring(2);
            int colNmIdx = MyVars.app.getMsgBroker().getHeaderIndex(colNm);
            if (MyVars.app.getMsgBroker().getConfigPanel().getSupplimentaryVariableTable().getCategoryType(i, 2).contains("BINARY") ||
                MyVars.app.getMsgBroker().getConfigPanel().getSupplimentaryVariableTable().getCategoryType(i, 2).contains("INTEGER")) {
                if (variableStr.length() == 0) {
                    variableStr = lastTrLine.get(colNmIdx);
                } else {
                    variableStr = variableStr + MyVars.commaDelimeter + lastTrLine.get(colNmIdx);
                }
            } else if (MyVars.app.getMsgBroker().getConfigPanel().getSupplimentaryVariableTable().getCategoryType(i, 2).contains("REAL")) {
                MyCategory category = MyVars.app.getMsgBroker().getCategoryList().getCategory(colNm);
                if (variableStr.length() == 0) { variableStr = category.getCategory(lastTrLine.get(colNmIdx)); }
                else { variableStr = variableStr + MyVars.commaDelimeter + category.getCategory(lastTrLine.get(colNmIdx));
                }
            } else break;
        }
        variableStr = this.variableMapper.mapVariableToID(variableStr);
        return (variableStr + MyVars.hyphenDelimeter);
    }

}