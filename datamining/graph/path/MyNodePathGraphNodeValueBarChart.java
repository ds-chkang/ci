package datamining.graph.path;

import datamining.graph.layout.MyDirectedSparseMultigraph;
import datamining.graph.MyNode;
import datamining.utils.MyMathUtil;
import datamining.utils.system.MySysUtil;
import datamining.utils.system.MyVars;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Random;

public class MyNodePathGraphNodeValueBarChart extends JPanel {

    private Image img;
    private Graphics graph;
    private LinkedHashMap<String, Float> data;
    private float maxVal = 0.00f;
    private Random rand = new Random();
    private boolean isMaxValueSet = false;
    private int gap = 2;
    private final int BAR_WIDTH = 65;
    private ArrayList<Color> colors;
    private MyDepthFirstGraphPathSercher betweenPathGraphDepthFirstSearch;

    public MyNodePathGraphNodeValueBarChart(MyDepthFirstGraphPathSercher betweenPathGraphDepthFirstSearch) {
        this.betweenPathGraphDepthFirstSearch = betweenPathGraphDepthFirstSearch;
        this.setNodeValueBarChart();
    }

    public synchronized void setNodeValueBarChart() {
        this.isMaxValueSet = false;
        this.data = new LinkedHashMap<>();
        this.colors = new ArrayList<>();
        Collection<MyNode> nodes = ((MyDirectedSparseMultigraph)this.betweenPathGraphDepthFirstSearch.betweenPathGraphViewer.getGraphLayout().getGraph()).getVertices();
        for (MyNode n : nodes) {
            this.data.put(n.getName(), (float)n.getContribution());
            final float hue = this.rand.nextFloat();
            final float saturation = 0.9f;
            final float luminance = 1.0f;
            Color randomColor = Color.getHSBColor(hue, saturation, luminance);
            this.colors.add(randomColor);
        }
        this.data = MySysUtil.sortMapByFloatValue(this.data);
        this.setMaximumValue();
        int x =  this.betweenPathGraphDepthFirstSearch.getWidth()-990;
        setBounds(x, 5, 600, 2000);
        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0.0f));
        this.isMaxValueSet = true;
    }

    private void setMaximumValue() {
        this.maxVal = 0;
        for (Float value : this.data.values()) {if (this.maxVal < value) {this.maxVal = value;}}
    }

    @Override synchronized public void paint(Graphics g) {
        if (!this.isMaxValueSet || this.maxVal == 0) return;
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        int i=0;

        g.setFont(MyVars.tahomaBoldFont10);
        g.setColor(Color.DARK_GRAY);
        g.drawString("NODE VALUES", 420, (9*(i+1))+((i*this.gap)));
        i++;

        int nodeCnt = 0;
        FontMetrics fontMetrics = g.getFontMetrics(MyVars.f_pln_10);
        for (String name : this.data.keySet()) {
            if (this.data.get(name) > 0) {
                g.setColor(this.colors.get(nodeCnt++));
                float valuePortion = this.data.get(name)/this.maxVal;
                valuePortion = BAR_WIDTH*valuePortion;
                g.fillRect(425, (9*i)+(i*this.gap), (int)valuePortion, 9);

                g.setColor(Color.BLUE);
                g.drawRect(425, (9*i)+(i*this.gap), (int)valuePortion, 9);
                
                String value = MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(this.data.get(name)));
                value = (value.endsWith(".0") || value.endsWith(".00") ? value.substring(0, value.indexOf(".0")) : value);
                value = name+"["+value+"]";
                int nameWritingPos = (424-fontMetrics.stringWidth(value));

                g.setFont(MyVars.f_pln_10);
                g.setColor(Color.DARK_GRAY);
                g.drawString(value, nameWritingPos, (9*(i+1))+((i*this.gap)-2));
                i++;
            }
        }
    }

    @Override protected synchronized void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(this.img ==null) {
            this.img =createImage(getWidth(), getHeight());
            this.graph= this.img.getGraphics();
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