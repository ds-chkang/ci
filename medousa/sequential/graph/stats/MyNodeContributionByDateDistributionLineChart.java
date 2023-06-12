package medousa.sequential.graph.stats;

import medousa.direct.utils.MyDirectGraphVars;
import medousa.sequential.utils.MyMathUtil;
import medousa.sequential.utils.MySequentialGraphSysUtil;
import medousa.sequential.utils.MySequentialGraphVars;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.*;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.ApplicationFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Ellipse2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;

public class MyNodeContributionByDateDistributionLineChart extends ApplicationFrame {

    private long max = 0;
    private long min = 10000000000L;
    private float avg = 0f;

    public MyNodeContributionByDateDistributionLineChart(String title) {
        super(title);

        try {
            BufferedReader br = new BufferedReader(new FileReader(MySequentialGraphSysUtil.getWorkingDir() + MySequentialGraphSysUtil.getDirectorySlash() + "nodedatefeatures.txt"));
            String line = "";
            LinkedHashMap<String, Long> nodeContributionByDateMap = new LinkedHashMap<>();
            while ((line = br.readLine()) != null) {
                String [] itemsets = line.split("-");
                for (String itemset : itemsets) {
                    String date = itemset.split(":")[1];
                    if (nodeContributionByDateMap.containsKey(date)) {
                        long nodeContribution = nodeContributionByDateMap.get(date) + 1;
                        nodeContributionByDateMap.put(date, nodeContribution);
                    } else {
                        nodeContributionByDateMap.put(date, 1L);
                    }
                }
            }
            nodeContributionByDateMap = MySequentialGraphSysUtil.sortMapByLongValue(nodeContributionByDateMap);

            float totalContribution = 0f;
            TimeSeries series = new TimeSeries("NODE CONTRIBUTIONS BY DATE");
            for (String date : nodeContributionByDateMap.keySet()) {
                long nodeContribution = nodeContributionByDateMap.get(date);
                totalContribution += nodeContribution;

                if (nodeContribution > max) {
                    max = nodeContribution;
                }

                if (min > nodeContribution) {
                    min = nodeContribution;
                }

                String [] dateElements = date.split("\\*");
                series.add(new Day(Integer.parseInt(dateElements[2]), Integer.parseInt(dateElements[1]), Integer.parseInt(dateElements[0])), nodeContributionByDateMap.get(date));
            }

            avg = totalContribution/nodeContributionByDateMap.size();

            TimeSeriesCollection dataset = new TimeSeriesCollection();
            dataset.addSeries(series);

            // Create chart
            JFreeChart chart = ChartFactory.createTimeSeriesChart(
                    "NODE CONTRIBUTIONS BY DATE",
                    "",
                    "",
                    dataset,
                    false,
                    true,
                    false
            );
            chart.getXYPlot().setBackgroundPaint(Color.WHITE);
            chart.getXYPlot().setDomainGridlinePaint(Color.DARK_GRAY);
            chart.getXYPlot().setRangeGridlinePaint(Color.DARK_GRAY);

            XYPlot plot = (XYPlot) chart.getPlot();
            DateAxis dateAxis = new DateAxis();
            dateAxis.setDateFormatOverride(new SimpleDateFormat("yyyy-MM-dd"));
            dateAxis.setLabelAngle(Math.PI / 6.0);
            plot.setDomainAxis(dateAxis);

            chart.getXYPlot().getDomainAxis().setTickLabelFont(MyDirectGraphVars.f_pln_12);
            chart.getXYPlot().getRangeAxis().setTickLabelFont(MyDirectGraphVars.f_pln_12);
            chart.getTitle().setFont(MySequentialGraphVars.tahomaBoldFont12);

            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
            renderer.setSeriesPaint(0, Color.DARK_GRAY);
            renderer.setSeriesStroke(0, new BasicStroke(1.4f));
            renderer.setSeriesShapesVisible(0, true);
            renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
            renderer.setSeriesFillPaint(0, Color.WHITE);
            renderer.setUseFillPaint(true);

            renderer.setSeriesToolTipGenerator(0, new StandardXYToolTipGenerator());
            chart.removeLegend();

            String [] propertyColumns = {"PROPERTY", "VALUE"};
            String [][] propertyData = {};

            DefaultTableModel topTableStatisticsModel = new DefaultTableModel(propertyData, propertyColumns);
            JTable tableTopStatistics = new JTable(topTableStatisticsModel);
            tableTopStatistics.setBackground(Color.WHITE);
            tableTopStatistics.getTableHeader().setFont(MySequentialGraphVars.tahomaBoldFont11);
            tableTopStatistics.getTableHeader().setBackground(new Color(0,0,0,0));
            tableTopStatistics.getTableHeader().setOpaque(false);
            tableTopStatistics.setRowHeight(26);
            tableTopStatistics.setFont(MySequentialGraphVars.tahomaPlainFont12);
            tableTopStatistics.getColumnModel().getColumn(1).setPreferredWidth(60);
            tableTopStatistics.getColumnModel().getColumn(0).setPreferredWidth(150);

            topTableStatisticsModel.addRow(new String[]{" NO. OF DATES", " " + MyMathUtil.getCommaSeperatedNumber(nodeContributionByDateMap.size())});
            topTableStatisticsModel.addRow(new String[]{" MAX.", " " + MyMathUtil.getCommaSeperatedNumber(max)});
            topTableStatisticsModel.addRow(new String[]{" MIN.", " " + MyMathUtil.getCommaSeperatedNumber(min)});
            topTableStatisticsModel.addRow(new String[]{" AVG.", " " + MyMathUtil.twoDecimalFormat(avg)});

            String [] dateTableStatisticsColumns = {"NO.", "DATE", "CONT."};
            String [][] dateTableStatisticsData = {};
            DefaultTableModel dateTableStatisticsModel = new DefaultTableModel(dateTableStatisticsData, dateTableStatisticsColumns);
            JTable dateTableStatistics = new JTable(dateTableStatisticsModel);
            dateTableStatistics.setBackground(Color.WHITE);
            dateTableStatistics.getTableHeader().setFont(MySequentialGraphVars.tahomaBoldFont11);
            dateTableStatistics.getTableHeader().setBackground(new Color(0,0,0,0));
            dateTableStatistics.getTableHeader().setOpaque(false);
            dateTableStatistics.setRowHeight(26);
            dateTableStatistics.setFont(MySequentialGraphVars.tahomaPlainFont12);
            dateTableStatistics.getColumnModel().getColumn(1).setPreferredWidth(120);
            dateTableStatistics.getColumnModel().getColumn(0).setPreferredWidth(60);
            dateTableStatistics.getColumnModel().getColumn(2).setPreferredWidth(60);

            int i = 0;
            for (String date : nodeContributionByDateMap.keySet()) {
                dateTableStatisticsModel.addRow(new String[]{" " + (++i),
                    " " + date.replaceAll("\\*", "-"),
                    " " + MyMathUtil.getCommaSeperatedNumber(nodeContributionByDateMap.get(date))
                });
            }

            JSplitPane tableSplitPane = new JSplitPane();
            tableSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
            tableSplitPane.setTopComponent(new JScrollPane(tableTopStatistics));
            tableSplitPane.setBottomComponent(new JScrollPane(dateTableStatistics));
            tableSplitPane.setDividerLocation(0.42f);
            tableSplitPane.addComponentListener(new ComponentAdapter() {
                @Override public void componentResized(ComponentEvent e) {
                    super.componentResized(e);
                    tableSplitPane.setDividerLocation(0.42f);
                }
            });

            JSplitPane tableGraphSplitPane = new JSplitPane();
            tableGraphSplitPane.setLeftComponent(new ChartPanel(chart));
            tableGraphSplitPane.setRightComponent(tableSplitPane);
            tableGraphSplitPane.setDividerLocation(0.82f);
            tableGraphSplitPane.addComponentListener(new ComponentAdapter() {
                @Override public void componentResized(ComponentEvent e) {
                    super.componentResized(e);
                    tableGraphSplitPane.setDividerLocation(0.82f);
                }
            });

            JFrame f = new JFrame();
            f.setPreferredSize(new Dimension(600, 400));
            f.setLayout(new BorderLayout(3,3));
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            f.getContentPane().add(tableGraphSplitPane, BorderLayout.CENTER);
            f.pack();
            f.setVisible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        MyNodeContributionByDateDistributionLineChart example = new MyNodeContributionByDateDistributionLineChart("Date X-Axis Example");
        example.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}