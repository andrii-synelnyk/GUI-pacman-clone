package Model;

import javax.swing.table.AbstractTableModel;

public class GameBoardTableModel extends AbstractTableModel {
    private GameBoard gameBoard;

    public GameBoardTableModel(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    @Override
    public int getRowCount() {
        return gameBoard.getRowCount();
    }

    @Override
    public int getColumnCount() {
        return gameBoard.getColumnCount();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return gameBoard.getCell(rowIndex, columnIndex).getContent();
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
}