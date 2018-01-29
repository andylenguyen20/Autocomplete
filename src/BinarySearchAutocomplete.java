import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
 * 
 * Using a sorted array of Term objects, this implementation uses binary search
 * to find the top term(s).
 * 
 * @author Austin Lu, adapted from Kevin Wayne
 * @author Jeff Forbes
 */
public class BinarySearchAutocomplete implements Autocompletor {

	Term[] myTerms;

	/**
	 * Given arrays of words and weights, initialize myTerms to a corresponding
	 * array of Terms sorted lexicographically.
	 * 
	 * This constructor is written for you, but you may make modifications to
	 * it.
	 * 
	 * @param terms
	 *            - A list of words to form terms from
	 * @param weights
	 *            - A corresponding list of weights, such that terms[i] has
	 *            weight[i].
	 * @return a BinarySearchAutocomplete whose myTerms object has myTerms[i] =
	 *         a Term with word terms[i] and weight weights[i].
	 * @throws a
	 *             NullPointerException if either argument passed in is null
	 */
	public BinarySearchAutocomplete(String[] terms, double[] weights) {
		if (terms == null || weights == null)
			throw new NullPointerException("One or more arguments null");
		myTerms = new Term[terms.length];
		for (int i = 0; i < terms.length; i++) {
			myTerms[i] = new Term(terms[i], weights[i]);
		}
		Arrays.sort(myTerms);
	}

	/**
	 * Uses binary search to find the index of the first Term in the passed in
	 * array which is considered equivalent by a comparator to the given key.
	 * This method should not call comparator.compare() more than 1+log n times,
	 * where n is the size of a.
	 * 
	 * @param a
	 *            - The array of Terms being searched
	 * @param key
	 *            - The key being searched for.
	 * @param comparator
	 *            - A comparator, used to determine equivalency between the
	 *            values in a and the key.
	 * @return The first index i for which comparator considers a[i] and key as
	 *         being equal. If no such index exists, return -1 instead.
	 */
	public static int firstIndexOf(Term[] a, Term key, Comparator<Term> comparator) {
		// TODO: Implement firstIndexOf
		int start = 0;
		int end = a.length - 1;
		int mid = (start + end) / 2;
		while (start <= end) {
			if (comparator.compare(a[mid], key) == 0) {
				if (mid == 0 || comparator.compare(a[mid - 1], key) != 0) {
					return mid;
				} else {
					end = mid - 1;
				}
			} else if (comparator.compare(key, a[mid]) < 0) {
				end = mid - 1;
			} else if (comparator.compare(key, a[mid]) > 0) {
				start = mid + 1;
			}
			mid = (start + end) / 2;
		}
		return -1;
	}

	/**
	 * The same as firstIndexOf, but instead finding the index of the last Term.
	 * 
	 * @param a
	 *            - The array of Terms being searched
	 * @param key
	 *            - The key being searched for.
	 * @param comparator
	 *            - A comparator, used to determine equivalency between the
	 *            values in a and the key.
	 * @return The last index i for which comparator considers a[i] and key as
	 *         being equal. If no such index exists, return -1 instead.
	 */
	public static int lastIndexOf(Term[] a, Term key, Comparator<Term> comparator) {
		// TODO: Implement lastIndexOf
		int start = 0;
		int end = a.length - 1;
		int mid = (start + end) / 2;
		while (start <= end) {
			if (comparator.compare(a[mid], key) == 0) {
				if (mid == a.length - 1 || comparator.compare(a[mid + 1], key) != 0) {
					return mid;
				} else {
					start = mid + 1;
				}
			} else if (comparator.compare(key, a[mid]) < 0) {
				end = mid - 1;
			} else if (comparator.compare(key, a[mid]) > 0) {
				start = mid + 1;
			}
			mid = (start + end) / 2;
		}
		return -1;
	}

	/**
	 * Required by the Autocompletor interface. Returns an array containing the
	 * k words in myTerms with the largest weight which match the given prefix,
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
	 * @return An array of the k words with the largest weights among all words
	 *         starting with prefix, in descending weight order. If less than k
	 *         such words exist, return an array containing all those words If
	 *         no such words exist, return an empty array
	 * @throws a
	 *             NullPointerException if prefix is null
	 */
	public Iterable<String> topMatches(String prefix, int k) {
		if (prefix == null) {
			throw new NullPointerException("prefix is null!");
		}
		ArrayList<Term> matchesList = new ArrayList<>();
		int start = firstIndexOf(myTerms, new Term(prefix, 0), new Term.PrefixOrder(prefix.length()));
		int end = lastIndexOf(myTerms, new Term(prefix, 0), new Term.PrefixOrder(prefix.length()));
		if(start == -1 || end == -1){
			return new ArrayList<String>();
		}
		for (; start <= end; start++) {
			matchesList.add(myTerms[start]);
		}
		Term[] matches = matchesList.toArray(new Term[matchesList.size()]);
		Arrays.sort(matches, new Term.ReverseWeightOrder());
		ArrayList<String> tMatches = new ArrayList<String>();
		if (k > matches.length) {
			for (int i = 0; i < matches.length; i++) {
				tMatches.add(matches[i].getWord());
			}
		} else {
			for (int i = 0; i < k; i++) {
				tMatches.add(matches[i].getWord());
			}
		}
		return tMatches;
	}

	/**
	 * Given a prefix, returns the largest-weight word in myTerms starting with
	 * that prefix. e.g. for {air:3, bat:2, bell:4, boy:1}, topMatch("b") would
	 * return "bell". If no such word exists, return an empty String.
	 * 
	 * @param prefix
	 *            - the prefix the returned word should start with
	 * @return The word from myTerms with the largest weight starting with
	 *         prefix, or an empty string if none exists
	 * @throws a
	 *             NullPointerException if the prefix is null
	 * 
	 */
	public String topMatch(String prefix) {
		// TODO: Implement topMatch
		if (prefix == null) {
			throw new NullPointerException("prefix is null!");
		}
		int start = firstIndexOf(myTerms, new Term(prefix, 0), new Term.PrefixOrder(prefix.length()));
		int end = lastIndexOf(myTerms, new Term(prefix, 0), new Term.PrefixOrder(prefix.length()));
		if(start == -1 || end == -1){
			return "";
		}
		double max = 0;
		int index = 0;
		for(int i = start; i <= end; i++){
			if(myTerms[i].getWeight() > max){
				index = i;
				max = myTerms[i].getWeight();
			}
		}
		return myTerms[index].getWord();
	}

	/**
	 * Return the weight of a given term. If term is not in the dictionary,
	 * return 0.0
	 */
	public double weightOf(String term) {
		// TODO complete weightOf
		int index = firstIndexOf(myTerms, new Term(term, 0), new Term.PrefixOrder(term.length()));
		if (index == -1) {
			return 0.0;
		} else {
			return myTerms[index].getWeight();
		}
	}
}
