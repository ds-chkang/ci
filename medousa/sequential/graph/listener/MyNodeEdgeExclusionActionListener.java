package medousa.sequential.graph.listener;

import medousa.MyProgressBar;
import medousa.message.MyMessageUtil;
import medousa.sequential.graph.MyEdge;
import medousa.sequential.graph.MyNode;
import medousa.sequential.graph.MyViewerComponentController;
import medousa.sequential.utils.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class MyNodeEdgeExclusionActionListener
implements ActionListener {

    private MyViewerComponentController vc;


    public MyNodeEdgeExclusionActionListener(MyViewerComponentController vc) {
        this.vc = vc;
    }

    @Override public void actionPerformed(ActionEvent e) {
        new Thread(new Runnable() {
            @Override public void run() {
                MyProgressBar pb = new MyProgressBar(false);
                try {
                    int nodesRemoved = 0;
                    int edgesRemoved = 0;

                    Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
                    for (MyEdge e : edges) {
                        if (e.getCurrentValue() == 0) {
                            edgesRemoved++;
                        }
                    }

                    Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
                    for (MyNode n : nodes) {
                        if (n.getCurrentValue() == 0) {
                            nodesRemoved++;
                        }
                    }

                    if (vc.nodeValueExcludeTxt.getText().length() > 0 &&
                        vc.nodeValueExcludeTxt.getText().matches("[0-9]+")) {
                        doNodeValueExclusion();
                    }

                    if (vc.edgeValueExcludeTxt.getText().length() > 0 &&
                        vc.edgeValueExcludeTxt.getText().matches("[0-9]+")) {
                        if (vc.edgeValueSelecter.getSelectedIndex() <= 1) {
                            MyMessageUtil.showInfoMsg(MySequentialGraphVars.app, "Select an edge value.");
                            return;
                        }
                        doEdgeValueExclusion();
                    }

                    if (vc.nodeLabelExcludeSelecter.isShowing() &&
                        vc.nodeLabelExcludeSelecter.getSelectedIndex() > 0 &&
                        vc.nodeLabelExcludeMathSymbolSelecter.isShowing() &&
                        vc.nodeLabelExcludeMathSymbolSelecter.getSelectedIndex() > 0) {
                        doNodeLabelExclusion();
                    }

                    if (vc.edgeLabelExcludeSelecter.isShowing() &&
                        vc.edgeLabelExcludeSelecter.getSelectedIndex() > 0 &&
                        vc.edgeLabelExcludeMathSymbolSelecter.isShowing() &&
                        vc.edgeLabelExcludeMathSymbolSelecter.getSelectedIndex() > 0) {
                        doEdgeLabelExclusion();
                    }

                    if (vc.depthExcludeSelecter.isShowing() &&
                        vc.depthExcludeSelecter.getSelectedIndex() > 0 &&
                        vc.depthExcludeSymbolSelecter.getSelectedIndex() > 0) {
                        exludeDepthNodes(Integer.parseInt(vc.depthExcludeSelecter.getSelectedItem().toString()));
                    }

                    if (vc.nodeDateValueExcludeTxt.getText().length() > 0) {
                        try {
                            String[] dateTimes = vc.nodeDateValueExcludeTxt.getText().split(",");
                            if (dateTimes.length == 2) {
                                LocalDate.parse(dateTimes[0].split(" ")[0], DateTimeFormatter.ISO_DATE);
                                LocalDate.parse(dateTimes[1].split(" ")[0], DateTimeFormatter.ISO_DATE);

                                if (dateTimes[0].split(" ").length > 1) {
                                    LocalTime.parse(dateTimes[0].split(" ")[1], DateTimeFormatter.ISO_TIME);
                                }

                                if (dateTimes[1].split(" ").length > 1) {
                                    LocalTime.parse(dateTimes[0].split(" ")[1], DateTimeFormatter.ISO_TIME);
                                }
                            } else {
                                LocalDate.parse(dateTimes[0].split(" ")[0], DateTimeFormatter.ISO_DATE);

                                if (dateTimes[0].split(" ").length > 1) {
                                    LocalTime.parse(dateTimes[0].split(" ")[1], DateTimeFormatter.ISO_TIME);
                                }
                            }

                            doNodeDateTimeExclusion();
                        } catch (DateTimeParseException e) {
                            MyMessageUtil.showErrorMsg("<html><body>Provide a valid date & time value.<br>" +
                                "Date can go alone but time must follow date with a space separator between data and time.<br>" +
                                "For BTW. option, when time is not used, use 2000-01-01,2000-01-02, otherwise, use 2000-01-01" +
                                "but when time is used, a time must follow a date as follows:<br>" +
                                "2000-01-01 12:00:00,2000-01-02 12:00:01, for example. </body></html>");
                            pb.updateValue(100, 100);
                            pb.dispose();
                            return;
                        }
                    }

                    doNodeDayExclusion();
                    updateBarCharts();
                    vc.updateTableInfos();
                    
                    if (MySequentialGraphVars.getSequentialGraphViewer().singleNode != null) {
                        MySequentialGraphVars.app.getSequentialGraphDashboard().setSingleNodeDashBoard();
                    } else if (MySequentialGraphVars.getSequentialGraphViewer().multiNodes != null) {
                        MySequentialGraphVars.app.getSequentialGraphDashboard().setMultiNodeDashBoard();
                    } else {
                        MySequentialGraphVars.app.getSequentialGraphDashboard().setDashboard();
                    }
                    MySequentialGraphVars.app.revalidate();
                    MySequentialGraphVars.app.repaint();

                    int afterEdgesRemoved = 0;
                    int afterNodesRemoved = 0;

                    for (MyEdge e : edges) {
                        if (e.getCurrentValue() == 0) {
                            afterEdgesRemoved++;
                        } else if (e.getSource().getCurrentValue() == 0 || e.getDest().getCurrentValue() == 0) {
                            afterEdgesRemoved++;
                        }
                    }

                    for (MyNode n : nodes) {
                        if (n.getCurrentValue() == 0) {
                            afterNodesRemoved++;
                        }
                    }

                    nodesRemoved = afterNodesRemoved - nodesRemoved;
                    edgesRemoved = afterEdgesRemoved - edgesRemoved;

                    pb.updateValue(100, 100);
                    pb.dispose();

                    MyMessageUtil.showInfoMsg(
                        MyMathUtil.getCommaSeperatedNumber(nodesRemoved) + " nodes & " +
                        MyMathUtil.getCommaSeperatedNumber(edgesRemoved) + " edges got removed.");

                    MySequentialGraphVars.getSequentialGraphViewer().excluded = true;
                } catch (Exception ex) {
                    pb.updateValue(100, 100);
                    pb.dispose();
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    public void exludeDepthNodes(int depth) {
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        float max = 0f;
        if (MySequentialGraphVars.getSequentialGraphViewer().vc.depthExcludeSymbolSelecter.getSelectedIndex() == 1) {
            for (MyNode n : nodes) {
                if (n.getCurrentValue() > 0) {
                    if (n.nodeDepthInfoMap.containsKey(depth)) {
                        n.setOriginalValue(n.getCurrentValue());
                        n.setCurrentValue(0);
                    } else if (max < n.getCurrentValue()) {
                        max = n.getCurrentValue();
                    }
                }
            }
        } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.depthExcludeSymbolSelecter.getSelectedIndex() == 2) {
            for (MyNode n : nodes) {
                if (n.getCurrentValue() > 0) {
                    if (!n.nodeDepthInfoMap.containsKey(depth)) {
                        n.setOriginalValue(n.getCurrentValue());
                        n.setCurrentValue(0);
                    } else if (max < n.getCurrentValue()) {
                        max = n.getCurrentValue();
                    }
                }
            }
        } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.depthExcludeSymbolSelecter.getSelectedIndex() == 3) {
            for (MyNode n : nodes) {
                if (n.getCurrentValue() > 0) {
                    boolean isLowerThanDepth = false;
                    for (int i = depth+1; i <= MySequentialGraphVars.mxDepth; i++) {
                        if (n.nodeDepthInfoMap.containsKey(i)) {
                            isLowerThanDepth = true;
                            break;
                        }
                    }

                    if (!isLowerThanDepth) {
                        n.setOriginalValue(n.getCurrentValue());
                        n.setCurrentValue(0);
                    } else if (max < n.getCurrentValue()) {
                        max = n.getCurrentValue();
                    }
                }
            }
        } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.depthExcludeSymbolSelecter.getSelectedIndex() == 4) {
            for (MyNode n : nodes) {
                if (n.getCurrentValue() > 0) {
                    boolean isGreaterThanOrEqualDepth = false;
                    for (int i = depth; i <= MySequentialGraphVars.mxDepth; i++) {
                        if (n.nodeDepthInfoMap.containsKey(i)) {
                            isGreaterThanOrEqualDepth = true;
                            break;
                        }
                    }

                    if (!isGreaterThanOrEqualDepth) {
                        n.setOriginalValue(n.getCurrentValue());
                        n.setCurrentValue(0);
                    } else if (max < n.getCurrentValue()) {
                        max = n.getCurrentValue();
                    }
                }
            }
        } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.depthExcludeSymbolSelecter.getSelectedIndex() == 5) {
            for (MyNode n : nodes) {
                if (n.getCurrentValue() > 0) {
                    boolean isLowerThanDepth = false;
                    for (int i = 1; i < depth; i++) {
                        if (n.nodeDepthInfoMap.containsKey(i)) {
                            isLowerThanDepth = true;
                            break;
                        }
                    }

                    if (isLowerThanDepth) {
                        n.setOriginalValue(n.getCurrentValue());
                        n.setCurrentValue(0);
                    } else if (max < n.getCurrentValue()) {
                        max = n.getCurrentValue();
                    }
                }
            }
        } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.depthExcludeSymbolSelecter.getSelectedIndex() == 6) {
            for (MyNode n : nodes) {
                if (n.getCurrentValue() > 0) {
                    boolean isLowerThanOrEqualDepth = false;
                    for (int i = 1; i <= depth; i++) {
                        if (n.nodeDepthInfoMap.containsKey(i)) {
                            isLowerThanOrEqualDepth = true;
                            break;
                        }
                    }

                    if (isLowerThanOrEqualDepth) {
                        n.setOriginalValue(n.getCurrentValue());
                        n.setCurrentValue(0);
                    } else if (max < n.getCurrentValue()) {
                        max = n.getCurrentValue();
                    }
                }
            }
        }

        if (max > 0) {
            MySequentialGraphVars.g.MX_N_VAL = max;
            MySequentialGraphVars.getSequentialGraphViewer().excluded = true;
        }
    }

    private void updateBarCharts() {
        if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueBarChart.isSelected()) {
            MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueBarChart.setSelected(false);
            MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueBarChart.setSelected(true);
        }

        if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueBarChart.isSelected()) {
            MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueBarChart.setSelected(false);
            MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueBarChart.setSelected(true);
        }
    }

    private void doEdgeValueExclusion() {
        double edgeExcludeValue1 = Double.parseDouble(vc.edgeValueExcludeTxt.getText().trim());

        /**
         * Check edge value conditions.
         */
        Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
        if (vc.edgeValueExcludeSymbolSelecter.getSelectedIndex() == 1) {
            for (MyEdge e : edges) {
                if (e.getCurrentValue() > 0) {
                    if (e.getCurrentValue() > edgeExcludeValue1) {
                        e.setOriginalValue(e.getCurrentValue());
                        e.setCurrentValue(0.0f);
                    }
                }
            }
        } else if (vc.edgeValueExcludeSymbolSelecter.getSelectedIndex() == 2) {
            for (MyEdge e : edges) {
                if (e.getCurrentValue() > 0) {
                    if (e.getCurrentValue() >= edgeExcludeValue1) {
                        e.setOriginalValue(e.getCurrentValue());
                        e.setCurrentValue(0.0f);
                    }
                }
            }
        } else if (vc.edgeValueExcludeSymbolSelecter.getSelectedIndex() == 3) {
            for (MyEdge e : edges) {
                if (e.getCurrentValue() == 0) {
                    if (e.getCurrentValue() > edgeExcludeValue1) {
                        e.setOriginalValue(e.getCurrentValue());
                        e.setCurrentValue(0.0f);
                    }
                }
            }
        } else if (vc.edgeValueExcludeSymbolSelecter.getSelectedIndex() == 4) {
            for (MyEdge e : edges) {
                if (e.getCurrentValue() > 0) {
                    if (e.getCurrentValue() != edgeExcludeValue1) {
                        e.setOriginalValue(e.getCurrentValue());
                        e.setCurrentValue(0.0f);
                    }
                }
            }
        } else if (vc.edgeValueExcludeSymbolSelecter.getSelectedIndex() == 5) {
            for (MyEdge e : edges) {
                if (e.getCurrentValue() > 0) {
                    if (e.getCurrentValue() < edgeExcludeValue1) {
                        e.setOriginalValue(e.getCurrentValue());
                        e.setCurrentValue(0.0f);
                    }
                }
            }
        } else if (vc.edgeValueExcludeSymbolSelecter.getSelectedIndex() == 6) {
            for (MyEdge e : edges) {
                if (e.getCurrentValue() > 0) {
                    if (e.getCurrentValue() <= edgeExcludeValue1) {
                        e.setOriginalValue(e.getCurrentValue());
                        e.setCurrentValue(0.0f);
                    }
                }
            }
        } else if (vc.edgeLabelExcludeMathSymbolSelecter.getSelectedIndex() == 7) {
            String [] numberPair = vc.edgeValueExcludeTxt.getText().split(",");
            double leftNum = Double.parseDouble(numberPair[0].trim());
            double rightNum = Double.parseDouble(numberPair[1].trim());
            for (MyEdge e : edges) {
                if (e.getCurrentValue() > 0) {
                    if (e.getCurrentValue() >= leftNum || e.getCurrentValue() <= rightNum) {
                        e.setOriginalValue(e.getCurrentValue());
                        e.setCurrentValue(0.0f);
                    }
                }
            }
        }

        /**
         * Remove nodes with edges of zero values of neighbor nodes.
         */
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            Collection<MyEdge> neighborEdges = MySequentialGraphVars.g.getIncidentEdges(n);
            boolean allZeroEdges = true;
            for (MyEdge e : neighborEdges) {
                if (e.getCurrentValue() > 0) {
                    allZeroEdges = false;
                    break;
                }
            }

            if (allZeroEdges) {
                n.setOriginalValue(n.getCurrentValue());
                n.setCurrentValue(0);
            }
        }

    }

    private void doNodeValueExclusion() {
        double nodeExcludeValue1 = Double.parseDouble(vc.nodeValueExcludeTxt.getText().trim());
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        if (vc.nodeValueExcludeSymbolSelecter.getSelectedIndex() == 1) {
            float maxVal = 0f;
            for (MyNode n : nodes) {
                if (n.getCurrentValue() > 0) {
                    if (n.getCurrentValue() > nodeExcludeValue1) {
                        n.setOriginalValue(n.getCurrentValue());
                        n.setCurrentValue(0);
                        if (maxVal < n.getCurrentValue()) {
                            maxVal = n.getCurrentValue();
                        }
                    }
                }
            }
            MySequentialGraphVars.g.MX_N_VAL = maxVal;
        } else if (vc.nodeValueExcludeSymbolSelecter.getSelectedIndex() == 2) {
            float maxVal = 0f;
            for (MyNode n : nodes) {
                if (n.getCurrentValue() > 0) {
                    if (n.getCurrentValue() >= nodeExcludeValue1) {
                        n.setOriginalValue(n.getCurrentValue());
                        n.setCurrentValue(0);

                        if (maxVal < n.getCurrentValue()) {
                            maxVal = n.getCurrentValue();
                        }
                    }
                }
            }
            MySequentialGraphVars.g.MX_N_VAL = maxVal;
        } else if (vc.nodeValueExcludeSymbolSelecter.getSelectedIndex() == 3) {
            float maxVal = 0f;
            for (MyNode n : nodes) {
                if (n.getCurrentValue() > 0) {
                    if (n.getCurrentValue() == nodeExcludeValue1) {
                        n.setOriginalValue(n.getCurrentValue());
                        n.setCurrentValue(0);
                        if (maxVal < n.getCurrentValue()) {
                            maxVal = n.getCurrentValue();
                        }
                    }
                }
            }
            MySequentialGraphVars.g.MX_N_VAL = maxVal;
        } else if (vc.nodeValueExcludeSymbolSelecter.getSelectedIndex() == 4) {
            float maxVal = 0f;
            for (MyNode n : nodes) {
                if (n.getCurrentValue() > 0) {
                    if (n.getCurrentValue() != nodeExcludeValue1) {
                        n.setOriginalValue(n.getCurrentValue());
                        n.setCurrentValue(0);
                        if (maxVal < n.getCurrentValue()) {
                            maxVal = n.getCurrentValue();
                        }
                    }
                }
            }
            MySequentialGraphVars.g.MX_N_VAL = maxVal;
        } else if (vc.nodeValueExcludeSymbolSelecter.getSelectedIndex() == 5) {
            float maxVal = 0f;
            for (MyNode n : nodes) {
                if (n.getCurrentValue() > 0) {
                    if (n.getCurrentValue() < nodeExcludeValue1) {
                        n.setOriginalValue(n.getCurrentValue());
                        n.setCurrentValue(0);
                        if (maxVal < n.getCurrentValue()) {
                            maxVal = n.getCurrentValue();
                        }
                    }
                }
            }
            MySequentialGraphVars.g.MX_N_VAL = maxVal;
        } else if (vc.nodeValueExcludeSymbolSelecter.getSelectedIndex() == 6) {
            float maxVal = 0f;
            for (MyNode n : nodes) {
                if (n.getCurrentValue() > 0) {
                    if (n.getCurrentValue() <= nodeExcludeValue1) {
                        n.setOriginalValue(n.getCurrentValue());
                        n.setCurrentValue(0);
                        if (maxVal < n.getCurrentValue()) {
                            maxVal = n.getCurrentValue();
                        }
                    }
                }
            }
            MySequentialGraphVars.g.MX_N_VAL = maxVal;
        } else if (vc.nodeValueExcludeSymbolSelecter.getSelectedIndex() == 7) {
            float maxVal = 0f;
            String[] numberPair = vc.nodeValueExcludeTxt.getText().split(",");
            double leftNum = Double.parseDouble(numberPair[0].trim());
            double rightNum = Double.parseDouble(numberPair[1].trim());
            for (MyNode n : nodes) {
                if (n.getCurrentValue() > 0) {
                    if (n.getCurrentValue() >= leftNum || n.getCurrentValue() <= rightNum) {
                        n.setOriginalValue(n.getCurrentValue());
                        n.setCurrentValue(0);
                        if (maxVal < n.getCurrentValue()) {
                            maxVal = n.getCurrentValue();
                        }
                    }
                }
            }
            MySequentialGraphVars.g.MX_N_VAL = maxVal;
        }

        /**
         * Remvoe edges with zero values of nodes.
         */
        Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
        for (MyEdge e : edges) {
            if (e.getSource().getCurrentValue() == 0 || e.getDest().getCurrentValue() == 0) {
                e.setOriginalValue(e.getCurrentValue());
                e.setCurrentValue(0f);
            }
        }
    }

    private void doNodeDayExclusion() {
        try {
            if (vc.dayExcludeSelecter.getSelectedIndex() == 1) {
                Set<String> filteredNodes = new HashSet<>();
                float maxVal = 0f;
                BufferedReader br = new BufferedReader(
                        new FileReader(MySequentialGraphSysUtil.getWorkingDir() + MySequentialGraphSysUtil.getDirectorySlash() + "nodedatefeatures.txt"));
                String line = "";
                while ((line = br.readLine()) != null) {
                    String[] itemsets = line.split("-");
                    for (String itemset : itemsets) {
                        String day = itemset.split(":")[1];
                        if (day.equals(vc.daySet.get(vc.dayExcludeSelecter.getSelectedIndex()-1))) {
                            filteredNodes.add(itemset.split(":")[0]);
                        }
                    }
                }

                Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
                for (MyNode n : nodes) {
                    if (n.getCurrentValue() > 0 && filteredNodes.contains(n.getName())) {
                        n.setOriginalValue(n.getCurrentValue());
                        n.setCurrentValue(0);
                    } else {
                        if (maxVal < n.getCurrentValue()) {
                            maxVal = n.getCurrentValue();
                        }
                    }
                }

                MySequentialGraphVars.g.MX_N_VAL = maxVal;
            } else if (vc.dayExcludeSelecter.getSelectedIndex() == 2) {
                if (vc.dayExcludeSelecter.getSelectedIndex() == 1) {
                    Set<String> filteredNodes = new HashSet<>();
                    float maxVal = 0f;
                    BufferedReader br = new BufferedReader(
                            new FileReader(MySequentialGraphSysUtil.getWorkingDir() + MySequentialGraphSysUtil.getDirectorySlash() + "nodedatefeatures.txt"));
                    String line = "";
                    while ((line = br.readLine()) != null) {
                        String[] itemsets = line.split("-");
                        for (String itemset : itemsets) {
                            String day = itemset.split(":")[1];
                            if (!day.equals(vc.daySet.get(vc.dayExcludeSelecter.getSelectedIndex()-1))) {
                                filteredNodes.add(itemset.split(":")[0]);
                            }
                        }
                    }

                    Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
                    for (MyNode n : nodes) {
                        if (n.getCurrentValue() > 0 && filteredNodes.contains(n.getName())) {
                            n.setOriginalValue(n.getCurrentValue());
                            n.setCurrentValue(0);
                        } else {
                            if (maxVal < n.getCurrentValue()) {
                                maxVal = n.getCurrentValue();
                            }
                        }
                    }

                    MySequentialGraphVars.g.MX_N_VAL = maxVal;
                }
            } else if (vc.dayExcludeSelecter.getSelectedIndex() == 3) {
                String dateFormatPattern = "EEEE";
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(dateFormatPattern);
                DayOfWeek dayOfWeek1 = DayOfWeek.from(dateFormatter.parse(vc.daySet.get(vc.dayExcludeSelecter.getSelectedIndex()-1)));

                Set<String> filteredNodes = new HashSet<>();
                float maxVal = 0f;
                BufferedReader br = new BufferedReader(
                        new FileReader(MySequentialGraphSysUtil.getWorkingDir() + MySequentialGraphSysUtil.getDirectorySlash() + "nodedatefeatures.txt"));
                String line = "";
                while ((line = br.readLine()) != null) {
                    String[] itemsets = line.split("-");
                    for (String itemset : itemsets) {
                        String day = itemset.split(":")[1];
                        DayOfWeek dayOfWeek2 = DayOfWeek.from(dateFormatter.parse(day));
                        int comparisonResult = dayOfWeek1.compareTo(dayOfWeek2);
                        if (comparisonResult > 0) {
                            filteredNodes.add(itemset.split(":")[0]);
                        }
                    }
                }

                Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
                for (MyNode n : nodes) {
                    if (n.getCurrentValue() > 0 && filteredNodes.contains(n.getName())) {
                        n.setOriginalValue(n.getCurrentValue());
                        n.setCurrentValue(0);
                    } else {
                        if (maxVal < n.getCurrentValue()) {
                            maxVal = n.getCurrentValue();
                        }
                    }
                }
                MySequentialGraphVars.g.MX_N_VAL = maxVal;
            } else if (vc.dayExcludeSelecter.getSelectedIndex() == 6) {
                String dateFormatPattern = "EEEE";
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(dateFormatPattern);
                DayOfWeek dayOfWeek1 = DayOfWeek.from(dateFormatter.parse(vc.daySet.get(vc.dayExcludeSelecter.getSelectedIndex()-1)));

                Set<String> filteredNodes = new HashSet<>();
                float maxVal = 0f;
                BufferedReader br = new BufferedReader(
                        new FileReader(MySequentialGraphSysUtil.getWorkingDir() + MySequentialGraphSysUtil.getDirectorySlash() + "nodedatefeatures.txt"));
                String line = "";
                while ((line = br.readLine()) != null) {
                    String[] itemsets = line.split("-");
                    for (String itemset : itemsets) {
                        String day = itemset.split(":")[1];
                        DayOfWeek dayOfWeek2 = DayOfWeek.from(dateFormatter.parse(day));
                        int comparisonResult = dayOfWeek1.compareTo(dayOfWeek2);
                        if (comparisonResult > 0 || day.equals(vc.daySet.get(vc.dayExcludeSelecter.getSelectedIndex()-1))) {
                            filteredNodes.add(itemset.split(":")[0]);
                        }
                    }
                }

                Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
                for (MyNode n : nodes) {
                    if (n.getCurrentValue() > 0 && filteredNodes.contains(n.getName())) {
                        n.setOriginalValue(n.getCurrentValue());
                        n.setCurrentValue(0);
                    } else {
                        if (maxVal < n.getCurrentValue()) {
                            maxVal = n.getCurrentValue();
                        }
                    }
                }
                MySequentialGraphVars.g.MX_N_VAL = maxVal;
            } else if (vc.dayExcludeSelecter.getSelectedIndex() == 5) {
                String dateFormatPattern = "EEEE";
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(dateFormatPattern);
                DayOfWeek dayOfWeek1 = DayOfWeek.from(dateFormatter.parse(vc.daySet.get(vc.dayExcludeSelecter.getSelectedIndex()-1)));

                Set<String> filteredNodes = new HashSet<>();
                float maxVal = 0f;
                BufferedReader br = new BufferedReader(
                        new FileReader(MySequentialGraphSysUtil.getWorkingDir() + MySequentialGraphSysUtil.getDirectorySlash() + "nodedatefeatures.txt"));
                String line = "";
                while ((line = br.readLine()) != null) {
                    String[] itemsets = line.split("-");
                    for (String itemset : itemsets) {
                        String day = itemset.split(":")[1];
                        DayOfWeek dayOfWeek2 = DayOfWeek.from(dateFormatter.parse(day));
                        int comparisonResult = dayOfWeek1.compareTo(dayOfWeek2);
                        if (comparisonResult < 0) {
                            filteredNodes.add(itemset.split(":")[0]);
                        }
                    }
                }

                Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
                for (MyNode n : nodes) {
                    if (n.getCurrentValue() > 0 && filteredNodes.contains(n.getName())) {
                        n.setOriginalValue(n.getCurrentValue());
                        n.setCurrentValue(0);
                    } else {
                        if (maxVal < n.getCurrentValue()) {
                            maxVal = n.getCurrentValue();
                        }
                    }
                }
                MySequentialGraphVars.g.MX_N_VAL = maxVal;
            } else if (vc.dayExcludeSelecter.getSelectedIndex() == 6) {
                String dateFormatPattern = "EEEE";
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(dateFormatPattern);
                DayOfWeek dayOfWeek1 = DayOfWeek.from(dateFormatter.parse(vc.daySet.get(vc.dayExcludeSelecter.getSelectedIndex()-1)));

                Set<String> filteredNodes = new HashSet<>();
                float maxVal = 0f;
                BufferedReader br = new BufferedReader(
                        new FileReader(MySequentialGraphSysUtil.getWorkingDir() + MySequentialGraphSysUtil.getDirectorySlash() + "nodedatefeatures.txt"));
                String line = "";
                while ((line = br.readLine()) != null) {
                    String[] itemsets = line.split("-");
                    for (String itemset : itemsets) {
                        String day = itemset.split(":")[1];
                        DayOfWeek dayOfWeek2 = DayOfWeek.from(dateFormatter.parse(day));
                        int comparisonResult = dayOfWeek1.compareTo(dayOfWeek2);
                        if (comparisonResult < 0 || day.equals(vc.daySet.get(vc.dayExcludeSelecter.getSelectedIndex()-1))) {
                            filteredNodes.add(itemset.split(":")[0]);
                        }
                    }
                }

                Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
                for (MyNode n : nodes) {
                    if (n.getCurrentValue() > 0 && filteredNodes.contains(n.getName())) {
                        n.setOriginalValue(n.getCurrentValue());
                        n.setCurrentValue(0);
                    } else {
                        if (maxVal < n.getCurrentValue()) {
                            maxVal = n.getCurrentValue();
                        }
                    }
                }
                MySequentialGraphVars.g.MX_N_VAL = maxVal;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void doNodeDateTimeExclusion() {
        try {
            if (vc.nodeDateValueExcludeSymbolSelecter.getSelectedIndex() == 1) {
                Set<String> filteredNodes = new HashSet<>();
                String [] userDateTime = vc.nodeDateValueExcludeTxt.getText().split(","); // Separate date and time with a space separator.
                String [] userDateElements = userDateTime[0].split(" ");
                String [] userTimeElements = (userDateElements.length > 1 ? userDateElements[1].split(":") : null);
                userDateElements = userDateTime[0].split("-");

                Date userDateTimeObject =new Date(
                    Integer.parseInt(userDateElements[0].trim()),
                    Integer.parseInt(userDateElements[1].trim()),
                    Integer.parseInt(userDateElements[2].trim()),
                    (userTimeElements != null ? Integer.parseInt(userTimeElements[0].trim()) : 0),
                    (userTimeElements != null ? Integer.parseInt(userTimeElements[1].trim()) : 0),
                    (userTimeElements != null ? Integer.parseInt(userTimeElements[2].trim()) : 0));

                float maxVal = 0f;
                BufferedReader br = new BufferedReader(
                    new FileReader(MySequentialGraphSysUtil.getWorkingDir() + MySequentialGraphSysUtil.getDirectorySlash() + "nodedatefeatures.txt"));
                String line = "";

                while ((line = br.readLine()) != null) {
                    String [] itemsets = line.split("-");
                    for (String itemset : itemsets) {
                        String itemsetDateTime = itemset.split(":")[1];
                        String [] currentDataDateTime = itemsetDateTime.split(" ");
                        Date dataDateTimeObject = null;
                        if (currentDataDateTime.length == 2) {
                            String [] currentDataDateElements = currentDataDateTime[0].split("\\*");
                            String [] currentDataTimeElements = currentDataDateTime[1].split(":");
                            dataDateTimeObject = new Date(Integer.parseInt(currentDataDateElements[0].trim()),
                                Integer.parseInt(currentDataDateElements[1].trim()),
                                Integer.parseInt(currentDataDateElements[2].trim()),
                                Integer.parseInt(currentDataTimeElements[0].trim()),
                                Integer.parseInt(currentDataTimeElements[1].trim()),
                                Integer.parseInt(currentDataTimeElements[2].trim())
                            );
                        } else {
                            String [] currentDataDateElements = currentDataDateTime[0].split("\\*");
                           // System.out.println(currentDataDateElements[0] + "-" + currentDataDateElements[1] + "-" + currentDataDateElements[2]);
                            dataDateTimeObject = new Date(
                                Integer.parseInt(currentDataDateElements[0].trim()),
                                Integer.parseInt(currentDataDateElements[1].trim()),
                                Integer.parseInt(currentDataDateElements[2].trim()),
                                0,
                                0,
                                0);
                        }
                        int comparison = userDateTimeObject.compareTo(dataDateTimeObject);
                        if (comparison > 0) {
                            filteredNodes.add(itemset.split(":")[0]);
                        }
                    }
                }

                Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
                for (MyNode n : nodes) {
                    if (n.getCurrentValue() > 0 && filteredNodes.contains(n.getName())) {
                        n.setOriginalValue(n.getCurrentValue());
                        n.setCurrentValue(0);
                    } else {
                        if (maxVal < n.getCurrentValue()) {
                            maxVal = n.getCurrentValue();
                        }
                    }
                }

                MySequentialGraphVars.g.MX_N_VAL = maxVal;
            } else if (vc.nodeDateValueExcludeSymbolSelecter.getSelectedIndex() == 2) {
                Set<String> filteredNodes = new HashSet<>();
                String [] userDateTime = vc.nodeDateValueExcludeTxt.getText().split(","); // Separate date and time with a space separator.
                String [] userDateElements = userDateTime[0].split(" ");
                String [] userTimeElements = (userDateElements.length > 1 ? userDateElements[1].split(":") : null);
                userDateElements = userDateTime[0].split("-");

                Date userDateTimeObject =new Date(
                        Integer.parseInt(userDateElements[0].trim()),
                        Integer.parseInt(userDateElements[1].trim()),
                        Integer.parseInt(userDateElements[2].trim()),
                        (userTimeElements != null ? Integer.parseInt(userTimeElements[0].trim()) : 0),
                        (userTimeElements != null ? Integer.parseInt(userTimeElements[1].trim()) : 0),
                        (userTimeElements != null ? Integer.parseInt(userTimeElements[2].trim()) : 0));

                float maxVal = 0f;
                BufferedReader br = new BufferedReader(
                        new FileReader(MySequentialGraphSysUtil.getWorkingDir() + MySequentialGraphSysUtil.getDirectorySlash() + "nodedatefeatures.txt"));
                String line = "";

                while ((line = br.readLine()) != null) {
                    String [] itemsets = line.split("-");
                    for (String itemset : itemsets) {
                        String itemsetDateTime = itemset.split(":")[1];
                        String [] currentDataDateTime = itemsetDateTime.split(" ");
                        Date dataDateTimeObject = null;
                        if (currentDataDateTime.length == 2) {
                            String [] currentDataDateElements = currentDataDateTime[0].split("\\*");
                            String [] currentDataTimeElements = currentDataDateTime[1].split(":");
                            dataDateTimeObject = new Date(Integer.parseInt(currentDataDateElements[0].trim()),
                                    Integer.parseInt(currentDataDateElements[1].trim()),
                                    Integer.parseInt(currentDataDateElements[2].trim()),
                                    Integer.parseInt(currentDataTimeElements[0].trim()),
                                    Integer.parseInt(currentDataTimeElements[1].trim()),
                                    Integer.parseInt(currentDataTimeElements[2].trim())
                            );
                        } else {
                            String [] currentDataDateElements = currentDataDateTime[0].split("\\*");
                            dataDateTimeObject = new Date(
                                    Integer.parseInt(currentDataDateElements[0].trim()),
                                    Integer.parseInt(currentDataDateElements[1].trim()),
                                    Integer.parseInt(currentDataDateElements[2].trim()),
                                    0,
                                    0,
                                    0);
                        }
                        boolean greaterThan = userDateTimeObject.after(dataDateTimeObject);
                        boolean equalTo = userDateTimeObject.equals(dataDateTimeObject);

                        if (greaterThan || equalTo) {
                            filteredNodes.add(itemset.split(":")[0]);
                        }
                    }
                }

                Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
                for (MyNode n : nodes) {
                    if (n.getCurrentValue() > 0 && filteredNodes.contains(n.getName())) {
                        n.setOriginalValue(n.getCurrentValue());
                        n.setCurrentValue(0);
                    } else {
                        if (maxVal < n.getCurrentValue()) {
                            maxVal = n.getCurrentValue();
                        }
                    }
                }
                MySequentialGraphVars.g.MX_N_VAL = maxVal;
            } else if (vc.nodeDateValueExcludeSymbolSelecter.getSelectedIndex() == 3) {
                Set<String> filteredNodes = new HashSet<>();
                String [] userDateTime = vc.nodeDateValueExcludeTxt.getText().split(","); // Separate date and time with a space separator.
                String [] userDateElements = userDateTime[0].split(" ");
                String [] userTimeElements = (userDateElements.length > 1 ? userDateElements[1].split(":") : null);
                userDateElements = userDateTime[0].split("-");

                Date userDateTimeObject =new Date(
                        Integer.parseInt(userDateElements[0].trim()),
                        Integer.parseInt(userDateElements[1].trim()),
                        Integer.parseInt(userDateElements[2].trim()),
                        (userTimeElements != null ? Integer.parseInt(userTimeElements[0].trim()) : 0),
                        (userTimeElements != null ? Integer.parseInt(userTimeElements[1].trim()) : 0),
                        (userTimeElements != null ? Integer.parseInt(userTimeElements[2].trim()) : 0));

                float maxVal = 0f;
                BufferedReader br = new BufferedReader(
                        new FileReader(MySequentialGraphSysUtil.getWorkingDir() + MySequentialGraphSysUtil.getDirectorySlash() + "nodedatefeatures.txt"));
                String line = "";

                while ((line = br.readLine()) != null) {
                    String [] itemsets = line.split("-");
                    for (String itemset : itemsets) {
                        String itemsetDateTime = itemset.split(":")[1];
                        String [] currentDataDateTime = itemsetDateTime.split(" ");
                        Date dataDateTimeObject = null;
                        if (currentDataDateTime.length == 2) {
                            String [] currentDataDateElements = currentDataDateTime[0].split("\\*");
                            String [] currentDataTimeElements = currentDataDateTime[1].split(":");
                            dataDateTimeObject = new Date(Integer.parseInt(currentDataDateElements[0].trim()),
                                    Integer.parseInt(currentDataDateElements[1].trim()),
                                    Integer.parseInt(currentDataDateElements[2].trim()),
                                    Integer.parseInt(currentDataTimeElements[0].trim()),
                                    Integer.parseInt(currentDataTimeElements[1].trim()),
                                    Integer.parseInt(currentDataTimeElements[2].trim())
                            );
                        } else {
                            String [] currentDataDateElements = currentDataDateTime[0].split("\\*");
                            dataDateTimeObject = new Date(
                                    Integer.parseInt(currentDataDateElements[0].trim()),
                                    Integer.parseInt(currentDataDateElements[1].trim()),
                                    Integer.parseInt(currentDataDateElements[2].trim()),
                                    0,
                                    0,
                                    0);
                        }
                        boolean equal = userDateTimeObject.equals(dataDateTimeObject);
                        if (equal) {
                            filteredNodes.add(itemset.split(":")[0]);
                        }
                    }
                }

                Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
                for (MyNode n : nodes) {
                    if (n.getCurrentValue() > 0 && filteredNodes.contains(n.getName())) {
                        n.setOriginalValue(n.getCurrentValue());
                        n.setCurrentValue(0);
                    } else {
                        if (maxVal < n.getCurrentValue()) {
                            maxVal = n.getCurrentValue();
                        }
                    }
                }
                MySequentialGraphVars.g.MX_N_VAL = maxVal;
            } else if (vc.nodeDateValueExcludeSymbolSelecter.getSelectedIndex() == 4) {
                Set<String> filteredNodes = new HashSet<>();
                String [] userDateTime = vc.nodeDateValueExcludeTxt.getText().split(","); // Separate date and time with a space separator.
                String [] userDateElements = userDateTime[0].split(" ");
                String [] userTimeElements = (userDateElements.length > 1 ? userDateElements[1].split(":") : null);
                userDateElements = userDateTime[0].split("-");

                Date userDateTimeObject =new Date(
                    Integer.parseInt(userDateElements[0].trim()),
                    Integer.parseInt(userDateElements[1].trim()),
                    Integer.parseInt(userDateElements[2].trim()),
                    (userTimeElements != null ? Integer.parseInt(userTimeElements[0].trim()) : 0),
                    (userTimeElements != null ? Integer.parseInt(userTimeElements[1].trim()) : 0),
                    (userTimeElements != null ? Integer.parseInt(userTimeElements[2].trim()) : 0));

                float maxVal = 0f;
                BufferedReader br = new BufferedReader(new FileReader(MySequentialGraphSysUtil.getWorkingDir() + MySequentialGraphSysUtil.getDirectorySlash() + "nodedatefeatures.txt"));
                String line = "";

                while ((line = br.readLine()) != null) {
                    String [] itemsets = line.split("-");
                    for (String itemset : itemsets) {
                        String itemsetDateTime = itemset.split(":")[1];
                        String [] currentDataDateTime = itemsetDateTime.split(" ");
                        Date dataDateTimeObject = null;
                        if (currentDataDateTime.length == 2) {
                            String [] currentDataDateElements = currentDataDateTime[0].split("\\*");
                            String [] currentDataTimeElements = currentDataDateTime[1].split(":");
                            dataDateTimeObject = new Date(Integer.parseInt(currentDataDateElements[0].trim()),
                                Integer.parseInt(currentDataDateElements[1].trim()),
                                Integer.parseInt(currentDataDateElements[2].trim()),
                                Integer.parseInt(currentDataTimeElements[0].trim()),
                                Integer.parseInt(currentDataTimeElements[1].trim()),
                                Integer.parseInt(currentDataTimeElements[2].trim())
                            );
                        } else {
                            String [] currentDataDateElements = currentDataDateTime[0].split("\\*");
                            dataDateTimeObject = new Date(
                                Integer.parseInt(currentDataDateElements[0].trim()),
                                Integer.parseInt(currentDataDateElements[1].trim()),
                                Integer.parseInt(currentDataDateElements[2].trim()),
                                0,
                                0,
                                0);
                        }
                        boolean equal = userDateTimeObject.equals(dataDateTimeObject);
                        if (!equal) {
                            filteredNodes.add(itemset.split(":")[0]);
                        }
                    }
                }

                Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
                for (MyNode n : nodes) {
                    if (n.getCurrentValue() > 0 && filteredNodes.contains(n.getName())) {
                        n.setOriginalValue(n.getCurrentValue());
                        n.setCurrentValue(0);
                    } else {
                        if (maxVal < n.getCurrentValue()) {
                            maxVal = n.getCurrentValue();
                        }
                    }
                }
                MySequentialGraphVars.g.MX_N_VAL = maxVal;
            } else if (vc.nodeDateValueExcludeSymbolSelecter.getSelectedIndex() == 5) {
                Set<String> filteredNodes = new HashSet<>();
                String [] userDateTime = vc.nodeDateValueExcludeTxt.getText().split(","); // Separate date and time with a space separator.
                String [] userDateElements = userDateTime[0].split(" ");
                String [] userTimeElements = (userDateElements.length > 1 ? userDateElements[1].split(":") : null);
                userDateElements = userDateTime[0].split("-");

                Date userDateTimeObject =new Date(
                    Integer.parseInt(userDateElements[0].trim()),
                    Integer.parseInt(userDateElements[1].trim()),
                    Integer.parseInt(userDateElements[2].trim()),
                    (userTimeElements != null ? Integer.parseInt(userTimeElements[0].trim()) : 0),
                    (userTimeElements != null ? Integer.parseInt(userTimeElements[1].trim()) : 0),
                    (userTimeElements != null ? Integer.parseInt(userTimeElements[2].trim()) : 0));

                float maxVal = 0f;
                BufferedReader br = new BufferedReader(
                        new FileReader(MySequentialGraphSysUtil.getWorkingDir() + MySequentialGraphSysUtil.getDirectorySlash() + "nodedatefeatures.txt"));
                String line = "";

                while ((line = br.readLine()) != null) {
                    String [] itemsets = line.split("-");
                    for (String itemset : itemsets) {
                        String itemsetDateTime = itemset.split(":")[1];
                        String [] currentDataDateTime = itemsetDateTime.split(" ");
                        Date dataDateTimeObject = null;
                        if (currentDataDateTime.length == 2) {
                            String [] currentDataDateElements = currentDataDateTime[0].split("\\*");
                            String [] currentDataTimeElements = currentDataDateTime[1].split(":");
                            dataDateTimeObject = new Date(Integer.parseInt(currentDataDateElements[0].trim()),
                                Integer.parseInt(currentDataDateElements[1].trim()),
                                Integer.parseInt(currentDataDateElements[2].trim()),
                                Integer.parseInt(currentDataTimeElements[0].trim()),
                                Integer.parseInt(currentDataTimeElements[1].trim()),
                                Integer.parseInt(currentDataTimeElements[2].trim())
                            );
                        } else {
                            String [] currentDataDateElements = currentDataDateTime[0].split("\\*");
                            // System.out.println(currentDataDateElements[0] + "-" + currentDataDateElements[1] + "-" + currentDataDateElements[2]);
                            dataDateTimeObject = new Date(
                                Integer.parseInt(currentDataDateElements[0].trim()),
                                Integer.parseInt(currentDataDateElements[1].trim()),
                                Integer.parseInt(currentDataDateElements[2].trim()),
                                0,
                                0,
                                0);
                        }
                        int comparison = userDateTimeObject.compareTo(dataDateTimeObject);
                        if (comparison < 0) {
                            filteredNodes.add(itemset.split(":")[0]);
                        }
                    }
                }

                Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
                for (MyNode n : nodes) {
                    if (n.getCurrentValue() > 0 && filteredNodes.contains(n.getName())) {
                        n.setOriginalValue(n.getCurrentValue());
                        n.setCurrentValue(0);
                    } else {
                        if (maxVal < n.getCurrentValue()) {
                            maxVal = n.getCurrentValue();
                        }
                    }
                }
                MySequentialGraphVars.g.MX_N_VAL = maxVal;
            } else if (vc.nodeDateValueExcludeSymbolSelecter.getSelectedIndex() == 6) {
                Set<String> filteredNodes = new HashSet<>();
                String [] userDateTime = vc.nodeDateValueExcludeTxt.getText().split(","); // Separate date and time with a space separator.
                String [] userDateElements = userDateTime[0].split(" ");
                String [] userTimeElements = (userDateElements.length > 1 ? userDateElements[1].split(":") : null);
                userDateElements = userDateTime[0].split("-");

                Date userDateTimeObject =new Date(
                        Integer.parseInt(userDateElements[0].trim()),
                        Integer.parseInt(userDateElements[1].trim()),
                        Integer.parseInt(userDateElements[2].trim()),
                        (userTimeElements != null ? Integer.parseInt(userTimeElements[0].trim()) : 0),
                        (userTimeElements != null ? Integer.parseInt(userTimeElements[1].trim()) : 0),
                        (userTimeElements != null ? Integer.parseInt(userTimeElements[2].trim()) : 0));

                float maxVal = 0f;
                BufferedReader br = new BufferedReader(
                        new FileReader(MySequentialGraphSysUtil.getWorkingDir() + MySequentialGraphSysUtil.getDirectorySlash() + "nodedatefeatures.txt"));
                String line = "";

                while ((line = br.readLine()) != null) {
                    String [] itemsets = line.split("-");
                    for (String itemset : itemsets) {
                        String itemsetDateTime = itemset.split(":")[1];
                        String [] currentDataDateTime = itemsetDateTime.split(" ");
                        Date dataDateTimeObject = null;
                        if (currentDataDateTime.length == 2) {
                            String [] currentDataDateElements = currentDataDateTime[0].split("\\*");
                            String [] currentDataTimeElements = currentDataDateTime[1].split(":");
                            dataDateTimeObject = new Date(Integer.parseInt(currentDataDateElements[0].trim()),
                                    Integer.parseInt(currentDataDateElements[1].trim()),
                                    Integer.parseInt(currentDataDateElements[2].trim()),
                                    Integer.parseInt(currentDataTimeElements[0].trim()),
                                    Integer.parseInt(currentDataTimeElements[1].trim()),
                                    Integer.parseInt(currentDataTimeElements[2].trim())
                            );
                        } else {
                            String [] currentDataDateElements = currentDataDateTime[0].split("\\*");
                            dataDateTimeObject = new Date(
                                    Integer.parseInt(currentDataDateElements[0].trim()),
                                    Integer.parseInt(currentDataDateElements[1].trim()),
                                    Integer.parseInt(currentDataDateElements[2].trim()),
                                    0,
                                    0,
                                    0);
                        }
                        boolean smallerThan = userDateTimeObject.before(dataDateTimeObject);
                        boolean equalTo = userDateTimeObject.equals(dataDateTimeObject);

                        if (smallerThan || equalTo) {
                            filteredNodes.add(itemset.split(":")[0]);
                        }
                    }
                }

                Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
                for (MyNode n : nodes) {
                    if (n.getCurrentValue() > 0 && filteredNodes.contains(n.getName())) {
                        n.setOriginalValue(n.getCurrentValue());
                        n.setCurrentValue(0);
                    } else {
                        if (maxVal < n.getCurrentValue()) {
                            maxVal = n.getCurrentValue();
                        }
                    }
                }
                MySequentialGraphVars.g.MX_N_VAL = maxVal;
            } else if (vc.nodeDateValueExcludeSymbolSelecter.getSelectedIndex() == 7) {
                Set<String> filteredNodes = new HashSet<>();
                String [] userDateTime = vc.nodeDateValueExcludeTxt.getText().split(","); // Separate date and time with a space separator.
                String [] userDateElements = userDateTime[0].split(" ");
                String [] userTimeElements = (userDateElements.length > 1 ? userDateElements[1].split(":") : null);
                userDateElements = userDateTime[0].split("-");

                Date userDateTimeObject =new Date(
                        Integer.parseInt(userDateElements[0].trim()),
                        Integer.parseInt(userDateElements[1].trim()),
                        Integer.parseInt(userDateElements[2].trim()),
                        (userTimeElements != null ? Integer.parseInt(userTimeElements[0].trim()) : 0),
                        (userTimeElements != null ? Integer.parseInt(userTimeElements[1].trim()) : 0),
                        (userTimeElements != null ? Integer.parseInt(userTimeElements[2].trim()) : 0));

                float maxVal = 0f;
                BufferedReader br = new BufferedReader(
                        new FileReader(MySequentialGraphSysUtil.getWorkingDir() + MySequentialGraphSysUtil.getDirectorySlash() + "nodedatefeatures.txt"));
                String line = "";

                while ((line = br.readLine()) != null) {
                    String [] itemsets = line.split("-");
                    for (String itemset : itemsets) {
                        String itemsetDateTime = itemset.split(":")[1];
                        String [] currentDataDateTime = itemsetDateTime.split(" ");
                        Date dataDateTimeObject = null;
                        if (currentDataDateTime.length == 2) {
                            String [] currentDataDateElements = currentDataDateTime[0].split("\\*");
                            String [] currentDataTimeElements = currentDataDateTime[1].split(":");
                            dataDateTimeObject = new Date(Integer.parseInt(currentDataDateElements[0].trim()),
                                    Integer.parseInt(currentDataDateElements[1].trim()),
                                    Integer.parseInt(currentDataDateElements[2].trim()),
                                    Integer.parseInt(currentDataTimeElements[0].trim()),
                                    Integer.parseInt(currentDataTimeElements[1].trim()),
                                    Integer.parseInt(currentDataTimeElements[2].trim())
                            );
                        } else {
                            String [] currentDataDateElements = currentDataDateTime[0].split("\\*");
                            dataDateTimeObject = new Date(
                                    Integer.parseInt(currentDataDateElements[0].trim()),
                                    Integer.parseInt(currentDataDateElements[1].trim()),
                                    Integer.parseInt(currentDataDateElements[2].trim()),
                                    0,
                                    0,
                                    0);
                        }
                        boolean smallerThan = userDateTimeObject.before(dataDateTimeObject);
                        boolean equalTo = userDateTimeObject.equals(dataDateTimeObject);

                        if (smallerThan || equalTo) {
                            filteredNodes.add(itemset.split(":")[0]);
                        }
                    }
                }

                Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
                for (MyNode n : nodes) {
                    if (n.getCurrentValue() > 0 && filteredNodes.contains(n.getName())) {
                        n.setOriginalValue(n.getCurrentValue());
                        n.setCurrentValue(0);
                    } else {
                        if (maxVal < n.getCurrentValue()) {
                            maxVal = n.getCurrentValue();
                        }
                    }
                }
                MySequentialGraphVars.g.MX_N_VAL = maxVal;
            }

            /**
             * Remvoe edges with zero values of nodes.
             */
            Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
            for (MyEdge e : edges) {
                if (e.getSource().getCurrentValue() == 0 || e.getDest().getCurrentValue() == 0) {
                    e.setOriginalValue(e.getCurrentValue());
                    e.setCurrentValue(0f);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void doNodeLabelExclusion() {
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        if (vc.nodeLabelExcludeMathSymbolSelecter.getSelectedIndex() > 0) {// Node label condition is on.
            if (vc.nodeLabelExcludeMathSymbolSelecter.getSelectedIndex() == 1) {
                for (MyNode n : nodes) {
                    if (n.getCurrentValue() > 0) {
                        if (n.nodeLabelMap.containsValue(vc.nodeLabelExcludeSelecter.getSelectedItem().toString().trim())) {
                            n.setCurrentValue(0);
                        }
                    }
                }
            } else if (vc.nodeLabelExcludeMathSymbolSelecter.getSelectedIndex() == 2) {
                for (MyNode n : nodes) {
                    if (n.getCurrentValue() > 0) {
                        if (!n.nodeLabelMap.containsValue(vc.nodeLabelExcludeSelecter.getSelectedItem().toString().trim())) {
                            n.setCurrentValue(0);
                        }
                    }
                }
            }
        }
    }

    private void doEdgeLabelExclusion() {
        Collection<MyEdge> edges = MySequentialGraphVars.g.getVertices();
        if (vc.edgeLabelExcludeMathSymbolSelecter.getSelectedIndex() > 0) {// Edge label exclusion condition is on.
            if (vc.edgeLabelExcludeMathSymbolSelecter.getSelectedIndex() == 1) {
                for (MyEdge e : edges) {
                    if (e.getCurrentValue() > 0) {
                        if (e.edgeLabelMap.containsValue(vc.edgeLabelExcludeSelecter.getSelectedItem().toString().trim())) {
                            e.setOriginalValue(e.getCurrentValue());
                            e.setCurrentValue(0.0f);
                        }
                    }
                }
            } else if (vc.edgeLabelExcludeMathSymbolSelecter.getSelectedIndex() == 2) {
                for (MyEdge e : edges) {
                    if (e.getCurrentValue() > 0) {
                        if (!e.edgeLabelMap.containsValue(vc.edgeLabelExcludeSelecter.getSelectedItem().toString().trim())) {
                            e.setOriginalValue(e.getCurrentValue());
                            e.setCurrentValue(0.0f);
                        }
                    }
                }
            }
        }
    }

}