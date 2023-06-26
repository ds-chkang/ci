package medousa.sequential.graph.element;

import medousa.sequential.utils.MySequentialGraphVars;

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
            for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
                boolean itemsetFoundInSequence = false;
                int i=0;
                for (; i < MySequentialGraphVars.seqs[s].length; i++) {
                    String itemset = MySequentialGraphVars.seqs[s][i].split(":")[0];
                    if (itemset.equals(this.prefix)) {
                        itemsetFoundInSequence = true;
                        this.contribution++;
                        if ((i+1) < MySequentialGraphVars.seqs[s].length) {
                            String [] successorProperties = MySequentialGraphVars.seqs[s][i+1].split(":");
                            this.duration += Long.valueOf(successorProperties[1]);
                            if (this.itemsetMap.containsKey(successorProperties[0])) {
                                this.itemsetMap.put(successorProperties[0], this.itemsetMap.get(successorProperties[0])+1);
                            } else {this.itemsetMap.put(successorProperties[0], 1);}
                        }
                    }
                }
                if (MySequentialGraphVars.mxDepth < i) {
                    MySequentialGraphVars.mxDepth = i;}
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
            if (bw != null) {
                bw.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}