package my.lucene;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.LMDirichletSimilarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Search {

	public static String Search(String analyser_in, String similarity_in) throws IOException, ParseException {
		
		String PathToIndex = "src/index";
		
		String PathToQry = "src/cran/cran.qry";
		
		String PathToStop = "src/stop.txt";
		
		File Stopdirectory = new File(PathToStop);
		
		Scanner stp = new Scanner(Stopdirectory);
		
		String row = stp.nextLine();
		
		List<String> stpwrd = new ArrayList<String>();
		
		while(stp.hasNext()){
			
			row = stp.nextLine();
			
		}
		
		List<String> words_stop = Arrays.asList("a", "about", "above", "after", "again", "against", "all", "am", "an", "and", "any", "are", "as", "at", "be", "because", "been", "before", "being", "below", "between", "both", "but", "by", "could", "did", "do", "does", "doing", "down", "during", "each", "few", "for", "from", "further", "had", "has", "have", "having", "he", "he'd", "he'll", "he's", "her", "here", "here's", "hers", "herself", "him", "himself", "his", "how", "how's", "i", "i'd", "i'll", "i'm", "i've", "if", "in", "into", "is", "it", "it's", "its", "itself", "let's", "me", "more", "most", "my", "myself", "nor", "of", "on", "once", "only", "or", "other", "ought", "our", "ours", "ourselves", "out", "over", "own", "same", "she", "she'd", "she'll", "she's", "should", "so", "some", "such", "than", "that", "that's", "the", "their", "theirs", "them", "themselves", "then", "there", "there's", "these", "they", "they'd", "they'll", "they're", "they've", "this", "those", "through", "to", "too", "under", "until", "up", "very", "was", "we", "we'd", "we'll", "we're", "we've", "were", "what", "what's", "when", "when's", "where", "where's", "which", "while", "who", "who's", "whom", "why", "why's", "with", "would", "you", "you'd", "you'll", "you're", "you've", "your", "yours", "yourself", "yourselves");
		
		CharArraySet stpset = new CharArraySet(words_stop, true);
		
		Analyzer ana = null;
		
		switch (analyser_in){
			case "EnglishAnalyzer":
				ana = new EnglishAnalyzer(stpset);
				break;
			case "StandardAnalyzer":
				ana = new StandardAnalyzer(stpset);
				break;
			default : break;
		}

		Directory docu_dire = FSDirectory.open(Paths.get(PathToIndex));
		
		IndexReader ir = DirectoryReader.open(docu_dire);
		
		IndexSearcher is = new IndexSearcher(ir);
		switch (similarity_in){
			case "ClassicSimilarity":
				is.setSimilarity(new ClassicSimilarity());
				break;
			case "BM25Similarity":
				is.setSimilarity(new BM25Similarity());
				break;
			case "LMDirichletSimilarity":
				is.setSimilarity(new LMDirichletSimilarity());
				break;

			default : break;
		}

		File fobj = new File(PathToQry);
		
		Scanner sred_scan = new Scanner(fobj);
		
		Map<Integer, Map<String,String>> docMap=new HashMap<Integer, Map<String,String>>();
		
		Map<String,String> hm =  new HashMap< String,String>();
		
		String line = sred_scan.nextLine();
		int count=0;
		while(line != null) {
			String query="";

			if(line.charAt(0)=='.' && line.charAt(1)=='I') {
				hm=new HashMap<String,String>();
				//sred_scan.nextLine();
				count++;
				hm.put("index",""+count);
				line=sred_scan.nextLine();
				}

				if (line.charAt(0)=='.' && line.charAt(1)=='W'){
					line=sred_scan.nextLine();
					while(line.charAt(0)!='.' && line.charAt(1)!='I'){
						query+=line+" ";
						if(sred_scan.hasNext())
						line=sred_scan.nextLine();
						else{
							hm.put("Query",query);
						docMap.put(count,hm);
						break;
						}
						
						}
						if(!sred_scan.hasNext())
						break;
						hm.put("Query",query);
						docMap.put(count,hm);
					}
		
		
		}
		
		MultiFieldQueryParser qp = new MultiFieldQueryParser(new String[] {"title", "published", "author", "content"}, ana);
		
		ArrayList<String> vars = new ArrayList<String>();
		
		for (Map.Entry<Integer, Map<String,String>> entry : docMap.entrySet()) {
			int ind = entry.getKey();
			String qry_val = entry.getValue().get("Query");
			qry_val = qry_val.replaceAll("[^a-zA-Z0-9\\s+]", "");
			org.apache.lucene.search.Query qry_val_parse = qp.parse(qry_val);
			ScoreDoc[] hits = is.search(qry_val_parse, 1000).scoreDocs;
            		int i = 0;
		 	while(i < hits.length) {
                		Document hitDoc = is.doc(hits[i].doc);
                		int rank = i+1;
                		double sc = hits[i].score;
                		if (sc >0){
                  			vars.add(ind + " Q0 " + hitDoc.get("index") + " "+ rank + " "+ sc  +" uncosidered \n");
                		}
				i++;
             	 	}
		}
		
		String path ="src/" +analyser_in+"_"+similarity_in+".out" ;
		
		FileWriter myWriter = new FileWriter(path);
		
		for (String vr : vars) {
			
			myWriter.write(vr);
			
		}
		sred_scan.close();
		myWriter.close();
		ir.close();
		docu_dire.close();
		
		return path;
		
	}
	
}
