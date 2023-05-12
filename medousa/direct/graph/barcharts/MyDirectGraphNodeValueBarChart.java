package medousa.direct.graph.barcharts;

import medousa.direct.graph.MyDirectNode;
import medousa.direct.utils.MyDirectGraphMathUtil;
import medousa.direct.utils.MyDirectGraphSysUtil;
import medousa.direct.utils.MyDirectGraphVars;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class MyDirectGraphNodeValueBarChart
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
    int barViewerWidth;

    public MyDirectGraphNodeValueBarChart() {
        barViewerWidth = MyDirectGraphVars.getDirectGraphViewer().getWidth()-500;
        setBounds(barViewerWidth, 5, 500, 2000);
        this.setNodeValueBarChart();
    }

    public void setNodeValueBarChart() {
        this.isMaxValueSet = false;
        this.data = new LinkedHashMap<>();
        this.colors = new ArrayList<>();
        Collection<MyDirectNode> nodes = MyDirectGraphVars.directGraph.getVertices();
        for (MyDirectNode n : nodes) {
            if (n.getCurrentValue() == 0) continue;
            this.data.put(n.getName(), n.getCurrentValue());
            final float hue = this.rand.nextFloat();
            final float saturation = 0.9f;
            final float luminance = 1.0f;
            this.colors.add(Color.getHSBColor(hue, saturation, luminance));
        }
        this.data = MyDirectGraphSysUtil.sortMapByFloatValue(this.data);
        this.setMaximumValue();
        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0));
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
        try {
            if (!this.isMaxValueSet || this.data.size() == 0) return;
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            FontMetrics fontMetrics = g.getFontMetrics(MyDirectGraphVars.f_pln_12);

            int i = 0;
            g.setFont(MyDirectGraphVars.tahomaBoldFont12);
            g.setColor(Color.DARK_GRAY);
            int titleStringWidth = fontMetrics.stringWidth("NODE VALUES");
            g.drawString("NODE VALUES", (this.getWidth() - (titleStringWidth + 3)), (10 * (i + 1)) + ((i * this.gap)) - 1);

            i++;
            int nodeCnt = 0;
            for (String name : this.data.keySet()) {
                if (nodeCnt < 100) {
                    g.setColor(this.colors.get(nodeCnt++));
                    float valuePortion = this.data.get(name) / this.maxVal;
                    float BAR_WIDTH_TO_DRAW = this.BAR_WIDTH * valuePortion;
                    int xPos = (this.getWidth() - ((int) BAR_WIDTH_TO_DRAW + 1));
                    g.fillRect(xPos - 1, (10 * i) + (i * this.gap), (int) BAR_WIDTH_TO_DRAW, 10);
                    g.setColor(Color.BLUE);
                    g.drawRect(xPos - 2, (10 * i) + (i * this.gap), (int) BAR_WIDTH_TO_DRAW + 1, 10);
                    String value = MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(this.data.get(name)));
                    value = (value.endsWith(".0") || value.endsWith(".00") ? value.substring(0, value.indexOf(".0")) : value);
                    value = name + "[" + value + "]";
                    int nameWritingPos = (xPos - 4) - fontMetrics.stringWidth(value);
                    g.setFont(MyDirectGraphVars.f_pln_12);
                    g.setColor(Color.DARK_GRAY);
                    g.drawString(value, nameWritingPos, (10 * (i + 1)) + ((i * this.gap) - 2));
                    i++;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
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