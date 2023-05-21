package medousa.direct.feature;

import medousa.direct.utils.MyDirectGraphVars;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Locale;

public class MyDirectGraphFeatureWriter {

    protected BufferedWriter bw;
    protected BufferedWriter bw2;

    public MyDirectGraphFeatureWriter() {
        try {
            MyDirectGraphVars.inputSequenceFile = MyDirectGraphVars.outputDir + "../features.txt";
            File feature_file = new File(MyDirectGraphVars.inputSequenceFile);
            if (feature_file.exists()) {
                feature_file.delete();
                feature_file.createNewFile();
            } else { feature_file.createNewFile(); }
            this.bw = new BufferedWriter(new FileWriter(feature_file));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public MyDirectGraphFeatureWriter(String fileName) {
        try {
            File feature_file = new File(MyDirectGraphVars.outputDir + "../" + fileName.replaceAll(" ", "").toLowerCase(Locale.ENGLISH));
            if (feature_file.exists()) {
                feature_file.delete();
                feature_file.createNewFile();
            } else { feature_file.createNewFile(); }
            this.bw = new BufferedWriter(new FileWriter(feature_file));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public MyDirectGraphFeatureWriter(String sequencesWithObjectIDFileName, String sequenceFileName) {
        try {
            MyDirectGraphVars.sequenceFileName = MyDirectGraphVars.outputDir + "../" + sequenceFileName;
            MyDirectGraphVars.sequenceWithObjectIDFileName = MyDirectGraphVars.outputDir + "../" + sequencesWithObjectIDFileName;
            File sequenceFile = new File(MyDirectGraphVars.sequenceFileName);
            File sequenceWithObjectIDFile = new File(MyDirectGraphVars.sequenceWithObjectIDFileName);
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
            this.bw2.write(objectID + MyDirectGraphVars.hyphenDelimeter + sequence + "\n");
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
