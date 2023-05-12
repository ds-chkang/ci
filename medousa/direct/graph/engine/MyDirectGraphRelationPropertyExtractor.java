package medousa.direct.graph.engine;

import medousa.direct.utils.MyDirectGraphVars;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;

public class MyDirectGraphRelationPropertyExtractor
extends MyDirectGraphRelationPropertyExtractionJobDoneNotifier {
    protected String prefix;
    protected BufferedWriter bw;
    protected File nodeFile;
    protected long contribution = 0L;
    protected long uniqueContribution = 0L;
    protected HashMap<String, Integer> itemsetMap = new HashMap<>();

    public MyDirectGraphRelationPropertyExtractor(String prefix) {
        this.prefix = prefix;
        this.createFileWriter();
    }

    @Override public void doRun() {
        try {
            for (int s = 0; s < MyDirectGraphVars.seqs.length; s++) {
                boolean itemsetFound = false;
                int i=0;
                for (; i < MyDirectGraphVars.seqs[s].length; i++) {
                    String itemset = MyDirectGraphVars.seqs[s][i].split(":")[0];
                    if (itemset.equals(this.prefix)) {
                        itemsetFound = true;
                        this.contribution++;
                        if ((i+1) < MyDirectGraphVars.seqs[s].length) {
                            String [] successorItemSet = MyDirectGraphVars.seqs[s][i+1].split(":");
                            if (this.itemsetMap.containsKey(successorItemSet[0])) {this.itemsetMap.put(successorItemSet[0], this.itemsetMap.get(successorItemSet[0])+1);}
                            else {this.itemsetMap.put(successorItemSet[0], 1);}
                        }
                    }
                }
                if (MyDirectGraphVars.mxDepth < i) {
                    MyDirectGraphVars.mxDepth = i;}
                if (itemsetFound) {this.uniqueContribution++;}
            }
        } catch (Exception ex) {ex.printStackTrace();}
        finally {
            this.writeItemsetMapToFile();
            this.closeFileWriter();
        }
    }

    protected void writeItemsetMapToFile() {
        try {
            for (String itemset : this.itemsetMap.keySet()) {
                this.bw.write(itemset + MyDirectGraphVars.contributionSymbol + this.itemsetMap.get(itemset) + "\n");
            }
        } catch (Exception ex) {ex.printStackTrace();}
    }

    protected BufferedWriter createFileWriter() {
        try {
            File dirFile = new File(MyDirectGraphVars.outputDir);
            if (!dirFile.exists()) {dirFile.mkdir();}
            this.nodeFile = new File(MyDirectGraphVars.outputDir + this.prefix);
            if (!this.nodeFile.exists()) {this.nodeFile.createNewFile();
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
            File renameFile = new File(MyDirectGraphVars.outputDir +
                this.nodeFile.getName() +
                MyDirectGraphVars.contributionSymbol +
                this.contribution +
                MyDirectGraphVars.uniqueContributionSeparator +
                this.uniqueContribution);
            bw.close();
            if (!this.nodeFile.renameTo(renameFile)) {System.out.println("Rename failed for " + renameFile.getName());}
        } catch (Exception ex) {ex.printStackTrace();}
    }
}