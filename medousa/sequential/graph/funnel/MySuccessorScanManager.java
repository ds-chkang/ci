package medousa.sequential.graph.funnel;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MySuccessorScanManager
implements MyJobCompleteListener {

    private int availableProcessors;
    private MySuccessorScanner [] scanners;
    private Set<String> pathSet;
    private int scannerCnt = 0;
    private Map<String, Long> successors = new HashMap<>();

    public MySuccessorScanManager(Set<String> pathSet) {
        try {
            this.pathSet = pathSet;
            this.scanners = new MySuccessorScanner [pathSet.size()];
            this.availableProcessors = Runtime.getRuntime().availableProcessors();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void run() {
        try {
            this.createScanners();
            this.startInitialScanners();
            this.waitForScannersToTerminate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void createScanners() {
        for (String path : this.pathSet) {
           // System.out.println(path);
            if (path.split("-")[0].endsWith("x")) {
                this.scanners[this.scannerCnt] = new MyVariableSuccessorScanner(path);
                this.scanners[this.scannerCnt++].addJob(this);
            } else {
                this.scanners[this.scannerCnt] = new MySuccessorScanner(path);
                this.scanners[this.scannerCnt++].addJob(this);
            }
        }
    }

    private void startInitialScanners() {
        synchronized (this) {
            scannerCnt = 0;
            int initialCnt = 0;
            if (this.pathSet.size() < this.availableProcessors) {
                initialCnt = this.pathSet.size();
            } else {
                initialCnt = this.availableProcessors;
            }

            while (scannerCnt < initialCnt) {
                this.scanners[scannerCnt++].start();
            }
        }
    }

    @Override public void startNextJob(Thread job) {
        if (job instanceof MySuccessorScanner) {
            MySuccessorScanner successorScanner = (MySuccessorScanner) job;
            if (successorScanner.successors.size() > 0) {
                for (String succesor : successorScanner.successors.keySet()) {
                    if (this.successors.containsKey(succesor)) {
                        this.successors.put(succesor, this.successors.get(succesor) + 1);
                    } else {
                        this.successors.put(succesor, 1L);
                    }
                }
            }
        } else {
            MyVariableSuccessorScanner variableSuccessorScanner = (MyVariableSuccessorScanner) job;
            if (variableSuccessorScanner.successors.size() > 0) {
                for (String succesor : variableSuccessorScanner.successors.keySet()) {
                    if (this.successors.containsKey(succesor)) {
                        this.successors.put(succesor, this.successors.get(succesor) + 1);
                    } else {
                        this.successors.put(succesor, 1L);
                    }
                }
            }
        }

        if (this.scannerCnt < this.scanners.length) {
            this.scanners[this.scannerCnt++].start();
        }
    }

    private void waitForScannersToTerminate() {
        try {
            for (int i = 0; i < this.scanners.length; i++) {
                if (this.scanners[i].isAlive()) {
                    this.scanners[i].join();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public Map<String, Long> getSuccessors() {return this.successors;}

}
