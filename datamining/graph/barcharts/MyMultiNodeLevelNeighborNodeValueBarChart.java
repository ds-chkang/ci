package datamining.graph.barcharts;

import datamining.graph.MyDirectEdge;
import datamining.utils.MyMathUtil;
import datamining.utils.system.MySysUtil;
import datamining.utils.system.MyVars;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Random;

public class MyMultiNodeLevelNeighborNodeValueBarChart extends JPanel {

    private Image i;
    private final int BAR_WIDTH = 60;
    private Graphics graph;
    private LinkedHashMap<String, Float> sharedPredecessors;
    private LinkedHashMap<String, Float> sharedSuccessors;
    private float predecessorMaxVal = 0.00f;
    private float successorMaxVal = 0.00f;
    private Random rand = new Random();
    private boolean isMaxValueSet = false;
    private int gap = 2;
    private ArrayList<Color> predecessorColors;
    private ArrayList<Color> successorColors;


    public MyMultiNodeLevelNeighborNodeValueBarChart() {
        this.setNeighborNodeValueBarChart();
    }

    public void setNeighborNodeValueBarChart() {
        this.isMaxValueSet = false;
        this.sharedPredecessors = new LinkedHashMap<>();
        this.sharedSuccessors = new LinkedHashMap<>();
        this.predecessorColors = new ArrayList<>();
        this.successorColors = new ArrayList<>();

        Collection<MyDirectEdge> edges = MyVars.directMarkovChain.getEdges();
        for (MyDirectEdge e : edges) {
            if (MyVars.getDirectGraphViewer().multiNodes.contains(e.getDest()) && MyVars.getDirectGraphViewer().multiNodeSharedPredecessorSet.contains(e.getSource())) {
                this.sharedPredecessors.put(e.getSource().getName(), e.getSource().getCurrentValue());
                final float hue = this.rand.nextFloat();
                final float saturation = 0.9f;
                final float luminance = 1.0f;
                Color predecessorRandomColor = Color.getHSBColor(hue, saturation, luminance);
                this.predecessorColors.add(predecessorRandomColor);
            }

            if (MyVars.getDirectGraphViewer().multiNodes.contains(e.getSource()) && MyVars.getDirectGraphViewer().multiNodeSharedSuccessorSet.contains(e.getDest())) {
                this.sharedSuccessors.put(e.getDest().getName(), e.getDest().getCurrentValue());
                final float hue = this.rand.nextFloat();
                final float saturation = 0.9f;
                final float luminance = 1.0f;
                Color successorRandomColor = Color.getHSBColor(hue, saturation, luminance);
                this.successorColors.add(successorRandomColor);
            }
        }

        this.sharedPredecessors = MySysUtil.sortMapByFloatValue(this.sharedPredecessors);
        this.sharedSuccessors = MySysUtil.sortMapByFloatValue(this.sharedSuccessors);
        setBounds(((MyVars.main.getWidth() > 1800) ? MyVars.main.getWidth()-964 : MyVars.main.getWidth()-896), 5, 515, 2000);

        this.setOpaque(false);
        this.setBackground(new Color(0,0,0,0.0f));
        this.setPredecessorMaximumValue();
        this.setSuccessorMaximumValue();
        this.isMaxValueSet = true;
    }

    private void setPredecessorMaximumValue() {
        this.predecessorMaxVal = 0;
        for (Float value : this.sharedPredecessors.values()) {
            if (this.predecessorMaxVal < value) {this.predecessorMaxVal = value;}
        }
    }

    private void setSuccessorMaximumValue() {
        this.successorMaxVal = 0;
        for (Float value : this.sharedSuccessors.values()) {
            if (this.successorMaxVal < value) {this.successorMaxVal = value;}
        }
    }

    @Override public void paint(Graphics g) {
        if (!this.isMaxValueSet) return;
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        FontMetrics fontMetrics = g.getFontMetrics(MyVars.f_pln_10);
        int i=0;

        if (this.sharedPredecessors.size() > 0 && this.sharedSuccessors.size() == 0) {
            g.setFont(MyVars.tahomaBoldFont10);
            g.setColor(Color.DARK_GRAY);
            g.drawString("SHARED PREDECESSOR VALUES", 330, (9*(i+1))+((i*gap))-2);

            i++;
            int predecessorIdx = 0;
            for (String name : this.sharedPredecessors.keySet()) {
                if (predecessorIdx < MyVars.BAR_CHART_RECORD_LIMIT) {
                    g.setColor(this.predecessorColors.get(predecessorIdx++));
                    float valuePortion = this.sharedPredecessors.get(name)/this.predecessorMaxVal;
                    float BAR_WIDTH_TO_DRAW = this.BAR_WIDTH*valuePortion;
                    int xPos = (432+(this.BAR_WIDTH-(int)BAR_WIDTH_TO_DRAW));
                    g.fillRect(xPos-1, (9*i)+(i*this.gap), (int)BAR_WIDTH_TO_DRAW, 9);
                    g.setColor(Color.BLUE);
                    g.drawRect(xPos-2, (9*i)+(i*this.gap), (int)BAR_WIDTH_TO_DRAW+1, 9);
                    String value = MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(this.sharedPredecessors.get(name)));
                    value = (value.endsWith(".0") || value.endsWith(".00") ? value.substring(0, value.indexOf(".0")) : value);
                    value = name + "[" + value + "]";
                    int nameWritingPos = (xPos-10) -fontMetrics.stringWidth(value);
                    g.setFont(MyVars.f_pln_11);
                    g.setColor(Color.DARK_GRAY);
                    g.drawString(value, nameWritingPos, (9*(i+1))+((i*this.gap)-2));
                    i++;
                }
            }
        } else if (this.sharedPredecessors.size() == 0 && this.sharedSuccessors.size() > 0) {
            g.setFont(MyVars.tahomaBoldFont10);
            g.setColor(Color.DARK_GRAY);
            g.drawString("SHARED SUCCESSOR VALUES", 344, (9*(i+1))+((i*gap)-2));
            i++;
            int successorIdx = 0;
            for (String name : this.sharedSuccessors.keySet()) {
                if (successorIdx < MyVars.BAR_CHART_RECORD_LIMIT) {
                    g.setColor(this.successorColors.get(successorIdx++));
                    float valuePortion = this.sharedSuccessors.get(name)/this.successorMaxVal;
                    float BAR_WIDTH_TO_DRAW = this.BAR_WIDTH*valuePortion;
                    int xPos = (432+(this.BAR_WIDTH-(int)BAR_WIDTH_TO_DRAW));
                    g.fillRect(xPos-1, (9*i)+(i*this.gap), (int)BAR_WIDTH_TO_DRAW, 9);
                    g.setColor(Color.BLUE);
                    g.drawRect(xPos-2, (9*i)+(i*this.gap), (int)BAR_WIDTH_TO_DRAW+1, 9);
                    String value = MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(this.sharedSuccessors.get(name)));
                    value = (value.endsWith(".0") || value.endsWith(".00") ? value.substring(0, value.indexOf(".0")) : value);
                    value = name + "[" + value + "]";
                    int nameWritingPos = (xPos-10) -fontMetrics.stringWidth(value);
                    g.setFont(MyVars.f_pln_11);
                    g.setColor(Color.DARK_GRAY);
                    g.drawString(value, nameWritingPos, (9*(i+1))+((i*this.gap)-2));
                    i++;
                }
            }
        } else if (this.sharedPredecessors.size() > 0 && this.sharedSuccessors.size() > 0) {
            g.setFont(MyVars.tahomaBoldFont10);
            g.setColor(Color.DARK_GRAY);
            g.drawString("SHARED PREDECESSOR VALUES", 330, (9*(i+1))+((i*gap))-2);
            i++;

            int predecessorIdx = 0;
            for (String name : this.sharedPredecessors.keySet()) {
                if (predecessorIdx < MyVars.BAR_CHART_RECORD_LIMIT) {
                    g.setColor(this.predecessorColors.get(predecessorIdx++));
                    float valuePortion = this.sharedPredecessors.get(name)/this.predecessorMaxVal;
                    float BAR_WIDTH_TO_DRAW = this.BAR_WIDTH*valuePortion;
                    int xPos = (432+(this.BAR_WIDTH-(int)BAR_WIDTH_TO_DRAW));
                    g.fillRect(xPos-1, (9*i)+(i*this.gap), (int)BAR_WIDTH_TO_DRAW, 9);
                    g.setColor(Color.BLUE);
                    g.drawRect(xPos-2, (9*i)+(i*this.gap), (int)BAR_WIDTH_TO_DRAW+1, 9);
                    String value = MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(this.sharedPredecessors.get(name)));
                    value = (value.endsWith(".0") || value.endsWith(".00") ? value.substring(0, value.indexOf(".0")) : value);
                    value = name + "[" + value + "]";
                    int nameWritingPos = (xPos-10) - fontMetrics.stringWidth(value);
                    g.setFont(MyVars.f_pln_11);
                    g.setColor(Color.DARK_GRAY);
                    g.drawString(value, nameWritingPos, (9*(i+1))+((i*this.gap)-2));
                    i++;
                }
            }

            g.setFont(MyVars.tahomaBoldFont10);
            g.setColor(Color.DARK_GRAY);
            g.drawString("", 5, (9*(i+1))+((i*this.gap)-2));
            i++;

            g.setFont(MyVars.tahomaBoldFont10);
            g.setColor(Color.DARK_GRAY);
            g.drawString("SHARED SUCCESSOR VALUES", 344, (9*(i+1))+((i*gap)-2));
            i++;

            int successorIdx = 0;
            for (String name : this.sharedSuccessors.keySet()) {
                if (successorIdx < MyVars.BAR_CHART_RECORD_LIMIT) {
                    g.setColor(this.successorColors.get(successorIdx++));
                    float valuePortion = this.sharedSuccessors.get(name)/this.successorMaxVal;
                    float BAR_WIDTH_TO_DRAW = this.BAR_WIDTH*valuePortion;
                    int xPos = (432+(this.BAR_WIDTH-(int)BAR_WIDTH_TO_DRAW));
                    g.fillRect(xPos-1, (9*i)+(i*this.gap), (int)BAR_WIDTH_TO_DRAW, 9);
                    g.setColor(Color.BLUE);
                    g.drawRect(xPos-2, (9*i)+(i*this.gap), (int)BAR_WIDTH_TO_DRAW+1, 9);
                    String value = MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(this.sharedSuccessors.get(name)));
                    value = (value.endsWith(".0") || value.endsWith(".00") ? value.substring(0, value.indexOf(".0")) : value);
                    value = name + "[" + value + "]";
                    int nameWritingPos = (xPos-10) -fontMetrics.stringWidth(value);
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
        if(i==null) {
            i=createImage(getWidth(), getHeight());
            this.graph=i.getGraphics();
        }
        this.graph.setColor(getBackground());
        this.graph.fillRect(0, 0, getWidth(), getHeight());
        this.graph.setColor(getForeground());
        paint(this.graph);
        g.drawImage(i,0,0,this);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Bar Chart");
        frame.setPreferredSize(new Dimension(400, 450));
        LinkedHashMap<String, Float> entities = new LinkedHashMap<>();

        entities.put("A", 90f);
        entities.put("B", 200f);
        entities.put("C", 120f);
        entities.put("D", 250f);
        entities.put("E", 19f);
        entities.put("F", 90f);
        entities.put("G", 20f);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}