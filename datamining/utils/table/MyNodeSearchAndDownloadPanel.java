package datamining.utils.table;

import datamining.utils.message.MyMessageUtil;
import datamining.utils.MyMathUtil;
import datamining.main.MyMedousa;
import datamining.main.MyProgressBar;
import datamining.utils.system.MyVars;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MyNodeSearchAndDownloadPanel
extends JPanel
implements ActionListener {

    private JTextField searchText = new JTextField(35);
    private JLabel searchLabel = new JLabel("SEARCH NODE: ");
    private JTable targetTable;
    private JFrame owner;
    private MyMedousa main;
    private final JButton downloadBtn = new JButton();
    private final ImageIcon downloadImageIcon = new ImageIcon(MyVars.imgDir +"save.png");

    public MyNodeSearchAndDownloadPanel() {}
    public MyNodeSearchAndDownloadPanel(
            final MyMedousa main,
            final JFrame owner,
            final JTable targetTable) {
        this.targetTable = targetTable;
        this.main = main;
        this.owner = owner;
        this.createAndShowGui();
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == this.downloadBtn) {
            Thread downloadThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    JFileChooser fc = new JFileChooser();
                    owner.setAlwaysOnTop(false);
                    fc.setPreferredSize(new Dimension(600, 460));
                    fc.showSaveDialog(main);
                    if (fc.getSelectedFile() == null) {
                        MyMessageUtil.showErrorMsg("File not selected! \nChoose a file and start downloading the current information.");
                        owner.setAlwaysOnTop(true);
                        return;
                    } else {
                        MyProgressBar pb = new MyProgressBar(false);
                        long fileSize = 0L;
                        long pbCount = 0L;
                        long totalRowCount = targetTable.getRowCount();
                        try {
                            File csvOutput = new File(fc.getSelectedFile().getAbsolutePath());
                            BufferedWriter bw = null;
                            FileWriter fw = null;
                            try {
                                if (!csvOutput.exists()) {
                                    csvOutput.createNewFile();
                                } else {
                                    csvOutput.delete();
                                    csvOutput.createNewFile();
                                }
                                fw = new FileWriter(csvOutput, false);
                                bw = new BufferedWriter(fw);
                                String lineSeperator = System.getProperty("line.separator");
                                for (int row=0; row < targetTable.getRowCount(); row++ ) {
                                    String rowInfoString = "";
                                    for (int col=0; col < targetTable.getColumnCount(); col++) { // do not save "show button"
                                        if (rowInfoString.length() == 0) {
                                            rowInfoString = targetTable.getValueAt(row, col).toString();
                                        } else {
                                            rowInfoString = rowInfoString + "," + targetTable.getValueAt(row, col).toString();
                                        }
                                    }
                                    bw.write(rowInfoString + lineSeperator);
                                    pb.updateValue(++pbCount, totalRowCount);
                                    Thread.sleep(3);
                                }
                                bw.flush();
                                fileSize = csvOutput.length();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                try {
                                    if (bw != null) bw.close();
                                    if (fw != null) fw.close();
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                            }
                            pb.updateValue(100,100);
                            pb.dispose();
                            String message = "Information has been saved to file[" + fc.getSelectedFile().getName() + "] in directory[" + fc.getSelectedFile().getParent() + "] \n";
                            message = message + "File size: " + MyMathUtil.getCommaSeperatedNumber(fileSize) + " bytes" + "\n";
                            message = message + "Records: " + MyMathUtil.getCommaSeperatedNumber(totalRowCount);
                            MyMessageUtil.showInfoMsg(message);
                        } catch (Exception ex) {
                            pb.dispose();
                            String message = "Downloading information has failed with the following exception:\n";
                            message = message + ex.toString();
                            MyMessageUtil.showInfoMsg(message);
                        } finally {
                            owner.setAlwaysOnTop(true);
                        }
                    }
                }
            }); downloadThread.start();
        }
    }

    private void createAndShowGui() {
        this.searchText.setFont(new Font("Arial", Font.PLAIN, 11));
        this.searchLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        this.searchText.setPreferredSize(new Dimension(350, 30));
        this.searchText.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {}
            @Override
            public void keyReleased(KeyEvent e) {
                int pos = searchText.getCaretPosition();
                searchText.setText(searchText.getText().toUpperCase());
                searchText.setCaretPosition(pos);
            }
        });

        final TableRowSorter<TableModel> tableSorter = new TableRowSorter<>((DefaultTableModel)this.targetTable.getModel());
        this.targetTable.setRowSorter(tableSorter);

        this.searchText.getDocument().addDocumentListener(new DocumentListener(){
            @Override public void insertUpdate(DocumentEvent e) { search(searchText.getText()); }
            @Override public void removeUpdate(DocumentEvent e) { search(searchText.getText()); }
            @Override public void changedUpdate(DocumentEvent e) { search(searchText.getText()); }
            public void search(String s) {
                if (s.length() == 0) { tableSorter.setRowFilter(null); }
                else { tableSorter.setRowFilter(RowFilter.regexFilter(s)); }
            }
        });

       // this.setBorder(BorderFactory.createEtchedBorder());
        this.setLayout(new BorderLayout());

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchPanel.setPreferredSize(new Dimension(600, 40));
        searchPanel.add(searchLabel);
        searchPanel.add(searchText);

        downloadBtn.addActionListener(this);
        downloadBtn.setToolTipText("Download as csv");
        downloadBtn.setEnabled(true);
        downloadBtn.setIcon(downloadImageIcon);
        downloadBtn.setFocusable(false);

        JPanel downLoadPanel = new JPanel();
        downLoadPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        downLoadPanel.setPreferredSize(new Dimension(60, 34));
        downLoadPanel.add(downloadBtn);

        add(searchPanel, BorderLayout.WEST);
        add(downLoadPanel, BorderLayout.EAST);
        revalidate();
        repaint();
    }


    public static void main(String [] args) {
        JFrame fr = new JFrame();
        fr.setLayout(new BorderLayout());
        fr.getContentPane().add(new MyNodeSearchAndDownloadPanel(MyVars.main, fr, new JTable()), BorderLayout.CENTER);
        fr.setPreferredSize(new Dimension(550, 70));
        fr.pack();
        fr.setVisible(true);
    }


}
