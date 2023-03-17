package datamining.graph.stats.depthnode;

import datamining.graph.MyNode;
import datamining.utils.MyMathUtil;
import datamining.utils.system.MySysUtil;
import datamining.utils.system.MyVars;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class MyDepthLevelNeighborNodeValueBarChart extends JPanel {

    private Image img;
    private final int BAR_WIDTH = 65;
    private Graphics graph;
    private ArrayList<Color> predecessorColors;
    private ArrayList<Color> successorColors;
    private LinkedHashMap<String, Float> predecessors;
    private LinkedHashMap<String, Float> successors;
    private float predecessorMaxVal = 0.00f;
    private float successorMaxVal = 0.00f;
    private Random rand = new Random();
    private boolean isMaxValueSet = false;
    private int gap = 2;


    public MyDepthLevelNeighborNodeValueBarChart() {}

    public void setNeighborNodeValueBarChart() {
        this.isMaxValueSet = false;
        this.predecessors = new LinkedHashMap<>();
        this.successors = new LinkedHashMap<>();
        this.predecessorColors = new ArrayList<>();
        this.successorColors = new ArrayList<>();

        if (MyVars.getViewer().vc.depthNeighborNodeTypeSelector.getSelectedItem().equals("S.")) {
            for (String depthNode : MyVars.getViewer().vc.depthNodeSuccessorMaps.keySet()) {
                Map<String, Integer> successorMap = MyVars.getViewer().vc.depthNodeSuccessorMaps.get(depthNode);
                for (String successor : successorMap.keySet()) {
                    String decodedName = (depthNode.contains("x") ? MySysUtil.decodeVariable(depthNode) : MySysUtil.decodeNodeName(depthNode));
                    this.successors.put(decodedName, (float) ((MyNode)MyVars.g.vRefs.get(successor)).getNodeDepthInfo(MyVars.currentGraphDepth +1).getInContribution());
                    final float hue = this.rand.nextFloat();
                    final float saturation = 0.9f;
                    final float luminance = 1.0f;
                    Color successorRandomColor = Color.getHSBColor(hue, saturation, luminance);
                    this.successorColors.add(successorRandomColor);
                }
            }
        } else if (MyVars.getViewer().vc.depthNeighborNodeTypeSelector.getSelectedItem().equals("P.")) {
            for (String depthNode : MyVars.getViewer().vc.depthNodePredecessorMaps.keySet()) {
                Map<String, Integer> predecessorMap = MyVars.getViewer().vc.depthNodePredecessorMaps.get(depthNode);
                for (String predecessor : predecessorMap.keySet()) {
                    String decodedName = (depthNode.contains("x") ? MySysUtil.decodeVariable(depthNode) : MySysUtil.decodeNodeName(depthNode));
                    this.predecessors.put(decodedName, (float) ((MyNode)MyVars.g.vRefs.get(predecessor)).getNodeDepthInfo(MyVars.currentGraphDepth -1).getOutContribution());
                    final float hue = this.rand.nextFloat();
                    final float saturation = 0.9f;
                    final float luminance = 1.0f;
                    Color successorRandomColor = Color.getHSBColor(hue, saturation, luminance);
                    this.predecessorColors.add(successorRandomColor);
                }
            }
        }

        this.predecessors = MySysUtil.sortMapByFloatValue(this.predecessors);
        this.successors = MySysUtil.sortMapByFloatValue(this.successors);
        if (MySysUtil.getViewerWidth() < 1200) {
            setBounds(0, 0, 0, 0);
        } else {
            setBounds((MySysUtil.getViewerWidth() <= 1900 ?
                    MySysUtil.getViewerWidth() - (765 + MyVars.getViewer().vc.nodeListTable.getWidth()) :
                    MySysUtil.getViewerWidth() - (810 + MyVars.getViewer().vc.nodeListTable.getWidth())), 5, 600, 2000);
        }        this.setOpaque(false);
        this.setBackground(new Color(0,0,0,0.0f));
        this.setPredecessorMaximumValue();
        this.setSuccessorMaximumValue();
        this.isMaxValueSet = true;
    }

    public void setSuccessorValueBarChartsForDepthNodes() {
        this.isMaxValueSet = false;
        this.successors = new LinkedHashMap<>();
        this.successorColors = new ArrayList<>();
        if (MyVars.getViewer().vc.depthNeighborNodeTypeSelector.getSelectedItem().equals("S.")) {
            for (String depthNode : MyVars.getViewer().vc.depthNodeSuccessorMaps.keySet()) {
                Map<String, Integer> successorMap = MyVars.getViewer().vc.depthNodeSuccessorMaps.get(depthNode);
                for (String successor : successorMap.keySet()) {
                    String decodedName = (depthNode.contains("x") ? MySysUtil.decodeVariable(depthNode) : MySysUtil.decodeNodeName(depthNode));
                    this.successors.put(decodedName, (float) ((MyNode)MyVars.g.vRefs.get(successor)).getNodeDepthInfo(MyVars.currentGraphDepth +1).getInContribution());
                    final float hue = this.rand.nextFloat();
                    final float saturation = 0.9f;
                    final float luminance = 1.0f;
                    Color successorRandomColor = Color.getHSBColor(hue, saturation, luminance);
                    this.successorColors.add(successorRandomColor);
                }
            }
        }
        this.successors = MySysUtil.sortMapByFloatValue(this.successors);
        if (MySysUtil.getViewerWidth() < 1200) {
            setBounds(0, 0, 0, 0);
        } else {
            setBounds((MySysUtil.getViewerWidth() <= 1900 ?
                    MySysUtil.getViewerWidth() - (765 + MyVars.getViewer().vc.nodeListTable.getWidth()) :
                    MySysUtil.getViewerWidth() - (810 + MyVars.getViewer().vc.nodeListTable.getWidth())), 5, 600, 2000);
        }        this.setOpaque(false);
        this.setBackground(new Color(0,0,0,0.0f));
        this.setSuccessorMaximumValue();
        this.isMaxValueSet = true;
    }

    public synchronized void setPredecessorValueBarChartsForDepthNodes() {
        this.isMaxValueSet = false;
        this.predecessors = new LinkedHashMap<>();
        this.predecessorColors = new ArrayList<>();

        for (String depthNode : MyVars.getViewer().vc.depthNodePredecessorMaps.keySet()) {
            Map<String, Integer> predecessorMap = MyVars.getViewer().vc.depthNodePredecessorMaps.get(depthNode);
            for (String predecessor : predecessorMap.keySet()) {
                String decodedName = (depthNode.contains("x") ? MySysUtil.decodeVariable(depthNode) : MySysUtil.decodeNodeName(depthNode));
                this.predecessors.put(decodedName, (float) ((MyNode) MyVars.g.vRefs.get(predecessor)).getNodeDepthInfo(MyVars.currentGraphDepth - 1).getOutContribution());
                final float hue = this.rand.nextFloat();
                final float saturation = 0.9f;
                final float luminance = 1.0f;
                Color successorRandomColor = Color.getHSBColor(hue, saturation, luminance);
                this.predecessorColors.add(successorRandomColor);
            }
        }

        this.predecessors = MySysUtil.sortMapByFloatValue(this.predecessors);
        if (MySysUtil.getViewerWidth() < 1200) {
            setBounds(0, 0, 0, 0);
        } else {
            setBounds((MySysUtil.getViewerWidth() <= 1900 ?
                    MySysUtil.getViewerWidth() - (765 + MyVars.getViewer().vc.nodeListTable.getWidth()) :
                    MySysUtil.getViewerWidth() - (810 + MyVars.getViewer().vc.nodeListTable.getWidth())), 5, 600, 2000);
        }
        this.setOpaque(false);
        this.setBackground(new Color(0,0,0,0.0f));
        this.setPredecessorMaximumValue();
        this.isMaxValueSet = true;
    }

    private void setPredecessorMaximumValue() {
        this.predecessorMaxVal = 0;
        for (Float value : this.predecessors.values()) {
            if (this.predecessorMaxVal < value) {this.predecessorMaxVal = value;}
        }
    }

    private void setSuccessorMaximumValue() {
        this.successorMaxVal = 0;
        for (Float value : this.successors.values()) {
            if (this.successorMaxVal < value) {this.successorMaxVal = value;}
        }
    }

    @Override public synchronized void paint(Graphics g) {
        if (!this.isMaxValueSet) return;
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        int i=0;
        FontMetrics fontMetrics = g.getFontMetrics(MyVars.f_pln_11);

        if (this.predecessors != null && this.predecessors.size() > 0) {
            g.setFont(MyVars.tahomaBoldFont10);
            g.setColor(Color.DARK_GRAY);
            g.drawString("DEPTH PREDECESSOR NODE VALUES", 190, (9*(i+1))+((i*this.gap))-1);

            i++;
            int pIdx = 0;
            for (String name : this.predecessors.keySet()) {
                if (this.predecessors.get(name) > 0 && pIdx < MyVars.BAR_CHART_RECORD_LIMIT) {
                    g.setColor(this.predecessorColors.get(pIdx++));
                    float valuePortion = this.predecessors.get(name)/this.predecessorMaxVal;
                    float BAR_WIDTH_TO_DRAW = this.BAR_WIDTH*valuePortion;
                    int xPos = (303+(this.BAR_WIDTH-(int)BAR_WIDTH_TO_DRAW));
                    g.fillRect(xPos-1, (9*i)+(i*this.gap), (int)BAR_WIDTH_TO_DRAW, 9);
                    g.setColor(Color.BLUE);
                    g.drawRect(xPos-2, (9*i)+(i*this.gap), (int)BAR_WIDTH_TO_DRAW+1, 9);
                    String value = MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(this.predecessors.get(name)));
                    value = (value.endsWith(".0") || value.endsWith(".00") ? value.substring(0, value.indexOf(".0")) : value);
                    value = name + "[" + value + "]";
                    int nameWritingPos = (xPos-6) -fontMetrics.stringWidth(value);
                    g.setFont(MyVars.f_pln_11);
                    g.setColor(Color.DARK_GRAY);
                    g.drawString(value, nameWritingPos, (9*(i+1))+((i*this.gap)-2));
                    i++;
                }
            }
        } else if (this.successors != null && this.successors.size() > 0) {
            g.setFont(MyVars.tahomaBoldFont10);
            g.setColor(Color.DARK_GRAY);
            g.drawString("DEPTH SUCCESSOR NODE VALUES", 195, (9*(i+1))+((i*this.gap))-1);
            i++;
            int sIdx = 0;
            for (String name : this.successors.keySet()) {
                if (this.successors.get(name) > 0 && sIdx < MyVars.BAR_CHART_RECORD_LIMIT) {
                    g.setColor(this.successorColors.get(sIdx++));
                    float valuePortion = this.successors.get(name)/this.successorMaxVal;
                    float BAR_WIDTH_TO_DRAW = this.BAR_WIDTH*valuePortion;
                    int xPos = (303+(this.BAR_WIDTH-(int)BAR_WIDTH_TO_DRAW));
                    g.fillRect(xPos-1, (9*i)+(i*this.gap), (int)BAR_WIDTH_TO_DRAW, 9);
                    g.setColor(Color.BLUE);
                    g.drawRect(xPos-2, (9*i)+(i*this.gap), (int)BAR_WIDTH_TO_DRAW+1, 9);
                    String value = MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(this.successors.get(name)));
                    value = (value.endsWith(".0") || value.endsWith(".00") ? value.substring(0, value.indexOf(".0")) : value);
                    value = name + "[" + value + "]";
                    int nameWritingPos = (xPos-6) -fontMetrics.stringWidth(value);
                    g.setFont(MyVars.f_pln_11);
                    g.setColor(Color.DARK_GRAY);
                    g.drawString(value, nameWritingPos, (9*(i+1))+((i*this.gap)-2));
                    i++;
                }
            }
        }
    }

    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(img ==null) {
            img =createImage(getWidth(), getHeight());
            this.graph= img.getGraphics();
        }
        this.graph.setColor(getBackground());
        this.graph.fillRect(0, 0, getWidth(), getHeight());
        this.graph.setColor(getForeground());
        paint(this.graph);
        g.drawImage(img,0,0,this);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Bar Chart");
        frame.setPreferredSize(new Dimension(400, 450));
        LinkedHashMap<String, Float> entities = new LinkedHashMap<>();

        entities.put("A", 100f);
        entities.put("B", 200f);
        entities.put("C", 120f);
        entities.put("D", 250f);
        entities.put("E", 110f);
        entities.put("F", 90f);
        entities.put("G", 20f);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}