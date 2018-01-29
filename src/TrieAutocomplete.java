import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.TreeMap;

/**
 * General trie/priority queue algorithm for implementing Autocompletor
 * 
 * @author Austin Lu
 * @author Jeff Forbes
 */
public class TrieAutocomplete implements Autocompletor {

	/**
	 * Root of entire trie
	 */
	protected Node myRoot;

	/**
	 * Constructor method for TrieAutocomplete. Should initialize the trie
	 * rooted at myRoot, as well as add all nodes necessary to represent the
	 * words in terms.
	 * 
	 * @param terms
	 *            - The words we will autocomplete from
	 * @param weights
	 *            - Their weights, such that terms[i] has weight weights[i].
	 * @throws NullPointerException
	 *             if either argument is null
	 * @throws IllegalArgumentException
	 *             if terms and weights are different weight
	 */
	public TrieAutocomplete(String[] terms, double[] weights) {
		if (terms == null || weights == null)
			throw new NullPointerException("One or more arguments null");
		if(terms.length != weights.length){
			throw new IllegalArgumentException("terms and weights are not the same lengths");
		}
		// Represent the root as a dummy/placeholder node
		myRoot = new Node('-', null, 0);
		HashSet<String> words = new HashSet<String>();
		
		for (int i = 0; i < terms.length; i++) {
			if(words.contains(terms[i])){
				throw new IllegalArgumentException("Duplicate term " + terms[i]);
			}
			words.add(terms[i]);
			add(terms[i], weights[i]);
		}
	}

	/**
	 * Add the word with given weight to the trie. If word already exists in the
	 * trie, no new nodes should be created, but the weight of word should be
	 * updated.
	 * 
	 * In adding a word, this method should do the following: Create any
	 * necessary intermediate nodes if they do not exist. Update the
	 * subtreeMaxWeight of all nodes in the path from root to the node
	 * representing word. Set the value of myWord, myWeight, isWord, and
	 * mySubtreeMaxWeight of the node corresponding to the added word to the
	 * correct values
	 * 
	 * @throws a
	 *             NullPointerException if word is null
	 * @throws an
	 *             IllegalArgumentException if weight is negative.
	 */
	private void add(String word, double weight) {
		// TODO: Implement add
		if(word == null){
			throw new NullPointerException("word is null!");
		}
		if(weight < 0){
			throw new IllegalArgumentException("weight is negative!");
		}
		
		Node curr = myRoot;
		for (char ch: word.toCharArray()) {
		    if (!curr.children.containsKey(ch)) {
		        curr.children.put(ch, new Node(ch,curr,weight));
		    }
		    if(curr.mySubtreeMaxWeight < weight){
		    	curr.mySubtreeMaxWeight = weight;
		    }
		    curr = curr.children.get(ch);
		}
		/*corner case: reassigning weight to word
		if(curr.isWord && curr.mySubtreeMaxWeight != weight){
	    	//TODO: implement way to reupdate trie
			Node lastChild = curr;
	    	while(curr.parent != null){
	    		curr = curr.parent;
	    		if(curr.mySubtreeMaxWeight < weight){
	    			curr.mySubtreeMaxWeight = weight;
	    		}else if(curr.mySubtreeMaxWeight > weight){
	    			double maxChildWeight = weight;
	    			for(Character ch:curr.children.keySet()){
	    				if(curr.children.get(ch) == lastChild){
	    					continue;
	    				}
	    				if(curr.children.get(ch).mySubtreeMaxWeight > maxChildWeight){
	    					maxChildWeight = curr.children.get(ch).mySubtreeMaxWeight;
	    				}
	    			}
	    			curr.mySubtreeMaxWeight = maxChildWeight;
	    		}
	    	}
	    }
	    */

		curr.setWord(word);
		curr.setWeight(weight);
		curr.isWord = true;
		if(curr.mySubtreeMaxWeight < weight){
			curr.mySubtreeMaxWeight = weight; // <--Somehow taking this out makes benchmark work?
		}
		
		
	}

	/**
	 * Required by the Autocompletor interface. Returns an array containing the
	 * k words in the trie with the largest weight which match the given prefix,
	 * in descending weight order. If less than k words exist matching the given
	 * prefix (including if no words exist), then the array instead contains all
	 * those words. e.g. If terms is {air:3, bat:2, bell:4, boy:1}, then
	 * topKMatches("b", 2) should return {"bell", "bat"}, but topKMatches("a",
	 * 2) should return {"air"}
	 * 
	 * @param prefix
	 *            - A prefix which all returned words must start with
	 * @param k
	 *            - The (maximum) number of words to be returned
	 * @return An Iterable of the k words with the largest weights among all
	 *         words starting with prefix, in descending weight order. If less
	 *         than k such words exist, return all those words. If no such words
	 *         exist, return an empty Iterable
	 * @throws a
	 *             NullPointerException if prefix is null
	 */
	public Iterable<String> topMatches(String prefix, int k) {
		// TODO: Implement topKMatches
		if(prefix == null){
			throw new NullPointerException("prefix is null!");
		}
		PriorityQueue<Node> queue = new PriorityQueue<>(new Node.ReverseSubtreeMaxWeightComparator());
		ArrayList<String> words = new ArrayList<String>();
		
		//navigate to end of prefix
		Node curr = myRoot;
		for(int i = 0; i < prefix.length(); i++){
			if(curr.children.containsKey(prefix.charAt(i))){
				curr = curr.children.get(prefix.charAt(i));
			}else{
				return new ArrayList<String>();
			}
		}

		//pop nodes of queue and add to words ArrayList
		queue.add(curr);
		while(k>0){
			curr = queue.remove();
			for(Node n : curr.children.values()){
				queue.add(n);
			}
			if(curr.isWord){
				words.add(curr.myWord);
				k--;
			}
			if(queue.isEmpty()) break;
		}
		return words;
	}

	/**
	 * Given a prefix, returns the largest-weight word in the trie starting with
	 * that prefix.
	 * 
	 * @param prefix
	 *            - the prefix the returned word should start with
	 * @return The word from with the largest weight starting with prefix, or an
	 *         empty string if none exists
	 * @throws a
	 *             NullPointerException if the prefix is null
	 */
	public String topMatch(String prefix) {
		// TODO: Implement topMatch
		if(prefix == null){
			throw new NullPointerException("prefix is null!");
		}
		//MY CODE
		Node curr = myRoot;
		for(int i = 0; i < prefix.length(); i++){
			if(curr.children.containsKey(prefix.charAt(i))){
				curr = curr.children.get(prefix.charAt(i));
			}else{
				return "";
			}
		}

		double maxWeight = curr.mySubtreeMaxWeight;
		while(curr.myWeight != maxWeight){
			for(Character ch:curr.children.keySet()){
				if(curr.children.get(ch).mySubtreeMaxWeight == maxWeight){
					curr = curr.children.get(ch);
					break;
				}
			}
		}
		/*Their code
		Node current = myRoot;
		while(current.mySubtreeMaxWeight != current.myWeight){
			for(Character ch:current.children.keySet()){
				if(current.children.get(ch).mySubtreeMaxWeight == current.mySubtreeMaxWeight){
					current = current.children.get(ch);
					break;
				}
			}
		}*/
		return curr.myWord;
	}

	/**
	 * Return the weight of a given term. If term is not in the dictionary,
	 * return 0.0
	 */
	public double weightOf(String term) {
		// TODO complete weightOf
		Node curr = myRoot;
		for(int i = 0; i < term.length(); i++){
			if(curr.children.containsKey(term.charAt(i))){
				curr = curr.children.get(term.charAt(i));
			}else{
				return 0.0;
			}
		}
		return curr.myWeight;
	}

	/**
	 * Optional: Returns the highest weighted matches within k edit distance of
	 * the word. If the word is in the dictionary, then return an empty list.
	 * 
	 * @param word
	 *            The word to spell-check
	 * @param dist
	 *            Maximum edit distance to search
	 * @param k
	 *            Number of results to return
	 * @return Iterable in descending weight order of the matches
	 */
	public static Iterable<String> spellCheck(String word, int dist, int k) {
		if(dist<=0 || k <0){
			throw new IllegalArgumentException("Bad argument!");
		}
		Scanner in = null;
		try {
			in = new Scanner(new File("/Users/macbookair/git/autocomplete-start/data/wiktionary.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		in.next();
		HashMap<String, Double> matches = new HashMap<String, Double>();
		while(in.hasNextLine()){
			double weight = in.nextDouble();
			String match = in.next();
			int diff = distance(word, match);
			if(diff <= dist){
				matches.put(match, weight);
			}
		}
		in.close();
		
		List<Entry<String,Double>> sortedEntries = new ArrayList<Entry<String,Double>>(matches.entrySet());
	    Collections.sort(sortedEntries, 
	            new Comparator<Entry<String,Double>>() {
	                @Override
	                public int compare(Entry<String,Double> e1, Entry<String,Double> e2) {
	                    if(e2.getValue() > e1.getValue()){
	                    	return 1;
	                    }
	                    else if(e2.getValue() < e1.getValue()){
	                    	return -1;
	                    }
	                    else{
	                    	return 0;
	                    }
	                }
	            }
	    );
	    ArrayList<String> goodDistWords = new ArrayList<String>();
	    for(int i = 0; i < sortedEntries.size(); i++){
	    	if(i == k){
	    		break;
	    	}
	    	goodDistWords.add(sortedEntries.get(i).getKey());
	    }
		return goodDistWords;
	}
	private static int distance(String a, String b){
		a = a.toLowerCase();
        b = b.toLowerCase();
        // i == 0
        int [] costs = new int [b.length() + 1];
        for (int j = 0; j < costs.length; j++)
            costs[j] = j;
        for (int i = 1; i <= a.length(); i++) {
            // j == 0; nw = lev(i - 1, j)
            costs[0] = i;
            int nw = i - 1;
            for (int j = 1; j <= b.length(); j++) {
                int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]), a.charAt(i - 1) == b.charAt(j - 1) ? nw : nw + 1);
                nw = costs[j];
                costs[j] = cj;
            }
        }
        return costs[b.length()];
	}
	public static void main(String[] args){
		ArrayList<String> arr = (ArrayList<String>)spellCheck("gurl", 1, 5);
		for(String s: arr){
			System.out.println(s);
		}
	}
}
