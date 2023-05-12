package medousa.direct.feature;

import medousa.direct.utils.MyDirectGraphSysUtil;
import medousa.direct.utils.MyDirectGraphVars;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by changhee on 2017. 7. 14.
 */
public class MyDirectGraphTimeConstrainedFeatureGenerator {

    /**
     * Feature writer to save features to a file.
     */
    MyDirectGraphFeatureWriter fw = new MyDirectGraphFeatureWriter("sequencesWithObjectIDs.txt", "sequences.txt");

    private MyDirectGraphVaraibleNameToIdMapper variableMapper;

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
    public MyDirectGraphTimeConstrainedFeatureGenerator() {}

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
        MyDirectGraphVars.totalRecords = dataIn.size();
        MyDirectGraphVars.itemToIdMap = new HashMap<>();
        if (MyDirectGraphVars.isSupplementaryOn) {
            this.variableMapper = new MyDirectGraphVaraibleNameToIdMapper();
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
        itemIDIdx = MyDirectGraphVars.app.getDirectGraphMsgBroker().getHeaderIndex(itemIDColNm);
    }

    /**
     * set object id index location.
     */
    private void setObjectIDIndex() { 
        objIDIdx = MyDirectGraphVars.app.getDirectGraphMsgBroker().getHeaderIndex(objIDColNm);
    }

    /**
     * set item name index location.
     */
    private void setItemNameIndex() { 
        itemNmIdx = MyDirectGraphVars.app.getDirectGraphMsgBroker().getHeaderIndex(itemNmColNm);
    }

    /**
     * set transaction id index location.
     */
    private void setTransactionIDIndex() { 
        trIDIdx = MyDirectGraphVars.app.getDirectGraphMsgBroker().getHeaderIndex(trIDColNm);
    }

    /**
     * set transaction time location.
     */
    private void setTrTimeIdx() {
        trTimeIdx = MyDirectGraphVars.app.getDirectGraphMsgBroker().getHeaderIndex(trTimeColNm);
    }

    private void generateSequencesWithVariables(ArrayList<ArrayList<String>> dataIn) {
        try {
            String items = "";
            String seq = "";
            String preObjID = dataIn.get(0).get(objIDIdx);
            String preTrID = dataIn.get(0).get(trIDIdx);
            String preTrTime = dataIn.get(0).get(trTimeIdx);
            for (int recCnt = 0; recCnt < dataIn.size(); recCnt++) {
                MyDirectGraphVars.itemToIdMap.put(dataIn.get(recCnt).get(itemIDIdx), dataIn.get(recCnt).get(itemNmIdx));
                if (dataIn.get(recCnt).get(objIDIdx).equals(preObjID)) {
                    if (dataIn.get(recCnt).get(trIDIdx).equals(preTrID)) {
                        if (items.length() == 0) {
                            items = dataIn.get(recCnt).get(this.itemIDIdx);
                        } else {
                            items = items + MyDirectGraphVars.commaDelimeter + dataIn.get(recCnt).get(itemIDIdx);
                        }
                    } else {
                        if (seq.length() == 0) {
                            seq = items + ":" + MyDirectGraphSysUtil.getTimeDifference(preTrTime, dataIn.get(recCnt-1).get(trTimeIdx));
                        } else {
                            seq = seq + MyDirectGraphVars.hyphenDelimeter + items + ":" + MyDirectGraphSysUtil.getTimeDifference(preTrTime, dataIn.get(recCnt-1).get(trTimeIdx));
                        }
                        items = dataIn.get(recCnt).get(itemIDIdx);
                        preTrID = dataIn.get(recCnt).get(trIDIdx);
                    }
                } else {
                    if (seq.length() == 0) {
                        seq = items + ":" + MyDirectGraphSysUtil.getTimeDifference(preTrTime, dataIn.get(recCnt-1).get(trTimeIdx));
                    } else {
                        seq = seq + MyDirectGraphVars.hyphenDelimeter + items + ":" + MyDirectGraphSysUtil.getTimeDifference(preTrTime, dataIn.get(recCnt-1).get(trTimeIdx));
                    }
                    String catStr = appendVariables(dataIn.get(--recCnt));
                    seq = catStr+seq;
                    this.fw.addSequence(dataIn.get(recCnt).get(objIDIdx), seq);
                    MyDirectGraphVars.sequeceFeatureCount++;
                    items = "";
                    seq = "";
                    preObjID = dataIn.get(recCnt+1).get(objIDIdx);
                    preTrID = dataIn.get(recCnt+1).get(trIDIdx);
                    preTrTime = dataIn.get(recCnt+1).get(trTimeIdx);
                }
            }
            items = items + ":" + MyDirectGraphSysUtil.getTimeDifference(preTrTime, dataIn.get(dataIn.size()-1).get(trTimeIdx));
            if (seq.length() != 0) {
                seq = seq + MyDirectGraphVars.hyphenDelimeter + items;
            } else {
                seq = items;
            }
            seq = appendVariables(dataIn.get(dataIn.size()-1))+seq;
            this.fw.addSequence(dataIn.get(dataIn.size()-1).get(objIDIdx), seq);
            MyDirectGraphVars.sequeceFeatureCount++;
            this.isSucceeded = true;
        } catch (Exception ex) {
            ex.printStackTrace();
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
                MyDirectGraphVars.itemToIdMap.put(dataIn.get(recCnt).get(itemIDIdx), dataIn.get(recCnt).get(itemNmIdx));
                if (dataIn.get(recCnt).get(objIDIdx).equals(preObjID)) { // 동일한 객체인지 확인.
                    if (dataIn.get(recCnt).get(trIDIdx).equals(preTrID)) { // 동일한 객체가 동일내에서 동일한 트랜잭션인지 확인.
                        if (items.length() == 0) {
                            items = dataIn.get(recCnt).get(this.itemIDIdx);
                        } else {
                            items = items + MyDirectGraphVars.commaDelimeter + dataIn.get(recCnt).get(itemIDIdx);
                        }
                    } else {
                        if (seq.length() == 0) {
                            seq = items + ":" + MyDirectGraphSysUtil.getTimeDifference(preTrTime, dataIn.get(recCnt-1).get(trTimeIdx));
                        } else {
                            seq = seq + MyDirectGraphVars.hyphenDelimeter + items + ":" + MyDirectGraphSysUtil.getTimeDifference(preTrTime, dataIn.get(recCnt-1).get(trTimeIdx));
                        }
                        items = dataIn.get(recCnt).get(itemIDIdx);
                        preTrID = dataIn.get(recCnt).get(trIDIdx);
                    }
                } else {
                    if (seq.length() == 0) {
                        seq = items + ":" + MyDirectGraphSysUtil.getTimeDifference(preTrTime, dataIn.get(recCnt-1).get(trTimeIdx));
                    } else {
                        seq = seq + MyDirectGraphVars.hyphenDelimeter + items + ":" + MyDirectGraphSysUtil.getTimeDifference(preTrTime, dataIn.get(recCnt-1).get(trTimeIdx));
                    }
                    this.fw.addSequence(dataIn.get(recCnt).get(objIDIdx), seq);
                    MyDirectGraphVars.sequeceFeatureCount++;
                    items = "";
                    seq = "";
                    preObjID = dataIn.get(recCnt).get(objIDIdx);
                    preTrID = dataIn.get(recCnt).get(trIDIdx);
                    preTrTime = dataIn.get(recCnt).get(trTimeIdx);
                    recCnt--;
                }
            }
            items = items + ":" + MyDirectGraphSysUtil.getTimeDifference(preTrTime, dataIn.get(dataIn.size()-1).get(trTimeIdx));
            if (seq.length() != 0) {
                seq = seq + MyDirectGraphVars.hyphenDelimeter + items;
            } else {
                seq = items;
            }
            this.fw.addSequence(dataIn.get(dataIn.size()-1).get(objIDIdx), seq);
            MyDirectGraphVars.sequeceFeatureCount++;
            this.isSucceeded = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {}
    }

    /**
     * appends categories fo the current record. The last line of records with same
     * transaction id of the same object id gets passed as the parameter.
     * @param lastTrLine
     * @return
     */
    private String appendVariables(ArrayList<String> lastTrLine) {
        return "";
                    }
}

