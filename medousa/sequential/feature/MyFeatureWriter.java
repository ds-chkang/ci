package medousa.sequential.feature;

import medousa.sequential.utils.MySequentialGraphSysUtil;
import medousa.sequential.utils.MySequentialGraphVars;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Locale;

public class MyFeatureWriter {

    protected BufferedWriter bw;
    protected BufferedWriter bw2;

    public MyFeatureWriter() {
        try {
            MySequentialGraphVars.inputSequenceFile = MySequentialGraphVars.outputDir + ".." + MySequentialGraphSysUtil.getDirectorySlash() + "features.txt";
            File feature_file = new File(MySequentialGraphVars.inputSequenceFile);
            if (feature_file.exists()) {
                feature_file.delete();
                feature_file.createNewFile();
            } else { feature_file.createNewFile(); }
            this.bw = new BufferedWriter(new FileWriter(feature_file));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public MyFeatureWriter(String fileName) {
        try {
            File feature_file = new File(MySequentialGraphVars.outputDir + "../" + fileName.replaceAll(" ", "").toLowerCase(Locale.ENGLISH));
            if (feature_file.exists()) {
                feature_file.delete();
                feature_file.createNewFile();
            } else { feature_file.createNewFile(); }
            this.bw = new BufferedWriter(new FileWriter(feature_file));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public MyFeatureWriter(String sequencesWithObjectIDFileName, String sequenceFileName) {
        try {
            MySequentialGraphVars.sequenceFileName = MySequentialGraphVars.outputDir + ".." + MySequentialGraphSysUtil.getDirectorySlash() + sequenceFileName;
            MySequentialGraphVars.sequenceWithObjectIDFileName = MySequentialGraphVars.outputDir + ".." + MySequentialGraphSysUtil.getDirectorySlash() + sequencesWithObjectIDFileName;
            File sequenceFile = new File(MySequentialGraphVars.sequenceFileName);
            File sequenceWithObjectIDFile = new File(MySequentialGraphVars.sequenceWithObjectIDFileName);
            if (sequenceFile.exists()) {
                sequenceFile.delete();
                sequenceFile.createNewFile();
            } else { sequenceFile.createNewFile(); }
            if (sequenceWithObjectIDFile.exists()) {
                sequenceWithObjectIDFile.delete();
                sequenceWithObjectIDFile.createNewFile();
            } else { sequenceWithObjectIDFile.createNewFile(); }
            this.bw = new BufferedWriter(new FileWriter(sequenceFile));
            this.bw2 = new BufferedWriter(new FileWriter(sequenceWithObjectIDFile));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void addSequence(String objectID, String sequence) {
        try {
            this.bw.write(sequence + "\n");
            this.bw2.write(objectID + MySequentialGraphVars.hyphenDelimeter + sequence + "\n");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void addSequence(String sequence) {
        try {
            this.bw.write(sequence + "\n");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void close() {
        try {
            if (this.bw != null) {
                this.bw.close();
            }
            if (this.bw2 != null) {
                this.bw2.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
