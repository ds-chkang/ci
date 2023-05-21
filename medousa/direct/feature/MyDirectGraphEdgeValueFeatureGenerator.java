package medousa.direct.feature;

import medousa.direct.utils.MyDirectGraphVars;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by changhee on 2017. 7. 14.
 */
public class MyDirectGraphEdgeValueFeatureGenerator {

    private int weightLoc;

    private int itemLoc;

    private int objIDIdx;

    private int trIDIdx;

    private String objIDColNm = "OBJECT ID";

    private String trIDColNm = "TRANSACTION ID";

    private String itemIDColNm = "ITEM ID";

    private MyDirectGraphWeightFeatureWriter fw;

    public MyDirectGraphEdgeValueFeatureGenerator() {}

    public void generateEdgeValueFeatures(String edgeValueVariable, ArrayList<ArrayList<String>> dataIn, String edgeValueType) {
        this.fw = new MyDirectGraphWeightFeatureWriter(edgeValueVariable.replaceAll(" ", "").toLowerCase(Locale.ENGLISH) + MyDirectGraphVars.edgeValueFileExt);
        setObjectIDIndex();
        setTransactionIDIndex();
        setItemNameIndex(this.itemIDColNm);
        setWeightNameIndex(edgeValueVariable);
        run(dataIn, edgeValueType);
        this.fw.close();
    }

    private void setObjectIDIndex() { 
        objIDIdx = MyDirectGraphVars.app.getDirectGraphMsgBroker().getHeaderIndex(objIDColNm);
    }

    private void setWeightNameIndex(String edgeWeightVariable) {
        weightLoc = MyDirectGraphVars.app.getDirectGraphMsgBroker().getHeaderIndex(edgeWeightVariable);
    }

    private void setItemNameIndex(String itemIDColNm) {
        itemLoc = MyDirectGraphVars.app.getDirectGraphMsgBroker().getHeaderIndex(itemIDColNm);
    }

    private void setTransactionIDIndex() { 
        trIDIdx = MyDirectGraphVars.app.getDirectGraphMsgBroker().getHeaderIndex(trIDColNm);
    }

    private void run(ArrayList<ArrayList<String>> dataIn, String edgeValueType) {
        try {
            String itemsetDelimeter = ((edgeValueType.contains("DIFFERENCE") || edgeValueType.contains("MAXIMUM")) ? ":" : MyDirectGraphVars.hyphenDelimeter);
            String items = "";
            String weights = "";
            String itemSeq = "";
            String weightSeq = "";
            String preObjID = dataIn.get(0).get(objIDIdx);
            String preTrID = dataIn.get(0).get(trIDIdx);
            int recCnt = 0;
            for (; recCnt < dataIn.size(); recCnt++) {
                if (dataIn.get(recCnt).get(objIDIdx).equals(preObjID)) {
                    if (dataIn.get(recCnt).get(trIDIdx).equals(preTrID)) {
                        if (weights.length() == 0) {
                            items = dataIn.get(recCnt).get(itemLoc);
                            weights = dataIn.get(recCnt).get(weightLoc);
                        } else {
                            weights = weights + MyDirectGraphVars.commaDelimeter + dataIn.get(recCnt).get(weightLoc);
                            items = items + MyDirectGraphVars.commaDelimeter + dataIn.get(recCnt).get(itemLoc);
                        }
                    } else {
                        if (weightSeq.length() == 0) {
                            weightSeq = weights;
                            itemSeq = items;
                        } else {
                            weightSeq += itemsetDelimeter + weights;
                            itemSeq += itemsetDelimeter + items;
                        }
                        items = dataIn.get(recCnt).get(itemLoc);
                        weights = dataIn.get(recCnt).get(weightLoc);
                        preTrID = dataIn.get(recCnt).get(trIDIdx);
                    }
                } else {
                    if (weightSeq.length() == 0) {
                        weightSeq = weights;
                        itemSeq = items;
                    } else {
                        weightSeq = weightSeq + itemsetDelimeter + weights;
                        itemSeq = itemSeq + itemsetDelimeter + items;
                    }
                    this.fw.addSequence(this.setEdgeValue(itemSeq, weightSeq, itemsetDelimeter));
                    weights = "";
                    weightSeq = "";
                    items = "";
                    itemSeq = "";
                    preObjID = dataIn.get(recCnt).get(objIDIdx);
                    preTrID = dataIn.get(recCnt).get(trIDIdx);
                    --recCnt;
                }
            }
            if (weightSeq.length() != 0) {
                weightSeq = weightSeq + itemsetDelimeter + weights;
                itemSeq = itemSeq + itemsetDelimeter + items;
            } else {
                weightSeq = weights;
                itemSeq = items;
            }
            this.fw.addSequence(this.setEdgeValue(itemSeq, weightSeq, itemsetDelimeter));
        } catch (Exception ex) {ex.printStackTrace();}
    }

    private String setEdgeValue(String itemSeq, String weightSeq, String itemsetDelimeter) {
        String [] weightItemSets = weightSeq.split(itemsetDelimeter);
        String [] itemSets = itemSeq.split(itemsetDelimeter);
        String resultSeq = "";
        for (int i = 0; i < weightItemSets.length; i++) {
            if (resultSeq.length() == 0) {resultSeq = itemSets[i] + itemsetDelimeter + weightItemSets[i];}
            else {resultSeq += itemsetDelimeter + itemSets[i] + itemsetDelimeter + weightItemSets[i];}
        }
        return resultSeq;
    }
}