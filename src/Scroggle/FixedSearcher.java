package Scroggle;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Stack;

public class FixedSearcher {
	public static Grid grid = GridSingleton.getGrid();
	public static int wordCounter = -1;
	static List<String> placedWords = new ArrayList<String>();
	static LinkedHashMap<String, Integer> words;
	static HashMap<String, Integer> valueMap;
	static boolean debugging = false;
	public static final boolean GREEDY = false;
	static double highScore = -1;
	static double KNOWN_HIGH_SCORE = 824669;
	static Cell startCell;
	

	public static void main(String[] args) {
		initializeSearcher();
		System.out.println(words.size());
		for(String word : words.keySet()) {
			System.out.println(word + " " + valueMap.get(word));
		}
	
		System.out.println("-------------------------------------");
		
		long start = System.currentTimeMillis();
		performBruteForce();
		long end = System.currentTimeMillis();
		System.out.println("Time millis: " + (end-start));
	
	}
	
	public static void initializeSearcher() {
	String path = "validWords_17.txt";
	File wordFile = new File(path);
	ScrabbleWordList.buildQueue(wordFile);
	valueMap = ScrabbleWordList.getMap();
	words = ScrabbleWordList.getSortedMap();
	grid.linkNeighbors();
	}
	
	public static void performBruteForce() {
		for(int i = 2; i > -1; i--) {
			for(int j = 2; j > -1; j--) {
				performBruteForce(i,j);
				grid.resetGrid();
				wordCounter = -1;
				placedWords.clear();
				System.out.println("Search completed for (" + i +", " + j +")");
				System.out.println("-------------------------------------");
			}
		}
	}

	public static void performBruteForce(int i, int j) {
		if(i < 0 || i > 2 || j < 0 || j > 2) {
			System.out.println("(" + i + ", " + j + ") is invalid start coordinate.");
		} else {
			startCell = grid.table[i][j];
			placeNextWord(startCell);
		}
	}
	

	private static boolean placeLetter(Stack<Character> letters, Cell cell) {
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
			grid.setValue(letters.pop(), cell, wordCounter, letters.size());
			return true;
		}
	}

	
	public static Cell placeLetters(Stack<Character> letters, Cell cell) {
		Thread.yield();
		if(placeLetter(letters, cell) == false) return null;
		if(letters.isEmpty()) {
			if(wordEndsProperly(cell, wordCounter)) {
				Cell finalCell = placeNextWord(cell);
				if(finalCell == null) {
					wordCounter--;
					letters.push(grid.clearValue(cell, wordCounter, letters.size()));
				}
				return finalCell;
			} else {
				letters.push(grid.clearValue(cell, wordCounter, letters.size()));
				return null;
			}
			
		} else {
			Cell placement;
			for(Cell neighbor : cell.neighbors) {
				placement = placeLetters(letters, neighbor);
				if(placement != null) {
					return placement;
				}
			}
			letters.push(grid.clearValue(cell, wordCounter, letters.size()));
			return null;
		}
	}
	
	public static Cell placeNextWord(Cell cell) {
		wordCounter++;
		if(wordCounter == 4) {
			if(GREEDY) {
				return cell;
			} else {
				successfulPlacement(cell);
				return null;
			}	
		}
		
		for(String word : words.keySet()) {	
			if(!placedWords.contains(word) 
					&& validateScores(word, wordCounter)
					) {
				placedWords.add(word);
				Stack<Character> stack =  buildLetterStack(word);
				Cell placement = placeLetters(stack, cell);
				Thread.yield();
				if(placement!= null) {
					return placement;
				}
				placedWords.remove(word);
			}
		}
		
		return null;
	}
	
	
	private static boolean validateScores(String word, int wordNumber) {
		/***
		 * The greedy algorithm will return the first working word sequence it finds
		 * before discovering any better configurations with a lower first-word value. 
		 * E.g.,  we might get a sequence of scores (30, 30, 30, 30) -> 810000 
		 * 		when there could be a sequence with (25, 34, 34, 34) -> 982600.
		 * This lets us experiment with word value cut-offs to counter this.
		 * 
		 * For the non-greedy (generous?) algorithm, the search space is huge.
		 * If we know the score we're trying to beat, we can check each word
		 * to see if it's even worth placing it before doing so, which considerably
		 * speeds up the exhaustive search.
		 */
		
		int score = valueMap.get(word);
		if(wordNumber == 0) {
			return 
					true
					//score == 38
					;
		} else if(wordNumber == 1) {
			int x0 = valueMap.get(placedWords.get(0));
			return 
					//true
					(score > (KNOWN_HIGH_SCORE/(1406 * x0)))
					;
		} else if(wordNumber == 2) {
			int x0 = valueMap.get(placedWords.get(0));
			int x1 = valueMap.get(placedWords.get(1));
			return 
					//true
					(score > (KNOWN_HIGH_SCORE/(38 * x0 * x1)))
					;
		} else {
			int x0 = valueMap.get(placedWords.get(0));
			int x1 = valueMap.get(placedWords.get(1));
			int x2 = valueMap.get(placedWords.get(2));
			return 
					//true
					(score > (KNOWN_HIGH_SCORE/(x0 * x1 * x2)))
					;
		}
	}
	
	private static void successfulPlacement(Cell cell) {
		double score = 1;
		for(String word : placedWords) {
			score*= valueMap.get(word);
		}
		if(score >= highScore) {
			System.out.println("Search Result (main method): " + cell + " for start cell: " + startCell);
			System.out.println(placedWords);
			System.out.println("Score: " + score);
			grid.printGrid();
				
			if(score > KNOWN_HIGH_SCORE) System.out.println("NEW HIGH SCORE!!!!!!!" + score);
			highScore = score;
		}
		
	}
		

	private static void goToDebug() {
		System.out.println("Breakpoint reached in goToDebug()");
		sleep(1000);
		debugging = true;
	}
	
	private static void sleep(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private static Stack<Character> buildLetterStack(String word){
		Stack<Character> letters = new Stack<Character>();
		for(int i = word.length(); i > 0; i--) {
				letters.push(word.charAt(i-1));
			}
		return letters;
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
		
}
