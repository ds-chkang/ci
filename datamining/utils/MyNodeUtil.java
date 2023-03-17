package datamining.utils;

import datamining.main.MyProgressBar;
import datamining.graph.MyNode;
import datamining.utils.system.MySysUtil;
import datamining.utils.system.MyVars;

import java.util.*;

public class MyNodeUtil {

    public static void setUserDefinedNodeValuesToNodes() {
        float maxVal = 0.00f;
        ArrayList<MyNode> nodes = new ArrayList<>(MyVars.g.getVertices());
        for (MyNode n : nodes) {
            synchronized (n) {
                n.setCurrentValue(n.nodeNumericValueMap.get(MyVars.getViewer().vc.nodeValueSelecter.getSelectedItem().toString()));
            }
            if (n.getCurrentValue() > maxVal) {maxVal = n.getCurrentValue();}
        }
        MyVars.g.MX_N_VAL = maxVal;
    }

    public static void setDefaultValuesToNodes() {
        MyVars.getViewer().nodeValueName = "CONTRIBUTION";
        float maxVal = 0.00f;
        Collection<MyNode> nodes = MyVars.g.getVertices();
        for (MyNode n : nodes) {
            synchronized (n) {
                n.setCurrentValue((float) n.getContribution());
            }
            if (maxVal < n.getCurrentValue()) {
                maxVal = n.getCurrentValue();
            }
        }
        synchronized (MyVars.g.MX_N_VAL) {
            MyVars.g.MX_N_VAL = Float.valueOf(maxVal);
        }
    }

    public static void setInOutNodeDifferenceToNodes() {
        float maxVal = 0.00f;
        Collection<MyNode> nodes = MyVars.g.getVertices();
        for (MyNode n : nodes) {
            n.setCurrentValue(Math.abs(n.getPredecessorCount() - n.getSuccessorCount()));
            if (maxVal < n.getCurrentValue()) {maxVal = n.getCurrentValue();}
        }
        synchronized (MyVars.g.MX_N_VAL) {
            MyVars.g.MX_N_VAL = Float.valueOf(maxVal);
        }
    }

    public static void setUniqueInNodeCountToNodes() {
        float maxVal = 0L;
        Collection<MyNode> nodes = MyVars.g.getVertices();
        for (MyNode n : nodes) {
            n.setCurrentValue(n.getPredecessorCount());
            if (maxVal < n.getCurrentValue()) {maxVal = n.getCurrentValue();}
        }
        synchronized (MyVars.g.MX_N_VAL) {
            MyVars.g.MX_N_VAL = Float.valueOf(maxVal);
        }
    }

    public static void setDurationToNodes() {
        float maxVal = 0.00f;
        Collection<MyNode> nodes = MyVars.g.getVertices();
        for (MyNode n : nodes) {
            float totalReachTime = 0f;
            for (Integer depth : n.getNodeDepthInfoMap().keySet()) {totalReachTime += (float)n.getNodeDepthInfoMap().get(depth).getReachTime();}
            synchronized (n) {
                n.setCurrentValue(totalReachTime);
            }
            if (maxVal < n.getCurrentValue()) {maxVal = n.getCurrentValue();}
        }
        synchronized (MyVars.g.MX_N_VAL) {
            MyVars.g.MX_N_VAL = Float.valueOf(maxVal);
        }
    }

    public static void setEigenVectorCentralityToNodes() {
        float maxVal = 0.00f;
        ArrayList<MyNode> nodes = new ArrayList<>(MyVars.g.getVertices());
        for (MyNode n : nodes) {
            synchronized (n) {
                n.setCurrentValue((float) n.getEignevector());
            }
            if (n.getCurrentValue() > maxVal) {maxVal = n.getCurrentValue();}
        }
        MyVars.g.MX_N_VAL = maxVal;
    }

    public static void setBetweenessCentralityToNodes() {
        float maxVal = 0.00f;
        ArrayList<MyNode> nodes = new ArrayList<>(MyVars.g.getVertices());
        for (MyNode n : nodes) {
            synchronized (n) {
                n.setCurrentValue((float) n.getBetweeness());
            }
            if (n.getCurrentValue() > maxVal) {maxVal = n.getCurrentValue();}
        }
        MyVars.g.MX_N_VAL = maxVal;
    }

    public static void setClosenessCentralityToNodes() {
        float maxVal = 0.00f;
        ArrayList<MyNode> nodes = new ArrayList<>(MyVars.g.getVertices());
        for (MyNode n : nodes) {
            synchronized (n) {
                n.setCurrentValue((float) n.getCloseness());
            }
            if (n.getCurrentValue() > maxVal) {maxVal = n.getCurrentValue();}
        }
        MyVars.g.MX_N_VAL = maxVal;
    }

    public static void setRecurrenceCountToNodes() {
        Collection<MyNode> nodes = MyVars.g.getVertices();
        float maxVal = 0.00f;
        for (MyNode n : nodes) {
            synchronized (n) {
                n.setCurrentValue(n.getDirectRecurrenceCount());
            }
            if (maxVal < n.getCurrentValue()) {maxVal = n.getCurrentValue();}
        }
        synchronized (MyVars.g.MX_N_VAL) {
            MyVars.g.MX_N_VAL = Float.valueOf(maxVal);
        }
    }

    public static void setAverageReachTimeToNodes() {
        float maxVal = 0.00f;
        Collection<MyNode> ns = MyVars.g.getVertices();
        for (MyNode n : ns) {
            float totalReachTime = 0.00f;
            for (Integer depth : n.getNodeDepthInfoMap().keySet()) {
                totalReachTime += (float)n.getNodeDepthInfoMap().get(depth).getReachTime();
            }
            synchronized (n) {
                n.setCurrentValue(totalReachTime/n.getNodeDepthInfoMap().size());
            }
            if (maxVal < n.getCurrentValue()) {maxVal = n.getCurrentValue();}
        }
        synchronized (MyVars.g.MX_N_VAL) {
            MyVars.g.MX_N_VAL = Float.valueOf(maxVal);
        }
    }
    public static void setEndNodeCountToNodes() {
        MyProgressBar pb = new MyProgressBar(false);
        int pbCnt = 0;
        Collection<MyNode> nodes = MyVars.g.getVertices();
        float maxVal = 0.00f;
        for (MyNode n : nodes) {
            synchronized (n) {
                n.setCurrentValue(n.getEndNodeCount());
            }
            if (maxVal < n.getCurrentValue()) {maxVal = n.getCurrentValue();}
            pb.updateValue(++pbCnt, nodes.size());
        }
        synchronized (MyVars.g.MX_N_VAL) {
            MyVars.g.MX_N_VAL = Float.valueOf(maxVal);
        }
        pb.updateValue(100, 100);
        pb.dispose();
    }

    public static void setUniqueContributionToNodes() {
        float maxVal = 0.00f;
        Collection<MyNode> nodes = MyVars.g.getVertices();
        for (MyNode n : nodes) {
            synchronized (n) {
                n.setCurrentValue(n.getUniqueContribution());
            }
            if (maxVal < n.getCurrentValue()) {maxVal = n.getCurrentValue();}
        }
        synchronized (MyVars.g.MX_N_VAL) {
            MyVars.g.MX_N_VAL = Float.valueOf(maxVal);
        }
    }

    public static void setUniqueOutNodeCountToNodes() {
        float maxVal = 0.00f;
        Collection<MyNode> nodes = MyVars.g.getVertices();
        for (MyNode n : nodes) {
            n.setCurrentValue(n.getSuccessorCount());
            if (maxVal < n.getCurrentValue()) {maxVal = n.getCurrentValue();}
        }
        synchronized (MyVars.g.MX_N_VAL) {
            MyVars.g.MX_N_VAL = Float.valueOf(maxVal);
        }
    }

    public static void setContributionToNodes() {
        MyVars.getViewer().nodeValueName = "CONTRIBUTION";
        float maxVal = 0.00f;
        Collection<MyNode> nodes = MyVars.g.getVertices();
        for (MyNode n : nodes) {
            synchronized (n) {
                n.setCurrentValue((float) n.getContribution());
            }
            if (maxVal < n.getCurrentValue()) {
                maxVal = n.getCurrentValue();
            }
        }
        synchronized (MyVars.g.MX_N_VAL) {
            MyVars.g.MX_N_VAL = Float.valueOf(maxVal);
        }
    }

    public static void setPageRankScoresToNodes() {
        MyVars.getViewer().nodeValueName = "PAGERANK";
        float maxVal = 0.00f;
        Collection<MyNode> nodes = MyVars.g.getVertices();
        for (MyNode n : nodes) {
            synchronized (n) {
                n.setCurrentValue((float) n.getPageRankScore());
            }
            if (maxVal < n.getCurrentValue()) {
                maxVal = n.getCurrentValue();
            }
        }
        synchronized (MyVars.g.MX_N_VAL) {
            MyVars.g.MX_N_VAL = Float.valueOf(maxVal);
        }
    }

    public static void setInContributionToNodes() {
        MyVars.getViewer().nodeValueName = "IN-CONTRIBUTION";
        Collection<MyNode> nodes = MyVars.g.getVertices();
        float maxVal = 0.00f;
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0f) continue;
            synchronized (n) {
                n.setCurrentValue(n.getInContribution());
            }
            if (maxVal < n.getCurrentValue()) {maxVal = n.getCurrentValue();}
        }
        synchronized (MyVars.g.MX_N_VAL) {
            MyVars.g.MX_N_VAL = Float.valueOf(maxVal);
        }
    }

    public static void setTotalTimeToNodes() {
        float maxVal = 0.00f;
        Collection<MyNode> nodes = MyVars.g.getVertices();
        for (MyNode n : nodes) {
            float totTime = 0;
            for (int s = 0; s < MyVars.seqs.length; s++) {
                for (int i = 0; i < MyVars.seqs[s].length; i++) {
                    String [] pProperty = MyVars.seqs[s][i].split(":");
                    if (n.getName().equals(pProperty[0])) {
                        totTime += Float.valueOf(pProperty[1]);
                    }
                }
            }
            if (totTime > maxVal) {maxVal = totTime;}
            synchronized (n) {
                n.setCurrentValue(totTime);
            }
        }
        synchronized (MyVars.g.MX_E_VAL) {
            MyVars.g.MX_E_VAL = Float.valueOf(maxVal);
        }
    }

    public static void setOutContributionToNodes() {
        MyVars.getViewer().nodeValueName = "OUT-CONTRIBUTION";
        Collection<MyNode> nodes = MyVars.g.getVertices();
        float maxVal = 0.00f;
        for (MyNode n : nodes) {
            synchronized (n) {
                n.setCurrentValue(n.getOutContribution());
            }
            if (maxVal < n.getCurrentValue()) {maxVal = n.getCurrentValue();}
        }
        synchronized (MyVars.g.MX_N_VAL) {
            MyVars.g.MX_N_VAL = Float.valueOf(maxVal);
        }
    }

    public static void setInOutContributionDifferenceToNodes() {
        MyVars.getViewer().nodeValueName = "INOUT-CONTRIBUTION-DIFF.";
        Collection<MyNode> nodes = MyVars.g.getVertices();
        float maxVal = 0.00f;
        for (MyNode n : nodes) {
            synchronized (n) {
                n.setCurrentValue(Math.abs(n.getOutContribution() - n.getInContribution()));
            }
            if (n.getCurrentValue() > maxVal) {maxVal = n.getCurrentValue();}
        }
        synchronized (MyVars.g.MX_N_VAL) {
            MyVars.g.MX_N_VAL = maxVal;
        }
    }

    public static void setNodeValue() {
        try {
            if (MyVars.isTimeOn) {
                if (MyVars.getViewer().vc.nodeValueSelecter.getSelectedIndex() == 0) {
                    setContributionToNodes();
                } else if (MyVars.getViewer().vc.nodeValueSelecter.getSelectedIndex() == 1) {
                    setInContributionToNodes();
                } else if (MyVars.getViewer().vc.nodeValueSelecter.getSelectedIndex() == 2) {
                    setOutContributionToNodes();
                } else if (MyVars.getViewer().vc.nodeValueSelecter.getSelectedIndex() == 3) {
                    setInOutContributionDifferenceToNodes();
                } else if (MyVars.getViewer().vc.nodeValueSelecter.getSelectedIndex() == 4) {
                    setUniqueContributionToNodes();
                } else if (MyVars.getViewer().vc.nodeValueSelecter.getSelectedIndex() == 5) {
                    setUniqueInNodeCountToNodes();
                } else if (MyVars.getViewer().vc.nodeValueSelecter.getSelectedIndex() == 6) {
                    setUniqueOutNodeCountToNodes();
                } else if (MyVars.getViewer().vc.nodeValueSelecter.getSelectedIndex() == 7) {
                    setInOutNodeDifferenceToNodes();
                } else if (MyVars.getViewer().vc.nodeValueSelecter.getSelectedIndex() == 8) {
                    setEndNodeCountToNodes();
                } else if (MyVars.getViewer().vc.nodeValueSelecter.getSelectedIndex() == 9) {
                    setDurationToNodes();
                } else if (MyVars.getViewer().vc.nodeValueSelecter.getSelectedIndex() == 10) {
                    setAverageReachTimeToNodes();
                } else if (MyVars.getViewer().vc.nodeValueSelecter.getSelectedIndex() == 11) {
                    setTotalTimeToNodes();
                } else if (MyVars.getViewer().vc.nodeValueSelecter.getSelectedIndex() == 12) {
                    setRecurrenceCountToNodes();
                } else if (MyVars.getViewer().vc.nodeValueSelecter.getSelectedIndex() == 13) {
                    setBetweenessCentralityToNodes();
                } else if (MyVars.getViewer().vc.nodeValueSelecter.getSelectedIndex() == 14) {
                    setClosenessCentralityToNodes();
                } else if (MyVars.getViewer().vc.nodeValueSelecter.getSelectedIndex() == 15) {
                    setEigenVectorCentralityToNodes();
                } else if (MyVars.getViewer().vc.nodeValueSelecter.getSelectedIndex() == 16) {
                    setPageRankScoresToNodes();
                } else if (MyVars.getViewer().vc.nodeValueSelecter.getSelectedIndex() > 16) {
                    setUserDefinedNodeValuesToNodes();
                }
            } else {
                if (MyVars.getViewer().vc.nodeValueSelecter.getSelectedIndex() == 0) {
                    setContributionToNodes();
                } else if (MyVars.getViewer().vc.nodeValueSelecter.getSelectedIndex() == 1) {
                    setInContributionToNodes();
                } else if (MyVars.getViewer().vc.nodeValueSelecter.getSelectedIndex() == 2) {
                    setOutContributionToNodes();
                } else if (MyVars.getViewer().vc.nodeValueSelecter.getSelectedIndex() == 3) {
                    setInOutContributionDifferenceToNodes();
                } else if (MyVars.getViewer().vc.nodeValueSelecter.getSelectedIndex() == 4) {
                    setUniqueContributionToNodes();
                } else if (MyVars.getViewer().vc.nodeValueSelecter.getSelectedIndex() == 5) {
                    setUniqueInNodeCountToNodes();
                } else if (MyVars.getViewer().vc.nodeValueSelecter.getSelectedIndex() == 6) {
                    setUniqueOutNodeCountToNodes();
                } else if (MyVars.getViewer().vc.nodeValueSelecter.getSelectedIndex() == 7) {
                    setInOutNodeDifferenceToNodes();
                } else if (MyVars.getViewer().vc.nodeValueSelecter.getSelectedIndex() == 8) {
                    setEndNodeCountToNodes();
                } else if (MyVars.getViewer().vc.nodeValueSelecter.getSelectedIndex() == 9) {
                    setRecurrenceCountToNodes();
                } else if (MyVars.getViewer().vc.nodeValueSelecter.getSelectedIndex() == 10) {
                    setBetweenessCentralityToNodes();
                } else if (MyVars.getViewer().vc.nodeValueSelecter.getSelectedIndex() == 11) {
                    setClosenessCentralityToNodes();
                } else if (MyVars.getViewer().vc.nodeValueSelecter.getSelectedIndex() == 12) {
                    setEigenVectorCentralityToNodes();
                } else if (MyVars.getViewer().vc.nodeValueSelecter.getSelectedIndex() == 13) {
                    setPageRankScoresToNodes();
                } else if (MyVars.getViewer().vc.nodeValueSelecter.getSelectedIndex() > 13) {
                    setUserDefinedNodeValuesToNodes();
                }
            }
            MyViewerControlComponentUtil.setGraphLevelTableStatistics();
            MyVars.getViewer().revalidate();
            MyVars.getViewer().repaint();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static double getNodeValueStandardDeviation() {
        Collection<MyNode> nodes = MyVars.g.getVertices();
        double sum = 0.00d;
        int numNodes = 0;
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0) {continue;}
            numNodes++;
            sum += n.getCurrentValue();
        }
        double mean = sum/numNodes;
        double std = 0.00D;
        for(MyNode n : nodes) {
            std += Math.pow(n.getCurrentValue()-mean, 2);
        }
        return std/numNodes;
    }

    public static int getRedNodeCount() {
        Collection<MyNode> ns = MyVars.g.getVertices();
        int cnt = 0;
        for (MyNode n : ns) {
            if (n.getCurrentValue() > 0) {
                if (MyVars.g.getPredecessorCount( n) == 0 && MyVars.g.getSuccessorCount( n) > 0) {
                    cnt++;
                }
            }
        }
        return cnt;
    }

    public static double getRedNodePercent() {
        Collection<MyNode> ns = MyVars.g.getVertices();
        int cnt = 0;
        for (MyNode n : ns) {
            if (n.getCurrentValue() > 0) {
                if (MyVars.g.getPredecessorCount( n) == 0 && MyVars.g.getSuccessorCount( n) > 0) {
                    cnt++;
                }
            }
        }
        return ((double)cnt/ns.size());
    }

    public static int getBlueNodeCount() {
        Collection<MyNode> ns = MyVars.g.getVertices();
        int cnt = 0;
        for (MyNode n : ns) {
            if (n.getCurrentValue() > 0) {
                if (MyVars.g.getPredecessorCount(n) > 0 && MyVars.g.getSuccessorCount(n) > 0) {
                    cnt++;
                }
            }
        }
        return cnt;
    }

    public static double getBlueNodePercent() {
        Collection<MyNode> ns = MyVars.g.getVertices();
        int cnt = 0;
        for (MyNode n : ns) {
            if (n.getCurrentValue() > 0) {
                if (MyVars.g.getPredecessorCount(n) > 0 && MyVars.g.getSuccessorCount(n) > 0) {
                    cnt++;
                }
            }
        }
        return ((double)cnt/ns.size());
    }

    public static double getGreenNodePercent() {
        Collection<MyNode> ns = MyVars.g.getVertices();
        int cnt = 0;
        for (MyNode n : ns) {
            if (n.getCurrentValue() > 0) {
                if (MyVars.g.getPredecessorCount(n) > 0 && MyVars.g.getSuccessorCount(n) == 0) {
                    cnt++;
                }
            }
        }
        return ((double)cnt/ns.size());
    }

    public static int getGreenNodeCount() {
        Collection<MyNode> ns = MyVars.g.getVertices();
        int cnt = 0;
        for (MyNode n : ns) {
            if (n.getCurrentValue() > 0) {
                if (MyVars.g.getPredecessorCount(n) > 0 && MyVars.g.getSuccessorCount(n) == 0) {
                    cnt++;
                }
            }
        }
        return cnt;
    }

    public static double getMaximumNodeValue() {
        double maxValue = 0D;
        Collection<MyNode> ns = MyVars.g.getVertices();
        for (MyNode n : ns) {
            if (n.getCurrentValue() == 0) continue;
            if (maxValue < n.getCurrentValue()) {
                maxValue = n.getCurrentValue();
            }
        }
        return maxValue;
    }
    public static double getMinimumNodeValue() {
        double minValue = 1000000000000D;
        Collection<MyNode> ns = MyVars.g.getVertices();
        for (MyNode n : ns) {
            if (n.getCurrentValue() > 0 && minValue > n.getCurrentValue()) {
                minValue = n.getCurrentValue();
            }
        }
        return (minValue == 1000000000000D ? 0.00D : minValue);
    }

    public static String getAverageNodeValue() {
        Collection<MyNode> ns = MyVars.g.getVertices();
        float totalVal = 0f;
        for (MyNode n : ns) {
            if (n.getCurrentValue() > 0) {
                totalVal += n.getCurrentValue();
            }
        }
        return MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(totalVal/ns.size()));
    }

    public static int getGreaterThanZeroNodeValueCount() {
        Collection<MyNode> ns = MyVars.g.getVertices();
        int cnt=0;
        for (MyNode n : ns) {
            if (n.getCurrentValue() > 0) {
                cnt++;
            }
        }
        return cnt;
    }

    public static double getAverageInContribution() {
        long total = 0L;
        Collection<MyNode> nodes = MyVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0) continue;
            total += n.getInContribution();
        }
        return (double)total/nodes.size();
    }

    public static double getAverageOutContribution() {
        long total = 0L;
        Collection<MyNode> nodes = MyVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0) continue;
            total += n.getOutContribution();
        }
        return (double)total/nodes.size();
    }

    public static double getAveragePredecessorCount() {
        long total = 0L;
        int ncnt = 0;
        Collection<MyNode> nodes = MyVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0) continue;
            int val = MyVars.g.getPredecessorCount(n);
            if (val > 0) {
                total += val;
                ncnt++;
            }
        }
        return (double)total/ncnt;
    }

    public static double getAverageSuccessorCount() {
        long total = 0L;
        int ncnt = 0;
        Collection<MyNode> nodes = MyVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0) continue;
            int val = MyVars.g.getSuccessorCount(n);
            if (val > 0) {
                total += val;
                ncnt++;
            }
        }
        return (double)total/ncnt;
    }

    public static double getAverageUnreachableNodeCount() {
        long total = 0L;
        int ncnt = 0;
        Collection<MyNode> nodes = MyVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0) continue;
            int val = n.getUnreachableNodeCount();
            if (val > 0) {
                total += val;
                ncnt++;
            }
        }
        return (double)total/ncnt;
    }

    public static double getAverageUniqueContribution() {
        long total = 0L;
        int ncnt = 0;
        Collection<MyNode> nodes = MyVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0) continue;
            long val = n.getUniqueContribution();
            if (val > 0) {
                total += val;
                ncnt++;
            }
        }
        return (double)total/ncnt;
    }

    public static double getMaximumUniqueContribution() {
        long max = 0;
        Collection<MyNode> nodes = MyVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0) continue;
            long val = n.getUniqueContribution();
            if (val > max) {
                max = val;
            }
        }
        return max;
    }

    public static long getMinimumUniqueContribution() {
        long min = 1000000000000L;
        Collection<MyNode> nodes = MyVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0) continue;
            long val = n.getUniqueContribution();
            if (val < min && n.getUniqueContribution() > 0) {
                min = val;
            }
        }
        return (min == 1000000000000L ? 0 : min);
    }

    public static double getUniqueContributionStandardDeviation() {
        Collection<MyNode> nodes = MyVars.g.getVertices();
        double sum = 0.00d;
        int numNodes = 0;
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0) {continue;}
            numNodes++;
            sum += n.getUniqueContribution();
        }
        double mean = sum/numNodes;
        double std = 0.00D;
        for(MyNode n : nodes) {std += Math.pow(n.getUniqueContribution()-mean, 2);}
        return Math.sqrt(std/numNodes);
    }

    public static long getTotalUnreachableNodeCount() {
        Collection<MyNode> nodes = MyVars.g.getVertices();
        long count = 0L;
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0) continue;;
            count += n.getUnreachableNodeCount();
        }
        return count;
    }

}
