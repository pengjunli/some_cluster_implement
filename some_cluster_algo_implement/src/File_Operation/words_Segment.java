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
 * �ִ�
 * @author pby
 * */
public class words_Segment {
	
	private String objdir;//Ŀ���ı�·��
	//public boolean Fre=false;//�շ���Ҫ���� ���������Ӧ��Ƶ
	private static Integer flag=0;//�ִ�ģʽѡ��(Ĭ����0)
	private static Integer Num2word=3;//��ģʽflag=1ʱ��ȡ�Ĺؼ�������(Ĭ����3)
	
	public static  String stopWordTable = "stopwords.txt";
	
	
	//��ز�����ֵ
	
	/**
	 * ����Ŀ���ļ�·��
	 * @param path �������ı�·��
	 * 
	 * */
	public void setobjdir(String path) {
		this.objdir=path;
	}
	
	/**
	 * ���÷ִ�ģʽ
	 * @param flag flag=0(Ĭ��)��׼�ִ�/ flag=1�ؼ��ֳ�ȡģʽ
	 * 
	 * */
	public void setflag(Integer flag) {
		this.flag=flag;
	}
	
	/**
	 * ���ùؼ��ʳ�ȡģʽ�Ĺؼ��ʳ�ȡ����
	 * @param Num2word
	 * */
	public void setNum2word(Integer Num2word) {
		this.Num2word=Num2word;
	}
	

	
	/**
	 *@Description: ����Ϊ�����ı�·�������ص����ı���׼�ִ����ݣ��������ã�
	 *@param ���ִ��ı�·��
	 *@param �ִ�ģʽ flag=0(Ĭ��)��׼�ִ� flag=1�ؼ��ʳ�ȡ
	 */
	public Map <String,Double> wordsSegment(String txtPath,Integer flag) throws FileNotFoundException, IOException{
		setobjdir(txtPath);
		setflag(flag);
		return wordsSegment();
		
	}
	/**
	 *@Description: ����Ϊ�����ı�·�������ص����ı���׼�ִ����ݣ��������ã�
	 *@param ���ִ��ı�·��
	 */
	public Map <String,Double> wordsSegment(String txtPath) throws FileNotFoundException, IOException{
		setobjdir(txtPath);
		return wordsSegment();
		
	}
	/**
	 *@Description: ����Ϊ�����ı�·�������ص����ı��ִ����ݣ�NLPTokenizer��
	 * @param: Fre
	 * @return: �ִʽ��ӳ�伯�ϣ����������Ӧtfֵ��
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * */
	public Map <String,Double> wordsSegment() throws FileNotFoundException, IOException {
		String content=readFile(objdir);
		
		//���طִʼ���
		List<Term> SegR = HanLP.segment(content);//��׼nlp�ִ�
		
		List<String>keyWordSegR=HanLP.extractKeyword(content, Num2word);//��ȡ�ؼ���
		
		//�õ�ͣ�ôʼ���
		HashSet<String> stopWordSet=getStopWset();
		
		//��ʼ����׼�ִʷ��ؽ��
		Map <String,Double>wordsFre=new HashMap<String,Double>();
		
		
		
		//��ʼ���ܴ�Ƶ��
		Double WordsTotal=0.0;
		
		
		//��ʼ����׼�ִʼ��ϵ�����
		Iterator<Term> lex = SegR.iterator();
		while(lex.hasNext()) {
			Term word=lex.next();
			String word_Str=word.word;//��ȡ����
			//Nature word_Nat=word.nature;//��ȡ����
			
			if (stopWordSet.contains(word_Str)||word_Str.length()<2) {
				continue;
			}
			if (!Regex(word_Str)) {
				continue;
			}
			//�ܴ�Ƶ��1����
			WordsTotal+=1.0;
			
			//�ж�ӳ�伯�����Ƿ�����ôʣ�������1������������1��
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
	 * @Description: ��ȡ�ļ�ת����string 
	 * @param: file ����ȡ�ı�·��
	 * @return �ı���ȡ����
	 */
	private static String readFile(String filePath) throws FileNotFoundException, IOException {
		StringBuffer sb = new StringBuffer();
		InputStreamReader is = new InputStreamReader(new FileInputStream(filePath), "UTF-8");
		BufferedReader br = new BufferedReader(is);
		//���ж�ȡ
		String line = br.readLine();
		while (line != null) {
			sb.append(line).append("\r\n");
			line = br.readLine();
		}
		br.close();
		return sb.toString();
	}
	
	/**
	 * ����ͣ�ôʱ�·������һ��HashSet����
	 * @return ͣ�ôʼ���
	 * @throws IOException 
	 * 
	 * */
	private static HashSet<String> getStopWset() throws IOException{
		// ����ͣ�ô��ļ�
		BufferedReader StopWordFileBr = new BufferedReader(new InputStreamReader(new FileInputStream(new File(stopWordTable)),"UTF-8"));
		// �������ͣ�ôʵļ���
		HashSet<String> stopWordSet = new HashSet<String>();
		// ���绯ͣ�ôʼ�
		String stopWord = null;
		for (;(stopWord = StopWordFileBr.readLine())!= null;) {
			stopWordSet.add(stopWord);
			}
		return stopWordSet;
		
	}
	
	/**
	 * ������ȡ
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
