package medousa.sequential.graph.funnel;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MySequenceScanManager
implements MyJobCompleteListener {

    private int availableProcessors;
    private MySuccessorScanner [] scanners;
    private Set<String> pathSet;
    private int scannerCnt = 0;
    private boolean isContribution;
    private Map<String, Long> nodeValueMap = new HashMap<>();
    private Map<String, Long> edgeValueMap = new HashMap<>();

    public MySequenceScanManager(Set<String> pathSet) {
        try {
            this.pathSet = pathSet;
            this.scanners = new MySuccessorScanner [pathSet.size()];
            this.availableProcessors = Runtime.getRuntime().availableProcessors();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public MySequenceScanManager(Set<String> pathSet, boolean isContribution) {
        try {
            this.isContribution = isContribution;
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
        if (this.isContribution) {
            for (String path : this.pathSet) {
                if (path.split("-")[0].endsWith("x")) {
                    this.scanners[this.scannerCnt] = new MyVariableContributionScanner(path);
                    this.scanners[this.scannerCnt++].addJob(this);
                } else {
                    this.scanners[this.scannerCnt] = new MyContributionScanner(path);
                    this.scanners[this.scannerCnt++].addJob(this);
                }
            }
        } else {
            for (String path : this.pathSet) {
                if (path.split("-")[0].endsWith("x")) {
                    this.scanners[this.scannerCnt] = new MyVariableSuccessorScanner(path);
                    this.scanners[this.scannerCnt++].addJob(this);
                } else {
                    this.scanners[this.scannerCnt] = new MySuccessorScanner(path);
                    this.scanners[this.scannerCnt++].addJob(this);
                }
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
        if (job instanceof MyContributionScanner) {
            MyContributionScanner contributionScanner = (MyContributionScanner) job;
            long nodeContribution = contributionScanner.getNodeContribution();
            if (this.nodeValueMap.containsKey(contributionScanner.getNodeName())) {
                this.nodeValueMap.put(contributionScanner.getNodeName(),
                this.nodeValueMap.get(contributionScanner.getNodeName()) + nodeContribution);
            } else {
                this.nodeValueMap.put(contributionScanner.getNodeName(), nodeContribution);
            }

            if (contributionScanner.getEdgeName().length() > 0) {
                long edgeContribution = contributionScanner.getEdgeContribution();
                if (this.edgeValueMap.containsKey(contributionScanner.getEdgeName())) {
                    this.edgeValueMap.put(contributionScanner.getEdgeName(),
                    this.edgeValueMap.get(contributionScanner.getEdgeName()) + edgeContribution);
                } else {
                    this.edgeValueMap.put(contributionScanner.getEdgeName(), edgeContribution);
                }
            }
        } else if (job instanceof MyVariableContributionScanner) {
            MyVariableContributionScanner variableContributionScanner = (MyVariableContributionScanner) job;
            long nodeContribution = variableContributionScanner.getNodeContribution();
            if (this.nodeValueMap.containsKey(variableContributionScanner.getNodeName())) {
                this.nodeValueMap.put(variableContributionScanner.getNodeName(),
                        this.nodeValueMap.get(variableContributionScanner.getNodeName()) + nodeContribution);
            } else {
                this.nodeValueMap.put(variableContributionScanner.getNodeName(), nodeContribution);
            }

            if (variableContributionScanner.getEdgeName().length() > 0) {
                long edgeContribution = variableContributionScanner.getEdgeContribution();
                if (this.edgeValueMap.containsKey(variableContributionScanner.getEdgeName())) {
                    this.edgeValueMap.put(variableContributionScanner.getEdgeName(),
                            this.edgeValueMap.get(variableContributionScanner.getEdgeName()) + edgeContribution);
                } else {
                    this.edgeValueMap.put(variableContributionScanner.getEdgeName(), edgeContribution);
                }
            }
        } else if (job instanceof MySuccessorScanner) {
            MySuccessorScanner successorScanner = (MySuccessorScanner) job;
            if (successorScanner.successors.size() > 0) {
                for (String succesor : successorScanner.successors.keySet()) {
                    if (this.nodeValueMap.containsKey(succesor)) {
                        this.nodeValueMap.put(succesor, this.nodeValueMap.get(succesor) + 1);
                    } else {
                        this.nodeValueMap.put(succesor, 1L);
                    }
                }
            }
        } else {
            MyVariableSuccessorScanner variableSuccessorScanner = (MyVariableSuccessorScanner) job;
            if (variableSuccessorScanner.successors.size() > 0) {
                for (String succesor : variableSuccessorScanner.successors.keySet()) {
                    if (this.nodeValueMap.containsKey(succesor)) {
                        this.nodeValueMap.put(succesor, this.nodeValueMap.get(succesor) + 1);
                    } else {
                        this.nodeValueMap.put(succesor, 1L);
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


    public Map<String, Long> getNodeValueMap() {return this.nodeValueMap;}
    public Map<String, Long> getEdgeValueMap() {return this.edgeValueMap;}

}
