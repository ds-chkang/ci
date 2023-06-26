package medousa.sequential.feature;

import medousa.sequential.utils.MySequentialGraphSysUtil;
import medousa.sequential.utils.MySequentialGraphVars;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by changhee on 2017. 7. 14.
 */
public class MyNodeDayFeatureGenerator {

    /**
     * Feature writer to save features to a file.
     */
    MyFeatureWriter fw = new MyFeatureWriter("nodeDayFeatures.txt");

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
     * default constructor.
     */
    public MyNodeDayFeatureGenerator() {}

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
        if (trTimeIdx == -1) return false;
        generateSequences(dataIn);
        this.fw.close();
        return true;
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

    private void generateSequences(ArrayList<ArrayList<String>> dataIn) {
        try {
            String items = "";
            String seq = "";
            String preObjID = dataIn.get(0).get(objIDIdx);
            String preTrID = dataIn.get(0).get(trIDIdx);
            String preTrTime = dataIn.get(0).get(trTimeIdx);
            for (int recCnt = 0; recCnt < dataIn.size(); recCnt++) {
                if (dataIn.get(recCnt).get(objIDIdx).equals(preObjID)) { // 동일한 객체인지 확인.
                    if (dataIn.get(recCnt).get(trIDIdx).equals(preTrID)) { // 동일한 객체가 동일내에서 동일한 트랜잭션인지 확인.
                        if (items.length() == 0) {
                            items = dataIn.get(recCnt).get(this.itemIDIdx);
                        } else {
                            items = items + MySequentialGraphVars.commaDelimeter + dataIn.get(recCnt).get(itemIDIdx);;

                        }
                    } else {
                        if (seq.length() == 0) {
                            seq = items + ":" + MySequentialGraphSysUtil.getDayFromDate(dataIn.get(dataIn.size()-1).get(trTimeIdx));
                        } else {
                            seq = seq + MySequentialGraphVars.hyphenDelimeter + items + ":" + MySequentialGraphSysUtil.getDayFromDate(dataIn.get(dataIn.size()-1).get(trTimeIdx));
                        }
                        items = dataIn.get(recCnt).get(itemIDIdx);
                        preTrID = dataIn.get(recCnt).get(trIDIdx);
                    }
                } else {
                    if (seq.length() == 0) {
                        seq = items + ":" + MySequentialGraphSysUtil.getDayFromDate(dataIn.get(dataIn.size()-1).get(trTimeIdx));
                    } else {
                        seq = seq + MySequentialGraphVars.hyphenDelimeter + items + ":" + MySequentialGraphSysUtil.getDayFromDate(dataIn.get(dataIn.size()-1).get(trTimeIdx));
                    }
                    this.fw.addSequence(seq);
                    items = "";
                    seq = "";
                    preObjID = dataIn.get(recCnt).get(objIDIdx);
                    preTrID = dataIn.get(recCnt).get(trIDIdx);
                    preTrTime = dataIn.get(recCnt).get(trTimeIdx);
                    recCnt--;
                }
            }
            items = items + ":" + MySequentialGraphSysUtil.getDayFromDate(dataIn.get(dataIn.size()-1).get(trTimeIdx));
            if (seq.length() != 0) {
                seq = seq + MySequentialGraphVars.hyphenDelimeter + items;
            } else {
                seq = items;
            }
            this.fw.addSequence(seq);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

