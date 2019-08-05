package Scroggle;

import java.util.HashSet;


public class Cell {
	int row;
	int column;
	public static final char EMPTY = ' ';
	char value = EMPTY;
	int indexInEachWord[] = {-1,-1,-1,-1};
	boolean isVowel;
	HashSet<Cell> neighbors = new HashSet<Cell>();
	int wordNumber = -1;
		
	public void setValue(char newVal) {
		if(!isVowel) {
			value = newVal;
			if(value == 'A' || value == 'E' || value == 'I' || value == 'O' || value == 'U') {
				isVowel = true;
			}
		}
	}
	
	public boolean wasFilledByWordNumber(int wordNumber) {
		return this.wordNumber == wordNumber;
	}
	
	public void setValue(char newVal, int wordNumber, int index) {
		setValue(newVal, wordNumber);
		indexInEachWord[wordNumber] = index;

	}
	
	public void setValue(char newVal, int wordNumber) {
		if(!isVowel) {
			value = newVal;
			if(value == 'A' || value == 'E' || value == 'I' || value == 'O' || value == 'U') {
				isVowel = true;
			}
		}	
		if(this.wordNumber == -1) this.wordNumber = wordNumber;
	}
	
	public char eraseValue(int wordNumber) {
		indexInEachWord[wordNumber] = -1;
		this.wordNumber = -1;
		if(!isVowel){
			char oldVal = value;
			value = EMPTY;
			return oldVal;
		} else {
			return value;
		}
	}
	
	public char eraseValue() {
		this.wordNumber = -1;
		if(!isVowel){
			char oldVal = value;
			value = EMPTY;
			return oldVal;
		} else {
			return value;
		}
	}
	
	public Cell(char val, int r, int c) {
		value = val;
		if(value == 'A' || value == 'E' || value == 'I' || value == 'O' || value == 'U') {
			isVowel = true;
		}
		row = r;
		column = c;
	}
	
	public Cell(int r, int c) {
		row = r;
		column = c;
	}
	
	public boolean isFilled() {
		return value != EMPTY;
	}
	
	public boolean addNeighbor(int row, int column) {
		if(row < 0 || row > 5 || column < 0 || column > 5) {
			return false;
		} else if(row == this.row && column == this.column) {
			return false;
		} else {
			neighbors.add(GridSingleton.getGrid().table[row][column]);
			return true;
		}
	}
	
	public int getRow() {
		return row;
	}
	public int getColumn() {
		return column;
	}
	
	public boolean isInRed() {
		return GridSingleton.getGrid().isInRed(row, column);
	}
	
	public boolean isInBlue() {
		return GridSingleton.getGrid().isInBlue(row, column);
	}
	
	public boolean isInGreen() {
		return GridSingleton.getGrid().isInGreen(row, column);
	}
	
	public boolean isInPurple() {
		return GridSingleton.getGrid().isInPurple(row, column);
	}
	
	@Override
	public String toString() {
		return ("Row: "+ row + " Column: " + column + " Value: " + value + ".");
	}


	
}
