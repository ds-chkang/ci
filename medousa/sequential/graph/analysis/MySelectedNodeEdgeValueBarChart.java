package medousa.sequential.graph.analysis;

import medousa.sequential.graph.MyEdge;
import medousa.sequential.graph.MyNode;
import medousa.sequential.utils.MyMathUtil;
import medousa.sequential.utils.MySequentialGraphSysUtil;
import medousa.sequential.utils.MySequentialGraphVars;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class MySelectedNodeEdgeValueBarChart
extends JPanel {

    private Image img;
    private Graphics graph;
    private LinkedHashMap<String, Long> predecessors;
    private LinkedHashMap<String, Long> successors;
    private float predecessorMaxVal = 0.00f;
    private float successorMaxVal = 0.00f;
    private Random rand = new Random();
    private final int BAR_WIDTH = 50;
    private int gap = 2;
    private ArrayList<Color> predecessorColors;
    private ArrayList<Color> successorColors;
    private int BAR_DISPLAY_LIMIT = 40;
    private MyGraphAnalyzerViewer graphAnalyzerViewer;

    public MySelectedNodeEdgeValueBarChart(MyGraphAnalyzerViewer graphAnalyzerViewer) {
        this.graphAnalyzerViewer = graphAnalyzerViewer;
    }

    public synchronized void setEdgeValueBarChartForSelectedNode() {
        removeAll();
        setBounds(8, 5, 700, 2000);
        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0.0f));

        successorMaxVal = 0;
        predecessorMaxVal = 0;
        this.predecessors = new LinkedHashMap<>();
        this.predecessorColors = new ArrayList<>();
        this.successors = new LinkedHashMap<>();
        this.successorColors = new ArrayList<>();
        Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
        if (graphAnalyzerViewer.graphMouseListener.selectedNode == null) return;
        for (MyEdge e : edges) {
            if (e.getSource() == graphAnalyzerViewer.graphMouseListener.selectedNode) {
                String successor = (e.getDest().getName().contains("x") ?
                    MySequentialGraphSysUtil.decodeVariable(e.getDest().getName()):
                    MySequentialGraphSysUtil.decodeNodeName(e.getDest().getName()));

                this.successors.put(successor, (long)e.getContribution());
                final float hue = this.rand.nextFloat();
                final float saturation = 0.9f;
                final float luminance = 1.0f;
                Color randomColor = Color.getHSBColor(hue, saturation, luminance);
                this.successorColors.add(randomColor);
            } else if (e.getDest() == graphAnalyzerViewer.graphMouseListener.selectedNode) {
                String predecessor = (e.getSource().getName().contains("x") ?
                        MySequentialGraphSysUtil.decodeVariable(e.getSource().getName()):
                        MySequentialGraphSysUtil.decodeNodeName(e.getSource().getName()));

                this.predecessors.put(predecessor, (long)e.getContribution());
                final float hue = this.rand.nextFloat();
                final float saturation = 0.9f;
                final float luminance = 1.0f;
                Color randomColor = Color.getHSBColor(hue, saturation, luminance);
                this.predecessorColors.add(randomColor);
            }
        }

        this.predecessors = MySequentialGraphSysUtil.sortMapByLongValue(this.predecessors);
        this.successors = MySequentialGraphSysUtil.sortMapByLongValue(this.successors);

        this.setMaximumValue();
    }

    private void setMaximumValue() {
        for (float value : this.predecessors.values()) {
            if (this.predecessorMaxVal < value) {
                this.predecessorMaxVal = value;
            }
        }

        for (float value : this.successors.values()) {
            if (this.successorMaxVal < value) {
                this.successorMaxVal = value;
            }
        }
    }

    @Override public synchronized void paint(Graphics g) {
        int i = 0;
        int edgeCnt = 0;

        if (this.predecessorMaxVal > 0) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

            g.setFont(MySequentialGraphVars.tahomaBoldFont12);
            g.setColor(Color.DARK_GRAY);
            g.drawString("PREDECESSORS", 0, (10*(i+1))+((i*gap)));
            i++;

            for (String predecessor : this.predecessors.keySet()) {
                if (BAR_DISPLAY_LIMIT > edgeCnt) {
                    g.setColor(this.predecessorColors.get(edgeCnt++));
                    float valuePortion = this.predecessors.get(predecessor)/this.predecessorMaxVal;
                    valuePortion = this.BAR_WIDTH * valuePortion;
                    g.fillRect(0, (10*i)+(i*gap), (int) valuePortion, 10);

                    g.setColor(Color.BLUE);
                    g.drawRect(0, (10*i)+(i*gap), (int) valuePortion, 10);

                    String cont = MyMathUtil.getCommaSeperatedNumber(predecessors.get(predecessor));
                    g.setFont(MySequentialGraphVars.f_pln_12);
                    g.setColor(Color.DARK_GRAY);
                    String value = "[" + cont + "]";
                    g.drawString(predecessor + value, ((int) valuePortion)+5, (10*(i+1))+((i*gap)-2));
                    i++;
                }
            }
        }

        if (this.successorMaxVal > 0) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            g.drawString("", 0, (10*(i+1))+((i++ * gap)));

            edgeCnt = 0;
            g.setFont(MySequentialGraphVars.tahomaBoldFont12);
            g.setColor(Color.DARK_GRAY);
            g.drawString("SUCCESSORS", 0, (10*(i+1))+((i*gap)));
            i++;

            for (String successor : this.successors.keySet()) {
                if (BAR_DISPLAY_LIMIT > edgeCnt) {
                    g.setColor(this.successorColors.get(edgeCnt++));
                    float valuePortion = this.successors.get(successor)/this.successorMaxVal;
                    valuePortion = this.BAR_WIDTH * valuePortion;
                    g.fillRect(0, (10*i)+(i*gap), (int) valuePortion, 10);

                    g.setColor(Color.BLUE);
                    g.drawRect(0, (10*i)+(i*gap), (int) valuePortion, 10);

                    String cont = MyMathUtil.getCommaSeperatedNumber(successors.get(successor));
                    g.setFont(MySequentialGraphVars.f_pln_12);
                    g.setColor(Color.DARK_GRAY);
                    String value = "[" + cont + "]";
                    g.drawString(successor + value, ((int) valuePortion)+5, (10*(i+1))+((i*gap)-2));
                    i++;
                }
            }
        }
    }

    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(this.img ==null) {
            this.img =createImage(getWidth(), getHeight());
            this.graph= this.img.getGraphics();
        }

        this.graph.setColor(Color.WHITE);
        this.graph.fillRect(0, 0, getWidth(), getHeight());
        this.graph.setColor(Color.WHITE);

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