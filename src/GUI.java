package src;
import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI {
    private JFrame frame;
    private JPanel panel;
    private Minesweeper game;
    private JButton[][] buttons;
    private int rows;
    private int cols;
    private ImageIcon bomb;
    private JPanel topPanel;

    public GUI(int rows, int cols, int totalMines) {
        this.rows=rows;
        this.cols=cols;
        game = new Minesweeper(rows, cols, totalMines);
        initGUI();
    }

    private void initGUI() {
        frame = new JFrame("Minesweeper");
        panel = new JPanel(new GridLayout(rows, cols));
        buttons = new JButton[rows][cols];
        bomb = new ImageIcon("bomb.png");
        topPanel = new JPanel();
        topPanel.setOpaque(true);
        topPanel.setBackground(Color.BLACK);


        for(int i=0; i<rows; i++) {
            for(int j=0; j<cols; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setPreferredSize(new Dimension(50,50));
                buttons[i][j].setFocusPainted(false);
                buttons[i][j].addActionListener(new ClickListener(i,j));
                panel.add(buttons[i][j]);
            }
        }
        frame.add(topPanel);
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private class ClickListener implements ActionListener{
        private int row;
        private int col;

        public ClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            leftClick(row, col);
        }
    }

    private void leftClick(int row, int col) {
        if (game.uncoverCell(row, col)) {
            
            updateGrid();
            if (game.isWon()) {
                JOptionPane.showMessageDialog(frame, "You won!");
            } else if (game.isLost()) {
                JOptionPane.showMessageDialog(frame, "Game over!");
                revealMines();
            }
        }
    }

    private void updateGrid() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Cell cell = game.getGrid()[i][j];
                if (!cell.isCovered()) {
                    if (cell.isMine()) {
                        buttons[i][j].setIcon(bomb);
                    } else if(cell.getAdjacentMines()!=0){
                        buttons[i][j].setText(String.valueOf(cell.getAdjacentMines()));
                    } else {
                        buttons[i][j].setText("");
                    }
                    buttons[i][j].setEnabled(false);
                } else {
                    buttons[i][j].setText("");
                    buttons[i][j].setEnabled(true);
                }
            }
        }
    }

    private void revealMines() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (game.getGrid()[row][col].isMine()) {
                    buttons[row][col].setIcon(bomb);
                }
            }
        }
    }
}
