package src;

public class Cell {
    private boolean mine;
    private int adjacentMines;
    private boolean covered;
    private boolean flagged;

    public Cell()
    {
        this.mine=false;
        this.adjacentMines=0;
        this.covered=true;
        this.flagged=false;
    }

    public boolean isMine()
    {
        return mine;
    }

    public int getAdjacentMines()
    {
        return adjacentMines;
    }
    
    public boolean isCovered()
    {
        return covered;
    }

    public boolean isFlagged()
    {
        return flagged;
    }

    public void setMine(boolean mine)
    {
        this.mine=mine;
    }

    public void setAdjacentMines(int adjacentMines)
    {
        this.adjacentMines=adjacentMines;
    }

    public void setCovered(boolean covered)
    {
        this.covered=covered;
    }

    public void setFlagged(boolean flagged)
    {
        this.flagged=flagged;
    }
}
