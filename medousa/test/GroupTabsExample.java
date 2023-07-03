package medousa.test;

import javax.swing.*;

public class GroupTabsExample extends JFrame {
    private JTabbedPane tabbedPane;

    public GroupTabsExample() {
        setTitle("Group Tabs Example");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create the main panel to hold the tabs
        JPanel mainPanel = new JPanel();

        // Create the tabbed pane
        tabbedPane = new JTabbedPane();

        // Create the first tab content
        JPanel tab1 = new JPanel();
        tab1.add(new JLabel("Tab 1 Content"));

        // Create the second tab content
        JPanel tab2 = new JPanel();
        tab2.add(new JLabel("Tab 2 Content"));

        // Add the tab contents to the main panel
        mainPanel.add(tab1);
        mainPanel.add(tab2);

        // Add the main panel as a tab to the tabbed pane
        tabbedPane.addTab("Grouped Tabs", mainPanel);

        // Add the tabbed pane to the frame
        getContentPane().add(tabbedPane);

        pack();
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GroupTabsExample example = new GroupTabsExample();
            example.setVisible(true);
        });
    }
}
