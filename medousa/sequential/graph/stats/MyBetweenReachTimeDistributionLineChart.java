package medousa.sequential.graph.stats;

import medousa.MyProgressBar;
import medousa.sequential.utils.MySequentialGraphVars;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyBetweenReachTimeDistributionLineChart
extends JPanel
implements ActionListener {

    public static int instances = 0;

    public MyBetweenReachTimeDistributionLineChart() {}

    public void show(MyBetweenReachTimeDistributionLineChart betweenReachTimeDistributionLineChart) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                MyProgressBar pb = new MyProgressBar(false);
                try {
                    decorate();
                    JFrame f = new JFrame(" REACH TIME STATISTICS BY DEPTH");
                    f.setLayout(new BorderLayout());
                    f.setBackground(Color.WHITE);
                    f.setPreferredSize(new Dimension(700, 500));
                    f.getContentPane().add(betweenReachTimeDistributionLineChart, BorderLayout.CENTER);
                    f.pack();
                    f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    f.setCursor(Cursor.HAND_CURSOR);
                    MySequentialGraphVars.app.setAlwaysOnTop(false);
                    pb.updateValue(100, 100);
                    pb.dispose();
                    f.setVisible(true);
                } catch (Exception ex) {
                    pb.updateValue(100, 100);
                    pb.dispose();
                }
            }
        });
    }

    public void decorate() {
        try {
            setLayout(new BorderLayout(3, 3));
            setBackground(Color.WHITE);

            JPanel betweenNodePanel = new JPanel();
            betweenNodePanel.setBackground(Color.WHITE);
            betweenNodePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));

            JLabel sourceLabel = new JLabel("SOUCE: ");
            sourceLabel.setBackground(Color.WHITE);
            sourceLabel.setFont(MySequentialGraphVars.tahomaPlainFont12);

            JTextField sourceTxt = new JTextField();
            sourceTxt.setFont(MySequentialGraphVars.tahomaPlainFont12);
            sourceTxt.setBackground(Color.WHITE);
            sourceTxt.setBorder(BorderFactory.createEtchedBorder());
            sourceTxt.setPreferredSize(new Dimension(250, 24));

            JLabel destLabel = new JLabel("  DEST: ");
            destLabel.setBackground(Color.WHITE);
            destLabel.setFont(MySequentialGraphVars.tahomaPlainFont12);

            JTextField destTxt = new JTextField();
            destTxt.setFont(MySequentialGraphVars.tahomaPlainFont12);
            destTxt.setBackground(Color.WHITE);
            destTxt.setBorder(BorderFactory.createEtchedBorder());
            destTxt.setPreferredSize(new Dimension(250, 25));

            JButton runBtn = new JButton(" RUN ");
            runBtn.setFocusable(false);
            runBtn.setPreferredSize(new Dimension(60, 24));
            runBtn.setFont(MySequentialGraphVars.tahomaPlainFont12);

            betweenNodePanel.add(sourceLabel);
            betweenNodePanel.add(sourceTxt);
            betweenNodePanel.add(destLabel);
            betweenNodePanel.add(destTxt);
            betweenNodePanel.add(runBtn);

            add(betweenNodePanel, BorderLayout.NORTH);

            revalidate();
            repaint();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
