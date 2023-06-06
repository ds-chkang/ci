package medousa.sequential.graph.stats.barchart;

import medousa.direct.utils.MyDirectGraphVars;
import medousa.sequential.graph.MyNode;
import medousa.sequential.utils.MyMathUtil;
import medousa.sequential.utils.MySequentialGraphSysUtil;
import medousa.sequential.utils.MySequentialGraphVars;

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
    private int barViewerWidth;

    public MyDepthLevelNeighborNodeValueBarChart() {
        barViewerWidth = MySequentialGraphVars.getSequentialGraphViewer().getWidth()-500;
        setBounds(barViewerWidth, 5, 500, 2000);
    }

    public void setNeighborNodeValueBarChart() {
        try {
            this.isMaxValueSet = false;
            this.predecessors = new LinkedHashMap<>();
            this.successors = new LinkedHashMap<>();
            this.predecessorColors = new ArrayList<>();
            this.successorColors = new ArrayList<>();

            if (MySequentialGraphVars.getSequentialGraphViewer().vc.depthNeighborNodeTypeSelector.getSelectedItem().equals("S.")) {
                if (MySequentialGraphVars.getSequentialGraphViewer().vc.depthSelecter.getSelectedIndex() == 1 && MySequentialGraphVars.isSupplementaryOn) {
                    for (String depthNode : MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeNameSet) {
                        for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
                            if (MySequentialGraphVars.seqs[s][0].split(":")[0].equals(depthNode)) {
                                for (int i = 1; i < MySequentialGraphVars.seqs[s].length; i++) {
                                    String sn = (MySequentialGraphVars.seqs[s][i].split(":")[0].contains("x") ? MySequentialGraphSysUtil.getDecodeVariableNodeName(MySequentialGraphVars.seqs[s][i].split(":")[0]) : MySequentialGraphSysUtil.decodeNodeName(MySequentialGraphVars.seqs[s][i].split(":")[0]));
                                    if (this.successors.containsKey(sn)) {
                                            this.successors.put(sn, this.successors.get(sn) + 1);
                                    } else {
                                        this.successors.put(sn, 1f);
                                        final float hue = this.rand.nextFloat();
                                        final float saturation = 0.9f;
                                        final float luminance = 1.0f;
                                        Color successorRandomColor = Color.getHSBColor(hue, saturation, luminance);
                                        this.successorColors.add(successorRandomColor);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    for (String depthNode : MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeSuccessorMaps.keySet()) {
                        Map<String, Long> successorMap = MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeSuccessorMaps.get(depthNode);
                        for (String successor : successorMap.keySet()) {
                            String decodedName = (depthNode.contains("x") ? MySequentialGraphSysUtil.getDecodeVariableNodeName(depthNode) : MySequentialGraphSysUtil.decodeNodeName(depthNode));
                            MyNode s = (MyNode) MySequentialGraphVars.g.vRefs.get(successor);
                            this.successors.put(decodedName, (float) s.getNodeDepthInfo(MySequentialGraphVars.currentGraphDepth + 1).getInContribution());
                            final float hue = this.rand.nextFloat();
                            final float saturation = 0.9f;
                            final float luminance = 1.0f;
                            Color successorRandomColor = Color.getHSBColor(hue, saturation, luminance);
                            this.successorColors.add(successorRandomColor);
                        }
                    }
                }
            } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.depthNeighborNodeTypeSelector.getSelectedItem().equals("P.")) {
                for (String depthNode : MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodePredecessorMaps.keySet()) {
                    Map<String, Long> predecessorMap = MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodePredecessorMaps.get(depthNode);
                    for (String predecessor : predecessorMap.keySet()) {
                        String decodedName = (depthNode.contains("x") ? MySequentialGraphSysUtil.getDecodeVariableNodeName(depthNode) : MySequentialGraphSysUtil.decodeNodeName(depthNode));
                        this.predecessors.put(decodedName, (float) ((MyNode) MySequentialGraphVars.g.vRefs.get(predecessor)).getNodeDepthInfo(MySequentialGraphVars.currentGraphDepth-1).getOutContribution());
                        final float hue = this.rand.nextFloat();
                        final float saturation = 0.9f;
                        final float luminance = 1.0f;
                        Color successorRandomColor = Color.getHSBColor(hue, saturation, luminance);
                        this.predecessorColors.add(successorRandomColor);
                    }
                }
            }

            this.predecessors = MySequentialGraphSysUtil.sortMapByFloatValue(this.predecessors);
            this.successors = MySequentialGraphSysUtil.sortMapByFloatValue(this.successors);
            this.setOpaque(false);
            this.setBackground(new Color(0, 0, 0, 0.0f));
            this.setPredecessorMaximumValue();
            this.setSuccessorMaximumValue();
            isMaxValueSet = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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

    @Override public void paint(Graphics g) {
        if (!this.isMaxValueSet) return;
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        FontMetrics fontMetrics = g.getFontMetrics(MySequentialGraphVars.f_pln_12);

        if (this.predecessors.size() > 0 && this.successors.size() == 0) {
            g.setFont(MySequentialGraphVars.tahomaBoldFont12);
            int titleStringWidth = fontMetrics.stringWidth("DEPTH PREDECESSOR VALUES");
            int i=0;
            g.setColor(Color.DARK_GRAY);
            g.drawString("DEPTH PREDECESSOR VALUES", (this.getWidth() - titleStringWidth), (10 * (i + 1)) + ((i++ * this.gap)) - 1);
            int predecessorIdx = 0;
            for (String name : this.predecessors.keySet()) {
                if (predecessorIdx < MySequentialGraphVars.BAR_CHART_RECORD_LIMIT) {
                    g.setColor(this.predecessorColors.get(predecessorIdx++));
                    float valuePortion = this.predecessors.get(name) / this.predecessorMaxVal;
                    float BAR_WIDTH_TO_DRAW = this.BAR_WIDTH * valuePortion;
                    int xPos = (this.getWidth() - ((int) BAR_WIDTH_TO_DRAW + 1));
                    g.fillRect(xPos - 1, (10 * i) + (i * this.gap), (int) BAR_WIDTH_TO_DRAW, 10);
                    g.setColor(Color.BLUE);
                    g.drawRect(xPos - 2, (10 * i) + (i * this.gap), (int) BAR_WIDTH_TO_DRAW + 1, 10);
                    String value = MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(this.predecessors.get(name)));
                    value = (value.endsWith(".0") || value.endsWith(".00") ? value.substring(0, value.indexOf(".0")) : value);
                    value = name + "[" + value + "]";
                    int nameWritingPos = (xPos - 4) - fontMetrics.stringWidth(value);
                    g.setFont(MyDirectGraphVars.f_pln_12);
                    g.setColor(Color.DARK_GRAY);
                    g.drawString(value, nameWritingPos, (10 * (i + 1)) + ((i * this.gap) - 2));
                    i++;
                }
            }
        } else if (this.predecessors.size() == 0 && this.successors.size() > 0) {
            g.setFont(MySequentialGraphVars.tahomaBoldFont12);
            int titleStringWidth = fontMetrics.stringWidth("DEPTH SUCCESSOR VALUES");
            int i=0;
            g.drawString("DEPTH SUCCESSOR VALUES", (this.getWidth() - titleStringWidth), (10 * (i + 1)) + ((i++ * this.gap)) - 1);
            int successorIdx = 0;
            for (String name : this.successors.keySet()) {
                if (successorIdx < MySequentialGraphVars.BAR_CHART_RECORD_LIMIT) {
                    g.setColor(this.successorColors.get(successorIdx++));
                    float valuePortion = this.successors.get(name) / this.successorMaxVal;
                    float BAR_WIDTH_TO_DRAW = this.BAR_WIDTH * valuePortion;
                    int xPos = (this.getWidth() - ((int) BAR_WIDTH_TO_DRAW + 1));
                    g.fillRect(xPos - 1, (10 * i) + (i * this.gap), (int) BAR_WIDTH_TO_DRAW, 10);
                    g.setColor(Color.BLUE);
                    g.drawRect(xPos - 2, (10 * i) + (i * this.gap), (int) BAR_WIDTH_TO_DRAW + 1, 10);
                    String value = MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(this.successors.get(name)));
                    value = (value.endsWith(".0") || value.endsWith(".00") ? value.substring(0, value.indexOf(".0")) : value);
                    value = name + "[" + value + "]";
                    int nameWritingPos = (xPos - 4) - fontMetrics.stringWidth(value);
                    g.setFont(MyDirectGraphVars.f_pln_12);
                    g.setColor(Color.DARK_GRAY);
                    g.drawString(value, nameWritingPos, (10 * (i + 1)) + ((i * this.gap) - 2));
                    i++;
                }
            }
        } else if (this.predecessors.size() > 0 && this.successors.size() > 0) {
            g.setFont(MySequentialGraphVars.tahomaBoldFont12);
            int titleStringWidth = fontMetrics.stringWidth("DEPTH REDECESSOR VALUES");
            int i=0;
            g.setColor(Color.DARK_GRAY);
            g.drawString("DEPTH PREDECESSOR VALUES", (this.getWidth() - titleStringWidth), (10 * (i + 1)) + ((i++ * this.gap)) - 1);
            int predecessorIdx = 0;
            for (String name : this.predecessors.keySet()) {
                if (predecessorIdx < MySequentialGraphVars.BAR_CHART_RECORD_LIMIT) {
                    g.setColor(this.predecessorColors.get(predecessorIdx++));
                    float valuePortion = this.predecessors.get(name) / this.predecessorMaxVal;
                    float BAR_WIDTH_TO_DRAW = this.BAR_WIDTH * valuePortion;
                    int xPos = (this.getWidth() - ((int) BAR_WIDTH_TO_DRAW + 1));
                    g.fillRect(xPos - 1, (10 * i) + (i * this.gap), (int) BAR_WIDTH_TO_DRAW, 10);
                    g.setColor(Color.BLUE);
                    g.drawRect(xPos - 2, (10 * i) + (i * this.gap), (int) BAR_WIDTH_TO_DRAW + 1, 10);
                    String value = MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(this.predecessors.get(name)));
                    value = (value.endsWith(".0") || value.endsWith(".00") ? value.substring(0, value.indexOf(".0")) : value);
                    value = name + "[" + value + "]";
                    int nameWritingPos = (xPos - 4) - fontMetrics.stringWidth(value);
                    g.setFont(MyDirectGraphVars.f_pln_12);
                    g.setColor(Color.DARK_GRAY);
                    g.drawString(value, nameWritingPos, (10 * (i + 1)) + ((i * this.gap) - 2));
                    i++;
                }
            }

            i++;
            g.setFont(MySequentialGraphVars.tahomaBoldFont12);
            titleStringWidth = fontMetrics.stringWidth("DEPTH SUCCESSOR VALUES");
            g.setColor(Color.DARK_GRAY);
            g.drawString("DEPTH SUCCESSOR VALUES", (this.getWidth() - titleStringWidth), (10 * (i + 1)) + ((i++ * this.gap)) - 1);
            int successorIdx = 0;
            for (String name : this.successors.keySet()) {
                if (successorIdx < (MySequentialGraphVars.BAR_CHART_RECORD_LIMIT+100)) {
                    g.setColor(this.successorColors.get(successorIdx++));
                    float valuePortion = this.successors.get(name) / this.successorMaxVal;
                    float BAR_WIDTH_TO_DRAW = this.BAR_WIDTH * valuePortion;
                    int xPos = (this.getWidth() - ((int) BAR_WIDTH_TO_DRAW + 1));
                    g.fillRect(xPos - 1, (10 * i) + (i * this.gap), (int) BAR_WIDTH_TO_DRAW, 10);
                    g.setColor(Color.BLUE);
                    g.drawRect(xPos - 2, (10 * i) + (i * this.gap), (int) BAR_WIDTH_TO_DRAW + 1, 10);
                    String value = MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(this.successors.get(name)));
                    value = (value.endsWith(".0") || value.endsWith(".00") ? value.substring(0, value.indexOf(".0")) : value);
                    value = name + "[" + value + "]";
                    int nameWritingPos = (xPos - 4) - fontMetrics.stringWidth(value);
                    g.setFont(MyDirectGraphVars.f_pln_12);
                    g.setColor(Color.DARK_GRAY);
                    g.drawString(value, nameWritingPos, (10 * (i + 1)) + ((i * this.gap) - 2));
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