package medousa.direct.feature;

import medousa.direct.utils.MyDirectGraphVars;

import java.util.ArrayList;
import java.util.HashMap;

public class MyDirectGraphFeatureGenerator {

    MyDirectGraphFeatureWriter fw = new MyDirectGraphFeatureWriter("sequencesWithObjectIDs.txt", "sequences.txt");
    private MyDirectGraphVaraibleNameToIdMapper variableMapper = new MyDirectGraphVaraibleNameToIdMapper();
    private int itemIDIdx;
    private int itemNmIdx;
    private int objIDIdx;
    private int trIDIdx;
    private String itemIDColNm = "ITEM ID";
    private String objIDColNm = "OBJECT ID";
    private String itemNmColNm = "ITEM NAME";
    private String trIDColNm = "TRANSACTION ID";

    public MyDirectGraphFeatureGenerator() {
        MyDirectGraphVars.sequeceFeatureCount = 0;
    }

    public void generateInputFeatures(ArrayList<ArrayList<String>> dataIn) {
        setObjectIDIndex();
        setTransactionIDIndex();
        setItemIDIndex();
        setItemNameIndex();
        MyDirectGraphVars.totalRecords = dataIn.size();
        if (MyDirectGraphVars.isSupplementaryOn) {
            runWithCategorization(dataIn);
        } else {
            runWithoutCategorization(dataIn);
        }
        this.fw.close();
    }

    private void setItemIDIndex() { 
        itemIDIdx = MyDirectGraphVars.app.getDirectGraphMsgBroker().getHeaderIndex(itemIDColNm);
    }
    private void setObjectIDIndex() { 
        objIDIdx = MyDirectGraphVars.app.getDirectGraphMsgBroker().getHeaderIndex(objIDColNm);
    }
    private void setItemNameIndex() { 
        itemNmIdx = MyDirectGraphVars.app.getDirectGraphMsgBroker().getHeaderIndex(itemNmColNm);
    }
    private void setTransactionIDIndex() { 
        trIDIdx = MyDirectGraphVars.app.getDirectGraphMsgBroker().getHeaderIndex(trIDColNm);
    }
    private void runWithCategorization(ArrayList<ArrayList<String>> dataIn) {
        try {
            MyDirectGraphVars.itemToIdMap = new HashMap<>();
            String items = "";
            String seq = "";
            String preObjID = dataIn.get(0).get(objIDIdx);
            String preTrID = dataIn.get(0).get(trIDIdx);
            int recCnt = 0;
            for (; recCnt < dataIn.size(); recCnt++) {
                MyDirectGraphVars.itemToIdMap.put(
                    dataIn.get(recCnt).get(itemIDIdx),
                    dataIn.get(recCnt).get(itemNmIdx)
                );
                if (dataIn.get(recCnt).get(objIDIdx).equals(preObjID)) {
                    if (dataIn.get(recCnt).get(trIDIdx).equals(preTrID)) {
                        if (items.length() == 0) {
                            items = dataIn.get(recCnt).get(itemIDIdx);
                        } else {
                            items = items + MyDirectGraphVars.commaDelimeter + dataIn.get(recCnt).get(itemIDIdx);
                        }
                    }
                    else {
                        if (seq.length() == 0) {
                            seq = items;
                        } else {
                            seq = seq + MyDirectGraphVars.hyphenDelimeter + items;
                        }
                        items = dataIn.get(recCnt).get(itemIDIdx);
                        preTrID = dataIn.get(recCnt).get(trIDIdx);
                    }
                }
                else {
                    if (seq.length() == 0) { seq = items; }
                    else {
                        seq = seq + MyDirectGraphVars.hyphenDelimeter + items;
                    }
                    String catStr = appendCategories(dataIn.get(--recCnt));
                    seq = catStr + seq;
                    this.fw.addSequence(dataIn.get(recCnt).get(objIDIdx), seq);
                    MyDirectGraphVars.sequeceFeatureCount++;
                    items = "";
                    seq = "";
                    preObjID = dataIn.get(recCnt+1).get(objIDIdx);
                    preTrID = dataIn.get(recCnt+1).get(trIDIdx);
                }
            }
            if (seq.length() != 0) {
                seq = seq + MyDirectGraphVars.hyphenDelimeter + items;
            } else { seq = items; }
            seq = appendCategories(dataIn.get(dataIn.size()-1)) + seq;
            this.fw.addSequence(dataIn.get(dataIn.size()-1).get(objIDIdx), seq);
            MyDirectGraphVars.sequeceFeatureCount++;
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
            MyDirectGraphVars.itemToIdMap = new HashMap<>();
            String items = "";
            String seq = "";
            String preObjID = dataIn.get(0).get(objIDIdx);
            String preTrID = dataIn.get(0).get(trIDIdx);
            int recCnt = 0;
            for (; recCnt < dataIn.size(); recCnt++) {
                MyDirectGraphVars.itemToIdMap.put(
                        dataIn.get(recCnt).get(itemIDIdx),
                        dataIn.get(recCnt).get(itemNmIdx)
                );
                if (dataIn.get(recCnt).get(objIDIdx).equals(preObjID)) {
                    if (dataIn.get(recCnt).get(trIDIdx).equals(preTrID)) {
                        if (items.length() == 0) {
                            items = dataIn.get(recCnt).get(itemIDIdx);
                        } else {
                            items = items + MyDirectGraphVars.commaDelimeter + dataIn.get(recCnt).get(itemIDIdx);
                        }
                    }
                    else {
                        if (seq.length() == 0) {
                            seq = items;
                        } else {
                            seq = seq + MyDirectGraphVars.hyphenDelimeter + items;
                        }
                        items = dataIn.get(recCnt).get(itemIDIdx);
                        preTrID = dataIn.get(recCnt).get(trIDIdx);
                    }
                }
                else {
                    if (seq.length() == 0) { seq = items; }
                    else {
                        seq = seq + MyDirectGraphVars.hyphenDelimeter + items;
                    }
                    this.fw.addSequence(dataIn.get(--recCnt).get(objIDIdx), seq);
                    MyDirectGraphVars.sequeceFeatureCount++;
                    items = "";
                    seq = "";
                    preObjID = dataIn.get(recCnt+1).get(objIDIdx);
                    preTrID = dataIn.get(recCnt+1).get(trIDIdx);
                }
            }
            if (seq.length() != 0) {
                seq = seq + MyDirectGraphVars.hyphenDelimeter + items;
            } else { seq = items; }
            this.fw.addSequence(dataIn.get(dataIn.size()-1).get(objIDIdx), seq);
            MyDirectGraphVars.sequeceFeatureCount++;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String appendCategories(ArrayList<String> lastTrLine) {
        String variableStr = "";

        variableStr = this.variableMapper.mapVariableToID(variableStr);
        return (variableStr + MyDirectGraphVars.hyphenDelimeter);
    }

}