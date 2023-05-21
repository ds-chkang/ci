package medousa.sequential.graph.funnel;

import medousa.sequential.utils.MySequentialGraphVars;

public class MyContributionScanner
extends MySuccessorScanner {

    private int nodeContribution;
    private int edgeContribution;

    public MyContributionScanner(String path) {
        super(path);
    }

    @Override public void doRun() {
        try {
            for (int s=0; s < MySequentialGraphVars.seqs.length; s++) {
                for (int i = 0; i < MySequentialGraphVars.seqs[s].length; i++) {
                    boolean pathFound = true;
                    int j=i;
                    if ((j+this.itemsets.length) < MySequentialGraphVars.seqs[s].length) {
                        for (int k = 0; k < this.itemsets.length; k++) {
                            String itemset = MySequentialGraphVars.seqs[s][j++].split(":")[0];
                            if (!itemsets[k].equals(itemset)) {
                                pathFound = false;
                                break;
                            }
                        }
                    } else pathFound = false;

                    if (pathFound) {
                        nodeContribution++;
                        edgeContribution++;
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getNodeName() {return this.itemsets[this.itemsets.length-1];}
    public String getEdgeName() {
        if (this.itemsets.length == 1) {
            return "";
        } else {
            String edgeName = this.itemsets[this.itemsets.length-2] + "-" + this.itemsets[this.itemsets.length-1];
            System.out.println(edgeName);
            return edgeName;
        }
    }
    public long getNodeContribution() {return this.nodeContribution;}
    public long getEdgeContribution() {return this.edgeContribution;}

}