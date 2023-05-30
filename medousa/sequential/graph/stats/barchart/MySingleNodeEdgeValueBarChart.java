package medousa.sequential.graph.stats.barchart;

import medousa.sequential.graph.MyEdge;
import medousa.sequential.utils.MyMathUtil;
import medousa.sequential.utils.MySequentialGraphSysUtil;
import medousa.sequential.utils.MySequentialGraphVars;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Random;

public class MySingleNodeEdgeValueBarChart
extends JPanel {

    private Image img;
    private Graphics graph;
    private LinkedHashMap<String, Float> data;
    private float maxVal = 0.00f;
    private Random rand = new Random();
    private boolean isMaxValueSet = false;
    private final int BAR_WIDTH = 65;
    private int gap = 2;
    private ArrayList<Color> colors;

    public MySingleNodeEdgeValueBarChart() {}

    public synchronized void setSingleNodeEdgeValueBarChart() {
        this.isMaxValueSet = false;
        this.data = new LinkedHashMap<>();
        this.colors = new ArrayList<>();
        Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
        for (MyEdge e : edges) {
            if (MySequentialGraphVars.getSequentialGraphViewer().edgeValName.contains("NONE")) {continue;}
            else if (e.getDest().getCurrentValue() ==0 || e.getSource().getCurrentValue() == 0) continue;
            else if (MySequentialGraphVars.getSequentialGraphViewer().singleNode != e.getSource() && MySequentialGraphVars.getSequentialGraphViewer().singleNode != e.getDest()) continue;
            String predecessor = (e.getSource().getName().contains("x") ? MySequentialGraphSysUtil.getDecodeVariableNodeName(e.getSource().getName()) : MySequentialGraphSysUtil.decodeNodeName(e.getSource().getName()));
            String successor = (e.getDest().getName().contains("x") ? MySequentialGraphSysUtil.getDecodeVariableNodeName(e.getDest().getName()) : MySequentialGraphSysUtil.decodeNodeName(e.getDest().getName()));
            String name = predecessor+"-"+successor;
           // System.out.println(name + "   " + e.getValue());
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

    private void setMaximumValue() {
        this.maxVal = 0;
        for (Float value : this.data.values()) {if (this.maxVal < value) {this.maxVal = value;}}
        this.isMaxValueSet = true;
    }

    @Override synchronized public void paint(Graphics g) {
        try {
            if (MySequentialGraphVars.getSequentialGraphViewer().edgeValName.contains("NONE")) {return;}
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            int i = 0;

                g.setFont(MySequentialGraphVars.tahomaBoldFont10);
                g.setColor(Color.DARK_GRAY);
                g.drawString("EDGE VALUES", 0, (9*(i+1))+((i*gap)));
                i++;

                int edgeCnt = 0;
                for (String name : this.data.keySet()) {
                    if (edgeCnt < MySequentialGraphVars.BAR_CHART_RECORD_LIMIT && this.data.get(name) > 0) {
                        g.setColor(this.colors.get(edgeCnt++));
                        float valuePortion = this.data.get(name)/this.maxVal;
                        valuePortion = this.BAR_WIDTH * valuePortion;
                        g.fillRect(0, (9*i)+(i*gap), (int) valuePortion, 9);

                        g.setColor(Color.BLUE);
                        g.drawRect(0, (9*i)+(i*gap), (int) valuePortion, 9);

                        String value = MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(this.data.get(name)));
                        value = (value.endsWith(".0") || value.endsWith(".00") ? value.substring(0, value.indexOf(".0")) : value);
                        g.setFont(MySequentialGraphVars.f_pln_10);
                        g.setColor(Color.DARK_GRAY);
                        g.drawString(name + "[" + value + "]", ((int)valuePortion)+5, (9*(i+1)) + ((i*gap)-2));
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