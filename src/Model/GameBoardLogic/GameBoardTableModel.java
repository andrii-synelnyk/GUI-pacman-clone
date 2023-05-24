package Model.GameBoardLogic;

import javax.swing.table.AbstractTableModel;

import Enum.CellContent;

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
    public CellContent getValueAt(int rowIndex, int columnIndex) {
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