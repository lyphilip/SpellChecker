package spellChecker;

import java.util.*;
import java.util.regex.*;
import java.io.*;

public class SpellChecker {
	private final Map<String, Integer> Dictionary = new HashMap<String, Integer>();

	public SpellChecker(String file) throws IOException {
		//Read in English words and put them in the dictionary map
		BufferedReader in = new BufferedReader(new FileReader(file));
		Pattern p = Pattern.compile("\\w+");
		for(String temp = ""; temp != null; temp = in.readLine()){
			Matcher m = p.matcher(temp.toLowerCase());
			while(m.find()) 
				Dictionary.put((temp = m.group()), Dictionary.containsKey(temp) ? Dictionary.get(temp) + 1 : 1);
		}
		in.close();
	}

	private List<String> findAllVowelCombos(String word) {
		List<String> result = new ArrayList<String>();
		word = word.toLowerCase();
		
		//check incorrect vowels: a,e,i,o,u
		for(int i=0; i < word.length(); ++i){ 
			char c = word.charAt(i);
			if(isVowel(c)){
				result.add(word.substring(0, i) + String.valueOf('a') + word.substring(i+1));
				result.add(word.substring(0, i) + String.valueOf('e') + word.substring(i+1));
				result.add(word.substring(0, i) + String.valueOf('i') + word.substring(i+1));
				result.add(word.substring(0, i) + String.valueOf('o') + word.substring(i+1));
				result.add(word.substring(0, i) + String.valueOf('u') + word.substring(i+1));
			}
			//for(char c='a'; c <= 'z'; ++c){ 
			//	result.add(word.substring(0, i) + String.valueOf(c) + word.substring(i+1));
			//}
		}
		
		String dedupWord = word;
		char last = dedupWord.charAt(0);
		for(int i=1; i < dedupWord.length(); ++i){ 
			char current = dedupWord.charAt(i);
			if(last!=current){
				last = current;
			}else{//last == current, found dup chars here
				dedupWord = dedupWord.substring(0, i) + dedupWord.substring(i+1);
				result.add(dedupWord); //remove char at i				
				i--;//since we remove char at i, next char we check should have index of i, instead of i+1 in normal case
			}
			
		}
		return result;
	}
	
	private static boolean isVowel(char c) {
	    return (c == 'a') || (c == 'e') || (c == 'i') || (c == 'o') || (c == 'u');
	}
	
	private String removeDupLetters(String word){
		if(word.length()<=1){
			return word;
		}
		StringBuffer sb = new StringBuffer();
		char last = word.charAt(0);
		sb.append(last);
		for(int i=1;i<word.length();i++){
			char current = word.charAt(i);
			if(last!=current){
				sb.append(current);
				last = current;
			}
		}
		return sb.toString();
	}
	
	public String correct(String word) {
		if(Dictionary.containsKey(word)) return word;
		List<String> list = findAllVowelCombos(word);
		Map<Integer, String> candidates = new HashMap<Integer, String>();
		for(String s : list) 
			if(Dictionary.containsKey(s)) 
				candidates.put(Dictionary.get(s),s);
		if(candidates.size() > 0) 
			return candidates.get(Collections.max(candidates.keySet()));
		
		for(String s : list) 
			for(String w : findAllVowelCombos(s)) 
				if(Dictionary.containsKey(w)) 
					candidates.put(Dictionary.get(w),w);
		
		return candidates.size() > 0 ? candidates.get(Collections.max(candidates.keySet())) : "NO SUGGESTION";
	}

	public static void main(String args[]) throws IOException {
		if(args.length > 0) System.out.println((new SpellChecker("big.txt")).correct(args[0].toLowerCase()));
	}

	
	
}
