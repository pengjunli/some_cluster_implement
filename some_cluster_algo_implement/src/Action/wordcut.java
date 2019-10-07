package Action;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import WordCut.dowordcut;
import Helper.FileHelper;
/**
 * 如果是要对一个txt或doc文件，或对一个文件夹中的所有文件进行分词，可直接调用Action包中wordcut，
 * 其中可得到两个HashMap：
 * HashMap<File,HashMap<String,Integer>>  articleWordsMap、 HashMap<String,Integer>   wordsDict
 * articleWordsMap中每个文件对应一个HashMap<String,Integer>，
 * String为这个文件中的词语，integer为该词语对应的词频。
 * wordsDict为单词词典。
 * @author pjl
 *
 */
public class wordcut extends dowordcut {
	public wordcut() {
		
	}
	/**
	 * 存放分词后所有文章的单词
	 */
	public static HashMap<File,HashMap<String,Integer>> articleWordsMap = new HashMap<File,HashMap<String,Integer>>();
	/**
	 * articleWordMap对应tf-idf格式
	 */
	private HashMap<File,HashMap<String,Double>> tfidfMap = new HashMap<File,HashMap<String,Double>>();
	
	/**
	 * 字典的所有词项和label
	 */
	public HashMap<String,Integer> wordsDict = new HashMap<String,Integer>();   //private
	public HashMap<String,Integer> classLabel = new HashMap<String,Integer>();  //private
	public HashMap<String,Double> ab = new HashMap<String,Double>();
	/**
	 * 从文件中加载单词字典
	 */
	public void loadWordsDict(File file) {  //private
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String temp = null;
			while((temp = reader.readLine())!=null) {
				String[] str = temp.split(" ");
				wordsDict.put(str[1], Integer.parseInt(str[0]));  //Integer.parseInt为将字符串转换为int类
			}
			}catch(Exception e) {
				e.printStackTrace();
		}
	}
	
	public HashMap<File,String> readFile(File[] files) throws Exception {  //private
		int curlndex = 0;
		HashMap<File,String> articles = new HashMap<File,String>();
		for(File file:files) {
			if(file.isDirectory()) {
				File[] file1 = file.listFiles();
				for(File file2:file1) {
					String content = FileHelper.readTxtOrDoc(file2);
					articles.put(file2, content);
					curlndex++;
				}
			}else {
				String content = FileHelper.readTxtOrDoc(file);
				articles.put(file, content);
				curlndex++;
			}
		}
		return articles;
	}
	
	/**
	 *通过文件名获得类标号  如：政治_1.txt 对于它的类别为“政治”
	 * @param className
	 * @return
	 */
	public int getClassLabel(String className) {  //private
		String[] arr = className.split("_");
		if(classLabel.containsKey(arr[0])) {
			return classLabel.get(arr[0]);
		}else {
			return -1;
		}
	}
	/**
	 * 通过文件的父目录获得类标号  如：政治/1.txt 对于的类别为“政治”
	 * @param className
	 * @return
	 */
	public int getClassLabel(File file) {  //private
		String className = file.getParentFile().getName(); //得到file文件父目录的名字
		if(classLabel.containsKey(className)) {
			return classLabel.get(className);
		}else {
			return -1;
		}
	}
	
	/**
	 * 对所有文章进行分词处理
	 * @param files
	 * @return Exception
	 */
	public  void cutWord(File file) throws Exception{
		if(file.isDirectory()) {
			File[] files = file.listFiles();
			HashMap<File,String> articles = this.readFile(files);
			Iterator<File> artIterator = articles.keySet().iterator();
//			File file1 = new File("wordDict/Dictionary.txt");
//			//将特征词写入Dictionary.txt
//			FileOutputStream out = new FileOutputStream(file1,true);
//			PrintStream writer = new PrintStream(out);
			int index = 1;
			while(artIterator.hasNext()) {
				File file1 = artIterator.next();
				String name = file1.getName();
				String content = articles.get(file1); //得到file所隐射的strin
				HashMap<String,Integer> artWords = this.docutword(content);//docutword将文章分词，提取出有用的词，并统计词频
				this.articleWordsMap.put(file1, artWords);
				
				Set<String> set = artWords.keySet(); //获取artWords集合中的key对象集合
				Iterator<String> it = set.iterator();
				while(it.hasNext()) {
					String str = (String)it.next();
					if(!wordsDict.containsKey(str)) {
						wordsDict.put(str, index);       //将特征词写入存放特征词的HashMap
//						writer.println(index+" "+str);  //将特征词写入Dictionary.txt
						index++;
					}
				}
				System.out.println(name);
			}
//			out.close();
		}
		
	}

	public static void main(String[] args) throws Exception{
//		File[] files = new File[]{
//				new File("article/政治法律/1.txt"),
//				new File("article/政治法律/2.txt"),
//				new File("article/艺术/1.txt")
//				};
//		run(files,null);
		dowordcut model = new dowordcut();
		model.docutword("我是一个中国人，我爱打篮球，我喜欢后仰跳投舟曲县城火箭(21-12)三连胜终止。先发五虎只有1人得分上双，钱德勒-帕森斯15分，德怀特-霍华德9分9个篮板，詹姆斯[微博]-哈登8分，林书豪[微博]6分3个篮板。替补出场的奥姆里-卡斯比15分，阿隆-布鲁克斯[微博]17分。");
	}
	
}
