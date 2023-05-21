package medousa.sequential.graph.funnel;

import medousa.sequential.utils.MySequentialGraphVars;

public class MyVariableContributionScanner
extends MyVariableSuccessorScanner {

    private int nodeContribution;
    private int edgeContribution;

    public MyVariableContributionScanner(String path) {
        super(path);
    }

    @Override public void doRun() {
        try {
            for (int s=0; s < MySequentialGraphVars.seqs.length; s++) {
                if (MySequentialGraphVars.seqs[s][0].split(":")[0].equals(this.itemsets[0])) {
                    for (int i = 1; i < MySequentialGraphVars.seqs[s].length; i++) {
                        boolean pathFound = true;
                        int j=i;
                        if ((j+(this.itemsets.length-1)) < MySequentialGraphVars.seqs[s].length) {
                            for (int k = 1; k < this.itemsets.length; k++) {
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