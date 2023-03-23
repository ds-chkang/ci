package datamining.data;

import datamining.utils.system.MyVars;

import java.io.File;
import java.util.Scanner;

public class MyHeaderLoader {

    private File headerFile;
    private int headerCount = 0;
    private String [] headers = null;

    public MyHeaderLoader() {}

    public String [] load(File headerFile) {
        try {
            Scanner scanner = new Scanner(headerFile);
            while (scanner.hasNextLine()) {
                String headerLine = scanner.nextLine();
                headers = headerLine.split(MyVars.commaDelimeter);
            }
            this.CapitalizeHeaders();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return headers;
    }

    public String [] load() {
        try {
            Scanner scanner = new Scanner(this.headerFile);
            while (scanner.hasNextLine()) {
                String headerLine = scanner.nextLine();
                headers = headerLine.split(MyVars.commaDelimeter);
            }
            this.CapitalizeHeaders();
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
