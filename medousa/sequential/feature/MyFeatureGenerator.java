package medousa.sequential.feature;

import medousa.sequential.category.MySequentialGraphCategory;
import medousa.sequential.utils.MySequentialGraphVars;

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
        MySequentialGraphVars.sequeceFeatureCount = 0;
    }

    public void generateInputFeatures(ArrayList<ArrayList<String>> dataIn) {
        setObjectIDIndex();
        setTransactionIDIndex();
        setItemIDIndex();
        setItemNameIndex();
        MySequentialGraphVars.totalRecords = dataIn.size();
        if (MySequentialGraphVars.isSupplementaryOn) {
            runWithCategorization(dataIn);
        } else {
            runWithoutCategorization(dataIn);
        }
        this.fw.close();
    }

    private void setItemIDIndex() { 
        itemIDIdx = MySequentialGraphVars.app.getSequentialGraphMsgBroker().getHeaderIndex(itemIDColNm);
    }
    private void setObjectIDIndex() { 
        objIDIdx = MySequentialGraphVars.app.getSequentialGraphMsgBroker().getHeaderIndex(objIDColNm);
    }
    private void setItemNameIndex() { 
        itemNmIdx = MySequentialGraphVars.app.getSequentialGraphMsgBroker().getHeaderIndex(itemNmColNm);
    }
    private void setTransactionIDIndex() { 
        trIDIdx = MySequentialGraphVars.app.getSequentialGraphMsgBroker().getHeaderIndex(trIDColNm);
    }
    private void runWithCategorization(ArrayList<ArrayList<String>> dataIn) {
        try {
            MySequentialGraphVars.itemToIdMap = new HashMap<>();
            String items = "";
            String seq = "";
            String preObjID = dataIn.get(0).get(objIDIdx);
            String preTrID = dataIn.get(0).get(trIDIdx);
            int recCnt = 0;
            for (; recCnt < dataIn.size(); recCnt++) {
                MySequentialGraphVars.itemToIdMap.put(
                    dataIn.get(recCnt).get(itemIDIdx),
                    dataIn.get(recCnt).get(itemNmIdx)
                );
                if (dataIn.get(recCnt).get(objIDIdx).equals(preObjID)) {
                    if (dataIn.get(recCnt).get(trIDIdx).equals(preTrID)) {
                        if (items.length() == 0) {
                            items = dataIn.get(recCnt).get(itemIDIdx);
                        } else {
                            items = items + MySequentialGraphVars.commaDelimeter + dataIn.get(recCnt).get(itemIDIdx);
                        }
                    }
                    else {
                        if (seq.length() == 0) {
                            seq = items;
                        } else {
                            seq = seq + MySequentialGraphVars.hyphenDelimeter + items;
                        }
                        items = dataIn.get(recCnt).get(itemIDIdx);
                        preTrID = dataIn.get(recCnt).get(trIDIdx);
                    }
                }
                else {
                    if (seq.length() == 0) { seq = items; }
                    else {
                        seq = seq + MySequentialGraphVars.hyphenDelimeter + items;
                    }
                    String catStr = appendCategories(dataIn.get(--recCnt));
                    seq = catStr + seq;
                    this.fw.addSequence(dataIn.get(recCnt).get(objIDIdx), seq);
                    MySequentialGraphVars.sequeceFeatureCount++;
                    items = "";
                    seq = "";
                    preObjID = dataIn.get(recCnt+1).get(objIDIdx);
                    preTrID = dataIn.get(recCnt+1).get(trIDIdx);
                }
            }
            if (seq.length() != 0) {
                seq = seq + MySequentialGraphVars.hyphenDelimeter + items;
            } else { seq = items; }
            seq = appendCategories(dataIn.get(dataIn.size()-1)) + seq;
            this.fw.addSequence(dataIn.get(dataIn.size()-1).get(objIDIdx), seq);
            MySequentialGraphVars.sequeceFeatureCount++;
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
            MySequentialGraphVars.itemToIdMap = new HashMap<>();
            String items = "";
            String seq = "";
            String preObjID = dataIn.get(0).get(objIDIdx);
            String preTrID = dataIn.get(0).get(trIDIdx);
            int recCnt = 0;
            for (; recCnt < dataIn.size(); recCnt++) {
                MySequentialGraphVars.itemToIdMap.put(
                        dataIn.get(recCnt).get(itemIDIdx),
                        dataIn.get(recCnt).get(itemNmIdx)
                );
                if (dataIn.get(recCnt).get(objIDIdx).equals(preObjID)) {
                    if (dataIn.get(recCnt).get(trIDIdx).equals(preTrID)) {
                        if (items.length() == 0) {
                            items = dataIn.get(recCnt).get(itemIDIdx);
                        } else {
                            items = items + MySequentialGraphVars.commaDelimeter + dataIn.get(recCnt).get(itemIDIdx);
                        }
                    }
                    else {
                        if (seq.length() == 0) {
                            seq = items;
                        } else {
                            seq = seq + MySequentialGraphVars.hyphenDelimeter + items;
                        }
                        items = dataIn.get(recCnt).get(itemIDIdx);
                        preTrID = dataIn.get(recCnt).get(trIDIdx);
                    }
                }
                else {
                    if (seq.length() == 0) { seq = items; }
                    else {
                        seq = seq + MySequentialGraphVars.hyphenDelimeter + items;
                    }
                    this.fw.addSequence(dataIn.get(--recCnt).get(objIDIdx), seq);
                    MySequentialGraphVars.sequeceFeatureCount++;
                    items = "";
                    seq = "";
                    preObjID = dataIn.get(recCnt+1).get(objIDIdx);
                    preTrID = dataIn.get(recCnt+1).get(trIDIdx);
                }
            }
            if (seq.length() != 0) {
                seq = seq + MySequentialGraphVars.hyphenDelimeter + items;
            } else { seq = items; }
            this.fw.addSequence(dataIn.get(dataIn.size()-1).get(objIDIdx), seq);
            MySequentialGraphVars.sequeceFeatureCount++;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String appendCategories(ArrayList<String> lastTrLine) {
        String variableStr = "";
        for (int i = 0; i < MySequentialGraphVars.app.getSequentialGraphMsgBroker().getConfigPanel().getSupplimentaryVariableTable().getRowCount(); i++) {
            if (MySequentialGraphVars.app.getSequentialGraphMsgBroker().getConfigPanel().getSupplimentaryVariableTable().getValueAt(i, 0).toString().contains("SET") &&
                MySequentialGraphVars.app.getSequentialGraphMsgBroker().getConfigPanel().getSupplimentaryVariableTable().getValueAt(i, 1).toString().replaceAll(" ", "").length() == 0 &&
                MySequentialGraphVars.app.getSequentialGraphMsgBroker().getConfigPanel().getSupplimentaryVariableTable().getValueAt(i, 2).toString().contains("SET")) continue;
            String colNm = MySequentialGraphVars.app.getSequentialGraphMsgBroker().getConfigPanel().getSupplimentaryVariableTable().getValueAt(i, 0).toString().substring(2);
            int colNmIdx = MySequentialGraphVars.app.getSequentialGraphMsgBroker().getHeaderIndex(colNm);
            if (MySequentialGraphVars.app.getSequentialGraphMsgBroker().getConfigPanel().getSupplimentaryVariableTable().getCategoryType(i, 2).contains("BINARY") ||
                MySequentialGraphVars.app.getSequentialGraphMsgBroker().getConfigPanel().getSupplimentaryVariableTable().getCategoryType(i, 2).contains("INTEGER")) {
                if (variableStr.length() == 0) {
                    variableStr = lastTrLine.get(colNmIdx);
                } else {
                    variableStr = variableStr + MySequentialGraphVars.commaDelimeter + lastTrLine.get(colNmIdx);
                }
            } else if (MySequentialGraphVars.app.getSequentialGraphMsgBroker().getConfigPanel().getSupplimentaryVariableTable().getCategoryType(i, 2).contains("REAL")) {
                MySequentialGraphCategory category = MySequentialGraphVars.app.getSequentialGraphMsgBroker().getCategoryList().getCategory(colNm);
                if (variableStr.length() == 0) {
                    variableStr = category.getCategory(lastTrLine.get(colNmIdx));
                } else {
                    variableStr = variableStr + MySequentialGraphVars.commaDelimeter + category.getCategory(lastTrLine.get(colNmIdx));
                }
            } else break;
        }
        variableStr = this.variableMapper.mapVariableToID(variableStr);
        return (variableStr + MySequentialGraphVars.hyphenDelimeter);
    }

}