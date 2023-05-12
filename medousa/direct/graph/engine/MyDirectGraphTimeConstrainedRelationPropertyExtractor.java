package medousa.direct.graph.engine;

import medousa.direct.utils.MyDirectGraphVars;
import java.io.File;

public class MyDirectGraphTimeConstrainedRelationPropertyExtractor
extends MyDirectGraphRelationPropertyExtractor {
    private long reachTime = 0L;
    private long duration = 0L;
    public MyDirectGraphTimeConstrainedRelationPropertyExtractor(String prefix) {
        super(prefix);
    }
    @Override public void doRun() {
        try {
            for (int s = 0; s < MyDirectGraphVars.seqs.length; s++) {
                boolean itemsetFoundInSequence = false;
                int i=0;
                for (; i < MyDirectGraphVars.seqs[s].length; i++) {
                    String itemset = MyDirectGraphVars.seqs[s][i].split(":")[0];
                    if (itemset.equals(this.prefix)) {
                        itemsetFoundInSequence = true;
                        this.contribution++;
                        if ((i+1) < MyDirectGraphVars.seqs[s].length) {
                            String [] successorProperties = MyDirectGraphVars.seqs[s][i+1].split(":");
                            this.duration += Long.valueOf(successorProperties[1]);
                            if (this.itemsetMap.containsKey(successorProperties[0])) {
                                this.itemsetMap.put(successorProperties[0], this.itemsetMap.get(successorProperties[0])+1);
                            } else {this.itemsetMap.put(successorProperties[0], 1);}
                        }
                    }
                }
                if (MyDirectGraphVars.mxDepth < i) {
                    MyDirectGraphVars.mxDepth = i;}
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
            File renameFile = new File(MyDirectGraphVars.outputDir +
                this.nodeFile.getName() +
                MyDirectGraphVars.contributionSymbol +
                this.contribution +
                MyDirectGraphVars.uniqueContributionSeparator +
                this.uniqueContribution +
                MyDirectGraphVars.timeSeparator +
                reachTime +
                MyDirectGraphVars.durationSeparator +
                duration
            );
            bw.close();
            if (!this.nodeFile.renameTo(renameFile)) {
                System.out.println("Rename failed for " + renameFile.getName());
            }
        } catch (Exception ex) {ex.printStackTrace();}
    }
}