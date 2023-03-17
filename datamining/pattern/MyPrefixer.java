package datamining.pattern;

import datamining.utils.system.MySysUtil;
import datamining.utils.system.MyVars;

import java.io.*;
import java.util.HashMap;

public class MyPrefixer
extends MyNotifyingPrefixer {

    private long cnt;
    private MyPrefix rootPrefix;
    private BufferedWriter bw;
    private File patternFile;
    private String outputDir = "";

    public MyPrefixer() {}
    public MyPrefixer(MyPrefix prior, String outputDir) {
        this.rootPrefix = prior;
        this.outputDir = outputDir;
        this.bw = this.createFileWriter(prior);
    }

    @Override
    public void doRun() {
        try {
            this.cnt++;
            this.createInitialPrefixes(this.rootPrefix);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            this.closeFileWriter();
        }
    }

    private void createInitialPrefixes(MyPrefix prior)
    throws IOException {
        HashMap<String, MyPrefix> initialPrefixList = new HashMap<>();
        for (Integer s : prior.keySet()) {
            MyPrefixGeo[] geos = prior.get(s);
            for (int g = 0; g < geos.length; g++) {
                short iSet = geos[g].getItemSetIdx();
                while (iSet < MySPMEngine.seqs[s].length) {
                    short i = 0;
                    do {
                        MyPrefix initialPrefix = initialPrefixList.putIfAbsent(MySPMEngine.seqs[s][iSet][i],
                            new MyPrefix('a' + MySPMEngine.seqs[s][iSet][i], s, new MyPrefixGeo(iSet, ++i)));
                        if (initialPrefix != null) {
                            initialPrefix.addGeo(s, new MyPrefixGeo(iSet, i));
                        }
                    }
                    while (i < MySPMEngine.seqs[s][iSet].length);
                    iSet++;
                }
            }
        }

        if (initialPrefixList.size() > 0) {
            for (MyPrefix nextPrefix : initialPrefixList.values()) {
                this.writePatternToFile(nextPrefix);
                this.createSeparatedPrefixes(nextPrefix);
                this.createPairedPrefixes(nextPrefix);
            }
        }
    }

    private void createSeparatedPrefixes(MyPrefix prior)
    throws IOException {
        HashMap<String, MyPrefix> separatedPrefixList = new HashMap<>();
        char length = prior.pattern.charAt(0);
        length++;

        for (Integer s : prior.keySet()) {
            MyPrefixGeo[] geos = prior.get(s);
            for (int g = 0; g < geos.length; g++) {
                short iSet = geos[g].getItemSetIdx();
                while (++iSet < MySPMEngine.seqs[s].length) {
                    short i = 0;
                    do {
                        MyPrefix separatedPrefix = separatedPrefixList.putIfAbsent(MySPMEngine.seqs[s][iSet][i],
                            new MyPrefix(length + MySPMEngine.seqs[s][iSet][i], s, new MyPrefixGeo(iSet, ++i)));
                        if (separatedPrefix != null) {
                            separatedPrefix.addGeo(s, new MyPrefixGeo(iSet, i));
                        }
                     } while (i < MySPMEngine.seqs[s][iSet].length);
                }
            }
        }

        if (separatedPrefixList.size() > 0) {
            for (MyPrefix current : separatedPrefixList.values()) {
                this.writePatternToFile(current);
                this.createSeparatedPrefixes(current);
                this.createPairedPrefixes(current);
            }
        }
    }

    private void createPairedPrefixes(MyPrefix prior)
    throws IOException {
        HashMap<String, MyPrefix> pairedPrefixList = new HashMap<>();
        for (Integer s : prior.keySet()) {
            MyPrefixGeo[] geos = prior.get(s);
            for (int g = 0; g < geos.length; g++) {
                short iSet = geos[g].getItemSetIdx();
                short i = geos[g].getItemIdx();
                while (i < MySPMEngine.seqs[s][iSet].length) {
                    MyPrefix pairedPrefix = pairedPrefixList.putIfAbsent(MySPMEngine.seqs[s][iSet][i],
                        new MyPrefix(prior.pattern + MySPMEngine.seqs[s][iSet][i], s, new MyPrefixGeo(iSet, ++i)));
                    if (pairedPrefix != null) {
                        pairedPrefix.addGeo(s, new MyPrefixGeo(iSet, i));
                    }
                }
            }
        }

        if (pairedPrefixList.size() > 0) {
            for (MyPrefix current : pairedPrefixList.values()) {
                this.writePatternToFile(current);
                this.createSeparatedPrefixes(current);
                this.createPairedPrefixes(current);
            }
        }
    }

    private void writePatternToFile(MyPrefix prefix)
    throws IOException {
        if (prefix.size() > MyVars.minSub) {
            this.bw.write(prefix.pattern + MyVars.contributionSymbol + prefix.size());
        } else {
            this.bw.write(prefix.pattern);
        }
        this.cnt++;
    }

    private BufferedWriter createFileWriter(MyPrefix prefix) {
        try {
            File dirFile = new File(this.outputDir);
            if (!dirFile.exists()) {
                dirFile.mkdir();
            }
            String fileNameSuffix = prefix.getUniqueContribution() > MyVars.minSub ?
                    (MyVars.contributionSymbol + prefix.getUniqueContribution() + "!" + prefix.totalContribution) :
                    ("!" + prefix.totalContribution);
            this.patternFile = new File(this.outputDir + MySysUtil.getDirectorySlash() + prefix.pattern + fileNameSuffix);
            if (!this.patternFile.exists()) {
                this.patternFile.createNewFile();
            } else {
                this.patternFile.delete();
                this.patternFile.createNewFile();
            }
            return new BufferedWriter(new FileWriter(this.patternFile), 1024);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private void closeFileWriter() {
        try {
            File renameFile = new File(this.outputDir + MySysUtil.getDirectorySlash() + this.patternFile.getName()+MyVars.patternCountSymbol+this.cnt);
            bw.close();
            this.patternFile.renameTo(renameFile);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public long getCount() {
        return this.cnt;
    }
}