package datamining.graph.stats.singlenode;

import datamining.graph.MyEdge;
import datamining.utils.MyMathUtil;
import datamining.utils.system.MySysUtil;
import datamining.utils.system.MyVars;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Random;

public class MySingleNodeNeighborNodeValueBarChart extends JPanel {

    private Image i;
    private final int BAR_WIDTH = 65;
    private Graphics graph;
    private LinkedHashMap<String, Float> predecessors;
    private LinkedHashMap<String, Float> successors;
    private float predecessorMaxVal = 0.00f;
    private float successorMaxVal = 0.00f;
    private Random rand = new Random();
    private boolean isMaxValueSet = false;
    private int gap = 2;
    private ArrayList<Color> predecessorColors;
    private ArrayList<Color> successorColors;


    public MySingleNodeNeighborNodeValueBarChart() {
        this.setNeighborNodeValueBarChart();
    }

    public synchronized void setNeighborNodeValueBarChart() {
        this.isMaxValueSet = false;
        this.predecessors = new LinkedHashMap<>();
        this.successors = new LinkedHashMap<>();
        this.predecessorColors = new ArrayList<>();
        this.successorColors = new ArrayList<>();

        Collection<MyEdge> edges = MyVars.g.getEdges();
        for (MyEdge e : edges) {
            if (e.getDest() == MyVars.getViewer().selectedNode && e.getCurrentValue() > 0) {
                String pName = (e.getSource().getName().contains("x") ? MySysUtil.decodeVariable(e.getSource().getName()) : MySysUtil.decodeNodeName(e.getSource().getName()));
                this.predecessors.put(pName, e.getSource().getCurrentValue());
                final float hue = this.rand.nextFloat();
                final float saturation = 0.9f;
                final float luminance = 1.0f;
                Color predecessorRandomColor = Color.getHSBColor(hue, saturation, luminance);
                this.predecessorColors.add(predecessorRandomColor);
            }

            if (e.getSource() == MyVars.getViewer().selectedNode && e.getCurrentValue() > 0) {
                String sName = (e.getDest().getName().contains("x") ? MySysUtil.decodeVariable(e.getDest().getName()) : MySysUtil.decodeNodeName(e.getDest().getName()));
                this.successors.put(sName, e.getDest().getCurrentValue());
                final float hue = this.rand.nextFloat();
                final float saturation = 0.9f;
                final float luminance = 1.0f;
                Color successorRandomColor = Color.getHSBColor(hue, saturation, luminance);
                this.successorColors.add(successorRandomColor);
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
        }

        this.setOpaque(false);
        this.setBackground(new Color(0,0,0,0.0f));
        this.setPredecessorMaximumValue();
        this.setSuccessorMaximumValue();
        this.isMaxValueSet = true;
    }

    public synchronized void setSuccessorDepthNodeValueBarChartForSelectedNode() {
        this.isMaxValueSet = false;
        this.predecessors = new LinkedHashMap<>();
        this.successors = new LinkedHashMap<>();
        this.predecessorColors = new ArrayList<>();
        this.successorColors = new ArrayList<>();

        for (String nn : MyVars.getViewer().vc.selectedNodeSuccessorDepthNodeMap.keySet()) {
            this.successors.put((nn.contains("x") ? MySysUtil.decodeVariable(nn) : MySysUtil.decodeNodeName(nn)), (float)MyVars.getViewer().vc.selectedNodeSuccessorDepthNodeMap.get(nn));
            final float hue = this.rand.nextFloat();
            final float saturation = 0.9f;
            final float luminance = 1.0f;
            Color successorRandomColor = Color.getHSBColor(hue, saturation, luminance);
            this.successorColors.add(successorRandomColor);
        }

        this.successors = MySysUtil.sortMapByFloatValue(this.successors);
        if (MySysUtil.getViewerWidth() < 1200) {
            setBounds(0, 0, 0, 0);
        } else {
            setBounds((MySysUtil.getViewerWidth() <= 1900 ?
                    MySysUtil.getViewerWidth() - (765 + MyVars.getViewer().vc.nodeListTable.getWidth()) :
                    MySysUtil.getViewerWidth() - (810 + MyVars.getViewer().vc.nodeListTable.getWidth())), 5, 600, 2000);
        }

        this.setOpaque(false);
        this.setBackground(new Color(0,0,0,0.0f));
        this.setSuccessorMaximumValue();
        this.isMaxValueSet = true;
    }

    public synchronized void setPredecessorDepthNodeValueBarChartForSelectedNode() {
        this.isMaxValueSet = false;
        this.predecessors = new LinkedHashMap<>();
        this.successors = new LinkedHashMap<>();
        this.predecessorColors = new ArrayList<>();
        this.successorColors = new ArrayList<>();

        for (String nn : MyVars.getViewer().vc.selectedNodePredecessorDepthNodeMap.keySet()) {
            this.predecessors.put((nn.contains("x") ? MySysUtil.decodeVariable(nn) : MySysUtil.decodeNodeName(nn)), (float)MyVars.getViewer().vc.selectedNodePredecessorDepthNodeMap.get(nn));
            final float hue = this.rand.nextFloat();
            final float saturation = 0.9f;
            final float luminance = 1.0f;
            Color predecessorRandomColor = Color.getHSBColor(hue, saturation, luminance);
            this.predecessorColors.add(predecessorRandomColor);
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
        FontMetrics fontMetrics = g.getFontMetrics(MyVars.f_pln_10);

        if (this.predecessors.size() > 0 && this.successors.size() == 0) {
            g.setFont(MyVars.tahomaBoldFont10);
            g.setColor(Color.DARK_GRAY);
            g.drawString("PREDECESSOR VALUES", 374, (9*(i+1))+((i*this.gap)));

            i++;
            int predecessorIdx = 0;
            for (String name : this.predecessors.keySet()) {
                if (predecessorIdx < (MyVars.BAR_CHART_RECORD_LIMIT+100)) {
                    g.setColor(this.predecessorColors.get(predecessorIdx++));
                    float valuePortion = this.predecessors.get(name)/this.predecessorMaxVal;
                    valuePortion = this.BAR_WIDTH *valuePortion;
                    g.fillRect(425, (9*i)+(i*this.gap), (int)valuePortion, 9);
                    g.setColor(Color.BLUE);
                    g.drawRect(425, (9*i)+(i*this.gap), (int)valuePortion, 9);
                    String value = MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(this.predecessors.get(name)));
                    value = (value.endsWith(".0") || value.endsWith(".00") ? value.substring(0, value.indexOf(".0")) : value);
                    value = name + "[" + value + "]";
                    int nameWritingPos = (424-fontMetrics.stringWidth(value));
                    g.setFont(MyVars.f_pln_10);
                    g.setColor(Color.DARK_GRAY);
                    g.drawString(value, nameWritingPos, (9*(i+1))+((i*this.gap)-2));
                    i++;
                }
            }
        } else if (this.predecessors.size() == 0 && this.successors.size() > 0) {
            g.setFont(MyVars.tahomaBoldFont10);
            g.setColor(Color.DARK_GRAY);
            g.drawString("SUCCESSOR VALUES", 387, (9*(i+1))+((i*this.gap)));
            i++;
            int successorIdx = 0;
            for (String name : this.successors.keySet()) {
                if (successorIdx < (MyVars.BAR_CHART_RECORD_LIMIT+100)) {
                    g.setColor(this.successorColors.get(successorIdx++));
                    float valuePortion = this.successors.get(name)/this.successorMaxVal;
                    valuePortion = this.BAR_WIDTH *valuePortion;
                    g.fillRect(425, (9*i)+(i*this.gap), (int)valuePortion, 9);
                    g.setColor(Color.BLUE);
                    g.drawRect(425, (9*i)+(i*this.gap), (int)valuePortion, 9);
                    String value = MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(this.successors.get(name)));
                    value = (value.endsWith(".0") || value.endsWith(".00") ? value.substring(0, value.indexOf(".0")) : value);
                    value = name + "[" + value + "]";
                    int nameWritingPos = (424-fontMetrics.stringWidth(value));
                    g.setFont(MyVars.f_pln_10);
                    g.setColor(Color.DARK_GRAY);
                    g.drawString(value, nameWritingPos, (9*(i+1))+((i*this.gap)-2));
                    i++;
                }
            }
        } else if (this.predecessors.size() > 0 && this.successors.size() > 0) {
            g.setFont(MyVars.tahomaBoldFont10);
            g.setColor(Color.DARK_GRAY);
            g.drawString("PREDECESSOR VALUES", 374, (9*(i+1))+((i*this.gap)));

            i++;
            int predecessorIdx = 0;
            for (String name : this.predecessors.keySet()) {
                if (predecessorIdx < MyVars.BAR_CHART_RECORD_LIMIT) {
                    g.setColor(this.predecessorColors.get(predecessorIdx++));
                    float valuePortion = this.predecessors.get(name)/this.predecessorMaxVal;
                    valuePortion = this.BAR_WIDTH *valuePortion;
                    g.fillRect(425, (9*i)+(i*this.gap), (int)valuePortion, 9);
                    g.setColor(Color.BLUE);
                    g.drawRect(425, (9*i)+(i*this.gap), (int)valuePortion, 9);
                    String value = MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(this.predecessors.get(name)));
                    value = (value.endsWith(".0") || value.endsWith(".00") ? value.substring(0, value.indexOf(".0")) : value);
                    value = name + "[" + value + "]";
                    int nameWritingPos = (424-fontMetrics.stringWidth(value));
                    g.setFont(MyVars.f_pln_10);
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
            g.drawString("SUCCESSOR VALUES", 387, (9*(i+1))+((i*this.gap)));
            i++;
            int successorIdx = 0;
            for (String name : this.successors.keySet()) {
                if (successorIdx < MyVars.BAR_CHART_RECORD_LIMIT) {
                    g.setColor(this.successorColors.get(successorIdx++));
                    float valuePortion = this.successors.get(name)/this.successorMaxVal;
                    valuePortion = this.BAR_WIDTH *valuePortion;
                    g.fillRect(425, (9*i)+(i*this.gap), (int)valuePortion, 9);
                    g.setColor(Color.BLUE);
                    g.drawRect(425, (9*i)+(i*this.gap), (int)valuePortion, 9);
                    String value = MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(this.successors.get(name)));
                    value = (value.endsWith(".0") || value.endsWith(".00") ? value.substring(0, value.indexOf(".0")) : value);
                    value = name + "[" + value + "]";
                    int nameWritingPos = (424-fontMetrics.stringWidth(value));
                    g.setFont(MyVars.f_pln_10);
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