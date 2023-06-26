package medousa.direct.broker;

import medousa.direct.data.MyDirectGraphHeaderLoader;

import java.io.File;

public class MyDirectGraphHeaderBroker
extends MyDirectGraphFileBroker {

    private MyDirectGraphHeaderLoader headerLoader;

    public MyDirectGraphHeaderBroker() {
        super();
        this.headerLoader = new MyDirectGraphHeaderLoader();
    }
    public String [] loadHeader(final File headerFile) {
        this.headerLoader = new MyDirectGraphHeaderLoader();
        String [] headers = this.headerLoader.load(headerFile);
        return headers;
    }
    public int getHeaderIndex(String columnName) {
        return this.headerLoader.getHeaderIndex(columnName);
    }
    public MyDirectGraphHeaderLoader getHeaderLoader() {return this.headerLoader;}
}
