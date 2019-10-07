package File_Operation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;

/**
 * 分词
 * @author pby
 * */
public class words_Segment {
	
	private String objdir;//目标文本路径
	//public boolean Fre=false;//收否需要返回 特征词与对应词频
	private static Integer flag=0;//分词模式选择(默认是0)
	private static Integer Num2word=3;//在模式flag=1时抽取的关键词数量(默认是3)
	
	public static  String stopWordTable = "stopwords.txt";
	
	
	//相关参数赋值
	
	/**
	 * 设置目标文件路径
	 * @param path 待处理文本路径
	 * 
	 * */
	public void setobjdir(String path) {
		this.objdir=path;
	}
	
	/**
	 * 设置分词模式
	 * @param flag flag=0(默认)标准分词/ flag=1关键字抽取模式
	 * 
	 * */
	public void setflag(Integer flag) {
		this.flag=flag;
	}
	
	/**
	 * 设置关键词抽取模式的关键词抽取数量
	 * @param Num2word
	 * */
	public void setNum2word(Integer Num2word) {
		this.Num2word=Num2word;
	}
	

	
	/**
	 *@Description: 输入为单个文本路径，返回单个文本标准分词内容（单独调用）
	 *@param 待分词文本路径
	 *@param 分词模式 flag=0(默认)标准分词 flag=1关键词抽取
	 */
	public Map <String,Double> wordsSegment(String txtPath,Integer flag) throws FileNotFoundException, IOException{
		setobjdir(txtPath);
		setflag(flag);
		return wordsSegment();
		
	}
	/**
	 *@Description: 输入为单个文本路径，返回单个文本标准分词内容（单独调用）
	 *@param 待分词文本路径
	 */
	public Map <String,Double> wordsSegment(String txtPath) throws FileNotFoundException, IOException{
		setobjdir(txtPath);
		return wordsSegment();
		
	}
	/**
	 *@Description: 输入为单个文本路径，返回单个文本分词内容（NLPTokenizer）
	 * @param: Fre
	 * @return: 分词结果映射集合（包含词与对应tf值）
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * */
	public Map <String,Double> wordsSegment() throws FileNotFoundException, IOException {
		String content=readFile(objdir);
		
		//返回分词集合
		List<Term> SegR = HanLP.segment(content);//标准nlp分词
		
		List<String>keyWordSegR=HanLP.extractKeyword(content, Num2word);//提取关键词
		
		//得到停用词集合
		HashSet<String> stopWordSet=getStopWset();
		
		//初始化标准分词返回结果
		Map <String,Double>wordsFre=new HashMap<String,Double>();
		
		
		
		//初始化总词频数
		Double WordsTotal=0.0;
		
		
		//初始化标准分词集合迭代器
		Iterator<Term> lex = SegR.iterator();
		while(lex.hasNext()) {
			Term word=lex.next();
			String word_Str=word.word;//获取词语
			//Nature word_Nat=word.nature;//获取词性
			
			if (stopWordSet.contains(word_Str)||word_Str.length()<2) {
				continue;
			}
			if (!Regex(word_Str)) {
				continue;
			}
			//总词频加1操作
			WordsTotal+=1.0;
			
			//判断映射集合中是否包含该词（包含加1操作，否则置1）
			if(wordsFre.containsKey(word_Str)) {
				Double num2word=wordsFre.get(word_Str)+1.0;
				wordsFre.put(word_Str, num2word);
				}else {
				wordsFre.put(word_Str,1.0);
				}
			}
		
		
		for(String key: wordsFre.keySet()) {
			Double tf2word=wordsFre.get(key)/WordsTotal;
			wordsFre.put(key, tf2word);
		}
		if(flag==0) {
			return wordsFre;
			}
		Map <String,Double>KeywordsFre=new HashMap<String,Double>();
		for(String KeyW:keyWordSegR) {
			if(wordsFre.containsKey(KeyW)) {
				KeywordsFre.put(KeyW, wordsFre.get(KeyW));
			}else {
				KeywordsFre.put(KeyW, 0.0);
			}
			
		}
		return KeywordsFre;
		
		
		
		
	
	}
	
	
	/**
	 * 
	 * @Title: readFile 
	 * @Description: 读取文件转化成string 
	 * @param: file 待读取文本路径
	 * @return 文本读取内容
	 */
	private static String readFile(String filePath) throws FileNotFoundException, IOException {
		StringBuffer sb = new StringBuffer();
		InputStreamReader is = new InputStreamReader(new FileInputStream(filePath), "UTF-8");
		BufferedReader br = new BufferedReader(is);
		//按行读取
		String line = br.readLine();
		while (line != null) {
			sb.append(line).append("\r\n");
			line = br.readLine();
		}
		br.close();
		return sb.toString();
	}
	
	/**
	 * 根据停用词表路径返回一个HashSet集合
	 * @return 停用词集合
	 * @throws IOException 
	 * 
	 * */
	private static HashSet<String> getStopWset() throws IOException{
		// 读入停用词文件
		BufferedReader StopWordFileBr = new BufferedReader(new InputStreamReader(new FileInputStream(new File(stopWordTable)),"UTF-8"));
		// 用来存放停用词的集合
		HashSet<String> stopWordSet = new HashSet<String>();
		// 初如化停用词集
		String stopWord = null;
		for (;(stopWord = StopWordFileBr.readLine())!= null;) {
			stopWordSet.add(stopWord);
			}
		return stopWordSet;
		
	}
	
	/**
	 * 正则化提取
	 * 
	 */

	private static boolean Regex(String word) {
		String Regex = "[^a-zA-Z0-9]{1,}";
		if (word.matches(Regex)) {
			return true;
		} else {
			return false;
		}

	}
	

}
