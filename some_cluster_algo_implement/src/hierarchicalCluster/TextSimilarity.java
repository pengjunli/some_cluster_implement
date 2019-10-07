package hierarchicalCluster;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

//import com.ansj.vec.LearnMoreVec;

//import com.cadal.focusOrder.basic.ListString;
//import com.cadal.focusOrder.dataProcess.*;
/**
 * 多种相似度计算方法
 * @author PJL
 *
 */
public class TextSimilarity {
	public static List<double[]> getVector(List<HashMap<String,Double>> tfidf){
		List<double[]> vector = new ArrayList<>();
//		List<List<Double>> vector = new ArrayList<>();
		HashMap<String,Double> lineMap = new HashMap<String, Double>();
		List<String> wordsBag = getWordsList(tfidf);
//		double[] wordlist = new double[wordsBag.size()];
//		List<Double> wordlist = new ArrayList<>();
		double[] wordlist;
		for(int i=0;i<tfidf.size();i++) {
			lineMap = tfidf.get(i);
//			wordlist.clear();
			wordlist = new double[wordsBag.size()];
//			List<Double> wordlist = new ArrayList<>();
//			wordlist.clear();
			for(int j=0;j<wordsBag.size();j++) {
				if(lineMap.containsKey(wordsBag.get(j))) {
					wordlist[j] = lineMap.get(wordsBag.get(j));
				}else {
					wordlist[j] = 0.0;
				}
//				if(lineMap.containsKey(wordsBag.get(j))) {
//					wordlist.add(lineMap.get(wordsBag.get(j)));
//				}else {
//					wordlist.add(0.0);
//				}
			}
			vector.add(wordlist);

//			lineMap.clear();
		}
		return vector;
	}
	
	/**
	 * 得到词语列表
	 * @param tfidf
	 * @return
	 */
	public static List<String> getWordsList(List<HashMap<String,Double>> tfidf){
		HashSet<String> wordsMap = new HashSet<>();
		HashMap<String,Double> linewords = new HashMap<String,Double>();
		for(int i=0;i<tfidf.size();i++) {
			linewords = tfidf.get(i);
			Iterator<String> itera = linewords.keySet().iterator();
			while(itera.hasNext()) {
				String str = itera.next();
				wordsMap.add(str);
			}
//			linewords.clear();
		}
		List<String> wordsBag = getWordsBag(wordsMap);
		return wordsBag;
	}
	
	public static List<String> getWordsBag(HashSet<String> wordsMap){
		List<String> wordsBag = new ArrayList<>();
		Iterator<String> itera = wordsMap.iterator();
		while(itera.hasNext()) {
			String str = itera.next();
			wordsBag.add(str);
		}
		return wordsBag;
	}
	 
	/**
	 * �������ƶȾ���
	 * @param 
	 * @return
	 */
	public static double[][] CalSimMatrix(List<double[]> vectorlist) {
		double[][] sim = new double[vectorlist.size()][vectorlist.size()]; 
//		double[] vec1 = new double[vectorlist.get(0).length];
//		double[] vec2 = new double[vectorlist.get(0).length];
		double[] vec1 = null;
		double[] vec2 = null;
		for (int i = 0; i < vectorlist.size(); i++) {
			vec1 = vectorlist.get(i);
			for (int j = i + 1; j < vectorlist.size(); j++) {
				vec2 = vectorlist.get(j);
				sim[i][j] = getSimilarDegree0(vec1, vec2);
				sim[j][i] = sim[i][j];
			}
		}
		
		return sim;
	}
	
//	public static double[][] CalSimMatrix1(List<double[]> vectorlist) {
//		List<double[]> sim = vectorlist;
//		double[] vec1 = null;
//		double[] vec2 = null;
//		for (int i = 0; i < vectorlist.size(); i++) {
//			vec1 = vectorlist.get(i);
//			for (int j = i + 1; j < vectorlist.size(); j++) {
//				vec2 = vectorlist.get(j);
//				sim[i][j] = getSimilarDegree0(vec1, vec2);
//				sim[j][i] = sim[i][j];
//			}
//		}
//		
//		return sim;
//	}
//	public static double[][] CalSimMatrix(List<List<Double>> vectorlist) {
//		double[][] sim = new double[vectorlist.size()][vectorlist.size()];
//		List<Double> vec1 = new ArrayList<>();
//		List<Double> vec2 = new ArrayList<>();
//		for (int i = 0; i < vectorlist.size(); i++) {
//			vec1 = vectorlist.get(i);
//			for (int j = i + 1; j < vectorlist.size(); j++) {
//				vec2 = vectorlist.get(j);
//				sim[i][j] = getSimilarDegree1(vec1, vec2);
//				sim[j][i] = sim[i][j];
//			}
//		}
//		
//		return sim;
//	}
	/**
	 * 求解余弦夹角
	 * @param vec1
	 * @param vec2
	 * @return
	 */
	private static double getSimilarDegree0(double[] vec1, double[] vec2) {
		// 
		double vector1Modulo = 0.0;// the first vec
		double vector2Modulo = 0.0;// the second vec
		double vectorProduct = 0.0; // 
		for (int i = 0; i < vec1.length; i++) {
			vector1Modulo += vec1[i] * vec1[i];
			vector2Modulo += vec2[i] * vec2[i];

			vectorProduct += vec1[i] * vec2[i];
		}
		vector1Modulo = Math.sqrt(vector1Modulo);
		vector2Modulo = Math.sqrt(vector2Modulo);
		
		double result = vectorProduct / (vector1Modulo * vector2Modulo);
		if (result > 1) result = 1.0;

		// return the cos-value of two vec
		return result;
	}
	
	/**
	 * 求解欧式距离
	 * @param vec1
	 * @param vec2
	 * @return
	 */
	private static double getSimilarDegree1(double[] vec1, double[] vec2) {
		double vectorModulo = 0.0;
		double temp = 0.0;
		for(int i=0;i<vec1.length;i++) {
			temp = vec1[i] - vec2[i];
			temp = temp * temp;
			vectorModulo += temp; 
		}
		vectorModulo = Math.sqrt(vectorModulo);
		return vectorModulo;
	}
	
	/**
	 * 求解欧式距离
	 * @param vec1
	 * @param vec2
	 * @return
	 */
//	public static double getSimilarDegree1(List<Double> vec1, List<Double> vec2) {
//		double vectorModulo = 0.0;
//		double temp = 0.0;
//		for(int i=0;i<vec1.size();i++) {
//			temp = vec1.get(i) - vec2.get(i);
//			temp = temp * temp;
//			vectorModulo += temp; 
//		}
//		vectorModulo = Math.sqrt(vectorModulo);
	
	
	
	
//		return vectorModulo;
//	}
}

