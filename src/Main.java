package src;

import java.awt.BorderLayout;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Main {
    public static void main(String[] args) {
        JTextField rowsField = new JTextField(5);
        JTextField colsField = new JTextField(5);
        JTextField minesField = new JTextField(5);

        JPanel panel = new JPanel(new BorderLayout(5,5));

        JPanel myPanel = new JPanel();
        myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.X_AXIS));
        myPanel.add(new JLabel("Rows:"));
        myPanel.add(rowsField);
        myPanel.add(Box.createHorizontalStrut(15));
        myPanel.add(new JLabel("Columns:"));
        myPanel.add(colsField);
        myPanel.add(Box.createHorizontalStrut(15));
        myPanel.add(new JLabel("Mines:"));
        myPanel.add(minesField);

        panel.add(myPanel, BorderLayout.CENTER);

        int result = JOptionPane.showConfirmDialog(null, myPanel, "Please enter the GRID SIZE and number of mines", JOptionPane.OK_CANCEL_OPTION);
        if(result==JOptionPane.OK_OPTION) {
            int rows = Integer.parseInt(rowsField.getText());
            int cols = Integer.parseInt(colsField.getText());
            int mines = Integer.parseInt(minesField.getText());

            new GUI(rows, cols, mines);
        } else {
            System.exit(0);
        }
    }
}