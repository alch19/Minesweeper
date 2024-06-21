package src;
import java.util.Random;

public class Minesweeper {
    private Cell[][] grid;
    private int rows;
    private int cols;
    private int totalMines;
    private boolean gameLost;
    private boolean gameWon;

    public Minesweeper(int rows, int cols, int totalMines) {
        this.rows=rows;
        this.cols=cols;
        this.totalMines=cols;
        this.gameLost=false;
        this.gameWon=false;
        initializeGrid();
        placeMines();
        calculateAdjacentMines();
    }

    public void coverCell(int row, int col, boolean bool) {
        grid[row][col].setCovered(bool);
    }

    private void initializeGrid() {
        grid = new Cell[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = new Cell();
            }
        }
    }

    private void placeMines() {
        Random random = new Random();
        int numMinesPlaced = 0;
        while(numMinesPlaced<totalMines) {
            int row = random.nextInt(rows);
            int col = random.nextInt(cols);
            if(grid[row][col].isMine()==false) {
                grid[row][col].setMine(true);
                numMinesPlaced++;
            }
        }
    }

    private void calculateAdjacentMines() {
        int[] dx = {-1, -1, -1, 0, 0, 1, 1, 1}; // x coords
        int[] dy = {-1, 0, 1, -1, 1, -1, 0, 1}; // y coords
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) { // iterates through every single cell
                if (!grid[row][col].isMine()) { // continues through loop if it is not a mine
                    int count = 0;
                    for (int k = 0; k < 8; k++) { // checks the 8 surrounding cells
                        int newRow = row + dx[k]; 
                        int newCol = col + dy[k];
                        if (isInBounds(newRow, newCol) && grid[newRow][newCol].isMine()) { // only adds to the counter if it is within the grid and is a mine
                            count++;
                        }
                    }
                    grid[row][col].setAdjacentMines(count);
                }
            }
        }
    }

    private boolean isInBounds(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    public boolean uncoverCell(int row, int col) {
        if (!isInBounds(row, col) || !grid[row][col].isCovered() || grid[row][col].isFlagged()) {
            return false;
        }

        grid[row][col].setCovered(false);

        if (grid[row][col].isMine()) {
            gameLost = true;
            return true;
        }

        if (grid[row][col].getAdjacentMines() == 0) { // if all coords around a revealead square are not mines, reveal them all
            int[] rowOffsets = {-1, -1, -1, 0, 0, 1, 1, 1};
            int[] colOffsets = {-1, 0, 1, -1, 1, -1, 0, 1};
            for (int direction = 0; direction < 8; direction++) {
                int neighborRow = row + rowOffsets[direction];
                int neighborCol = col + colOffsets[direction];
                uncoverCell(neighborRow, neighborCol); // calls recursion to check again
            }
        }
        checkWinCondition();

        return true;
    }

    private void checkWinCondition() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (!grid[i][j].isMine() && grid[i][j].isCovered()) {
                    return;
                }
            }
        }
        gameWon = true;
    }

    public boolean isWon() {
        return gameWon;
    }

    public boolean isLost() {
        return gameLost;
    }

    public Cell[][] getGrid() {
        return grid;
    }
}
