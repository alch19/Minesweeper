package src;

public interface CellUpdateCallBack {
    void onCellUpdated(int row, int col, int adjacentMines, boolean isMine);
}
