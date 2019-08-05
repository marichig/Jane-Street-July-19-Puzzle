package Scroggle;

import java.util.Comparator;
import java.util.HashMap;

public class ScrabbleWordComparator implements Comparator<String> {
	HashMap<String, Integer> wordToValueMap;
	
	public ScrabbleWordComparator(HashMap<String,Integer> aMap) {
		wordToValueMap = aMap;
	}
	
	@Override
	public int compare(String arg0, String arg1) {
		int val0 = wordToValueMap.get(arg0);
		int val1 = wordToValueMap.get(arg1);
		
		if(val0 > val1) {
			return -1;
		} else if (val0 < val1) {
			return 1;
		} else { //prioritize shorter words
			if(arg0.length() > arg1.length()){
				return 1;
			} else if(arg0.length() < arg1.length()){
				return -1;
			} else {
				return 0;
				}
		
		}
	}

}
