package medousa.sequential.utils;


import medousa.message.MyMessageUtil;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.*;

/**
 * A font selection dialog.
 * <p>
 * Note: can take a long time to start up on systems with (literally) hundreds
 * of fonts. TODO change list to JList, add a SelectionChangedListener to
 * preview.
 *
 * @author Ian Darwin
 * @version $Id: FontChooser.java,v 1.19 2004/03/20 20:44:56 ian Exp $
 */
public class MyFontChooser extends JDialog {

    // Results:

    /** The font the user has chosen */
    protected Font resultFont;

    /** The resulting font name */
    protected String resultName;

    /** The resulting font size */
    protected int resultSize;

    /** The resulting boldness */
    protected boolean isBold;

    /** The resulting italicness */
    protected boolean isItalic;
    protected boolean isPlain;

    // Working fields

    /** Display text */
    protected String displayText = "Qwerty Yuiop";

    /** The font name chooser */
    protected JList fontNameChoices;

    /** The font size chooser */
    protected JList fontSizeChoices;

    /** The bold and italic choosers */
    private JCheckBox boldCheckBox, italicCheckBox, plainCheckBox;

    /** The list of font sizes */
    protected String fontSizes[] = {
            "8", "10", "11", "12", "14", "16", "18",
            "20", "22", "24", "28", "30", "32", "34",
            "36", "40", "48", "50", "52", "54", "56",
            "58", "60", "64", "68",
            "72", "76", "80", "84", "88", "90", "96",
            "100", "110", "120"};

    /** The index of the default size (e.g., 14 point == 4) */
    protected static final int DEFAULT_SIZE = 12;

    /**
     * The display area. Use a JLabel as the AWT label doesn't always honor
     * setFont() in a timely fashion :-)
     */
    protected JLabel previewArea;

    /**
     * Construct a FontChooser -- Sets title and gets array of fonts on the
     * system. Builds a GUI to let the user choose one font at one size.
     */
    public MyFontChooser(Frame f) {
        super(f, f.getTitle(), true);
        f.setLayout(new BorderLayout(3, 3));

        JPanel cp = new JPanel();
        cp.setLayout(new BorderLayout(3,3));

        Panel top = new Panel();
        top.setPreferredSize(new Dimension(400, 200));
        top.setLayout(new BorderLayout(3,3));

        String [] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

        fontNameChoices = new JList(fontNames);
        fontSizeChoices = new JList(fontSizes);
        fontNameChoices.setFont(MySequentialGraphVars.f_pln_12);
        fontSizeChoices.setFont(MySequentialGraphVars.f_pln_12);
        fontNameChoices.setSelectedIndex(0);
        fontSizeChoices.setSelectedIndex(0);

        JSplitPane fontNameSizeSplitPane = new JSplitPane();
        fontNameSizeSplitPane.setPreferredSize(new Dimension(300, 200));
        fontNameSizeSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        fontNameSizeSplitPane.setDividerLocation(0.5);
        fontNameSizeSplitPane.setDividerSize(10);
        fontNameSizeSplitPane.setLeftComponent(new JScrollPane(fontNameChoices));
        fontNameSizeSplitPane.setRightComponent(new JScrollPane(fontSizeChoices));
        fontNameSizeSplitPane.addComponentListener(new ComponentAdapter() {
            @Override public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                fontNameSizeSplitPane.setDividerLocation((int) (0.5 * fontNameSizeSplitPane.getWidth()));
            }
        });

        boldCheckBox = new JCheckBox("Bold    ");
        boldCheckBox.setFocusable(false);
        boldCheckBox.setFont(MySequentialGraphVars.tahomaPlainFont12);
        boldCheckBox.setSelected(false);
        boldCheckBox.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                if (boldCheckBox == e.getSource()) {
                    italicCheckBox.setSelected(false);
                    plainCheckBox.setSelected(false);
                }
            }
        });

        italicCheckBox = new JCheckBox("Italic    ");
        italicCheckBox.setFont(MySequentialGraphVars.tahomaPlainFont12);
        italicCheckBox.setSelected(false);
        italicCheckBox.setFocusable(false);
        italicCheckBox.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                if (italicCheckBox == e.getSource()) {
                    boldCheckBox.setSelected(false);
                    plainCheckBox.setSelected(false);
                }
            }
        });

        plainCheckBox = new JCheckBox("Plain    ");
        plainCheckBox.setFont(MySequentialGraphVars.tahomaPlainFont12);
        plainCheckBox.setSelected(true);
        plainCheckBox.setFocusable(false);
        plainCheckBox.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                if (italicCheckBox == e.getSource()) {
                    boldCheckBox.setSelected(false);
                    italicCheckBox.setSelected(false);
                }
            }
        });

        JLabel emptyTopLabel = new JLabel(" ");
        emptyTopLabel.setFont(new Font("Arial", Font.PLAIN, 2));
        emptyTopLabel.setPreferredSize(new Dimension(50, 1));

        Panel fontAttrPanel = new Panel();
        fontAttrPanel.setPreferredSize(new Dimension(80, 60));
        fontAttrPanel.setMaximumSize(new Dimension(80, 60));
        fontAttrPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3,3));
        fontAttrPanel.setLayout(new GridLayout(0, 1));
        fontAttrPanel.add(emptyTopLabel);
        fontAttrPanel.add(plainCheckBox);
        fontAttrPanel.add(boldCheckBox);
        fontAttrPanel.add(italicCheckBox);

        JPanel emptyCenterFontAttrPanel = new JPanel();
        emptyCenterFontAttrPanel.setPreferredSize(new Dimension(100, 100));

        JPanel fontPanel = new JPanel();
        fontPanel.setLayout(new BorderLayout(3,3));
        fontPanel.add(fontAttrPanel, BorderLayout.NORTH);
        fontPanel.add(emptyCenterFontAttrPanel, BorderLayout.CENTER);

        top.add(fontNameSizeSplitPane, BorderLayout.CENTER);
        top.add(fontPanel, BorderLayout.EAST);
        cp.add(top, BorderLayout.NORTH);

        previewArea = new JLabel(displayText, JLabel.CENTER);
        previewArea.setSize(200, 80);
        cp.add(previewArea, BorderLayout.CENTER);

        Panel btnPanel = new Panel();
        btnPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 3,3));

        JButton okButton = new JButton("SELECT");
        okButton.setFont(MySequentialGraphVars.tahomaPlainFont12);
        btnPanel.add(okButton);
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                previewFont();
                dispose();
                setVisible(false);
                if (getTitle().contains("NODE")) {
                    MySequentialGraphVars.getSequentialGraphViewer().nodeFont = resultFont;
                } else if (getTitle().contains("EDGE")) {
                    MySequentialGraphVars.getSequentialGraphViewer().edgeFont = resultFont;
                }
                MySequentialGraphVars.getSequentialGraphViewer().revalidate();
                MySequentialGraphVars.getSequentialGraphViewer().repaint();
            }
        });

        JLabel l1 = new JLabel("  ");
        l1.setBackground(new Color(0,0,0,0));
        btnPanel.add(l1);

        JButton pvButton = new JButton("PREVIEW");
        pvButton.setFont(MySequentialGraphVars.tahomaPlainFont12);
        btnPanel.add(pvButton);
        pvButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                previewFont();
            }
        });

        JLabel l2= new JLabel("  ");
        l2.setBackground(new Color(0,0,0,0));
        btnPanel.add(l2);

        JButton canButton = new JButton("CANCEL");
        btnPanel.add(canButton);
        canButton.setFont(MySequentialGraphVars.tahomaPlainFont12);
        canButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Set all values to null. Better: restore previous.
                resultFont = null;
                resultName = null;
                resultSize = 0;
                isBold = false;
                isItalic = false;

                dispose();
                setVisible(false);
            }
        });

        cp.add(btnPanel, BorderLayout.SOUTH);

        previewFont(); // ensure view is up to date!

        getContentPane().add(cp, BorderLayout.CENTER);
        setPreferredSize(new Dimension(550, 380));
        setLocationRelativeTo(MySequentialGraphVars.app);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        isPlain = true;
        pack();
    }

    /**
     * Called from the action handlers to get the font info, build a font, and
     * set it.
     */
    protected void previewFont() {
        resultName = fontNameChoices.getSelectedValue().toString();
        String resultSizeName = fontSizeChoices.getSelectedValue().toString();
        int resultSize = Integer.parseInt(resultSizeName);

        isBold = boldCheckBox.isSelected();
        isItalic = italicCheckBox.isSelected();
        isPlain = plainCheckBox.isSelected();

        if (resultName == null) {
            MyMessageUtil.showConfirmMessage(this,"Select a font.");
            return;
        } else if (resultSizeName == null) {
            MyMessageUtil.showConfirmMessage(this,"Select a font size.");
            return;
        } else if (!isBold && !isPlain && !isItalic) {
            MyMessageUtil.showConfirmMessage(this,"Select a font attribute.");
            return;
        }

        int attrs = Font.PLAIN;
        if (isBold)
            attrs = Font.BOLD;
        if (isItalic)
            attrs |= Font.ITALIC;
        if (isPlain)
            attrs |= Font.PLAIN;

        resultFont = new Font(resultName, attrs, resultSize);
        // System.out.println("resultName = " + resultName + "; " +
        //     "resultFont = " + resultFont);
        previewArea.setFont(resultFont);
       // pack(); // ensure Dialog is big enough.
    }

    /** Retrieve the selected font name. */
    public String getSelectedName() {
        return resultName;
    }

    /** Retrieve the selected size */
    public int getSelectedSize() {
        return resultSize;
    }

    /** Retrieve the selected font, or null */
    public Font getSelectedFont() {
        return resultFont;
    }

    /** Simple main program to start it running */
    public static void main(String[] args) {
        final JFrame f = new JFrame("FontChooser Startup");
        final MyFontChooser fc = new MyFontChooser(f);
        final Container cp = f.getContentPane();
        cp.setLayout(new GridLayout(0, 1)); // one vertical column

        JButton theButton = new JButton("Change font");
        cp.add(theButton);

        final JLabel theLabel = new JLabel("Java is great!", JLabel.CENTER);
        cp.add(theLabel);

        // Now that theButton and theLabel are ready, make the action listener
        theButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fc.setVisible(true);
                Font myNewFont = fc.getSelectedFont();
                System.out.println("You chose " + myNewFont);
                theLabel.setFont(myNewFont);
                f.pack(); // adjust for new size
                fc.dispose();
            }
        });

        f.setSize(150, 100);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
