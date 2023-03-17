package datamining.feature;

import datamining.utils.system.MyVars;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by changhee on 2017. 7. 14.
 */
public class MyEdgeLabelFeatureGenerator {

    private int weightLoc;
    private int itemLoc;
    private int objIDIdx;
    private int trIDIdx;
    private String objIDColNm = "OBJECT ID";
    private String trIDColNm = "TRANSACTION ID";
    private String itemIDColNm = "ITEM ID";
    private MyWeightFeatureWriter fw;
    public MyEdgeLabelFeatureGenerator() {}

    public void generateEdgeLabelFeatures(String edgeValueVariable, ArrayList<ArrayList<String>> dataIn, String edgeValueType) {
        this.fw = new MyWeightFeatureWriter(edgeValueVariable.replaceAll(" ", "").toLowerCase(Locale.ENGLISH) + MyVars.edgeLabelFileExt);
        setObjectIDIndex();
        setTransactionIDIndex();
        setItemNameIndex(this.itemIDColNm);
        setWeightNameIndex(edgeValueVariable);
        run(dataIn, edgeValueType);
        this.fw.close();
    }

    private void setObjectIDIndex() { 
        objIDIdx = MyVars.app.getMsgBroker().getHeaderIndex(objIDColNm);
    }

    private void setWeightNameIndex(String edgeWeightVariable) {weightLoc = MyVars.app.getMsgBroker().getHeaderIndex(edgeWeightVariable);}

    private void setItemNameIndex(String itemIDColNm) {itemLoc = MyVars.app.getMsgBroker().getHeaderIndex(itemIDColNm);}

    private void setTransactionIDIndex() { 
        trIDIdx = MyVars.app.getMsgBroker().getHeaderIndex(trIDColNm);
    }

    private void run(ArrayList<ArrayList<String>> dataIn, String edgeValueType) {
        try {
            String itemsetDelimeter = MyVars.hyphenDelimeter;
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
                            weights = weights + MyVars.commaDelimeter + dataIn.get(recCnt).get(weightLoc);
                            items = items + MyVars.commaDelimeter + dataIn.get(recCnt).get(itemLoc);
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
                    this.fw.addSequence(this.setEdgeLabel(itemSeq, weightSeq, itemsetDelimeter));
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
            this.fw.addSequence(this.setEdgeLabel(itemSeq, weightSeq, itemsetDelimeter));
        } catch (Exception ex) {ex.printStackTrace();}
    }

    private String setEdgeLabel(String itemSeq, String weightSeq, String itemsetDelimeter) {
        String [] weightItemSets = weightSeq.split(itemsetDelimeter);
        String [] itemSets = itemSeq.split(itemsetDelimeter);
        String resultSeq = "";
        for (int i = 0; i < weightItemSets.length; i++) {
            if (resultSeq.length() == 0) {resultSeq = itemSets[i] + itemsetDelimeter + weightItemSets[i];
            } else {resultSeq += itemsetDelimeter + itemSets[i] + itemsetDelimeter + weightItemSets[i];}
        }
        return resultSeq;
    }
}