package datamining.feature;

import datamining.utils.system.MyVars;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Locale;

public class MyFeatureWriter {

    protected BufferedWriter bw;
    protected BufferedWriter bw2;

    public MyFeatureWriter() {
        try {
            MyVars.inputSequenceFile = MyVars.outputDir + "../features.txt";
            File feature_file = new File(MyVars.inputSequenceFile);
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
            File feature_file = new File(MyVars.outputDir + "../" + fileName.replaceAll(" ", "").toLowerCase(Locale.ENGLISH));
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
            MyVars.sequenceFileName = MyVars.outputDir + "../" + sequenceFileName;
            MyVars.sequenceWithObjectIDFileName = MyVars.outputDir + "../" + sequencesWithObjectIDFileName;
            File sequenceFile = new File(MyVars.sequenceFileName);
            File sequenceWithObjectIDFile = new File(MyVars.sequenceWithObjectIDFileName);
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
            this.bw2.write(objectID + MyVars.hyphenDelimeter + sequence + "\n");
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
