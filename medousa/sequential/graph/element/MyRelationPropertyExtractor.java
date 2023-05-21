package medousa.sequential.graph.element;

import medousa.sequential.utils.MySequentialGraphVars;

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
            for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
                boolean itemsetFound = false;
                int i=0;

                for (; i < MySequentialGraphVars.seqs[s].length; i++) {
                    String itemset = MySequentialGraphVars.seqs[s][i].split(":")[0];
                    if (itemset.equals(this.prefix)) {
                        itemsetFound = true;
                        this.contribution++;
                        if ((i+1) < MySequentialGraphVars.seqs[s].length) {
                            String [] successorItemSet = MySequentialGraphVars.seqs[s][i+1].split(":");
                            if (this.itemsetMap.containsKey(successorItemSet[0])) {this.itemsetMap.put(successorItemSet[0], this.itemsetMap.get(successorItemSet[0])+1);}
                            else {this.itemsetMap.put(successorItemSet[0], 1);}
                        }
                    }
                }

                if (MySequentialGraphVars.mxDepth < i) {
                    MySequentialGraphVars.mxDepth = i;
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
                this.bw.write(itemset + MySequentialGraphVars.contributionSymbol + this.itemsetMap.get(itemset) + "\n");
            }
        } catch (Exception ex) {ex.printStackTrace();}
    }

    protected BufferedWriter createFileWriter() {
        try {
            this.nodeFile = new File(MySequentialGraphVars.outputDir + this.prefix);
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
            String fileName = MySequentialGraphVars.outputDir + this.nodeFile.getName() + MySequentialGraphVars.contributionSymbol +
                              this.contribution + MySequentialGraphVars.uniqueContributionSeparator + this.uniqueContribution;
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