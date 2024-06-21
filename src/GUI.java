package src;
import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GUI {
    private JFrame frame;
    private JPanel panel;
    private Minesweeper game;
    private JButton[][] buttons;
    private int rows;
    private int cols;
    private ImageIcon bomb;
    private ImageIcon flag;

    private JLabel flagLabel;
    
    private JPanel topPanel;

    private JLabel timerLabel;
    private JLabel flagsRemainingLabel;
    private Timer timer;

    private int numFlags;

    public GUI(int rows, int cols, int totalMines) {
        this.rows=rows;
        this.cols=cols;
        game = new Minesweeper(rows, cols, totalMines);
        this.numFlags=game.getNumBombs();
        initGUI();
    }

    private void initGUI() {
        frame = new JFrame("Minesweeper");
        panel = new JPanel(new GridLayout(rows, cols, -4, -4));
        buttons = new JButton[rows][cols];
        bomb = new ImageIcon("bomb.png");

        topPanel = new JPanel();
        topPanel.setOpaque(true);
        topPanel.setBackground(new Color(0xe0e0e0));
        topPanel.setPreferredSize(new Dimension(frame.getWidth(), 60));

        flag = new ImageIcon("flag.png");
        flagLabel = new JLabel(flag);

        flagsRemainingLabel = new JLabel(String.valueOf(numFlags));
        flagsRemainingLabel.setFont(new Font("MV Boli", Font.PLAIN, 45));

        JPanel flagPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        flagPanel.setOpaque(false);
        flagPanel.add(flagsRemainingLabel);
        flagPanel.add(flagLabel);

        timerLabel = new JLabel("Time: 0s");
        timerLabel.setFont(new Font("MV Boli", Font.PLAIN, 40));

        topPanel.setLayout(new BorderLayout());
        topPanel.add(flagPanel, BorderLayout.CENTER);
        topPanel.add(timerLabel, BorderLayout.EAST);

        for(int i=0; i<rows; i++) {
            for(int j=0; j<cols; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setPreferredSize(new Dimension(50,50));
                buttons[i][j].setFocusPainted(false);
                buttons[i][j].addActionListener(new ClickListener(i,j));
                buttons[i][j].addMouseListener(new RightClickListener(i,j));
                panel.add(buttons[i][j]);
            }
        }
        frame.setLayout(new BorderLayout());
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        startTimer();
    }

    private void startTimer() {
        timer = new Timer(1000, new ActionListener() {
            private int elapsedSeconds = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                elapsedSeconds++;
                timerLabel.setText("Time: " + elapsedSeconds + "s");
            }
        });
        timer.start();
    }

    private void resetTimer() {
        if (timer != null) {
            timer.stop();
        }
        timerLabel.setText("Time: 0s");
        startTimer();
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

    private class RightClickListener extends MouseAdapter {
        private int row;
        private int col;

        public RightClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (SwingUtilities.isRightMouseButton(e)) {
                rightClick(row, col);
            }
        }
    }

    private void leftClick(int row, int col) {
        int response=10;
        String[] options = {"EXIT", "NO", "YES"};
        if (game.uncoverCell(row, col)) {
            updateGrid();
            if (game.isWon()) {
                timer.stop();
                response = JOptionPane.showOptionDialog(null, "Would you like to play again?", "You won!", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, null);
                revealMines();
                disableButtons();
            } else if (game.isLost()) {
                timer.stop();
                response = JOptionPane.showOptionDialog(null, "Would you like to play again?", "Game Over!", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, null);
                revealMines();
                disableButtons();
            }

            if(response==2) {
                resetGame();
            } else if(response==0) {
                System.exit(0);
            }
        }
    }

    private void rightClick(int row, int col) {
        Cell cell = game.getGrid()[row][col];
        if (cell.isCovered() && !cell.isFlagged() && numFlags>0) {
            cell.setFlagged(true);
            buttons[row][col].setIcon(flag);
            numFlags--;
        } else if (cell.isCovered() && cell.isFlagged()) {
            cell.setFlagged(false);
            buttons[row][col].setIcon(null);
            numFlags++;
        }
        flagsRemainingLabel.setText(String.valueOf(numFlags));
    }

    private void disableButtons() {
        for(int i=0; i<rows; i++) {
            for(int j=0; j<cols; j++) {
                if(buttons[i][j].isEnabled()) {
                    buttons[i][j].setEnabled(false);
                    buttons[i][j].setEnabled(false);
                    buttons[i][j].setBackground(Color.lightGray);
                    buttons[i][j].setOpaque(true);
                }
            }
        }
    }

    private void updateGrid() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Cell cell = game.getGrid()[i][j];
                if (!cell.isCovered()) {
                    if(cell.isFlagged()) {
                        cell.setFlagged(false);
                        numFlags++;
                        buttons[i][j].setIcon(null);
                    }
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
        flagsRemainingLabel.setText(String.valueOf(numFlags));
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

    private void resetGame() {
        game = new Minesweeper(rows, cols, game.getNumBombs());
        numFlags = game.getNumBombs();
        flagsRemainingLabel.setText(String.valueOf(numFlags));
        resetTimer();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setIcon(null);
                buttons[i][j].setEnabled(true);
                buttons[i][j].setBackground(null);
            }
        }
    }
}
