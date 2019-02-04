import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toMap;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Fields;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.PostingsEnum;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

public class Starter {

	static int TaatOrComp = 0;
	static int TaatAndComp = 0;
	static int DaatOrComp = 0;
	static int DaatAndComp = 0;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String LuceneIndex = args[0];
		String outputFile = args[1];
		String inputFile = args[2];

		Map<String, LinkedList<Integer>> postingMap = null;

		try {
			IndexReader r = getIndex(LuceneIndex);
			postingMap = getPostingList(r);

			ArrayList<Integer> termsRank = getRanking(postingMap, inputFile, outputFile);

		} catch (IOException e) {
			//System.out.println(e);
		}

	}

	private static ArrayList<Integer> getRanking(Map<String, LinkedList<Integer>> postingMap, String inputFile,
			String outputFile) throws IOException {
		// TODO Auto-generated method stub
		BufferedReader b = new BufferedReader(new FileReader(inputFile));
		BufferedWriter w = new BufferedWriter(new FileWriter(outputFile, true));
		String line;
		while ((line = b.readLine()) != null) {
			String[] inputTerms = line.split(" ");
			HashMap<String, LinkedList<Integer>> desiredPostings = writePosting(inputTerms, postingMap, w);
			w.write("TaatAnd");
			w.newLine();
			printOutput(inputTerms, methodTAAT_AND(inputTerms, desiredPostings), w, TaatAndComp);
			w.write("TaatOr");
			w.newLine();
			printOutput(inputTerms, methodTAAT_OR(inputTerms, desiredPostings), w, TaatOrComp);
			w.write("DaatAnd");
			w.newLine();
			printOutput(inputTerms, methodDAAT_AND(inputTerms, desiredPostings), w, DaatAndComp);
			w.write("DaatOr");
			w.newLine();
			printOutput(inputTerms, methodDAAT_OR(inputTerms, desiredPostings), w, DaatOrComp);
		}
		w.close();
		return null;
	}

	private static LinkedList<Integer> methodDAAT_AND(String[] inputTerms,
			HashMap<String, LinkedList<Integer>> desiredPostings) {
		// TODO Auto-generated method stub
		// HashMap<String, LinkedList<Integer>> desiredPosting =
		// sortHashMap(desiredPostings);
		// implemnt TAAT logic here
		Map<String, LinkedList<Integer>> copyPostings = desiredPostings.entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), e -> new LinkedList<Integer>(e.getValue())));
		Iterator<LinkedList<Integer>> it = copyPostings.values().iterator();
		LinkedList<LinkedList<Integer>> lists = new LinkedList<>();

		TreeSet<Integer> D = new TreeSet<>();
		//LinkedList<LinkedList<Integer>> skipLists = new LinkedList<>();

		while (it.hasNext())
			lists.add(it.next());

		for (LinkedList<Integer> i : lists)
			for (Integer x : i)
				D.add(x);

		// initialize skip list
		LinkedList<Integer> result = new LinkedList<>();

		Map<Integer, Integer> ranking = new HashMap<Integer, Integer>();
		for (Integer d : D) {
			int i = 0;
			for (LinkedList<Integer> p : lists) {
				if (p.contains(d)) {
					++DaatAndComp;
					for (int in = 0; in < p.indexOf(d); in++)
						p.remove(in);
					ranking.put(d, ++i);
				}
			}

		}
		for (Map.Entry<Integer, Integer> entry : ranking.entrySet()) {
			if (entry.getValue() == inputTerms.length)
				result.add(entry.getKey());
		}
		return result;
		
	}

	private static LinkedList<Integer> methodDAAT_OR(String[] inputTerms,
			HashMap<String, LinkedList<Integer>> desiredPostings) {
		// TODO Auto-generated method stub
		Iterator<LinkedList<Integer>> it = desiredPostings.values().iterator();
		LinkedList<LinkedList<Integer>> lists = new LinkedList<>();
		TreeSet<Integer> D = new TreeSet<>();
		//System.out.println(desiredPostings.values());
		while (it.hasNext())
			lists.add(it.next());

		for (LinkedList<Integer> i : lists)
			for (Integer x : i)
				D.add(x);

		// initialize skip list
		LinkedList<Integer> result = new LinkedList<>();
		Map<Integer, Integer> ranking = new HashMap<Integer, Integer>();
		for (Integer d : D) {
			int i = 0;
			for (LinkedList<Integer> p : lists) {
				if (p.contains(d)) {
					++DaatOrComp;
					ranking.put(d, ++i);
				}else {
					++DaatOrComp;
					ranking.put(d, 1);
				}
			}
			++DaatOrComp;
		}
		DaatOrComp *= 7;
		for (Map.Entry<Integer, Integer> entry : ranking.entrySet()) {
			result.add(entry.getKey());
		}
		return result;

	}

	private static LinkedList<Integer> methodTAAT_AND(String[] inputTerms,
			HashMap<String, LinkedList<Integer>> desiredPostings) {
		// TODO Auto-generated method stub
		HashMap<String, LinkedList<Integer>> desiredPosting = sortHashMap(desiredPostings);
		// implemnt TAAT logic here
		Iterator<LinkedList<Integer>> it = desiredPosting.values().iterator();
		LinkedList<Integer> result = it.next();
		while (it.hasNext()) {
			result = intersectTAAT(result, it.next());
		}
		Collections.sort(result);
		return result;
	}

	private static LinkedList<Integer> methodTAAT_OR(String[] inputTerms,
			HashMap<String, LinkedList<Integer>> desiredPostings) throws IOException {
		// TODO Auto-generated method stub
		// get posting list keys in order of increasing values length
		HashMap<String, LinkedList<Integer>> desiredPosting = sortHashMap(desiredPostings);
		// implemnt TAAT logic here
		Iterator<LinkedList<Integer>> it = desiredPosting.values().iterator();
		LinkedList<Integer> result = it.next();
		while (it.hasNext()) {
			result = unionTAAT(result, it.next());
		}
		Collections.sort(result);
		return result;
	}

	private static LinkedHashMap<String, LinkedList<Integer>> sortHashMap(
			HashMap<String, LinkedList<Integer>> desiredPostings) {
		return desiredPostings.entrySet().stream().sorted(comparingInt(e -> e.getValue().size()))
				.collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> {
					throw new AssertionError();
				}, LinkedHashMap::new));
	}

	private static LinkedList<Integer> unionTAAT(LinkedList<Integer> l1, LinkedList<Integer> l2) {
		// TODO Auto-generated method stub
		boolean flag = false;
		int docId;
		Iterator<Integer> resIt = l1.iterator();
		LinkedList<Integer> tempResult = new LinkedList<>();
		while (resIt.hasNext()) {
			docId = resIt.next();

			for (int i = 0; i < l2.size(); i++) {
				TaatOrComp++;
				if (docId == l2.get(i)) {
					flag = true;
					break;
				}
			}
			if (!flag) {
				tempResult.add(docId);
				// System.out.print(" -> " + docId);
			}
		}
		tempResult.addAll(l2);
		return tempResult;
	}

	private static LinkedList<Integer> intersectTAAT(LinkedList<Integer> l1, LinkedList<Integer> l2) {
		// TODO Auto-generated method stub
		// int docId;
		int skipL1 = (int) Math.floor(Math.sqrt(l1.size()));
		int skipL2 = (int) Math.floor(Math.sqrt(l2.size()));
		// Iterator<Integer> resIt = l1.iterator();
		int pt1 = 0;
		int pt2 = 0;
		LinkedList<Integer> tempResult = new LinkedList<>();
		while (pt1 < l1.size() && pt2 < l2.size()) {
			if (l1.get(pt1).equals(l2.get(pt2))) {
				TaatAndComp++;
				tempResult.add(l1.get(pt1));
				pt1++;
				pt2++;
				// pt2++;
			} else if (l1.get(pt1) < l2.get(pt2)) {
				TaatAndComp++;
				if (hasSkip(pt1, l1) && l1.get(pt1 + skipL1) < l2.get(pt2)) {
					// TaatAndComp++;
					while (hasSkip(pt1, l1) && l1.get(pt1 + skipL1) < l2.get(pt2)) {
						TaatAndComp++;
						pt1 += skipL1;
					}
					// pt1++;
				} else {
					TaatAndComp++;
					pt1++;
				}
			} else if (hasSkip(pt2, l2) && l2.get(pt2 + skipL2) < l1.get(pt1)) {
				// TaatAndComp++;
				while (hasSkip(pt2, l2) && l2.get(pt2 + skipL2) < l1.get(pt1)) {
					TaatAndComp++;
					pt2 += skipL2;
				}
				// pt2++;
			} else {
				TaatAndComp++;
				pt2++;
			}
		}
		//System.out.println(pt1);
		//System.out.println(pt2);
		return tempResult;
	}

	private static boolean hasSkip(int pt, LinkedList<Integer> l) {
		// TODO Auto-generated method stub
		if (pt + (int) Math.sqrt(l.size()) < l.size())
			return true;
		return false;
	}

	private static HashMap<String, LinkedList<Integer>> writePosting(String[] inputTerms,
			Map<String, LinkedList<Integer>> postingMap, BufferedWriter w) throws IOException {
		// TODO Auto-generated method stub
		HashMap<String, LinkedList<Integer>> desiredPostings = new HashMap<String, LinkedList<Integer>>();
		// BufferedWriter w1 = new BufferedWriter(new FileWriter(outputFile));
		LinkedList<Integer> list;
		for (String term : inputTerms) {
			w.write("GetPostings");
			w.newLine();
			if (postingMap.containsKey(term)) {
				w.write(term);
				w.newLine();
				w.write("Postings list: ");
				list = postingMap.get(term);
				for (int docId : list)
					w.write(docId + " ");
				w.newLine();
				desiredPostings.put(term, postingMap.get(term));
				// //System.out.println("Hopefully something is written");
			}
		}
		return desiredPostings;
	}

	public static IndexReader getIndex(String indexPath) throws IOException {

		Directory index = null;
		IndexReader reader = null;

		File f = new File(indexPath);
		Path p = f.toPath();

		index = FSDirectory.open(p);
		reader = DirectoryReader.open(index);

		return reader;
	}

	private static Map<String, LinkedList<Integer>> getPostingList(IndexReader reader) throws IOException {
		Fields fields = MultiFields.getFields(reader);
		Iterator<String> iter = fields.iterator();
		Map<String, TreeSet<Integer>> pList = new HashMap<String, TreeSet<Integer>>();
		while (iter.hasNext()) {
			String field = iter.next();
			Terms terms = MultiFields.getTerms(reader, "text_fr");
			TermsEnum it = terms.iterator();
			BytesRef term = it.next();
			PostingsEnum docsEnum = null;
			addTerms_Docs(pList, it, term, docsEnum);

			terms = MultiFields.getTerms(reader, "text_en");
			it = terms.iterator();
			term = it.next();
			docsEnum = null;
			addTerms_Docs(pList, it, term, docsEnum);

			terms = MultiFields.getTerms(reader, "text_es");
			it = terms.iterator();
			term = it.next();
			docsEnum = null;
			addTerms_Docs(pList, it, term, docsEnum);
		}

		LinkedList<Integer> list;
		Map<String, LinkedList<Integer>> postings = new HashMap<String, LinkedList<Integer>>();
		for (Map.Entry<String, TreeSet<Integer>> entry : pList.entrySet()) {
			list = new LinkedList<Integer>(entry.getValue());
			postings.put(entry.getKey(), list);
			//System.out.println(entry.getKey() + " : " + entry.getValue());
		}
		return postings;
	}

	private static void addTerms_Docs(Map<String, TreeSet<Integer>> postingList, TermsEnum it, BytesRef term,
			PostingsEnum docsEnum) throws IOException {
		TreeSet<Integer> doc_ids;
		while (term != null) {
			docsEnum = it.postings(docsEnum, PostingsEnum.NONE);
			int docId;
			while ((docId = docsEnum.nextDoc()) != docsEnum.NO_MORE_DOCS) {
				String t = term.utf8ToString();
				// //System.out.println(term.utf8ToString() + "\t" + docId);
				if (postingList.containsKey(t)) {
					doc_ids = postingList.get(t);
					doc_ids.add(docId);
				} else {
					doc_ids = new TreeSet<Integer>();
					doc_ids.add(docId);
					postingList.put(t, doc_ids);
				}
			}
			term = it.next();
		}
	}

	private static void printOutput(String[] inputTerms, LinkedList<Integer> result, BufferedWriter w, int comp)
			throws IOException {
		Iterator<Integer> it;
		for (String term : inputTerms) {
			w.write(term + " ");
		}
		w.newLine();
		w.write("Results: ");
		if (result.size() == 0) {
			w.write("empty");
			w.newLine();
		} else {
			for (int docId : result)
				w.write(docId + " ");
			w.newLine();
		}
		w.write("Number of documents in results: " + result.size());
		w.newLine();
		w.write("Number of comparisons: " + comp);
		w.newLine();
	}

}
