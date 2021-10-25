package my.lucene;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
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
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.LMDirichletSimilarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Index {

		public static void Index (String analyser_in, String similarity_in) throws IOException {
			
			String PathToIndex = "src/index";
			
			String PathToCranDocs = "src/cran/cran.all.1400";
			
			String PathToStop = "src/stop.txt";
			
			File directory = new File(PathToIndex);
			
			File Stopdirectory = new File(PathToStop);
			
			Scanner stp = new Scanner(Stopdirectory);
			
			String row = stp.nextLine();
			
			//List<String> stpwrd = new ArrayList<String>();
			
			while(stp.hasNext()){
				
				row = stp.nextLine();
				
			}

			String[] delFiles;    
	           if(directory.isDirectory()){
	        	   delFiles = directory.list();
	               for (int i=0; i<delFiles.length; i++) {
	                   File my = new File(directory, delFiles[i]); 
	                   my.delete();
	               }
	            }
			
	        List<String> stp_wrd_lst = Arrays.asList("a", "about", "above", "after", "again", "against", "all", "am", "an", "and", "any", "are", "as", "at", "be", "because", "been", "before", "being", "below", "between", "both", "but", "by", "could", "did", "do", "does", "doing", "down", "during", "each", "few", "for", "from", "further", "had", "has", "have", "having", "he", "he'd", "he'll", "he's", "her", "here", "here's", "hers", "herself", "him", "himself", "his", "how", "how's", "i", "i'd", "i'll", "i'm", "i've", "if", "in", "into", "is", "it", "it's", "its", "itself", "let's", "me", "more", "most", "my", "myself", "nor", "of", "on", "once", "only", "or", "other", "ought", "our", "ours", "ourselves", "out", "over", "own", "same", "she", "she'd", "she'll", "she's", "should", "so", "some", "such", "than", "that", "that's", "the", "their", "theirs", "them", "themselves", "then", "there", "there's", "these", "they", "they'd", "they'll", "they're", "they've", "this", "those", "through", "to", "too", "under", "until", "up", "very", "was", "we", "we'd", "we'll", "we're", "we've", "were", "what", "what's", "when", "when's", "where", "where's", "which", "while", "who", "who's", "whom", "why", "why's", "with", "would", "you", "you'd", "you'll", "you're", "you've", "your", "yours", "yourself", "yourselves");
	           
//			List<String> stopWordList = Arrays.asList();
			
			CharArraySet stpset = new CharArraySet( stp_wrd_lst, true);
			
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
		
			
			IndexWriterConfig index_writer_config = new IndexWriterConfig(ana);
			
			index_writer_config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
			
			switch (similarity_in){
				case "ClassicSimilarity":
					index_writer_config.setSimilarity(new ClassicSimilarity());
					break;
				case "BM25Similarity":
					index_writer_config.setSimilarity(new BM25Similarity());
					break;
				case "LMDirichletSimilarity":
					index_writer_config.setSimilarity(new LMDirichletSimilarity());
					break;

				default : break;
			}
			
			
			Directory docu_dire = FSDirectory.open(Paths.get(PathToIndex));
			
			IndexWriter iw = new IndexWriter(docu_dire, index_writer_config);
			
			File fobj = new File(PathToCranDocs);
			
			Scanner sred_scan = new Scanner(fobj);
			
			sred_scan.useDelimiter("\n");
			
			//int index_file_count = 0;
			
			String line = sred_scan.nextLine();
			
			Map<Integer, Map<String,String>> docMap=new HashMap<Integer, Map<String,String>>();
			
			//String token;
			
			Map<String,String> hm =  new HashMap< String,String>();
			int count=0;
			while(line != null) {
				//count=0;
				String title ="";
				String author ="";
				String pub = "";
				String words ="";
				//System.out.println(count);
				if(line.charAt(0)=='.' && line.charAt(1)=='I') {
					hm=new HashMap<String,String>();
					//sred_scan.nextLine();
					count++;
					hm.put("index",""+count);
					line=sred_scan.nextLine();
					}
				if(line.charAt(0)=='.' && line.charAt(1)=='T'){
					line=sred_scan.nextLine();
					while(line.charAt(0)!='.' && line.charAt(1)!='A'){
						
						title+=line+" ";
						line=sred_scan.nextLine();
						}
						hm.put("title",title);
					}
				if(line.charAt(0)=='.' && line.charAt(1)=='A'){
					line=sred_scan.nextLine();
					while(line.charAt(0)!='.' && line.charAt(1)!='B'){
						author+=line+" ";
						line=sred_scan.nextLine();
						}
						hm.put("author",author);
				
					}
				if(line.charAt(0)=='.' && line.charAt(1)=='B'){
					line=sred_scan.nextLine();
					while(line.charAt(0)!='.' && line.charAt(1)!='W'){
						pub+=line+" ";
						line=sred_scan.nextLine();
						}
						hm.put("published",pub);
					}
				if (line.charAt(0)=='.' && line.charAt(1)=='W'){
					line=sred_scan.nextLine();
					while(line.charAt(0)!='.' && line.charAt(1)!='I'){
						words+=line+" ";
						if(sred_scan.hasNext())
						line=sred_scan.nextLine();
						else{
							hm.put("content",words);
						docMap.put(count,hm);
						break;
						}
						
						}
						if(!sred_scan.hasNext())
						break;
						hm.put("content",words);
						docMap.put(count,hm);
					}
				}

			for (Map.Entry<Integer, Map<String,String>> entry : docMap.entrySet()) {
				
				//System.out.println(entry.getValue().get("title"));
				Document documents = new Document();
				documents.add(new TextField("index", entry.getKey()+"", Field.Store.YES));
				documents.add(new TextField("title", entry.getValue().get("title"), Field.Store.YES));
				documents.add(new TextField("author", entry.getValue().get("author"), Field.Store.YES));
				documents.add(new TextField("published", entry.getValue().get("published"), Field.Store.YES));
				documents.add(new TextField("content", entry.getValue().get("content"), Field.Store.YES));
				//System.out.println(doc);
				iw.addDocument(documents);
				
			}
			sred_scan.close();
			iw.close();
			docu_dire.close();
		}
}


