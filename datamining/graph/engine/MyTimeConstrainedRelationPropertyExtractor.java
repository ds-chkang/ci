package datamining.graph.engine;

import datamining.utils.system.MyVars;
import java.io.File;

public class MyTimeConstrainedRelationPropertyExtractor
extends MyRelationPropertyExtractor {
    private long reachTime = 0L;
    private long duration = 0L;
    public MyTimeConstrainedRelationPropertyExtractor(String prefix) {
        super(prefix);
    }
    @Override public void doRun() {
        try {
            for (int s = 0; s < MyVars.seqs.length; s++) {
                boolean itemsetFoundInSequence = false;
                int i=0;
                for (; i < MyVars.seqs[s].length; i++) {
                    String itemset = MyVars.seqs[s][i].split(":")[0];
                    if (itemset.equals(this.prefix)) {
                        itemsetFoundInSequence = true;
                        this.contribution++;
                        if ((i+1) < MyVars.seqs[s].length) {
                            String [] successorProperties = MyVars.seqs[s][i+1].split(":");
                            this.duration += Long.valueOf(successorProperties[1]);
                            if (this.itemsetMap.containsKey(successorProperties[0])) {
                                this.itemsetMap.put(successorProperties[0], this.itemsetMap.get(successorProperties[0])+1);
                            } else {this.itemsetMap.put(successorProperties[0], 1);}
                        }
                    }
                }
                if (MyVars.mxDepth < i) {MyVars.mxDepth = i;}
                if (itemsetFoundInSequence) {this.uniqueContribution++;}
            }
        }
        catch (Exception ex) {ex.printStackTrace();}
        finally {
            this.writeItemsetMapToFile();
            this.closeFileWriter();
        }
    }

    @Override protected void closeFileWriter() {
        try {
            File renameFile = new File(MyVars.outputDir +
                this.nodeFile.getName() +
                MyVars.contributionSymbol +
                this.contribution +
                MyVars.uniqueContributionSeparator +
                this.uniqueContribution +
                MyVars.timeSeparator +
                reachTime +
                MyVars.durationSeparator +
                duration
            );
            bw.close();
            if (!this.nodeFile.renameTo(renameFile)) {
                System.out.println("Rename failed for " + renameFile.getName());
            }
        } catch (Exception ex) {ex.printStackTrace();}
    }
}