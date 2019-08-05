package Scroggle;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.PriorityQueue;

public class ScrabbleWordList {
	static HashMap<String, Integer> wordToValueMap = new HashMap<String, Integer>();
	static PriorityQueue<String> wordQueue = new PriorityQueue<String>(new ScrabbleWordComparator(wordToValueMap));
	static int[] charValuesByIndex = {1,3,3,2,1,4,2,4,1,8,5,1,3,1,1,3,10,1,1,1,1,4,4,8,4,10};
	static final int CUTOFF_VALUE_LOW = 17;
	
	public static void main(String[] args) {
		String path = "scrabble.txt";
		File wordFile = new File(path);
		ScrabbleWordList.buildQueue(wordFile);
		LinkedHashMap<String, Integer> words = getSortedMap();
		int[] firstLetterFrequencies = new int[26];
		int[] lastLetterFrequencies = new int[26];
		for(String word : words.keySet()) {
			firstLetterFrequencies[word.charAt(0) - 'A']++;
			lastLetterFrequencies[word.charAt(word.length()-1) - 'A']++;
		}
		System.out.println("First Letters: " + printArray(firstLetterFrequencies));
		System.out.println("Last Letters: " + printArray(lastLetterFrequencies));
	}
	
	private static String printArray(int[] array) {
		StringBuilder builder = new StringBuilder("[");
		for(int i = 0; i < array.length; i++) {
			builder.append((char) (i + 'A'));
			builder.append(": ");
			builder.append(array[i]);
			builder.append(", ");
		}
		builder.delete(builder.length()-2, builder.length());
		builder.append("]");
		return builder.toString();
	}
	
	public static void buildQueue(File wordList) {
		Charset charset = Charset.forName("US-ASCII");
		int val;
		try (BufferedReader reader = Files.newBufferedReader(wordList.toPath(), charset)) {
		    String line = null;
		    while ((line = reader.readLine()) != null) {
		    	line = line.toUpperCase();
		    	val = getScrabbleValue(line);
		    	if(val != -1) {
		    		wordToValueMap.put(line, val);
		    		wordQueue.add(line);
		    	}
		    }
		} catch (IOException x) {
		    System.err.format("IOException: %s%n", x);
		}
	}
	
	public static PriorityQueue<String> getQueue() {
		return wordQueue;
	}
	
	public static HashMap<String, Integer> getMap() {
		return wordToValueMap;
	}
	
	public static LinkedHashMap<String, Integer> getSortedMap(){
		//destroys priority queue
		LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();
		while(!wordQueue.isEmpty()) {
			sortedMap.put(wordQueue.peek(), wordToValueMap.get(wordQueue.poll()));
		}
		
		return sortedMap;
	}
	
	public static int getScrabbleValue(String word) {
		//bad separation of concerns, but weeds out low scoring words while calculating score
		int value = 0;
		char lastChar = 'a';
		for(int i = 0; i < word.length(); i++) {
			char c = word.charAt(i);
			if(lastChar == 'a' && c == 'U') {
	//			System.out.println("First letter is U in " + word + ", impossible with board config.");
				return -1;
			}
			
			if(c == lastChar) {
	//			System.out.println("Double Letter Encountered in Word: " + word + " at character " + c);
				return -1;
			} else if(lastChar == 'U') {
				if(c == 'I' || c == 'O') {
	//				System.out.println("U followed by I or O in " + word + ": impossible with board config.");
					return -1;
				}
			} else if(c == 'U') {
				if(lastChar == 'O' || lastChar == 'I') {
	//				System.out.println("I or O followed by U in " + word + ": impossible with board config.");
					return -1;
				} else if(i < 2 || word.length() - i < 3) {
	//				System.out.println("U in first or last 2 letters in " + word + ": impossible with board config.");
					return -1;
				}
			} 
			
			value += charValuesByIndex[c - 'A'];
			lastChar = c;
		}
		if(lastChar == 'U') {
	//		System.out.println("Last letter is U in " + word + ", impossible with board config.");
			return -1;
		}
		
		//greedy check 
		if(value < CUTOFF_VALUE_LOW ) {
			return -1;
		}
		
		return value;
	}
	

}
