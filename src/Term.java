
/*************************************************************************
 * @author Kevin Wayne
 *
 * Description: A term and its weight.
 * 
 *************************************************************************/

import java.util.Comparator;

public class Term implements Comparable<Term> {

	private final String myWord;
	private final double myWeight;

	/**
	 * The constructor for the Term class. Should set the values of word and
	 * weight to the inputs, and throw the exceptions listed below
	 * 
	 * @param word
	 *            The word this term consists of
	 * @param weight
	 *            The weight of this word in the Autocomplete algorithm
	 * @throws NullPointerException
	 *             if word is null
	 * @throws IllegalArgumentException
	 *             if weight is negative
	 */
	public Term(String word, double weight) {
		// TODO: Complete Term constructor
		if(word == null){
			throw new NullPointerException("Error: word is null");
		}
		if(weight < 0){
			throw new IllegalArgumentException("Error: negative weight");
		}
		myWord = word;
		myWeight = weight;
	}

	/**
	 * A Comparator for comparing Terms using a set number of the letters they
	 * start with. This Comparator may be useful in writing your implementations
	 * of Autocompletors.
	 *
	 */
	public static class PrefixOrder implements Comparator<Term> {
		private final int r;

		public PrefixOrder(int r) {
			this.r = r;
		}

		/**
		 * Compares v and w lexicographically using only their first r letters.
		 * If the first r letters are the same, then v and w should be
		 * considered equal. This method should take O(r) to run, and be
		 * independent of the length of v and w's length. You can access the
		 * Strings to compare using v.word and w.word.
		 * 
		 * @param v/w
		 *            - Two Terms whose words are being compared
		 */
		public int compare(Term v, Term w) {
			// TODO: Implement compare
			
			String v1;
			String w1;
			if(r > v.myWord.length() && r <= w.myWord.length()){
				v1 = v.myWord;
				w1 = w.myWord.substring(0,r);
			}else if(r > w.myWord.length() && r <= v.myWord.length()){
				v1 = v.myWord.substring(0,r);
				w1 = w.myWord;
			}else if(r > w.myWord.length() && r > v.myWord.length()){
				v1 = v.myWord;
				w1 = w.myWord;
			}else{
				v1 = v.myWord.substring(0,r);
				w1 = w.myWord.substring(0,r);
			}
			if(v1.compareTo(w1)>0){
				return 1;
			}
			if(v1.compareTo(w1)<0){
				return -1;
			}
			/*
			for(int i = 0; i < r; i++){
				if(i == v.myWord.length() || i == w.myWord.length()){
					break;
				}
				if(v.myWord.substring(i,i+1).compareTo(w.myWord.substring(i, i+1)) > 0){
					return 1;
				}
				if(v.myWord.substring(i,i+1).compareTo(w.myWord.substring(i, i+1)) < 0){
					return -1;
				}
			}*/
			
			return 0;
		}
	}

	/**
	 * A Comparator for comparing Terms using only their weights, in descending
	 * order. This Comparator may be useful in writing your implementations of
	 * Autocompletor
	 *
	 */
	public static class ReverseWeightOrder implements Comparator<Term> {
		public int compare(Term v, Term w) {
			// TODO: Implement ReverseWeightOrder.compare
			if(v.myWeight > w.myWeight){
				return -1;
			}
			if(v.myWeight < w.myWeight){
				return 1;
			}
			return 0;
		}
	}

	/**
	 * A Comparator for comparing Terms using only their weights, in ascending
	 * order. This Comparator may be useful in writing your implementations of
	 * Autocompletor
	 *
	 */
	public static class WeightOrder implements Comparator<Term> {
		public int compare(Term v, Term w) {
			// TODO: Implement WeightOrder.compare
			if(v.myWeight > w.myWeight){
				return 1;
			}
			if(v.myWeight < w.myWeight){
				return -1;
			}
			return 0;
		}
	}

	/**
	 * The default sorting of Terms is lexicographical ordering.
	 */
	public int compareTo(Term that) {
		return myWord.compareTo(that.myWord);
	}

	/**
	 * Getter methods, use these in other classes which use Term
	 */
	public String getWord() {
		return myWord;
	}

	public double getWeight() {
		return myWeight;
	}

	public String toString() {
		return String.format("%14.1f\t%s", myWeight, myWord);
	}
}
