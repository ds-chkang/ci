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

public class MyDirectGraphNeighborNodeValueBarChart
extends JPanel {

    private Image i;
    private Graphics graph;
    private final int BAR_WIDTH = 60;
    private LinkedHashMap<String, Float> predecessors;
    private LinkedHashMap<String, Float> successors;
    private float predecessorMaxVal = 0.00f;
    private float successorMaxVal = 0.00f;
    private Random rand = new Random();
    private boolean isMaxValueSet = false;
    private int gap = 2;
    private ArrayList<Color> predecessorColors;
    private ArrayList<Color> successorColors;
    int barViewerWidth;

    public MyDirectGraphNeighborNodeValueBarChart() {
        if (!MyDirectGraphVars.getDirectGraphViewer().isPredecessorOnly &&
            !MyDirectGraphVars.getDirectGraphViewer().isSuccessorOnly &&
            !MyDirectGraphVars.getDirectGraphViewer().isSharedPredecessorOnly &&
            !MyDirectGraphVars.getDirectGraphViewer().isSharedPredecessorOnly) {
            barViewerWidth = MyDirectGraphVars.getDirectGraphViewer().getWidth() - 500;
            setBounds(barViewerWidth, 5, 500, 2000);
            setNeighborNodeValueBarcharts();
        }
    }

    public void setPredecessorValueBarChartOnly() {
        this.isMaxValueSet = false;
        this.successors = new LinkedHashMap<>();
        this.predecessors = new LinkedHashMap<>();
        this.predecessorColors = new ArrayList<>();
        Collection<MyDirectEdge> edges = MyDirectGraphVars.directGraph.getInEdges(MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode);
        for (MyDirectEdge e : edges) {
            this.predecessors.put(e.getSource().getName(), e.getCurrentValue());
            final float hue = this.rand.nextFloat();
            final float saturation = 0.9f;
            final float luminance = 1.0f;
            Color predecessorRandomColor = Color.getHSBColor(hue, saturation, luminance);
            this.predecessorColors.add(predecessorRandomColor);
        }
        this.predecessors = MyDirectGraphSysUtil.sortMapByFloatValue(this.predecessors);
        this.setPredecessorMaximumValue();
        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0.0f));
        this.isMaxValueSet = true;
    }

    public void setSuccessorValueBarChartOnly() {
        this.isMaxValueSet = false;
        this.successors = new LinkedHashMap<>();
        this.predecessors = new LinkedHashMap<>();
        this.successorColors = new ArrayList<>();
        Collection<MyDirectEdge> edges = MyDirectGraphVars.directGraph.getOutEdges(MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode);
        for (MyDirectEdge e : edges) {
            this.successors.put(e.getSource().getName(), e.getCurrentValue());
            final float hue = this.rand.nextFloat();
            final float saturation = 0.9f;
            final float luminance = 1.0f;
            Color predecessorRandomColor = Color.getHSBColor(hue, saturation, luminance);
            this.successorColors.add(predecessorRandomColor);
        }
        this.successors = MyDirectGraphSysUtil.sortMapByFloatValue(this.successors);
        this.setSuccessorMaximumValue();
        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0.0f));
        this.isMaxValueSet = true;
    }

    public void setNeighborNodeValueBarcharts() {
        this.isMaxValueSet = false;
        this.predecessors = new LinkedHashMap<>();
        this.successors = new LinkedHashMap<>();
        this.predecessorColors = new ArrayList<>();
        this.successorColors = new ArrayList<>();
        Collection<MyDirectEdge> edges = MyDirectGraphVars.directGraph.getInEdges(MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode);
        for (MyDirectEdge e : edges) {
            this.predecessors.put(e.getSource().getName(), (e.getCurrentValue() == -1 ? e.getSource().getContribution() : e.getCurrentValue()));
            final float hue = this.rand.nextFloat();
            final float saturation = 0.9f;
            final float luminance = 1.0f;
            Color predecessorRandomColor = Color.getHSBColor(hue, saturation, luminance);
            this.predecessorColors.add(predecessorRandomColor);
        }

        edges = MyDirectGraphVars.directGraph.getOutEdges(MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode);
        for (MyDirectEdge e : edges) {
            this.successors.put(e.getDest().getName(), (e.getCurrentValue() == -1 ? e.getDest().getContribution() : e.getCurrentValue()));
            final float hue = this.rand.nextFloat();
            final float saturation = 0.9f;
            final float luminance = 1.0f;
            Color successorRandomColor = Color.getHSBColor(hue, saturation, luminance);
            this.successorColors.add(successorRandomColor);
        }

        this.predecessors = MyDirectGraphSysUtil.sortMapByFloatValue(this.predecessors);
        this.successors = MyDirectGraphSysUtil.sortMapByFloatValue(this.successors);
        this.setPredecessorMaximumValue();
        this.setSuccessorMaximumValue();
        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0.0f));
        this.isMaxValueSet = true;
    }

    private void setPredecessorMaximumValue() {
        this.predecessorMaxVal = 0;
        for (Float value : this.predecessors.values()) {
            if (this.predecessorMaxVal < value) {
                this.predecessorMaxVal = value;
            }
        }
    }

    private void setSuccessorMaximumValue() {
        this.successorMaxVal = 0;
        for (Float value : this.successors.values()) {
            if (this.successorMaxVal < value) {
                this.successorMaxVal = value;
            }
        }
    }

    @Override public void paint(Graphics g) {
        if (!this.isMaxValueSet) return;
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        FontMetrics fontMetrics = g.getFontMetrics(MyDirectGraphVars.f_pln_12);
        int i=0;

        if (this.predecessors.size() > 0 && this.successors.size() == 0) {
            g.setFont(MyDirectGraphVars.tahomaBoldFont12);
            g.setColor(Color.DARK_GRAY);

            int titleStringWidth = fontMetrics.stringWidth("PREDECESSOR VALUES");
            g.drawString("PREDECESSOR VALUES", (this.getWidth()-(titleStringWidth)), ((10*(i+1))+(i*gap)));
            i++;

            if (this.predecessorMaxVal == 0) {
                int predecessorIdx = 0;
                for (String name : this.predecessors.keySet()) {
                    if (predecessorIdx < MyDirectGraphVars.BAR_CHART_RECORD_LIMIT * 6) {
                        g.setColor(this.predecessorColors.get(predecessorIdx++));
                        float valuePortion = this.predecessors.get(name)/this.predecessorMaxVal;
                        float BAR_WIDTH_TO_DRAW = this.BAR_WIDTH*valuePortion;
                        int xPos = (this.getWidth()-((int)BAR_WIDTH_TO_DRAW+1));
                        g.fillRect(xPos-1, (10*i)+(i*this.gap)+1, (int)BAR_WIDTH_TO_DRAW, 10);
                        g.setColor(Color.BLUE);
                        g.drawRect(xPos-2, (10*i)+(i*this.gap)+1, (int)BAR_WIDTH_TO_DRAW+1, 10);
                        String value = MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(this.predecessors.get(name)));
                        value = (value.endsWith(".0") || value.endsWith(".00") ? value.substring(0, value.indexOf(".0")) : value);
                        value = name + "[" + value + "]";
                        int nameWritingPos = (xPos-10) -fontMetrics.stringWidth(value);
                        g.setFont(MyDirectGraphVars.f_pln_12);
                        g.setColor(Color.DARK_GRAY);
                        g.drawString(value, nameWritingPos+4, (10*(i+1))+((i*this.gap)+1));
                    }
                }
            } else {
                int predecessorIdx = 0;
                for (String name : this.predecessors.keySet()) {
                    if ( predecessorIdx < MyDirectGraphVars.BAR_CHART_RECORD_LIMIT * 6) {
                        g.setColor(this.predecessorColors.get(predecessorIdx++));
                        float valuePortion = this.predecessors.get(name)/this.predecessorMaxVal;
                        float BAR_WIDTH_TO_DRAW = this.BAR_WIDTH*valuePortion;
                        int xPos = (this.getWidth()-((int)BAR_WIDTH_TO_DRAW+1));
                        g.fillRect(xPos-1, (10*i)+(i*this.gap)+1, (int)BAR_WIDTH_TO_DRAW, 10);
                        g.setColor(Color.BLUE);
                        g.drawRect(xPos-2, (10*i)+(i*this.gap)+1, (int)BAR_WIDTH_TO_DRAW+1, 10);
                        String value = MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(this.predecessors.get(name)));
                        value = (value.endsWith(".0") || value.endsWith(".00") ? value.substring(0, value.indexOf(".0")) : value);
                        value = name + "[" + value + "]";
                        int nameWritingPos = (xPos-10) -fontMetrics.stringWidth(value);
                        g.setFont(MyDirectGraphVars.f_pln_12);
                        g.setColor(Color.DARK_GRAY);
                        g.drawString(value, nameWritingPos+4, (10*(i+1))+((i*this.gap)+1));
                        i++;
                    }
                }
            }
        } else if (this.predecessors.size() == 0 && this.successors.size() > 0) {
            g.setFont(MyDirectGraphVars.tahomaBoldFont12);
            g.setColor(Color.DARK_GRAY);

            int titleStringWidth = fontMetrics.stringWidth("SUCCESSOR VALUES");
            g.drawString("SUCCESSOR VALUES", (this.getWidth()-(titleStringWidth)), (10*(i+1))+((i*gap)));
            i++;

            if (this.successorMaxVal == 0) {
                int successorIdx = 0;
                for (String name : this.successors.keySet()) {
                    if (successorIdx < MyDirectGraphVars.BAR_CHART_RECORD_LIMIT * 6) {
                        g.setColor(this.successorColors.get(successorIdx++));
                        float valuePortion = this.successors.get(name)/this.successorMaxVal;
                        float BAR_WIDTH_TO_DRAW = this.BAR_WIDTH*valuePortion;
                        int xPos = (this.getWidth()-((int)BAR_WIDTH_TO_DRAW+1));
                        g.fillRect(xPos-1, (10*i)+(i*this.gap)+1, (int)BAR_WIDTH_TO_DRAW, 10);
                        g.setColor(Color.BLUE);
                        g.drawRect(xPos-2, (10*i)+(i*this.gap)+1, (int)BAR_WIDTH_TO_DRAW+1, 10);
                        String value = MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(this.successors.get(name)));
                        value = (value.endsWith(".0") || value.endsWith(".00") ? value.substring(0, value.indexOf(".0")) : value);
                        value = name + "[" + value + "]";
                        int nameWritingPos = (xPos-10) -fontMetrics.stringWidth(value);
                        g.setFont(MyDirectGraphVars.f_pln_12);
                        g.setColor(Color.DARK_GRAY);
                        g.drawString(value, nameWritingPos+4, (10*(i+1))+((i*this.gap)+1));
                        i++;
                    }
                }
            } else {
                int successorIdx = 0;
                for (String name : this.successors.keySet()) {
                    if (successorIdx < MyDirectGraphVars.BAR_CHART_RECORD_LIMIT * 6) {
                        g.setColor(this.successorColors.get(successorIdx++));
                        float valuePortion = this.successors.get(name)/this.successorMaxVal;
                        float BAR_WIDTH_TO_DRAW = this.BAR_WIDTH*valuePortion;
                        int xPos = (this.getWidth()-((int)BAR_WIDTH_TO_DRAW+1));
                        g.fillRect(xPos-1, (10*i)+(i*this.gap)+1, (int)BAR_WIDTH_TO_DRAW, 10);
                        g.setColor(Color.BLUE);
                        g.drawRect(xPos-2, (10*i)+(i*this.gap)+1, (int)BAR_WIDTH_TO_DRAW+1, 10);
                        String value = MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(this.successors.get(name)));
                        value = (value.endsWith(".0") || value.endsWith(".00") ? value.substring(0, value.indexOf(".0")) : value);
                        value = name + "[" + value + "]";
                        int nameWritingPos = (xPos-10) -fontMetrics.stringWidth(value);
                        g.setFont(MyDirectGraphVars.f_pln_12);
                        g.setColor(Color.DARK_GRAY);
                        g.drawString(value, nameWritingPos+4, (10*(i+1))+((i*this.gap)+1));
                        i++;
                    }
                }
            }
        } else if (this.predecessors.size() > 0 && this.successors.size() > 0) {
            g.setFont(MyDirectGraphVars.tahomaBoldFont12);
            g.setColor(Color.DARK_GRAY);

            int titleStringWidth = fontMetrics.stringWidth("PREDECESSOR VALUES");
            g.drawString("PREDECESSOR VALUES",  (this.getWidth()-(titleStringWidth)), (10*(i+1))+((i*gap)));
            i++;

            int predecessorIdx = 0;
            for (String name : this.predecessors.keySet()) {
                if (predecessorIdx < MyDirectGraphVars.BAR_CHART_RECORD_LIMIT) {
                    g.setColor(this.predecessorColors.get(predecessorIdx++));
                    float valuePortion = this.predecessors.get(name)/this.predecessorMaxVal;
                    float BAR_WIDTH_TO_DRAW = this.BAR_WIDTH*valuePortion;
                    int xPos = (this.getWidth()-((int)BAR_WIDTH_TO_DRAW+1));
                    g.fillRect(xPos-1, (10*i)+(i*this.gap)+1, (int)BAR_WIDTH_TO_DRAW, 10);
                    g.setColor(Color.BLUE);
                    g.drawRect(xPos-2, (10*i)+(i*this.gap)+1, (int)BAR_WIDTH_TO_DRAW+1, 10);
                    String value = MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(this.predecessors.get(name)));
                    value = (value.endsWith(".0") || value.endsWith(".00") ? value.substring(0, value.indexOf(".0")) : value);
                    value = name + "[" + value + "]";
                    int nameWritingPos = (xPos-10) -fontMetrics.stringWidth(value);
                    g.setFont(MyDirectGraphVars.f_pln_12);
                    g.setColor(Color.DARK_GRAY);
                    g.drawString(value, nameWritingPos+4, (10*(i+1))+((i*this.gap)+1));
                    i++;
                }
            }

            g.setFont(MyDirectGraphVars.tahomaBoldFont12);
            g.setColor(Color.DARK_GRAY);
            g.drawString("", 5, (9*(i+1))+((i*this.gap)-2));
            i++;

            g.setFont(MyDirectGraphVars.tahomaBoldFont12);
            g.setColor(Color.DARK_GRAY);

            titleStringWidth = fontMetrics.stringWidth("SUCCESSOR VALUES");
            g.drawString("SUCCESSOR VALUES",  (this.getWidth()-(titleStringWidth)), (10*(i+1))+((i*gap)));
            i++;

            int successorIdx = 0;
            for (String name : this.successors.keySet()) {
                if (successorIdx < MyDirectGraphVars.BAR_CHART_RECORD_LIMIT) {
                    g.setColor(this.successorColors.get(successorIdx++));
                    float valuePortion = this.successors.get(name)/this.successorMaxVal;
                    float BAR_WIDTH_TO_DRAW = this.BAR_WIDTH*valuePortion;
                    int xPos = (this.getWidth()-((int)BAR_WIDTH_TO_DRAW+1));
                    g.fillRect(xPos-1, (10*i)+(i*this.gap)+1, (int)BAR_WIDTH_TO_DRAW, 10);
                    g.setColor(Color.BLUE);
                    g.drawRect(xPos-2, (10*i)+(i*this.gap)+1, (int)BAR_WIDTH_TO_DRAW+1, 10);
                    String value = MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(this.successors.get(name)));
                    value = (value.endsWith(".0") || value.endsWith(".00") ? value.substring(0, value.indexOf(".0")) : value);
                    value = name + "[" + value + "]";
                    int nameWritingPos = (xPos-10) -fontMetrics.stringWidth(value);
                    g.setFont(MyDirectGraphVars.f_pln_12);
                    g.setColor(Color.DARK_GRAY);
                    g.drawString(value, nameWritingPos+4, (10*(i+1))+((i*this.gap)+1));
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