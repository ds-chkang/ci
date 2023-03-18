package datamining.graph.element;

import datamining.utils.system.MyVars;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

public class MyRelationPropertyExtractionManager
implements MyRelationExtractionJobCompleteListener {

    private long startTime;
    private long endTime;
    private int availableProcessors;
    private int prefixerCnt = 0;
    private MyRelationPropertyExtractor [] prefixers;

    public MyRelationPropertyExtractionManager() {
        try {
            MyVars.globalPatternCount = 0L;
            MyVars.mxDepth = 0;
            this.availableProcessors = Runtime.getRuntime().availableProcessors();
            MyVars.seqs = new String[MyVars.sequeceFeatureCount][];
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean run() {
        try {
            File outputDir = new File(MyVars.outputDir);
            if (!outputDir.exists()) {
                outputDir.mkdir();
            } else {
                String [] entries = outputDir.list();
                for(String s: entries) {
                    File currentFile = new File(outputDir.getPath(),s);
                    currentFile.delete();
                }
            }
            startTime = System.currentTimeMillis();
            startMining();
            endTime = System.currentTimeMillis();
           //printStatistics();
            return true;
        } catch (Exception ex) {ex.printStackTrace();}
        return false;
    }

    public void printStatistics() {
        StringBuilder r = new StringBuilder(200);
        r.append("=============  SEQUENTIAL PATTERN MINER - 2017 ===============\n");
        r.append(" Total time: ");
        r.append(this.getElapsedExecutionTime()*0.001);
        r.append(" secs");
        r.append("\n");
        r.append("==============================================================\n");
        System.out.println(r.toString());
    }

    public long getElapsedExecutionTime() {
        return (this.endTime - this.startTime);
    }

    private void startMining() throws Exception {
        this.loadSequences();
        HashSet<String> prefixerSet = this.createItemsets();
        this.createPrefixers(prefixerSet);
        this.startInitialPrefixers();
        this.waitForPrefixersToTerminate();
    }

    private void createPrefixers(HashSet<String> prefixerSet) {
        this.prefixers = new MyRelationPropertyExtractor[prefixerSet.size()];
        if (!MyVars.isTimeOn) {
            for (String rPrf : prefixerSet) {
                this.prefixers[this.prefixerCnt] = new MyRelationPropertyExtractor(rPrf);
                this.prefixers[this.prefixerCnt++].addListener(this);
            }
        } else {
            for (String rPrf : prefixerSet) {
                this.prefixers[this.prefixerCnt] = new MyTimeConstrainedRelationPropertyExtractor(rPrf);
                this.prefixers[this.prefixerCnt++].addListener(this);
            }
        }
    }

    private HashSet<String> createItemsets() {
        HashSet<String> prfs = new HashSet<>();
        for (int s=0; s < MyVars.seqs.length; s++) {
            for (short i = 0; i < MyVars.seqs[s].length; i++) {
                prfs.add(MyVars.seqs[s][i].split(":")[0]);
            }
        }
        return prfs;
    }

    private void loadSequences() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(MyVars.sequenceFileName));
        String l = "";
        int s = MyVars.sequeceFeatureCount-1;
        while ((l = br.readLine()) != null) {
            MyVars.seqs[s] = l.trim().split(MyVars.hyphenDelimeter);
            s--;
        }
    }

    public void startInitialPrefixers() {
        this.prefixerCnt = this.availableProcessors < this.prefixers.length ?
                this.availableProcessors + 1 :
                this.prefixers.length;
        int i=0;
        synchronized (this) {
            while (i < this.prefixerCnt) {
                this.prefixers[i++].start();
            }
        }
    }

    @Override
    public synchronized void startNextPrefixer(Thread t) {
        if (this.prefixerCnt < this.prefixers.length) {
            this.prefixers[this.prefixerCnt++].start();
        }
    }

    private void waitForPrefixersToTerminate() {
        for (int i = 0; i < this.prefixers.length; i++) {
            try {if (this.prefixers[i].isAlive()) {this.prefixers[i].join();}}
            catch (Exception ex) {ex.printStackTrace();}
        }
    }
}
