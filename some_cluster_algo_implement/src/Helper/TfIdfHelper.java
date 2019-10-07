package Helper;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
/**
 * 接收一个HashMap<File,HashMap<String,Integer>>，
 * 可计算出该HashMap中所有词的TfIdf权重值，并返回
 * HashMap<File,HashMap<String,Double>>。
 * 其中包括词频DF筛选和单词贡献度TC筛选。
 * @author pjl
 *
 */
public class TfIdfHelper {
	/**
	 * 所有训练集分词的map
	 */
	HashMap<File,HashMap<String,Integer>> wordsMap = new HashMap<File,HashMap<String,Integer>>();
	/**
	 * wordsMap对应的tf-idf频率
	 */
	HashMap<File,HashMap<String,Double>> tfIdfMap = new HashMap<File,HashMap<String,Double>>();
	/**
	 * 存放DF值的map
	 */
	public HashMap<String,Double> DF = new HashMap<String,Double>();
	
//	HashMap<String,Double> a_b = new HashMap<String,Double>();
	
	public TfIdfHelper(HashMap<File,HashMap<String,Integer>> wordsMap) { //,HashMap<String,Double> a_b)
		this.wordsMap = wordsMap;
//		this.a_b = a_b;
	}
	
	/**
	 * 计算单词的tf
	 * @param item
	 * @param article
	 * @throws
	 */
	
	private double getTf(String item,HashMap<String,Integer> article) {
		int count = article.get(item); //item这个词的出现次数
		int sum = 0; //所有词总共出现的次数
		Iterator<String> iterator = article.keySet().iterator();
		while(iterator.hasNext()) {
			String itemName = iterator.next();
			sum += article.get(itemName);
		}
		double result = (double)((double)count/(double)sum);
		return result;
	}
	
	/**
	 * 计算文档的idf
	 * @param item
	 * @return
	 */
	private double getIdf(String item) {
		Iterator<File> fIterator = wordsMap.keySet().iterator();
		int count = 0;
		int sum = wordsMap.size();
		while(fIterator.hasNext()) {
			File file = fIterator.next();
			HashMap<String,Integer> itemMap = wordsMap.get(file);
			if(itemMap.containsKey(item)) {
				count++;
			}
		}
		return Math.log(((double)sum/(double)count));
	}
	
	/**
	 * 计算单词的tf-idf
	 * @param item
	 * @param article
	 * @return
	 */
	private double getTfIdf(String item,HashMap<String,Integer> article) {
		double tf = getTf(item,article);
		double idf = getIdf(item);
		return tf*idf;
	}
	
	/**
	 * 对所有单词进行tfidf计算并返回计算结果
	 * @return
	 */
	
	public HashMap<File,HashMap<String,Double>> calculate(){
		Iterator<File> fIterator = wordsMap.keySet().iterator();
//		HashMap<String,Double> DF = new HashMap<String,Double>();
		DF = DFMap();
		while(fIterator.hasNext()) {
			File file = fIterator.next();
			//原来的每篇文章的分词
			HashMap<String,Integer> article = wordsMap.get(file);
			//计算过tf-idf后的分词
			HashMap<String,Double> tempMap = new HashMap<String,Double>();
			Iterator<String> itemIterator = article.keySet().iterator();
			while(itemIterator.hasNext()) {
				String item = itemIterator.next();
				if(DF.containsKey(item)) {
					double tfidf = getTfIdf(item,article);
					tempMap.put(item,tfidf);
				}
//				double tfidf = getTfIdf(item,article);
//				tempMap.put(item,tfidf);
			}
			tfIdfMap.put(file,tempMap);
		}
		return this.tfIdfMap;
	}
	
	/**
	 * 得到单个特征的DF值
	 * @param item
	 * @return
	 */
	private double getDF(String item1) {
		Iterator<File> fIterator = wordsMap.keySet().iterator();
		int count = 0;
		int sum = wordsMap.size();
		while(fIterator.hasNext()) {
			File file = fIterator.next();
			HashMap<String,Integer> itemMap = wordsMap.get(file);
			if(itemMap.containsKey(item1)) {
				count++;
			}
		}
		return (double)count/sum;
	}
	
	/**
	 * 计算出每个词语的DF值，并过滤DF值过高和过低的特征，返回DFMap
	 * @return
	 */
	public HashMap<String,Double> DFMap(){
		
		HashMap<String,Double> DF = new HashMap<String,Double>();
		Iterator<File> fIterator = wordsMap.keySet().iterator();
		while(fIterator.hasNext()) {
			File file = fIterator.next();
			//原来的每篇文章的分词
			HashMap<String,Integer> article1 = wordsMap.get(file);
			//计算过tf-idf后的分词
			Iterator<String> itemIterator = article1.keySet().iterator();
			while(itemIterator.hasNext()) {
				String item1 = itemIterator.next();
				double Df = getDF(item1);
//				double W = Df*a_b.get(item1);
//				if((!DF.containsKey(item1))&&W>0.01) { //0.03    DF*词性权值因子*词长权值因子过滤方法
//					DF.put(item1,Df);
//				}
//				if((!DF.containsKey(item1))&&Df>0.04&&Df<0.8) { //0.03     只有DF的过滤方法  <0.86
				if((!DF.containsKey(item1))&&Df<0.9) { //0.03     只有DF的过滤方法  <0.86
					DF.put(item1,Df);
				}
			}
		}
		return DF;
	}
	
	/**
	 * 计算特征的贡献度TC
	 * @return
	 */
	public HashMap<String,Double> getTC(HashMap<File,HashMap<String,Double>> tfIdfMap,
			HashMap<String,Double> DF){
		HashMap<String,Double> tc = new HashMap<String,Double>();
		Iterator<String> dfiterator = DF.keySet().iterator();
		Iterator<File> tcsiterator = tfIdfMap.keySet().iterator();
		String[] tclist = new String[DF.size()];
		int tci = 0;
		while(dfiterator.hasNext()) {
			String tf = dfiterator.next();
			tclist[tci] = tf;
			tci++;
		}
		int file_size = 0;
		double tclist1[][] = new double[tfIdfMap.size()][tclist.length];
		while(tcsiterator.hasNext()) {
			File file = tcsiterator.next();
			HashMap<String,Double> item = tfIdfMap.get(file);
			for(int i=0;i<tclist.length;i++) {
				if(item.containsKey(tclist[i])) {
					tclist1[file_size][i] = item.get(tclist[i]);
				}else {
					tclist1[file_size][i] = 0;
				}
			}
			file_size++;
		}
		for(int i=0;i<(tci-1);i++) {   //tclist.length
			double sum = 0;
			for(int j=0,k=tfIdfMap.size()-1;j<k;j++) {
				for(int z=j+1;z<k+1;z++) {
					sum +=(tclist1[j][i]*tclist1[z][i]);
				}
			}
			if(sum>0.1) {     // 0.004 错误率0.37799 240维     0.005 错误率0.36842 224维    0.006  0.02  0.0.099
				tc.put(tclist[i], sum);
			}
//			tc.put(tclist[i], sum);
		}
		return tc;
	}

}