package medousa.sequential.category;

import medousa.message.MyMessageUtil;
import medousa.sequential.data.MyDataLoader;
import medousa.sequential.data.MySequentialGraphFileMerger;
import medousa.sequential.config.MySupplementaryVariableTable;
import medousa.sequential.utils.MySequentialGraphVars;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class MySequentialGraphCategorizer
extends MyDataLoader {

    private MySequentialGraphCategoryList categoryList;

    public MySequentialGraphCategorizer() { super(); }
    public MySequentialGraphCategorizer(final File [] inFiles) {
        super(inFiles);
        categoryList = new MySequentialGraphCategoryList();
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
            MySupplementaryVariableTable supplementVariableTbl = MySequentialGraphVars.app.getSequentialGraphMsgBroker().getConfigPanel().getSupplimentaryVariableTable();
            int supplemenatryBindingVariableCount = supplementVariableTbl.getRowCount();
            for (int i = 0; i < supplemenatryBindingVariableCount; i++) { // loop the number of times as the number of supplementary variables.
                if ((supplementVariableTbl.getCategoryType(i, 2)).trim().contains("REAL")) { // if supplementary variable categorized by real number.
                    String howManyCategories = supplementVariableTbl.getCategoryNumber(i, 1);
                    String categoryName = supplementVariableTbl.getVariableName(i, 0); // get supplementary variable name.
                    int headerIndex = getHeadIndex(categoryName); // get location of supplementary variable in the headerloader.
                    int numOfCategories = Integer.valueOf(howManyCategories); // get the number of categories of the current supplementary variable.
                    categoryList.createCategoriesForRealVariable(new MySequentialGraphCategory(categoryName, 0.0, 0.0, headerIndex, numOfCategories)); // add category with given information above.
                }
            }
            return true;
        } catch (Exception ex) {
            //ex.printStackTrace();
        }
        return false;
    }

    @Override
    protected boolean readInputData() {
        boolean retVal = false;
        try {
            mergedFile = MySequentialGraphFileMerger.mergeFiles(inFiles, new File(inFiles[0].getParent()+mergedFileName));
            long fileSize = mergedFile.length();        // input file size in bytes
            long bytesRead = 0;
            rawData = new ArrayList<>();
            scanner = new Scanner(mergedFile);
            while (scanner.hasNextLine()) {
                String inputDataLine = scanner.nextLine();
                String [] splitedInputDataLine = inputDataLine.split(MySequentialGraphVars.commaDelimeter);
                rawData.add(new ArrayList(Arrays.asList(splitedInputDataLine)));
                for (int i=0; i < categoryList.size(); i++) {
                    MySequentialGraphCategory category = categoryList.getCategory(i);
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
        for (MySequentialGraphCategory category : categoryList) {
            category.setCategoryIntervals();
        }
    }

    private int getHeadIndex(String categoryName) {
        return MySequentialGraphVars.app.getSequentialGraphMsgBroker().getHeaderIndex(categoryName);
    }

    /**
     * returns the categorized list.
      */
    public MySequentialGraphCategoryList getCategoryList() {
        return categoryList;
    }

    // Display categories.
    public void display() { categoryList.display(); }

}

