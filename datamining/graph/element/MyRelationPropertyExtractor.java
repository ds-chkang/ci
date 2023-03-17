package datamining.graph.element;

import datamining.utils.system.MyVars;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;

public class MyRelationPropertyExtractor
extends MyRealtionPropertyExtractionJobDoneNotifier {
    protected String prefix;
    protected BufferedWriter bw;
    protected File nodeFile;
    protected long contribution = 0L;
    protected long uniqueContribution = 0L;
    protected HashMap<String, Integer> itemsetMap = new HashMap<>();

    public MyRelationPropertyExtractor(String prefix) {
        this.prefix = prefix;
        this.createFileWriter();
    }

    @Override public void doRun() {
        try {
            for (int s = 0; s < MyVars.seqs.length; s++) {
                boolean itemsetFound = false;
                int i=0;

                for (; i < MyVars.seqs[s].length; i++) {
                    String itemset = MyVars.seqs[s][i].split(":")[0];
                    if (itemset.equals(this.prefix)) {
                        itemsetFound = true;
                        this.contribution++;
                        if ((i+1) < MyVars.seqs[s].length) {
                            String [] successorItemSet = MyVars.seqs[s][i+1].split(":");
                            if (this.itemsetMap.containsKey(successorItemSet[0])) {this.itemsetMap.put(successorItemSet[0], this.itemsetMap.get(successorItemSet[0])+1);}
                            else {this.itemsetMap.put(successorItemSet[0], 1);}
                        }
                    }
                }

                if (MyVars.mxDepth < i) {
                    MyVars.mxDepth = i;
                }

                if (itemsetFound) {
                    this.uniqueContribution++;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            this.writeItemsetMapToFile();
            this.closeFileWriter();
        }
    }

    protected void writeItemsetMapToFile() {
        try {
            for (String itemset : this.itemsetMap.keySet()) {
                this.bw.write(itemset + MyVars.contributionSymbol + this.itemsetMap.get(itemset) + "\n");
            }
        } catch (Exception ex) {ex.printStackTrace();}
    }

    protected BufferedWriter createFileWriter() {
        try {
            this.nodeFile = new File(MyVars.outputDir + this.prefix);
            if (!this.nodeFile.exists()) {
                this.nodeFile.createNewFile();
            } else {
                this.nodeFile.delete();
                this.nodeFile.createNewFile();
            }
            this.bw = new BufferedWriter(new FileWriter(this.nodeFile), 1024);
        } catch (Exception ex) {ex.printStackTrace();}
        return null;
    }

    protected void closeFileWriter() {
        try {
            String fileName = MyVars.outputDir + this.nodeFile.getName() + MyVars.contributionSymbol +
                              this.contribution + MyVars.uniqueContributionSeparator + this.uniqueContribution;
            File renameFile = new File(fileName);
            bw.close();
            if (!this.nodeFile.renameTo(renameFile)) {
                System.out.println("Rename failed for " + this.nodeFile.getName() + "   ----   " + renameFile.getName());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}