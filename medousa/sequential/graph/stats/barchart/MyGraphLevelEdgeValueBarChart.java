package medousa.sequential.graph.stats.barchart;

import medousa.sequential.graph.MyEdge;
import medousa.sequential.utils.MyMathUtil;
import medousa.sequential.utils.MySequentialGraphSysUtil;
import medousa.sequential.utils.MySequentialGraphVars;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class MyGraphLevelEdgeValueBarChart
extends JPanel {

    private Image img;
    private Graphics graph;
    private LinkedHashMap<String, Float> data;
    private float maxVal = 0.00f;
    private Random rand = new Random();
    private boolean isMaxValueSet = false;
    private final int BAR_WIDTH = 60;
    private int gap = 2;
    private ArrayList<Color> colors;

    public MyGraphLevelEdgeValueBarChart() {}

    public void setEdgeValueBarChartForDepthNodes() {
        this.isMaxValueSet = false;
        this.data = new LinkedHashMap<>();
        this.colors = new ArrayList<>();
        Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
        float edgeMaxValue = 0f;
        if (MySequentialGraphVars.getSequentialGraphViewer().vc.depthNeighborNodeTypeSelector.getSelectedItem().toString().contains("S.")) {
            Map<String, Map<String, Long>> depthNodeSuccessorMaps = MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeSuccessorMaps;
            for (MyEdge e : edges) {
                if (e.getCurrentValue() == 0) continue;
                if (depthNodeSuccessorMaps.keySet().contains(e.getSource().getName())) {
                    for (String depthNode : depthNodeSuccessorMaps.keySet()) {
                        Map<String, Long> successorSet = depthNodeSuccessorMaps.get(depthNode);
                        if (successorSet.containsKey(e.getDest().getName())) {
                            String predecessor = MySequentialGraphSysUtil.getNodeName(e.getSource().getName());
                            String successor = MySequentialGraphSysUtil.getNodeName(e.getDest().getName());
                            String name = predecessor + "-" + successor;
                            this.data.put(name, (float) e.getDest().getNodeDepthInfo(MySequentialGraphVars.currentGraphDepth+1).getInContribution());
                            e.setCurrentValue((float) e.getDest().getNodeDepthInfo(MySequentialGraphVars.currentGraphDepth+1).getInContribution());
                            if (e.getCurrentValue() > edgeMaxValue) {
                                edgeMaxValue = e.getCurrentValue();
                            }
                            final float hue = this.rand.nextFloat();
                            final float saturation = 0.9f;
                            final float luminance = 1.0f;
                            Color randomColor = Color.getHSBColor(hue, saturation, luminance);
                            this.colors.add(randomColor);
                        }
                    }
                }
            }
        } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.depthNeighborNodeTypeSelector.getSelectedItem().toString().contains("P.")) {
            Map<String, Map<String, Long>> depthNodePredecessorSetMap = MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodePredecessorMaps;
            for (MyEdge e : edges) {
                if (e.getCurrentValue() == 0) continue;
                if (depthNodePredecessorSetMap.keySet().contains(e.getDest().getName())) {
                    for (String depthNode : depthNodePredecessorSetMap.keySet()) {
                        Map<String, Long> predecessorMap = depthNodePredecessorSetMap.get(depthNode);
                        if (predecessorMap.containsKey(e.getSource().getName())) {
                            String predecessor = MySequentialGraphSysUtil.getNodeName(e.getSource().getName());
                            String successor = MySequentialGraphSysUtil.getNodeName(e.getDest().getName());
                            String name = predecessor + "-" + successor;
                            this.data.put(name, (float) e.getDest().getNodeDepthInfo(MySequentialGraphVars.currentGraphDepth -1).getInContribution());
                            e.setCurrentValue((float) e.getSource().getNodeDepthInfo(MySequentialGraphVars.currentGraphDepth -1).getInContribution());
                            if (e.getCurrentValue() > edgeMaxValue) {
                                edgeMaxValue = e.getCurrentValue();
                            }
                            final float hue = this.rand.nextFloat();
                            final float saturation = 0.9f;
                            final float luminance = 1.0f;
                            Color randomColor = Color.getHSBColor(hue, saturation, luminance);
                            this.colors.add(randomColor);
                        }
                    }
                }
            }
        }
        this.maxVal = edgeMaxValue;
        MySequentialGraphVars.g.MX_E_VAL = edgeMaxValue;
        this.data = MySequentialGraphSysUtil.sortMapByFloatValue(this.data);
        this.setMaximumValue();
        setBounds(8, 5, 700, 2000);
        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0.0f));
    }

    public void setEdgeValueBarChart() {
        this.isMaxValueSet = false;
        this.data = new LinkedHashMap<>();
        this.colors = new ArrayList<>();
        Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();

        for (MyEdge e : edges) {
            if (e.getCurrentValue() == 0) continue;
            if (MySequentialGraphVars.getSequentialGraphViewer().edgeValName.contains("NONE")) {continue;}
            String predecessor = MySequentialGraphSysUtil.getNodeName(e.getSource().getName());
            String successor = MySequentialGraphSysUtil.getNodeName(e.getDest().getName());
            String name = predecessor+"-"+successor;
            this.data.put(name, e.getCurrentValue());
            final float hue = this.rand.nextFloat();
            final float saturation = 0.9f;
            final float luminance = 1.0f;
            Color randomColor = Color.getHSBColor(hue, saturation, luminance);
            this.colors.add(randomColor);
        }

        this.data = MySequentialGraphSysUtil.sortMapByFloatValue(this.data);
        setBounds(8, 5, 700, 2000);
        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0.0f));
        this.setMaximumValue();
    }

    public void setEdgeValueBarChartForSelectedNode() {
        this.isMaxValueSet = false;
        this.data = new LinkedHashMap<>();
        this.colors = new ArrayList<>();
        Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
        for (MyEdge e : edges) {
            if (e.getCurrentValue() == 0) continue;
            if (e.getSource() != MySequentialGraphVars.getSequentialGraphViewer().singleNode && e.getDest() != MySequentialGraphVars.getSequentialGraphViewer().singleNode) continue;
            String predecessor = MySequentialGraphSysUtil.getNodeName(e.getSource().getName());
            String successor = MySequentialGraphSysUtil.getNodeName(e.getDest().getName());
            String name = predecessor+"-"+successor;
            this.data.put(name, e.getCurrentValue());
            final float hue = this.rand.nextFloat();
            final float saturation = 0.9f;
            final float luminance = 1.0f;
            Color randomColor = Color.getHSBColor(hue, saturation, luminance);
            this.colors.add(randomColor);
        }

        this.data = MySequentialGraphSysUtil.sortMapByFloatValue(this.data);
        this.setMaximumValue();
        setBounds(8, 5, 700, 2000);
        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0.0f));
    }

    private void setMaximumValue() {
        this.maxVal = 0;
        for (Float value : this.data.values()) {if (this.maxVal < value) {this.maxVal = value;}}
        this.isMaxValueSet = true;
    }

    @Override public void paint(Graphics g) {
        try {
            if (MySequentialGraphVars.getSequentialGraphViewer().edgeValName.contains("NONE")) {return;}
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            int i = 0;

                g.setFont(MySequentialGraphVars.tahomaBoldFont12);
                g.setColor(Color.DARK_GRAY);
                g.drawString("EDGE VALUES", 0, (10*(i+1))+((i*gap)));
                i++;

                int edgeCnt = 0;
                for (String name : this.data.keySet()) {
                    if (this.data.get(name) > 0) {
                        g.setColor(this.colors.get(edgeCnt++));
                        float valuePortion = this.data.get(name) / this.maxVal;
                        valuePortion = this.BAR_WIDTH * valuePortion;
                        g.fillRect(0, (10*i)+(i*gap), (int) valuePortion, 10);

                        g.setColor(Color.BLUE);
                        g.drawRect(0, (10*i)+(i*gap), (int) valuePortion, 10);

                        String value = MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(this.data.get(name)));
                        value = (value.endsWith(".0") || value.endsWith(".00") ? value.substring(0, value.indexOf(".0")) : value);
                        g.setFont(MySequentialGraphVars.f_pln_12);
                        g.setColor(Color.DARK_GRAY);
                        g.drawString(name + "[" + value + "]", ((int)valuePortion)+5, (10*(i+1)) + ((i*gap)-2));
                        i++;
                    }
                }

        } catch (Exception ex) {ex.printStackTrace();}
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