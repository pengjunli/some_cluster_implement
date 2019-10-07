package Helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Iterator;

import Action.wordcut;
import kmeansCluster.Word2vecRemove;
/**
 * 输入一个放有多个文件的文件夹，或是包含多个文件夹（每个文件夹中都有文档）
 * 的文件夹，可以得到对应的归一化的TfIdf权重矩阵，和矩阵每行对应的文件列表
 * @author pjl
 *
 */
public class getTfIdfMatrixAndDir {
	HashMap<String,Double> DF = new HashMap<String,Double>();
	HashMap<File,HashMap<String,Double>> tfIdfMap = new HashMap<File,HashMap<String,Double>>();
	HashMap<File,HashMap<String,Integer>> articleWordsMap = new HashMap<File,HashMap<String,Integer>>();
	File clusterfile = null;
	public File[] filesd = null;
	
	public getTfIdfMatrixAndDir(File clusterfile) {
		this.clusterfile = clusterfile;
	}
	
	public getTfIdfMatrixAndDir(HashMap<File,HashMap<String,Double>> tfIdfMap,HashMap<String,Double> DF) {
		this.tfIdfMap = tfIdfMap;
		this.DF = DF;
	}
	
	public double[][] getTfIdfMatrix() throws FileNotFoundException{
		HashMap<String,Double> wordsDict = DF;
		File file1 = new File("wordDict/Dictionary.txt");
	    //将特征词写入Dictionary.txt
	    FileOutputStream out = new FileOutputStream(file1,false);
	    PrintStream writer = new PrintStream(out);
	    String wordlist[] = new String[wordsDict.size()];
	    int index = 1;
	    Iterator<String> DictIterator = wordsDict.keySet().iterator();
	    while (DictIterator.hasNext()) {
		    String str = DictIterator.next();
		    writer.println(index+" "+str);
		    wordlist[index-1] = str;
		    index++;
	    }
	    writer.close();
	    int file_size=0;
	    double tfidflist[][] = new double[tfIdfMap.size()][wordlist.length];
	    Iterator<File> tfidfiterator = tfIdfMap.keySet().iterator();
	    filesd = new File[tfIdfMap.size()];

	    while(tfidfiterator.hasNext()) {
		    File file2 = tfidfiterator.next();
		    HashMap<String,Double> itemMap = tfIdfMap.get(file2);
		    filesd[file_size] = file2;
		    for(int i=0;i<wordlist.length;i++) {
			    if(itemMap.containsKey(wordlist[i])) {
				    tfidflist[file_size][i] = itemMap.get(wordlist[i]);    //tfidflist[file_size][i] = itemMap.get(wordlist[i]);
			    }else {
			    	tfidflist[file_size][i] = 0.0;
		    	}
		    }
		    file_size++;
	    }
	    double[][] tfidflist1 = uniformizationTfIdf(tfidflist);
	    return tfidflist1;
	}
	/**
	 * 定义好文档路径，可直接得到权重矩阵，包括分词，去停用词，df、tc等
	 * @return
	 * @throws Exception
	 */
	public double[][] getTfIdfMatrix_Cluster() throws Exception{
		wordcut model = new wordcut();
		model.cutWord(clusterfile);
		articleWordsMap = model.articleWordsMap;
		Word2vecRemove remove = new Word2vecRemove(articleWordsMap);
		articleWordsMap = remove.SameMerge();
		TfIdfHelper modeltfidf = new TfIdfHelper(articleWordsMap);
		tfIdfMap = modeltfidf.calculate();
//		HashMap<String,Double> TC = modeltfidf.getTC();
//	    HashMap<String,Double> 	wordsDict = TC;
	    HashMap<String,Double> TC = modeltfidf.DF;
	    HashMap<String,Double> 	wordsDict = TC;
	    File file1 = new File("wordDict/Dictionary.txt");
	    //将特征词写入Dictionary.txt
	    FileOutputStream out = new FileOutputStream(file1,false);
	    PrintStream writer = new PrintStream(out);
	    String wordlist[] = new String[wordsDict.size()];
	    int index = 1;
	    Iterator<String> DictIterator = wordsDict.keySet().iterator();
	    while (DictIterator.hasNext()) {
		    String str = DictIterator.next();
		    writer.println(index+" "+str);
		    wordlist[index-1] = str;
		    index++;
	    }
	    writer.close();
	    int file_size=0;
	    double tfidflist[][] = new double[tfIdfMap.size()][wordlist.length];
	    Iterator<File> tfidfiterator = tfIdfMap.keySet().iterator();
	    filesd = new File[tfIdfMap.size()];

	    while(tfidfiterator.hasNext()) {
		    File file2 = tfidfiterator.next();
		    HashMap<String,Double> itemMap = tfIdfMap.get(file2);
		    filesd[file_size] = file2;
		    for(int i=0;i<wordlist.length;i++) {
			    if(itemMap.containsKey(wordlist[i])) {
				    tfidflist[file_size][i] = itemMap.get(wordlist[i]);    //tfidflist[file_size][i] = itemMap.get(wordlist[i]);
			    }else {
			    	tfidflist[file_size][i] = 0;
		    	}
		    }
		    file_size++;
	    }
	    double[][] tfidflist1 = uniformizationTfIdf(tfidflist);
	    return tfidflist1;
	}
	
	/**
	 * tfidf归一化(min-max归一化)
	 * @param tfidflist
	 * @return
	 */
	public double[][] uniformizationTfIdf(double[][] tfidflist){
		for(int i=0;i<tfidflist.length;i++) {
			//找出一行中的最大、最小值
			double min = Double.MAX_VALUE;
			double max = Double.MIN_VALUE;
			for(int j=0;j<tfidflist[0].length;j++) {
				if(tfidflist[i][j]<min) {
					min = tfidflist[i][j];
				}
				if(tfidflist[i][j]>max) {
					max = tfidflist[i][j];
				}
			}
			double max_min = max - min;
			
			//归一化计算
			for(int k=0;k<tfidflist[0].length;k++) {
				tfidflist[i][k] = (tfidflist[i][k] - min)/max_min;
			}
		}
		return tfidflist;
	}

}