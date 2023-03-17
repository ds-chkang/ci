package datamining.graph.stats.singlenode;

import datamining.graph.MyNode;
import datamining.utils.MyMathUtil;
import datamining.utils.system.MySysUtil;
import datamining.utils.system.MyVars;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class MySingleNodeValueBarChart extends JPanel {

    private Image img;
    private Graphics graph;
    public LinkedHashMap<String, Float> data;
    private float maxVal = 0.00f;
    private Random rand = new Random();
    private boolean isMaxValueSet = false;
    private int gap = 2;
    private final int BAR_WIDTH = 65;
    private ArrayList<Color> colors;

    public MySingleNodeValueBarChart() {
        this.setNodeValueBarChart();
    }

    public synchronized void setNodeValueBarChart() {
        this.isMaxValueSet = false;
        this.data = new LinkedHashMap<>();
        this.colors = new ArrayList<>();
        Set<MyNode> nodes = new HashSet<>(MyVars.getViewer().selectedSingleNodePredecessors);
        nodes.addAll(MyVars.getViewer().selectedSingleNodeSuccessors);
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0) continue;
            String name = (n.getName().contains("x") ? MySysUtil.decodeVariable(n.getName()) : MySysUtil.decodeNodeName(n.getName()));
            this.data.put(name, n.getCurrentValue());
            final float hue = this.rand.nextFloat();
            final float saturation = 0.9f;
            final float luminance = 1.0f;
            Color randomColor = Color.getHSBColor(hue, saturation, luminance);
            this.colors.add(randomColor);
        }
        this.data = MySysUtil.sortMapByFloatValue(this.data);
        this.setMaximumValue();
        if (MySysUtil.getViewerWidth() < 1200) {
            setBounds(0, 0, 0, 0);
        } else {
            setBounds((MySysUtil.getViewerWidth() <= 1900 ?
                    MySysUtil.getViewerWidth() - (765 + MyVars.getViewer().vc.nodeListTable.getWidth()) :
                    MySysUtil.getViewerWidth() - (810 + MyVars.getViewer().vc.nodeListTable.getWidth())), 5, 600, 2000);
        }
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
            if (this.data.get(name) > 0 && nodeCnt < MyVars.BAR_CHART_RECORD_LIMIT) {
                g.setColor(this.colors.get(nodeCnt++));
                float valuePortion = this.data.get(name)/this.maxVal;
                float BAR_WIDTH_TO_DRAW = this.BAR_WIDTH*valuePortion;
                int xPos = (428+(this.BAR_WIDTH-(int)BAR_WIDTH_TO_DRAW));
                g.fillRect(xPos-1, (9*i)+(i*this.gap), (int)BAR_WIDTH_TO_DRAW, 9);
                g.setColor(Color.BLUE);
                g.drawRect(xPos-2, (9*i)+(i*this.gap), (int)BAR_WIDTH_TO_DRAW+1, 9);
                String value = MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(this.data.get(name)));
                value = (value.endsWith(".0") || value.endsWith(".00") ? value.substring(0, value.indexOf(".0")) : value);
                value = name + "[" + value + "]";
                int nameWritingPos = (xPos-6) -fontMetrics.stringWidth(value);
                g.setFont(MyVars.f_pln_11);
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
}