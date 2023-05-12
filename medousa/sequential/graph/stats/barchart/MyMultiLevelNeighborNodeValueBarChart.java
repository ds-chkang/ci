package medousa.sequential.graph.stats.barchart;

import medousa.direct.utils.MyDirectGraphVars;
import medousa.sequential.graph.MyNode;
import medousa.sequential.utils.MyMathUtil;
import medousa.sequential.utils.MySequentialGraphSysUtil;
import medousa.sequential.utils.MySequentialGraphVars;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Random;

public class MyMultiLevelNeighborNodeValueBarChart extends JPanel {

    private Image i;
    private final int BAR_WIDTH = 65;
    private Graphics graph;
    private LinkedHashMap<String, Float> sharedPredecessorMap;
    private LinkedHashMap<String, Float> sharedSuccessorMap;
    private float predecessorMaxVal = 0.00f;
    private float successorMaxVal = 0.00f;
    private Random rand = new Random();
    private boolean isMaxValueSet = false;
    private int gap = 2;
    private ArrayList<Color> predecessorColors;
    private ArrayList<Color> successorColors;
    private int barViewerWidth;


    public MyMultiLevelNeighborNodeValueBarChart() {
        barViewerWidth = MySequentialGraphVars.getSequentialGraphViewer().getWidth()-500;
        setBounds(barViewerWidth, 5, 500, 2000);
        this.setNeighborNodeValueBarChart();
    }

    public void setNeighborNodeValueBarChart() {
        this.isMaxValueSet = false;
        this.sharedPredecessorMap = new LinkedHashMap<>();
        this.sharedSuccessorMap = new LinkedHashMap<>();
        this.predecessorColors = new ArrayList<>();
        this.successorColors = new ArrayList<>();

        for (MyNode n : MySequentialGraphVars.getSequentialGraphViewer().sharedPredecessors) {
            String pName = (n.getName().contains("x") ? MySequentialGraphSysUtil.decodeVariable(n.getName()) : MySequentialGraphSysUtil.decodeNodeName(n.getName()));
            this.sharedPredecessorMap.put(pName, n.getCurrentValue());
            final float hue = this.rand.nextFloat();
            final float saturation = 0.9f;
            final float luminance = 1.0f;
            Color predecessorRandomColor = Color.getHSBColor(hue, saturation, luminance);
            this.predecessorColors.add(predecessorRandomColor);
        }

        for (MyNode n : MySequentialGraphVars.getSequentialGraphViewer().sharedSuccessors) {
            String sName = (n.getName().contains("x") ? MySequentialGraphSysUtil.decodeVariable(n.getName()) : MySequentialGraphSysUtil.decodeNodeName(n.getName()));
            this.sharedSuccessorMap.put(sName, n.getCurrentValue());
            final float hue = this.rand.nextFloat();
            final float saturation = 0.9f;
            final float luminance = 1.0f;
            Color successorRandomColor = Color.getHSBColor(hue, saturation, luminance);
            this.successorColors.add(successorRandomColor);
        }
        this.sharedPredecessorMap = MySequentialGraphSysUtil.sortMapByFloatValue(this.sharedPredecessorMap);
        this.sharedSuccessorMap = MySequentialGraphSysUtil.sortMapByFloatValue(this.sharedSuccessorMap);

        this.setOpaque(false);
        this.setBackground(new Color(0,0,0,0.0f));
        this.setPredecessorMaximumValue();
        this.setSuccessorMaximumValue();
        this.isMaxValueSet = true;
    }

    private void setPredecessorMaximumValue() {
        this.predecessorMaxVal = 0;
        for (Float value : this.sharedPredecessorMap.values()) {
            if (this.predecessorMaxVal < value) {this.predecessorMaxVal = value;}
        }
    }

    private void setSuccessorMaximumValue() {
        this.successorMaxVal = 0;
        for (Float value : this.sharedSuccessorMap.values()) {
            if (this.successorMaxVal < value) {this.successorMaxVal = value;}
        }
    }

    @Override public void paint(Graphics g) {
        if (!this.isMaxValueSet) return;
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        FontMetrics fontMetrics = g.getFontMetrics(MySequentialGraphVars.f_pln_12);

        if (this.sharedPredecessorMap.size() > 0 && this.sharedSuccessorMap.size() == 0) {
            g.setFont(MySequentialGraphVars.tahomaBoldFont12);
            int titleStringWidth = fontMetrics.stringWidth("SHARED PREDECESSOR VALUES");
            int i=0;
            g.setColor(Color.DARK_GRAY);
            g.drawString("SHARED PREDECESSOR VALUES", (this.getWidth() - titleStringWidth), (10 * (i + 1)) + ((i++ * this.gap)) - 1);
            int predecessorIdx = 0;
            for (String name : this.sharedPredecessorMap.keySet()) {
                if (predecessorIdx < MySequentialGraphVars.BAR_CHART_RECORD_LIMIT) {
                    g.setColor(this.predecessorColors.get(predecessorIdx++));
                    float valuePortion = this.sharedPredecessorMap.get(name) / this.predecessorMaxVal;
                    float BAR_WIDTH_TO_DRAW = this.BAR_WIDTH * valuePortion;
                    int xPos = (this.getWidth() - ((int) BAR_WIDTH_TO_DRAW + 1));
                    g.fillRect(xPos - 1, (10 * i) + (i * this.gap), (int) BAR_WIDTH_TO_DRAW, 10);
                    g.setColor(Color.BLUE);
                    g.drawRect(xPos - 2, (10 * i) + (i * this.gap), (int) BAR_WIDTH_TO_DRAW + 1, 10);
                    String value = MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(this.sharedPredecessorMap.get(name)));
                    value = (value.endsWith(".0") || value.endsWith(".00") ? value.substring(0, value.indexOf(".0")) : value);
                    value = name + "[" + value + "]";
                    int nameWritingPos = (xPos - 4) - fontMetrics.stringWidth(value);
                    g.setFont(MyDirectGraphVars.f_pln_12);
                    g.setColor(Color.DARK_GRAY);
                    g.drawString(value, nameWritingPos, (10 * (i + 1)) + ((i * this.gap) - 2));
                    i++;
                }
            }
        } else if (this.sharedPredecessorMap.size() == 0 && this.sharedSuccessorMap.size() > 0) {
            g.setFont(MySequentialGraphVars.tahomaBoldFont12);
            int titleStringWidth = fontMetrics.stringWidth("SHARED SUCCESSOR VALUES");
            int i=0;
            g.drawString("SHARED SUCCESSOR VALUES", (this.getWidth() - titleStringWidth), (10 * (i + 1)) + ((i++ * this.gap)) - 1);
            int successorIdx = 0;
            for (String name : this.sharedSuccessorMap.keySet()) {
                if (successorIdx < MySequentialGraphVars.BAR_CHART_RECORD_LIMIT) {
                    g.setColor(this.successorColors.get(successorIdx++));
                    float valuePortion = this.sharedSuccessorMap.get(name) / this.successorMaxVal;
                    float BAR_WIDTH_TO_DRAW = this.BAR_WIDTH * valuePortion;
                    int xPos = (this.getWidth() - ((int) BAR_WIDTH_TO_DRAW + 1));
                    g.fillRect(xPos - 1, (10 * i) + (i * this.gap), (int) BAR_WIDTH_TO_DRAW, 10);
                    g.setColor(Color.BLUE);
                    g.drawRect(xPos - 2, (10 * i) + (i * this.gap), (int) BAR_WIDTH_TO_DRAW + 1, 10);
                    String value = MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(this.sharedSuccessorMap.get(name)));
                    value = (value.endsWith(".0") || value.endsWith(".00") ? value.substring(0, value.indexOf(".0")) : value);
                    value = name + "[" + value + "]";
                    int nameWritingPos = (xPos - 4) - fontMetrics.stringWidth(value);
                    g.setFont(MyDirectGraphVars.f_pln_12);
                    g.setColor(Color.DARK_GRAY);
                    g.drawString(value, nameWritingPos, (10 * (i + 1)) + ((i * this.gap) - 2));
                    i++;
                }
            }
        } else if (this.sharedPredecessorMap.size() > 0 && this.sharedSuccessorMap.size() > 0) {
            g.setFont(MySequentialGraphVars.tahomaBoldFont12);
            int titleStringWidth = fontMetrics.stringWidth("SHARED PREDECESSOR VALUES");
            int i=0;
            g.setColor(Color.DARK_GRAY);
            g.drawString("SHARED PREDECESSOR VALUES", (this.getWidth() - titleStringWidth), (10 * (i + 1)) + ((i++ * this.gap)) - 1);
            int predecessorIdx = 0;
            for (String name : this.sharedPredecessorMap.keySet()) {
                if (predecessorIdx < MySequentialGraphVars.BAR_CHART_RECORD_LIMIT) {
                    g.setColor(this.predecessorColors.get(predecessorIdx++));
                    float valuePortion = this.sharedPredecessorMap.get(name) / this.predecessorMaxVal;
                    float BAR_WIDTH_TO_DRAW = this.BAR_WIDTH * valuePortion;
                    int xPos = (this.getWidth() - ((int) BAR_WIDTH_TO_DRAW + 1));
                    g.fillRect(xPos - 1, (10 * i) + (i * this.gap), (int) BAR_WIDTH_TO_DRAW, 10);
                    g.setColor(Color.BLUE);
                    g.drawRect(xPos - 2, (10 * i) + (i * this.gap), (int) BAR_WIDTH_TO_DRAW + 1, 10);
                    String value = MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(this.sharedPredecessorMap.get(name)));
                    value = (value.endsWith(".0") || value.endsWith(".00") ? value.substring(0, value.indexOf(".0")) : value);
                    value = name + "[" + value + "]";
                    int nameWritingPos = (xPos - 4) - fontMetrics.stringWidth(value);
                    g.setFont(MyDirectGraphVars.f_pln_12);
                    g.setColor(Color.DARK_GRAY);
                    g.drawString(value, nameWritingPos, (10 * (i + 1)) + ((i * this.gap) - 2));
                    i++;
                }
            }

            i++;
            g.setFont(MySequentialGraphVars.tahomaBoldFont12);
            titleStringWidth = fontMetrics.stringWidth("SHARED SUCCESSOR VALUES");
            g.setColor(Color.DARK_GRAY);
            g.drawString("SHARED SUCCESSOR VALUES", (this.getWidth() - titleStringWidth), (10 * (i + 1)) + ((i++ * this.gap)) - 1);
            int successorIdx = 0;
            for (String name : this.sharedSuccessorMap.keySet()) {
                if (successorIdx < (MySequentialGraphVars.BAR_CHART_RECORD_LIMIT+100)) {
                    g.setColor(this.successorColors.get(successorIdx++));
                    float valuePortion = this.sharedSuccessorMap.get(name) / this.successorMaxVal;
                    float BAR_WIDTH_TO_DRAW = this.BAR_WIDTH * valuePortion;
                    int xPos = (this.getWidth() - ((int) BAR_WIDTH_TO_DRAW + 1));
                    g.fillRect(xPos - 1, (10 * i) + (i * this.gap), (int) BAR_WIDTH_TO_DRAW, 10);
                    g.setColor(Color.BLUE);
                    g.drawRect(xPos - 2, (10 * i) + (i * this.gap), (int) BAR_WIDTH_TO_DRAW + 1, 10);
                    String value = MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(this.sharedSuccessorMap.get(name)));
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

        entities.put("A", 90f);
        entities.put("B", 200f);
        entities.put("C", 120f);
        entities.put("D", 250f);
        entities.put("E", 19f);
        entities.put("F", 90f);
        entities.put("G", 20f);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}