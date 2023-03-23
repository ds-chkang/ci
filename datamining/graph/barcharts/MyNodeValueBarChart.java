package datamining.graph.barcharts;

import datamining.graph.MyDirectNode;
import datamining.utils.MyMathUtil;
import datamining.utils.system.MySysUtil;
import datamining.utils.system.MyVars;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Random;

public class MyNodeValueBarChart
extends JPanel {

    private Image img;
    private Graphics graph;
    private final int BAR_WIDTH = 50;
    private LinkedHashMap<String, Float> data;
    private float maxVal = 0.00f;
    private Random rand = new Random();
    private boolean isMaxValueSet = false;
    private int gap = 2;
    private ArrayList<Color> colors;

    public MyNodeValueBarChart() {
        this.setNodeValueBarChart();
    }

    public void setNodeValueBarChart() {
        this.isMaxValueSet = false;
        this.data = new LinkedHashMap<>();
        this.colors = new ArrayList<>();
        Collection<MyDirectNode> nodes = MyVars.directMarkovChain.getVertices();
        for (MyDirectNode n : nodes) {
            this.data.put(n.getName(), n.getCurrentValue());
            final float hue = this.rand.nextFloat();
            final float saturation = 0.9f;
            final float luminance = 1.0f;
            this.colors.add(Color.getHSBColor(hue, saturation, luminance));
        }
        this.data = MySysUtil.sortMapByFloatValue(this.data);
        this.setMaximumValue();
        setBounds(((MyVars.main.getWidth() > 1800) ? MyVars.main.getWidth()-964 : MyVars.main.getWidth()-890), 5, 515, 2000);
        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0.0f));
        this.isMaxValueSet = true;
    }

    private void setMaximumValue() {
        this.maxVal = 0;
        for (Float value : this.data.values()) {
            if (this.maxVal < value) {
                this.maxVal = value;
            }
        }
    }

    @Override public void paint(Graphics g) {
        if (!this.isMaxValueSet) return;
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        FontMetrics fontMetrics = g.getFontMetrics(MyVars.f_pln_11);
        int i=0;
        g.setFont(MyVars.tahomaBoldFont10);
        g.setColor(Color.DARK_GRAY);
        g.drawString("NODE VALUES", 415, (9*(i+1))+((i*this.gap))-1);

        i++;
        int nodeCnt = 0;
        for (String name : this.data.keySet()) {
            if (nodeCnt < 100) {
                g.setColor(this.colors.get(nodeCnt++));
                float valuePortion = this.data.get(name)/this.maxVal;
                float BAR_WIDTH_TO_DRAW = this.BAR_WIDTH*valuePortion;
                int xPos = (437+(this.BAR_WIDTH-(int)BAR_WIDTH_TO_DRAW));
                g.fillRect(xPos-1, (9*i)+(i*this.gap), (int)BAR_WIDTH_TO_DRAW, 9);
                g.setColor(Color.BLUE);
                g.drawRect(xPos-2, (9*i)+(i*this.gap), (int)BAR_WIDTH_TO_DRAW+1, 9);
                String value = MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(this.data.get(name)));
                value = (value.endsWith(".0") || value.endsWith(".00") ? value.substring(0, value.indexOf(".0")) : value);
                value = name + "[" + value + "]";
                int nameWritingPos = (xPos-4) -fontMetrics.stringWidth(value);
                g.setFont(MyVars.f_pln_11);
                g.setColor(Color.DARK_GRAY);
                g.drawString(value, nameWritingPos, (9*(i+1))+((i*this.gap)-2));
                i++;
            }
        }
    }

    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(img ==null) {
            img =createImage(getWidth(), getHeight());
            graph= img.getGraphics();
        }
        graph.setColor(getBackground());
        graph.fillRect(0, 0, getWidth(), getHeight());
        graph.setColor(getForeground());
        paint(graph);
        g.drawImage(img,0,0,this);
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