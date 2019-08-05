package Scroggle;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Stack;

public class SearchTester {
	public static Grid grid = GridSingleton.getGrid();
	public static int wordCounter = 0;
	Set<String> placedWords = new HashSet<String>(4);
	static LinkedHashMap<String, Integer> words;
	static HashMap<String, Integer> valueMap;
	static boolean debugging = true;
	static BufferedWriter writer;
	static Cell startCell;
	static String currentWord;
	static boolean discoveredPlacement;
	

	public static void initializeSearcher() {
	String path = "gitlist.txt";
	File wordFile = new File(path);
	ScrabbleWordList.buildQueue(wordFile);
	valueMap = ScrabbleWordList.getMap();
	words = ScrabbleWordList.getSortedMap();
	grid.linkNeighbors();
	Charset charset = Charset.forName("US-ASCII");
	File validWords = new File("out.txt");
	try {
		writer = Files.newBufferedWriter(validWords.toPath(), charset, StandardOpenOption.APPEND);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	
	public static void main(String[] args) {
		initializeSearcher();
					
		System.out.println(words.size());	
		System.out.println("-------------------------------------");
		for(String word : SearchTester.words.keySet()) {
			isPlaceable(word);
		}
		try {
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void goToDebug() {
		System.out.println("Breakpoint reached in goToDebug()");
		debugging = true;
	}
	
	public static Cell placeLetters(Stack<Character> letters, Cell cell) {
		Thread.yield();
		if(placeLetter(letters, cell, true) == false) return null;
		if(letters.isEmpty()) {
			return cell;	
		} else {
			Cell placement;
			for(Cell neighbor : cell.neighbors) {
				if(discoveredPlacement) break;
				placement = placeLetters(letters, neighbor);
				if(placement != null) {
					verifyAndStorePlacement(letters, placement);
				}
			}
			letters.push(grid.clearValue(cell,0, letters.size()));
			return null;
		}
	}
	
	private static void verifyAndStorePlacement(Stack<Character> letters, Cell placement) {
		if(wordBeginsAndEndsProperly(startCell, placement)) {
			discoveredPlacement = true;
			try {
				writer.write(currentWord);
				writer.newLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		letters.push(grid.clearValue(placement, 0, letters.size()));
		
	}

	private static void sleep(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private static boolean placeLetter(Stack<Character> letters, Cell cell, boolean testing) {
	//	if(letters.isEmpty() || letters.peek() == 'S') goToDebug();
		Thread.yield();
		if(cell.isFilled()) {
			if(cell.value == letters.peek()) {
				letters.pop();
				return true;
			} else {
				return false;
			}
		} else if (grid.containsLetter(letters.peek())) {
			return false;
		} else {
			grid.setValue(letters.pop(), cell, 0, letters.size());
			return true;
		}
	}
	
	public static boolean wordEndsProperly(Cell cell, int wordNumber) {
		if(wordNumber == 0) {
			return cell.isInRed() && cell.isInBlue();
		} else if(wordNumber == 1) {
			return cell.isInBlue() && cell.isInGreen();
		} else if(wordNumber == 2) {
			return cell.isInGreen() && cell.isInPurple();
		} else if(wordNumber == 3) {
			return cell.isInPurple();
		} else {return false;}
	}
	
	public static boolean wordBeginsAndEndsProperly(Cell start, Cell end) {
		if(start.isInRed() && wordEndsProperly(end,0)) {
			return true;
		} else if(wordEndsProperly(start,0) && wordEndsProperly(end,1)) {
			return true;
		} else if(wordEndsProperly(start,1) && wordEndsProperly(end,2)) {
			return true;
		} else if(wordEndsProperly(start,2) && end.isInPurple()) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isPlaceable(String word) {
		//System.out.println("Checking "+ word);
		currentWord = word;
		discoveredPlacement = false;
		Stack<Character> stack;
		for(int k = 0; k < 4; k++) {
			for(int i = 0; i < 3; i++) {
				for(int j = 0; j < 3; j++) {
					if(!discoveredPlacement) {
					startCell = grid.table[k+i][k+j];
					stack =  buildLetterStack(word);
					placeLetters(stack, startCell);
					grid.resetGrid();
					}
				}
			}
		}
		return false;
	}
	
	
	private static Stack<Character> buildLetterStack(String word){
		Stack<Character> letters = new Stack<Character>();
		for(int i = word.length(); i > 0; i--) {
				letters.push(word.charAt(i-1));
			}
		return letters;
	}
	
	
}
