package my.lucene;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.lucene.queryparser.classic.ParseException;

public class MainApplication {

	public static void main(String[] args) throws IOException, ParseException {
		ArrayList<String> analyser_list = new ArrayList<>();

        ArrayList<String> similarity_list = new ArrayList<>();

        analyser_list.add("EnglishAnalyzer");
        analyser_list.add("StandardAnalyzer");

        similarity_list.add("ClassicSimilarity");
        similarity_list.add("BM25Similarity");
        similarity_list.add("LMDirichletSimilarity");
    
        
        System.out.println("\n!!**********Searching***************!!");
        
        for (String analyse : analyser_list) {
            
            for (String similar : similarity_list) {
                
                Index.Index(analyse,similar);
                
                String root = Search.Search(analyse, similar);
                
                System.out.println("\nPath for Result while using Analyser is = " + analyse + " while using simalarity named = " + similar + " is = " + root);
                
                System.out.println();
                
            }
            
        }
        
        System.out.println("!!************Done Searching*************!!");
        System.out.println();
        
        System.out.println("We calculate value of Mean Average Precision MAP");
        System.out.println();
        
        System.out.println("WE GET THE BEST RESULT WHEN THERE IS ENGLISH ANALYSER & BM25 SIMILARITY AND IT IS 0.4087");
        System.out.println();
        
    
		
	}
}
