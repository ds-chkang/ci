package medousa.direct.category;

import medousa.direct.data.MyDirectGraphDirectGraphDataLoader;
import medousa.direct.data.MyDirectGraphFileMerger;
import medousa.message.MyMessageUtil;
import medousa.direct.utils.MyDirectGraphVars;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class MyDirectGraphCategorizer
extends MyDirectGraphDirectGraphDataLoader {

    private MyDirectGraphCategoryList categoryList;

    public MyDirectGraphCategorizer() { super(); }
    public MyDirectGraphCategorizer(final File [] inFiles) {
        super(inFiles);
        categoryList = new MyDirectGraphCategoryList();
    }

    public boolean categorize() {
        try {
            if (MyDirectGraphVars.isDirectMarkovRelation) {
                readDirectRelationInputData();
            } else {
                if (!getCategoryInfo()) {
                    MyMessageUtil.showErrorMsg("Set the number of categories!");
                    return false;
                } else {
                    if (!readInputData()) {
                        MyMessageUtil.showErrorMsg("Failed to read in input data!");
                        return false;
                    } else {
                        createIntervals();
                        display();
                        return true;
                    }
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
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private void readDirectRelationInputData() {
        try {
            mergedFile = MyDirectGraphFileMerger.mergeDirectRelationFiles(inFiles, new File(inFiles[0].getParent()+mergedFileName));
            long fileSize = mergedFile.length();        // input file size in bytes
            long bytesRead = 0;
            rawData = new ArrayList<>();
            scanner = new Scanner(mergedFile);
            while (scanner.hasNextLine()) {
                String inputDataLine = scanner.nextLine();
                String [] splitedInputDataLine = inputDataLine.split(MyDirectGraphVars.commaDelimeter);
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
            mergedFile = MyDirectGraphFileMerger.mergeFiles(inFiles, new File(inFiles[0].getParent()+mergedFileName));
            long fileSize = mergedFile.length();        // input file size in bytes
            long bytesRead = 0;
            rawData = new ArrayList<>();
            scanner = new Scanner(mergedFile);
            while (scanner.hasNextLine()) {
                String inputDataLine = scanner.nextLine();
                String [] splitedInputDataLine = inputDataLine.split(MyDirectGraphVars.commaDelimeter);
                rawData.add(new ArrayList(Arrays.asList(splitedInputDataLine)));
                for (int i=0; i < categoryList.size(); i++) {
                    MyDirectGraphCategory category = categoryList.getCategory(i);
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
        for (MyDirectGraphCategory category : categoryList) {
            category.setCategoryIntervals();
        }
    }

    private int getHeadIndex(String categoryName) {
        return MyDirectGraphVars.app.getDirectGraphMsgBroker().getHeaderIndex(categoryName);
    }

    /**
     * returns the categorized list.
      */
    public MyDirectGraphCategoryList getCategoryList() {
        return categoryList;
    }

    // Display categories.
    public void display() { categoryList.display(); }

}

