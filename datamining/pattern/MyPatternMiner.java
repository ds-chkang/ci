package datamining.pattern;

import datamining.utils.message.MyMessageUtil;
import datamining.utils.system.MySysUtil;
import datamining.utils.system.MyVars;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Set;

public class MyPatternMiner
extends JFrame {

    private JTree patternTree;
    private DefaultTreeModel treeModel;

    public MyPatternMiner() {
        super("PATTERN EXPLORER");
        decorate();
    }

    private void decorate() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                setPreferredSize(new Dimension(700, 600));
                add(setPatternStructure(), BorderLayout.CENTER);
                pack();
                setAlwaysOnTop(true);
                setVisible(true);
                setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                setAlwaysOnTop(false);
            }
        });
    }

    private void decorateTree() {
        patternTree.setVisibleRowCount(100);
        for (int row=patternTree.getRowCount(); row>=0; row--) {
            patternTree.expandRow(row);
        }
    }

    private JTextField patternSearchTxt = new JTextField();

    private JPanel setPatternStructure() {
        final MyPatternMiner pm = this;
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout(3,3));
        contentPanel.setBackground(Color.WHITE);

        JPanel controlPanel = new JPanel();
        controlPanel.setBackground(Color.WHITE);
        controlPanel.setLayout(new BorderLayout(3,3));

        patternSearchTxt.setFont(MyVars.f_pln_13);
        patternSearchTxt.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        patternSearchTxt.setBackground(Color.WHITE);

        JButton runBtn = new JButton("RUN");
        runBtn.setFocusable(false);
        runBtn.setPreferredSize(new Dimension(52, 26));
        runBtn.setFont(MyVars.tahomaPlainFont12);
        runBtn.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        File [] files = MySysUtil.getFileList(MySysUtil.getWorkingDir() + MySysUtil.getDirectorySlash() + "patterns");
                        if (files.length == 0) {
                            int res = MyMessageUtil.showConfirmMessage(pm,
                                    "<html><body>If this is the first time to find patterns, this operation may take a while. <br>Would you like to execute the operation?</body></html>"
                            );
                            if (res == 1) {
                                MySPMEngine spmEngine = new MySPMEngine();
                                spmEngine.run();
                            }
                        }

                        MyMessageUtil.showInfoMsg(pm, "<html><body>This operation may take a while.</body></html>");
                        loadPatterns();
                        decorateTree();
                        contentPanel.add(new JScrollPane(patternTree), BorderLayout.CENTER);
                        contentPanel.revalidate();
                        contentPanel.repaint();
                    }
                }).start();
            }
        });
        controlPanel.add(patternSearchTxt, BorderLayout.CENTER);
        controlPanel.add(runBtn, BorderLayout.EAST);
        contentPanel.add(controlPanel, BorderLayout.NORTH);
        contentPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        return contentPanel;
    }

    private void loadPatterns() {
        try {
            Set<Character> pathLengthPosCharMap = MySPMEngine.itemsetPosMap.keySet();
            File [] files = MySysUtil.getFileList(MySysUtil.getWorkingDir() + MySysUtil.getDirectorySlash() + "patterns" + MySysUtil.getDirectorySlash());

            for (File f : files) {
                String root = f.getName().split(MyVars.contributionSymbol)[0];
                if (root.equals(patternSearchTxt.getText().replaceAll(" ", "").split("-")[0])) {
                    BufferedReader br = new BufferedReader(new FileReader(f));
                    String itemset = "";
                    char c = (char) br.read();
                    int itemsetPos = MySPMEngine.itemsetPosMap.get(c);
                    if (itemsetPos == -1) return;

                    String[] itemsets = new String[MyVars.mxDepth];
                    itemsets[0] = root;
                    long[] uniqueContributions = new long[MyVars.mxDepth];
                    uniqueContributions[0] = Long.parseLong(f.getName().split(MyVars.contributionSymbol)[1].split("!")[1].split("#")[0]);
                    String uniqueContribution = "";
                    boolean contributionFound = false;
                    int i = 0;

                    while ((c = (char) br.read()) != -1) {
                        if (pathLengthPosCharMap.contains(c)) {
                            uniqueContributions[itemsetPos] = Long.parseLong(uniqueContribution);
                            itemsets[itemsetPos] = itemset;
                            itemsetPos = MySPMEngine.itemsetPosMap.get(c);
                            itemset = "";
                            uniqueContribution = "";
                            contributionFound = false;
                        } else if (c >= 'A' && c <= 'I') {
                            if (itemset.length() == 0) {
                                itemset += MySPMEngine.charToIntMap.get(c);
                            } else {
                                itemset += "," + MySPMEngine.charToIntMap.get(c);
                            }
                        } else if (c >= '0' && c <= '9' && !contributionFound) {
                            itemset += c;
                        } else if (c == '@') {
                            contributionFound = true;
                        } else {
                            uniqueContribution += c;
                        }
                        if (++i == 100) System.exit(0);
                    }
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void addNodeToTree(String parent, String [] itemsets, int depth) {
        for (int j=1; j <= depth; j++) {

        }
    }

    private void setNodeToTree() {

    }

    public static void main(String [] args) {
        MyPatternMiner pm = new MyPatternMiner();
    }

}
