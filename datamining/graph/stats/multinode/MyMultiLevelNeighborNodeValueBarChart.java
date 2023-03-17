package datamining.graph.stats.multinode;

import datamining.graph.MyEdge;
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


    public MyMultiLevelNeighborNodeValueBarChart() {
        this.setNeighborNodeValueBarChart();
    }

    public void setNeighborNodeValueBarChart() {
        this.isMaxValueSet = false;
        this.sharedPredecessorMap = new LinkedHashMap<>();
        this.sharedSuccessorMap = new LinkedHashMap<>();
        this.predecessorColors = new ArrayList<>();
        this.successorColors = new ArrayList<>();

        for (MyNode n : MyVars.getViewer().sharedPredecessors) {
            String pName = (n.getName().contains("x") ? MySysUtil.decodeVariable(n.getName()) : MySysUtil.decodeNodeName(n.getName()));
            this.sharedPredecessorMap.put(pName, n.getCurrentValue());
            final float hue = this.rand.nextFloat();
            final float saturation = 0.9f;
            final float luminance = 1.0f;
            Color predecessorRandomColor = Color.getHSBColor(hue, saturation, luminance);
            this.predecessorColors.add(predecessorRandomColor);
        }

        for (MyNode n : MyVars.getViewer().sharedSuccessors) {
            String sName = (n.getName().contains("x") ? MySysUtil.decodeVariable(n.getName()) : MySysUtil.decodeNodeName(n.getName()));
            this.sharedSuccessorMap.put(sName, n.getCurrentValue());
            final float hue = this.rand.nextFloat();
            final float saturation = 0.9f;
            final float luminance = 1.0f;
            Color successorRandomColor = Color.getHSBColor(hue, saturation, luminance);
            this.successorColors.add(successorRandomColor);
        }

        this.sharedPredecessorMap = MySysUtil.sortMapByFloatValue(this.sharedPredecessorMap);
        this.sharedSuccessorMap = MySysUtil.sortMapByFloatValue(this.sharedSuccessorMap);
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
        FontMetrics fontMetrics = g.getFontMetrics(MyVars.f_pln_11);
        int i=0;

        if (this.sharedPredecessorMap.size() > 0 && this.sharedSuccessorMap.size() == 0) {
            g.setFont(MyVars.tahomaBoldFont10);
            g.setColor(Color.DARK_GRAY);
            g.drawString("SHARED PREDECESSOR VALUES", 372, (9*(i+1))+((i*this.gap)));

            i++;
            int predecessorIdx = 0;
            for (String name : this.sharedPredecessorMap.keySet()) {
                g.setColor(this.predecessorColors.get(predecessorIdx++));
                float valuePortion = this.sharedPredecessorMap.get(name) / this.predecessorMaxVal;
                float BAR_WIDTH_TO_DRAW = this.BAR_WIDTH * valuePortion;
                int xPos = (428 + (this.BAR_WIDTH - (int) BAR_WIDTH_TO_DRAW));
                g.fillRect(xPos - 1, (9 * i) + (i * this.gap), (int) BAR_WIDTH_TO_DRAW, 9);
                g.setColor(Color.BLUE);
                g.drawRect(xPos - 2, (9 * i) + (i * this.gap), (int) BAR_WIDTH_TO_DRAW + 1, 9);
                String value = MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(this.sharedPredecessorMap.get(name)));
                value = (value.endsWith(".0") || value.endsWith(".00") ? value.substring(0, value.indexOf(".0")) : value);
                value = name + "[" + value + "]";
                int nameWritingPos = (xPos - 6) - fontMetrics.stringWidth(value);
                g.setFont(MyVars.f_pln_11);
                g.setColor(Color.DARK_GRAY);
                g.drawString(value, nameWritingPos, (9 * (i + 1)) + ((i * this.gap) - 2));
                i++;
            }
        } else if (this.sharedPredecessorMap.size() == 0 && this.sharedSuccessorMap.size() > 0) {
            g.setFont(MyVars.tahomaBoldFont10);
            g.setColor(Color.DARK_GRAY);
            g.drawString("SHARED SUCCESSOR VALUES", 372, (9*(i+1))+((i*this.gap)));
            i++;
            int successorIdx = 0;
            for (String name : this.sharedSuccessorMap.keySet()) {
                g.setColor(this.successorColors.get(successorIdx++));
                float valuePortion = this.sharedSuccessorMap.get(name) / this.successorMaxVal;
                float BAR_WIDTH_TO_DRAW = this.BAR_WIDTH * valuePortion;
                int xPos = (428 + (this.BAR_WIDTH - (int) BAR_WIDTH_TO_DRAW));
                g.fillRect(xPos - 1, (9 * i) + (i * this.gap), (int) BAR_WIDTH_TO_DRAW, 9);
                g.setColor(Color.BLUE);
                g.drawRect(xPos - 2, (9 * i) + (i * this.gap), (int) BAR_WIDTH_TO_DRAW + 1, 9);
                String value = MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(this.sharedSuccessorMap.get(name)));
                value = (value.endsWith(".0") || value.endsWith(".00") ? value.substring(0, value.indexOf(".0")) : value);
                value = name + "[" + value + "]";
                int nameWritingPos = (xPos - 6) - fontMetrics.stringWidth(value);
                g.setFont(MyVars.f_pln_11);
                g.setColor(Color.DARK_GRAY);
                g.drawString(value, nameWritingPos, (9 * (i + 1)) + ((i * this.gap) - 2));
                i++;
            }
        } else if (this.sharedPredecessorMap.size() > 0 && this.sharedSuccessorMap.size() > 0) {
            g.setFont(MyVars.tahomaBoldFont11);
            g.setColor(Color.DARK_GRAY);
            g.drawString("SHARED PREDECESSOR VALUES", 372, (9*(i+1))+((i*this.gap)));
            i++;
            int predecessorIdx = 0;
            for (String name : this.sharedPredecessorMap.keySet()) {
                g.setColor(this.predecessorColors.get(predecessorIdx++));
                float valuePortion = this.sharedPredecessorMap.get(name) / this.predecessorMaxVal;
                float BAR_WIDTH_TO_DRAW = this.BAR_WIDTH * valuePortion;
                int xPos = (428 + (this.BAR_WIDTH - (int) BAR_WIDTH_TO_DRAW));
                g.fillRect(xPos - 1, (9 * i) + (i * this.gap), (int) BAR_WIDTH_TO_DRAW, 9);
                g.setColor(Color.BLUE);
                g.drawRect(xPos - 2, (9 * i) + (i * this.gap), (int) BAR_WIDTH_TO_DRAW + 1, 9);
                String value = MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(this.sharedPredecessorMap.get(name)));
                value = (value.endsWith(".0") || value.endsWith(".00") ? value.substring(0, value.indexOf(".0")) : value);
                value = name + "[" + value + "]";
                int nameWritingPos = (xPos - 6) - fontMetrics.stringWidth(value);
                g.setFont(MyVars.f_pln_11);
                g.setColor(Color.DARK_GRAY);
                g.drawString(value, nameWritingPos, (9 * (i + 1)) + ((i * this.gap) - 2));
                i++;
            }

            g.setFont(MyVars.tahomaBoldFont10);
            g.setColor(Color.DARK_GRAY);
            g.drawString("", 5, (9*(i+1))+((i*this.gap)-2));
            i++;
            g.setFont(MyVars.tahomaBoldFont10);
            g.setColor(Color.DARK_GRAY);
            g.drawString("SHARED SUCCESSOR VALUES", 372, (9*(i+1))+((i*this.gap)));
            i++;
            int succesorIdx = 0;
            for (String name : this.sharedSuccessorMap.keySet()) {
                g.setColor(this.successorColors.get(succesorIdx++));
                float valuePortion = this.sharedSuccessorMap.get(name) / this.successorMaxVal;
                float BAR_WIDTH_TO_DRAW = this.BAR_WIDTH * valuePortion;
                int xPos = (428 + (this.BAR_WIDTH - (int) BAR_WIDTH_TO_DRAW));
                g.fillRect(xPos - 1, (9 * i) + (i * this.gap), (int) BAR_WIDTH_TO_DRAW, 9);
                g.setColor(Color.BLUE);
                g.drawRect(xPos - 2, (9 * i) + (i * this.gap), (int) BAR_WIDTH_TO_DRAW + 1, 9);
                String value = MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(this.sharedSuccessorMap.get(name)));
                value = (value.endsWith(".0") || value.endsWith(".00") ? value.substring(0, value.indexOf(".0")) : value);
                value = name + "[" + value + "]";
                int nameWritingPos = (xPos - 6) - fontMetrics.stringWidth(value);
                g.setFont(MyVars.f_pln_11);
                g.setColor(Color.DARK_GRAY);
                g.drawString(value, nameWritingPos, (9 * (i + 1)) + ((i * this.gap) - 2));
                i++;
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