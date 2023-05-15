package medousa.sequential.graph.funnel;

import edu.uci.ics.jung.visualization.DefaultVisualizationModel;
import medousa.sequential.graph.MyEdge;
import medousa.sequential.graph.MyNode;
import medousa.sequential.graph.layout.MyStaticLayout;

import javax.swing.*;
import java.awt.*;

public class MyFunnelAnalysisApp
extends JFrame {

    protected MyFunnelGraphViewer funnelGraphViewer;
    protected MyFunnelGraph funnelGraph;


    public MyFunnelAnalysisApp() {
        super("FUNNEL EXPLORATION");
        decorate();
    }

    private void decorate() {
        try {
            setBackground(Color.WHITE);
            setLayout(new BorderLayout(3,3));

            this.funnelGraph = new MyFunnelGraph<MyNode, MyEdge>();
            MyStaticLayout staticLayout = new MyStaticLayout(this.funnelGraph, new Dimension(400, 500));
            this.funnelGraphViewer = new MyFunnelGraphViewer(new DefaultVisualizationModel<>(staticLayout, new Dimension(400, 500)), this);
            this.getContentPane().add(this.funnelGraphViewer, BorderLayout.CENTER);
            this.pack();
            this.setExtendedState(JFrame.MAXIMIZED_BOTH);
            this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            this.setVisible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
