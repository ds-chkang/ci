package datamining.main;

import datamining.utils.system.MyVars;
import org.jfree.ui.RefineryUtilities;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class MyLogoPanel
extends JPanel {

    private Image logoImage = null;
    private static JFrame logoFrame = null;
    private final String logoDir = "/images/logo.png";

    public MyLogoPanel() { this.doInit(); }

    private void doInit() {
        try {
            this.setLayout(new BorderLayout());
            this.setPreferredSize(new Dimension(550, 240));
            this.logoImage = ImageIO.read(getClass().getResourceAsStream((logoDir)));
            this.setBackground(Color.WHITE);
            this.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
            this.setBounds(new Rectangle(3,3, 547, 237));
        } catch (IOException ex) { ex.printStackTrace(); }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawImage(this.logoImage, 20, 30, this);
        g2d.setFont(MyVars.f_bold_22);
        g2d.drawString("medousa.net", 230, 90);
        g2d.setFont(MyVars.f_pln_16);
        g2d.drawString("Ver 1.0", 230, 150);
        g2d.setFont(MyVars.f_pln_16);
        g2d.drawString("Developed by medousa.net in Oct, 2013", 230, 170);
        g2d.setFont(MyVars.f_pln_16);
        g2d.drawString("", 370, 165);
    }

    public static void launchLogo(MyMedousa main) {
        logoFrame = new JFrame();
        MyLogoPanel logoPanel = new MyLogoPanel();
        logoPanel.setBorder(BorderFactory.createLoweredSoftBevelBorder());
        logoPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        logoFrame.setBackground(Color.decode("#ffffff"));
        logoFrame.setUndecorated(true);
        logoFrame.setLayout(new BorderLayout());
        logoFrame.getContentPane().add(logoPanel);
        logoFrame.pack();
        logoFrame.setAlwaysOnTop(true);
        logoFrame.setLocationRelativeTo(main);
        logoFrame.setResizable(false);
        RefineryUtilities.centerFrameOnScreen(logoFrame);
        if (logoFrame.isDisplayable()) logoFrame.setVisible(true);
    }

    public static void disposeLogo() {
        logoFrame.dispose();
        logoFrame = null;
    }

    public static void main(String [] args) {}
}
