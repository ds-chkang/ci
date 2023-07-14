package medousa.table;

import medousa.direct.utils.MyDirectGraphVars;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MyHeaderColumnButtonRenderer
implements TableCellRenderer {

    private JButton btn;

    public MyHeaderColumnButtonRenderer(Object headerValue) {
        btn = new JButton("+");
        btn.setFont(MyDirectGraphVars.tahomaPlainFont16);
        btn.setText(headerValue.toString());
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("Hello, the world!");
                    }
                }).start();

            }
        });
    }

    @Override
    public Component getTableCellRendererComponent(
            JTable arg0,
            Object arg1,
            boolean arg2,
            boolean arg3,
            int arg4,
            int arg5) {
        return btn;
    }

}