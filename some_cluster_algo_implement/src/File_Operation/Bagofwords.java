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
 * ͳ�����д������Ӧ���ĵ�Ƶ��  
 * ������ʽΪMap<String,Map<String,Integer>> �� ������-<���-�ĵ�Ƶ��>
 * @author pby
 * */
public class Bagofwords {
	
	
	/**
	 * �ִ�ģʽ Ĭ��flag=0 ��׼�ִ�
	 * */
	private static Integer flag=0;
	/**
	 *������·��
	 * */
	private static String maindir;
	
	/**
	 * ����������Ӧ�ĵ����ļ���
	 * */
	private static Map<String,Integer>Label_docNum;
	
	/**
	 *����������Ӧ�ĵ�·������ 
	 * */
	private static Map<String,Set<String>>label_docPathset;
	
	

	/**
	 * ���ں���������������
	 * ��ȡ�����ĵ���Ƶ��Alpha
	 * ��� ������-<�������-������ı���tf��Ƶ�ܺ�>
	 * 
	 * */
	private static Map<String,Map<String,Double>>BagOfWordFre=new HashMap<String,Map<String,Double>>();
	
	/**
	 * ��� ������-<���-�ĵ�Ƶ��>
	 * */
	private static Map<String,Map<String,Integer>>Bag2words=new HashMap<String,Map<String,Integer>>();
	
	/**
	 *���ñ�����·��
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
	 * ͳ�����д������Ӧ���ĵ�Ƶ�� 
	 * @param  Trainpath Ŀ��·��
	 * @param flagSet �ִ�ģʽ Ĭ��flag=0 ��׼�ִ� flag=1 Ϊ�ؼ�����ȡ��Ĭ��ÿƪ�ĵ���ȡ3���ؼ��ʣ�
	 * ������ʽΪMap<String,Map<String,Integer>> �� ������-<���-�ĵ�Ƶ��>
	 * 
	 * */
	public static Map<String,Map<String,Integer>>Get(String Trainpath,Integer flagSet) throws FileNotFoundException, IOException{
		flag=flagSet;
		return Get(Trainpath);
	}
	
	/**
	 * 
	 * ͳ�����д������Ӧ���ĵ�Ƶ��  
	 * ������ʽΪMap<String,Map<String,Integer>> �� ������-<���-�ĵ�Ƶ��>
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
				ws.setflag(flag);//��׼�ִ�
				ws.setobjdir(path);
				
				//�õ��ִ�tf����
				Map <String,Double>tf2word=ws.wordsSegment();
				//�Եõ��ķִʼ�������һ��������
				Iterator<String> itr=tf2word.keySet().iterator();
				while(itr.hasNext()) {
					String Objword=itr.next();
					//����һ������ͳ�ƴ�Ƶtf�ܺ�
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
	 * ����Ŀ��·���µ����ĵ���
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
	 * ����Ŀ��·���µ����ĵ���(��������)
	 * @param Trainpath Ŀ��·��
	 * @return res Ŀ��·���µ����ĵ��� Integer����
	 * */
	public static Integer GetNumOfDocs(String Trainpath) {
		maindir=Trainpath;
		Initial();
		return GetNumOfDocs();
		
	}
	
	/**
	 * ���ڷ�����������Ӧ����µ��ĵ�����
	 * 
	 * */
	public static Map<String,Integer> GetLabel_docNum() {
		return Label_docNum;
		
	}
	
	
	/**
	 * �����ĵ���Ƶ��alpha����
	 * @return BagOfWordFre
	 * 
	 * */
	public static Map<String,Map<String,Double>> GetBagOfWordFre() {
		return BagOfWordFre;
		
	}
	
	
	/**
	 * ��ȡ�ĵ���Ƶ�ʶ�alpha����
	 * @param Objword Ŀ��������
	 * @param label ��Ӧ��ǩ
	 * @param tf2word ������������ǰ�ĵ���tf����
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
