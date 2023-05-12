package medousa.sequential.feature;

import medousa.sequential.utils.MySequentialGraphVars;

import java.io.*;

public class MyTimeConstrainedFeatureModifier {

    public MyTimeConstrainedFeatureModifier() {}

    public void modifyTime() {
        try {
            File features = new File(MySequentialGraphVars.sequenceFileName);
            File modifiedFeatures = new File(MySequentialGraphVars.sequenceFileName + ".bak");
            modifiedFeatures.delete();
            modifiedFeatures.createNewFile();
            BufferedReader br = new BufferedReader(new FileReader(features));
            BufferedWriter bw = new BufferedWriter(new FileWriter(modifiedFeatures));
            String feature = "";
            String modifiedFeature = "";
            long previousTime = 0L;
            while ((feature = br.readLine()) != null) {
                String [] itemsets = feature.split(MySequentialGraphVars.hyphenDelimeter);
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
            modifiedFeatures.renameTo(new File(MySequentialGraphVars.sequenceFileName));
        } catch (Exception ex) {ex.printStackTrace();}
    }
}
