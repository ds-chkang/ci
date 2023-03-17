package datamining.feature;

public class MyWeightFeatureWriter
extends MyFeatureWriter {

    public MyWeightFeatureWriter() {
        super();
    }

    public MyWeightFeatureWriter(String fileName) {
        super(fileName);
    }

    public MyWeightFeatureWriter(String sequencesWithObjectIDFileName, String sequenceFileName) {
        super(sequencesWithObjectIDFileName, sequenceFileName);
    }

    public void addSequence(String objectID, String sequence) {
        try {
            super.addSequence(objectID, sequence);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void addSequence(String sequence) {
        try {
            super.addSequence(sequence);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void close() {
        try {
            super.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
