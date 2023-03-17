/**
 *
 * Data: 26/09/2017
 * Author: Changhee Kang
 * Class name: Prefix.java
 *
 * Original gong network generator does not have headFrequency.
 * Original gone network focuses on generating sequential patterns only without considering frequencies of previous patterns.
 *
 *
 */

package datamining.pattern;

import java.util.HashMap;

public class MyPrefix
extends HashMap<Integer, MyPrefixGeo[]> {

    public String pattern;
    public int totalContribution;

    public MyPrefix(String pattern, int seqIdx, MyPrefixGeo geo)
    {
        this.pattern = pattern;
        this.putIfAbsent(seqIdx, new MyPrefixGeo[]{geo});
        this.totalContribution++;
    }

    public void addGeo(int seqIdx, MyPrefixGeo geo)
    {
        this.totalContribution++;
        if (this.putIfAbsent(seqIdx, new MyPrefixGeo[]{geo}) != null)
        {
            MyPrefixGeo [] prefixGeos = this.get(seqIdx);
            MyPrefixGeo [] newGeos = new MyPrefixGeo[prefixGeos.length+1];
            System.arraycopy(prefixGeos, 0, newGeos, 0, prefixGeos.length);
            newGeos[prefixGeos.length] = geo;
            this.put(seqIdx, newGeos);
        }
    }

    public int getUniqueContribution() {
        return this.size();
    }

}
