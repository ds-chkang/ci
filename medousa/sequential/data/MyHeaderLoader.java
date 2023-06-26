package medousa.sequential.data;

import medousa.message.MyMessageUtil;
import medousa.sequential.utils.MyMathUtil;
import medousa.sequential.utils.MySequentialGraphVars;

import java.io.File;
import java.util.Scanner;

public class MyHeaderLoader {

    private File headerFile;
    private String [] headers = null;

    public MyHeaderLoader() {}

    public String [] load(File headerFile) {
        try {
            Scanner scanner = new Scanner(headerFile);
            if (scanner.hasNextLine()) {
                String headerLine = scanner.nextLine();
                if (headerLine.split(MySequentialGraphVars.commaDelimeter).length > 0) {
                    headers = headerLine.split(MySequentialGraphVars.commaDelimeter);
                    this.CapitalizeHeaders();
                    MyMessageUtil.showInfoMsg("The header file has " + MyMathUtil.getCommaSeperatedNumber(headers.length) + " columns. \n" +
                            "It has been loaded successfully.");
                } else {
                    MyMessageUtil.showErrorMsg( "Please, check the format of the header file.");
                }
                MySequentialGraphVars.numberOfInputDataColumns = headers.length;
            } else {
                MyMessageUtil.showErrorMsg( "Please, check the format of the header file.\n" +
                                            "The provided header file seems empty.");
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return headers;
    }

    public String [] load() {
        try {
            Scanner scanner = new Scanner(this.headerFile);
            if (scanner.hasNextLine()) {
                String headerLine = scanner.nextLine();
                if (headerLine.split(MySequentialGraphVars.commaDelimeter).length > 0) {
                    headers = headerLine.split(MySequentialGraphVars.commaDelimeter);
                    this.CapitalizeHeaders();
                    MyMessageUtil.showInfoMsg("The header file has " + MyMathUtil.getCommaSeperatedNumber(headers.length) + " columns. \n" +
                                              "It has been loaded successfully.");
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return headers;
    }

    private void CapitalizeHeaders() {
        for (int i=0; i < headers.length; i++) {
            headers[i] = headers[i].toUpperCase();
        }
    }

    public int getHeaderIndex(String columnHeaderName) {
        for (int i=0; i < headers.length; i++) {
            if (columnHeaderName.replaceAll(" ", "").toUpperCase().contains(headers[i].replaceAll(" ", "").toUpperCase())) {
                return i;
            }
        }
        return -1;
    }



    public String [] getHeaders() { return headers; }
}
