package datamining.category;

import datamining.config.MySupplementaryVariableTable;
import datamining.data.MyDataLoader;
import datamining.data.MyFileMerger;
import datamining.utils.message.MyMessageUtil;
import datamining.utils.system.MyVars;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class MyCategorizer
extends MyDataLoader {

    private MyCategoryList categoryList;

    public MyCategorizer() { super(); }
    public MyCategorizer(final File [] inFiles) {
        super(inFiles);
        categoryList = new MyCategoryList();
    }

    public boolean categorize() {
        try {
            if (!getCategoryInfo()) {
                    MyMessageUtil.showErrorMsg("An exception occurred while gettting categorization information.");
                    return false;
                } else {
                if (!readInputData()) {
                    MyMessageUtil.showErrorMsg("An exception occurred while reading in input data!");
                    return false;
                } else {
                    createIntervals();
                    display();
                    return true;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * Fetch information to categorize from supplementary binding table.
     *
     */
    private boolean getCategoryInfo() {
        try {
            MySupplementaryVariableTable supplementVariableTbl = MyVars.app.getMsgBroker().getConfigPanel().getSupplimentaryVariableTable();
            int supplemenatryBindingVariableCount = supplementVariableTbl.getRowCount();
            for (int i = 0; i < supplemenatryBindingVariableCount; i++) { // loop the number of times as the number of supplementary variables.
                if ((supplementVariableTbl.getCategoryType(i, 2)).trim().contains("REAL")) { // if supplementary variable categorized by real number.
                    String howManyCategories = supplementVariableTbl.getCategoryNumber(i, 1);
                    String categoryName = supplementVariableTbl.getVariableName(i, 0); // get supplementary variable name.
                    int headerIndex = getHeadIndex(categoryName); // get location of supplementary variable in the headerloader.
                    int numOfCategories = Integer.valueOf(howManyCategories); // get the number of categories of the current supplementary variable.
                    categoryList.createCategoriesForRealVariable(new MyCategory(categoryName, 0.0, 0.0, headerIndex, numOfCategories)); // add category with given information above.
                }
            }
            return true;
        } catch (Exception ex) {
            //ex.printStackTrace();
        }
        return false;
    }

    private void readDirectRelationInputData() {
        try {
            mergedFile = MyFileMerger.mergeDirectRelationFiles(inFiles, new File(inFiles[0].getParent()+mergedFileName));
            long fileSize = mergedFile.length();        // input file size in bytes
            long bytesRead = 0;
            rawData = new ArrayList<>();
            scanner = new Scanner(mergedFile);
            while (scanner.hasNextLine()) {
                String inputDataLine = scanner.nextLine();
                String [] splitedInputDataLine = inputDataLine.split(MyVars.commaDelimeter);
                rawData.add(new ArrayList(Arrays.asList(splitedInputDataLine)));
                recordCount++;
                bytesRead += inputDataLine.length();
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }  finally {
            try {if (scanner != null) scanner.close();
            } catch(Exception e) {e.printStackTrace();}
        }
    }

    @Override
    protected boolean readInputData() {
        boolean retVal = false;
        try {
            mergedFile = MyFileMerger.mergeFiles(inFiles, new File(inFiles[0].getParent()+mergedFileName));
            long fileSize = mergedFile.length();        // input file size in bytes
            long bytesRead = 0;
            rawData = new ArrayList<>();
            scanner = new Scanner(mergedFile);
            while (scanner.hasNextLine()) {
                String inputDataLine = scanner.nextLine();
                String [] splitedInputDataLine = inputDataLine.split(MyVars.commaDelimeter);
                rawData.add(new ArrayList(Arrays.asList(splitedInputDataLine)));
                for (int i=0; i < categoryList.size(); i++) {
                    MyCategory category = categoryList.getCategory(i);
                    int categoryHeaderIndex = category.getHeaderIndex();
                    category.setMin(Integer.valueOf(splitedInputDataLine[categoryHeaderIndex]));
                    category.setMax(Integer.valueOf(splitedInputDataLine[categoryHeaderIndex]));
                }
                recordCount++;
                bytesRead += inputDataLine.length();
            }
            retVal = true;
        } catch(Exception ex) {
            ex.printStackTrace();
        }  finally {
            try {if (scanner != null) scanner.close();
            } catch(Exception e) {e.printStackTrace();}
        }
        return retVal;
    }

    // Create categorization intervals.
    private void createIntervals() {
        for (MyCategory category : categoryList) {category.setCategoryIntervals();}
    }

    private int getHeadIndex(String categoryName) {
        return MyVars.app.getMsgBroker().getHeaderIndex(categoryName);
    }

    /**
     * returns the categorized list.
      */
    public MyCategoryList getCategoryList() {
        return categoryList;
    }

    // Display categories.
    public void display() { categoryList.display(); }

}

