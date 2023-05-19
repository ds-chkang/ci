package medousa.sequential.graph.funnel;

import medousa.sequential.utils.MySequentialGraphVars;

import java.util.HashMap;
import java.util.Map;

public class MySuccessorScanner
extends MyJobDoneNotifier {
    protected String [] itemsets;
    protected Map<String, Long> successors = new HashMap<>();

    public MySuccessorScanner(String path) {
        this.itemsets = path.split("-");
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
                        j = (i+this.itemsets.length);
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

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {

        }
    }

}