package medousa.test;

import javax.swing.*;
import java.awt.*;

public class DataFrameExample {
    public static void main(String[] args) {
        // Create the main JFrame
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a JPanel to hold the data frame
        JPanel dataFramePanel = new JPanel(new GridLayout(2, 2));

        // Create labels and text fields for each cell in the data frame
        JLabel label1 = new JLabel("Cell 1");
        JTextField textField1 = new JTextField();

        JLabel label2 = new JLabel("Cell 2");
        JTextField textField2 = new JTextField();

        JLabel label3 = new JLabel("Cell 3");
        JTextField textField3 = new JTextField();

        JLabel label4 = new JLabel("Cell 4");
        JTextField textField4 = new JTextField();

        // Add the labels and text fields to the data frame panel
        dataFramePanel.add(label1);
        dataFramePanel.add(textField1);
        dataFramePanel.add(label2);
        dataFramePanel.add(textField2);
        dataFramePanel.add(label3);
        dataFramePanel.add(textField3);
        dataFramePanel.add(label4);
        dataFramePanel.add(textField4);

        // Add the data frame panel to the main frame
        frame.add(dataFramePanel);

        // Set frame properties and display it
        frame.setSize(400, 200);
        frame.setVisible(true);
    }
}

