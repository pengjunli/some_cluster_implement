package Word2Vec;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.deeplearning4j.models.word2vec.Word2Vec;

public class sumWordVector {
	
	public static File[] filelist = null;
	
	/**
	 * 获得文本向量
	 * @param articlewordsMap
	 * @param DF
	 * @param wordMap
	 * @param modelsize
	 * @return
	 */
	public static double[][] getFileVector(HashMap<File,HashMap<String,Integer>> articlewordsMap,
			HashMap<String,Double> DF,Word2Vec vec,int modelsize){
		filelist = new File[articlewordsMap.size()];
		double[][] filevector = new double[articlewordsMap.size()][modelsize];
		
		Iterator<File> fileiterator = articlewordsMap.keySet().iterator();
		int index = 0;
		while(fileiterator.hasNext()) {
			File file = fileiterator.next();
			filelist[index] = file;
			HashMap<String,Integer> words = articlewordsMap.get(file);
			Iterator<String> worditerator = words.keySet().iterator();
			double[] onefile = new double[modelsize];
			int count = 0;
			while(worditerator.hasNext()) {
				String str = worditerator.next();
				if(DF.containsKey(str)) {
					double[] oneword = vec.getWordVector(str);
					onefile = sum(onefile,oneword);
					count++;
				}
			}
			for(int i=0;i<onefile.length;i++) {
				onefile[i] /= count;
			}
//			filevector[index] = onefile;
			for(int i=0;i<onefile.length;i++) {
				filevector[index][i] = onefile[i];  //保持精度不变的情况下把float转换为double
			}
			index++;
		}
		return filevector;
	}
	
	public File[] getFileList() {
		return filelist;
	}
	
	/**
	 * 同一文本中的向量相加
	 * @param center
	 * @param fs
	 * @return
	 */
	private static double[] sum(double[] center, double[] fs) {
		// TODO Auto-generated method stub

		if (center == null && fs == null) {
			return null;
		}

		if (fs == null) {
			return center;
		}

		if (center == null) {
			return fs;
		}

		for (int i = 0; i < fs.length; i++) {
			center[i] += fs[i];
		}

		return center;
	}
	
	
	/**
	 * 获得文本向量
	 * @param articlewordsMap
	 * @param DF
	 * @param wordMap
	 * @param modelsize
	 * @return
	 */
//	@SuppressWarnings("null")
	public static double[][] getFileVector(HashMap<File,HashMap<String,Integer>> articlewordsMap,
			Word2Vec vec,int modelsize){
		filelist = new File[articlewordsMap.size()];
		double[][] filevector = new double[articlewordsMap.size()][modelsize];
		
		Iterator<File> fileiterator = articlewordsMap.keySet().iterator();
		int index = 0;
		while(fileiterator.hasNext()) {
			File file = fileiterator.next();
			filelist[index] = file;
			HashMap<String,Integer> words = articlewordsMap.get(file);
			Iterator<String> worditerator = words.keySet().iterator();
			double[] onefile = null;
			int count = 0;
			while(worditerator.hasNext()) {
				String str = worditerator.next();
					double[] oneword = vec.getWordVector(str);
					onefile = sum(onefile,oneword);
					count++;
			}
			if(onefile==null) {
				for(int i=0;i<modelsize;i++) {
					filevector[index][i] = 0;  //保持精度不变的情况下把float转换为double
				}
				index++;
				continue;
			}
				
			for(int i=0;i<onefile.length;i++) {
				onefile[i] /= count;
			}
//			filevector[index] = onefile;
			for(int i=0;i<onefile.length;i++) {
				filevector[index][i] = onefile[i];  //保持精度不变的情况下把float转换为double
			}
			index++;
		}
		return filevector;
	}

}
