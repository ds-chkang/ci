package medousa.sequential.feature;

import medousa.sequential.category.MySequentialGraphCategory;
import medousa.sequential.utils.MySequentialGraphSysUtil;
import medousa.sequential.utils.MySequentialGraphVars;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by changhee on 2017. 7. 14.
 */
public class MyTimeConstrainedFeatureGenerator {

    /**
     * Feature writer to save features to a file.
     */
    MyFeatureWriter fw = new MyFeatureWriter("sequencesWithObjectIDs.txt", "sequences.txt");

    private MyVaraibleNameToIdMapper variableMapper;

    /**
     * declare item id index that user has set in the UI table.
     */
    private int itemIDIdx;

    /**
     * declare item name index that user has set in the UI table.
     */
    private int itemNmIdx;

    /**
     * declare object id index that user has set in the UI table.
     */
    private int objIDIdx;

    /**
     * declare transaction id index that user has set in the UI table.
     */
    private int trIDIdx;

    private int trTimeIdx;

    /**
     * declare item id column name that user has set in the UI table.
     */
    private String itemIDColNm = "ITEM ID";

    /**
     * declare object item column name that user has set in the UI table.
     */
    private String objIDColNm = "OBJECT ID";

    /**
     * declare item name column name that user has set in the UI table.
     */
    private String itemNmColNm = "ITEM NAME";

    /**
     * declare transaction id column anme that user has set in the UI table.
     */
    private String trIDColNm = "TRANSACTION ID";

    private String trTimeColNm = "TRANSACTION TIME";

    /**
     * declare flag to set if feature generation has been successful or not.
     */
    private boolean isSucceeded = false;

    /**
     * default constructor.
     */
    public MyTimeConstrainedFeatureGenerator() {}

    /**
     * generates input features with choices of running with categorization or without categorization.
     * @param dataIn
     */
    public boolean generateInputFeatures(ArrayList<ArrayList<String>> dataIn) {
        setObjectIDIndex();
        setTransactionIDIndex();
        setItemIDIndex();
        setItemNameIndex();
        setTrTimeIdx();
        MySequentialGraphVars.totalRecords = dataIn.size();
        MySequentialGraphVars.itemToIdMap = new HashMap<>();
        this.variableMapper = new MyVaraibleNameToIdMapper();
        if (MySequentialGraphVars.isSupplementaryOn) {
            generateSequencesWithVariables(dataIn);
        } else {
            generateSequencesWithoutVariables(dataIn);
        }
        this.fw.close();
        return isSucceeded;
    }

    /**
     * set item id index location.
     */
    private void setItemIDIndex() { 
        itemIDIdx = MySequentialGraphVars.app.getSequentialGraphMsgBroker().getHeaderIndex(itemIDColNm);
    }

    /**
     * set object id index location.
     */
    private void setObjectIDIndex() { 
        objIDIdx = MySequentialGraphVars.app.getSequentialGraphMsgBroker().getHeaderIndex(objIDColNm);
    }

    /**
     * set item name index location.
     */
    private void setItemNameIndex() { 
        itemNmIdx = MySequentialGraphVars.app.getSequentialGraphMsgBroker().getHeaderIndex(itemNmColNm);
    }

    /**
     * set transaction id index location.
     */
    private void setTransactionIDIndex() { 
        trIDIdx = MySequentialGraphVars.app.getSequentialGraphMsgBroker().getHeaderIndex(trIDColNm);
    }

    /**
     * set transaction time location.
     */
    private void setTrTimeIdx() {
        trTimeIdx = MySequentialGraphVars.app.getSequentialGraphMsgBroker().getHeaderIndex(trTimeColNm);
        MySequentialGraphVars.accesssTimeColumnIdx = trTimeIdx;
    }

    private void generateSequencesWithVariables(ArrayList<ArrayList<String>> dataIn) {
        try {
            String items = "";
            String seq = "";
            String preObjID = dataIn.get(0).get(objIDIdx);
            String preTrID = dataIn.get(0).get(trIDIdx);
            String preTrTime = dataIn.get(0).get(trTimeIdx);
            for (int recCnt = 0; recCnt < dataIn.size(); recCnt++) {
                MySequentialGraphVars.itemToIdMap.put(dataIn.get(recCnt).get(itemIDIdx), dataIn.get(recCnt).get(itemNmIdx));
                if (dataIn.get(recCnt).get(objIDIdx).equals(preObjID)) {
                    if (dataIn.get(recCnt).get(trIDIdx).equals(preTrID)) {
                        if (items.length() == 0) {
                            items = dataIn.get(recCnt).get(this.itemIDIdx);
                        } else {
                            items = items + MySequentialGraphVars.commaDelimeter + dataIn.get(recCnt).get(itemIDIdx);
                        }
                    } else {
                        if (seq.length() == 0) {
                            seq = items + ":" + MySequentialGraphSysUtil.getTimeDifference(preTrTime, dataIn.get(recCnt-1).get(trTimeIdx));
                        } else {
                            seq = seq + MySequentialGraphVars.hyphenDelimeter + items + ":" + MySequentialGraphSysUtil.getTimeDifference(preTrTime, dataIn.get(recCnt-1).get(trTimeIdx));
                        }
                        items = dataIn.get(recCnt).get(itemIDIdx);
                        preTrID = dataIn.get(recCnt).get(trIDIdx);
                    }
                } else {
                    if (seq.length() == 0) {
                        seq = items + ":" + MySequentialGraphSysUtil.getTimeDifference(preTrTime, dataIn.get(recCnt-1).get(trTimeIdx));
                    } else {
                        seq = seq + MySequentialGraphVars.hyphenDelimeter + items + ":" + MySequentialGraphSysUtil.getTimeDifference(preTrTime, dataIn.get(recCnt-1).get(trTimeIdx));
                    }
                    String catStr = appendVariables(dataIn.get(--recCnt));
                    seq = catStr+seq;
                    this.fw.addSequence(dataIn.get(recCnt).get(objIDIdx), seq);
                    MySequentialGraphVars.sequeceFeatureCount++;
                    items = "";
                    seq = "";
                    preObjID = dataIn.get(recCnt+1).get(objIDIdx);
                    preTrID = dataIn.get(recCnt+1).get(trIDIdx);
                    preTrTime = dataIn.get(recCnt+1).get(trTimeIdx);
                }
            }
            items = items + ":" + MySequentialGraphSysUtil.getTimeDifference(preTrTime, dataIn.get(dataIn.size()-1).get(trTimeIdx));
            if (seq.length() != 0) {
                seq = seq + MySequentialGraphVars.hyphenDelimeter + items;
            } else {
                seq = items;
            }
            seq = appendVariables(dataIn.get(dataIn.size()-1))+seq;
            this.fw.addSequence(dataIn.get(dataIn.size()-1).get(objIDIdx), seq);
            MySequentialGraphVars.sequeceFeatureCount++;
            this.isSucceeded = true;
        } catch (Exception ex) {
            //ex.printStackTrace();
        }
    }

    private void generateSequencesWithoutVariables(ArrayList<ArrayList<String>> dataIn) {
        try {
            String items = "";
            String seq = "";
            String preObjID = dataIn.get(0).get(objIDIdx);
            String preTrID = dataIn.get(0).get(trIDIdx);
            String preTrTime = dataIn.get(0).get(trTimeIdx);
            for (int recCnt = 0; recCnt < dataIn.size(); recCnt++) {
                MySequentialGraphVars.itemToIdMap.put(dataIn.get(recCnt).get(itemIDIdx), dataIn.get(recCnt).get(itemNmIdx));
                //System.out.println(dataIn.get(recCnt) + "           objIDIdx: " + objIDIdx + "         preObjID: " + preObjID);
                if (dataIn.get(recCnt).get(objIDIdx).equals(preObjID)) {
                    if (dataIn.get(recCnt).get(trIDIdx).equals(preTrID)) {
                        if (items.length() == 0) {
                            items = dataIn.get(recCnt).get(this.itemIDIdx);
                        } else {
                            items = items + MySequentialGraphVars.commaDelimeter + dataIn.get(recCnt).get(itemIDIdx);
                        }
                    } else {
                        if (seq.length() == 0) {
                            seq = items + ":" + MySequentialGraphSysUtil.getTimeDifference(preTrTime, dataIn.get(recCnt-1).get(trTimeIdx));
                        } else {
                            seq = seq + MySequentialGraphVars.hyphenDelimeter + items + ":" + MySequentialGraphSysUtil.getTimeDifference(preTrTime, dataIn.get(recCnt-1).get(trTimeIdx));
                        }
                        items = dataIn.get(recCnt).get(itemIDIdx);
                        preTrID = dataIn.get(recCnt).get(trIDIdx);
                    }
                } else {
                    if (seq.length() == 0) {
                        seq = items + ":" + MySequentialGraphSysUtil.getTimeDifference(preTrTime, dataIn.get(recCnt-1).get(trTimeIdx));
                    } else {
                        seq = seq + MySequentialGraphVars.hyphenDelimeter + items + ":" + MySequentialGraphSysUtil.getTimeDifference(preTrTime, dataIn.get(recCnt-1).get(trTimeIdx));
                    }
                    //String catStr = appendVariables(dataIn.get(--recCnt));
                    //seq = catStr+seq;
                    this.fw.addSequence(dataIn.get(--recCnt).get(objIDIdx), seq);
                    MySequentialGraphVars.sequeceFeatureCount++;
                    items = "";
                    seq = "";
                    preObjID = dataIn.get(recCnt+1).get(objIDIdx);
                    preTrID = dataIn.get(recCnt+1).get(trIDIdx);
                    preTrTime = dataIn.get(recCnt+1).get(trTimeIdx);
                }
            }
            items = items + ":" + MySequentialGraphSysUtil.getTimeDifference(preTrTime, dataIn.get(dataIn.size()-1).get(trTimeIdx));
            if (seq.length() != 0) {
                seq = seq + MySequentialGraphVars.hyphenDelimeter + items;
            } else {
                seq = items;
            }
            //seq = appendVariables(dataIn.get(dataIn.size()-1))+seq;
            this.fw.addSequence(dataIn.get(dataIn.size()-1).get(objIDIdx), seq);
            MySequentialGraphVars.sequeceFeatureCount++;
            this.isSucceeded = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * appends categories fo the current record. The last line of records with same
     * transaction id of the same object id gets passed as the parameter.
     * @param lastTrLine
     * @return
     */
    private String appendVariables(ArrayList<String> lastTrLine) {
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
                if (variableStr.length() == 0) { variableStr = category.getCategory(lastTrLine.get(colNmIdx)); }
                else { variableStr = variableStr + MySequentialGraphVars.commaDelimeter + category.getCategory(lastTrLine.get(colNmIdx));
                }
            } else break;
        }
        if (MySequentialGraphVars.isTimeOn) {
            variableStr = this.variableMapper.mapVariableToID(variableStr) + ":0";
        } else {
            variableStr = this.variableMapper.mapVariableToID(variableStr);
        }
        return (variableStr + MySequentialGraphVars.hyphenDelimeter);
    }
}

