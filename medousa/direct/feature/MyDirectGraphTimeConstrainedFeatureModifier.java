package medousa.direct.feature;

import medousa.direct.utils.MyDirectGraphVars;

import java.io.*;

public class MyDirectGraphTimeConstrainedFeatureModifier {

    public MyDirectGraphTimeConstrainedFeatureModifier() {}

    public void modifyTime() {
        try {
            File features = new File(MyDirectGraphVars.sequenceFileName);
            File modifiedFeatures = new File(MyDirectGraphVars.sequenceFileName + ".bak");
            modifiedFeatures.delete();
            modifiedFeatures.createNewFile();
            BufferedReader br = new BufferedReader(new FileReader(features));
            BufferedWriter bw = new BufferedWriter(new FileWriter(modifiedFeatures));
            String feature = "";
            String modifiedFeature = "";
            long previousTime = 0L;
            while ((feature = br.readLine()) != null) {
                String [] itemsets = feature.split(MyDirectGraphVars.hyphenDelimeter);
                for (int i=0; i < itemsets.length; i++) {
                    if (i == 0) {
                        previousTime = Long.parseLong(itemsets[i].split(":")[1]);
                        modifiedFeature = itemsets[0];
                    } else {
                        String [] itemAndTime = itemsets[i].split(":");
                        long currentTime = Long.parseLong(itemAndTime[1]);
                        long timeDifference = currentTime - previousTime;
                        previousTime = currentTime;
                        modifiedFeature += "-" + itemAndTime[0] + ":" + timeDifference;
                    }
                }
                bw.write(modifiedFeature+"\n");
                modifiedFeature = "";
            }
            br.close();
            bw.close();
            features.delete();
            modifiedFeatures.renameTo(new File(MyDirectGraphVars.sequenceFileName));
        } catch (Exception ex) {ex.printStackTrace();}
    }

    public static void main(String [] args) {
        MyDirectGraphTimeConstrainedFeatureModifier featureTimeModifier = new MyDirectGraphTimeConstrainedFeatureModifier();
    }

}
