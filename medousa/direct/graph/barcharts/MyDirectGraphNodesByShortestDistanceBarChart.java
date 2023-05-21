package medousa.direct.graph.barcharts;

import medousa.direct.graph.MyDirectNode;
import medousa.direct.utils.MyDirectGraphMathUtil;
import medousa.direct.utils.MyDirectGraphSysUtil;
import medousa.direct.utils.MyDirectGraphVars;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class MyDirectGraphNodesByShortestDistanceBarChart
extends JPanel {

    private Image img;
    private Graphics graph;
    private final int BAR_WIDTH = 50;
    private TreeMap<String, Integer> data;
    private int maxVal = 0;
    private Random rand = new Random();
    private boolean isMaxValueSet = false;
    private int gap = 2;
    private ArrayList<Color> colors;
    private int barViewerWidth;

    public MyDirectGraphNodesByShortestDistanceBarChart(Set<MyDirectNode> visitedNodes) {
        barViewerWidth = MyDirectGraphVars.getDirectGraphViewer().getWidth()-500;
        this.setLayout(new FlowLayout(FlowLayout.RIGHT, 3,3));
        setBounds(barViewerWidth, 5, 500, 2000);
        this.setDistanceDistributionBarChart(visitedNodes);
    }

    public void setDistanceDistributionBarChart(Set<MyDirectNode> visitedNodes) {
        this.isMaxValueSet = false;
        this.data = new TreeMap<>();
        this.colors = new ArrayList<>();
        if (visitedNodes != null) {
            for (MyDirectNode n : visitedNodes) {
                if (MyDirectGraphVars.app.getDirectGraphDashBoard().shortestDistancePathMenu.getSelectedIndex() == 0) {
                    if (this.data.containsKey("DISTANCE " + ((int) n.shortestOutDistance))) {
                        this.data.put("DISTANCE " + ((int) n.shortestOutDistance), this.data.get("DISTANCE " + ((int) n.shortestOutDistance)) + 1);
                    } else {
                        this.data.put("DISTANCE " + ((int) n.shortestOutDistance), 1);
                    }
                } else {
                    if (this.data.containsKey("DISTANCE " + ((int) n.shortestInDistance))) {
                        this.data.put("DISTANCE " + ((int) n.shortestInDistance), this.data.get("DISTANCE " + ((int) n.shortestInDistance)) + 1);
                    } else {
                        this.data.put("DISTANCE " + ((int) n.shortestInDistance), 1);
                    }
                }
            }

            for (int i = 0; i < this.data.size(); i++) {
                final float hue = this.rand.nextFloat();
                final float saturation = 0.9f;
                final float luminance = 1.0f;
                this.colors.add(Color.getHSBColor(hue, saturation, luminance));
            }

            this.setMaximumValue();
            setOpaque(false);
            setBackground(new Color(0, 0, 0, 0));
            this.isMaxValueSet = true;
        }
    }

    private void setMaximumValue() {
        this.maxVal = 0;
        for (int value : this.data.values()) {
            if (this.maxVal < value) {
                this.maxVal = value;
            }
        }
    }

    @Override public synchronized void paint(Graphics g) {
        if (!this.isMaxValueSet || this.data.size() == 0) return;
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        FontMetrics fontMetrics = g.getFontMetrics(MyDirectGraphVars.f_pln_11);

        int i=0;
        g.setFont(MyDirectGraphVars.tahomaBoldFont11);
        g.setColor(Color.DARK_GRAY);
        int titleStringWidth = fontMetrics.stringWidth("NODES BY DISTANCE");
        g.drawString("NODES BY DISTANCE", (this.getWidth()-(titleStringWidth+3)), (9*(i+1))+((i*this.gap))-1);

        i++;
        int distanceCnt = 0;
        for (String distancPrefix : this.data.keySet()) {
            g.setColor(this.colors.get(distanceCnt++));
            float valuePortion = (float) this.data.get(distancPrefix) / this.maxVal;
            float BAR_WIDTH_TO_DRAW = this.BAR_WIDTH * valuePortion;
            int xPos = (this.getWidth() - ((int) BAR_WIDTH_TO_DRAW + 1));
            g.fillRect(xPos - 1, (9 * i) + (i * this.gap), (int) BAR_WIDTH_TO_DRAW, 9);
            g.setColor(Color.BLUE);
            g.drawRect(xPos - 2, (9 * i) + (i * this.gap), (int) BAR_WIDTH_TO_DRAW + 1, 9);
            String value = MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(this.data.get(distancPrefix)));
            value = (value.endsWith(".0") || value.endsWith(".00") ? value.substring(0, value.indexOf(".0")) : value);
            value = distancPrefix + "[" + value + "]";
            int nameWritingPos = (xPos - 4) - fontMetrics.stringWidth(value);
            g.setFont(MyDirectGraphVars.f_pln_11);
            g.setColor(Color.DARK_GRAY);
            g.drawString(value, nameWritingPos, (9 * (i + 1)) + ((i * this.gap) - 2));
            i++;
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