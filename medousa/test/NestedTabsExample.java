package medousa.test;

import javax.swing.*;

public class NestedTabsExample extends JFrame {
    private JTabbedPane outerTabbedPane;
    private JTabbedPane innerTabbedPane;

    public NestedTabsExample() {
        setTitle("Nested Tabs Example");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create the outer tabbed pane
        outerTabbedPane = new JTabbedPane();

        // Create the first tab content for the outer tabbed pane
        JPanel tab1 = new JPanel();
        tab1.add(new JLabel("Outer Tab 1 Content"));

        // Create the second tab content for the outer tabbed pane
        JPanel tab2 = new JPanel();
        tab2.add(new JLabel("Outer Tab 2 Content"));

        // Add the outer tab contents to the outer tabbed pane
        outerTabbedPane.addTab("Outer Tab 1", tab1);
        outerTabbedPane.addTab("Outer Tab 2", tab2);

        // Create the inner tabbed pane
        innerTabbedPane = new JTabbedPane();

        // Create the first tab content for the inner tabbed pane
        JPanel innerTab1 = new JPanel();
        innerTab1.add(new JLabel("Inner Tab 1 Content"));

        // Create the second tab content for the inner tabbed pane
        JPanel innerTab2 = new JPanel();
        innerTab2.add(new JLabel("Inner Tab 2 Content"));

        // Add the inner tab contents to the inner tabbed pane
        innerTabbedPane.addTab("Inner Tab 1", innerTab1);
        innerTabbedPane.addTab("Inner Tab 2", innerTab2);

        // Create a panel to hold the inner tabbed pane
        JPanel innerPanel = new JPanel();
        innerPanel.add(innerTabbedPane);

        // Add the inner panel as a tab to the outer tabbed pane
        outerTabbedPane.addTab("Nested Tabs", innerPanel);

        // Add the outer tabbed pane to the frame
        getContentPane().add(outerTabbedPane);

        pack();
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            NestedTabsExample example = new NestedTabsExample();
            example.setVisible(true);
        });
    }
}
