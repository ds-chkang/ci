package medousa.table;

import javax.swing.*;
import java.awt.*;

public class MyVariableBindingColumnEditor
extends DefaultCellEditor {

    // Declare a model that is used for adding the elements to the `Combo box`
    private DefaultComboBoxModel model;
    private String [] headers;
    private boolean isUpdate;

    public MyVariableBindingColumnEditor(
            String [] headers, boolean isUpdate) {
        super(new JComboBox());
        this.headers = headers;
        this.isUpdate = isUpdate;
        if (getComponent() instanceof JComboBox) {
            ((JComboBox) getComponent()).setFocusable(false);
            ((JComboBox) getComponent()).setBackground(Color.ORANGE);
        }
        model = (DefaultComboBoxModel) ((JComboBox)getComponent()).getModel();

    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.getComponent().setBackground(Color.WHITE);
        if (isUpdate && model.getSize() == 0) {
            for (int i = 0; i < headers.length; i++) {
                model.addElement(" "+headers[i]);
            }
        }
        isUpdate = false;
        return super.getTableCellEditorComponent(table, value, isSelected, row, column);
    }
}
