package medousa.direct.graph.barcharts;

import medousa.direct.graph.MyDirectEdge;
import medousa.direct.utils.MyDirectGraphMathUtil;
import medousa.direct.utils.MyDirectGraphSysUtil;
import medousa.direct.utils.MyDirectGraphVars;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Random;

public class MyDirectGraphEdgeValueBarChart extends JPanel {

    private Image img;
    private Graphics graph;
    private final int BAR_WIDTH = 50;
    private LinkedHashMap<String, Float> edgeData;
    private float maxVal = 0.00f;
    private float predecessorMaxVal = 0.00f;
    private float successorMaxVal = 0.00f;
    private Random rand = new Random();
    private int gap = 2;
    private ArrayList<Color> colors;

    public MyDirectGraphEdgeValueBarChart() {}

    public void setPredecessorEdgeValueBarChart() {
        this.edgeData = new LinkedHashMap<>();
        this.colors = new ArrayList<>();
        Collection<MyDirectEdge> edges = MyDirectGraphVars.directGraph.getEdges();
        if (MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode != null) {
            for (MyDirectEdge e : edges) {
                if (e.getDest() != MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode) continue;
                String name = e.getSource().getName();
                this.edgeData.put(name, e.getCurrentValue());
                final float hue = this.rand.nextFloat();
                final float saturation = 0.9f;
                final float luminance = 1.0f;
                this.colors.add(Color.getHSBColor(hue, saturation, luminance));
            }
        } else if (MyDirectGraphVars.getDirectGraphViewer().multiNodes != null) {
            for (MyDirectEdge e : edges) {
                if (!MyDirectGraphVars.getDirectGraphViewer().multiNodePredecessorSet.contains(e.getSource())) continue;
                String name = e.getSource().getName();
                this.edgeData.put(name, e.getCurrentValue());
                final float hue = this.rand.nextFloat();
                final float saturation = 0.9f;
                final float luminance = 1.0f;
                this.colors.add(Color.getHSBColor(hue, saturation, luminance));
            }
        }

        this.edgeData = MyDirectGraphSysUtil.sortMapByFloatValue(this.edgeData);
        this.setMaximumValue();
        setBounds(8, 5, 600, 2000);
        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0.0f));
    }

    public void setSuccessorEdgeValueBarChart() {
        this.edgeData = new LinkedHashMap<>();
        this.colors = new ArrayList<>();
        Collection<MyDirectEdge> edges = MyDirectGraphVars.directGraph.getEdges();
        if (MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode != null) {
            for (MyDirectEdge e : edges) {
                if (e.getDest() != MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode) continue;
                String name = e.getSource().getName();
                this.edgeData.put(name, e.getCurrentValue());
                final float hue = this.rand.nextFloat();
                final float saturation = 0.9f;
                final float luminance = 1.0f;
                this.colors.add(Color.getHSBColor(hue, saturation, luminance));
            }
        } else if (MyDirectGraphVars.getDirectGraphViewer().multiNodes != null) {
            for (MyDirectEdge e : edges) {
                if (e.getSource() != MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode) continue;
                String name = e.getSource().getName();
                this.edgeData.put(name, e.getCurrentValue());
                final float hue = this.rand.nextFloat();
                final float saturation = 0.9f;
                final float luminance = 1.0f;
                this.colors.add(Color.getHSBColor(hue, saturation, luminance));
            }
        }

        this.edgeData = MyDirectGraphSysUtil.sortMapByFloatValue(this.edgeData);
        this.setMaximumValue();
        setBounds(8, 5, 600, 2000);
        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0.0f));
    }

    public void showPredecesorAndSuccessorEdgeBarChart() {
        this.showBoth = true;
        this.predecessorData = new LinkedHashMap<>();
        this.successorData = new LinkedHashMap<>();
        this.colors = new ArrayList<>();
        Collection<MyDirectEdge> edges = MyDirectGraphVars.directGraph.getEdges();
        int count = 0;

        for (MyDirectEdge e : edges) {
            if (e.getDest() != MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode) continue;
            if (count < MyDirectGraphVars.BAR_CHART_RECORD_LIMIT) {
                String name = e.getSource().getName();
                this.predecessorData.put(name, e.getCurrentValue());
                final float hue = this.rand.nextFloat();
                final float saturation = 0.9f;
                final float luminance = 1.0f;
                this.colors.add(Color.getHSBColor(hue, saturation, luminance));
                if (this.predecessorMaxVal < e.getCurrentValue()) {
                    this.predecessorMaxVal = e.getCurrentValue();
                }
            }
            count++;
        }

        count = 0;
        for (MyDirectEdge e : edges) {
            if (e.getSource() != MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode) continue;
            if (count < MyDirectGraphVars.BAR_CHART_RECORD_LIMIT) {
                String name = e.getDest().getName();
                this.successorData.put(name, e.getCurrentValue());
                final float hue = this.rand.nextFloat();
                final float saturation = 0.9f;
                final float luminance = 1.0f;
                this.colors.add(Color.getHSBColor(hue, saturation, luminance));
                if (this.successorMaxVal < e.getCurrentValue()) {
                    this.successorMaxVal = e.getCurrentValue();
                }
            }
            count++;
        }

        this.predecessorData = MyDirectGraphSysUtil.sortMapByFloatValue(this.predecessorData);
        this.successorData = MyDirectGraphSysUtil.sortMapByFloatValue(this.successorData);

        setBounds(8, 5, 600, 2000);
        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0.0f));
    }

    private LinkedHashMap<String, Float> predecessorData;
    private LinkedHashMap<String, Float> successorData;

    public void setEdgeValueBarChart() {
        this.edgeData = new LinkedHashMap<>();
        this.colors = new ArrayList<>();
        Collection<MyDirectEdge> edges = MyDirectGraphVars.directGraph.getEdges();

        for (MyDirectEdge e : edges) {
            if (e.getCurrentValue() <= 0) continue;
            String name = e.getSource().getName() + "-" + e.getDest().getName();
            this.edgeData.put(name, e.getCurrentValue());
            if (this.maxVal < e.getCurrentValue()) {
                this.maxVal = e.getCurrentValue();
            }
            final float hue = this.rand.nextFloat();
            final float saturation = 0.9f;
            final float luminance = 1.0f;
            this.colors.add(Color.getHSBColor(hue, saturation, luminance));
        }

        this.edgeData = MyDirectGraphSysUtil.sortMapByFloatValue(this.edgeData);
        setBounds(8, 5, 600, 2000);
        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0.0f));
    }

    private void setMaximumValue() {
        this.maxVal = 0;
        for (Float value : this.edgeData.values()) {
            if (this.maxVal < value) {this.maxVal = value;}
        }
    }

    private boolean showBoth = false;

    @Override public void paint(Graphics g) {
        if (showBoth) { // Show predecessor and successor edges.
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

            int i = 0;
            g.setFont(MyDirectGraphVars.tahomaBoldFont12);
            g.setColor(Color.DARK_GRAY);
            g.drawString("PREDECESSOR EDGE VALUES", 0, (10 * (i + 1)) + ((i * this.gap)) - 1);
            i++;

            int nodeCnt = 0;
            for (String name : this.predecessorData.keySet()) {
                g.setColor(this.colors.get(nodeCnt++));
                float valuePortion = this.predecessorData.get(name) / this.predecessorMaxVal;
                valuePortion = BAR_WIDTH * valuePortion;
                g.fillRect(0, (10 * i) + (i * this.gap), (int) valuePortion, 10);

                g.setColor(Color.BLUE);
                g.drawRect(0, (10 * i) + (i * this.gap), (int) valuePortion, 10);

                String value = MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(this.predecessorData.get(name)));
                value = (value.endsWith(".0") || value.endsWith(".00") ? value.substring(0, value.indexOf(".0")) : value);
                g.setFont(MyDirectGraphVars.f_pln_12);
                g.setColor(Color.DARK_GRAY);
                g.drawString(name + "[" + value + "]", ((int) valuePortion) + 5, (10 * (i + 1)) + ((i * this.gap) - 2));
                i++;
            }

            nodeCnt = 0;
            g.setFont(MyDirectGraphVars.tahomaBoldFont12);
            g.setColor(Color.DARK_GRAY);
            g.drawString("", 0, (10 * (i + 1)) + ((i * this.gap)) - 1); i++;
            g.drawString("SUCCESSOR EDGE VALUES", 0, (10 * (i + 1)) + ((i * this.gap)) - 2); i++;
            for (String name : this.successorData.keySet()) {
                g.setColor(this.colors.get(nodeCnt++));
                float valuePortion = this.successorData.get(name) / this.successorMaxVal;
                valuePortion = BAR_WIDTH * valuePortion;
                g.fillRect(0, (10 * i) + (i * this.gap), (int) valuePortion, 9);

                g.setColor(Color.BLUE);
                g.drawRect(0, (10 * i) + (i * this.gap), (int) valuePortion, 9);

                String value = MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(this.successorData.get(name)));
                value = (value.endsWith(".0") || value.endsWith(".00") ? value.substring(0, value.indexOf(".0")) : value);
                g.setFont(MyDirectGraphVars.f_pln_12);
                g.setColor(Color.DARK_GRAY);
                g.drawString(name + "[" + value + "]", ((int) valuePortion) + 5, (10 * (i + 1)) + ((i * this.gap) - 2));
                i++;
            }
        } else {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

            int i = 0;
            g.setFont(MyDirectGraphVars.tahomaBoldFont12);
            g.setColor(Color.DARK_GRAY);
            g.drawString("EDGE VALUES", 0, (10 * (i + 1)) + ((i * this.gap)) - 1);
            i++;

            int nodeCnt = 0;
            for (String name : this.edgeData.keySet()) {
                g.setColor(this.colors.get(nodeCnt++));
                float valuePortion = this.edgeData.get(name) / this.maxVal;
                valuePortion = BAR_WIDTH * valuePortion;
                g.fillRect(0, (10 * i) + (i * this.gap), (int) valuePortion, 10);

                g.setColor(Color.BLUE);
                g.drawRect(0, (10 * i) + (i * this.gap), (int) valuePortion, 10);

                String value = MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(this.edgeData.get(name)));
                value = (value.endsWith(".0") || value.endsWith(".00") ? value.substring(0, value.indexOf(".0")) : value);
                g.setFont(MyDirectGraphVars.f_pln_12);
                g.setColor(Color.DARK_GRAY);
                g.drawString(name + "[" + value + "]", ((int) valuePortion) + 5, (10 * (i + 1)) + ((i * this.gap) - 2));
                i++;
            }
        }
    }

    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(this.img ==null) {
            this.img =createImage(getWidth(), getHeight());
            this.graph = this.img.getGraphics();
        }
        this.graph.setColor(getBackground());
        this.graph.fillRect(0, 0, getWidth(), getHeight());
        this.graph.setColor(getForeground());
        paint(this.graph);
        g.drawImage(this.img,0,0,this);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Bar Chart");
        frame.setPreferredSize(new Dimension(500, 200));

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