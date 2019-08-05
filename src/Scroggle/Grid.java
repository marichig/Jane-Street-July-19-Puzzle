package Scroggle;

import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JTable;

public class Grid {
	boolean showModel = false;
	public CustomTableModel model = new CustomTableModel(6, 6);
	 Cell[][] table = new Cell[6][6];
	public static final char EMPTY = ' ';
	int[] placedLetters = {1,0,0,0,1,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0};
	
	

	public void setValue(char aValue, Cell cell, int wordNumber, int index) {
		cell.setValue(aValue, wordNumber, index);
		if(showModel) model.setValueAt(aValue, cell.row, cell.column);
		updatePlacedLetters(aValue);
	}
	
	public void setValue(Character aValue, Cell cell) {
		cell.setValue(aValue);
		if(showModel) model.setValueAt(aValue, cell.row, cell.column);
		updatePlacedLetters(aValue);
	}
	
	public void setValue(char aValue, int row, int column) {
		table[row][column].setValue(aValue);
		if(showModel) model.setValueAt(aValue, row, column);
		updatePlacedLetters(aValue);
	}
	
	public char clearValue(Cell cell, int wordNumber, int index) {
		if(!cell.isVowel && cell.isFilled()) {
			if(cell.wordNumber == wordNumber && cell.indexInEachWord[wordNumber] == index) {
			placedLetters[cell.value - 'A'] = 0;
			if(showModel) model.setValueAt(EMPTY, cell.row, cell.column);
			return cell.eraseValue(wordNumber);
			} else {
				return cell.value;
			}
		} else if (cell.isVowel) {
			return cell.eraseValue();
		} else {
			return EMPTY;
		}
	}
	
	public char clearValue(Cell cell) {
		cell.indexInEachWord = new int[]{-1,-1,-1,-1};
		if(!cell.isVowel && cell.isFilled()) {
			placedLetters[cell.value - 'A'] = 0;
			if(showModel) model.setValueAt(EMPTY, cell.row, cell.column);	
		} 
		return cell.eraseValue();
	}
	
	public Grid() {
		if(showModel) displayGrid();
		buildCells();
		initializeTable();
	}
	
	public void printGrid() {
		for(int i = 0; i < 6; i++) {
			for(int j = 0;j < 6; j++) {
				System.out.print(table[i][j].value);
			}
			System.out.println();
		}
	}
		
	private void buildCells() {
		for(int i = 0; i < 6; i++) {
			for(int j = 0; j < 6; j++) {
				table[i][j] = new Cell(i,j);
			}
		}
	}
	
	public void linkNeighbors() {
		for(int i = 0; i < 6; i++) {
			for(int j = 0; j < 6; j++) {
				for(int k = -1; k <= 1; k++) {
					for(int l = -1; l <= 1; l++) {
						table[i][j].addNeighbor(i+k,j+l);
					}
				}
			}
		}
	}
		
	private void updatePlacedLetters(char val) {
		if(val >= 'A' && val <='Z') {
			placedLetters[val - 'A'] = 1;
		}
	}
	
	public boolean containsLetter(char letter) {
		if(letter < 'A' && letter >'Z') return false;
		if(letter == EMPTY) {
			return false;
		} else {
		return placedLetters[letter - 'A'] == 1;
		}
	}
		
	public void resetGrid() {	
		for(int i = 0; i < 6; i++) {
			for(int j = 0; j < 6; j++) {
				clearValue(table[i][j]);
			}
		}
		placedLetters = new int[]{1,0,0,0,1,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0};
	}
		
	public char getValue(int row, int column) {
		return table[row][column].value;
	}
		
	public boolean isInRed(int row, int column) {
		return (row < 3 && column < 3);
	}
	
	public boolean isInBlue(int row, int column) {
		return (row > 0 && row < 4 && column > 0 && column < 4);
	}
	
	public boolean isInGreen(int row, int column) {
		return (row > 1 && row < 5 && column > 1 && column < 5);
	}
	
	public boolean isInPurple(int row, int column) {
		return (row > 2 && column > 2);
	}
	
	private void displayGrid() {	
		JFrame frame = new JFrame("Scroggle");
		
		frame.setSize(700, 700);
		
		JTable table = new JTable(model);
		table.setFont(new Font("Arial", Font.BOLD, 20));
		initializeModel();
		
		table.setRowHeight(100);
		for(int i = 0; i< 6; i++) {
		table.getColumnModel().getColumn(i).setPreferredWidth(100);
		}
		table.setVisible(true);
		frame.add(table);
		frame.setVisible(true);
		
		
	}
	
	private void initializeModel() {
		model.setValueAt('O', 0, 1);
		model.setValueAt('E', 0, 3);
		model.setValueAt('U', 0, 5);
		model.setValueAt('I', 1, 0);
		model.setValueAt('A', 1, 2);
		model.setValueAt('A', 1, 4);
		model.setValueAt('E', 2, 1);
		model.setValueAt('I', 2, 3);
		model.setValueAt('O', 2, 5);
		model.setValueAt('A', 3, 0);
		model.setValueAt('O', 3, 2);
		model.setValueAt('E', 3, 4);
		model.setValueAt('E', 4, 1);
		model.setValueAt('A', 4, 3);
		model.setValueAt('I', 4, 5);
		model.setValueAt('U', 5, 0);
		model.setValueAt('I', 5, 2);
		model.setValueAt('O', 5, 4);		
	}

	private void initializeTable() {
		table[0][1].setValue('O');
		table[0][3].setValue('E');
		table[0][5].setValue('U');
		table[1][0].setValue('I');
		table[1][2].setValue('A');
		table[1][4].setValue('A');
		table[2][1].setValue('E');
		table[2][3].setValue('I');
		table[2][5].setValue('O');
		table[3][0].setValue('A');
		table[3][2].setValue('O');
		table[3][4].setValue('E');
		table[4][1].setValue('E');
		table[4][3].setValue('A');
		table[4][5].setValue('I');
		table[5][0].setValue('U');
		table[5][2].setValue('I');
		table[5][4].setValue('O');
			
	}
	
}
