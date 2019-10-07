package File_Operation;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import File_Operation.labels_filepaths;
import File_Operation.words_Segment;

/**
 * 
 * 统计所有词与其对应的文档频数  
 * 返回形式为Map<String,Map<String,Integer>> 即 特征词-<类别-文档频数>
 * @author pby
 * */
public class Bagofwords {
	
	
	/**
	 * 分词模式 默认flag=0 标准分词
	 * */
	private static Integer flag=0;
	/**
	 *遍历主路径
	 * */
	private static String maindir;
	
	/**
	 * 定义类别与对应文档数的集合
	 * */
	private static Map<String,Integer>Label_docNum;
	
	/**
	 *定义类别与对应文档路径集合 
	 * */
	private static Map<String,Set<String>>label_docPathset;
	
	

	/**
	 * 用于后续卡方特征计算
	 * 提取计算文档内频度Alpha
	 * 存放 特征词-<包含类别-类别下文本内tf词频总和>
	 * 
	 * */
	private static Map<String,Map<String,Double>>BagOfWordFre=new HashMap<String,Map<String,Double>>();
	
	/**
	 * 存放 特征词-<类别-文档频数>
	 * */
	private static Map<String,Map<String,Integer>>Bag2words=new HashMap<String,Map<String,Integer>>();
	
	/**
	 *设置遍历主路径
	 *
	 * */
	private void setmaindir(String path) {
		this.maindir=path;
	}
	
	private static void Initial() {
		labels_filepaths lf=new labels_filepaths();
		lf.setflag(1);
		Label_docNum=lf.Get(maindir);
		lf.setflag(0);
		label_docPathset=lf.Get(maindir);
	}
	
	
	/**
	 * 
	 * 统计所有词与其对应的文档频数 
	 * @param  Trainpath 目标路径
	 * @param flagSet 分词模式 默认flag=0 标准分词 flag=1 为关键词提取（默认每篇文档提取3个关键词）
	 * 返回形式为Map<String,Map<String,Integer>> 即 特征词-<类别-文档频数>
	 * 
	 * */
	public static Map<String,Map<String,Integer>>Get(String Trainpath,Integer flagSet) throws FileNotFoundException, IOException{
		flag=flagSet;
		return Get(Trainpath);
	}
	
	/**
	 * 
	 * 统计所有词与其对应的文档频数  
	 * 返回形式为Map<String,Map<String,Integer>> 即 特征词-<类别-文档频数>
	 * 
	 * */
	public static Map<String,Map<String,Integer>>Get(String Trainpath) throws FileNotFoundException, IOException{
		
		maindir=Trainpath;
		Initial();
		
		
		Set<String>labels=label_docPathset.keySet();
		for(String label:labels) {
			Set<String>Pathset=label_docPathset.get(label);
			for(String path:Pathset) {
				words_Segment ws=new words_Segment();
				ws.setflag(flag);//标准分词
				ws.setobjdir(path);
				
				//得到分词tf集合
				Map <String,Double>tf2word=ws.wordsSegment();
				//对得到的分词集合生成一个迭代器
				Iterator<String> itr=tf2word.keySet().iterator();
				while(itr.hasNext()) {
					String Objword=itr.next();
					//插入一个函数统计词频tf总和
					BagOfWordFreOperation(Objword,label,tf2word);
					if(Bag2words.containsKey(Objword)&&Bag2words.get(Objword).containsKey(label)) {
						Integer update=Bag2words.get(Objword).get(label)+1;
						Bag2words.get(Objword).put(label, update);
					}
					if(Bag2words.containsKey(Objword)&&!Bag2words.get(Objword).containsKey(label)) {
						Integer update=1;
						Bag2words.get(Objword).put(label, update);
					}
					if(!Bag2words.containsKey(Objword)) {
						Integer update=1;
						Map<String,Integer>add=new HashMap<String,Integer>();
						add.put(label, update);
						Bag2words.put(Objword,add);
					}
					
				}
				
				
				
			}
		}
		
		return Bag2words;
	}
	
	
	/**
	 * 返回目标路径下的总文档数
	 * */
	public static Integer GetNumOfDocs() {
		Integer res=0;
		for(Iterator<String> itr=Label_docNum.keySet().iterator();itr.hasNext();) {
			String lb=itr.next();
			res+=Label_docNum.get(lb);
		}
		return res;
		
	}
	/**
	 * 返回目标路径下的总文档数(单独调用)
	 * @param Trainpath 目标路径
	 * @return res 目标路径下的总文档数 Integer类型
	 * */
	public static Integer GetNumOfDocs(String Trainpath) {
		maindir=Trainpath;
		Initial();
		return GetNumOfDocs();
		
	}
	
	/**
	 * 用于返回类别与其对应类别下的文档总数
	 * 
	 * */
	public static Map<String,Integer> GetLabel_docNum() {
		return Label_docNum;
		
	}
	
	
	/**
	 * 返回文档内频度alpha集合
	 * @return BagOfWordFre
	 * 
	 * */
	public static Map<String,Map<String,Double>> GetBagOfWordFre() {
		return BagOfWordFre;
		
	}
	
	
	/**
	 * 获取文档内频率度alpha集合
	 * @param Objword 目标特征词
	 * @param label 对应标签
	 * @param tf2word 特征词所属当前文档的tf集合
	 * 
	 * */
	
	private static void BagOfWordFreOperation(String Objword,String label,Map <String,Double>tf2word) {
		if(BagOfWordFre.containsKey(Objword)&&BagOfWordFre.get(Objword).containsKey(label)) {
			Double update=BagOfWordFre.get(Objword).get(label)+tf2word.get(Objword);
			BagOfWordFre.get(Objword).put(label, update);
			
		}
		if(BagOfWordFre.containsKey(Objword)&&!BagOfWordFre.get(Objword).containsKey(label)) {
			Double update=tf2word.get(Objword);
			BagOfWordFre.get(Objword).put(label, update);
		}
		if(!BagOfWordFre.containsKey(Objword)) {
			Double update=tf2word.get(Objword);
			Map<String,Double>add=new HashMap<String,Double>();
			add.put(label, update);
			BagOfWordFre.put(Objword,add);
			
		}
		
	}
	
	
	
	
	

}
