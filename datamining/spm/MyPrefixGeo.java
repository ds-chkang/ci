package datamining.spm;

/*
 * Copyright (c) 2021.
 * Author: Changhee Kang
 */

public class MyPrefixGeo {

    private short isetLoc;
    private short itmLoc;

    public MyPrefixGeo(short isetLoc, short itmLoc) {
        this.isetLoc = isetLoc;
        this.itmLoc = itmLoc;
    }

    public short getItemSetIdx() {
        return this.isetLoc;
    }
    public short getItemIdx() {
        return this.itmLoc;
    }
    public void setItemSetIdx(short isetLoc) {
        this.isetLoc = isetLoc;
    }
}
