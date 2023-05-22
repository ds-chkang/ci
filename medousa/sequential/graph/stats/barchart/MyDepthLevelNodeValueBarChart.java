package medousa.sequential.graph.stats.barchart;

import medousa.direct.utils.MyDirectGraphVars;
import medousa.sequential.graph.MyNode;
import medousa.sequential.utils.MyMathUtil;
import medousa.sequential.utils.MySequentialGraphSysUtil;
import medousa.sequential.utils.MySequentialGraphVars;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Random;

public class MyDepthLevelNodeValueBarChart extends JPanel {

    private Image img;
    private Graphics graph;
    private LinkedHashMap<String, Float> data;
    private float maxVal = 0.00f;
    private final int BAR_WIDTH = 65;
    private Random rand = new Random();
    private boolean isMaxValueSet = false;
    private int gap = 2;
    private ArrayList<Color> colors;
    private int barViewerWidth;

    public MyDepthLevelNodeValueBarChart() {
        barViewerWidth = MySequentialGraphVars.getSequentialGraphViewer().getWidth()-500;
        setBounds(barViewerWidth, 5, 500, 2000);
    }

    public void setDepthNodeValueBarCharts() {
        this.isMaxValueSet = false;
        this.data = new LinkedHashMap<>();
        this.colors = new ArrayList<>();
        Collection<MyNode> nodes = MySequentialGraphVars.g.getDepthNodes();
        for (MyNode n : nodes) {
            String name = MySequentialGraphSysUtil.getNodeName(n.getName());
            this.data.put(name, n.getCurrentValue());
            final float hue = this.rand.nextFloat();
            final float saturation = 0.9f;
            final float luminance = 1.0f;
            Color randomColor = Color.getHSBColor(hue, saturation, luminance);
            this.colors.add(randomColor);
        }
        this.data = MySequentialGraphSysUtil.sortMapByFloatValue(this.data);
        setMaximumValue();
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
        if (!this.isMaxValueSet || this.maxVal == 0) return;
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        FontMetrics fontMetrics = g.getFontMetrics(MySequentialGraphVars.f_pln_12);
        int i=0;
        int titleStringWidth = fontMetrics.stringWidth("DEPTH NODE VALUES");

        g.setFont(MySequentialGraphVars.tahomaBoldFont12);
        g.setColor(Color.DARK_GRAY);
        g.drawString("DEPTH NODE VALUES", (this.getWidth() - titleStringWidth), (10 * (i + 1)) + ((i++ * this.gap)) - 1);

        int nodeCnt = 0;
        for (String name : this.data.keySet()) {
            if (this.data.get(name) > 0 && nodeCnt < MySequentialGraphVars.BAR_CHART_RECORD_LIMIT) {
                g.setColor(this.colors.get(nodeCnt++));
                float valuePortion = this.data.get(name) / this.maxVal;
                float BAR_WIDTH_TO_DRAW = this.BAR_WIDTH * valuePortion;
                int xPos = (this.getWidth() - ((int) BAR_WIDTH_TO_DRAW + 1));
                g.fillRect(xPos - 1, (10 * i) + (i * this.gap), (int) BAR_WIDTH_TO_DRAW, 10);
                g.setColor(Color.BLUE);
                g.drawRect(xPos - 2, (10 * i) + (i * this.gap), (int) BAR_WIDTH_TO_DRAW + 1, 10);
                String value = MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(this.data.get(name)));
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

    @Override protected void paintComponent(Graphics g) {
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