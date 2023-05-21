package medousa.sequential.graph.funnel;

import medousa.sequential.utils.MySequentialGraphVars;

import java.util.HashMap;
import java.util.Map;

public class MyVariableSuccessorScanner
extends MySuccessorScanner {

    public MyVariableSuccessorScanner(String path) {
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
                            j = (i+(this.itemsets.length-1));
                            if (j < MySequentialGraphVars.seqs[s].length) {
                                String succesor = MySequentialGraphVars.seqs[s][j].split(":")[0];
                                if (this.successors.containsKey(succesor)) {
                                    this.successors.put(succesor, this.successors.get(succesor) + 1);
                                } else {
                                    this.successors.put(succesor, 1L);
                                }
                            }
                        }
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {

        }
    }

}