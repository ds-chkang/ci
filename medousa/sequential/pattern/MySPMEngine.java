package medousa.sequential.pattern;

/**
 *
 * Sequential Pattern Mining - PrefixSpan
 *
 * Author       :   Changhee Kang
 *
 * Copyright    :   Do not redistribute or use it for commercial purposes. It is only allowed for educational purposes.
 *                  If you are a student or teacher, you can use it for your academic purposes. If you want to use it for any commercial purposes,
 *                  please inform me with the purposes of uses. If it is a commercial purpose, there is charge for commercial uses.
 *
 * Date written: 07.08.2013
 *
 * Email: ds.chkang@gmail.com
 *
 */


import medousa.MyProgressBar;
import medousa.sequential.utils.MyMathUtil;
import medousa.sequential.utils.MySequentialGraphSysUtil;
import medousa.sequential.utils.MySequentialGraphVars;

import java.util.*;
import java.io.*;

public class MySPMEngine
implements MyPrefixerCompleteListener {

    /**
     * maximum gap allowed.
     */
    private int maxGap = 0;

    /** for statistics **/
    private long startTime;
    private long endTime;
    private String miningTime;
    private MyProgressBar pb;
    private HashMap<String, MyPrefix> prefixMap;
    private long workDone = 0L;
    private int CONT_THRESHOLD = 1;
    private int availableProcessors;
    private int rootPrefixerCnt = 0;
    protected static String [][][] seqs;
    private MyNotifyingPrefixer [] prefixes;
    protected String outputDir = "";
    protected static Map<Character, Character> intToCharMap = new HashMap<>();
    protected static Map<Character, Character> charToIntMap = new HashMap<>();
    protected static Map<Character, Integer> itemsetPosMap = new HashMap<>();

    public MySPMEngine() {
        try {
            this.pb = new MyProgressBar(false);
            outputDir = MySequentialGraphSysUtil.getWorkingDir() + MySequentialGraphSysUtil.getDirectorySlash() + "patterns";
            this.pb.setAlwaysOnTop(false);
            MySequentialGraphVars.globalPatternCount = 0L;
            this.availableProcessors = Runtime.getRuntime().availableProcessors();
            int rows = 0;
            BufferedReader br = new BufferedReader(new FileReader(MySequentialGraphVars.sequenceFileName));
            String line = "";
            while ((line = br.readLine()) != null) {
                rows++;
            }
            this.seqs = new String[rows][][];

            char upperCaseAlphabet = 'A';
            for (char i='1'; i <= '9'; i++) {
                intToCharMap.put(i, upperCaseAlphabet++);
            }

            char digit = '1';
            for (char i='A'; i <= 'I'; i++) {
                charToIntMap.put(i, digit++);
            }

            char patternLengthlowCaseAlphabet = 'a';
            for (int i = 1; i < MySequentialGraphVars.mxDepth; i++) {
                itemsetPosMap.put(patternLengthlowCaseAlphabet++, i);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public MySPMEngine(ArrayList<String> data) {
        try {
            this.pb = new MyProgressBar(false);
            this.outputDir = MySequentialGraphSysUtil.getWorkingDir() + MySequentialGraphSysUtil.getDirectorySlash() + "patterns";
            this.pb.setAlwaysOnTop(false);
            MySequentialGraphVars.globalPatternCount = 0L;
            this.availableProcessors = Runtime.getRuntime().availableProcessors();
            this.seqs = new String[data.size()][][];

            char upperCaseAlphabet = 'A';
            for (char i='1'; i <= '9'; i++) {
                intToCharMap.put(i, upperCaseAlphabet++);
            }

            char digit = '1';
            for (char i='A'; i <= 'I'; i++) {
                charToIntMap.put(i, digit++);
            }

            char patternLengthlowCaseAlphabet = 'a';
            for (int i = 1; i < MySequentialGraphVars.mxDepth; i++) {
                itemsetPosMap.put(patternLengthlowCaseAlphabet++, i);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void getUpperCase(char c) {

    }

    /**
     * runs the engine.
     * @return
     */
    public boolean run() {
        try {
            File outputDir = new File(this.outputDir);
            if (!outputDir.exists()) {
                outputDir.mkdir();
            } else {
                String[]entries = outputDir.list();
                for(String s: entries) {
                    File currentFile = new File(outputDir.getPath(),s);
                    currentFile.delete();
                }
            }
            //System.out.println("\nStarted at: " + new Date());
            startTime = System.currentTimeMillis();
            startMining();  // begins medousa.sequential pattern mining.
            endTime = System.currentTimeMillis();
            miningTime = MyMathUtil.twoDecimalFormat((endTime-startTime)*0.001);
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
        r.append('\n');
        r.append(" NO. OF TOTAL PATTERNS : ");
        r.append(MyMathUtil.getCommaSeperatedNumber(MySequentialGraphVars.globalPatternCount));
        r.append('\n');
        //r.append(" Max memory (mb) : ");
        //r.append(MemoryLogger.getInstance().getMaxMemory());
        r.append("\n");
        r.append("==============================================================\n");
        System.out.println(r.toString());
    }

    public long getElapsedExecutionTime() {
        return (this.endTime - this.startTime);
    }

    private HashMap<String, MyPrefix> dropPrefix(HashMap<String, MyPrefix> prefixes) {
        final HashMap<String, MyPrefix> healthyPrefixList = new HashMap<>();
        for (Map.Entry<String, MyPrefix> entry : prefixes.entrySet()) {
            if (entry.getValue().size() >= this.CONT_THRESHOLD) {
                healthyPrefixList.put(entry.getKey(), entry.getValue());
            }
        }
        return healthyPrefixList;
    }

    private void startMining() throws Exception {
        this.buildDatabase();
        HashMap<String, MyPrefix> singlePrefixList = this.createSingleRootPrefix();
        HashMap<String, MyPrefix> prefixList = new HashMap<>();
        prefixList.putAll(singlePrefixList);
        for (MyPrefix prefix : singlePrefixList.values()) {this.createPairedRootPrefix(prefix, prefixList);}
        for (MyPrefix prefix : prefixList.values()) {this.increaseItemSetPosition(prefix);}
        this.createPrefixers(prefixList);
        this.startInitialPrefixers();
        this.waitForPrefixersToTerminate();
        this.pb.dispose();
    }

    private void createPrefixers(HashMap<String, MyPrefix> prefixList)
    throws IOException {
        this.prefixes = new MyPrefixer[prefixList.size()];
        for (MyPrefix rPrf : prefixList.values()) {
            this.prefixes[this.rootPrefixerCnt] = new MyPrefixer(rPrf, this.outputDir);
            this.prefixes[this.rootPrefixerCnt++].addListener(this);
        }
    }

    private void startInitialPrefixers() {
        this.rootPrefixerCnt = this.availableProcessors < this.prefixes.length ?
                               this.availableProcessors + 1 :
                               this.prefixes.length;
        int i=0;
        synchronized (this) {
            while (i < this.rootPrefixerCnt) {
                this.prefixes[i++].start();
            }
        }
    }

    @Override
    public synchronized void startNextPrefixer(Thread t) {
        if (this.rootPrefixerCnt < this.prefixes.length) {
            this.prefixes[this.rootPrefixerCnt++].start();
        }
        MySequentialGraphVars.globalPatternCount +=((MyPrefixer)t).getCount();
        this.pb.updateValue(++this.workDone, this.rootPrefixerCnt);
    }

    private void waitForPrefixersToTerminate() {
        for (int i = 0; i < this.prefixes.length; i++) {
            try {if (this.prefixes[i].isAlive()) {this.prefixes[i].join();}}
            catch (Exception ex) {ex.printStackTrace();}
        }
    }

    private void buildDatabase()
    throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(MySequentialGraphVars.sequenceFileName));
        String line = "";
        int s = seqs.length-1;
        if (MySequentialGraphVars.isTimeOn) {
            while ((line = br.readLine()) != null) {
                String[] iSets = line.trim().split(MySequentialGraphVars.hyphenDelimeter);
                seqs[s] = new String[iSets.length][];
                for (int iSet = 0; iSet < seqs[s].length; iSet++) {
                    seqs[s][iSet] = iSets[iSet].split(MySequentialGraphVars.commaDelimeter);
                    seqs[s][iSet][seqs[s][iSet].length-1] = seqs[s][iSet][seqs[s][iSet].length-1].split(":")[0];
                    for (int i=0; i < seqs[s][iSet].length; i++) { // convert first number letter to uppercase alphabet.
                        if (!seqs[s][iSet][i].contains("x")) {
                            seqs[s][iSet][i] = intToCharMap.get(seqs[s][iSet][i].charAt(0)) + seqs[s][iSet][i].substring(1);
                        }
                    }
                }
                if (iSets.length > MySequentialGraphVars.mxDepth) {
                    MySequentialGraphVars.mxDepth = iSets.length;
                }
                s--;
            }
        } else {
            while ((line = br.readLine()) != null) {
                String[] iSets = line.trim().split(MySequentialGraphVars.hyphenDelimeter);
                seqs[s] = new String[iSets.length][];
                for (int iSet = 0; iSet < seqs[s].length; iSet++) {
                    seqs[s][iSet] = iSets[iSet].split(MySequentialGraphVars.commaDelimeter);
                    for (int i=0; i < seqs[s][iSet].length; i++) {  // convert first number letter to uppercase alphabet.
                        if (!seqs[s][iSet][i].contains("x")) {
                            seqs[s][iSet][i] = intToCharMap.get(seqs[s][iSet][i].charAt(0)) + seqs[s][iSet][i].substring(1);
                        }
                    }
                }
                if (iSets.length > MySequentialGraphVars.mxDepth) {
                    MySequentialGraphVars.mxDepth = iSets.length;
                }
                s--;
            }
        }
    }

    private HashMap<String, MyPrefix> createSingleRootPrefix() {
        HashMap<String, MyPrefix> prfs = new HashMap<String, MyPrefix>();
        for (int s=0; s < this.seqs.length; s++) {
            int iSets = this.seqs[s].length;
            for (short iSet = 0; iSet < iSets; iSet++) {
                int iSetL = this.seqs[s][iSet].length;
                for (short i=0; i < iSetL;) {
                    MyPrefix prefix = prfs.putIfAbsent(this.seqs[s][iSet][i], new MyPrefix(this.seqs[s][iSet][i], s, new MyPrefixGeo(iSet, ++i)));
                    if (prefix != null) {prefix.addGeo(s, new MyPrefixGeo(iSet, i));}
                }
            }
        }
        return prfs;
    }

    private void createPairedRootPrefix(MyPrefix prior, HashMap<String, MyPrefix> prefixList) {
        HashMap<String, MyPrefix> nextPrefixList = new HashMap<String, MyPrefix>();
        for (Integer s : prior.keySet()) {
            MyPrefixGeo[] geos = prior.get(s);
            for (int g = 0; g < geos.length; g++) {
                short iSet = geos[g].getItemSetIdx();
                short i = geos[g].getItemIdx();
                while (i < MySPMEngine.seqs[s][iSet].length) {
                    MyPrefix prefix = nextPrefixList.putIfAbsent(MySPMEngine.seqs[s][iSet][i],
                        new MyPrefix(prior.pattern + MySPMEngine.seqs[s][iSet][i], s, new MyPrefixGeo(iSet, ++i)));
                    if (prefix != null) {prefix.addGeo(s, new MyPrefixGeo(iSet, i));}
                }
            }
        }

        if (nextPrefixList.size() > 0) {
            for (MyPrefix next : nextPrefixList.values()) {
                prefixList.put(next.pattern, next);
                createPairedRootPrefix(next, prefixList);
            }
        }
    }

    private void increaseItemSetPosition(MyPrefix prefix) {
        for (Integer s : prefix.keySet()) {
            MyPrefixGeo[] geos = prefix.get(s);
            for (int g = 0; g < geos.length; g++) {
                geos[g].setItemSetIdx((short)(geos[g].getItemSetIdx()+1));
            }
        }
    }

    public static void main(String [] args) {
        ArrayList<String> data = new ArrayList<String>();
        data.add("1-1,2,3-1,3-4-3,6");
        MySPMEngine engine = new MySPMEngine(data);
        engine.run();
        System.exit(0);
    }
}
